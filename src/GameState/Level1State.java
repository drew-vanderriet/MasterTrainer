package GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import AI.DetermineAction;
import Entity.Creature;
import Entity.GameOver;
import Entity.HUD;
import Entity.Title;
import Entity.Trainer;
import Entity.Creatures.BlueDragon;
import Entity.Creatures.GreenDragon;
import Entity.Effects.Effects;
import Handlers.Keys;
import Handlers.Save;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;
	
	private Save save;
	
	private Creature player;
	private Creature opponent;
	
	private DetermineAction determineAction;
	
	public Effects effects;
	
	private boolean blockInput = false;
		
	private HUD hud;
	
	private BufferedImage playerImage;
	private BufferedImage opponentImage;
	private Title playerTitle;
	private Title opponentTitle;
	private Trainer playerTrainer;
	private Trainer opponentTrainer;
	
	private int screenX;
	private int screenY;
	
	private GameOver gameOverTitle;
	private boolean gameOver;
	private boolean win;
	
	// the player levels are:
	private boolean newGame;
	private int[] newPlayerLevelSet = {
			100, 	// max health
			50, 	// strength
			50, 	// speed
			50, 	// stamina
			50, 	// focus
			50, 	// defense
			50, 	// aggression
			50,		// courage
			20,		// independence
			0		// XP
	};
	
	
			
	/**
	 * Flow of Game States
	 * init() - initialize state
	 * update() - apply changes
	 * draw(Graphics2D g) - draw changes
	 * keyPressed(int k) - fetch info
	 * keyReleased(int k)  - fetch info
	 * @param gsm
	 */
	public Level1State(GameStateManager gsm, boolean b) {
		super(gsm);
		newGame = b;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(500, 0);
		tileMap.setTween(0.15);
		
		bg = new Background("/Backgrounds/grassbg.gif", 0.1);
		
		gameOver = false;
		win = false;
		
		populateCreatures();
		
		determineAction = new DetermineAction();
		
		effects = new Effects(tileMap);
				
		hud = new HUD(player, opponent);
		
		loadTitle();

		
	}
	
	/**
	 * @param creature - creature to be loaded
	 * @param s - address of the file to load
	 * @return false if load failed.
	 */
	
	private void loadTitle() {
		// title and subtitle
		try {
			playerImage = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Creatures/greendragonblock.gif")
					);
			opponentImage = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Creatures/bluedragonblock.gif")
					);

			playerTitle = new Title(playerImage.getSubimage(0, 150, 30, 30), player.getName(), 
					true, player.getLevel());
			playerTitle.sety(70);

			opponentTitle = new Title(opponentImage.getSubimage(0, 150, 30, 30), opponent.getName(), 
					false, opponent.getLevel());
			opponentTitle.sety(70);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		playerTitle.begin();
		playerTrainer = new Trainer(Trainer.TRAINER_BROWN, true);
		
		opponentTitle.begin();
		opponentTrainer = new Trainer(Trainer.TRAINER_BLUE, false);
	}
	
	private void loadGameOver() {
		String[] info = {"","","","","",""};
		String[] levelInfo;
		
		//============================================
		// Print Battle data:
		System.out.println("\nGame Over");
		System.out.println("Battle Data:");
		for(int i = 0; i < player.getActionSet().size(); i++) {
			System.out.println(player.getActionSet().get(i).getName() + "\t-\t" 
					+ player.getActionSet().get(i).getOccurance());
		}
		System.out.println("Number of orders given\t-\t" + playerTrainer.getOrderCounter());
		System.out.println("Battle time\t-\t" + player.getBattleCounter());
		//============================================
		
		gameOver = true;
		if(win) {
			// Calculate XP gained
			levelInfo = player.gainXP(opponent.getLevel());
			if(levelInfo[0] != "") {
				info[0] = levelInfo[0];
				info[1] = levelInfo[1];
				info[2] = "You gained " + Integer.toString((opponent.getLevel() * opponent.getLevel())) + " XP from that battle!";
				info[3]	= "Your XP is " + Integer.toString(player.getXP()) + " / " + Integer.toString(player.getLevelUpXP()) + " XP";
				info[4] = "Your level is " + Integer.toString(player.getLevel());
				info[5] = "Game Saved!";
			} else {
				info[0] = "You gained " + Integer.toString((opponent.getLevel() * opponent.getLevel())) + " XP from that battle!";
				info[1]	= "Your XP is " + Integer.toString(player.getXP()) + " / " + Integer.toString(player.getLevelUpXP()) + " XP";
				info[2] = "Your level is " + Integer.toString(player.getLevel());
				info[3] = "Game Saved!";
			}
		} else {
			info[0] = "You are at level " + Integer.toString(player.getLevel());
			info[1] = "Game Saved!";
			}
		// Save Progress
		//save.savePlayer(player);
		save.saveData(player.getData());
		
		// Load Game Over Title
			
		// title and subtitle
		try {
			playerImage = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Creatures/greendragonblock.gif")
					);

			gameOverTitle = new GameOver(playerImage.getSubimage(0, 150, 30, 30), info, 
					win, player.getLevel());
			gameOverTitle.sety(70);

		}
		catch(Exception e) {
			e.printStackTrace();
		}

		gameOverTitle.begin();
		
	}
	
	private void populateCreatures() {
				
		player = new GreenDragon(tileMap);
		player.setPosition(500, 100);
		
		// establishing save path for player
		save = new Save(player, "Saves/PlayerSaveObj.sav");
		// checking if new game of if previous save exists
		if(newGame || save.loadFailed()) {
			player.setLevel(newPlayerLevelSet);
			System.out.println("New Game!");

		} else { 
			//player.setLevel(save.loadPlayer().getLevelSet());
			player.setData(save.loadData());
			}
		player.init();
		
		opponent = new BlueDragon(tileMap);
		opponent.setPosition(700, 100);
		// generating random blue dragon level based on player level
		opponent.randomLevel(player.getLevel(), 20, 10);
		opponent.init();
	}
	
	private void findScreenPos(Creature player, Creature opponent) {
		// setting screen position to center of player or opponent or both.
		if(player.isDead())	{
			screenX = opponent.getx();
			screenY = opponent.gety();
		}
		else if(opponent.isDead()) {
			screenX = player.getx();
			screenY = player.gety();
		}
		else {
			screenX = (player.getx() + opponent.getx()) / 2;
			screenY = (player.gety() + opponent.gety()) / 2;
		}
	}
	
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
		if(blockInput || player.getHealth() == 0 || playerTrainer.isOrder()) return;
		
		if(Keys.isPressed(Keys.BUTTON1)) {
			playerTrainer.setOrder(Creature.ORD_REST, player.strategy.setRest());
		}
		if(Keys.isPressed(Keys.BUTTON2)) {
			playerTrainer.setOrder(Creature.ORD_RANGE, player.strategy.setDistance());
		}
		if(Keys.isPressed(Keys.BUTTON3)) {
			playerTrainer.setOrder(Creature.ORD_CLOSE, player.strategy.setClose());
		}
		if(Keys.isPressed(Keys.BUTTON4)) {
			playerTrainer.setOrder(Creature.ORD_COUNTER, player.strategy.setCounter());
		}
		if(Keys.isPressed(Keys.BUTTON5)) {
			playerTrainer.setOrder(Creature.ORD_BLOCK, player.strategy.setBlock());
		}
		if(Keys.isPressed(Keys.BUTTON6)) {
			playerTrainer.setOrder(Creature.ORD_DODGE, player.strategy.setDodge());
		}
	}
	
	public void update() {
		
		// check keys
		handleInput();
		
		// setting screen position to center of player or opponent or both.
		findScreenPos(player, opponent);
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - screenX,
				GamePanel.HEIGHT / 2 - screenY
				);
		
		// move title and subtitle
		if(playerTitle != null) {
			playerTitle.update();
			if(playerTitle.shouldRemove()) playerTitle = null;
		}
		if(opponentTitle != null) {
			opponentTitle.update();
			if(opponentTitle.shouldRemove()) opponentTitle = null;
		}

		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// update trainer
		playerTrainer.update();
		opponentTrainer.update();
		
		// wait for title
		if(playerTitle != null) return;
				
		// update creatures
		player.updateActions(determineAction.Decide(player, opponent));
		opponent.updateActions(determineAction.Decide(opponent, player));
		
		player.update();
		opponent.update();
		
		// check for creature collision
		player.checkCreatureCollision(opponent);
		opponent.checkCreatureCollision(player);
		
		// attack creatures
		player.checkAttack(opponent);
		opponent.checkAttack(player);
		
		// check if dead
		if(player.isDead() && !player.Exploded()) {
			effects.addDead(player.getx(), player.gety());
			player.setExploded(true);
			win = false;
			loadGameOver();
		}
		if(opponent.isDead() && !opponent.Exploded()) {
			effects.addDead(opponent.getx(), opponent.gety());
			opponent.setExploded(true);
			win = true;
			loadGameOver();
		}
		
		// update explosions
		effects.update();
		
		// update Game Over
		if(gameOver && gameOverTitle != null) { 
			gameOverTitle.update();
			if(gameOverTitle.shouldRemove()) gameOverTitle = null;
			}

	}

	public void draw(Graphics2D g) {

		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw Trainer
		playerTrainer.draw(g);
		opponentTrainer.draw(g);
		
		// draw title
		if(playerTitle != null) playerTitle.draw(g);
		if(opponentTitle != null) opponentTitle.draw(g);
		
		// draw creatures
		if(!player.isDead()) player.draw(g);
		else { 
			player.setMapPosition();
			player.drawDamage(g);
		}
		if(!opponent.isDead()) opponent.draw(g);
		else {
			opponent.setMapPosition();
			opponent.drawDamage(g);
		}
		
		// draw explosions
		effects.draw(g);
		
		// draw HUD
		hud.draw(g);
		
		// draw title
		if(gameOverTitle != null) gameOverTitle.draw(g);
		
		
	}
	
	
	
}









