package interfaces;

import exceptions.*;
import model.*;

import java.io.IOException;
import java.util.*;

public interface IGameController {


        Game getGameById(UUID id) throws GameNotFoundException;

        public Game initNewGame() throws IOException, InvalidFormattedDataException;

        public Game loadGame(UUID gameId) throws IOException, GameNotFoundException, InvalidFormattedDataException;

        public void addPlayer(UUID gameId, String playerName) throws GameNotFoundException, MaximumNumberOfPlayersReachedException, InvalidPlayerNameException;


        public void addCountry(UUID gameId, String countryAsString, Player player) throws GameNotFoundException, CountryAlreadyOccupiedException, NoSuchCountryException;

        public void changeUnits(UUID gameId, Country country, int units) throws GameNotFoundException, NoSuchCountryException;


        public void changeFrozenUnits(UUID gameId, Country country, int frozenUnits) throws GameNotFoundException, NoSuchCountryException;

        public void assignCountries(UUID gameId) throws GameNotFoundException, CountriesAlreadyAssignedException;

        public void assignMissions(UUID gameId) throws GameNotFoundException, MaximumNumberOfPlayersReachedException;

        public int assignUnits(UUID gameId) throws GameNotFoundException, InvalidNumberOfPlayersException;
        public void addCard(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;

        public void awardUnits(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;

        public void changeUnitsToPlace(UUID gameId, Player player, int unitChange) throws GameNotFoundException, NoSuchPlayerException;

        public void useCards(UUID gameId, Player player, int oneStarCards, int twoStarCards) throws GameNotFoundException, NoSuchCardException, NoSuchPlayerException;

        public Map<String, Country> getCountriesAttackCanBeLaunchedFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

        public Map<String, Country> getCountriesWithMoreThanOneUnit(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException;


        public boolean hasCountryToMoveFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

        public boolean hasCountryToAttackFrom(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, NoSuchCountryException;

        public Map<String, Country> getHostileNeighbors(UUID gameId, Country country) throws GameNotFoundException, NoSuchCountryException;


        public AttackResult fight(UUID gameId, Country attackingCountry, Country defendingCountry, int attackingUnits, int defendingUnits) throws NotEnoughUnitsException, CountriesNotAdjacentException, GameNotFoundException, NoSuchCountryException;

        public void moveUnits(UUID gameId, Country srcCountry, Country destCountry, int amount) throws GameNotFoundException, NotEnoughUnitsException, CountryNotOwnedException, NoSuchCountryException, CountriesNotAdjacentException;


        public boolean checkWinCondidtion(UUID gameId, Player player) throws GameNotFoundException, NoSuchPlayerException, IOException;

        public void setTurn(UUID gameId) throws GameNotFoundException;

        public void switchTurns(UUID gameId) throws GameNotFoundException, NoSuchPlayerException, NoSuchCardException, CardAlreadyOwnedException;
        public void updatePlayerGraphMap(UUID gameId, Player p) throws GameNotFoundException, NoSuchPlayerException;

        public void postTurnCheck(UUID gameId, Player player) throws GameNotFoundException, IOException, NoSuchPlayerException;
        public boolean isConnected(Country srcCountry, Country destCountry) ;
        public void saveGame(UUID gameId) throws GameNotFoundException, IOException, DuplicateGameIdException ;

        public void removeGame(UUID gameId) throws GameNotFoundException, IOException;
        public List<String> loadAvailableGameIds() throws IOException;

}
