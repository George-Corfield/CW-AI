package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.LogEntry;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Move.SingleMove;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Piece.Detective;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;
import java.util.List;

import java.util.ArrayList;
import java.util.Random;

/**
 * decisionTree - holds a rootNode, a maximum depth, the best possible move and mrX's last known location
 */

public class DecisionTree {
    /**
     * holds the root node of the tree
     */
    public Branch rootNode;
    /**
     * holds the maximum Depth of the tree
     */
    Integer maxDepth = 3;
    /**
     * holds the best move after the algorithm has run
     */
    public Move finalDecisionMove;
    /**
     * holds MrX's last known location on map
     */
    public Integer mrXLastLocation = null;


    /**
     *
     * @param state
     * Constructor to set the rootNode and also get the last known location of MrX
     * by looking at MrX's travel log and getting the last reveal move location
     * Creates a decision tree and also calls the minMax function to retrieve the best move
     */
    DecisionTree(GameState state){
        rootNode = new Branch(state,null,true);
        for (LogEntry l : state.getMrXTravelLog().reverse()){
            if (!l.location().isEmpty()){
                mrXLastLocation = l.location().get();
                break;
            }
        }
        createDecisionTree();
        minMaxFunction(rootNode,maxDepth);
        finalDecisionMove = rootNode.move;
    }

    /**
     * calls create Decision tree level in order to create each level of the tree
     */

    public void createDecisionTree(){
        createDecisionTreeLevel(rootNode,maxDepth,rootNode.maximisingNode);
    }

    /**
     *
     * @param currentNode
     * @param depth
     * @param maximisingLevel
     * @return true to ensure that the tree is created successfully
     *
     * recursively calls createDecisionTreeLevel with decreasing depth and alternating maximising level
     * when it is a maximising level, it is MrX's move and so children are created for each possible move and a heuristic calculated
     *
     * when it is a minimising level, it is Detectives turn and so an optimal GameState is created by calling calculateDetectiveMoves
     * this ultimately creates a minimising level by playing optimal detective moves
     */

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

    /**
     *
     * @param state
     * @return the optimal GameState after all detective moves based on MrX's last known location
     *
     * if the last known location is null, random moves for each detective are made
     *
     * otherwise optimal move to MrX is calculated and used to create a minimising level for tree
     */

    public GameState calculateDetectiveMoves(GameState state){
        LogEntry l = state.getMrXTravelLog().get(state.getMrXTravelLog().size()-1);
        if (!l.location().isEmpty()){
            mrXLastLocation = l.location().get();
        }
        if (mrXLastLocation == null){
            state = getRandomMoves(state);
        }else{
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
                        moveMade = validateMove(moves,state,p,location,t,optimalDestination);
                        if (moveMade){
                            state.advance(new SingleMove(p,location,t.requiredTicket(),optimalDestination));
                            if (!state.getWinner().isEmpty()) return state;
                            break;
                        }
                    }
                    if (!moveMade){
                        for (Transport t : Transport.values()){
                            for (Integer destination : state.getSetup().graph.adjacentNodes(location)){
                                moveMade = validateMove(moves,state,p,location,t,destination);
                                if (moveMade){
                                    state = state.advance(new SingleMove(p,location,t.requiredTicket(),destination));
                                    if (!state.getWinner().isEmpty()) return state;
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

    /**
     *
     * @param moves
     * @param state
     * @param p
     * @param location
     * @param t
     * @param destination
     * @return a boolean if the given move is valid for the given detective
     */

    public Boolean validateMove(ImmutableSet<Move> moves, GameState state, Piece p, Integer location, Transport t, Integer destination){
        if (moves.contains(new SingleMove(p,location,t.requiredTicket(),destination))) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param state
     * @return the gameState after each detectives' random move is calculated
     */
    public GameState getRandomMoves(GameState state){
        for (Piece p : state.getPlayers()){
            if(p.isDetective()){
                var moves=state.getAvailableMoves().asList();
                state=state.advance(moves.get(new Random().nextInt(moves.size())));
                if (!state.getWinner().isEmpty()) return state;
            }
        }
        return state;
    }

    /**
     *
     * @param branch
     * @param depth
     * @return a Boolean of true if the program has achieved what is is supposed to do
     *
     * Method recursively calls itself while decreasing the depth for each branch in order to pick maximum and minimums
     */

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

    /**
     *
     * @param branch
     *
     * gets the maximum for the children of a branch by comparing heuristic values
     * if the branch is the root it sets the move of the root to the same move for the maximum branch
     * sets the heuristic value of the given branch to the maximum of the children
     */

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

    /**
     *
     * @param branch
     *
     * gets the minimum for the children of hte branch by comparing heuristic values
     * sets the heuristic value of the given branch to the minimum of the children
     */

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
