package View;

import Model.SysData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

public class MainMenu extends JFrame {
    /** The default width for the checkers window. */
    public static final int DEFAULT_WIDTH = 620;

    /** The default height for the checkers window. */
    public static final int DEFAULT_HEIGHT = 750;

    /** The default title for the checkers window. */
    public static final String DEFAULT_TITLE = "Hamka Hedgehog";

    private MainMenuCanvas mMC;



    private JButton NewGame;
    private JButton LoadGame;
    private JButton Questions;
    private JButton LeaderBoard;
    private JButton ExitGame;
    private JTextField user1 = new JTextField("Username 1");
    private JTextField user2 = new JTextField("Username 2");

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
        JPanel oPtsUsers= new JPanel();
        oPts.setLayout(new GridLayout(6, 1));
        oPtsUsers.setLayout(new GridLayout(0, 2));

        this.NewGame = new JButton("New Game");
        this.LoadGame = new JButton("Load Game");
        this.Questions = new JButton("Question Management");
        this.LeaderBoard = new JButton("Leader Board");
        this.ExitGame = new JButton("Exit");
        this.NewGame.addActionListener(new MMOptionListener());
        this.LeaderBoard.addActionListener(new MMOptionListener());
        this.LoadGame.addActionListener(new MMOptionListener());
        this.ExitGame.addActionListener(new MMOptionListener());
        this.Questions.addActionListener(new MMOptionListener());

        Font font1 = new Font(Font.DIALOG_INPUT ,  Font.ITALIC, 12);
        user1.setLocation(5, 5);
        user1.setSize(150,20);
        user1.setFont(font1);
        user1.setHorizontalAlignment(JTextField.CENTER);
        user2.setLocation(5, 5);
        user2.setSize(150,20);
        user2.setFont(font1);
        user2.setHorizontalAlignment(JTextField.CENTER);
        oPtsUsers.add(user1);
        oPtsUsers.add(user2);

        oPts.add(NewGame);
        oPts.add(oPtsUsers);
        oPts.add(LoadGame);
        oPts.add(Questions);
        oPts.add(LeaderBoard);
        oPts.add(ExitGame);

        layout.add(oPts);
        this.add(layout);


    }

    public class MMOptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Object src = e.getSource();
            // Handle the user action
            if (src == NewGame) {
                dispose();
                HamkaWindow window = new HamkaWindow(user1.getText(),user2.getText());
                window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
                window.setVisible(true);
                window.restart();
            }
            if(src==LoadGame){
                try {
//
                    new HamkaGameHistory();


                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
            if(src==LeaderBoard){
                dispose();

                SysData sysdata=new SysData();

                sysdata.readLeaderFromFile("JSON/leaderhistory.JSON");
                String[][] board=new String[25][2];
                int i=0;
                int j=0;

              for (Map.Entry<String, Integer> en : sysdata.sortByValue(sysdata.leaderboard).entrySet()) {
                    board[i][j]=en.getKey();
                    j++;
                    board[i][j]=Integer.toString(en.getValue());
                    i++;
                    j=0;
                    if(i==25)break;
                   // System.out.println("Key = " + en.getKey() +
                     //       ", Value = " + en.getValue());
                }

                LeaderBoard leaderB = new LeaderBoard(board);
                leaderB.setDefaultCloseOperation(leaderB.EXIT_ON_CLOSE);
                leaderB.setVisible(true);
            }
            if(src == ExitGame){
                dispose();
            }
            if(src == Questions){
                dispose();
                LogIn window = new LogIn();
                window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        }

    }
}




