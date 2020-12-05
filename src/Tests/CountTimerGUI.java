package Tests;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountTimerGUI implements ActionListener {

    private JFrame frame;
    private JPanel panel;

    private JLabel timeLabel = new JLabel();

    private JButton startBtn = new JButton("Start");
    private JButton pauseBtn = new JButton("Pause");
    private JButton resumeBtn = new JButton("Resume");
    private JButton stopBtn = new JButton("Stop");
    private JButton resetBtn = new JButton("Reset");

    private JButton greenBtn = new JButton("Green");
    private JButton redBtn = new JButton("Red");

    private CountTimer cntd;


    public CountTimerGUI() {
        setTimerText("         ");
        GUI();
    }

    private void GUI() {
        frame = new JFrame();
        panel = new JPanel();

        panel.setLayout(new BorderLayout());
        timeLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(timeLabel, BorderLayout.NORTH);

        startBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        resumeBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        resetBtn.addActionListener(this);
        greenBtn.addActionListener(this);
        redBtn.addActionListener(this);

        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new GridLayout());

        cmdPanel.add(startBtn);
        cmdPanel.add(pauseBtn);
        cmdPanel.add(resumeBtn);
        cmdPanel.add(stopBtn);
        cmdPanel.add(resetBtn);

        panel.add(cmdPanel, BorderLayout.SOUTH);

        JPanel clrPanel = new JPanel();
        clrPanel.setLayout(new GridLayout(0,1));

        clrPanel.add(greenBtn);
        clrPanel.add(redBtn);

        panel.add(clrPanel, BorderLayout.EAST);

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

        cntd = new CountTimer();

    }

    private void setTimerText(String sTime) {
        timeLabel.setText(sTime);
    }


    private void setTimerColor(Color sColor) {
        timeLabel.setForeground(sColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        JButton btn = (JButton) e.getSource();

        if (btn.equals(greenBtn))        { setTimerColor(Color.GREEN.darker()); }
        else if (btn.equals(redBtn))     { setTimerColor(Color.RED); }
        else if (btn.equals(startBtn))   { cntd.start(); }
        else if (btn.equals(pauseBtn))   { cntd.pause(); }
        else if (btn.equals(resumeBtn))  { cntd.resume(); }
        else if (btn.equals(stopBtn))    { cntd.stop(); }
        else if (btn.equals(resetBtn))   { cntd.reset(); }
    }


    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CountTimerGUI();
            }
        });

    }

    private class CountTimer implements ActionListener {

        private static final int ONE_SECOND = 1000;
        private int count = 0;
        private boolean isTimerActive = false;
        private Timer tmr = new Timer(ONE_SECOND, this);

        public CountTimer() {
            count=0;
            setTimerText(TimeFormat(count));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isTimerActive) {
                count++;
                setTimerText(TimeFormat(count));
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
            tmr.restart();

        }

    }

    private String TimeFormat(int count) {

        int hours = count / 3600;
        int minutes = (count-hours*3600)/60;
        int seconds = count-minutes*60;

        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}