package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.Move;


/**
 * Leaf inherits from Node
 */

public class Leaf extends Node{


    Leaf(GameState state, Move m, Boolean maxNode){
        super(state,m,maxNode);
    }


}
