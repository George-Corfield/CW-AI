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
