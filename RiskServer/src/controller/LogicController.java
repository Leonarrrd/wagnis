package controller;

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
import server.SocketGameManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static helper.Events.*;

/**
 * Controller that handles Logic Operations
 */
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

    /**
     * @see interfaces.IGameController#changeUnits(UUID, Country, int)
     */
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
     * @see interfaces.IGameController#fight(UUID, Country, Country, int, int)
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
     * Gets the trade bonus type by type card
     */
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
     * @see interfaces.IGameController#useCards(UUID, Player, int, int, int)
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
     * Method that checks the player's wining condition
     * @param player the player whose win condition needs to be checked
     * @return true if game is won, otherwise false
     * @throws GameNotFoundException if the game with the provided game Id could not be found
     * @throws NoSuchPlayerException if the player whose win condition needs to be checked does not exist
     * @throws IOException
     */
    boolean checkWinCondition(Game game, Player player) throws NoSuchPlayerException, IOException, GameNotFoundException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        return player.getMission().isAccomplished(player, game);
    }

    /**
     * @see interfaces.IGameController#postPhaseCheck(UUID, Turn)
     */
    void postPhaseCheck(Game game, Turn turn) throws NoSuchPlayerException, IOException, GameNotFoundException, NoSuchCardException, CardAlreadyOwnedException {
        Player currentPlayer = turn.getPlayer();
        Phase phase = turn.getPhase();
        if(!game.getPlayers().contains(currentPlayer)) {
            throw new NoSuchPlayerException(currentPlayer);
        }

        //check for winner
        for (Player player : game.getPlayers()) {
            if (checkWinCondition(game, player)){
                for (Socket socket : SocketGameManager.getInstance().getGameInitById(game.getId()).getSockets()) {
                    SocketGameManager.getInstance().getSocketServerIOThreadMap().get(socket).endGame(player.getName());
                }
                return;
            }
        }

        // update graphs aswell as check if player has been kicked out
        if (phase.equals(Phase.TRAIL_UNITS)) {
            Player playerToBeRemoved = null; // avoid ConcurrentModificationException
            for (Player p : game.getPlayers()) {
                GraphController.getInstance().updateGraph(game, p);
                if (p.getCountries().values().isEmpty()){
                    playerToBeRemoved = p;
                    break;
                }
            }
            if (playerToBeRemoved != null){
                game.getPlayers().remove(playerToBeRemoved);
                for (Socket socket : SocketGameManager.getInstance().getGameIdSocketMap().get(game.getId())){
                    String name = SocketGameManager.getInstance().getSocketPlayerNameMap().get(socket);
                    if (name.equals(playerToBeRemoved.getName())){
                        SocketGameManager.getInstance().getSocketServerIOThreadMap().get(socket).removePlayer();

                    }
                }

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
