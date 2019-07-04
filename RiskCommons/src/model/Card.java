package model;

import datastructures.CardSymbol;

import java.io.Serializable;

/*
 * Class for Card objects
 * holds the symbol and an id
 */
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;


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


