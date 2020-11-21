package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.Player;

/**
 * The {@code OptionPanel} class provides a user interface component to control
 * options for the game of checkers being played in the window.
 */
public class OptionPanel extends JPanel {

	private static final long serialVersionUID = -4763875452164030755L;

	/** The checkers window to update when an option is changed. */
	private HamkaWindow window;
	
	/** The button that when clicked, restarts the game. */
	private JButton restartBtn;
	/** The button that when clicked, saves the game. */
	private JButton saveBtn;
	/** The button that when clicked, pauses the game. */
	private JButton pauseBtn;
	/** The combo box that changes what type of player player 1 is. */
	private JComboBox<String> player1Opts;

	
	/** The button to perform an action based on the type of player. */
	private JButton player1Btn;

	/** The combo box that changes what type of player player 2 is. */
	private JComboBox<String> player2Opts;

	
	/** The button to perform an action based on the type of player. */
	private JButton player2Btn;
	
	/**
	 * Creates a new option panel for the specified checkers window.
	 * 
	 * @param window	the window with the game of checkers to update.
	 */
	public OptionPanel(HamkaWindow window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components
		OptionListener ol = new OptionListener();
		final String[] playerTypeOpts = {"Human", "Computer"};
		this.pauseBtn = new JButton("Pause");
		this.restartBtn = new JButton("Restart");
		this.saveBtn = new JButton("Save");

		this.player1Opts = new JComboBox<>(playerTypeOpts);
		this.player2Opts = new JComboBox<>(playerTypeOpts);
		this.restartBtn.addActionListener(ol);
		this.player1Opts.addActionListener(ol);
		this.player2Opts.addActionListener(ol);

		JPanel top2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middle2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		this.player1Btn = new JButton("Set Connection");
		this.player1Btn.addActionListener(ol);
		this.player1Btn.setVisible(false);
		this.player2Btn = new JButton("Set Connection");
		this.player2Btn.addActionListener(ol);
		this.player2Btn.setVisible(false);
		
		// Add components to the layout
		top2.add(new JLabel("Total Time:  "));
		top.add(pauseBtn);
		top.add(saveBtn);
		top.add(restartBtn);
		this.add(top2);
		this.add(top);

		middle.add(new JLabel("(black) Player 1 Score: "));
		this.add(middle);
		middle2.add(new JLabel("(black) Player 1 Time: "));
		this.add(middle2);
		bottom.add(new JLabel("(white) Player 2 Score: "));
		this.add(bottom);
		bottom2.add(new JLabel("(white) Player 2 Time: "));
		this.add(bottom2);

	}

	public HamkaWindow getWindow() {
		return window;
	}

	public void setWindow(HamkaWindow window) {
		this.window = window;
	}

	
	/**
	 * Gets a new instance of the type of player selected for the specified
	 * combo box.
	 * 
	 * @param playerOpts	the combo box with the player options.
	 * @return a new instance of a {@link Model.Player} object that corresponds
	 * with the type of player selected.
	 */
	private static Player getPlayer(JComboBox<String> playerOpts) {
		
		Player player = new Player();
		if (playerOpts == null) {
			return player;
		}
		
		// Determine the type
		String type = "" + playerOpts.getSelectedItem();
		if (type.equals("Computer")) {
			player = new Player();
		}
		
		return player;
	}
	
	/**
	 * The {@code OptionListener} class responds to the components within the
	 * option panel when they are clicked/updated.
	 */

	private class OptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// No window to update
			if (window == null) {
				return;
			}
			
			Object src = e.getSource();

			// Handle the user action
			JButton btn = null;
			boolean isNetwork = false, isP1 = true;
			if (src == restartBtn) {
				window.restart();
			} else if (src == player1Opts) {
				Player player = getPlayer(player1Opts);
				window.setPlayer1(player);
				btn = player1Btn;
			} else if (src == player2Opts) {
				Player player = getPlayer(player2Opts);
				window.setPlayer2(player);
				btn = player2Btn;
				isP1 = false;
			}



		}
	}
}
