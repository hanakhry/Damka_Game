package View;

import Controller.RandomEvents;
import Model.Game;
import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * This class is responsible for managing a window. This
 * window contains a game of Hamka and also options to save\pause\restart
 */

public class HamkaWindow extends JFrame {

	/** The default width for the checkers window. */
	public static final int DEFAULT_WIDTH = 620;

	/** The default height for the checkers window. */
	public static final int DEFAULT_HEIGHT = 750;

	/** The default title for the checkers window. */
	public static final String DEFAULT_TITLE = "Hamka Hedgehog";

	static RandomEvents random = new RandomEvents(new Game().getBoard().find(0));
	public static List<Point>  yellowSquare = random.yellowEvents();
	public static Point redSquare = random.redEvents(new Game(),new Game().isP1Turn(), yellowSquare);
	public static Point greenSquare = random.greenEvents(new Game(), new Game().getBoard().find(0), redSquare);
	public static Point purpleSquare = random.purpleEvents(new Game(), new Game().getBoard().find(0), redSquare);
	public static List<Point>  orangeSquares = random.orangeEvents(new Game(), new Game().getBoard().find(0));
	/** The checker board component playing the updatable game. */
	private HamkaBoard board;
	private HamkaOptionPanel opts;


	public HamkaWindow(String user1,String user2) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE,user1,user2);
	}

	public HamkaWindow(int width, int height, String title,String user1,String user2) {

		// Setup the window
		super(title);
		super.setSize(width, height);
		setLocationRelativeTo(null);


		// Setup the components
		JPanel layout = new JPanel(new BorderLayout());

		this.board = new HamkaBoard(this);
		getBoard().getGame().getBlack1Player().setpUsername(user1);
		getBoard().getGame().getWhite2Player().setpUsername(user2);
		this.opts = new HamkaOptionPanel(this);
		layout.add(board, BorderLayout.CENTER);
		layout.add(opts, BorderLayout.SOUTH);
		this.add(layout);
	}

	public static List<Point> getStartingSquares(){
		return yellowSquare;
	}
	public static Point getStartingRed(){
		return redSquare;
	}
	public static Point getStartingGreen(){
		return greenSquare;
	}
	public static Point getStartingPurple() { return purpleSquare; }
	public static List<Point> getStartingOrange(){ return orangeSquares; }
	public HamkaBoard getBoard() {
		return board;
	}
	public HamkaOptionPanel getOpts() {
		return opts;
	}

	public void restart() {
		this.board.getGame().restart();
		this.board.update(true);
		//to reset colors on board after restarting (clicking white tile)
		this.board.handleClick(0,0, 1);
	}

	public void setGameState(String state) {
		this.board.getGame().setGameState(state);
	}

}