/*
 * RULES
 * >>>>>>>>>>>>>
 * 1.	Soldier can only move diagonally, on dark tiles.
 * 
 * 2.	Normal Soldier can only move forward diagonally (for black Soldier,
 * 		this is down and for white Soldier this is up).
 * 
 * 3.	A Soldier becomes a king when it reaches the opponents end and cannot
 * 		move forward anymore.
 * 
 * 4.	Once a Soldier becomes a king, the player's turn is over.
 * 
 * 5.	After a Soldier/king moves one space diagonally, the player's turn is
 * 		over.
 * 
 * 6.	If an opponent's Soldier/Queen can be eaten, it must be eaten.
 * 
 * 7.	If after eating, the same Soldier can eat again, it must. Otherwise,
 * 		the turn is over.
 * 
 * 8.	The game is over if a player either has no more Soldiers or cannot make
 * 		a move on their turn.
 * 
 * 9.	The player with the White Soldiers moves first.
 */
package Controller;

import View.MainMenu;
import javax.swing.*;
import java.io.IOException;

public class StartGame {

	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace(); }

		MainMenu window = new MainMenu();
		MainMenu.NewGame.addActionListener(new MMOptionListener(window,MainMenu.NewGame));
		MainMenu.ExitGame.addActionListener(new MMOptionListener(window,MainMenu.ExitGame));
		window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
		window.setVisible(true);

	}
}
