package controller;

import datastructures.Graph;
import exceptions.NoSuchPlayerException;
import model.Country;
import model.Game;
import model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphController {

    private static GraphController instance;

    private Map<Player, Graph> playerGraphMap;

    private GraphController() {
        playerGraphMap = new HashMap<>();
    }

    public static GraphController getInstance() {
        if (instance == null) {
            instance = new GraphController();
        }
        return instance;
    }

    public void updatePlayerGraphMap(Game game, Player player) throws NoSuchPlayerException {
        if (!game.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player + " does not exist.");
        }
        Graph graph = createGraph(player);
        playerGraphMap.put(player, graph);
    }

    private Graph createGraph(Player player) {


        Graph graph = new Graph();
        List<Country> countryList = new ArrayList<>(player.getCountries().values());
        graph = graph.createGraph(countryList);

        return graph;
    }


    public Map<Player, Graph> getPlayerGraphMap() {
        return playerGraphMap;
    }
}
