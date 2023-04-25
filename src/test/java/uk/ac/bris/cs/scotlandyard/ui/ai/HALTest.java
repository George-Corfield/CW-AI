package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import org.junit.Test;
import uk.ac.bris.cs.scotlandyard.model.*;

import static uk.ac.bris.cs.scotlandyard.model.Piece.Detective.*;
import static uk.ac.bris.cs.scotlandyard.model.Piece.MrX.MRX;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultDetectiveTickets;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultMrXTickets;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class HALTest extends ParameterisedModelTestBase{

    // public Pair<Long,TimeUnit> timeoutUnitPair = new Pair<>((long) 30, TimeUnit.SECONDS);

    @Test public void testNoDetectivesWhenNonePresent(){
        var halAI = new HAL();
        var red = new Player(RED, defaultDetectiveTickets(),141);
        var blue = new Player(BLUE,defaultDetectiveTickets(),129);
        var green = new Player(GREEN,defaultDetectiveTickets(),143);
        var yellow = new Player(YELLOW,defaultDetectiveTickets(),118);
        var white = new Player(WHITE,defaultDetectiveTickets(),115);
        var mrX = new Player(MRX,defaultMrXTickets(),160);
        Move.SingleMove move = taxi(MRX, 160, 128);
        Board board = gameStateFactory.build(standard24MoveSetup(),mrX,red,blue,green,yellow,white);
        assertThat(halAI.noDetectives(board,move.destination)).isEqualTo(true);
    }

    @Test public void testNoDetectivesWhenDetectivePresent(){
        var halAI = new HAL();
        var red = new Player(RED, defaultDetectiveTickets(),141);
        var blue = new Player(BLUE,defaultDetectiveTickets(),128);
        var green = new Player(GREEN,defaultDetectiveTickets(),143);
        var yellow = new Player(YELLOW,defaultDetectiveTickets(),118);
        var white = new Player(WHITE,defaultDetectiveTickets(),115);
        var mrX = new Player(MRX,defaultMrXTickets(),160);
        Move.SingleMove move = taxi(MRX, 160, 128);
        Board board = gameStateFactory.build(standard24MoveSetup(),mrX,red,blue,green,yellow,white);
        assertThat(halAI.noDetectives(board,move.destination)).isEqualTo(false);

    }
  
}