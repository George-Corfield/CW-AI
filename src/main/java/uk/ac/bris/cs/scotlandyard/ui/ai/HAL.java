package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

public class HAL implements Ai {
	@Nonnull @Override public String name() { return "HAL"; }

	@Nonnull @Override public Move pickMove(
			@Nonnull Board board,
			Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
		var moves = board.getAvailableMoves().asList();
		return moves.get(new Random().nextInt(moves.size()));
	}

	public Move getBestMove(Board board){
		ImmutableSet<Move> moves = board.getAvailableMoves();
		int maxScore = 0;
		Move bestMove = moves.asList().get(0);
		for (Move move: moves){
			int moveScore = score(board,move);
			if (moveScore > maxScore){
				maxScore = moveScore;
				bestMove = move;
			}
		}
		return bestMove;
	}

	public int score(Board board,
					 Move move){
		int weight = 0;
		for (Piece p : board.getPlayers())
			if (p.isDetective()){
				int location = 0;
		}

		return weight;
	}
}
