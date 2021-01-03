package Controller;

import View.MainMenu;
import javax.swing.*;
import java.io.IOException;

public class StartGame {

	public static void main(String[] args) throws IOException {

		try {
			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace(); }

		MainMenu window = new MainMenu();
		window.setDefaultCloseOperation(MainMenu.DISPOSE_ON_CLOSE);
		window.setVisible(true);

	}
}
