package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Ticket;
import static uk.ac.bris.cs.scotlandyard.model.Piece.Detective.*;
import static uk.ac.bris.cs.scotlandyard.model.Piece.MrX.MRX;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultDetectiveTickets;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultMrXTickets;

import static org.assertj.core.api.Assertions.assertThat;
public class NodeTest extends ParameterisedModelTestBase {

    @Test
    public void testSetHeuristicValue(){
        var mrX = new Player(MRX,defaultMrXTickets(),140);
        Node node = new Node(gameStateFactory.build(
                standard24MoveSetup(),
                blackPlayer(),
                redPlayer()),
                bus(MRX, 140,82),
                true);
        node.setHeuristicValue(20);
        assertThat(node.heuristicValue).isEqualTo(20);
        node.setHeuristicValue(10);
        assertThat(node.heuristicValue).isEqualTo(15);

    }

    @Test
    public void testBranchAddChild(){
        var mrX = new Player(MRX,defaultMrXTickets(),79);
        Branch branch = new Branch(gameStateFactory.build(
                standard24MoveSetup(),
                blackPlayer(),
                redPlayer()),
                bus(MRX, 79,63),
                true);
        Node n = new Node(gameStateFactory.build(
                standard24MoveSetup(),
                blackPlayer(),
                redPlayer()),
                underground(MRX, 111, 153),
                false);
        branch.addChildNode(n);
        assertThat(branch.children.get(0)).isEqualTo(n);
    }
}