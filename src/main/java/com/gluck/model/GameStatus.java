package com.gluck.model;

public enum GameStatus {

    IN_PROGRESS("Game is in Progress"), END_DRAW("Game ended in a draw"), END_WIN("Game won by {0}"), INACTIVE(
            "Game is inactive");

    String desc;

    private GameStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }

}
