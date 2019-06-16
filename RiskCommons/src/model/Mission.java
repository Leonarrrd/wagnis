package model;

import java.io.Serializable;

public abstract class Mission implements Serializable {

    private static final long serialVersionUID = 1L;


    private int id;

    public Mission(int id){
        this.id = id;
    }


    public int getId(){
        return id;
    }

    /**
     * Builds and returns a description of how the mission can be accomplished
     * @return
     */
    public abstract String getMessage();

    /**
     * Checks if the win condition described by the message is met
     * @return
     */
    public abstract boolean isAccomplished(Player owner, Game game);
}
