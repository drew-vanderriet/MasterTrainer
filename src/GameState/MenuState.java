package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Audio.AudioPlayer;
import Handlers.Keys;
import TileMap.Background;

public class MenuState extends GameState {

	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
			"New Game",
			"Continue",
			"Help",
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	private Font helpFont;
	
	private boolean help;
	
	public MenuState(GameStateManager gsm) {
		
		super(gsm);
		
		try {
			
			bg = new Background("/Backgrounds/menubgclouds.gif", 1);
			bg.setVector(-0.1, 0);
			
			// fonts
			titleColor = new Color(128,0,0);
			titleFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				28);

			font = new Font("Arial", Font.PLAIN, 12);
			helpFont = new Font("Century Gothic", Font.PLAIN, 14);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
				
		init();
		
	}
	
	private void loadAudio() {
		if(AudioPlayer.Loaded()) return;
		// loading music
		AudioPlayer.load("/Music/level1-1bg.mp3", "level1-1");
		// loading SFX
		AudioPlayer.load("/SFX/jump1.mp3", "jump");
		AudioPlayer.load("/SFX/scratch1.mp3", "scratch");
		AudioPlayer.load("/SFX/Slap.mp3", "hit");
		AudioPlayer.setVolume("hit", -12);
		AudioPlayer.load("/SFX/Attribution/Flame Arrow.mp3", "fireball");
		AudioPlayer.load("/SFX/Attribution/Swords_Collide.mp3", "block");
		AudioPlayer.load("/SFX/Attribution/Dog Bite.mp3", "critical");
		AudioPlayer.load("/SFX/Attribution/Bir Poop Splat.mp3", "dead");

		AudioPlayer.setLoaded(true);

		// playing bg music
		AudioPlayer.loop("level1-1");
		AudioPlayer.setVolume("level1-1", -11);
	}
	
	private void drawHelp(Graphics2D g) {

		g.setColor(Color.RED);
		g.setFont(titleFont);
		g.drawString("Help", 140, 25);
		
		g.setColor(Color.BLACK);
		g.setFont(helpFont);g.drawString("Press 'ESC' to exit back to main menu", 30, 45);
		g.setColor(Color.RED);
		g.drawString("Controls:", 30, 65);
		g.setColor(Color.BLACK);
		g.drawString("ESC - pause game", 30, 80);
		g.setColor(Color.RED);
		g.drawString("Defensive Strategy", 30, 100);
		g.setColor(Color.BLACK);
		g.drawString("A - Counter attacks", 30, 115);
		g.drawString("S - Block attacks", 30, 130);
		g.drawString("D - Dodge attacks", 30, 145);
		
		g.setColor(Color.RED);
		g.drawString("Offensive Strategy", 30, 165);
		g.setColor(Color.BLACK);
		g.drawString("Q - Conserve energy,", 30, 180);
		g.drawString("retreat and don't attack", 50, 195);
		g.drawString("W - Use fireball from a distance", 30, 210);
		g.drawString("E - Use scratch attack", 30, 225);
		
	}
	
	public void init() {
		// load audio
		loadAudio();
		
		help = false;
	}
		
	public void update() {
		// check keys
		handleInput();
		
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		if(help) {
			drawHelp(g);
			return;
		}
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Master Trainer", 70, 70);
		
		// draw menu options
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 130, 140 + i * 15);
		}
		
		
	}
	
	private void select() {
		if(currentChoice == 0) {	// New Game
			gsm.setState(GameStateManager.NEWGAME);
		}
		if (currentChoice == 1) {	// Continue
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 2) {	// Help
			help = true;
		}
		if (currentChoice == 3) {	// Quit
			System.exit(0);
		}
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) help = false;
		
		if(Keys.isPressed(Keys.ENTER)) select();
		if(Keys.isPressed(Keys.UP)) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	
}
