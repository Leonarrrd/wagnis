package view.cui;

import exceptions.GameNotFoundException;
import exceptions.NoSuchCardException;
import exceptions.NoSuchCountryException;
import exceptions.NoSuchPlayerException;
import model.Game;
import model.Player;

import java.util.UUID;

public class PlaceUnitsCUI extends AbstractCUI {


    /**
     * Place units
     *
     */
    @Override
    public void run() {
        System.out.println("Placement Phase...");
        int unitsToPlace = 0;
        Game game = null;
        Player player = null;
        try {
            game = gc.getGameById(gameId);

        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }
        player = game.getTurn().getPlayer();

        try {
            unitsToPlace = awardUnits(gameId, player);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }

        while (unitsToPlace > 0) {
            String ans = "";
            while (!player.getCountries().keySet().contains(ans)) {
                System.out.println("All units must be placed to continue.");
                System.out.println("Total available units: " + unitsToPlace);
                System.out.println("Select a country to place units on.");
                System.out.println("Possible options:");
                System.out.println(player.getCountries().values());
                ans = reader.nextLine();
                if (!player.getCountries().keySet().contains(ans)) {
                    checkForSpecialInput(ans, "Invalid input. Make sure you input matches the country name exactly");
                }
            }

            int howManyUnits = 0;
            while (howManyUnits <= 0 || howManyUnits > unitsToPlace) {
                System.out.println("How many units do you want to place on " + ans + "?");
                String numAsString = reader.nextLine();

                try {
                    //TODO: Following two lines are a bit awkward. Had problems with readInt() method, so did this instead
                    howManyUnits = Integer.parseInt(numAsString);
                } catch (NumberFormatException e) {
                    checkForSpecialInput(numAsString, "Please enter a number");
                }
            }
            try {
                gc.changeUnits(gameId, player.getCountries().get(ans), howManyUnits);
            } catch (GameNotFoundException | NoSuchCountryException e) {
                e.printStackTrace();
            }
            unitsToPlace -= howManyUnits;
            System.out.println(howManyUnits + " unit(s) has(have) been placed on " + ans);
        }
    }

    /**
     * Awards the user with Units
     *
     * @param gameId
     * @param player
     * @return
     * @throws GameNotFoundException pushing the exception to the last caller
     */
    private int awardUnits(UUID gameId, Player player) throws GameNotFoundException {
        Game game = gc.getGameById(gameId);

        try {
            gc.awardUnits(gameId, player);
        } catch (NoSuchPlayerException e) {
            e.printStackTrace();
        }

        int awardedUnits = player.getUnitsToPlace();

        System.out.println(player + " is awarded " + awardedUnits + " units.");

        if (!player.getCards().isEmpty()){
            String ans = "";
            while (!(ans.equals("y") || ans.equals("n"))) {
                System.out.println("Card Exchange Rate: Number of Cards squared");
                System.out.println("Do you want to use any cards? (y/n)");
                ans = reader.nextLine();
                if (!(ans.equals("y") || ans.equals("n"))) {
                    checkForSpecialInput(ans, "Neither 'y' nor 'n'");
                }
                if (ans.equals("y")) {
                    int cardsToBeUsed = 0;
                    int maxCards = player.getCards().size();
                    System.out.println("How many cards do you want to use? Max : " + maxCards);
                    while (cardsToBeUsed < 1 || cardsToBeUsed > maxCards){
                        String numAsString = reader.nextLine();
                        try {
                            //TODO: Following two lines are a bit awkward. Had problems with readInt() method, so did this instead
                            cardsToBeUsed = Integer.parseInt(numAsString);
                        } catch (NumberFormatException e) {
                            checkForSpecialInput(numAsString, "Please enter a number");
                        }
                    }
//                    try {
//                        awardedUnits += gc.useCards(gameId, player, cardsToBeUsed);
//                    } catch (NoSuchCardException | NoSuchPlayerException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }

        return awardedUnits;
    }

}
