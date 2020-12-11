package Model;

import Controller.RandomEvents;
import Utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code Game} class represents a game of Hamka and ensures that all
 * moves made are valid as per the rules of Hamka.
 */
public class Game {
	/** The ID for game.*/
	private int id;
	/** The current state of the Hamka board. */
	private Board board;

	/** The flag indicating if it is player 1's (Black) turn. */
	private boolean isP1Turn;

	/** The index of the last skip, to allow for multiple skips in a turn. */
	private int skipIndex;


	/** Players **/
	private Player black1Player;
	private Player white2Player;

	public List<Point> yellowSquares;
	public HashMap<String, List<Point>> colors;
	public List<Point> redSquare;
	public List<Point> greenSquare;
	public List<Point> orangeSquares;

	public boolean isGreen = false;

	public boolean isOrange = false;

	public Game() {
		this.black1Player=new Player("",0);
		this.white2Player=new Player("",0);
		this.colors = new HashMap<>();
		this.redSquare = new ArrayList<>();
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		restart();
	}

	public Game(String state) {
		setGameState(state);
	}

	public Game(int id,Board board, boolean isP1Turn, int skipIndex) {
		this.id=id;
		this.board = (board == null)? new Board() : board;
		this.isP1Turn = isP1Turn;
		this.skipIndex = skipIndex;
		this.colors = new HashMap<>();
		this.redSquare = new ArrayList<>();
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		this.black1Player=new Player("",0);
		this.white2Player=new Player("",0);
	}
	public Game(int id, ArrayList<Integer> tiles, boolean isP1Turn) {
		this.id = id;
		this.colors = new HashMap<>();
		this.redSquare = new ArrayList<>();
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		//TODO board cons from tiles
		this.board = new Board(tiles);
		this.isP1Turn = isP1Turn;
		this.black1Player=new Player("",0);
		this.white2Player=new Player("",0);
	}

	/**
	 * Creates a copy of this game such that any modifications made to one are
	 * not made to the other.
	 * return an exact copy of this game.
	 */
	public Game copy() {
		colors.put("yellow", this.yellowSquares);
		colors.put("red", this.redSquare);
		colors.put("green", this.greenSquare);
		colors.remove("orange");
		colors.put("orange", this.orangeSquares);
		Game g = new Game();
		g.board = board.copy();
		g.isP1Turn = isP1Turn;
		g.skipIndex = skipIndex;
		return g;
	}

	/**
	 * Resets the game of Hamka to the initial state.
	 */
	public void restart() {
		this.board = new Board();
		//White player starts
		this.isP1Turn = false;
		this.skipIndex = -1;
		this.isGreen = false;
		this.isOrange = false;
	}

	/**
	 * Attempts to make a move from the start point to the end point.
	 * parameter start, the start point for the move.
	 * parameter end, the end point for the move.
	 * return true if and only if an update was made to the game state.
	 */
	public boolean move(Point start, Point end) {
		if (start == null || end == null) {
			return false;
		}
		this.isGreen = false;
		this.isOrange = false;
		return move(Board.toIndex(start), Board.toIndex(end));
	}

	/**
	 * Attempts to make a move given the start and end index of the move.
	 * startIndex the start index of the move.
	 * endIndex the end index of the move.
	 * return true if and only if an update was made to the game state.
	 */
	public boolean move(int startIndex, int endIndex) {
		// Validate the move
		if (!MoveLogic.isValidMove(this, startIndex, endIndex)) {
			return false;
		}

		// Make the move
		Point middle = Board.middle(startIndex, endIndex);
		int midIndex = Board.toIndex(middle);
		this.board.set(endIndex, board.get(startIndex));
		this.board.set(midIndex, Constants.EMPTY);
		this.board.set(startIndex, Constants.EMPTY);

		// Make the soldier a queen if necessary
		Point end = Board.toPoint(endIndex);
		int id = board.get(endIndex);
		boolean switchTurn = false;
		if (end.y == 0 && id == Constants.WHITE_SOLDIER) {
			this.board.set(endIndex, Constants.WHITE_QUEEN);
			switchTurn = true;
		} else if (end.y == 7 && id == Constants.BLACK_SOLDIER) {
			this.board.set(endIndex, Constants.BLACK_QUEEN);
			switchTurn = true;
		}

		// Check if the turn should switch (i.e. no more skips)
		boolean midValid = Board.isValidIndex(midIndex);
		if (midValid) {
			this.skipIndex = endIndex;
		}
		if (!midValid || MoveLogic.getSkips(
				board.copy(), endIndex).isEmpty()) {
			switchTurn = true;
		}
		if (switchTurn) {
			this.isP1Turn = !isP1Turn;

			this.skipIndex = -1;
		}


		return true;
	}

	/**
	 * Gets a copy of the current board state.
	 * return a non-reference to the current game board state.
	 */
	public Board getBoard() {
		return board.copy();
	}

	/**
	 * Determines if the game is over. The game is over if one or both players
	 * cannot make a single move during their turn.
	 * return true if the game is over.
	 */
	public boolean isGameOver() {
		// Ensure there is at least one of each soldier
		List<Point> black = board.find(Constants.BLACK_SOLDIER);
		black.addAll(board.find(Constants.BLACK_QUEEN));
		if (black.isEmpty()) {
			return true;
		}
		List<Point> white = board.find(Constants.WHITE_SOLDIER);
		white.addAll(board.find(Constants.WHITE_QUEEN));
		if (white.isEmpty()) {
			return true;
		}

		// Check that the current player can move
		List<Point> test = isP1Turn? black : white;
		for (Point p : test) {
			int i = Board.toIndex(p);
			if (!MoveLogic.getMoves(board, i).isEmpty() ||
					!MoveLogic.getSkips(board, i).isEmpty()) {
				return false;
			}
		}

		// No moves
		return true;
	}

	public boolean isP1Turn() {
		return isP1Turn;
	}

	public boolean isGreen(){
		return isGreen;
	}

	public void setGreen(boolean flag){
		this.isGreen = flag;
	}

	public boolean isOrange(){
		return isOrange;
	}

	public void setOrange(boolean flag){
		this.isOrange = flag;
	}

	public void setP1Turn(boolean isP1Turn) {
		this.isP1Turn = isP1Turn;
	}

	public int getSkipIndex() {
		return skipIndex;
	}

	/**
	 * Gets the current game state as a string of data that can be parsed by setGameState(String)
	 * return a string representing the current game state.
	 */
	public String getGameState() {

		// Add the game board
		String state = "";
		for (int i = 0; i < 32; i ++) {
			state += "" + board.get(i);
		}
		// Add the other info
		state += (isP1Turn? "1" : "0");
		state += skipIndex;

		return state;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }


	public Player getBlack1Player() {
		return black1Player;
	}

	public Player getWhite2Player() {
		return white2Player;
	}

	public HashMap<String, List<Point>> getColors(){ return colors; }
	public void setColors(HashMap<String, List<Point>> colors){
		this.colors = colors;
		refreshColors();
	}
	/**
	 * Parses a string representing a game state that was generated from getGameState()
	 * parameter state the game state.
	 */
	public void setGameState(String state) {

		restart();

		// Trivial cases
		if (state == null || state.isEmpty()) {
			return;
		}

		// Update the board
		int n = state.length();
		for (int i = 0; i < 32 && i < n; i ++) {
			try {
				int id = Integer.parseInt("" + state.charAt(i));
				this.board.set(i, id);
			} catch (NumberFormatException e) {}
		}

		// Update the other info
		if (n > 32) {
			this.isP1Turn = (state.charAt(32) == '1');
		}
		if (n > 33) {
			try {
				this.skipIndex = Integer.parseInt(state.substring(33));
			} catch (NumberFormatException e) {
				this.skipIndex = -1;
			}
		}

		refreshColors();
	}

	private void refreshColors(){
		RandomEvents random = new RandomEvents(this.getBoard().find(0));
		yellowSquares = random.yellowEvents();
		Point redPoint = random.redEvents(this ,isP1Turn, yellowSquares);
		Point greenPoint = random.greenEvents(this, this.getBoard().find(0), redPoint);
		List<Point> orangePoints = random.orangeEvents(this, this.getBoard().find(0));

		if(redPoint != null)
			redSquare.add(redPoint);
		else
			redSquare.add(new Point(0, 0));

		if(greenPoint != null)
			greenSquare.add(greenPoint);
		else
			greenSquare.add(new Point(0, 0));

		if(orangePoints != null)
			orangeSquares.addAll(orangePoints);
		else
			orangeSquares.add(new Point(0, 0));


	}

}