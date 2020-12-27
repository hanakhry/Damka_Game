package View;

import Controller.CountTimerScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * This class provides a user interface component to control
 * options for the game of Hamka being played in the window.
 */
public class HamkaOptionPanel extends JPanel {




	/** The checkers window to update when an option is changed. */
	private HamkaWindow window;

	private JButton showQuestion=new JButton("Question");;
	/** The button that when clicked, restarts the game. */
	private JButton restartBtn;
	/** The button that when clicked, saves the game. */
	private JButton saveBtn;
	/** The button that when clicked, pauses the game. */
	private JButton pauseBtn;
	/** The button that when clicked, resumes the game. */
	private JButton resumeBtn;
	/** The button that when clicked, Quit to Main Menu. */
	private JButton quitBtn;
	/** Timers labels */
	private JLabel timeLabel=new JLabel();
	private JLabel scoreLabel=new JLabel();
	public CountTimerScore cntd=new CountTimerScore(this, timeLabel,scoreLabel,1);
	private JLabel timeLabel2=new JLabel();
	private JLabel scoreLabel2=new JLabel();
	public CountTimerScore cntd2=new CountTimerScore(this, timeLabel2,scoreLabel2,2);
	private JLabel timeLabel3=new JLabel();
	public CountTimerScore cntd3=new CountTimerScore(this, timeLabel3,null,3);
	public File[] files;



	/**
	 * Creates a new option panel for the specified Hamka window.
	 */

	public HamkaOptionPanel(HamkaWindow window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components & Add listeners to them
		//this.showQuestion=new JButton("Question");
		this.pauseBtn = new JButton("Pause");
		this.resumeBtn = new JButton("Resume");
		this.restartBtn = new JButton("Restart");
		this.saveBtn = new JButton("Save");
		this.quitBtn = new JButton("Quit Game");
		//this.showQuestion.addActionListener(new HamkaOptionListener());
		this.saveBtn.addActionListener(new HamkaOptionListener());
		this.restartBtn.addActionListener(new HamkaOptionListener());
		this.pauseBtn.addActionListener(new HamkaOptionListener());
		this.resumeBtn.addActionListener(new HamkaOptionListener());
		this.quitBtn.addActionListener(new HamkaOptionListener());


		// Add components to the layout
		JPanel top2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middle2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottom2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel tT=new JLabel("Total Time:  ");
		top2.add(tT);
		Font f = tT.getFont();
		tT.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		top2.add(tT);
		top2.add(timeLabel3);
		cntd3.start();

		//top.add(showQuestion);
		top.add(pauseBtn);
		top.add(resumeBtn);
		top.add(saveBtn);
		top.add(restartBtn);
		top.add(quitBtn);
		this.add(top2);
		this.add(top);

		JLabel bpS=new JLabel(window.getBoard().getGame().getBlack1Player().getpUsername()+" Score: ");
		f = bpS.getFont();
		bpS.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		middle.add(bpS);
		middle.add(scoreLabel);
		scoreLabel.setText(String.valueOf(0));
		this.add(middle);

		JLabel bpT=new JLabel(window.getBoard().getGame().getBlack1Player().getpUsername()+" Time: ");
		bpT.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		middle2.add(bpT);
		middle2.add(timeLabel);
		cntd.start();
		this.add(middle2);

		JLabel wpS=new JLabel(window.getBoard().getGame().getWhite2Player().getpUsername()+" Score: ");
		wpS.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		bottom.add(wpS);
		scoreLabel2.setText(String.valueOf(0));
		bottom.add(scoreLabel2);
		this.add(bottom);

		JLabel wpT=new JLabel(window.getBoard().getGame().getWhite2Player().getpUsername()+" Time: ");
		wpT.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		bottom2.add(wpT);
		bottom2.add(timeLabel2);
		cntd2.start();
		this.add(bottom2);


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
			/**
			if (src == showQuestion) {
				final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));

				SysData sysData = new SysData();
				int result=sysData.randomQuestionFromJSON("JSON/questions.JSON");
				System.out.println("\n Points Gained from Question: "+result);
                if(result>0)JOptionPane.showMessageDialog(null,
						"Correct Answer! You won "+result+" points.","Correct",
						JOptionPane.INFORMATION_MESSAGE,
						icon);
				else JOptionPane.showMessageDialog(null,
						"Wrong Answer! You lost "+result*-1+" points.",
						"Wrong",
						JOptionPane.ERROR_MESSAGE);

			}
			 */
			if (src == restartBtn) {
				if(window.getBoard().getGame().isGameOver()){
					String user1=window.getBoard().getGame().getBlack1Player().getpUsername();
					String user2=window.getBoard().getGame().getWhite2Player().getpUsername();
					window.dispose();
					HamkaWindow window = new HamkaWindow(user1,user2);
					window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
					window.setVisible(true);
					window.restart();
				}
				HamkaBoard.saveRed = new Point(0, 0);
				scoreLabel.setText("0");
				scoreLabel2.setText("0");
				cntd.reset();
				cntd2.reset();
				cntd3.reset();
				window.restart();

			}

			if(src==saveBtn){


				ArrayList<Integer> tiles=new ArrayList<>();
				try {
					files = countNumberOfTxtFile();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}

				HamkaBoard state=window.getBoard();
				for(int i=0;i<33;i++){
					int a=(state.getGame().getGameState().charAt(i));
					if(a== '6') a=2;
					if(a=='0') a=0;
					if(a=='4'||a=='1') a=1;
					if(a=='7') a=22;
					if(a=='5') a=11;
                   if(i!=32)
					tiles.add(i,a);
                   else {
                   	if(a==0){
                   		a='B';
                   		tiles.add(i,a);}
                   	else{ a='W';
						tiles.add(i,a);}
				   }
				}
				int i=1;

				//create the path of new txt file
				String user1=window.getBoard().getGame().getBlack1Player().getpUsername();
				String user2=window.getBoard().getGame().getWhite2Player().getpUsername();
				String path=("TEXT\\"+user1+".vs."+user2+".txt");

				boolean result=pathExist(path);
				//loop to change path if exist
				while(result==true){
					path=("TEXT\\"+user1+".vs."+user2+"("+i+")"+".txt");
					i++;
					result=pathExist(path);
				}
                     //create new file and write inside
				try {
					FileWriter myObj = new FileWriter(path);
					String a = String.valueOf(tiles);

					a=a.replace("87","B");
					a=a.replace("66","W");
					a = a.replace(" ", "");
					a = a.replace("[", "");
					a = a.replace("]", "");
					myObj.write(a);
					myObj.close();



				} catch (IOException c) {
					System.out.println("An error occurred.");
					c.printStackTrace();
				}

                path=path.replace(".txt","");
				path=path.replace("TEXT\\" , "");
				String msg="<html>Game Saved: <b>"+path+"</b></html>";
				JLabel label = new JLabel(msg);
				final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/v-icon.png"));
				JOptionPane.showMessageDialog(null,
						label, "Save game",
						JOptionPane.INFORMATION_MESSAGE,
						icon);
			}


			if(src==quitBtn){
					window.restart();
					window.dispose();
					window = null;
					MainMenu window = new MainMenu();
					window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
					window.setVisible(true);
				}
			if(src==pauseBtn){
				cntd.pause();
				cntd2.pause();
				cntd3.pause();
			}
			if(src==resumeBtn){
				cntd.resume();
				cntd2.resume();
				cntd3.resume();
			}
			}
		}
	public HamkaWindow getWindow() {
		return window;
	}
	//return all the txt file in TEXT directory
	public File[] countNumberOfTxtFile() throws IOException {
		File f = new File("TEXT");

		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		};

		File[] files = f.listFiles(textFilter);

		return files;
	}
	//check if path file exist in TEXT
	public  boolean pathExist(String path){
		File file = new File(path);
		if(file.exists())
		    return true;
		else
			return false;
	}


}


