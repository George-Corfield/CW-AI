package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.Move;

/**
 * Node is the parent class of Branch and Leaf
 * Holds a GameState, a Heuristic Value, a Move made to create GameState and Boolean to determine if Node is a maximising value
 */

public class Node {
    public GameState gameState;
    public Integer heuristicValue;

    public Move move;
    public Boolean maximisingNode;

    /**
     *
     * @param state
     * @param m
     * @param maxNode
     *
     * Sets the gameState, move and maximisingNode value
     */

    Node(GameState state,Move m, Boolean maxNode){
        gameState = state;
        move = m;
        maximisingNode = maxNode;
    }

    /**
     *
     * @param value
     * if the HeuristicValue is null, it is set to the new passed value
     * if the HeuristicValue is not null, it is averaged with the passed value
     */

    public void setHeuristicValue(Integer value){
        if (heuristicValue != null) heuristicValue = (heuristicValue + value)/2;
        else heuristicValue = value;
    }

}
