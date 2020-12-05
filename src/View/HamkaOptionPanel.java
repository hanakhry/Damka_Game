package View;

import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * This class provides a user interface component to control
 * options for the game of Hamka being played in the window.
 */
public class HamkaOptionPanel extends JPanel {


	/** The checkers window to update when an option is changed. */
	private HamkaWindow window;
	
	/** The button that when clicked, restarts the game. */
	private JButton restartBtn;
	/** The button that when clicked, saves the game. */
	private JButton saveBtn;
	/** The button that when clicked, pauses the game. */
	private JButton pauseBtn;
	/** The button that when clicked, Quit to Main Menu. */
	private JButton quitBtn;

	/**
	 * Creates a new option panel for the specified Hamka window.
	 */
	public HamkaOptionPanel(HamkaWindow window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components & Add listeners to them

		this.pauseBtn = new JButton("Pause");
		this.restartBtn = new JButton("Restart");
		this.saveBtn = new JButton("Save");
		this.quitBtn = new JButton("Quit Game");
		this.restartBtn.addActionListener(new HamkaOptionListener());
		this.quitBtn.addActionListener(new HamkaOptionListener());


		// Add components to the layout
		JPanel top2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middle2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		top2.add(new JLabel("Total Time:  "));
		top2.add(new JLabel(DateFormat.getDateTimeInstance().format(new Date())));
		top.add(pauseBtn);
		top.add(saveBtn);
		top.add(restartBtn);
		top.add(quitBtn);
		this.add(top2);
		this.add(top);

		middle.add(new JLabel("Black Player Score: "));
		this.add(middle);
		middle2.add(new JLabel("Black Player Time: "));
		this.add(middle2);
		bottom.add(new JLabel("White Player Score: "));
		this.add(bottom);
		bottom2.add(new JLabel("White Player Time: "));
		this.add(bottom2);

	}

	public HamkaWindow getWindow() {
		return window;
	}

	public void setWindow(HamkaWindow window) {
		this.window = window;
	}

	
	/**
	 * Gets a new instance of the player

	 * @return a new instance of a Player
	 */
	private static Player getPlayer() {
		
		Player player = new Player("Player",0);
		return player;

	}



	private class HamkaOptionListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {

			// No window to update
			if (window == null) {
				return;
			}
			Object src = e.getSource();
			// Handle the user action
			if (src == restartBtn) window.restart();
				if(src==quitBtn){
					window.dispose();
					MainMenu window = new MainMenu();
					window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
					window.setVisible(true);
				}

			}
		}
	}


