package model;

import java.util.List;

public class ConquerMission extends Mission {

    private List<Continent> continentsToConquer;

    public ConquerMission(int id, List<Continent> continentsToConquer, String message){
        super(id, message);
        this.continentsToConquer = continentsToConquer;
    }

    public boolean isAccomplished(){
        boolean isAccomplished = true;
        for (Continent continent : continentsToConquer){
            if (!continent.isOwnedByPlayer(this.getOwner())){
                isAccomplished = false;
            }
        }
        return isAccomplished;
    }
}
