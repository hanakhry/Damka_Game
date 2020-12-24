package Model;

import Utils.Constants;

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
		if (board.get(endIndex) != Constants.EMPTY) {
			return false;
		}
		
		// Check if proper ID
		int id = board.get(startIndex);
		if ((isP1Turn && id != Constants.BLACK_SOLDIER && id != Constants.BLACK_QUEEN)
				|| (!isP1Turn && id != Constants.WHITE_SOLDIER
				&& id != Constants.WHITE_QUEEN)) {
			return false;
		}
		// Check the middle
		int black = Constants.BLACK_QUEEN;
		int white = Constants.WHITE_QUEEN;

		int onBoard = board.get(startIndex);
		if(onBoard != black && onBoard != white) {
			Point middle = Board.middle(startIndex, endIndex);
			int midID = board.get(Board.toIndex(middle));
			if (midID != Constants.INVALID && ((!isP1Turn &&
					midID != Constants.BLACK_SOLDIER && midID != Constants.BLACK_QUEEN) ||
					(isP1Turn && midID != Constants.WHITE_SOLDIER &&
							midID != Constants.WHITE_QUEEN))) {

				return false;
			}
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
		int black = Constants.BLACK_QUEEN;
		int white = Constants.WHITE_QUEEN;
		int blackS = Constants.BLACK_SOLDIER;
		int whiteS = Constants.WHITE_SOLDIER;

		int onBoard = board.get(startIndex);

		int px = -1;
		int py = -1;

		int dx = end.x - start.x;
		int dy = end.y - start.y;

		if(onBoard == black || onBoard == white){
			if(Math.abs(dx) != Math.abs(dy)) {
				return false;
			}
			if(Math.abs(dx) == Math.abs(dy) && Math.abs(dx) == 1) {
				return normalMove(board,dx, dy, startIndex, endIndex, isP1Turn);
			}

			if(dx > 0)
				px = 1;
			if(dy > 0)
				py = 1;

			int endX = end.x-px;
			int endY = end.y-py;
			int toEat = board.get(endX, endY);
			if(onBoard == black){
				if(toEat != white && toEat != whiteS)
					return false;
			}
			if(onBoard == white){
				if(toEat != black && toEat != blackS)
					return false;
			}
			int x = start.x;
			int y = start.y;
			x += px;
			y += py;
			while(x != endX){
				if(board.get(x,y) != 0) {
					return false;
				}
				x += px;
				y += py;
			}

			// Check that if this is not a skip, there are none available
			Point middle = Board.middle(startIndex, endIndex);
			int midID = board.get(Board.toIndex(middle));
			if (midID < 0) {
				// Get the correct soldiers
				List<Point> soldiers;
				if (isP1Turn) {
					soldiers = board.find(Constants.BLACK_SOLDIER);
					soldiers.addAll(board.find(Constants.BLACK_QUEEN));
				} else {
					soldiers = board.find(Constants.WHITE_SOLDIER);
					soldiers.addAll(board.find(Constants.WHITE_QUEEN));
				}
				// Check if any of them have a skip available
				for (Point p : soldiers) {
					int index = Board.toIndex(p);
					if (!getSkips(board, index).isEmpty()) {
						return false;
					}
				}

			}

		} else {
			return normalMove(board,dx, dy, startIndex, endIndex, isP1Turn);
		}
		
		// Passed all tests
		return true;
	}

	private static boolean normalMove(Board board, int dx, int dy, int startIndex, int endIndex, boolean isP1Turn){
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}

		// Check that it was in the right direction
		int id = board.get(startIndex);
		if ((id == Constants.WHITE_SOLDIER && dy > 0) ||
				(id == Constants.BLACK_SOLDIER && dy < 0)) {
			return false;
		}

		// Check that if this is not a skip, there are none available
		Point middle = Board.middle(startIndex, endIndex);
		int midID = board.get(Board.toIndex(middle));
		if (midID < 0) {

			// Get the correct soldiers
			List<Point> soldiers;
			if (isP1Turn) {
				soldiers = board.find(Constants.BLACK_SOLDIER);
				soldiers.addAll(board.find(Constants.BLACK_QUEEN));
			} else {
				soldiers = board.find(Constants.WHITE_SOLDIER);
				soldiers.addAll(board.find(Constants.WHITE_QUEEN));
			}

			// Check if any of them have a skip available
			for (Point p : soldiers) {
				int index = Board.toIndex(p);
				if (!getSkips(board, index).isEmpty()) {
					return false;
				}
			}
		}
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
		if (id == Constants.EMPTY) {
			return true;
		}
		
		// Determine if it can be skipped
		boolean isBlack = (id == Constants.BLACK_SOLDIER || id == Constants.BLACK_QUEEN);
		List<Point> check = new ArrayList<>();
		addPoints(check, soldier, Constants.BLACK_QUEEN, 1);
		for (Point p : check) {
			int start = Board.toIndex(p);
			int tid = board.get(start);
			
			// Nothing here
			if (tid == Constants.EMPTY || tid == Constants.INVALID) {
				continue;
			}
			
			// Check ID
			boolean isWhite = (tid == Constants.WHITE_SOLDIER ||
					tid == Constants.WHITE_QUEEN);
			if (isBlack && !isWhite) {
				continue;
			}
			boolean isQueen = (tid == Constants.BLACK_QUEEN || tid == Constants.BLACK_QUEEN);
			
			// Determine if valid skip direction
			int dx = (soldier.x - p.x) * 2;
			int dy = (soldier.y - p.y) * 2;
			if (!isQueen && (isWhite ^ (dy < 0))) {
				continue;
			}
			int endIndex = Board.toIndex(new Point(p.x + dx, p.y + dy));
			if (isValidSkip(board, start, endIndex)) {
				return false;
			}
		}
		
		return true;
	}
	/** MoveMore logic **/

	/**
	 * Gets a list of move end-points for a given start index.
	 * board, the board to look for available moves.
	 * start, the center index to look for moves around.
	 * return the list of points such that the start to a given point represents a move available.
	 */

	public static List<Point> getMoves(Board board, int startIndex) {

		// Trivial cases
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Board.isValidIndex(startIndex)) {
			return endPoints;
		}

		// Determine possible points
		int id = board.get(startIndex);
		Point p = Board.toPoint(startIndex);
		addPoints(endPoints, p, id, 1);

		// Remove invalid points
		for (int i = 0; i < endPoints.size(); i ++) {
			Point end = endPoints.get(i);
			if (board.get(end.x, end.y) != Constants.EMPTY) {
				endPoints.remove(i --);
			}
		}

		return endPoints;
	}


	/**
	 * Gets a list of skip end-points for a given starting point.
	 *
	 * board, the board to look for available skips.
	 * start, the center index to look for skips around.
	 * return the list of points such that the start to a given point represents a skip available.
	 */

	public static List<Point> getSkips(Board board, int startIndex) {

		// Trivial cases
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Board.isValidIndex(startIndex)) {
			return endPoints;
		}

		// Determine possible points
		int id = board.get(startIndex);
		Point p = Board.toPoint(startIndex);
		addPoints(endPoints, p, id, 2);

		// Remove invalid points
		for (int i = 0; i < endPoints.size(); i ++) {

			// Check that the skip is valid
			Point end = endPoints.get(i);
			if (!isValidSkip(board, startIndex, Board.toIndex(end))) {
				endPoints.remove(i --);
			}
		}

		return endPoints;
	}

	/**
	 * Checks if a skip is valid.
	 * board, the board to check against.
	 * startIndex, the start index of the skip.
	 * endIndex, the end index of the skip.
	 * return true if and only if the skip can be performed.
	 */

	public static boolean isValidSkip(Board board,
									  int startIndex, int endIndex) {

		if (board == null) {
			return false;
		}

		// Check that end is empty
		if (board.get(endIndex) != Constants.EMPTY) {
			return false;
		}

		// Check that middle is enemy
		int id = board.get(startIndex);
		int midID = board.get(Board.toIndex(Board.middle(startIndex, endIndex)));
		if (id == Constants.INVALID || id == Constants.EMPTY) {
			return false;
		} else if (midID == Constants.INVALID || midID == Constants.EMPTY) {
			return false;
		} else if ((midID == Constants.BLACK_SOLDIER || midID == Constants.BLACK_QUEEN)
				^ (id == Constants.WHITE_SOLDIER || id == Constants.WHITE_QUEEN)) {
			return false;
		}

		return true;
	}

	/**
	 * Adds points that could potentially result in moves/skips.
	 * points, the list of points to add to.
	 * p, the center point.
	 * id, the ID at the center point.
	 * delta, the amount to add/subtract.
	 */
	public static void addPoints(List<Point> points, Point p, int id, int delta) {

		// Add points moving down
		boolean isQueen = (id == Constants.BLACK_QUEEN || id == Constants.WHITE_QUEEN);
		if (isQueen || id == Constants.BLACK_SOLDIER) {
			points.add(new Point(p.x + delta, p.y + delta));
			points.add(new Point(p.x - delta, p.y + delta));
		}

		// Add points moving up
		if (isQueen || id == Constants.WHITE_SOLDIER) {
			points.add(new Point(p.x + delta, p.y - delta));
			points.add(new Point(p.x - delta, p.y - delta));
		}
	}


}
