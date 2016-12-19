package Entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private boolean firingDown;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	private double xVec;
	private double yVec;
	private double currentMoveSpeed;
	private int damage;
	private double fireBallScale;

	public FireBall(TileMap tm, boolean right, int type, int damage) {
		
		super(tm);
		
		facingRight = right;
		firingDown = down;
		this.damage = damage;
		calculateSize();
		
		moveSpeed = 3.8;
		if(firingDown) dy = moveSpeed;
		else if(facingRight) {
			dx = moveSpeed;
		}
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		// load sprites
		switch(type) {
		case Creature.GREEN:
			sprites = Content.FireBall[0];
			hitSprites = Content.FireBall[1];
			break;
		case Creature.BLUE:
			sprites = Content.FireBallBlue[0];
			hitSprites = Content.FireBallBlue[1];
			break;
		default:
			System.out.println("Error: Creature Type Unknown");
			System.out.println("Using default 'red' fireballs.");
			sprites = Content.FireBall[0];
			hitSprites = Content.FireBall[1];
			break;
		}
		animation = new Animation();
		animation.setFrames(sprites, 4);
		animation.setDelay(4);

		
	}
	
	private void calculateSize() {
		
		fireBallScale = 0.3 + (double)damage / 25;
		
	}
	
	public boolean direction(Creature c) {
		// calculate vector to opponent
		xVec = c.getx() - x;
		yVec = c.gety() - y;
		if(yVec == 0) {
			if(facingRight) dx = moveSpeed;
			else dx = -moveSpeed;
			return false;
		}
		// calculate size of movement vectors dx, dy
		dy = Math.sqrt(Math.pow(moveSpeed, 2) / (1 + Math.pow(xVec, 2) / Math.pow(yVec, 2)));
		dx = Math.abs(dy * xVec / yVec);
		// match signs of movement vectors to distance vectors
		if(xVec < 0) dx = -dx;
		if(yVec < 0) dy = -dy;
		// checking if firing down
		if(yVec > xVec * 3 && dy > 0) return true;
		else return false;
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites, 3);
		animation.setDelay(4);
		dx = 0;
	}
	
	public int getDamage() { return damage; }
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		currentMoveSpeed = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		if(currentMoveSpeed < moveSpeed - 0.02 && !hit) {
			setHit();
		}
		
		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {

		setMapPosition();

		AffineTransform at = new AffineTransform();
		at.translate(
				x + xmap, 
				y + ymap
			);
		at.concatenate(AffineTransform.getScaleInstance(fireBallScale, fireBallScale));	// 0.3 - 2
		at.translate(
				- width / 2, 
				- height / 2
			);
		g.drawImage(
			animation.getImage(),
			at,
			null
		);
		
		//super.draw(g);

	}
		
	
}










