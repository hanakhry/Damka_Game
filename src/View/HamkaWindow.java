package View;

import Model.Player;

import javax.swing.*;
import java.awt.*;


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
	
	/** The checker board component playing the updatable game. */
	private HamkaBoard board;
	
	private HamkaOptionPanel opts;
	

	
	public HamkaWindow() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
	}
	
	public HamkaWindow(Player player1, Player player2) {
		this();
		setPlayer1(player1);
		setPlayer2(player2);
	}
	
	public HamkaWindow(int width, int height, String title) {
		
		// Setup the window
		super(title);
		super.setSize(width, height);
		setLocationRelativeTo(null);

		
		// Setup the components
		JPanel layout = new JPanel(new BorderLayout());

		this.board = new HamkaBoard(this);
		this.opts = new HamkaOptionPanel(this);
		layout.add(board, BorderLayout.CENTER);
		layout.add(opts, BorderLayout.SOUTH);
		this.add(layout);
		

	}
	
	public HamkaBoard getBoard() {
		return board;
	}
	public HamkaOptionPanel getOpts() {
		return opts;
	}

	/**
	 * Updates the type of player that is being used for player 1.
	 * 
	 * @param player1	the new player instance to control player 1.
	 */
	public void setPlayer1(Player player1) {
		this.board.setPlayer1(player1);
		this.board.update();
	}
	
	/**
	 * Updates the type of player that is being used for player 2.
	 * 
	 * @param player2	the new player instance to control player 2.
	 */
	public void setPlayer2(Player player2) {
		this.board.setPlayer2(player2);
		this.board.update();
	}
	
	/**
	 * Resets the game of checkers in the window.
	 */
	public void restart() {

		this.board.getGame().restart();
		this.board.update();
		//to reset colors on board after restarting (clicking white tile)
		this.board.handleClick(0,0);


	}
	
	public void setGameState(String state) {
		this.board.getGame().setGameState(state);
	}

}
