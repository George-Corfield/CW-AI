package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Board;

import javax.annotation.Nonnull;


public interface ScoreFunction {

    /**
     * Computes the weight for a given move based on:
     * The type of move
     * The distance to detectives
     * Available locations to move to after the move
     * The ticket used
     *
     * @param board
     * @param move
     * @return the weight for a given move
     *
     */
    @Nonnull
    Integer score(@Nonnull Board board, @Nonnull Move move);

    /**
     * Compute the total distance to Opponent from Players location
     * by calling ShortestPath.getShortestDistance()
     *
     * @param board
     * @param source
     * @return total distance
     */
    @Nonnull
    Integer getDistanceToOpponent(@Nonnull Board board, @Nonnull Integer source);




}
