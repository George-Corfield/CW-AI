package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;

public class HAL implements Ai {
	@Nonnull @Override public String name() { return "HAL"; }

	@Nonnull @Override public Move pickMove(
			@Nonnull Board board,
			Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
//		MrXScore scoreMrX = new MrXScore();
//		return scoreMrX.getBestMove(board);
		DecisionTree tree = new DecisionTree((GameState) board);
		return tree.finalDecisionMove;
	}

}
