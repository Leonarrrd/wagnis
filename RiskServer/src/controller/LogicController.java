package controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import datastructures.CardBonus;
import datastructures.Phase;
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
import server.SocketGameManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static helper.Events.END_GAME;
import static helper.Events.PLAYER_JOIN;

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
        player.setUnitsToPlace(player.getUnitsToPlace() + units);
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

    public CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards){
        if (infantryCards + cavalryCards + artilleryCards != 3){
            return null;
        }
        if (infantryCards == 3){
            return CardBonus.INFANTRY;
        }
        if (cavalryCards == 3){
            return CardBonus.CAVALRY;
        }
        if (artilleryCards == 3){
            return CardBonus.ARTILLERY;
        }
        if (infantryCards == 1 && cavalryCards == 1 && artilleryCards == 1){
            return CardBonus.MULTI;
        }
        return null;
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
    void useCards(Game game, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws NoSuchPlayerException, NoSuchCardException{
        if(!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        CardBonus bonusType = getTradeBonusType(infantryCards, cavalryCards, artilleryCards);
        List<Card> cardsToPutIntoDeck = player.removeCards(bonusType);
        game.getCardDeck().addAll(cardsToPutIntoDeck);


        int bonusUnits = 0;
        switch (bonusType){
            case INFANTRY:
                bonusUnits = 2;
                break;
            case CAVALRY:
                bonusUnits = 3;
                break;
            case ARTILLERY:
                bonusUnits = 4;
                break;
            case MULTI:
                bonusUnits = 5;
                break;
        }
        player.setUnitsToPlace(player.getUnitsToPlace() + bonusUnits);
    }

    /**
     * Returns true if player has a country that has: a) more than one unit, b) at least one hostile neighbor
     *
     * @param player
     * @return
     */
    boolean hasCountryToAttackFrom(Game game, Player player) throws NoSuchPlayerException, NoSuchCountryException {

        for (Player p : game.getPlayers()) {
            GraphController.getInstance().updateGraph(game, p);
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
            GraphController.getInstance().updateGraph(game, p);
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

    public boolean hasCountryToMoveTo(Game game, Country country) throws GameNotFoundException {
        List<String> countriesInGraph = country.getOwner().getCountryGraph().evaluateCountriesAllowedToMoveTo(country.getName());
        return countriesInGraph.size() == 1;
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
     * checks if the win condition of param player is met
     * if true: removes the savegame from file
     * @param player
     * @return
     */
    //FIXME: IOException muss da besser gehandlet werden. Macht semantisch keinen Sinn.
    boolean checkWinCondition(Game game, Player player) throws NoSuchPlayerException, IOException, GameNotFoundException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

//        boolean winConditionMet = player.getMission().isAccomplished(player, game);
//        if(winConditionMet) {
//            FileWriter.getInstance().removeGame(game);
//            return winConditionMet;
//        }
        return player.getMission().isAccomplished(player, game);
    }

    /**
     * checks that get performed every time a phase switch happens
     * updates country graphs on every phase
     * awards player units before using cards
     * awards player a card at the end of his turn (if country has been conquered)
     * also checks if a player if out of the game and if true removes him
     * @param game
     * @param turn
     * @throws NoSuchPlayerException
     * @throws IOException
     * @throws GameNotFoundException
     * @throws NoSuchCardException
     * @throws CardAlreadyOwnedException
     */
    void postPhaseCheck(Game game, Turn turn) throws NoSuchPlayerException, IOException, GameNotFoundException, NoSuchCardException, CardAlreadyOwnedException {
        Player currentPlayer = turn.getPlayer();
        Phase phase = turn.getPhase();
        if(!game.getPlayers().contains(currentPlayer)) {
            throw new NoSuchPlayerException(currentPlayer);
        }

        //check for winner
        for (Player p : game.getPlayers()) {
            if (checkWinCondition(game, p)){
                for (Socket s : SocketGameManager.getInstance().getGameInitById(game.getId()).getSockets()) {
                    ObjectOutputStream sOos = SocketGameManager.getInstance().getSocketObjectOutputStreamMap().get(s);
                    sOos.writeUTF(END_GAME + "," + game.getId() + "," + p.getName());
                    sOos.flush();
                }
            }
        }

        // update graphs aswell as check if player has been kicked out
        if (phase.equals(Phase.TRAIL_UNITS)) {
            Player toBeRemoved = null; // avoid ConcurrentModificationException
            for (Player p : game.getPlayers()) {
                GraphController.getInstance().updateGraph(game, p);
                if (p.getCountries().values().isEmpty()){
                    toBeRemoved = p;
                    break;
                }
            }
            if (toBeRemoved != null){
                game.getPlayers().remove(toBeRemoved);
            }

        }

        if (phase.equals(Phase.PLACE_UNITS)) {
            awardUnits(game, turn.getPlayer());
        }
        if (phase.equals(Phase.MOVE)) {
            if (currentPlayer.hasConqueredCountry()) {
                    GameController.getInstance().addCardToPlayer(game.getId(), currentPlayer);
                    currentPlayer.setHasConqueredCountry(false);
            }
        }
    }
}
