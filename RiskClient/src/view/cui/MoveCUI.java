//package view.cui;
//
//import controller.GraphController;
//import datastructures.Graph;
//import exceptions.*;
//import model.Country;
//import model.Game;
//import model.Player;
//
//import java.util.Map;
//
////public class MoveCUI extends AbstractCUI {
//
//
//    /**
//     * Move units from one country to another, only possible if the destination country is connected with the source country
//     *
//     * @param
//     */
//    @Override
//    public void run() {
//
//
//        Game game = null;
//        Player player = null;
//        try {
//            game = gc.getGameById(gameId);
//
//        } catch (GameNotFoundException e) {
//            e.printStackTrace();
//        }
//        player = game.getTurn().getPlayer();
//
//        //might need another phase to do this
//        //might wanna do this in the controller move method
//
//        System.out.println("Move phase starting...");
//
//        System.out.println("Do you want to move any units? (y/n)");
//
//        String inputYN = reader.nextLine();
//
//        if (inputYN.equals("n")) {
//            return;
//        }
//
//        boolean movePhaseFinished = false;
//
//        while (!movePhaseFinished) {
//
//            Map<String, Country> countriesToMoveFrom = null;
//            boolean hasCountryToMoveFrom = false;
//            try {
//                countriesToMoveFrom = gc.getCountriesWithMoreThanOneUnit(gameId, player);
//                hasCountryToMoveFrom = gc.hasCountryToMoveFrom(gameId, player);
//            } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCountryException e) {
//                e.printStackTrace();
//            }
//
//            if (!hasCountryToMoveFrom) {
//                System.out.println(player.getName() + " has no countries to move from");
//                return;
//            }
//
//            System.out.println("Select a country to move from: ");
//            System.out.println(countriesToMoveFrom.keySet());
//
//            String srcCountryStr = reader.nextLine();
//            while (!countriesToMoveFrom.containsKey(srcCountryStr)) {
//                checkForSpecialInput(srcCountryStr, "Not a country or not owned");
//                srcCountryStr = reader.nextLine();
//            }
//
//            System.out.println("Possible options to move to from Country " + srcCountryStr);
//            Graph graph = GraphController.getInstance().getPlayerGraphMap().get(player);
//            System.out.println(graph.evaluateCountriesAllowedToMoveTo(srcCountryStr));
//
//
//            String destCountryStr = reader.nextLine();
//            while (!graph.evaluateCountriesAllowedToMoveTo(srcCountryStr).contains(srcCountryStr)) {
//                checkForSpecialInput(srcCountryStr, "Not a country or not a valid target");
//                destCountryStr = reader.nextLine();
//            }
//
//            Country srcCountry = game.getCountries().get(srcCountryStr);
//            Country destCountry = game.getCountries().get(destCountryStr);
//
//            int possibleUnits = srcCountry.getUnits() - 1;
//            if (possibleUnits < 1) { // this should never happen since it's now impossible to select a country with only one unit
//                System.out.println("Only one unit on country.");
//                return;
//            }
//            System.out.println("How many units would you like to move from " + srcCountry + " to " + destCountry + "?");
//            System.out.println("Max. Units possible: " + possibleUnits);
//            int unitsToMove = reader.nextInt();
//            while (!(unitsToMove <= possibleUnits && unitsToMove > 0)) {
//                unitsToMove = reader.nextInt();
//            }
//
//            try {
//                gc.moveUnits(gameId, srcCountry, destCountry, unitsToMove);
//            } catch (GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException | CountriesNotAdjacentException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(player.getName() + " moved " + unitsToMove + " units moved from " + srcCountry + " to " + destCountry);
//            System.out.println(srcCountry + " now has " + srcCountry.getUnits() + " units.");
//            System.out.println(destCountry + " now has " + destCountry.getUnits() + " units.");
//
//            String ans = "";
//            while (!(ans.equals("y") || ans.equals("n"))) {
//                System.out.println("Do you want to perform another move operation? (y,n)");
//                ans = reader.nextLine();
//                if (!(ans.equals("y") || ans.equals("n"))) {
//                    checkForSpecialInput(ans, "Neither 'y' nor 'n'");
//                }
//                if (ans.equals("n")) {
//                    movePhaseFinished = true;
//                }
//            }
//        }
//        System.out.println("Move phase finished.");
//    }
//
//}
//
