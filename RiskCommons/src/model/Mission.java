package model;

public abstract class Mission {

    private int id;
    private String message;

    public Mission(int id, String message){
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public int getId(){
        return id;
    }

    /**
     * Checks if the win condition described by the message is met
     * @return
     */
    public abstract boolean isAccomplished(Player owner);
}
