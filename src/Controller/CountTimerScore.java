package Controller;

import View.HamkaOptionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CountTimerScore {

    private static final int ONE_SECOND = 800;
    private final HamkaOptionPanel hamkaOptionPanel;
    private int count;
    private boolean isTimerActive = false;
    private Timer tmr = new Timer(ONE_SECOND, this::actionPerformed);
    /**
     * Jlabel from main class
     **/
    private JLabel cntL;
    private JLabel cnsL;
    private int pTime;



    public CountTimerScore(HamkaOptionPanel hamkaOptionPanel, JLabel tL, JLabel sL, int pT) {
        this.hamkaOptionPanel = hamkaOptionPanel;
        pTime = pT;
        cnsL = sL;
        cntL = tL;
        setTimerText(cntL, TimeFormat(count));
        if (pTime == 1 || pTime == 2) setTimerColor(cntL, Color.GREEN.darker());
    }

   // @Override
    public void actionPerformed(ActionEvent e) {

        if (pTime == 1 && hamkaOptionPanel.getWindow().getBoard().getGame().isP1Turn()) {
            if (isTimerActive) {
                count++;
                setTimerText(cntL, TimeFormat(count));
                if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());


            }
        }
        if (pTime == 1 && !hamkaOptionPanel.getWindow().getBoard().getGame().isP1Turn()) {
            if (count != 0)
                hamkaOptionPanel.getWindow().getBoard().getGame().setPlayer1Score(hamkaOptionPanel.getWindow().getBoard().getGame().getPlayer1Score() + (60 - count));
            count = 0;

            cnsL.setText(String.valueOf(hamkaOptionPanel.getWindow().getBoard().getGame().getPlayer1Score()));
            setTimerText(cntL, TimeFormat(count));
            setTimerColor(cntL, Color.GREEN.darker());
        }
        if (pTime == 2 && !hamkaOptionPanel.getWindow().getBoard().getGame().isP1Turn()) {
            if (isTimerActive) {
                count++;
                setTimerText(cntL, TimeFormat(count));
                if (count > 60 && (pTime == 1 || pTime == 2)) setTimerColor(cntL, Color.RED.darker());

            }
        }
        if (pTime == 2 && hamkaOptionPanel.getWindow().getBoard().getGame().isP1Turn()) {
            if (count != 0 )
                hamkaOptionPanel.getWindow().getBoard().getGame().setPlayer2Score(hamkaOptionPanel.getWindow().getBoard().getGame().getPlayer2Score() + (60 - count));
            count = 0;

            cnsL.setText(String.valueOf(hamkaOptionPanel.getWindow().getBoard().getGame().getPlayer2Score()));
            setTimerText(cntL, TimeFormat(count));
            setTimerColor(cntL, Color.GREEN.darker());
        }

        if (pTime == 3) {
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
        hamkaOptionPanel.getWindow().getBoard().getGame().setPlayer1Score(0);
        hamkaOptionPanel.getWindow().getBoard().getGame().setPlayer2Score(0);
        count = 0;
        isTimerActive = true;
        setTimerText(cntL, TimeFormat(count));
        if (pTime == 1 || pTime == 2) setTimerColor(cntL, Color.GREEN.darker());
        tmr.restart();

    }

    private String TimeFormat(int count) {

        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;
        int seconds = count - minutes * 60;

        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    private void setTimerText(JLabel tL, String sTime) {
        tL.setText(sTime);
    }


    private void setTimerColor(JLabel tL, Color sColor) {
        tL.setForeground(sColor);
    }

}
