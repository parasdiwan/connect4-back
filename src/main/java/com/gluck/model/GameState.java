package com.gluck.model;

import java.awt.Dimension;
import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = -4172992110766741025L;
    private Long gameId;
    private Dimension size;
    private Long[][] config;
    private String status;
    private Long activePlayer;

    public GameState(Long gameId, Dimension size, Long activePlayer) {
        super();
        this.gameId = gameId;
        this.size = size;
        config = new Long[size.width][size.height];
        status = "In Progress";
        this.activePlayer = activePlayer;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public Long[][] getConfig() {
        return config;
    }

    public void setConfig(Long[][] config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Long activePlayer) {
        this.activePlayer = activePlayer;
    }

    public boolean isValidColumn(int column) {
        return column <= size.width && column > 0;
    }

    public boolean isColumnFilled(int column) {
        return config[0][column - 1] == null;
    }

    @Override
    public String toString() {
        return "GameState [gameId=" + gameId + ", size=" + size + ", status=" + status + ", activePlayer="
                + activePlayer + "]";
    }

}
