package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;

import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Ticket;
import static uk.ac.bris.cs.scotlandyard.model.Piece.Detective.*;
import static uk.ac.bris.cs.scotlandyard.model.Piece.MrX.MRX;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultDetectiveTickets;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.defaultMrXTickets;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MrXScoreTest extends ParameterisedModelTestBase {

        @Test public void testNoDetectivesWhenNonePresent(){
        var mrXScore = new MrXScore();
        var red = new Player(RED, defaultDetectiveTickets(),141);
        var blue = new Player(BLUE,defaultDetectiveTickets(),129);
        var green = new Player(GREEN,defaultDetectiveTickets(),143);
        var yellow = new Player(YELLOW,defaultDetectiveTickets(),118);
        var white = new Player(WHITE,defaultDetectiveTickets(),115);
        var mrX = new Player(MRX,defaultMrXTickets(),160);
        Move.SingleMove move = taxi(MRX, 160, 128);
        Board board = gameStateFactory.build(standard24MoveSetup(),mrX,red,blue,green,yellow,white);
        assertThat(mrXScore.noDetectives(board,move.destination)).isEqualTo(true);
    }

    @Test public void testNoDetectivesWhenDetectivePresent(){
        var mrXScore = new MrXScore();
        var red = new Player(RED, defaultDetectiveTickets(),141);
        var blue = new Player(BLUE,defaultDetectiveTickets(),128);
        var green = new Player(GREEN,defaultDetectiveTickets(),143);
        var yellow = new Player(YELLOW,defaultDetectiveTickets(),118);
        var white = new Player(WHITE,defaultDetectiveTickets(),115);
        var mrX = new Player(MRX,defaultMrXTickets(),160);
        Move.SingleMove move = taxi(MRX, 160, 128);
        Board board = gameStateFactory.build(standard24MoveSetup(),mrX,red,blue,green,yellow,white);
        assertThat(mrXScore.noDetectives(board,move.destination)).isEqualTo(false);

    }

    @Test public void testGetPossibilitiesForMove(){
        var mrXScore = new MrXScore();
        var gameState = gameStateFactory.build(standard24MoveSetup(),
                blackPlayer(),redPlayer());
        List<Integer> destinations = List.of(1, 153, 75, 68, 90, 199, 3, 89);
        List<Integer> possibilities = List.of(4,12,4,4,3,5,5,8);
        for (int i = 0; i < destinations.size(); i++){
            assertThat(mrXScore.getPossibilitiesForMove((Board) gameState, destinations.get(i))).isEqualTo(possibilities.get(i));
        }
    }

    @Test public void testGetDistanceToOpponent(){
            var mrXScore = new MrXScore();
            var mrX = new Player(MRX,defaultMrXTickets(),101);
            var red = new Player(RED, defaultDetectiveTickets(), 199);
            var blue = new Player(BLUE, defaultDetectiveTickets(), 89);
            var green = new Player(GREEN, defaultDetectiveTickets(), 7);
            var state = gameStateFactory.build(standard24MoveSetup(),mrX,red,blue,green);
            assertThat(mrXScore.getDistanceToOpponent((Board) state, 101 )).isEqualTo(14);
    }

    @Test public void testScoreForSingleMoves(){
            var mrXScore = new MrXScore();
            var taxi = taxi(MRX, 67, 66);
            var bus = bus(MRX, 67, 102);
            var underground = underground(MRX,67,111);
            var secret = secret(MRX,67, 52);
            var mrX = new Player(MRX,defaultMrXTickets(),67);
            Board board = gameStateFactory.build(standard24MoveSetup(),mrX,redPlayer(),greenPlayer(),whitePlayer(),bluePlayer());
            assertThat(mrXScore.score(board,taxi)).isEqualTo(21);
            assertThat(mrXScore.score(board,bus)).isEqualTo(25);
            assertThat(mrXScore.score(board,underground)).isEqualTo(29);
            assertThat(mrXScore.score(board,secret)).isEqualTo(20);
    }

    @Test public void testScoreForDoubleMoves(){
        var mrXScore = new MrXScore();
        var mrX = new Player(MRX,defaultMrXTickets(),67);
        Board board = gameStateFactory.build(standard24MoveSetup(),mrX,redPlayer(),greenPlayer(),whitePlayer(),bluePlayer());
        assertThat(mrXScore.score(board, x2(MRX,
                67,
                Ticket.TAXI,
                66,
                Ticket.TAXI,
                82))).isEqualTo(22);
        assertThat(mrXScore.score(board,x2(MRX,
                67,
                Ticket.BUS,
                102,Ticket.BUS,
                86))).isEqualTo(23);
        assertThat(mrXScore.score(board,x2(MRX,
                67,
                Ticket.UNDERGROUND,
                111,
                Ticket.UNDERGROUND,
                153))).isEqualTo(35);
        assertThat(mrXScore.score(board,x2(MRX,
                67,
                Ticket.SECRET,
                52,
                Ticket.SECRET,
                13))).isEqualTo(16);
    }


}