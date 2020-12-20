/**
 * The Board class represents a game state for soldiers. A standard
 * soldier board is 8 x 8 (64) tiles, alternating white/black. soldiers are
 * only allowed on black tiles and can therefore only move diagonally. The
 * board is optimized to use as little memory space as possible and only uses
 * 3 integers to represent the state of the board (3 bits for each of the 32
 * tiles). This makes it fast and efficient to the board state.
 *
 * This class uses integers to represent the state of each tile and
 * specifically uses these constants for IDs: Empty,
 * Black_Soldier, White_Soldier, Black_Queen, White_Queen.
 *
 * Tile states can be retrieved through get(int) and get(int,int).
 * And set through set(int,int) , set(int,int,int).
 * Game can be reset through reset().
 */
package Model;


import Utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class Board {
	


	/** The current state of the board, represented as three integers. */
	private int[] state;



	/**
	 * Constructs a new Hamka game board, pre-filled with a new game state.
	 */

	public  Board(List tiles){
		//TODO
//		ArrayList<String>tile=new ArrayList<>();
//		int counter=0;
//		for(int i=0;i<72;i++){
//			counter++;
//			tile.add(0,"[");
//			tile.add(72,"]");
//			if(i%8==0){
//				tile.add(i,"\n");
//			}
//			if(i%2==0){
//				tile.add(i,",");
//			}
//			tile.add(i,String.valueOf( tiles.get(i)));
//
//		}

	}
	/**
	 * Creates an exact copy of the board. Any changes made to the copy will
	 * not affect the current object.
	 * return a copy of this Hamka board.
	 */
	public Board() {
		reset();
	}

	public Board copy() {
		Board copy = new Board();
		copy.state = state.clone();
		return copy;
	}
	/**
	 * Resets the Hamka board to the original game state with black soldiers
	 * on top and white on the bottom. There are both 12 black soldiers and 12
	 * white soldiers.
	 */
	public void reset() {
		// Reset the state
		this.state = new int[3];
		for (int i = 0; i < 12; i ++) {
			set(i, Constants.BLACK_SOLDIER);
			set(31 - i, Constants.WHITE_SOLDIER);
		}
	}
	
	/**
	 * Searches through the Hamka board and finds black tiles that match the
	 * specified ID.
	 * id = the ID to search for.
	 * Return a list of points on the board with the specified ID. If none
	 * exist, an empty list is returned.
	 */
	public List<Point> find(int id) {
		// Find all black tiles with matching IDs
		List<Point> points = new ArrayList<>();
		for (int i = 0; i < 32; i ++) {
			if (get(i) == id) {
				points.add(toPoint(i));
			}
		}
		
		return points;
	}

	/**
	 * Sets the ID of a black tile on the board at the specified location.
	 * If the location is not a black tile, nothing is updated. If the ID is
	 * less than 0, the board at the location will be set to EMPTY.
	 * index the index of the black tile (from 0 to 31 inclusive).
	 * ID, the new ID to set the black tile to.
	 */
	public void set(int index, int id) {
		
		// Out of range
		if (!isValidIndex(index)) {
			return;
		}
		
		// Invalid ID, so just set to EMPTY
		if (id < 0) {
			id = Constants.EMPTY;
		}
		
		// Set the state bits
		for (int i = 0; i < state.length; i ++) {
			boolean set = ((1 << (state.length - i - 1)) & id) != 0;
			this.state[i] = setBit(state[i], index, set);
		}
	}
	/**
	 * Sets the ID of a black tile on the board at the specified location.
	 * If the location is not a black tile, nothing is updated. If the ID is
	 * less than 0, the board at the location will be set to empty.
	 * x , the x-coordinate on the board (from 0 to 7 inclusive).
	 * y , the y-coordinate on the board (from 0 to 7 inclusive).
	 * id, the new ID to set the black tile to.
	 */
	public void set(int x, int y, int id) {
		set(toIndex(x, y), id);
	}
	
	/**
	 * Gets the ID corresponding to the specified point on the Hamka board.
	 * 
	 * x, the x-coordinate on the board (from 0 to 7 inclusive).
	 * y, the y-coordinate on the board (from 0 to 7 inclusive).
	 * return the ID at the specified location or INVALID if the
	 * location is not on the board or the location is a white tile.
	 */
	public int get(int x, int y) {
		return get(toIndex(x, y));
	}
	/**
	 * Gets the ID corresponding to the specified point on the Hamka board.
	 * 
	 * parameter Index, the index of the black tile (from 0 to 31 inclusive).
	 * Return the ID at the specified location or INVALID if the
	 * location is not on the board.
	 */
	public int get(int index) {
		if (!isValidIndex(index)) {
			return Constants.INVALID;
		}
		return getBit(state[0], index) * 4 + getBit(state[1], index) * 2
				+ getBit(state[2], index);
	}
	
	/**
	 * Converts a black tile index (0 to 31 inclusive) to an (x, y) point, such
	 * that index 0 is (1, 0), index 1 is (3, 0), ... index 31 is (7, 7).
	 * Parameter index , the index of the black tile to convert to a point.
	 * Return the (x, y) point corresponding to the black tile index or the
	 * point (-1, -1) if the index is not between 0 - 31 .
	 */
	public static Point toPoint(int index) {
		int y = index / 4;
		int x = 2 * (index % 4) + (y + 1) % 2;
		return !isValidIndex(index)? new Point(-1, -1) : new Point(x, y);
	}
	
	/**
	 * Converts a point to an index of a black tile on the Hamka board, such
	 * that (1, 0) is index 0, (3, 0) is index 1, ... (7, 7) is index 31.
	 * x, the x-coordinate on the board (from 0 to 7 inclusive).
	 * y, the y-coordinate on the board (from 0 to 7 inclusive).
	 * return the index of the black tile or -1 if the point is not a black
	 * tile.
	 */
	public static int toIndex(int x, int y) {
		// Invalid (x, y) (not in board, or white tile)
		if (!isValidPoint(new Point(x, y))) {
			return -1;
		}
		
		return y * 4 + x / 2;
	}
	
	/**
	 * Converts a point to an index of a black tile on the Hamka board, such
	 * that (1, 0) is index 0, (3, 0) is index 1, ... (7, 7) is index 31.
	 * p, the point to convert to an index.
	 * return the index of the black tile or -1 if the point is not a black tile.
	 */

	public static int toIndex(Point p) {

		return (p == null)? -1 : toIndex(p.x, p.y);
	}

	/**
	 * Sets or clears the specified bit in the target value and returns
	 * the updated value.
	 * Parameter target, the target value to update.
	 * Parameter bit, the bit to update (from 0 to 31 inclusive).
	 * Parameter set, true to set the bit, false to clear the bit.
	 * return the updated target value with the bit set or cleared.
	 */
	public static int setBit(int target, int bit, boolean set) {
		
		// Nothing to do
		if (bit < 0 || bit > 31) {
			return target;
		}
		
		// Set the bit
		if (set) {
			target |= (1 << bit);
		}
		
		// Clear the bit
		else {
			target &= (~(1 << bit));
		}
		
		return target;
	}
	
	/**
	 * Gets the state of a bit and determines if it is set (1) or not (0).
	 * Parameter target, the target value to get the bit from.
	 * Parameter bit, the bit to get (from 0 to 31 inclusive).
	 * return 1 if and only if the specified bit is set, 0 otherwise.
	 */
	public static int getBit(int target, int bit) {
		
		// Out of range
		if (bit < 0 || bit > 31) {
			return 0;
		}
		
		return (target & (1 << bit)) != 0? 1 : 0;
	}
	
	/**
	 * Gets the middle point on the Hamka board between two points.
	 * p1 the first point of a black tile on the Hamka board.
	 * p2 the second point of a black tile on the Hamka board.
	 * return the middle point between two points or (-1, -1) if the points
	 * are not on the board, are not distance 2 from each other in x and y,or are on a white tile.
	 */
	public static Point middle(Point p1, Point p2) {
		
		// A point isn't initialized
		if (p1 == null || p2 == null) {
			return new Point(-1, -1);
		}
		
		return middle(p1.x, p1.y, p2.x, p2.y);
	}
	
	/**
	 * Gets the middle point on the Hamka board between two points.
	 * index1 the index of the first point (from 0 to 31 inclusive).
	 * index2 the index of the second point (from 0 to 31 inclusive).
	 * return the middle point between two points or (-1, -1) if the points
	 * are not on the board, are not distance 2 from each other in x and y,or are on a white tile.
	 */
	public static Point middle(int index1, int index2) {

		return middle(toPoint(index1), toPoint(index2));
	}
	
	/**
	 * Gets the middle point on the Hamka board between two points.
	 * x1 the x-coordinate of the first point.
	 * y1 the y-coordinate of the first point.
	 * x2 the x-coordinate of the second point.
	 * y2 the y-coordinate of the second point.
	 * return the middle point between two points or (-1, -1) if the points
	 * are not on the board, are not distance 2 from each other in x and y,or are on a white tile.
	 */
	public static Point middle(int x1, int y1, int x2, int y2) {
		
		// Check coordinates
		int dx = x2 - x1, dy = y2 - y1;
		if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || // Not in the board
				x1 > 7 || y1 > 7 || x2 > 7 || y2 > 7) {
			return new Point(-1, -1);
		} else if (x1 % 2 == y1 % 2 || x2 % 2 == y2 % 2) { // white tile
			return new Point(-1, -1);
		} else if (Math.abs(dx) != Math.abs(dy) || Math.abs(dx) != 2) {
			return new Point(-1, -1);
		}
		
		return new Point(x1 + dx / 2, y1 + dy / 2);
	}
	
	/**
	 * Checks if an index corresponds to a black tile on the Hamka board.
	 * testIndex the index to check.
	 * return true if and only if the index is between 0 and 31 inclusive.
	 */
	public static boolean isValidIndex(int testIndex) {
		return testIndex >= 0 && testIndex < 32;
	}
	
	/**
	 * Checks if a point corresponds to a black tile on the Hamka board.
	 * testPoint the point to check.
	 * return true if and only if the point is on the board, specifically on a black tile.
	 */
	public static boolean isValidPoint(Point testPoint) {
		
		if (testPoint == null) {
			return false;
		}
		
		// Check that it is on the board
		int x = testPoint.x, y = testPoint.y;
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return false;
		}
		
		// Check that it is on a black tile
		if (x % 2 == y % 2) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		String obj = getClass().getName() + "[";
		for (int i = 0; i < 31; i ++) {
			obj += get(i) + ", ";
		}
		obj += get(31);
		
		return obj + "]";
	}
}
