package Controller;

import View.HamkaWindow;
import View.MainMenu;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Menu Option Listener
 */
public class MMOptionListener implements ActionListener {
    private JButton btn;
    private MainMenu mMenu;
    public MMOptionListener(MainMenu mMenu, JButton button)
    {
        this.btn = button;
        this.mMenu=mMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        // Handle the user action
        if (e.getSource() == btn) {
            if(btn.getText()=="New Game"){
            mMenu.dispose();
            HamkaWindow window = new HamkaWindow();
            window.setDefaultCloseOperation(HamkaWindow.EXIT_ON_CLOSE);
            window.setVisible(true);
        }
            if(btn.getText()=="Exit"){
                mMenu.dispose();

            }
        }

    }
}