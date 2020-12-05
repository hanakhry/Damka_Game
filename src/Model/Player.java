package Model;

/**
 * The Player class represents a player in a game of checkers.
 */
public class Player {



	private String pUsername;
	private int pScore;


	public Player(String pUsername, int pScore) {
		this.pUsername = pUsername;
		this.pScore = pScore;

	}

	public void setpUsername(String pUsername) {
		this.pUsername = pUsername;
	}

	public void setpScore(int pScore) {
		this.pScore = pScore;
	}

	public String getpUsername() {
		return pUsername;
	}
	public int getpScore() {
		return pScore;
	}

	/**
	 * Determines how the game is updated. If true, the user must interact with
	 * the user interface to make a move. Otherwise, the game is updated via updateGame
	 * Return true if this player represents an active user.
	 */


	



}
