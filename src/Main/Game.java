package Main;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Master Trainer");
		window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		// sets window in center of screen
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}

}
