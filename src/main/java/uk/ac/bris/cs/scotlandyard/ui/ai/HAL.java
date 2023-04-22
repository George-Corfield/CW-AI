package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

public class HAL implements Ai {
	@Nonnull @Override public String name() { return "HAL"; }

	@Nonnull @Override public Move pickMove(
			@Nonnull Board board,
			Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
		return getBestMove(board);
//		var moves = board.getAvailableMoves().asList();
//		return moves.get(new Random().nextInt(moves.size()));

	}

	public Move getBestMove(Board board){
		ImmutableSet<Move> moves = board.getAvailableMoves();
		HashMap<Move,Integer> moveScores = new HashMap<>();
		int maxScore = 0;
		Move bestMove = moves.asList().get(0);
		for (Move move: moves){
			int moveScore = score(board,move);
			if (moveScore > maxScore){
				maxScore = moveScore;
				bestMove = move;
			}
			moveScores.put(move,moveScore);
		}
//		System.out.println(moveScores);
//		for (Move move: moveScores.keySet()){
//			if (moveScores.get(move) == maxScore) System.out.println(move);
//		}
		System.out.println(bestMove);
		ShortestPath SD = new ShortestPath(bestMove.source(),board.getSetup().graph);
		SD.BreadthFirstSearch();
		for (Piece p : board.getPlayers()) {
			if (p.isDetective()) {
				Piece.Detective d = (Piece.Detective) p;
				System.out.println(SD.getShortestDistance(board.getDetectiveLocation(d).get()));
			}
		}
//		System.out.println(maxScore);
		return bestMove;
	}

	/**
	 * @param board
	 * @param move
	 * @return score for specific move based on:
	 * if there is a detective at the destination
	 * No. of options post move
	 * Type of Move
	 * etc
	 */

	public int score(Board board,
					 Move move){
		int weight = 0;
		if (move.getClass() == Move.SingleMove.class){
			Move.SingleMove m = (Move.SingleMove) move;
			if (!noDetectives(board,m.destination)) return 0;
			else weight += 5;
			if (m.ticket == ScotlandYard.Ticket.SECRET) weight -=1;
			for (int node : board.getSetup().graph.adjacentNodes(m.destination)){
				if (noDetectives(board,node)) weight += 1;
			}
		} else {
			Move.DoubleMove m = (Move.DoubleMove) move;
			if (!noDetectives(board,m.destination1) || !noDetectives(board,m.destination2)) return 0;
			else weight += 3;
			if (m.ticket1 == ScotlandYard.Ticket.SECRET || m.ticket2== ScotlandYard.Ticket.SECRET) weight -=1;
			for (int node : board.getSetup().graph.adjacentNodes(m.destination2)){
				if (noDetectives(board,node)) weight += 1;
			};
		}
		return weight;
	}

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

//	public int BFS(int startV, Board board, int detective){
//		List<Integer> visited = new ArrayList<>();
//		Queue<Integer> toVisit = new ArrayDeque<>();
//		Integer currentV = startV;
//		Integer distance = 0;
//		visited.add(startV);
//		toVisit.addAll(board.getSetup().graph.adjacentNodes(startV));
//		while (currentV != detective){
//			distance += 1;
//			currentV = toVisit.poll();
//			visited.add(currentV);
//			for (Integer node : board.getSetup().graph.adjacentNodes(currentV)){
//				if (!visited.contains(node)) toVisit.add(node);
//			}
//		}
//		System.out.println(currentV);
//		System.out.println(visited);
//		System.out.println(toVisit);
//		System.out.println(distance+ "\n");
//		return 0;
//	}
}
