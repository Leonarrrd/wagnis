package model;

import java.io.Serializable;
import java.util.List;

/**
 * AttackResult class
 * will be initialized when a fight is done
 */
public class AttackResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private List<Integer> attackerDices;
    private List<Integer> defenderDices;
    private Country winner;

    public AttackResult(Country winner, List<Integer> attackerDices, List<Integer> defenderDices) {
        this.attackerDices = attackerDices;
        this.defenderDices = defenderDices;
        this.winner = winner;
    }

    public List<Integer> getAttackerDices() {
        return attackerDices;
    }

    public List<Integer> getDefenderDices() {
        return defenderDices;
    }

    public Country getWinner() {
        return winner;
    }
}
