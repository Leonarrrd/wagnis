package model;

import java.util.List;

public class CountryMission extends Mission {

    private int countriesToConquer = 30;

    public CountryMission(int id, int countriesToConquer, String message){
        super(id, message);
        this.countriesToConquer = countriesToConquer;
    }

    public boolean isAccomplished(Player owner){
        return owner.getCountries().size() >= countriesToConquer;
    }
}
