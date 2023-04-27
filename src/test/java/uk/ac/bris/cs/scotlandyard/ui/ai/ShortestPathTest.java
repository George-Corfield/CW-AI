package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import junit.framework.TestCase;
import org.junit.Test;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.GameSetup;
import uk.ac.bris.cs.scotlandyard.model.ImmutableBoard;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortestPathTest extends ParameterisedModelTestBase{

    @Test
    public void testBFS(){
        ShortestPath SP = new ShortestPath(100,standardGraph());
        SP.BreadthFirstSearch();
        assertThat(SP.previous.size()).isEqualTo(198);
    }

    @Test
    public void testShortestDistanceAcrossMap(){
        ShortestPath SP = new ShortestPath(1,standardGraph());
        SP.BreadthFirstSearch();
        assertThat(SP.getShortestDistance(199)).isEqualTo(5);
    }

    @Test
    public void testShortestDistanceBetween2Nodes(){
        ShortestPath SP = new ShortestPath(54,standardGraph());
        SP.BreadthFirstSearch();
        assertThat(SP.getShortestDistance(36)).isEqualTo(6);
    }

}