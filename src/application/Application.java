package application;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Application {

	public static void main(String[] p_args) {
		ApllicationView mainView = new ApllicationView();
		mainView.setSize(450, 200);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - mainView.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - mainView.getHeight()) / 2);
		mainView.setLocation(x, y);
		mainView.setVisible(true);
	}
}
