package model;

import datastructures.Phase;

public class Turn {

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
