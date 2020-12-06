package View;

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
	public CountTimer cntd=new CountTimer(timeLabel,scoreLabel,1);
	private JLabel timeLabel2=new JLabel();
	private JLabel scoreLabel2=new JLabel();
	public CountTimer cntd2=new CountTimer(timeLabel2,scoreLabel2,2);
	private JLabel timeLabel3=new JLabel();
	public CountTimer cntd3=new CountTimer(timeLabel3,null,3);

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

		top.add(pauseBtn);
		top.add(resumeBtn);
		top.add(saveBtn);
		top.add(restartBtn);
		top.add(quitBtn);
		this.add(top2);
		this.add(top);

		JLabel bpS=new JLabel("Black Player Score: ");
		f = bpS.getFont();
		bpS.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		middle.add(bpS);
		middle.add(scoreLabel);
		scoreLabel.setText(String.valueOf(0));
		this.add(middle);

		JLabel bpT=new JLabel("Black Player Time: ");
		bpT.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		middle2.add(bpT);
		middle2.add(timeLabel);
		cntd.start();
		this.add(middle2);

		JLabel wpS=new JLabel("White Player Score: ");
		wpS.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		bottom.add(wpS);
		scoreLabel2.setText(String.valueOf(0));
		bottom.add(scoreLabel2);
		this.add(bottom);

		JLabel wpT=new JLabel("White Player Time: ");
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
			if (src == restartBtn) {
				scoreLabel.setText("0");
				scoreLabel2.setText("0");
				cntd.reset();
				cntd2.reset();
				cntd3.reset();
				window.restart();

			}
				if(src==quitBtn){
					window.dispose();
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



	public class CountTimer implements ActionListener {

		private static final int ONE_SECOND = 800;
		private int count;
		private boolean isTimerActive = false;
		private Timer tmr = new Timer(ONE_SECOND, this);
		/** Jlabel from main class **/
		private JLabel cntL;
		private JLabel cnsL;
		private int pTime;
		private boolean flag1;
		private boolean flag2;


		public CountTimer(JLabel tL,JLabel sL,int pT) {
			pTime=pT;
			cnsL=sL;
			cntL=tL;
			setTimerText(cntL,TimeFormat(count));
			if(pTime==1||pTime==2) setTimerColor(cntL,Color.GREEN.darker());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			flag1=false;
			flag2=false;
            if(pTime==1&&window.getBoard().getGame().isP1Turn()) {
				if (isTimerActive) {
					count++;
					setTimerText(cntL, TimeFormat(count));
					if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());
					flag1=true;

				}
			}
			if(pTime==1&&!window.getBoard().getGame().isP1Turn()) {
				if(count!=0||flag1)

					window.getBoard().getGame().setPlayer1Score(window.getBoard().getGame().getPlayer1Score()+(60-count));
				count=0;
			    flag1=false;
				cnsL.setText(String.valueOf(window.getBoard().getGame().getPlayer1Score()));
				setTimerText(cntL, TimeFormat(count));
				setTimerColor(cntL,Color.GREEN.darker());
			}
			if(pTime==2&& !window.getBoard().getGame().isP1Turn()) {
				if (isTimerActive) {
					count++;
					setTimerText(cntL, TimeFormat(count));
					if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());
					flag2=true;
				}
			}
			if(pTime==2&&window.getBoard().getGame().isP1Turn()) {
				if(count!=0||flag2)
				    window.getBoard().getGame().setPlayer2Score(window.getBoard().getGame().getPlayer2Score()+(60-count));
				count=0;
				flag2=false;
                cnsL.setText(String.valueOf(window.getBoard().getGame().getPlayer2Score()));
				setTimerText(cntL, TimeFormat(count));
				setTimerColor(cntL,Color.GREEN.darker());
			}

			if(pTime==3) {
				if (isTimerActive) {
					count++;
					setTimerText(cntL, TimeFormat(count));


				}
			}
		}

		public void start() {
			count = 0;
			isTimerActive = true;
			tmr.start();
		}

		public void resume() {
			isTimerActive = true;
			tmr.restart();
		}

		public void stop() {
			tmr.stop();
		}

		public void pause() {
			isTimerActive = false;
		}

		public void reset() {
			window.getBoard().getGame().setPlayer1Score(0);
			window.getBoard().getGame().setPlayer2Score(0);
			count = 0;
			isTimerActive = true;
			setTimerText(cntL,TimeFormat(count));
            if(pTime==1||pTime==2) setTimerColor(cntL,Color.GREEN.darker());
			tmr.restart();

		}

	}
	private String TimeFormat(int count) {

		int hours = count / 3600;
		int minutes = (count-hours*3600)/60;
		int seconds = count-minutes*60;

		return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
	}
	private void setTimerText(JLabel tL,String sTime) {
		tL.setText(sTime);
	}


	private void setTimerColor(JLabel tL,Color sColor) {
		tL.setForeground(sColor);
	}

}


