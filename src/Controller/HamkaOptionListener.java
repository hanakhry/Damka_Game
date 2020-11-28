package Controller;

import View.HamkaWindow;
import View.MainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OptionListener responsible for listening to user clicks on the option panel
 * and activate relevant buttons
 */
public class HamkaOptionListener implements ActionListener {

    private HamkaWindow hWindow;
    private JButton btn;

    public HamkaOptionListener(HamkaWindow hWindow, JButton button)
    {
        this.hWindow = hWindow;
        this.btn=button;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        // No window to update
        if (hWindow == null) {
            return;
        }

        // Handle the user action
        if (e.getSource() == btn) {
            if(btn.getText()=="Restart")hWindow.restart();
            if(btn.getText()=="Quit Game"){
                hWindow.dispose();
                MainMenu window = new MainMenu();
                MainMenu.NewGame.addActionListener(new MMOptionListener(window,MainMenu.NewGame));
                MainMenu.ExitGame.addActionListener(new MMOptionListener(window,MainMenu.ExitGame));
                window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
                window.setVisible(true);
            }

        }
    }
}