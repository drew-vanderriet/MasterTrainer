package Entity.Effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Animation;
import Handlers.Content;

public class Explosion {
	
	private int x;
	private int y;
	private double xmap;
	private double ymap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(int type, int x, int y) {
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		switch(type) {
		case Effects.EXPLOSION: 
			sprites = Content.Explosion[0];
			break;
		case Effects.BLOCK:
			sprites = Content.Block[0];
			break;
		case Effects.DEAD:
			sprites = Content.Dead[0];
			break;
		default:
			break;
		}
		animation = new Animation();
		animation.setFrames(sprites, 6);
		animation.setDelay(4);
		
		
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(double x, double y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
			animation.getImage(),
			// getting the top corner of the image
			x + (int)xmap - width / 2,
			y + (int)ymap - height / 2,
			null
		);
	}

}









