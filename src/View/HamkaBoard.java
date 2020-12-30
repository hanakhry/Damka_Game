package View;


import Controller.RandomEvents;
import Model.Board;
import Model.Game;
import Model.MoveLogic;
import Model.SysData;
import Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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
	public Point selected;
	
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

	public static Point blueSquare;

	private static boolean colorChange = true;

	private static List<Point> orangeSquare;

	public static Point saveRed = new Point(0, 0);

	private Point checkPoint = new Point(35, 35);

	private Point preserved;

	private boolean showColor = true;

	private boolean reviveClickFlag = false;

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
			g.fillRect(OFFSET_X + yellowPoint.x * BOX_SIZE +1, OFFSET_Y + yellowPoint.y * BOX_SIZE +1 , BOX_SIZE-2, BOX_SIZE-2);
		}
	}


	private void updateOrange(Graphics g, List<Point> orangeSquare, int OFFSET_X, int OFFSET_Y, int BOX_SIZE){
		for (int i = 0; i < orangeSquare.size(); i++) {
			Point orangePoint = orangeSquare.get(i);
			g.setColor(Color.orange);
			g.fillRect(OFFSET_X + orangePoint.x * BOX_SIZE +3, OFFSET_Y + orangePoint.y * BOX_SIZE +3 , BOX_SIZE-6, BOX_SIZE-6);
		}
	}

	/**
	 * Checks if the game is over and redraws the component graphics.
	 */
	public void update(boolean flag) {
		if(!flag) {
			this.isGameOver = game.isGameOver();
			yellowSquare = game.colors.get("yellow");
			if (!game.colors.get("red").isEmpty()) {
				redSquare = game.colors.get("red").get(game.colors.get("red").size() - 1);
			}
			if (!game.colors.get("green").isEmpty()) {
				greenSquare = game.colors.get("green").get(game.colors.get("green").size() - 1);
			}
			if (!game.colors.get("orange").isEmpty()) {
				orangeSquare = game.colors.get("orange");
			}
			if(!game.colors.get("blue").isEmpty()){
				blueSquare = game.colors.get("blue").get(game.colors.get("blue").size() - 1);
			}
		} else{
			HashMap<String, List<Point>> colors = new HashMap<String, List<Point>>();
			colors.put("green", new ArrayList<>());
			colors.put("red", new ArrayList<>());
			colors.put("orange", new ArrayList<>());
			colors.put("yellow", new ArrayList<>());
			colors.put("blue", new ArrayList<>());
			game.setColors(colors);
		}

		repaint();
	}
	

	
	public synchronized boolean setGameState(boolean testValue, String newState, String expected) {
		RandomEvents random = new RandomEvents(this.game.getBoard().find(0));
		yellowSquare = random.yellowEvents();
		redSquare = random.redEvents(this.game, this.game.isP1Turn(), yellowSquare);
		greenSquare = random.greenEvents(this.game, this.game.getBoard().find(0), redSquare);
		orangeSquare = random.orangeEvents(this.game, this.game.getBoard().find(0));

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
		Game game = this.game.copy(yellowSquare, greenSquare, this.game.isGreen, redSquare, blueSquare);


		// Perform calculations
		final int BOX_PADDING = 4;
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H ? W : H, BOX_SIZE = (DIM - 2 * Constants.PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		final int SOLDIER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

		// Draw Hamka board
		g.setColor(Color.BLACK);
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
		g.setColor(lightTile);
		g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		g.setColor(darkTile);
		for (int y = 0; y < 8; y++) {
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
						BOX_SIZE, BOX_SIZE);
			}
		}

		//draw yellow squares
		if(showColor){
		try {
			updateYellow(g, yellowSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);

		} catch (NullPointerException e) {
			yellowSquare = HamkaWindow.getStartingSquares();
			updateYellow(g, yellowSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);
		}

		//draw red square

		try {
			if (!redSquare.equals(new Point(0, 0))) {
				g.setColor(new Color(220,17,17));
				g.fillRect(OFFSET_X + redSquare.x * BOX_SIZE + 1, OFFSET_Y + redSquare.y * BOX_SIZE + 1, BOX_SIZE - 2, BOX_SIZE - 2);
			}
		} catch (NullPointerException e) {
			redSquare = HamkaWindow.getStartingRed();
			if (!redSquare.equals(new Point(0, 0))) {
				g.fillRect(OFFSET_X + redSquare.x * BOX_SIZE + 1, OFFSET_Y + redSquare.y * BOX_SIZE + 1, BOX_SIZE - 2, BOX_SIZE - 2);
			}
		}

		//green square
		if (this.game.isGreen) {
			try {
				if(greenSquare.equals(new Point(0, 0))){
					game.refreshColors();
				}

				g.setColor(Color.green);
				g.fillRect(OFFSET_X + greenSquare.x * BOX_SIZE + 3, OFFSET_Y + greenSquare.y * BOX_SIZE + 3, BOX_SIZE - 6, BOX_SIZE - 6);
			} catch (NullPointerException e) {
				greenSquare = HamkaWindow.getStartingGreen();
				if (!greenSquare.equals(new Point(0, 0))) {
					g.fillRect(OFFSET_X + greenSquare.x * BOX_SIZE + 3, OFFSET_Y + greenSquare.y * BOX_SIZE + 3, BOX_SIZE - 6, BOX_SIZE - 6);
				}
			}

		}

		//orange squares
		if (this.game.isOrange) {
			try {
				RandomEvents rnd = new RandomEvents(this.game.getBoard().find(0));
				orangeSquare = rnd.orangeEvents(this.game, this.game.getBoard().find(0));
				updateOrange(g, orangeSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);

			} catch (NullPointerException e) {
				orangeSquare = HamkaWindow.getStartingOrange();
				updateOrange(g, orangeSquare, OFFSET_X, OFFSET_Y, BOX_SIZE);
			}
		}

		//blue square
		if (!blueSquare.equals(checkPoint)) {
			try {
				g.setColor(new Color(30,144,255));
				g.fillRect(OFFSET_X + blueSquare.x * BOX_SIZE + 3, OFFSET_Y + blueSquare.y * BOX_SIZE + 3, BOX_SIZE - 6, BOX_SIZE - 6);
			} catch (NullPointerException e) {

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
			SysData sysdata=new SysData();
			try {
				sysdata.writeLeaderToFile("JSON/leaderhistory.JSON",getGame().getBlack1Player().getpUsername(),getGame().getBlack1Player().getpScore());
				sysdata.writeLeaderToFile("JSON/leaderhistory.JSON",getGame().getWhite2Player().getpUsername(),getGame().getWhite2Player().getpScore());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			getWindow().getOpts().cntd.pause();
			getWindow().getOpts().cntd2.pause();
			getWindow().getOpts().cntd3.pause();
			g.setFont(new Font("Arial", Font.BOLD, 20));
			if(getGame().getBlack1Player().getpScore() > getGame().getWhite2Player().getpScore())
			msg = getGame().getBlack1Player().getpUsername()+" Won!";
			else if (getGame().getBlack1Player().getpScore() < getGame().getWhite2Player().getpScore())
				     msg= getGame().getWhite2Player().getpUsername()+" Won!";
			else msg= "It's a tie!";
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

	private Point getPoint(int x, int y){
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H ? W : H, BOX_SIZE = (DIM - 2 * Constants.PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE;
		y = (y - OFFSET_Y) / BOX_SIZE;
		return new Point(x, y);
	}

	//all 4 *possiple* points around a point
	private List<Point> surround(Point p){
		List<Point> points = new ArrayList<>();
		Point p1 = new Point(p.x-1, p.y-1);
		Point p2 = new Point(p.x-1, p.y+1);
		Point p3 = new Point(p.x+1, p.y+1);
		Point p4 = new Point(p.x+1, p.y-1);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		return  points;
	}

	//checks that 4 points surrounding a point are safe
	private boolean areaCheck(Point p, Game game){
		int soldier = game.isP1Turn() ? Constants.WHITE_SOLDIER : Constants.BLACK_SOLDIER;
		int queen = game.isP1Turn() ? Constants.WHITE_QUEEN : Constants.BLACK_QUEEN;
		int check;


		List<Point> points = surround(p);
		check = game.getBoard().get(points.get(0).x, points.get(0).y);
		if(check == soldier || check == queen)
			return false;

		check = game.getBoard().get(points.get(1).x, points.get(1).y);
		if(check == soldier || check == queen)
			return false;

		check = game.getBoard().get(points.get(2).x, points.get(2).y);
		if(check == soldier || check == queen)
			return false;

		check = game.getBoard().get(points.get(3).x, points.get(3).y);
		if(check == soldier || check == queen)
			return false;

		return true;
	}

	//new string to "create" game scenario
	private String newString(Game game, Point p){
		String str = game.getGameState();
		String str1 = str.substring(0, game.getBoard().toIndex(p));
		String str2 = str.substring(game.getBoard().toIndex(p) + 1);
		int turn = game.isP1Turn() ? Constants.BLACK_SOLDIER : Constants.WHITE_SOLDIER;
		return str1 + turn + str2;
	}

	//new game for non existing scenario
	private Game newGame(Game game, Point p){
		Game g = new Game();
		g.setGameState(newString(game, p));
		g.setP1Turn(game.isP1Turn());
		return g;
	}

	//checks the surrounding of the surrounding of potential revived soldier
	private boolean firstCheck(List<Point> points, Game game){
		for(Point p : points){
			if(p.x < 8 && p.x > -1 && p.y < 8 && p.y > -1){
				Game g = newGame(game, p);
				if (!areaCheck(p, g))
					return false;
			}
		}
		return true;
	}


	/**
	 * Handles a click on this component at the specified point.
	 * Parameter x the x-coordinate of the click on this component.
	 * Parameter y the y-coordinate of the click on this component.
	 */
	public void handleClick(int x, int y, int changeColor) {
		if (isGameOver) {
			return;
		};
		Point p = getPoint(x, y);
		Game copy = game.copy(yellowSquare, greenSquare, game.isGreen, redSquare, blueSquare);
		if(copy.isChangeBlue){
			showColor = false;
			if(this.game.getBoard().get(p.x, p.y) == 0){
				Game g = newGame(copy, p);
				String str;
				List<Point> av = surround(p);
				reviveClickFlag = false;
				if(areaCheck(p, g)) {
					if (firstCheck(av, g)) {
						//create new soldier when stepping on blue
						str = newString(copy, p);
						game.setGameState(str);
						update(false);
						copy.isChangeBlue = false;
						game.setP1Turn(!copy.isP1Turn());
						showColor = true;
						handleClick(x, y, 2);
					} else{
						reviveClickFlag = true;
                        JOptionPane.showMessageDialog(null, "Invalid revive location.");
                    }
                    return;
				} else{
					reviveClickFlag = true;
					JOptionPane.showMessageDialog(null, "Invalid revive location.");
				}
				return;
			}
		}
		Point check = new Point(0, 0);
		// Determine what square (if any) was selected

		//check if red has been stepped on last turn
		if(saveRed.equals(check)){
			Point sel = getPoint(x, y);
			// Determine if a move should be attempted
			if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
				boolean change = copy.isP1Turn();
				String expected = copy.getGameState();
				boolean[] move = copy.move(selected, sel);
				if (move[1]) {
					saveRed = redSquare;
					preserved = getPoint(x, y);
				}
				if (changeColor == 2)
					colorChange = true;
				else
					colorChange = false;
				boolean updated = (move[0] ? setGameState(true, copy.getGameState(), expected) : false);
				selected.setLocation(0, 0);
				change = (copy.isP1Turn() != change);
				this.selected = change ? null : sel;

			} else {
				this.selected = sel;
			}
		} else {
			Point sel = getPoint(x, y);
			if (!MoveLogic.getMoves(this.game.getBoard(), Board.toIndex(preserved)).isEmpty()) {
				if (selected != null && sel != null) {
					if (selected.equals(saveRed) || sel.equals(saveRed)) {
						if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) { boolean change = copy.isP1Turn();
							String expected = copy.getGameState();
							boolean[] move = copy.move(selected, sel);
							if (move[0])
								saveRed = new Point(0, 0);
							if (changeColor == 2)
								colorChange = true;
							else
								colorChange = false;
							boolean updated = (move[0] ? setGameState(true, copy.getGameState(), expected) : false);
							selected.setLocation(0, 0);
							change = (copy.isP1Turn() != change);
							this.selected = change ? null : sel;
						}
					}
				}
			} else {
				if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
					boolean change = copy.isP1Turn();
					String expected = copy.getGameState();
					boolean[] move = copy.move(selected, sel);
					if (move[0])
						saveRed = new Point(0, 0);
					if (changeColor == 2)
						colorChange = true;
					else
						colorChange = false;
					boolean updated = (move[0] ? setGameState(true, copy.getGameState(), expected) : false);
					selected.setLocation(0, 0);
					change = (copy.isP1Turn() != change);
					this.selected = change ? null : sel;
				}
				JOptionPane.showMessageDialog(null, "Sadly your soldier can't move any further, turn skipped.");
				saveRed = new Point(0, 0);
				this.game.setP1Turn(!this.game.isP1Turn());
				handleClick(x, y, 0);
			}
		}


		// Check if the selection is valid
		this.selectionValid = isValidSelection(
				copy.getBoard(), copy.isP1Turn(), selected);
		update(false);
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
				//currently revive is not used
				if(!reviveClickFlag)
				    handleClick(m.x, m.y, 2);
			}
		}
	}
}
