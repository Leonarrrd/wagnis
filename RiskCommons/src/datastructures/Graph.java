
package datastructures;

import model.Country;

import java.util.*;


public class Graph {

    private Map<Country, List<Country>> adjVertices;

    public Graph() {
        adjVertices = new HashMap<>();
    }

    void addVertex(String name) {
        adjVertices.putIfAbsent(new Country(name), new ArrayList<>());
    }

    void addEdge(String name1, String name2) {
        Country c1 = new Country(name1);
        Country c2 = new Country(name2);
        adjVertices.get(c1).add(c2);
        adjVertices.get(c2).add(c1);
    }

    public Graph createGraph(List<Country> playerCountries) {
        Graph graph = new Graph();

        for (Country c : playerCountries) {
            graph.addVertex(c.getName());

        }
        for (Country c : playerCountries) {
            for (Country neighbour : c.getNeighbors()) {
                if (playerCountries.contains(neighbour)) {
                    graph.addEdge(c.getName(), neighbour.getName());
                }
            }
        }

        return graph;
    }

    List<Country> getAdjVertices(String name) {
        return adjVertices.get(new Country(name));
    }

    //FIXME: remove source country from List
    public List<String> evaluateCountriesAllowedToMoveTo(String root) {
        List<String> visited = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            String country = stack.pop();
            if (!visited.contains(country)) {
                visited.add(country);
                for (Country c : this.getAdjVertices(country)) {
                    stack.push(c.getName());
                }
            }
        }
        return visited;
    }

}
