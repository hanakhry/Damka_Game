package Controller;

import View.HamkaBoard;
import View.HamkaWindow;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClickListener implements ActionListener {

        private HamkaWindow hWindow = new HamkaWindow();
        public HamkaBoard hBoard = new HamkaBoard(hWindow);

        @Override
        public void actionPerformed(ActionEvent e) {

            // Get the new mouse coordinates and handle the click
            Point m = hBoard.getMousePosition();
            if (m != null) {
                hBoard.handleClick(m.x, m.y);
            }
        }

}
