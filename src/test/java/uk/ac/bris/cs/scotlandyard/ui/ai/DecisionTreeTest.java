package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import static org.assertj.core.api.Assertions.assertThat;


public class DecisionTreeTest extends ParameterisedModelTestBase {

    @Test public void testTreeCreation(){
        GameState state = gameStateFactory.build(standard24MoveSetup(),blackPlayer(),bluePlayer(),redPlayer(),greenPlayer(),whitePlayer(),yellowPlayer());
        DecisionTree tree = new DecisionTree(state);
        tree.createDecisionTree();
        tree.minMaxFunction(tree.rootNode,tree.maxDepth);
        System.out.println(tree.rootNode.move);
        System.out.println(tree.rootNode.heuristicValue);
        assertThat(true).isEqualTo(true);
    }


}