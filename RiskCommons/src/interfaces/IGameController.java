package interfaces;

import datastructures.CardBonus;
import exceptions.*;
import model.*;

import java.io.IOException;
import java.util.*;

public interface IGameController {


        Game getGameById(UUID id) throws GameNotFoundException;

        Game initNewGame() throws IOException, InvalidFormattedDataException ;


        Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException ;

        void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException;

        void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException;

        void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException;

        void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException;

        void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException;


        void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException;


        int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException;

        void addCardToPlayer(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException ;

        void addCardsToDeck(UUID gameId, List<Card> cards) throws GameNotFoundException;

        CardBonus getTradeBonusType(int infantryCards, int cavalryCards, int artilleryCards);


        void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;

        void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException;


        void useCards(UUID gameId, Player player, int infantryCards, int cavalryCards, int artilleryCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException;

        Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

        Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;


        boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;


        boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

        Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException;

        AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException;

        void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException;


        boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException;

        void setTurn(UUID gameId) throws GameNotFoundException;

        void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException, IOException;

        void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException;

        void postPhaseCheck(UUID gameId, Turn turn) throws GameNotFoundException, IOException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

        boolean isConnected(Country srcCountry, Country destCountry);

        void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException;

        void removeGame(UUID gameId) throws GameNotFoundException, IOException;

        List<String> loadAvailableGameIds() throws IOException;


}
