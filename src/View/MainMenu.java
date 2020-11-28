package View;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    /** The default width for the checkers window. */
    public static final int DEFAULT_WIDTH = 620;

    /** The default height for the checkers window. */
    public static final int DEFAULT_HEIGHT = 750;

    /** The default title for the checkers window. */
    public static final String DEFAULT_TITLE = "Hamka Hedgehog";

    private MainMenuCanvas mMC;



    public static JButton NewGame;
    public static JButton LoadGame;
    public static JButton Questions;
    public static JButton LeaderBoard;
    public static JButton ExitGame;


    public MainMenu() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
    }


    public MainMenu(int width, int height, String title) {

        // Setup the window
        super(title);
        super.setSize(width, height);
        super.setLocationRelativeTo(null);

        // Setup the components
        JPanel layout = new JPanel();
        layout.setLayout(new GridLayout(2, 1));
        this.mMC = new MainMenuCanvas();

        layout.add(mMC);


        JPanel oPts= new JPanel();
        oPts.setLayout(new GridLayout(5, 1));
        this.NewGame = new JButton("New Game");
        this.LoadGame = new JButton("Load Game");
        this.Questions = new JButton("Question Management");
        this.LeaderBoard = new JButton("Leader Board");
        this.ExitGame = new JButton("Exit");


        oPts.add(NewGame);
        oPts.add(LoadGame);
        oPts.add(Questions);
        oPts.add(LeaderBoard);
        oPts.add(ExitGame);

        layout.add(oPts);
        this.add(layout);


    }


}
