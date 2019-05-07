package controller;

import model.AttackResult;
import exceptions.*;
import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicController {

    private static LogicController instance;

    private LogicController() {
    }

    public static LogicController getInstance() {
        if (instance == null) {
            instance = new LogicController();
        }
        return instance;
    }

       /**
     * Calculates the amount of units the player is eligible to get for this turn
     * Get units for: default, total number of countries, full continents, Cards played
     *
     * @return number of units the player can place this turn
     */
    int awardUnits(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        int units = 3;
        units += this.addUnitsByCountries(game, player);
        units += this.addUnitsByContinents(game, player);
        return units;
    }

    /**
     * Checks if a player has all units of a country and adds bonus units accordingly
     *
     * @param player
     * @return number of units the player gets as bonus
     */
    int addUnitsByContinents(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist");
        }
        int bonusUnits = 0;
        for (Continent continent : game.getContinents()) {
            if (continent.isOwnedByPlayer(player)) {
                bonusUnits += continent.getBonusUnits();
            }
        }
        return bonusUnits;
    }

    /**
     * Carries out one round of an attack
     * Rolls the dices and deducts units from the countries depending on the roll's outcome
     * The method does NOT handle invalid parameters. The instance that calls this function must ensure the parameters are valid
     * The method is rather long and might be rewritten in a more elegant way
     * Probably better to split this method in 2-3 smaller methods, with dice rolls, unit deducts, and checking for a winner separate
     *
     * @param attackingCountry
     * @param defendingCountry
     * @param attackingUnits
     * @param defendingUnits
     * @return Attack-Result object
     */
    AttackResult fight(Game game, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, NoSuchCountryException {
        if(!game.getCountries().containsValue(attackingCountry)) {
            throw new NoSuchCountryException("Country " + attackingCountry + " does not exist.");
        }
        if(!game.getCountries().containsValue(defendingCountry)) {
            throw new NoSuchCountryException("Country " + defendingCountry + " does not exist.");
        }
        //check if attack is valid based on units
        if (attackingUnits > attackingCountry.getUnits()) {
            throw new NotEnoughUnitsException("Country " + attackingCountry + " does not hold enough units to attack with " + attackingUnits + " units.");
        }

        if (defendingUnits > defendingCountry.getUnits()) {
            throw new NotEnoughUnitsException("Country " + defendingCountry + " does not hold enough units to defend with " + defendingUnits + " units.");
        }

        //check if attack is valid based on adjacency
        boolean adjacent = false;
        for (Country c : attackingCountry.getNeighbors()) {
            if (c.getId() == defendingCountry.getId()) {
                adjacent = true;
            }
        }

        if (!adjacent) {
            throw new CountriesNotAdjacentException(attackingCountry.getName() + " is ont adjacent with " + defendingCountry);
        }

        List<Integer> attackerDices = rollDices(attackingUnits);
        List<Integer> defenderDices = rollDices(defendingUnits);

        // determine how many dices will be compared
        int turns = (attackingUnits < defendingUnits) ? attackingUnits : defendingUnits;

        // count dying attackers so that they won't move into defeated country
        int remainingAttackingUnits = attackingUnits;

        for (int i = 0; i < turns; i++) {
            if (attackerDices.get(i) > defenderDices.get(i)) {
                defendingCountry.setUnits(defendingCountry.getUnits() - 1);
            } else if (attackerDices.get(i) <= defenderDices.get(i)) {
                attackingCountry.setUnits(attackingCountry.getUnits() - 1);
                remainingAttackingUnits--;
            }
        }

        if (attackingCountry.getUnits() == 1) {
            return new AttackResult(defendingCountry, attackerDices, defenderDices);
        } else if (defendingCountry.getUnits() == 0) { // attacker won
            attackingCountry.getOwner().setHasConqueredCountry(true);
            WorldController.getInstance().changeCountryOwnership(game, attackingCountry, defendingCountry, remainingAttackingUnits);
            attackingCountry.setUnits(attackingCountry.getUnits() - remainingAttackingUnits);
            defendingCountry.setUnits(defendingCountry.getUnits() + remainingAttackingUnits);
            return new AttackResult(attackingCountry, attackerDices, defenderDices);
        } else {
            return new AttackResult(null, attackerDices, defenderDices);
        }
    }


    /**
     * Calculates how many units the player is eligible to get for the total amount of countries he's occupying
     *
     * @param player
     * @return number of units the player gets as bonus
     */
    int addUnitsByCountries(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        int playerCountries = player.getCountries().size();
        int bonusUnits = (playerCountries - 9) / 3;
        return (bonusUnits > 0) ? bonusUnits : 0;
    }

    /**
     * TODO: OVERSIMPLIFIED AT THE MOMENT
     * Removes the cards the player wants to use from his hand
     * Calculates and returns the number of units the player is awarded for the value of his cards
     *
     * @param player
     * @param cardsToBeUsed
     * @return
     */
    int useCards(Game game, Player player, int cardsToBeUsed) throws NoSuchPlayerException, NoSuchCardException{
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist");
        }

        List<Card> usableCards = new ArrayList();
        boolean validCards = true;
        for (int i = 0; i < cardsToBeUsed; i++) {
            Card card = player.getCards().get(i);
            usableCards.add(card);
            if(!game.getCardDeck().contains(card)) {
                validCards = false;
            }
        }
        if(!validCards) {
            throw new NoSuchCardException("Card does not exist");
        }

        for (int i = 0; i < cardsToBeUsed; i++) {
            player.getCards().remove(0);
        }
        return cardsToBeUsed * cardsToBeUsed;
    }

    /**
     * Returns true if player has a country that has: a) more than one unit, b) at least one hostile neighbor
     *
     * @param player
     * @return
     */
    boolean hasCountryToAttackFrom(Game game, Player player) throws NoSuchPlayerException, NoSuchCountryException {

        for (Player p : game.getPlayers()) {
            GraphController.getInstance().updatePlayerGraphMap(game, p);
        }
        boolean hasCountryToAttackFrom = false;
        for (Country country : WorldController.getInstance().getCountriesWithMoreThanOneUnit(game, player).values()) {
            if (!WorldController.getInstance().getHostileNeighbors(game, country).isEmpty()) {
                hasCountryToAttackFrom = true;
            }
        }
        return hasCountryToAttackFrom;
    }

    /**
     * Returns true if player has a country that has: a) more than one unit, b) is connected to at least one friendly country
     *
     * @param player
     * @return
     */
    boolean hasCountryToMoveFrom(Game game, Player player) throws NoSuchPlayerException, NoSuchCountryException {
        for (Player p : game.getPlayers()) {
            GraphController.getInstance().updatePlayerGraphMap(game, p);
        }
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        boolean hasCountryToMoveFrom = false;
        for (Country country : WorldController.getInstance().getCountriesWithMoreThanOneUnit(game, player).values()) {
            if (!WorldController.getInstance().getAlliedNeighbors(game, country).isEmpty()) {
                hasCountryToMoveFrom = true;
            }
        }
        return hasCountryToMoveFrom;
    }


    /**
     * Also responsible for sorting the list
     *
     * @param numberOfDices to be rolled
     * @return
     */
    List<Integer> rollDices(int numberOfDices) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < numberOfDices; i++) {
            result.add((int) (Math.floor(Math.random() * 6 + 1)));
        }

        Collections.sort(result);
        Collections.reverse(result);

        return result;
    }

    /**
     * AT THE MOMENT FOR PRESENTATION PURPOSES ONLY
     * At the moment, the only win condition is to conquer all countries
     * Will be updated to player missions once they are implemented
     *
     * @param player
     * @return
     */
    boolean checkWinCondition(Game game, Player player) throws NoSuchPlayerException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        //FIXME: enter real win condition
        return player.getMission().isAccomplished(player);
    }
}
