package Controller;


import View.HamkaWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionListener implements ActionListener {
    private HamkaWindow window = new HamkaWindow();
    private JButton restartBtn;

    @Override
    public void actionPerformed(ActionEvent e) {

        // No window to update
        if (window == null) {
            return;
        }

        Object src = e.getSource();

        // Handle the user action
        JButton btn = null;

        if (src == restartBtn) {
            window.restart();
        }



    }
}