package view.cui;

import model.AttackResult;
import exceptions.*;
import model.Country;
import model.Game;
import model.Player;

import java.util.InputMismatchException;
import java.util.Map;

public class AttackCUI extends AbstractCUI {



    /**
     * Method to attack a hostile neighbour country

     */
    @Override
    public void run() {


        Game game = null;
        Player player = null;
        try {
            game = gc.getGameById(gameId);
        } catch (GameNotFoundException e) {
            e.printStackTrace();
        }
        player = game.getTurn().getPlayer();
        boolean hasCountryToAttackFrom = false;
        try {
            hasCountryToAttackFrom = gc.hasCountryToAttackFrom(gameId, player);
        } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCountryException e) {
            e.printStackTrace();
        }

        if (!hasCountryToAttackFrom) {
            System.out.println(player.getName() + " has no countries to attack from.");
            return;
        }

        System.out.println("Attacking Phase...");
        System.out.println("If you don't want to attack this turn, type \"no\". Otherwise just hit enter.");
        if (reader.nextLine().equals("no")) return; //FIXME: messy, needs proper (y/n) I/O

        boolean attackPhaseFinished = false;
        while (!attackPhaseFinished) {


                System.out.println(player.getName() + " has no countries to attack from");


            // find srcCountry
            String ans = "";
            Map<String, Country> countriesAttacksCanBeLaunchedFrom = null;
            try {
                countriesAttacksCanBeLaunchedFrom = gc.getCountriesAttackCanBeLaunchedFrom(gameId, player);
            } catch (GameNotFoundException | NoSuchPlayerException | NoSuchCountryException e) {
                e.printStackTrace();
            }
            while (!countriesAttacksCanBeLaunchedFrom.keySet().contains(ans)) {
                System.out.println(player + ", select a country the attack will be launched from.");
                System.out.println("Possible options:");
                System.out.println(countriesAttacksCanBeLaunchedFrom.keySet());
                ans = reader.nextLine();
                if (!countriesAttacksCanBeLaunchedFrom.keySet().contains(ans)) {
                    checkForSpecialInput(ans, "Not a country");
                }
            }
            Country srcCountry = game.getCountries().get(ans);
            // find destCountry
            ans = "";
            Map<String, Country> hostileNeighbors = null;
            try {
                 hostileNeighbors = gc.getHostileNeighbors(gameId, srcCountry);
            } catch (GameNotFoundException | NoSuchCountryException e) {
                e.printStackTrace();
            }
            while (!hostileNeighbors.keySet().contains(ans)) {
                System.out.println(player + " select a country to attack");
                System.out.println("Possible options:");
                System.out.println(hostileNeighbors.keySet());
                ans = reader.nextLine();
                if (!hostileNeighbors.keySet().contains(ans)) {
                    checkForSpecialInput(ans, "Not a country");
                }
            }
            Country destCountry = game.getCountries().get(ans);

            System.out.println("Fight starting.");

            boolean fightFinished = false;
            int round = 1;
            while (!fightFinished) {
                System.out.println("Attacking country: " + srcCountry + " Units: " + srcCountry.getUnits());
                System.out.println("Defending country: " + destCountry + " Units: " + destCountry.getUnits());

                int attackingUnits = 0;
                int maxAttackers = 3;
                if (srcCountry.getUnits() < maxAttackers + 1) {
                    maxAttackers = srcCountry.getUnits() - 1;
                }
                while (attackingUnits < 1 || attackingUnits > maxAttackers) {
                    System.out.println(player + ", input the number of units you want to attack with. Min 1, Max " + maxAttackers);
                    try {
                        attackingUnits = reader.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Not a number or invalid number");
                    }
                }

                int defendingUnits = 0;
                int maxDefenders = (destCountry.getUnits() == 1) ? 1 : 2; // lazy but okay
                while (defendingUnits < 1 || defendingUnits > maxDefenders) {
                    int max = (srcCountry.getUnits() == 1) ? 1 : 2;
                    System.out.println(destCountry.getOwner() + ", input the number of units you want to defend with. Min 1, Max " + maxDefenders);
                    try {
                        // Following two lines are a bit awkward. Had problems with readInt() method, so did this instead
                        String numAsString = reader.nextLine();
                        defendingUnits = Integer.parseInt(numAsString);
                    } catch (Exception e) {
                        checkForSpecialInput(ans, "Not a number");
                    }
                }

                AttackResult attackResult = null;
                try {
                    attackResult = gc.fight(gameId, srcCountry, destCountry, attackingUnits, defendingUnits);
                } catch (NotEnoughUnitsException | CountriesNotAdjacentException | GameNotFoundException | NoSuchCountryException e) {
                    e.printStackTrace();
                }
                System.out.println("Attacker dices: " + attackResult.getAttackerDices());
                System.out.println("Defender dices: " + attackResult.getDefenderDices());

                if (attackResult.getWinner() != null) {
                    fightFinished = true;
                    System.out.println(attackResult.getWinner() + " has won the war.");

                    if (attackResult.getWinner().equals(srcCountry) && srcCountry.getUnits() > 1) {
                        ans = "";
                        while (!(ans.equals("y") || ans.equals("n"))) {
                            System.out.println("Do you want to nachrueck units? (y,n)");
                            ans = reader.nextLine();
                            if (!(ans.equals("y") || ans.equals("n"))) {
                                checkForSpecialInput(ans, "Neither 'y' nor 'n'");
                            }
                            if (ans.equals("y")) {
                                int maxNachRueckUnits = (srcCountry.getUnits() - 1);
                                int nachRueckUnits = 0;

                                while (nachRueckUnits < 1 || nachRueckUnits > maxNachRueckUnits) {
                                    System.out.println("How many units do you want to nachrueck?");
                                    System.out.println("Max: " + maxNachRueckUnits);
                                    try {
                                        // Following two lines are a bit awkward. Had problems with readInt() method, so did this instead
                                        String numAsString = reader.nextLine();
                                        nachRueckUnits = Integer.parseInt(numAsString);
                                    } catch (Exception e) {
                                        checkForSpecialInput(ans, "Not a number.");
                                    }
                                }

                                try {
                                    gc.moveUnits(gameId, srcCountry, destCountry, nachRueckUnits);
                                } catch (GameNotFoundException | NotEnoughUnitsException | CountryNotOwnedException | NoSuchCountryException | NoSuchPlayerException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(nachRueckUnits + " units have been moved.");
                            }
                        }
                    }
                } else {
                    System.out.println("Round finished!");
                    System.out.println("Attacking country: " + srcCountry + " Units: " + srcCountry.getUnits());
                    System.out.println("Defending country: " + destCountry + " Units: " + destCountry.getUnits());

                    ans = "";
                    while (!(ans.equals("y") || ans.equals("n"))) {
                        System.out.println("Do you want to continue attacking? (y,n)");
                        ans = reader.nextLine();
                        if (!(ans.equals("y") || ans.equals("n"))) {
                            checkForSpecialInput(ans, "Neither 'y' nor 'n'");
                        }
                        if (ans.equals("n")) {
                            fightFinished = true;
                        }
                    }
                }
            }

            // two lines below will  be changed, just copied it here so we can see that units got actually deducted
            System.out.println("Attacking country: " + srcCountry + " Units: " + srcCountry.getUnits() + " Owner: " + srcCountry.getOwner());
            System.out.println("Defending country: " + destCountry + " Units: " + destCountry.getUnits() + " Owner: " + destCountry.getOwner());

            ans = "";
            while (!(ans.equals("y") || ans.equals("n"))) {
                System.out.println("Do you want to launch another attack? (y,n)");
                ans = reader.nextLine();
                if (!(ans.equals("y") || ans.equals("n"))) {
                    checkForSpecialInput(ans, "Neither 'y' nor 'n'");
                }
                if (ans.equals("n")) {
                    attackPhaseFinished = true;
                }
            }
        }

    }

}

