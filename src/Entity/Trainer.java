package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Handlers.Content;
import Main.GamePanel;

public class Trainer {

	public BufferedImage image;
	public BufferedImage bg;

	private ArrayList<BufferedImage[]> sprites;
	
	private Animation animation;

	public int count;
	private boolean remove;
	
	private int width;
	private int height;
	
	private boolean facingRight;


	// Trainer Type
	public static final int TRAINER_YELLOW = 0;
	public static final int TRAINER_BROWN = 1;
	public static final int TRAINER_BLUE = 2;
	public static final int TRAINER_RED = 3;
	
	private final int[] numFrames = {
			3, 3
	};

	// animation actions - based on sprite sheet
	private static final int A_IDLE = 0;
	private static final int A_ORDER = 1;
	
	private int currentAction;
	private int orderCounter = 0;
	
	// trainer position
	private static final int X = 5;
	private static final int Y = 55;
	private static final int SIZE = 44;
	private int gap;
	
	String text;
	private Font font;
	
	public Trainer(int type, boolean f) {
		
		try {
			bg = ImageIO.read(getClass().getResourceAsStream(
					"/Backgrounds/trainergrassbg.gif"
					));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		width = bg.getWidth();
		height = bg.getHeight();
		
		gap = (SIZE - width) / 2;
		
		setTrainer(type);
		
		facingRight = f;
		font = new Font("Arial", Font.PLAIN, 10);

	}
	
	public void setTrainer(int type) {
		switch (type) {
		case TRAINER_YELLOW: loadYellow();
			break;
		case TRAINER_BROWN: loadBrown();
			break;
		case TRAINER_BLUE: loadBlue();
			break;
		case TRAINER_RED: loadRed();
			break;
		default: loadBrown();
			break;
		}
		return;
	}
	
	private void loadYellow() {
		// load sprites
		sprites = new ArrayList<BufferedImage[]>();

		sprites.add(Content.YellowTrainer[0]);
		sprites.add(Content.YellowTrainer[1]);

		animation = new Animation();
		currentAction = A_IDLE;
		animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
		animation.setDelay(32);
		return;
	}
	
	private void loadBrown() {
		// load sprites
		sprites = new ArrayList<BufferedImage[]>();

		sprites.add(Content.BrownTrainer[0]);
		sprites.add(Content.BrownTrainer[1]);

		animation = new Animation();
		currentAction = A_IDLE;
		animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
		animation.setDelay(32);
		return;
	}
	
	private void loadBlue() {
		// load sprites
		sprites = new ArrayList<BufferedImage[]>();

		sprites.add(Content.BlueTrainer[0]);
		sprites.add(Content.BlueTrainer[1]);

		animation = new Animation();
		currentAction = A_IDLE;
		animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
		animation.setDelay(32);
		return;
	}
	
	private void loadRed() {
		// lood sprites
		sprites = new ArrayList<BufferedImage[]>();

		sprites.add(Content.RedTrainer[0]);
		sprites.add(Content.RedTrainer[1]);

		animation = new Animation();
		currentAction = A_IDLE;
		animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
		animation.setDelay(32);
		return;
	}
	
	public void setOrder(int order, boolean action) {
		orderCounter++;
		currentAction = A_ORDER;
		animation.setFrames(sprites.get(A_ORDER), numFrames[A_ORDER]);
		animation.setDelay(10);
		
		switch (order) {
		case Creature.ORD_BLOCK:
			if(action) text = "Block his attacks!";
			else text = "Choose your own defense!";
			break;
		case Creature.ORD_DODGE:
			if(action) text = "Dodge his attacks!";
			else text = "Choose your own defense!";
			break;
		case Creature.ORD_COUNTER:
			if(action) text = "Counter his attacks!";
			else text = "Choose your own defense!";
			break;
		case Creature.ORD_CLOSE:
			if(action) text = "Use scratch attack!";
			else text = "Choose your own attack!";
			break;
		case Creature.ORD_RANGE:
			if(action) text = "Use fireball!";
			else text = "Choose your own attack!";
			break;
		case Creature.ORD_REST:
			if(action) text = "Conserve your energy!";
			else text = "Choose your own attack!";
			break;
		default:
			text = "";
			break;
		}
		
		return;
	}
	
	public boolean shouldRemove() { return remove; }
	public int getOrderCounter() { return orderCounter; }
	
	public boolean isOrder() { return currentAction == A_ORDER;	}
	
	public void update() {
		
		// update animation
		if(animation.hasPlayedOnce() && currentAction == A_ORDER) {
			currentAction = A_IDLE;
			animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
			animation.setDelay(32);
		}
		
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		int xPos;
		
		if(facingRight) {
			xPos = X;
		}
		else {
			xPos = GamePanel.WIDTH - X - SIZE;
		}
		
		g.setColor(Color.GRAY);
		
		// draw rectangle
		g.fillRect(xPos, Y, SIZE, SIZE);

		// draw background
		g.drawImage(bg, xPos + gap, Y + gap, null);
		
		// draw image of trainer
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				xPos + gap,
				Y + gap,
				null
			);
		}
		else {
			// placing a negative width flips the image horizontally
			// x starting position is now the far end of the object
			g.drawImage(
				animation.getImage(),
				xPos + gap + width,
				Y + gap,
				-width,
				height,
				null
			);
		}
		
		// draw orders
		if(currentAction == A_ORDER) {
			// draw speech bubble
			g.setColor(Color.WHITE);
			
			g.fillOval(xPos + gap, Y + SIZE + gap, text.length() * 6 + 10, 17);
			
			// draw speech
			g.setColor(Color.RED);
			g.setFont(font);
			g.drawString(text, xPos + gap + 15, Y + SIZE + gap + 12);
		}
		
	}
	
}
