package Controller;

import View.HamkaBoard;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ClickListener responsible for listening to user clicks on the board and translate
 * Soldier movements to board view manipulation (handleclick)
 */
public class HamkaClickListener implements ActionListener {

    private HamkaBoard hBoard;

    public HamkaClickListener(HamkaBoard hBoard)
    {
        this.hBoard = hBoard;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

            // Get the new mouse coordinates and handle the click
            Point m = hBoard.getMousePosition();
            if (m != null) {
                hBoard.handleClick(m.x, m.y);
            }
        }
}
