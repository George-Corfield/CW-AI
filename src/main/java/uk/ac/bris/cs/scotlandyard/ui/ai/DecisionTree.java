package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.LogEntry;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Move.SingleMove;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Piece.Detective;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;
import java.util.List;

import java.util.ArrayList;
import com.google.common.collect.ImmutableSet;
import java.util.Random;


public class DecisionTree {
    public Branch rootNode;

    Integer maxDepth = 3;

    public Move finalDecisionMove;

    public Integer mrXLastLocation = null;


    DecisionTree(GameState state){
        rootNode = new Branch(state,null,true);
        for (LogEntry l : state.getMrXTravelLog().reverse()){
            if (!l.location().isEmpty()){
                mrXLastLocation = l.location().get();
            }
        }
        createDecisionTree();
        minMaxFunction(rootNode,maxDepth);
        finalDecisionMove = rootNode.move;
    }

    public void createDecisionTree(){
        createDecisionTreeLevel(rootNode,maxDepth,rootNode.maximisingNode);
    }

    public Boolean createDecisionTreeLevel(Branch currentNode, Integer depth, Boolean maximisingLevel) {
        if (depth > 0) {
            if (maximisingLevel) {
                List<Move> moves = new ArrayList<>(currentNode.gameState.getAvailableMoves());
                for (Move move : moves) {
                    GameState nextGameState = currentNode.gameState.advance(move);
                    if (depth == 1 || !nextGameState.getWinner().isEmpty()) {
                        Leaf newNode = new Leaf(nextGameState, move, false);
                        currentNode.addChildNode(newNode);
                        newNode.setHeuristicValue(new MrXScore().score(nextGameState,move));
                    } else {
                        Branch newNode = new Branch(nextGameState,move,false);
                        currentNode.addChildNode(newNode);
                        newNode.setHeuristicValue(new MrXScore().score(nextGameState,move));
                        createDecisionTreeLevel(newNode, depth - 1, false);
                    }
                }
            } else {
                GameState newDetectiveState = calculateDetectiveMoves(currentNode.gameState);
                if (newDetectiveState.getWinner().isEmpty()) {
                    Branch newNode = new Branch(newDetectiveState,null,true);
                    currentNode.addChildNode(newNode);
                    createDecisionTreeLevel(newNode, depth - 1, true);
                } else {
                    Leaf newNode = new Leaf(newDetectiveState,null,false);
                    currentNode.addChildNode(newNode);
                }
            }
        }
        return true;
    }

    public GameState calculateDetectiveMoves(GameState state){
        LogEntry l = state.getMrXTravelLog().get(state.getMrXTravelLog().size()-1);
        if (!l.location().isEmpty()){
            mrXLastLocation = l.location().get();
        }
        if (mrXLastLocation == null){
            for (Piece p : state.getPlayers()){
                if (p.isDetective()) {
                    var moves = state.getAvailableMoves().asList();
                    state = state.advance(moves.get(new Random().nextInt(moves.size())));
                    if (!state.getWinner().isEmpty()) return state;
                }
            }
        } else {
            ShortestPath SD = new ShortestPath(mrXLastLocation,state.getSetup().graph);
            SD.BreadthFirstSearch();
            for (Piece p : state.getPlayers()) {
                if (p.isDetective()) {
                    Detective d = (Detective) p;
                    var moves = state.getAvailableMoves();
                    Integer location = state.getDetectiveLocation(d).get();
                    Integer optimalDestination = SD.previous.get(location);
                    Boolean moveMade = false;
                    for (Transport t : Transport.values()){
                        if (moves.contains( new SingleMove(p,location,t.requiredTicket(),mrXLastLocation))){
                            state = state.advance(new SingleMove(p,location,t.requiredTicket(),mrXLastLocation));
                            if (!state.getWinner().isEmpty()) return state;
                            moveMade = true;
                            break;
                        }
                    }
                    if (!moveMade){
                        for (Transport t : Transport.values()){
                            for (Integer destination : state.getSetup().graph.adjacentNodes(location)){
                                if (moves.contains(new SingleMove(p,location,t.requiredTicket(),destination))){
                                    state = state.advance(new SingleMove(p,location,t.requiredTicket(),destination));
                                    if (!state.getWinner().isEmpty()) return state;
                                    moveMade = true;
                                    break;
                                }
                            }
                            if (moveMade) break;
                        }
                    }

                }
            }
        }
        return state;
    }

    public Boolean minMaxFunction(Branch branch, Integer depth){
        if (depth > 1){
            if (branch.maximisingNode){
                for (Node n: branch.children){
                    if (n.getClass() == Branch.class) minMaxFunction((Branch) n,depth-1);
                }
                getMaximum(branch);
            }else{
                for (Node n: branch.children){
                    if (n.getClass() == Branch.class) minMaxFunction((Branch) n,depth-1);
                }
                getMinimum(branch);
            }
        } else {
            if (branch.maximisingNode) getMaximum(branch);
            else getMinimum(branch);
        }
        return true;
    }

    public void getMaximum(Branch branch){
        Integer maximum = Integer.MIN_VALUE;
        for (Node n : branch.children){
            if (n.heuristicValue != null && n.heuristicValue > maximum){
                maximum = n.heuristicValue;
                if (branch == rootNode) rootNode.move=n.move;
            }
        }
        branch.setHeuristicValue(maximum);
    }

    public void getMinimum(Branch branch){
        Integer minimum = Integer.MAX_VALUE;
        for (Node n : branch.children){
            if (n.heuristicValue != null && n.heuristicValue < minimum){
                minimum = n.heuristicValue;
            }
        }
        branch.setHeuristicValue(minimum);
    }
}
