package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Ticket;

import javax.annotation.Nonnull;

public class MrXScore implements ScoreFunction{


    @Override
    @Nonnull
    public Integer score(Board board,
                     Move move){
        int weight = 0;
        if (move.getClass() == Move.SingleMove.class){
            Move.SingleMove m = (Move.SingleMove) move;
            weight += getDistanceToOpponent(board, m.destination);
            weight += getScoreForTicket(m.ticket);
            weight += getPossibilitiesForMove(board, m.destination);
        } else {
            Move.DoubleMove m = (Move.DoubleMove) move;
            weight += getDistanceToOpponent(board,m.destination2);
            weight += (getScoreForTicket(m.ticket1) + getScoreForTicket(m.ticket2))/2;
            weight += getPossibilitiesForMove(board, m.destination2);
        }
        return weight;
    }

    /**
     * Compute the total distance to each detective from MrX's location
     * by calling ShortestPath.getShortestDistance() for each detective
     *
     * @param board
     * @param source
     * @return total distance to detectives
     */

    @Override
    public Integer getDistanceToOpponent(Board board, Integer source){
        Integer distance = 0;
        ShortestPath SD = new ShortestPath(source,board.getSetup().graph);
        SD.BreadthFirstSearch();
        for (Piece p : board.getPlayers()) {
            if (p.isDetective()) {
                Piece.Detective d = (Piece.Detective) p;
                distance += SD.getShortestDistance(board.getDetectiveLocation(d).get());
            }
        }
        return distance;
    }

    @Override
    public Integer getScoreForTicket(Ticket ticket){
        if (ticket == Ticket.SECRET) {
            return -5;
        } else if (ticket == Ticket.BUS) {
            return 4;
        } else if (ticket == Ticket.TAXI) {
            return 2;
        } else{
            return 8;
        }
    }

    @Override
    public Integer getPossibilitiesForMove(Board board, Integer destination){
        Integer possibleMoves = 0;
        for (int node : board.getSetup().graph.adjacentNodes(destination)){
            if (noDetectives(board,node)) possibleMoves += 1;
        }
        return possibleMoves;
    }

    /**
     * Calculates if any detective is present at a given location called "destination"
     *
     * @param board
     * @param destination
     * @return Boolean True if no detectives are present, False if a detective is present
     */
    public boolean noDetectives(Board board,
                                int destination){
        for (Piece p : board.getPlayers()){
            if (p.isDetective()) {
                Piece.Detective d = (Piece.Detective) p;
                if (destination==board.getDetectiveLocation(d).get()) return false;
            }
        }
        return true;
    }


}
