package model;

import java.io.Serializable;
import java.util.List;

public class CountryMission extends Mission implements Serializable {

    private static final long serialVersionUID = 1L;


    private int countriesToConquer = 30;

    public CountryMission(int id, int countriesToConquer){
        super(id);
        this.countriesToConquer = countriesToConquer;
    }

    public boolean isAccomplished(Player owner, Game game){
        return owner.getCountries().size() >= countriesToConquer;
    }

    public String getMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("Conquer ");
        sb.append(countriesToConquer);
        sb.append(" Countries.");
        return sb.toString();
    }
}
