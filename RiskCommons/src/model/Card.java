package model;

/*
* Class for Card objects
 */

import datastructures.CardSymbol;

public class Card {
    private int id;
    private CardSymbol symbol;


    public Card(int id, CardSymbol symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public CardSymbol getSymbol() {
        return symbol;
    }

    public int getId() {
        return id;
    }

}


