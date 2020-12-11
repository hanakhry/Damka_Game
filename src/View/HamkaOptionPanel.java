package View;

import Controller.CountTimerScore;
import Model.SysData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	/**
	 * Creates a new option panel for the specified Hamka window.
	 */

	public HamkaOptionPanel(HamkaWindow window) {
		super(new GridLayout(0, 1));
		
		this.window = window;
		
		// Initialize the components & Add listeners to them

		this.pauseBtn = new JButton("Pause");
		this.resumeBtn = new JButton("Resume");
		this.restartBtn = new JButton("Restart");
		this.saveBtn = new JButton("Save");
		this.quitBtn = new JButton("Quit Game");
		this.showQuestion.addActionListener(new HamkaOptionListener());
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

		top.add(showQuestion);
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
			if (src == showQuestion) {
				SysData sysData = new SysData();
				String wholeQ=sysData.randomQuestionFromJSON("JSON/questions.JSON");
				String q=wholeQ.substring(wholeQ.indexOf("question=")+9,wholeQ.indexOf(","));
				String a=wholeQ.substring(wholeQ.indexOf("answers=[")+9,wholeQ.indexOf("]"));
				String l=wholeQ.substring(wholeQ.indexOf("level=")+6,wholeQ.indexOf(", answers"));
				String c=wholeQ.substring(wholeQ.indexOf("correct_ans=")+12,wholeQ.indexOf("}"));
				String[] sArray=a.split(", ");
				JList list = new JList(sArray);
				HamkaQuestion dialog = new HamkaQuestion("Random From JSON: "+q, list);
				dialog.show();
				System.out.println(list.getSelectedValue());


			}
			if (src == restartBtn) {
				scoreLabel.setText("0");
				scoreLabel2.setText("0");
				cntd.reset();
				cntd2.reset();
				cntd3.reset();
				window.restart();

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
			}
			if(src==resumeBtn){
				cntd.resume();
				cntd2.resume();
			}
			}
		}
	public HamkaWindow getWindow() {
		return window;
	}

}


