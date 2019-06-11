package view.gui.sockets;

import datastructures.CardBonus;
import exceptions.*;
import interfaces.IGameController;
import model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameControllerFassade implements IGameController {
    @Override
    public Game getGameById(UUID id) throws GameNotFoundException {
        return null;
    }

    @Override
    public Game initNewGame() throws IOException, InvalidFormattedDataException {
        return null;
    }

    @Override
    public Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException {
        return null;
    }

    @Override
    public void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException {

    }

    @Override
    public void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException {

    }

    @Override
    public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException {

    }

    @Override
    public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException {

    }

    @Override
    public void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException {

    }

    @Override
    public void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException {

    }

    @Override
    public int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException {
        return 0;
    }

    @Override
    public void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {

    }

    @Override
    public void addCardsToDeck(UUID gameId, List<Card> cards) {

    }

    @Override
    public CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards) {
        return null;
    }

    @Override
    public void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {

    }

    @Override
    public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException {

    }

    @Override
    public void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException {

    }

    @Override
    public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return null;
    }

    @Override
    public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException {
        return null;
    }

    @Override
    public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return false;
    }

    @Override
    public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException {
        return false;
    }

    @Override
    public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException {
        return null;
    }

    @Override
    public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException {
        return null;
    }

    @Override
    public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException {

    }

    @Override
    public boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException {
        return false;
    }

    @Override
    public void setTurn(UUID gameId) throws GameNotFoundException {

    }

    @Override
    public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException {

    }

    @Override
    public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException {

    }

    @Override
    public void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException {

    }

    @Override
    public boolean isConnected(Country srcCountry, Country destCountry) {
        return false;
    }

    @Override
    public void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException {

    }

    @Override
    public void removeGame(UUID gameId) throws GameNotFoundException, IOException {

    }

    @Override
    public List<String> loadAvailableGameIds() throws IOException {
        return null;
    }
}
