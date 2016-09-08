package com.gluck.service;

import com.gluck.dto.GameStateDto;

public interface AppService {

    GameStateDto startGame(String player1, String player2);

    Long createPlayer(String name);

    void test();

    GameStateDto nextTurn(Long gameId, Long pl, int column);

}
