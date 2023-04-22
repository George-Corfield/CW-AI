package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.graph.ImmutableValueGraph;

import java.util.*;

public class ShortestPath {
    public int startNode;
    public ImmutableValueGraph<Integer,com.google.common.collect.ImmutableSet<uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport>> graph;

    public HashMap<Integer,Integer> previous = new HashMap<>();
    ShortestPath(int start, ImmutableValueGraph g){
        startNode = start;
        graph = g;
    }

    public void BreadthFirstSearch(){
        List<Integer> visitedNodes = new ArrayList<>(startNode);
        Queue<Integer> toVisit = new ArrayDeque<>();
        toVisit.addAll(graph.adjacentNodes(startNode));
        int currentNode = startNode;
        int previousNode = startNode;
        while (!toVisit.isEmpty()){
            currentNode = toVisit.poll();
            visitedNodes.add(currentNode);
            previous.put(currentNode,previousNode);
            for (int node : graph.adjacentNodes(currentNode)){
                if (!visitedNodes.contains(node)) toVisit.add(node);
            }
            previousNode = currentNode;
        }
    }

    public int getShortestDistance(int destination){
        int distance = 0;
        Integer previousNode = previous.get(destination);
        while (!previousNode.equals(null)){
            distance += 1;
            previousNode = previous.get(previousNode);
        }
        return distance;
    }
}
