//MARK: CUI is abandoned


//package view.cui;
//
//import controller.GraphController;
//import exceptions.*;
//import model.Country;
//import model.Game;
//import model.Player;
//import persistence.FileReader;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//
//public class PreGameCUI extends AbstractCUI {
//
//    @Override
//    public void run() {
//        boolean preGameDone = false;
//        System.out.println("Welcome to Risk!");
//
//        boolean savedGame = false;
//        try {
//            savedGame = checkForLoadingGame();
//        } catch (IOException | InvalidFormattedDataException | GameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (savedGame) {
//            new TurnCUI().run();
//        } else {
//            Game game = null;
//                game = null;
//
//            setGameId(game.getId());
//            System.out.println("Game with id " + gameId + " initialized.");
//            while (!preGameDone) {
//                System.out.println("You need at least 2 and at most 5 people to play.");
//                System.out.println("If you're done adding players, type \"done\". Otherwise, type in the players name.");
//                String ans = reader.nextLine();
//                try {
//                    if (!ans.equals("done")) {
//
//                        //gc.addPlayer(game.getId(), ans);
//
//                        System.out.println(ans + " has been added.");
//                        if (gc.getGameById(gameId).getPlayers().size() == 5) {
//                            System.out.println("Maximum number of players has been reached. Starting game...");
//                            preGameDone = true;
//                        }
//                    } else if (gc.getGameById(gameId).getPlayers().size() < 2) {
//                        System.out.println("Add more players.");
//                    } else {
//                        preGameDone = true;
//                    }
//                } catch (GameNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                gc.setTurn(gameId);
//                gc.assignMissions(gameId);
//            } catch (GameNotFoundException | MaximumNumberOfPlayersReachedException | NoSuchPlayerException e) {
//                e.printStackTrace();
//            }
//            countryAssignmentphase(gameId);
//        }
//    }
//
//    /**
//     * Only used in simple mode
//     * Prints each players countries after they've been assigned randomly
//     *
//     * @param gameId
//     */
//    private void countryAssignmentphase(UUID gameId) {
//        Game game = null;
//        try {
//            game = gc.getGameById(gameId);
//            gc.assignCountries(gameId);
//        } catch (GameNotFoundException | CountriesAlreadyAssignedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Countries have been assigned the following way:");
//        for (Country country : game.getCountries().values()) {
//            System.out.println(country + ": Units:" + country.getUnits() + " Owner: " + country.getOwner());
//        }
//
//        for (Player p : game.getPlayers()) {
//            try {
//                GraphController.getInstance().updatePlayerGraphMap(game, p);
//            } catch (NoSuchPlayerException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//
//    private boolean checkForLoadingGame() throws IOException, InvalidFormattedDataException, GameNotFoundException {
//        System.out.println("Do you want to load an existing game? (y/n) n - create new game");
//        String input = reader.nextLine();
//
//        if (input.equals("y")) {
//            System.out.println("Following games have been saved:");
//
//            List<String> availableGameIds = FileReader.getInstance().loadAvailableGameIds();
//            for (String s : availableGameIds) {
//                System.out.println(s);
//            }
//            //FIXME: Might want to map the gameIds to something that it's easier to type
//            //TODO: Might want to show more information on the games (e.g. date, game state?)
//            System.out.println("Which game do you want to load?");
//            //TODO: load game etc
//            input = reader.nextLine();
//            Game game = gc.loadGame(UUID.fromString(input));
//            System.out.println(game);
//            setGameId(game.getId());
//            return true;
//        } else if (input.equals("n")) {
//            return false;
//        }
//        System.out.println("Wrong input. Please type (y/n)");
//        return checkForLoadingGame();
//    }
//}
//
//    //might be useful later
//    /**
//     * NOT USED AT THE MOMENT
//     * Players get to put their units on the field one at a time until all units are used up
//     * Number of units each player can place is determined by number of players
//     *
//     * @param gameId
//     */
//    /*private void initialUnitPlacement(UUID gameId) {
//        int units = 0;
//        Game game = null;
//        Map<String, Country> unoccupiedCountries = null;
//        try {
//            game = gc.getGameById(gameId);
//            units = gc.assignUnits(gameId); // gets the number of units each player can place
//            unoccupiedCountries = gc.getUnoccupiedCountries(gameId);
//        } catch (InvalidNumberOfPlayersException | GameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println("Unit Placing Round starting...");
//        for (int i = 0; i < units; i++) {
//            for (Player player : game.getPlayers()) {
//                if (!unoccupiedCountries.isEmpty()) {
//                    String ans = "";
//                    while (!unoccupiedCountries.containsKey(ans)) {
//                        System.out.println(player.getName() + ", please select a country to occupy.");
//                        System.out.println("Possible options:" + unoccupiedCountries.keySet());
//                        ans = reader.nextLine();
//                        if (!unoccupiedCountries.containsKey(ans)) {
//                            checkForSpecialInput(ans, "Make sure the input matches the key exactly.", game.toString());
//                        }
//                    }
//                    try {
//                        gc.addCountry(gameId, ans, player);
//                        gc.changeUnits(gameId, player.getCountries().get(ans), 1);
//                        unoccupiedCountries.remove(ans);
//                    } catch (GameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    String ans = "";
//                    // Following 4 lines could be avoided if we store countries as Map<String, Country> in Player object
//                    List<String> playerCountries = new ArrayList<>();
//                    for (Country country : player.getCountries().values()) {
//                        playerCountries.add(country.getName());
//                    }
//                    while (!playerCountries.contains(ans)) {
//                        System.out.println(player.getName() + ", please select one of your countries to place a unit on.");
//                        System.out.println("Remaining units: " + (units - i));
//                        System.out.println("Possible options:" + player.getCountries().values());
//                        ans = reader.nextLine();
//                        if (!playerCountries.contains(ans)) {
//                            checkForSpecialInput(ans, "Invalid input. Make sure your input matches the key exactly", game.toString());
//                            System.out.println("Invalid input. Make sure your input matches the key exactly.");
//                        }
//                    }
//                    try {
//                        gc.changeUnits(gameId, player.getCountries().get(ans), 1);
//                    } catch (GameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        // print the outcome
//        for (Country country : game.getCountries().values()) {
//            System.out.println(country + ": Units:" + country.getUnits() + " Owner: " + country.getOwner());
//        }
//
//        for (Player p : game.getPlayers()) {
//            GraphController.getInstance().updatePlayerGraphMap(p);
//
//        }
//    }*/