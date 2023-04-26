package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.Move;


import java.util.ArrayList;

public class Branch extends Node{

    public ArrayList<Node> children = new ArrayList<>();

    Branch(GameState state, Move m, Boolean maxNode){
        super(state,m,maxNode);
    }

    public void addChildNode(Node n){
        children.add(n);
    }
}
