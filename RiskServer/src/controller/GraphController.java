package controller;

import datastructures.Graph;
import exceptions.NoSuchCardException;
import exceptions.NoSuchPlayerException;
import model.Country;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that controls the Graph mechanism
 */
public class GraphController {

    private static GraphController instance;

    public static GraphController getInstance() {
        if (instance == null) {
            instance = new GraphController();
        }
        return instance;
    }

    /**
     * Creates the graph by providing the game
     * @param game the game that the graph needs to be created for
     * @throws NoSuchPlayerException
     */
    public void createGraphs(Game game) throws NoSuchPlayerException {
        for (Player player : game.getPlayers()){
            updateGraph(game, player);
        }
    }

    /**
     * Updates a Graph (e.g. if a country changes ownership).
     * @param game
     * @param player
     * @throws NoSuchPlayerException
     */
    public void updateGraph(Game game, Player player) throws NoSuchPlayerException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        Graph graph = new Graph();
        List<Country> countryList = new ArrayList<>(player.getCountries().values());
        graph = graph.createGraph(countryList);
        player.setCountryGraph(graph);
    }
}
