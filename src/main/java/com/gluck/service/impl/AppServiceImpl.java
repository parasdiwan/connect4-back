package com.gluck.service.impl;

import static com.gluck.model.GameStatus.END_DRAW;
import static com.gluck.model.GameStatus.END_WIN;
import static com.gluck.model.GameStatus.IN_PROGRESS;
import static com.gluck.service.impl.Utils.assertNotBlank;
import static com.gluck.service.impl.Utils.assertNotNull;
import static com.gluck.service.impl.Utils.assertTrue;
import static java.lang.Double.valueOf;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.String.format;

import java.awt.Dimension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gluck.dao.GameDao;
import com.gluck.dao.PlayerDao;
import com.gluck.dao.RedisDao;
import com.gluck.dto.GameStateDto;
import com.gluck.model.Game;
import com.gluck.model.GameState;
import com.gluck.model.GameStatus;
import com.gluck.model.Player;
import com.gluck.service.AppService;

@Service
public class AppServiceImpl implements AppService {

    private @Autowired PlayerDao playerDao;
    private @Autowired GameDao gameDao;
    private @Autowired RedisDao redisDao;

    @Override
    @Transactional
    public GameStateDto startGame(String player1, String player2) {
        assertNotBlank(player1, "Player 1 cannot be empty");
        assertNotBlank(player2, "Player 2 cannot be empty");
        Long p1Long = Long.parseLong(player1);
        Long p2Long = Long.parseLong(player2);
        Player p1 = playerDao.findById(p1Long);
        Player p2 = playerDao.findById(p2Long);
        assertNotNull(p1, "Player 1 does not exist");
        assertNotNull(p2, "Player 2 does not exist");

        Game game = new Game(p1, p2, IN_PROGRESS);
        Long gameId = gameDao.create(game);
        Long firstPlayer = getFirstPlayer(p1, p2).getId();
        redisDao.create(new GameState(gameId, new Dimension(8, 8), firstPlayer));
        return new GameStateDto(gameId, p1Long, p2Long, IN_PROGRESS.toString(), firstPlayer, null);
    }

    @Override
    @Transactional
    public Long createPlayer(String name) {
        assertNotBlank(name, "Player name cannot be blank");
        Player player = new Player();
        player.setName(name);
        player.setGamesPlayed(0);
        return playerDao.create(player);
    }

    @Override
    public void test() {
        redisDao.create(new GameState(1L, new Dimension(7, 7), 1L));
        redisDao.findById(1L);
    }

    @Override
    @Transactional
    public GameStateDto nextTurn(Long gameId, Long pl, int column) {

        assertNotNull(gameId, "Game Id cannot be blank");
        assertNotNull(pl, "Player id cannot be blank");
        assertNotNull(column, "Colmun cannot be blank");

        Game game = gameDao.findById(gameId);
        assertNotNull(game, "Game does not exist for id " + gameId);
        Player player = playerDao.findById(pl);
        assertNotNull(player, "Player does not exist for Id " + pl);

        assertTrue(game.getP1().getId().equals(pl) || game.getP2().getId().equals(pl),
                "Player " + pl + " does not have access to this game" + gameId);
        assertTrue(IN_PROGRESS.equals(game.getStatus()), format(game.getStatus().toString(), pl));
        Long otherPlayer = game.getP1().getId().equals(pl) ? game.getP2().getId() : game.getP1().getId();

        GameState gs = redisDao.findById(gameId);
        assertTrue(gs.getActivePlayer().equals(pl), "This is player " + pl + "'s turn");

        assertTrue(gs.isValidColumn(column), "Column value must be between 0 and " + gs.getSize().width);
        assertTrue(gs.isColumnFilled(column), "This column is already filled please choose another column");
        GameStatus gameStatus = updateGameConfig(gs, column, pl);

        switch (gameStatus) {
        case END_WIN:
            game.setWonBy(player);
        case END_DRAW:
            game.setStatus(gameStatus);
            gs.setActivePlayer(null);
            break;
        default:
            gs.setActivePlayer(otherPlayer);
        }
        redisDao.update(gs);
        return new GameStateDto(gameId, pl, otherPlayer, format(gameStatus.toString(), pl), otherPlayer,
                gs.getConfig());
    }

    private GameStatus updateGameConfig(GameState gs, int column, Long pl) {
        Long[][] gameConfig = gs.getConfig();
        int width = gs.getSize().width;
        int height = gs.getSize().height;
        int i = 0;
        while (i < height && gameConfig[i][column - 1] == null) {
            i++;
        }
        gameConfig[i - 1][column - 1] = pl;
        return checkGameStatus(pl, gameConfig, i - 1, column - 1, width, height);
    }

    private GameStatus checkGameStatus(Long player, Long[][] config, int position, int column, int width, int height) {
        // Checking vertically
        int count = 0;
        for (int i = position; i < height && i - position <= 3; i++) {
            if (player.equals(config[i][column])) {
                count++;
                if (count == 4) {
                    return END_WIN;
                }
            } else {
                count = 0;
            }
        }

        // Checking horizontally
        count = 0;
        int colStart = (column - 3 < 0) ? 0 : column - 3;
        int colEnd = column + 3 >= width ? width - 1 : column + 3;
        for(int j = colStart; j< colEnd; j++) {
            if(player.equals(config[position][j])) {
                count++;
                if (count == 4) {
                    return END_WIN;
                }
            } else {
                count = 0;
            }
        }

        // Checking diagonally
        int colDiffLeft = column - colStart;
        int colDiffRight = colEnd - column;
        int rowDiffUp = position - 3 < 0 ? position : 3;
        int rowDiffDown = position + 3 >= height ? (height - 1) - position : 3;
        int leftUp = colDiffLeft < rowDiffUp ? -colDiffLeft : -rowDiffUp;
        int leftDown = colDiffLeft < rowDiffDown ? -colDiffLeft : -rowDiffDown;
        int rightUp = colDiffRight < rowDiffUp ? colDiffRight : rowDiffUp;
        int rightDown = colDiffRight < rowDiffDown ? colDiffRight : rowDiffDown;

        count = 0;
        for(int m = leftDown; m < rightUp ; m++) {
            if (player.equals(config[position - m][column + m])) {
                count++;
                if (count == 4) {
                    return END_WIN;
                }
            } else {
                count = 0;
            }
        }

        for (int m = leftUp; m < rightDown; m++) {
            if (player.equals(config[position + m][column + m])) {
                count++;
                if (count == 4) {
                    return END_WIN;
                }
            } else {
                count = 0;
            }
        }

        // Check if all columns are filled
        for (int k = 0; k < width; k++) {
            if (config[0][k] == null) {
                return IN_PROGRESS;
            }
        }
        return END_DRAW;
    }

    private Player getFirstPlayer(Player p1, Player p2) {
        // Write whatever First Player logic
        Player selectedPlayer = round(valueOf(random())) == 1 ? p1 : p2;
        return selectedPlayer;
    }

}
