package Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The MoveLogic class determines what a valid move is. It fully
 * implements all the rules of Hamka.
 */
public class MoveLogic {

	/**
	 * Determines if the specified move is valid based on the rules of Hamka.
	 * parameter game, the game to check against.
	 * parameter startIndex, the start index of the move.
	 * parameter endIndex, the end index of the move.
	 * return true if the move is legal according to the rules of Hamka.
	 */
	public static boolean isValidMove(Game game,
			int startIndex, int endIndex) {
		return game == null? false : isValidMove(game.getBoard(),
				game.isP1Turn(), startIndex, endIndex, game.getSkipIndex());
	}
	
	/**
	 * Determines if the specified move is valid based on the rules of Hamka.
	 * 
	 * board, the current board to check against.
	 * isP1Turn, the flag indicating if it is player 1's turn.
	 * startIndex, the start index of the move.
	 * endIndex, the end index of the move.
	 * skipIndex, the index of the last skip this turn.
	 * return true if the move is legal according to the rules of Hamka.
	 */
	public static boolean isValidMove(Board board, boolean isP1Turn,
			int startIndex, int endIndex, int skipIndex) {
		
		// Basic checks
		if (board == null || !Board.isValidIndex(startIndex) ||
				!Board.isValidIndex(endIndex)) {
			return false;
		} else if (startIndex == endIndex) {
			return false;
		} else if (Board.isValidIndex(skipIndex) && skipIndex != startIndex) {
			return false;
		}
		
		// Perform the tests to validate the move
		if (!validateIDs(board, isP1Turn, startIndex, endIndex)) {
			return false;
		} else if (!validateDistance(board, isP1Turn, startIndex, endIndex)) {
			return false;
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Validates all ID related values for the start, end, and middle (if the move is a skip).
	 * board, the current board to check against.
	 * isP1Turn, the flag indicating if it is player 1's turn.
	 * startIndex, the start index of the move.
	 * endIndex, the end index of the move.
	 * return true if and only if all IDs are valid.
	 */
	private static boolean validateIDs(Board board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		// Check if end is clear
		if (board.get(endIndex) != Board.EMPTY) {
			return false;
		}
		
		// Check if proper ID
		int id = board.get(startIndex);
		if ((isP1Turn && id != Board.BLACK_SOLDIER && id != Board.BLACK_QUEEN)
				|| (!isP1Turn && id != Board.WHITE_SOLDIER
				&& id != Board.WHITE_QUEEN)) {
			return false;
		}
		
		// Check the middle
		Point middle = Board.middle(startIndex, endIndex);
		int midID = board.get(Board.toIndex(middle));
		if (midID != Board.INVALID && ((!isP1Turn &&
				midID != Board.BLACK_SOLDIER && midID != Board.BLACK_QUEEN) ||
				(isP1Turn && midID != Board.WHITE_SOLDIER &&
				midID != Board.WHITE_QUEEN))) {
			return false;
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Checks that the move is diagonal and magnitude 1 or 2 in the correct
	 * direction. If the magnitude is not 2 (not a skip), it checks that
	 * no skips are available by other soldiers of the same player.
	 * board, the current board to check against.
	 * isP1Turn, the flag indicating if it is player 1's turn.
	 * startIndex, the start index of the move.
	 * endIndex, the end index of the move.
	 * return true if and only if the move distance is valid.
	 */
	private static boolean validateDistance(Board board, boolean isP1Turn,
			int startIndex, int endIndex) {
		
		// Check that it was a diagonal move
		Point start = Board.toPoint(startIndex);
		Point end = Board.toPoint(endIndex);
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}
		
		// Check that it was in the right direction
		int id = board.get(startIndex);
		if ((id == Board.WHITE_SOLDIER && dy > 0) ||
				(id == Board.BLACK_SOLDIER && dy < 0)) {
			return false;
		}
		
		// Check that if this is not a skip, there are none available
		Point middle = Board.middle(startIndex, endIndex);
		int midID = board.get(Board.toIndex(middle));
		if (midID < 0) {
			
			// Get the correct soldiers
			List<Point> soldiers;
			if (isP1Turn) {
				soldiers = board.find(Board.BLACK_SOLDIER);
				soldiers.addAll(board.find(Board.BLACK_QUEEN));
			} else {
				soldiers = board.find(Board.WHITE_SOLDIER);
				soldiers.addAll(board.find(Board.WHITE_QUEEN));
			}
			
			// Check if any of them have a skip available
			for (Point p : soldiers) {
				int index = Board.toIndex(p);
				if (!MoveMore.getSkips(board, index).isEmpty()) {
					return false;
				}
			}
		}
		
		// Passed all tests
		return true;
	}
	
	/**
	 * Checks if the specified soldier is safe (i.e. the opponent cannot skip the soldier).
	 * board, the current board state.
	 * soldier, the point where the test soldier is located at.
	 * return true if and only if the soldier at the point is safe.
	 */
	public static boolean isSafe(Board board, Point soldier) {
		
		// Trivial cases
		if (board == null || soldier == null) {
			return true;
		}
		int index = Board.toIndex(soldier);
		if (index < 0) {
			return true;
		}
		int id = board.get(index);
		if (id == Board.EMPTY) {
			return true;
		}
		
		// Determine if it can be skipped
		boolean isBlack = (id == Board.BLACK_SOLDIER || id == Board.BLACK_QUEEN);
		List<Point> check = new ArrayList<>();
		MoveMore.addPoints(check, soldier, Board.BLACK_QUEEN, 1);
		for (Point p : check) {
			int start = Board.toIndex(p);
			int tid = board.get(start);
			
			// Nothing here
			if (tid == Board.EMPTY || tid == Board.INVALID) {
				continue;
			}
			
			// Check ID
			boolean isWhite = (tid == Board.WHITE_SOLDIER ||
					tid == Board.WHITE_QUEEN);
			if (isBlack && !isWhite) {
				continue;
			}
			boolean isQueen = (tid == Board.BLACK_QUEEN || tid == Board.BLACK_QUEEN);
			
			// Determine if valid skip direction
			int dx = (soldier.x - p.x) * 2;
			int dy = (soldier.y - p.y) * 2;
			if (!isQueen && (isWhite ^ (dy < 0))) {
				continue;
			}
			int endIndex = Board.toIndex(new Point(p.x + dx, p.y + dy));
			if (MoveMore.isValidSkip(board, start, endIndex)) {
				return false;
			}
		}
		
		return true;
	}
}
