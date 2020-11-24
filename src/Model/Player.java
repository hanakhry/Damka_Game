package Model;

import java.sql.Time;

/**
 * The {@code Player} class is an abstract class that represents a player in a
 * game of checkers.
 */
public class Player {

	public String pUsername;
	public int pScore;
	public Time pTime;


	/**
	 * Determines how the game is updated. If true, the user must interact with
	 * the user interface to make a move. Otherwise, the game is updated via
	 * {@link #updateGame(Game)}.
	 * 
	 * @return true if this player represents a active user.
	 */
	public boolean isActive() {
		return true;
	};
	
	/**
	 * Updates the game state to take a move for the current player. If there
	 * is a move available that is multiple skips, it may be performed at once
	 * by this method or one skip at a time.
	 * 
	 * @param game	the game to update.
	 */
	public void updateGame(Game game) {}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[isActive=" + isActive() + "]";
	}
}
