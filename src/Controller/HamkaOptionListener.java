package Controller;

import View.HamkaWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OptionListener responsible for listening to user clicks on the option panel
 * and activate relevant buttons
 */
public class HamkaOptionListener implements ActionListener {

    private HamkaWindow hWindow;
    private JButton restartBtn;

    public HamkaOptionListener(HamkaWindow hWindow, JButton restartBtn)
    {
        this.hWindow = hWindow;
        this.restartBtn=restartBtn;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        // No window to update
        if (hWindow == null) {
            return;
        }

        // Handle the user action
        if (e.getSource() == restartBtn) {
            hWindow.restart();
        }
    }
}