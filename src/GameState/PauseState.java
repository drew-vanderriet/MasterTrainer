package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Handlers.Keys;
import Main.GamePanel;

public class PauseState extends GameState {
	
	private Font font;
	
	public PauseState(GameStateManager gsm) {
		
		super(gsm);
		
		// fonts
		font = new Font("Century Gothic", Font.PLAIN, 14);
		
	}
	
	public void init() {}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 115, 70);
		g.drawString("Press 'Q' to exit to main menu", 70, 120);
		g.drawString("Press 'E' to fight a new battle!", 70, 150);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
		if(Keys.isPressed(Keys.BUTTON1)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(Keys.isPressed(Keys.BUTTON3)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
	}

}

