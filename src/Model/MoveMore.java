package Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


/**
 * The MoveGenerator class provides a method for determining if a given
 * soldier can make any move or skip.
 */
public class MoveMore {

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
			if (board.get(end.x, end.y) != Board.EMPTY) {
				endPoints.remove(i --);
			}
		}
		
		return endPoints;
	}

	public static List<Point> getMoves(Board board, Point start) {
		return getMoves(board, Board.toIndex(start));
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

	public static List<Point> getSkips(Board board, Point start) {
		return getSkips(board, Board.toIndex(start));
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
		if (board.get(endIndex) != Board.EMPTY) {
			return false;
		}
		
		// Check that middle is enemy
		int id = board.get(startIndex);
		int midID = board.get(Board.toIndex(Board.middle(startIndex, endIndex)));
		if (id == Board.INVALID || id == Board.EMPTY) {
			return false;
		} else if (midID == Board.INVALID || midID == Board.EMPTY) {
			return false;
		} else if ((midID == Board.BLACK_SOLDIER || midID == Board.BLACK_QUEEN)
				^ (id == Board.WHITE_SOLDIER || id == Board.WHITE_QUEEN)) {
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
		boolean isQueen = (id == Board.BLACK_QUEEN || id == Board.WHITE_QUEEN);
		if (isQueen || id == Board.BLACK_SOLDIER) {
			points.add(new Point(p.x + delta, p.y + delta));
			points.add(new Point(p.x - delta, p.y + delta));
		}
		
		// Add points moving up
		if (isQueen || id == Board.WHITE_SOLDIER) {
			points.add(new Point(p.x + delta, p.y - delta));
			points.add(new Point(p.x - delta, p.y - delta));
		}
	}
}
