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
	private CountTimer cntd=new CountTimer(timeLabel,1);
	private JLabel timeLabel2=new JLabel();
	private CountTimer cntd2=new CountTimer(timeLabel2,2);
	private JLabel timeLabel3=new JLabel();
	private CountTimer cntd3=new CountTimer(timeLabel3,3);

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

		top2.add(new JLabel("Total Time:  "));
		top2.add(timeLabel3);
		cntd3.start();
		top.add(pauseBtn);
		top.add(resumeBtn);
		top.add(saveBtn);
		top.add(restartBtn);
		top.add(quitBtn);
		this.add(top2);
		this.add(top);

		middle.add(new JLabel("Black Player Score: "));
		this.add(middle);
		middle2.add(new JLabel("Black Player Time: "));
		middle2.add(timeLabel);
		cntd.start();
		//cntd.pause();
		this.add(middle2);
		bottom.add(new JLabel("White Player Score: "));
		this.add(bottom);
		bottom2.add(new JLabel("White Player Time: "));
		bottom2.add(timeLabel2);
		cntd2.start();
		//cntd2.pause();
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



	private class CountTimer implements ActionListener {

		private static final int ONE_SECOND = 1000;
		private int count;
		private boolean isTimerActive = false;
		private Timer tmr = new Timer(ONE_SECOND, this);
		/** Jlabel from main class **/
		private JLabel cntL;
		private int pTime;

		public CountTimer(JLabel tL,int pT) {
			pTime=pT;
			count=0;
			cntL=tL;
			setTimerText(cntL,TimeFormat(count));
			if(pTime==1||pTime==2) setTimerColor(cntL,Color.GREEN.darker());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
            if(pTime==1&&window.getBoard().getGame().isP1Turn()) {
				if (isTimerActive) {
					count++;
					setTimerText(cntL, TimeFormat(count));
					if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());


				}
			}
			if(pTime==1&&!window.getBoard().getGame().isP1Turn()) {
				count=0;
				setTimerText(cntL, TimeFormat(count));
				setTimerColor(cntL,Color.GREEN.darker());
			}
			if(pTime==2&& !window.getBoard().getGame().isP1Turn()) {
				if (isTimerActive) {
					count++;
					setTimerText(cntL, TimeFormat(count));
					if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());

				}
			}
			if(pTime==2&&window.getBoard().getGame().isP1Turn()) {
				count=0;
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


