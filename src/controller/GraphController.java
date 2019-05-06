package controller;

import datastructures.Graph;
import model.Country;
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
        if(instance == null) {
            instance = new GraphController();
        }
        return instance;
    }

    private Graph createGraph(Player player) {

        Graph graph = new Graph();
        List<Country> countryList = new ArrayList<>(player.getCountries().values());
        graph = graph.createGraph(countryList);

        return graph;
    }

    public void updatePlayerGraphMap(Player player) {
        Graph graph = createGraph(player);
        playerGraphMap.put(player, graph);
    }

    public Map<Player, Graph> getPlayerGraphMap() {
        return playerGraphMap;
    }
}
