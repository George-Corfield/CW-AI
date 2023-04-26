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
    public void testBFSAcrossMap(){
        ShortestPath SD = new ShortestPath(1,standardGraph());
        SD.BreadthFirstSearch();
        assertThat(SD.getShortestDistance(199)).isEqualTo(5);
    }

}