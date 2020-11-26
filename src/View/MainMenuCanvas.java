package View;
import java.awt.*;

public class MainMenuCanvas extends Canvas{

    public void paint(Graphics g) {

        Toolkit t=Toolkit.getDefaultToolkit();
        Image i=t.getImage(this.getClass().getResource("/Images/board.png"));
        g.drawImage(i, 17,5,this);

    }
}
