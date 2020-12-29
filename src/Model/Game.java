package Model;

import Controller.RandomEvents;
import Utils.Constants;

import javax.swing.*;
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

	/** used to create event squares **/
	public List<Point> yellowSquares;
	public List<Point> greenSquare;
	public HashMap<String, List<Point>> colors;
	public List<Point> redSquare;
	public List<Point> orangeSquares;
	public List<Point> blueSquare;

	/** used to check if the players has stepped on an event square **/
	public static List<Point> tempYellow;
	public static Point tempGreen;
	public static Point tempRed;
	public boolean redSwitch = false;
	public static boolean tempIsOrange;
	public static boolean tempIsGreen;
	public static Point tempBlue;
	public static boolean isChangeBlue;


	/** keep track of timer to display **/
	public boolean isGreen;
	public boolean isOrange;

	private boolean queenSkip = false;


	/** **/

	public Game() {
		this.black1Player = new Player("",0);
		this.white2Player = new Player("",0);
		this.colors = new HashMap<>();
		this.yellowSquares = new ArrayList<>();
		this.redSquare = new ArrayList<>();
		this.blueSquare = new ArrayList<>();
		this.tempGreen = new Point();
		this.tempYellow = new ArrayList<>();
		this.tempRed = new Point();
		this.tempBlue = new Point();
		this.isOrange = false;
		this.isGreen = false;
		this.tempIsGreen = false;
		this.tempIsOrange = false;
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		restart();
	}

//	public Game(String state) {
//		setGameState(state);
//	}

//	public Game(Board board, boolean isP1Turn, int skipIndex) {
//		this.id=id;
//		this.board = (board == null)? new Board() : board;
//		this.isP1Turn = isP1Turn;
//		this.skipIndex = skipIndex;
//		this.colors = new HashMap<>();
//		this.redSquare = new ArrayList<>();
//		this.blueSquare = new ArrayList<>();
//		this.greenSquare = new ArrayList<>();
//		this.orangeSquares = new ArrayList<>();
//		this.yellowSquares = new ArrayList<>();
//		this.blueSquare = new ArrayList<>();
//		this.tempGreen = new Point();
//		this.tempRed = new Point();
//		this.tempBlue = new Point();
//		this.tempYellow = new ArrayList<>();
//		this.tempIsOrange = false;
//		this.tempIsGreen = false;
//		this.black1Player = new Player("",0);
//		this.white2Player = new Player("",0);
//	}
	public Game(boolean turn) {
		this.colors = new HashMap<>();
		this.redSquare = new ArrayList<>();
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		this.yellowSquares = new ArrayList<>();
		this.isOrange=false;
		this.isGreen=false;
		this.tempGreen = new Point();
		this.tempRed = new Point();
		this.tempBlue = new Point();
		this.tempYellow = new ArrayList<>();
		this.tempIsOrange = false;
		this.tempIsGreen = false;
		isP1Turn = turn;
		this.black1Player = new Player("",0);
		this.white2Player = new Player("",0);

	}

	public Game(ArrayList<Integer> tiles, boolean p1Turn) {
		this.colors = new HashMap<>();
		this.redSquare = new ArrayList<>();
		this.greenSquare = new ArrayList<>();
		this.orangeSquares = new ArrayList<>();
		this.yellowSquares = new ArrayList<>();
		this.isOrange=false;
		this.isGreen=false;
		this.tempGreen = new Point();
		this.tempRed = new Point();
		this.tempBlue = new Point();
		this.tempYellow = new ArrayList<>();
		this.tempIsOrange = false;
		this.tempIsGreen = false;
		isP1Turn = p1Turn;
		this.black1Player = new Player("",0);
		this.white2Player = new Player("",0);
	}

	/**
	 * Creates a copy of this game such that any modifications made to one are
	 * not made to the other.
	 * return an exact copy of this game.
	 */
	public Game copy(List<Point> yellowTemp, Point greenTemp, boolean isGreenTemp, Point redTemp, Point tempBlue) {
		colors.put("yellow", this.yellowSquares);
		colors.put("red", this.redSquare);
		colors.put("green", this.greenSquare);
		colors.remove("orange");
		colors.put("orange", this.orangeSquares);
		colors.put("blue", this.blueSquare);
		Game g = new Game();
		g.tempGreen = greenTemp;
		g.tempYellow = yellowTemp;
		g.tempIsGreen = isGreenTemp;
		g.tempRed = redTemp;
		g.tempBlue = tempBlue;
		g.board = board.copy();
		g.isP1Turn = isP1Turn;
		g.skipIndex = skipIndex;
		g.white2Player=white2Player;
		g.black1Player=black1Player;
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
	public boolean[] move(Point start, Point end) {
		boolean ret[] = new boolean[2];
	    if(!this.redSwitch) {
            if (start == null || end == null) {
            	ret[0] = false;
                return ret;
            }
            this.isGreen = false;
            this.isOrange = false;
			ret = move(Board.toIndex(start), Board.toIndex(end));
            return ret;
        }
		ret[0] = false;
		return ret;
	}

	/**
	 * Attempts to make a move given the start and end index of the move.
	 * startIndex the start index of the move.
	 * endIndex the end index of the move.
	 * return true if and only if an update was made to the game state.
	 */
	public boolean[] move(int startIndex, int endIndex) {
		boolean ret[] = new boolean[2];
		// Validate the move
		if (!MoveLogic.isValidMove(this, startIndex, endIndex)) {
			ret[0] = false;
			return ret;
		}

		// Make the move
		Point middle = Board.middle(startIndex, endIndex);
		int midIndex = Board.toIndex(middle);
		this.board.set(endIndex, board.get(startIndex));
		this.board.set(midIndex, Constants.EMPTY);
		this.board.set(startIndex, Constants.EMPTY);

		int px = -1;
		int py = -1;
		Point start = Board.toPoint(startIndex);
		Point end = Board.toPoint(endIndex);
		int dx = end.x - start.x;
		int dy = end.y - start.y;

		if(Math.abs(dx) > 1){
			if(dx > 0)
				px = 1;
			if(dy > 0)
				py = 1;
			int x = start.x;
			int y = start.y;
			x += px;
			y += py;
			int endX = end.x-px;
			while(x != endX){
				x += px;
				y += py;
			}
			System.out.println("qqq");
			this.board.set(x, y, 0);
			if(!MoveLogic.getSkips(board, endIndex).isEmpty())
				queenSkip = true;
		}
		// Make the soldier a queen if necessary
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
			if(!queenSkip) {
				switchTurn = true;
			} else{
				queenSkip = false;
			}
		}


		// give 50 for stepping on green
		int onGreen = Board.toIndex(tempGreen.x, tempGreen.y);
		if(endIndex == onGreen && this.tempIsGreen) {
			if (isP1Turn) this.black1Player.setpScore(this.black1Player.getpScore() + 50);
			if (!isP1Turn) this.white2Player.setpScore(this.white2Player.getpScore() + 50);
			final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));
			JOptionPane.showMessageDialog(null,
					"You got 50 points for stepping on Green!", "Green",
					JOptionPane.INFORMATION_MESSAGE,
					icon);
		}

		//handle stepping on red square
		int onRed = Board.toIndex(tempRed.x, tempRed.y);
		if(onRed == endIndex){
			redSwitch = true;
			final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon-red.png"));
			JOptionPane.showMessageDialog(null,
					"You got extra Move!", "Red",
					JOptionPane.INFORMATION_MESSAGE,
					icon);
		}


		//handle stepping on blue square
		int onBlue = Board.toIndex(tempBlue.x, tempBlue.y);
		if(onBlue == endIndex){
			isChangeBlue = true;
			final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon-blue.png"));
			JOptionPane.showMessageDialog(null,
					"You get to revive a soldier!", "Blue",
					JOptionPane.INFORMATION_MESSAGE,
					icon);
		}


		//handle question on yellow before switching player
		for(Point p : tempYellow){
			int onYellow = Board.toIndex(p.x, p.y);
			if(endIndex == onYellow){
				final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));

				SysData sysData = new SysData();
				int result=sysData.randomQuestionFromJSON("JSON/questions.JSON");
				if(isP1Turn)this.black1Player.setpScore(this.black1Player.getpScore()+result);
				if(!isP1Turn)this.white2Player.setpScore(this.white2Player.getpScore()+result);
				if(result>0)JOptionPane.showMessageDialog(null,
						"Correct Answer! You won "+result+" points.","Correct",
						JOptionPane.INFORMATION_MESSAGE,
						icon);
				else JOptionPane.showMessageDialog(null,
						"Wrong Answer! You lost "+result*-1+" points.",
						"Wrong",
						JOptionPane.ERROR_MESSAGE);
			}
		}


		if (switchTurn && !redSwitch && !isChangeBlue) {
			this.isP1Turn = !isP1Turn;
			this.skipIndex = -1;
		}
		ret[0] = true;
		if(redSwitch)
			ret[1] = true;
		return ret;
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
	public boolean getP1Turn() {
		return isP1Turn;
	}

	/**
	 * Gets the current game state as a string of data that can be parsed by setGameState(String)
	 * return a string representing the current game state.
	 */

	public String getGameState() {
		// Add the game board
		String state = "";
		for (int i = 0; i < 32; i ++) {
			state += ("" + board.get(i));
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

	public void refreshColors(){
		RandomEvents random = new RandomEvents(this.getBoard().find(0));
		yellowSquares = random.yellowEvents();

		Point redPoint = random.redEvents(this ,isP1Turn, yellowSquares);
		Point greenPoint = random.greenEvents(this, this.getBoard().find(0), redPoint);
		List<Point> orangePoints = random.orangeEvents(this, this.getBoard().find(0));
		List<Point> availableBlocks = this.getBoard().find(0);
		availableBlocks.removeAll(yellowSquares);
		availableBlocks.remove(redPoint);
		Point bluePoint;
		bluePoint = random.blueEvents(this, availableBlocks);

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

		if(bluePoint != null)
			blueSquare.add(bluePoint);
		else
			blueSquare.add(new Point(35, 35));

	}

}