package model;

import java.io.Serializable;
import java.util.List;

public class ContinentMission extends Mission implements Serializable {

    private List<Continent> continentsToConquer;

    public ContinentMission(int id, List<Continent> continentsToConquer){
        super(id);
        this.continentsToConquer = continentsToConquer;
    }

    public boolean isAccomplished(Player owner, Game game){
        boolean isAccomplished = true;
        for (Continent continent : continentsToConquer){
            if (!continent.isOwnedByPlayer(owner)){
                isAccomplished = false;
            }
        }
        return isAccomplished;
    }

    public String getMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("Conquer ");
        sb.append(continentsToConquer.get(0).getName());
        sb.append(" and ");
        sb.append(continentsToConquer.get(1).getName());
        sb.append(".");
        return sb.toString();
    }
}
