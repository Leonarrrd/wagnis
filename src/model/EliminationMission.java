package model;

public class EliminationMission extends Mission {
    private Player target;

    public EliminationMission(int id, Player target, String message){
        super(id, message);
        this.target = target;
    }

    public boolean isAccomplished(){
        return target.getCountries().isEmpty();
    }
}
