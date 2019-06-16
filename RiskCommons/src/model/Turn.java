package model;

import datastructures.Phase;

import java.io.Serializable;

public class Turn implements Serializable {

    private static final long serialVersionUID = 1L;


    private Player player;
    private Phase phase;

    public Turn(Player player, Phase phase) {
        this.player = player;
        this.phase = phase;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
