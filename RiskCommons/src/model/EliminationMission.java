package model;

import datastructures.Color;

import java.util.List;

public class EliminationMission extends Mission {
    private Color targetColor;
    private List<Continent> continentsToConquer;

    public EliminationMission(int id, Color targetColor, List<Continent> continentsToConquer){
        super(id);
        this.targetColor = targetColor;
        this.continentsToConquer = continentsToConquer;
    }

    /**
     * Checks if the winning condition of the mission is met
     * First checks if a player with the target color is in the game
     * If so, true will be returned if he has no countries (is eliminated from the game)
     * Otherwise,  true will be returned if all member continents are owned by the player
     * Otherwise,  false will be returned
     * @param owner
     * @param game
     * @return
     */
    public boolean isAccomplished(Player owner, Game game){
        Player target = null;
        for (Player player : game.getPlayers()){
            if (player.getColor().equals(targetColor)){
                target = player;
            }
        }

        boolean isAccomplished = true;

        if (target != null){
            if (!target.getCountries().isEmpty()){
                isAccomplished = false;
            }
        } else {
            for (Continent continent : continentsToConquer) {
                if (!continent.isOwnedByPlayer(owner)) {
                    isAccomplished = false;
                }
            }
        }
        return isAccomplished;
    }

    public String getMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("Eliminate the ");
        sb.append(targetColor.toString().toLowerCase());
        sb.append(" Player. If no player has that color, conquer ");
        sb.append(continentsToConquer.get(0).getName());
        sb.append(" and ");
        sb.append(continentsToConquer.get(1).getName());
        sb.append(".");
        return sb.toString();
    }
}
