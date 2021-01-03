package View;
import java.awt.*;

public class MainMenuCanvas extends Canvas{

    public void paint(Graphics g) {

        Toolkit t=Toolkit.getDefaultToolkit();
        Image i=t.getImage(this.getClass().getResource("/Images/board.png"));
        Dimension d = getSize();
        g.drawImage(i, 1, 1, d.width, d.height, this);
    }
}
