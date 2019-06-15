package model;

/*
* Class for Card objects
 */

import datastructures.CardSymbol;

import java.io.Serializable;

public class Card implements Serializable {
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


