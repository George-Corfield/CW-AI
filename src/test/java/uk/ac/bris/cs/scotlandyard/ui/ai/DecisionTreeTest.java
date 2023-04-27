package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import static org.assertj.core.api.Assertions.assertThat;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Ticket;
import static uk.ac.bris.cs.scotlandyard.model.Piece.Detective.*;
import static uk.ac.bris.cs.scotlandyard.model.Piece.MrX.MRX;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultDetectiveTickets;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultMrXTickets;

import java.util.ArrayList;
import java.util.Arrays;

public class DecisionTreeTest extends ParameterisedModelTestBase {

    public Integer getMax(ArrayList<Node> arr){
        Integer max = Integer.MIN_VALUE;
        for (Node n: arr){
            if (n.heuristicValue > max) max = n.heuristicValue;
        }
        return max;
    }

    public Integer getMin(ArrayList<Node> arr){
        Integer min = Integer.MAX_VALUE;
        for (Node n: arr){
            if (n.heuristicValue < min) min = n.heuristicValue;
        }
        return min;
    }

    @Test public void testTreeCreation(){
        var mrX = new Player(MRX, makeTickets(2,2,2,0,0),67);
        var red = new Player(RED, defaultDetectiveTickets(),7);
        var white = new Player(WHITE, defaultDetectiveTickets(), 187);
        GameState state = gameStateFactory.build(standard24MoveSetup(),mrX,red,white);
        DecisionTree tree = new DecisionTree(state);
        assertThat(tree.rootNode.children.size()).isEqualTo(13);
        Branch node = (Branch) tree.rootNode.children.get(0);
        assertThat(node.children.size()).isEqualTo(1);
        node = (Branch) node.children.get(0);
        assertThat(node.children.size()).isGreaterThan(0);
    }

    @Test public void testValidateMoves(){
        var mrX = new Player(MRX, makeTickets(1,1,1,0,0),67);
        GameState state = gameStateFactory.build(standard24MoveSetup(),mrX,redPlayer());
        DecisionTree tree = new DecisionTree(state);
        assertThat(tree.validateMove(state.getAvailableMoves(),
                state,
                MRX,
                67,
                ScotlandYard.Transport.TAXI,
                84)).isEqualTo(true);
    }

    @Test public void testGetRandomMoves(){
        var red = new Player(RED,defaultDetectiveTickets(),123);
        GameState state = gameStateFactory.build(standard24MoveSetup(),blackPlayer(),red);
        DecisionTree tree = new DecisionTree(state);
        state = state.advance(state.getAvailableMoves().asList().get(0));
        GameState newState = tree.getRandomMoves(state);
        assertThat(state.getDetectiveLocation(RED)).isNotEqualTo(newState.getDetectiveLocation(RED));
    }

    @Test public void testMinMaxFunction(){
        var mrX = new Player(MRX, makeTickets(1,1,0,0,0), 34);
        var red = new Player(RED, makeTickets(2, 0, 0,0,0),126);
        var state = gameStateFactory.build(
                new GameSetup(standardGraph(),moves(true,false,false)),mrX,red);
        DecisionTree tree = new DecisionTree(state);
        assertThat(tree.finalDecisionMove).isEqualTo(bus(MRX,34,46));
    }

    @Test public void testGetMaximum(){
        var mrX = new Player(MRX, makeTickets(2,2,2,0,0),67);
        GameState state = gameStateFactory.build(standard24MoveSetup(),mrX,redPlayer());
        Branch branch = new Branch(state,null,true);
        Integer inc = 0;
        for (Move m: state.getAvailableMoves()){
            Node n = new Node(state.advance(m),m,false);
            n.setHeuristicValue(inc++);
            branch.addChildNode(n);
        }
        DecisionTree tree = new DecisionTree(state);
        tree.getMaximum(branch);
        assertThat(branch.heuristicValue).isEqualTo(getMax(branch.children));
    }

    @Test public void testGetMinimum(){
        var mrX = new Player(MRX, makeTickets(2,2,2,0,0),67);
        GameState state = gameStateFactory.build(standard24MoveSetup(),mrX,redPlayer());
        Branch branch = new Branch(state,null,false);
        Integer inc = 0;
        for (Move m: state.getAvailableMoves()){
            Node n = new Node(state.advance(m),m,true);
            n.setHeuristicValue(inc--);
            branch.addChildNode(n);
        }
        DecisionTree tree = new DecisionTree(state);
        tree.getMinimum(branch);
        assertThat(branch.heuristicValue).isEqualTo(getMin(branch.children));
    }

}