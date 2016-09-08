package com.gluck.dto;

public class GameStateDto {

    private Long gameId;
    private Long player1;
    private Long player2;
    private String gameStatus;
    private Long nextPlayer;
    private Object config;

    public GameStateDto() {
    }

    public GameStateDto(Long gameId, Long player1, Long player2, String gameStatus, Long nextPlayer, Object config) {
        this();
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
        this.gameStatus = gameStatus;
        this.nextPlayer = nextPlayer;
        this.config = config;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getPlayer1() {
        return player1;
    }

    public void setPlayer1(Long player1) {
        this.player1 = player1;
    }

    public Long getPlayer2() {
        return player2;
    }

    public void setPlayer2(Long player2) {
        this.player2 = player2;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Long getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Long nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Long[][] config) {
        this.config = config;
    }

}
