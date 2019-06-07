package controller;

import exceptions.*;
import exceptions.CardAlreadyOwnedException;
import exceptions.CountriesNotAdjacentException;
import exceptions.GameNotFoundException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchCountryException;
import exceptions.NoSuchPlayerException;
import exceptions.NotEnoughUnitsException;
import model.*;
import model.AttackResult;
import model.Continent;
import model.Country;
import model.Game;
import model.Player;
import persistence.FileWriter;

import java.io.IOException;
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
    void awardUnits(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }
        int units = 3;
        units += this.addUnitsByCountries(game, player);
        units += this.addUnitsByContinents(game, player);
        player.setUnitsToPlace(units);
    }

    void changeUnits(Game game, Player player, int unitChange) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }
        player.setUnitsToPlace(player.getUnitsToPlace() + unitChange);
    }

    /**
     * Checks if a player has all units of a country and adds bonus units accordingly
     *
     * @param player
     * @return number of units the player gets as bonus
     */
    int addUnitsByContinents(Game game, Player player) throws NoSuchPlayerException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
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
            throw new NoSuchCountryException(attackingCountry);
        }
        if(!game.getCountries().containsValue(defendingCountry)) {
            throw new NoSuchCountryException(defendingCountry);
        }
        //check if attack is valid based on units
        if (attackingUnits > attackingCountry.getUnits() || attackingCountry.getUnits() == 1 ) {
            throw new NotEnoughUnitsException(attackingCountry);
        }

        if (defendingUnits > defendingCountry.getUnits()) {
            throw new NotEnoughUnitsException(defendingCountry);
        }

        //check if attack is valid based on adjacency
        boolean adjacent = false;
        for (Country c : attackingCountry.getNeighbors()) {
            if (c.getId() == defendingCountry.getId()) {
                adjacent = true;
            }
        }

        if (!adjacent) {
            throw new CountriesNotAdjacentException(attackingCountry,defendingCountry);
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
            WorldController.getInstance().changeCountryOwnership(game, attackingCountry, defendingCountry);
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
            throw new NoSuchPlayerException(player);
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
     * @param
     * @return
     */
    void useCards(Game game, Player player, int oneStarCards, int twoStarCards) throws NoSuchPlayerException, NoSuchCardException{
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        // TODO: FIX THIS TOGETHER WITH THE WHOLE CARD SYSTEM
//        boolean validCards = true;
//        for (int i = 0; i < cardsToBeUsed; i++) {
//            Card card = player.getCards().get(i);
//            if(!game.getCards().contains(card)) {
//                validCards = false;
//            }
//        }
//        if(!validCards) {
//            throw new NoSuchCardException("Card does not exist");
//        }
//
        int oneStarCardsRemoved = 0;
        int twoStarCardsRemoved = 0;

        while (oneStarCardsRemoved < oneStarCards){
            for (int i = 0; i < player.getCards().size(); i++){
                if (player.getCards().get(i).getValue() == 1) {
                    player.getCards().remove(i);
                    oneStarCardsRemoved++;
                }
                break;
            }
        }

        while (twoStarCardsRemoved < twoStarCards){
            for (int i = 0; i < player.getCards().size(); i++){
                if (player.getCards().get(i).getValue() == 2) {
                    player.getCards().remove(i);
                    twoStarCardsRemoved++;
                }
                break;
            }
        }

        int bonusUnits = getUnitsForValue(oneStarCards + twoStarCards * 2);
        player.setUnitsToPlace(player.getUnitsToPlace() + bonusUnits);
    }

    int getUnitsForValue(int value){
        int unitsToGet;
        switch (value){
            case 1:
                unitsToGet = 1;
                break;
            case 2:
                unitsToGet = 2;
                break;
            case 3:
                unitsToGet = 4;
                break;
            case 4:
                unitsToGet = 7;
                break;
            case 5:
                unitsToGet = 10;
                break;
            case 6:
                unitsToGet = 13;
                break;
            case 7:
                unitsToGet = 17;
                break;
            case 8:
                unitsToGet = 21;
                break;
            case 9:
                unitsToGet = 25;
                break;
            case 10:
                unitsToGet = 30;
                break;
            default:
                unitsToGet = 0;
        }
        return  unitsToGet;
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
            throw new NoSuchPlayerException(player);
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
    //FIXME: IOException muss da besser gehandlet werden. Macht semantisch keinen Sinn.
    boolean checkWinCondition(Game game, Player player) throws NoSuchPlayerException, IOException, GameNotFoundException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }
        boolean winConditionMet = player.getMission().isAccomplished(player, game);
        if(winConditionMet) {
            FileWriter.getInstance().removeGame(game);
            return winConditionMet;
        }
        return player.getMission().isAccomplished(player, game);
    }

    // TODO: should be refactored and checkWinCondition should be a separate method
    void postTurnCheck(Game game, Player player) throws NoSuchPlayerException, IOException, GameNotFoundException {
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        boolean winCondition = checkWinCondition(game, player);
        if (winCondition) {
            // TODO: REFACTOR THIS TO SOMEWHERE ELSE
            System.out.println(player + "HAS WON THE GAME");
            return;
        }

        if (player.hasConqueredCountry()) {
            try {
                try {
                    // FIXME: WRONG
                    GameController.getInstance().addCard(game.getId(), player);
                } catch (NoSuchPlayerException | NoSuchCardException | CardAlreadyOwnedException e) {
                    e.printStackTrace();
                }
                player.setHasConqueredCountry(false);
            } catch (GameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
