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

//public class Card {
//    private Country country;
//    private int value;
//
//    public Card(Country country, int value) {
//        this.country = country;
//        this.value = value;
//    }
//
//    public void setCountry(Country country) {
//        this.country = country;
//    }
//
//    public Country getCountry() {
//        return country;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//}
