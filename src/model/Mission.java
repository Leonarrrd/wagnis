package model;

import java.util.List;

public abstract class Mission {

    private int id;
    private String message;
    private Player owner;

    public Mission(int id, String message){
        this.message = message;
    }

    public void setPlayer(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Checks if the win condition described by the message is met
     * @return
     */
    public abstract boolean isAccomplished();
}
