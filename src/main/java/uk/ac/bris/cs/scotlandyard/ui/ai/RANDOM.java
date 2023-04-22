package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class RANDOM implements Ai {

    @Nonnull
    @Override public String name() { return "RANDOM"; }

    @Nonnull @Override public Move pickMove(
            @Nonnull Board board,
            Pair<Long, TimeUnit> timeoutPair) {
		var moves = board.getAvailableMoves().asList();
		return moves.get(new Random().nextInt(moves.size()));
    }
}
