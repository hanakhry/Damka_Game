/*
 * RULES
 * >>>>>>>>>>>>>
 * 1.	Soldier can only move diagonally, on dark tiles.
 * 
 * 2.	Normal Soldier can only move forward diagonally (for black Soldier,
 * 		this is down and for white Soldier, this is up).
 * 
 * 3.	A Soldier becomes a king when it reaches the opponents end and cannot
 * 		move forward anymore.
 * 
 * 4.	Once a Soldier becomes a king, the player's turn is over.
 * 
 * 5.	After a Soldier/king moves one space diagonally, the player's turn is
 * 		over.
 * 
 * 6.	If an opponent's Soldier/king can be skipped, it must be skipped.
 * 
 * 7.	If after a skip, the same Soldier can skip again, it must. Otherwise,
 * 		the turn is over.
 * 
 * 8.	The game is over if a player either has no more Soldiers or cannot make
 * 		a move on their turn.
 * 
 * 9.	The player with the black Soldiers moves first.
 */
package View;
import javax.swing.UIManager;

public class hDashboard {

	public static void main(String[] args) {
		
		//Set the look and feel to the OS look and feel
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create a window to display the checkers game
		HamkaWindow window = new HamkaWindow();
		window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
