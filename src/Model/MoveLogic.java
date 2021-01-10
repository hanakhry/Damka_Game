package Model;

import Utils.Constants;
import java.awt.*;
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
		return game == null? false : isValidMove(game ,game.getBoard(),
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
	public static boolean isValidMove(Game g, Board board, boolean isP1Turn,int startIndex, int endIndex, int skipIndex) {
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
		} else if (!validateDistance(g, board, isP1Turn, startIndex, endIndex)) {
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
	private static boolean validateIDs(Board board, boolean isP1Turn, int startIndex, int endIndex) {
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
	 * from startingIndex the method will move to all 4 possible close border sides, then decides where it will be if
	 * the queen moved outside the border
	 */
	private static Point[] getBorder(Game game, int startIndex, Board board, boolean isP1Turn, Point end){
		int soldier = isP1Turn ? Constants.WHITE_SOLDIER : Constants.BLACK_SOLDIER;
		int queen = isP1Turn ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;
		int tempX;
		int tempY;
		boolean skipFlag = false;
		Point point1 = null;
		Point point2 = null;
		Point point3 = null;
		Point point4 = null;
		Point start = Board.toPoint(startIndex);
		int x = start.x;
		int y = start.y;
		while(x < 8 && y < 8){
			x++;
			y++;
			if (board.get(x, y) != 0 && x < 8 && y < 8) {
				if(x == 7 || y == 7 && (board.get(x, y) == soldier || board.get(x, y) == queen)) {
					tempX = x;
					tempY = y;
					x--;
					y++;
					if(x == 8)
						x = 0;
					if(y == 8)
						y = 0;
					if (new Point(x, y).equals(end)) {
						game.eat = new Point(tempX, tempY);
						break;
					}
				}
				skipFlag = true;
				break;
			}
		}
		if(x == 8)
			x = 0;
		if(y == 8)
			y = 0;
		if(!skipFlag) {
			point1 = new Point(x, y);
		} else{
			skipFlag = false;
		}
		x = start.x;
		y = start.y;
		while ( x > -1 && y < 8) {
			x--;
			y++;
			if (board.get(x, y) != 0 && x > -1 && y < 8) {
				if(x == 0 || y == 7 && (board.get(x, y) == soldier || board.get(x, y) == queen)) {
					tempX = x;
					tempY = y;
					x--;
					y++;
					if(x == -1)
						x = 7;
					if(y == 8)
						y = 0;
					if (new Point(x, y).equals(end)) {
						game.eat = new Point(tempX, tempY);
						break;
					}
				}
				skipFlag = true;
				break;
			}
		}
		if(x == -1)
			x = 7;
		if(y == 8)
			y = 0;
		if(!skipFlag) {
			point2 = new Point(x, y);
		} else{
			skipFlag = false;
		}
		x = start.x;
		y = start.y;
		while ( x > -1 && y > -1) {
			x--;
			y--;
			if (board.get(x, y) != 0 && x > -1 && y > -1) {
				if(x == 0 || y == 0 && (board.get(x, y) == soldier || board.get(x, y) == queen)) {
					tempX = x;
					tempY = y;
					x--;
					y++;
					if(x == -1)
						x = 7;
					if(y == -1)
						y = 7;
					if (new Point(x, y).equals(end)) {
						game.eat = new Point(tempX, tempY);
						break;
					}
				}
				skipFlag = true;
				break;
			}
		}
		if(x == -1)
			x = 7;
		if(y == -1)
			y = 7;
		if(!skipFlag) {
			point3 = new Point(x, y);
		} else{
			skipFlag = false;
		}


		x = start.x;
		y = start.y;
		while ( x < 8 && y > -1) {
			x++;
			y--;
			if (board.get(x, y) != 0 && x < 8 && y > -1) {
				if(x == 7 || y == 0 && (board.get(x, y) == soldier || board.get(x, y) == queen)) {
					tempX = x;
					tempY = y;
					x--;
					y++;
					if(x == 8)
						x = 0;
					if(y == -1)
						y = 7;
					if (new Point(x, y).equals(end)) {
						game.eat = new Point(tempX, tempY);
						break;
					}
				}
				skipFlag = true;
				break;
			}
		}
		if(x == 8)
			x = 0;
		if(y == -1)
			y = 7;
		if(!skipFlag) {
			point4 = new Point(x, y);
		}
		Point points[] = new Point[4];
		points[0] = point1;
		points[1] = point2;
		points[2] = point3;
		points[3] = point4;
		return points;
	}
	/**
	 * the method will check if it's a valid move for the queen from point to end
	 * @param point new starting point on the board
	 * @param end end point to get to
	 * @param board
	 * @param endIndex
	 * @param movement determines in which direction the queen is moving
	 * @return true if it's a possible move
	 */
	private static boolean crossBoard(Point point, Point end, Board board, int endIndex, boolean isP1Turn, int movement){
		int soldier = isP1Turn ? Constants.WHITE_SOLDIER : Constants.BLACK_SOLDIER;
		int queen = isP1Turn ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;
		if(point.equals(end) && board.get(endIndex) == 0) {
			return true;
		}
		int px = -1;
		int py = -1;
		if(movement == 1){
			px = 1;
			py = 1;
		} else if(movement == 2){
			py = 1;
		}  else if(movement == 4){
			px = 1;
		}
		int x = point.x;
		int y = point.y;
		if(movement == 1){
			while (x < 7 && y < 7 && x != end.x) {
				if (board.get(x, y) != 0) {
					int onBoard = board.get(end.x-1, end.y-1);
					if((onBoard == soldier || onBoard == queen) && x == end.x-1) {
						return true;
					}
					return false;
				}
				x += px;
				y += py;
			}
		} else if(movement == 2){
			while (x > 0 && y < 7 && x != end.x) {
				if (board.get(x, y) != 0) {
					int onBoard = board.get(end.x+1, end.y-1);
					if((onBoard == soldier || onBoard == queen) && x == end.x+1) {
						return true;
					}
					return false;
				}
				x += px;
				y += py;
			}
		} else if(movement == 3){
			while (x > 0 && y > 0 && x != end.x) {
				if (board.get(x, y) != 0) {
					int onBoard = board.get(end.x+1, end.y+1);
					if((onBoard == soldier || onBoard == queen) && x == end.x+1) {
						return true;
					}
					return false;
				}
				x += px;
				y += py;
			}
		} else if(movement == 4){
			while (x < 7 && y > 0 && x != end.x) {
				if (board.get(x, y) != 0) {
					int onBoard = board.get(end.x-1, end.y+1);
					if((onBoard == soldier || onBoard == queen) && x == end.x-1) {
						return true;
					}
					return false;
				}
				x += px;
				y += py;
			}
		}

		if(point.equals(end))
			return true;
		if(Board.toIndex(x, y) != endIndex) {
			return false;
		}

		return true;
	}

	private static boolean middlePoint(Game game, int startIndex, int endIndex, Board board, boolean isP1Turn){
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
			for (Point point : soldiers) {
				int index = Board.toIndex(point);
				if (!getSkips(game, board, index).isEmpty()) {
					game.didntEat = point;
				}
			}
		}
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
	public static boolean validateDistance(Game game, Board board, boolean isP1Turn, int startIndex, int endIndex) {
		// Check that it was a diagonal move
		Point start = Board.toPoint(startIndex);
		Point end = Board.toPoint(endIndex);
		int black = Constants.BLACK_QUEEN;
		int white = Constants.WHITE_QUEEN;
		boolean flags[] = new boolean[4];
		int onBoard = board.get(startIndex);
		int px = -1;
		int py = -1;
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		//if selected is queen
		if(onBoard == black || onBoard == white){
			//not a diagonal move
			if(Math.abs(dx) != 1 || Math.abs(dy) != 1) {
				Point p[] = getBorder(game, startIndex, board, isP1Turn, end);
				for (int i = 0; i < 4; i++) {
					if (p[i] == null)
						flags[i] = false;
					else
						flags[i] = true;
				}
				//check all 4 possible points
				if (flags[0] && crossBoard(p[0], end, board, endIndex, isP1Turn, 1)) {
					return true;
				} else if (flags[1] && crossBoard(p[1], end, board, endIndex, isP1Turn, 2)) {
					return true;
				} else if (flags[2] && crossBoard(p[2], end, board, endIndex, isP1Turn, 3)) {
					return true;
				} else if (flags[3] && crossBoard(p[3], end, board, endIndex, isP1Turn, 4)) {
					return true;
				}
				//up until this point, all is well
			}
			if(Math.abs(dx) == Math.abs(dy) && Math.abs(dx) > 1){
				if (dx > 0)
					px = 1;
				if (dy > 0)
					py = 1;

				int endX = end.x - px;

				int x = start.x;
				int y = start.y;
				x += px;
				y += py;
				while (x != endX) {
					if (board.get(x, y) != 0) {
						return false;
					}
					x += px;
					y += py;
				}

				// Check that if this is not a skip, there are none available
				return middlePoint(game, startIndex, endIndex, board, isP1Turn);

			}
			else if (Math.abs(dx) == Math.abs(dy) && Math.abs(dx) == 1) {
				return middlePoint(game, startIndex, endIndex, board, isP1Turn);
			}
			return false;

		} else { //if not queen
			return normalMove(game, board, dx, dy, startIndex, endIndex, isP1Turn);
		}
		// Passed all tests

	}

	private static boolean normalMove(Game game, Board board, int dx, int dy, int startIndex, int endIndex, boolean isP1Turn){
		if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) > 2 || dx == 0) {
			return false;
		}

		// Check that it was in the right direction
		int id = board.get(startIndex);
		if(!game.chainEat) {
			if ((id == Constants.WHITE_SOLDIER && dy > 0) ||
					(id == Constants.BLACK_SOLDIER && dy < 0)) {
				return false;
			}
		} else if(Math.abs(dx) == 1 && Math.abs(dy) == 1){
			if ((id == Constants.WHITE_SOLDIER && dy > 0) ||
					(id == Constants.BLACK_SOLDIER && dy < 0)) {
				return false;
			}
		}

		// Check that if this is not a skip, there are none available
		boolean mp = middlePoint(game, startIndex, endIndex, board, isP1Turn);
		if(!mp)
			return false;
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
		addPoints(false, check, soldier, Constants.BLACK_QUEEN, 1);
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
		addPoints(false, endPoints, p, id, 1);

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

	public static List<Point> getSkips(Game game, Board board, int startIndex) {

		// Trivial cases
		List<Point> endPoints = new ArrayList<>();
		if (board == null || !Board.isValidIndex(startIndex)) {
			return endPoints;
		}

		// Determine possible points
		int id = board.get(startIndex);
		Point p = Board.toPoint(startIndex);
		addPoints(game.chainEat, endPoints, p, id, 2);

		// Remove invalid points
		if(!game.chainEat){
			for (int i = 0; i < endPoints.size(); i++) {
				// Check that the skip is valid
				Point end = endPoints.get(i);
				if (!isValidSkip(board, startIndex, Board.toIndex(end))) {
					endPoints.remove(i--);
				}
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

	public static boolean isValidSkip(Board board, int startIndex, int endIndex) {

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
	public static void addPoints(boolean eatChain, List<Point> points, Point p, int id, int delta) {

		// Add points moving down
		boolean isQueen = (id == Constants.BLACK_QUEEN || id == Constants.WHITE_QUEEN);
		if (isQueen || id == Constants.BLACK_SOLDIER) {
			points.add(new Point(p.x + delta, p.y + delta));
			points.add(new Point(p.x - delta, p.y + delta));
			if(eatChain){
				points.add(new Point(p.x + delta, p.y - delta));
				points.add(new Point(p.x - delta, p.y - delta));
			}
		}

		// Add points moving up
		if (isQueen || id == Constants.WHITE_SOLDIER) {
			points.add(new Point(p.x + delta, p.y - delta));
			points.add(new Point(p.x - delta, p.y - delta));
			if(eatChain){
				points.add(new Point(p.x + delta, p.y + delta));
				points.add(new Point(p.x - delta, p.y + delta));
			}
		}
	}


}