package View;


import Controller.RandomEvents;
import Model.Board;
import Model.Game;
import Model.MoveLogic;
import Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The HamkaBoard class is a graphical user interface component that
 * is capable of drawing any Hamka game state. It also handles player turns.
 * This means interacting with and selecting tiles on the Hamka board.
 */

public class HamkaBoard extends JButton {

	/** The game of Hamka that is being played on this component. */
	private Game game;
	
	/** The window containing this Hamka board UI component. */
	private HamkaWindow window;

	
	/** The last point that the current player selected on the Hamka board. */
	private Point selected;
	
	/** The flag to determine the colour of the selected tile. If the selection
	 * is valid, a green colour is used to highlight the tile. Otherwise, a red
	 * colour is used. */
	private boolean selectionValid;


	/** The colour of the light tiles (by default, this is white). */
	private Color lightTile;

	/** The colour of the dark tiles (by default, this is black). */
	private Color darkTile;
	
	/** A convenience flag to check if the game is over. */
	private boolean isGameOver;

	private static List<Point> yellowSquare;

	private static Point redSquare;

	public static Point greenSquare;

	private static boolean colorChange = true;


	public HamkaBoard(HamkaWindow window) {
		this(window, new Game());
	}
	
	public HamkaBoard(HamkaWindow window, Game game) {

		// Setup the component
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new HamkaClickListener());
		
		// Setup the game
		this.game = (game == null)? new Game() : game;
		this.lightTile = Color.WHITE;
		this.darkTile = Color.BLACK;
		this.window = window;

	}

	private void updateYellow(Graphics g, List<Point> yellowSquare, int OFFSET_X, int OFFSET_Y, int BOX_SIZE){
		for (int i = 0; i < 3; i++) {
			Point yellowPoint = yellowSquare.get(i);
			g.setColor(Color.yellow);
			g.fillRect(OFFSET_X + yellowPoint.x * BOX_SIZE +2, OFFSET_Y + yellowPoint.y * BOX_SIZE +2 , BOX_SIZE-4, BOX_SIZE-4);
		}
	}
	
	/**
	 * Checks if the game is over and redraws the component graphics.
	 */
	public void update() {
		this.isGameOver = game.isGameOver();
		yellowSquare = game.colors.get("yellow");
		if(!game.colors.get("red").isEmpty()) {
			redSquare = game.colors.get("red").get(game.colors.get("red").size() -1);
		}
		if(!game.colors.get("green").isEmpty()) {
			greenSquare = game.colors.get("green").get(game.colors.get("green").size() -1);
		}
		repaint();
	}
	

	
	public synchronized boolean setGameState(boolean testValue, String newState, String expected) {
		RandomEvents random = new RandomEvents(this.game.getBoard().find(0));
		yellowSquare = random.yellowEvents();
		redSquare = random.redEvents(this.game, this.game.isP1Turn(), this.game.getBoard().find(0));
		greenSquare = random.greenEvents(this.game, this.game.getBoard().find(0), redSquare);
		// Test the value if requested
		if (testValue && !game.getGameState().equals(expected)) {
			return false;
		}
		
		// Update the game state
		this.game.setGameState(newState);
		repaint();
		
		return true;
	}
	

	
	/**
	 * Draws the current Hamka game state.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Game game = this.game.copy();
		
		// Perform calculations
		final int BOX_PADDING = 4;
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * Constants.PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		final int SOLDIER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);
		
		// Draw Hamka board
		g.setColor(Color.BLACK);
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
		g.setColor(lightTile);
		g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		g.setColor(darkTile);
		for (int y = 0; y < 8; y ++) {
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
						BOX_SIZE, BOX_SIZE);
			}
		}

		//draw yellow squares
		try {
			updateYellow(g, yellowSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);

		} catch (NullPointerException e){
			yellowSquare = HamkaWindow.getStartingSquares();
			updateYellow(g,yellowSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);
		}

		//draw red square

		try {
			if(!redSquare.equals(new Point(0, 0))) {
				g.setColor(Color.red);
				g.fillRect(OFFSET_X + redSquare.x * BOX_SIZE+2, OFFSET_Y + redSquare.y * BOX_SIZE+2, BOX_SIZE - 4, BOX_SIZE - 4);
			}
		} catch (NullPointerException e) {
			redSquare = HamkaWindow.getStartingRed();
			if(!redSquare.equals(new Point(0, 0))) {
				g.fillRect(OFFSET_X + redSquare.x * BOX_SIZE +2, OFFSET_Y + redSquare.y * BOX_SIZE +2, BOX_SIZE - 4, BOX_SIZE - 4);
			}
		}

		//green square
		if(this.game.isGreen) {
			try {
				if (!greenSquare.equals(new Point(0, 0))) {
					g.setColor(Color.green);
					g.fillRect(OFFSET_X + greenSquare.x * BOX_SIZE+2, OFFSET_Y + greenSquare.y * BOX_SIZE+2, BOX_SIZE - 4, BOX_SIZE - 4);
				}
			} catch (NullPointerException e) {
				greenSquare = HamkaWindow.getStartingGreen();
				if (!greenSquare.equals(new Point(0, 0))) {
					g.fillRect(OFFSET_X + greenSquare.x * BOX_SIZE, OFFSET_Y + greenSquare.y * BOX_SIZE, BOX_SIZE - 1, BOX_SIZE - 1);
				}
			}

		}

		// Highlight the selected tile if valid
		if (Board.isValidPoint(selected) && colorChange) {
			g.setColor(selectionValid? Color.GREEN : Color.RED);
			g.drawRect(OFFSET_X + selected.x * BOX_SIZE - 1 , OFFSET_Y + selected.y * BOX_SIZE - 1 , BOX_SIZE +2, BOX_SIZE +2);

		}

		// Draw the soldiers
		Board b = game.getBoard();
		for (int y = 0; y < 8; y ++) {
			int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				int id = b.get(x, y);
				
				// Empty, just skip
				if (id == Constants.EMPTY) {
					continue;
				}
				
				int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;
				
				// Black soldier
				if (id == Constants.BLACK_SOLDIER) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
				}
				
				// Black queen
				else if (id == Constants.BLACK_QUEEN) {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.fillOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.drawOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.BLACK);
					g.fillOval(cx - 1, cy - 2, SOLDIER_SIZE, SOLDIER_SIZE);
				}
				
				// White soldier
				else if (id == Constants.WHITE_SOLDIER) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
				}
				
				// White queen
				else if (id == Constants.WHITE_QUEEN) {
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx + 1, cy + 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.LIGHT_GRAY);
					g.fillOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.DARK_GRAY);
					g.drawOval(cx, cy, SOLDIER_SIZE, SOLDIER_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(cx - 1, cy - 2, SOLDIER_SIZE, SOLDIER_SIZE);
				}
				
				// Any queen (add some extra highlights)
				if (id == Constants.BLACK_QUEEN || id == Constants.WHITE_QUEEN) {
					g.setColor(new Color(255, 240,0));
					g.drawOval(cx - 1, cy - 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.drawOval(cx + 1, cy, SOLDIER_SIZE - 4, SOLDIER_SIZE - 4);
				}

			}
		}
		
		// Draw the player turn sign
		
		String msg = game.isP1Turn()? getGame().getBlack1Player().getpUsername()+" Turn" : getGame().getWhite2Player().getpUsername()+" Turn";

		int width = g.getFontMetrics().stringWidth(msg);
		Color back = game.isP1Turn()? Color.BLACK : Color.WHITE;
		Color front = game.isP1Turn()? Color.WHITE : Color.BLACK;
		g.setColor(back);
		g.fillRect(W / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2, width + 10, 15);
		g.setColor(front);
		g.drawString(msg, W / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 11);
		
		// Draw a game over sign
		if (isGameOver) {
			getWindow().getOpts().cntd.pause();
			getWindow().getOpts().cntd2.pause();
			getWindow().getOpts().cntd3.pause();
			g.setFont(new Font("Arial", Font.BOLD, 20));
			if(getGame().getBlack1Player().getpScore() > getGame().getWhite2Player().getpScore())
			msg = getGame().getBlack1Player().getpUsername()+" Won!";
			else
				msg= getGame().getWhite2Player().getpUsername()+" Won!";
			width = g.getFontMetrics().stringWidth(msg);
			g.setColor(new Color(240, 240, 255));
			g.fillRoundRect(W / 2 - width / 2 - 5,
					OFFSET_Y + BOX_SIZE * 4 - 16,
					width + 10, 30, 10, 10);
			g.setColor(Color.GREEN);
			g.drawString(msg, W / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
		}
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = (game == null)? new Game() : game;
	}

	public HamkaWindow getWindow() {
		return window;
	}

	public void setWindow(HamkaWindow window) {
		this.window = window;
	}



	public Color getLightTile() {
		return lightTile;
	}

	public void setLightTile(Color lightTile) {
		this.lightTile = (lightTile == null)? Color.WHITE : lightTile;
	}

	public Color getDarkTile() {
		return darkTile;
	}

	public void setDarkTile(Color darkTile) {
		this.darkTile = (darkTile == null)? Color.BLACK : darkTile;
	}

	/**
	 * Handles a click on this component at the specified point.
	 * Parameter x the x-coordinate of the click on this component.
	 * Parameter y the y-coordinate of the click on this component.
	 */
	public void handleClick(int x, int y, int changeColor) {
		if (isGameOver) {
			return;
		}
		
		Game copy = game.copy();
		
		// Determine what square (if any) was selected
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * Constants.PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE;
		y = (y - OFFSET_Y) / BOX_SIZE;
		Point sel = new Point(x, y);
		
		// Determine if a move should be attempted
		if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
			boolean change = copy.isP1Turn();
			String expected = copy.getGameState();
			boolean move = copy.move(selected, sel);
			if(changeColor == 2)
				colorChange = true;
			else
				colorChange = false;
			boolean updated = (move? setGameState(true, copy.getGameState(), expected) : false);
			selected.setLocation(0, 0);
			change = (copy.isP1Turn() != change);
			this.selected = change? null : sel;
		} else {
			this.selected = sel;
		}
		
		// Check if the selection is valid
		this.selectionValid = isValidSelection(
				copy.getBoard(), copy.isP1Turn(), selected);
		update();
	}

	/**
	 * Checks if a selected point is valid in the context of the current
	 * player's turn.
	 * Return true if and only if the selected point is a soldier that would
	 * be allowed to make a move in the current turn.
	 */
	private boolean isValidSelection(Board b, boolean isP1Turn, Point selected) {

		// Trivial cases
		int i = Board.toIndex(selected), id = b.get(i);
		if (id == Constants.EMPTY || id == Constants.INVALID) { // no soldier here
			return false;
		} else if(isP1Turn ^ (id == Constants.BLACK_SOLDIER ||
				id == Constants.BLACK_QUEEN)) { // wrong soldier
			return false;
		} else if (!MoveLogic.getSkips(b, i).isEmpty()) { // skip available + add points to skip (player must skip)
			if(isP1Turn){
				game.getBlack1Player().setpScore(game.getBlack1Player().getpScore()+50);
			}
			if(!isP1Turn){
				game.getWhite2Player().setpScore(game.getWhite2Player().getpScore()+50);
			}
			return true;
		} else if (MoveLogic.getMoves(b, i).isEmpty()) { // no moves
			return false;
		}
		
		// Determine if there is a skip available for another soldier
		List<Point> points = b.find(isP1Turn? Constants.BLACK_SOLDIER : Constants.WHITE_SOLDIER);
		points.addAll(b.find(isP1Turn? Constants.BLACK_QUEEN : Constants.WHITE_QUEEN));
		for (Point p : points) {
			int soldier = Board.toIndex(p);
			if (soldier == i) {
				continue;
			}
			if (!MoveLogic.getSkips(b, soldier).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private class HamkaClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Get the new mouse coordinates and handle the click
			Point m = HamkaBoard.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y, 1);
				handleClick(m.x, m.y, 2);
			}
		}
	}
}
