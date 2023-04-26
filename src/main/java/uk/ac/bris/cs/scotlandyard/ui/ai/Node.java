package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.Move;


import java.util.ArrayList;

public class Node {
    public GameState gameState;
    public Integer heuristicValue;

    public Move move;
    public Boolean maximisingNode;

    Node(GameState state,Move m, Boolean maxNode){
        gameState = state;
        move = m;
        maximisingNode = maxNode;
    }

    public void setHeuristicValue(Integer value){
        if (heuristicValue != null) heuristicValue = (heuristicValue + value)/2;
        else heuristicValue = value;
    }

}
