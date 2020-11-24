package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.Timer;
import Model.MoveMore;
import Model.Board;
import Model.Game;
import Model.Player;

/**
 * The {@code HamkaBoard} class is a graphical user interface component that
 * is capable of drawing any Hamka game state. It also handles player turns.
 * For human players, this means interacting with and selecting tiles on the
 * Hamka board. For non-human players, this means using the logic implemented
 * by the specified player object itself is used.
 */
public class HamkaBoard extends JButton {

	private static final long serialVersionUID = -6014690893709316364L;
	
	/** The amount of milliseconds before a computer player takes a move. */
	private static final int TIMER_DELAY = 1000;
	
	/** The number of pixels of padding between this component's border and the
	 * actual Hamka board that is drawn. */
	private static final int PADDING = 16;

	/** The game of Hamka that is being played on this component. */
	private Game game;
	
	/** The window containing this Hamka board UI component. */
	private HamkaWindow window;
	
	/** The player in control of the black soldiers. */
	private Player player1;
	
	/** The player in control of the white soldiers. */
	private Player player2;
	
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
	
	/** The timer to control how fast a computer player makes a move. */
	private Timer timer;
	
	public HamkaBoard(HamkaWindow window) {
		this(window, new Game(), null, null);
	}
	
	public HamkaBoard(HamkaWindow window, Game game,
					  Player player1, Player player2) {
		
		// Setup the component
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		
		// Setup the game
		this.game = (game == null)? new Game() : game;
		this.lightTile = Color.WHITE;
		this.darkTile = Color.BLACK;
		this.window = window;
		setPlayer1(player1);
		setPlayer2(player2);
	}
	
	/**
	 * Checks if the game is over and redraws the component graphics.
	 */
	public void update() {
		runPlayer();
		this.isGameOver = game.isGameOver();
		repaint();
	}
	
	private void runPlayer() {
		
		// Nothing to do
		Player player = getCurrentPlayer();
		if (player == null || player.isActive()) {
			return;
		}
		
		// Set a timer to run
		this.timer = new Timer(TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPlayer().updateGame(game);
				timer.stop();
				update();
			}
		});
		this.timer.start();
	}
	

	
	public synchronized boolean setGameState(boolean testValue,
			String newState, String expected) {
		
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
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Game game = this.game.copy();
		
		// Perform calculations
		final int BOX_PADDING = 4;
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
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
		
		// Highlight the selected tile if valid
		if (Board.isValidPoint(selected)) {
			g.setColor(selectionValid? Color.GREEN : Color.RED);
			g.fillRect(OFFSET_X + selected.x * BOX_SIZE,
					OFFSET_Y + selected.y * BOX_SIZE,
					BOX_SIZE, BOX_SIZE);

		}
		
		// Draw the soldiers
		Board b = game.getBoard();
		for (int y = 0; y < 8; y ++) {
			int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				int id = b.get(x, y);
				
				// Empty, just skip
				if (id == Board.EMPTY) {
					continue;
				}
				
				int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;
				
				// Black soldier
				if (id == Board.BLACK_SOLDIER) {
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
				else if (id == Board.BLACK_QUEEN) {
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
				else if (id == Board.WHITE_SOLDIER) {
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
				else if (id == Board.WHITE_QUEEN) {
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
				if (id == Board.BLACK_QUEEN || id == Board.WHITE_QUEEN) {
					g.setColor(new Color(255, 240,0));
					g.drawOval(cx - 1, cy - 2, SOLDIER_SIZE, SOLDIER_SIZE);
					g.drawOval(cx + 1, cy, SOLDIER_SIZE - 4, SOLDIER_SIZE - 4);
				}
			}
		}
		
		// Draw the player turn sign
		String msg = game.isP1Turn()? "Player 1's turn" : "Player 2's turn";
		int width = g.getFontMetrics().stringWidth(msg);
		Color back = game.isP1Turn()? Color.BLACK : Color.WHITE;
		Color front = game.isP1Turn()? Color.WHITE : Color.BLACK;
		g.setColor(back);
		g.fillRect(W / 2 - width / 2 - 5, OFFSET_Y + 8 * BOX_SIZE + 2,
				width + 10, 15);
		g.setColor(front);
		g.drawString(msg, W / 2 - width / 2, OFFSET_Y + 8 * BOX_SIZE + 2 + 11);
		
		// Draw a game over sign
		if (isGameOver) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			msg = "Game Over!";
			width = g.getFontMetrics().stringWidth(msg);
			g.setColor(new Color(240, 240, 255));
			g.fillRoundRect(W / 2 - width / 2 - 5,
					OFFSET_Y + BOX_SIZE * 4 - 16,
					width + 10, 30, 10, 10);
			g.setColor(Color.RED);
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

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = (player1 == null)? new Player() : player1;
		if (game.isP1Turn() && !this.player1.isActive()) {
			this.selected = null;
		}
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = (player2 == null)? new Player() : player2;
		if (!game.isP1Turn() && !this.player2.isActive()) {
			this.selected = null;
		}
	}
	
	public Player getCurrentPlayer() {
		return game.isP1Turn()? player1 : player2;
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
	 * Handles a click on this component at the specified point. If the current
	 * player is not human, this method does nothing. Otherwise, the selected
	 * point is updated and a move is attempted if the last click and this one
	 * both are on black tiles.
	 * 
	 * @param x	the x-coordinate of the click on this component.
	 * @param y	the y-coordinate of the click on this component.
	 */
	public void handleClick(int x, int y) {
		
		// The game is over or the current player isn't human
		if (isGameOver || !getCurrentPlayer().isActive()) {
			return;
		}
		
		Game copy = game.copy();
		
		// Determine what square (if any) was selected
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
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
			boolean updated = (move?
					setGameState(true, copy.getGameState(), expected) : false);

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
	 * 
	 * @param b			the current board.
	 * @param isP1Turn	the flag indicating if it is player 1's turn.
	 * @param selected	the point to test.
	 * @return true if and only if the selected point is a soldier that would
	 * be allowed to make a move in the current turn.
	 */
	private boolean isValidSelection(Board b, boolean isP1Turn, Point selected) {

		// Trivial cases
		int i = Board.toIndex(selected), id = b.get(i);
		if (id == Board.EMPTY || id == Board.INVALID) { // no soldier here
			return false;
		} else if(isP1Turn ^ (id == Board.BLACK_SOLDIER ||
				id == Board.BLACK_QUEEN)) { // wrong soldier
			return false;
		} else if (!MoveMore.getSkips(b, i).isEmpty()) { // skip available
			return true;
		} else if (MoveMore.getMoves(b, i).isEmpty()) { // no moves
			return false;
		}
		
		// Determine if there is a skip available for another soldier
		List<Point> points = b.find(
				isP1Turn? Board.BLACK_SOLDIER : Board.WHITE_SOLDIER);
		points.addAll(b.find(
				isP1Turn? Board.BLACK_QUEEN : Board.WHITE_QUEEN));
		for (Point p : points) {
			int soldier = Board.toIndex(p);
			if (soldier == i) {
				continue;
			}
			if (!MoveMore.getSkips(b, soldier).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * The {@code ClickListener} class is responsible for responding to click
	 * events on the Hamka board component. It uses the coordinates of the
	 * mouse relative to the location of the Hamka board component.
	 */


	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Get the new mouse coordinates and handle the click
			Point m = HamkaBoard.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y);
			}
		}
	}
}
