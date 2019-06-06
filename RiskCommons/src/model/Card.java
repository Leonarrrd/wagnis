package model;

/*
* Class for Card objects
 */

public class Card {
    private int id;
    private int value;

    public Card(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

}


