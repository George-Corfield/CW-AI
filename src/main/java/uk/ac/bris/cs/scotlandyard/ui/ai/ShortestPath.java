package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;

import java.util.*;

/**
 * class used to determine the shortest distance between two points
 * holds a startNode as an integer, a graph and a HashMap to hold the previously visited Node
 */

public class ShortestPath {
    public int startNode;
    public ImmutableValueGraph<Integer,ImmutableSet<Transport>> graph;

    public HashMap<Integer,Integer> previous = new HashMap<>();

    /**
     *
     * @param start
     * @param g
     *
     * sets the startNode ot the passed Node and the graph to the current gameState graph
     */
    ShortestPath(int start, ImmutableValueGraph g){
        startNode = start;
        graph = g;
    }

    /**
     * Performs BFS on the given graph starting at the start Node
     * Uses a Queue to store nodes which need to be visited and adds to previous when a new Node is visisted
     *
     */
    public void BreadthFirstSearch(){
        List<Integer> visitedNodes = new ArrayList<>(startNode);
        Queue<Integer> toVisit = new ArrayDeque<>();
        int currentNode = startNode;
        visitedNodes.add(currentNode);
        for (int node : graph.adjacentNodes(startNode)){
            toVisit.add(node);
            visitedNodes.add(node);
            previous.put(node,startNode);
        }
        while (!toVisit.isEmpty()){
            currentNode = toVisit.poll();
            for (int node : graph.adjacentNodes(currentNode)){
                if (!visitedNodes.contains(node)) {
                    toVisit.add(node);
                    visitedNodes.add(node);
                    previous.put(node,currentNode);
                }
            }
        }
    }

    /**
     *
     * @param destination
     * @return the shortest distance to the given destination by tracing backwards towards the start node
     */

    public int getShortestDistance(int destination){
        int distance = 0;
        Integer previousNode = previous.get(destination);
        while (previousNode != null){
            distance += 1;
            previousNode = previous.get(previousNode);
        }
        return distance;
    }
}
