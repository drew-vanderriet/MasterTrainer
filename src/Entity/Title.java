package Entity;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Title {
	
	public BufferedImage image;
	
	public int count;
	private boolean done;
	private boolean remove;
	
	private double x;
	private double y;
	private double dx;
	
	private int width;
	private int height;
	
	private String name;
	private String levelString;
	private boolean facingRight;
	
	private Font font;
	private static final int STOPPOS = 100;

	
	// two constructors, first receives a string of image
	public Title(String s, String n, boolean f, int l) {
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			height = image.getHeight();
			x = -width;
			done = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		name = n;
		facingRight = f;
		levelString = "Level: " + Integer.toString(l);
		
	}
	
	// or second receives the buffered image
	public Title(BufferedImage image, String n, boolean f, int l) {
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		done = false;
		
		name = n;
		facingRight = f;
		levelString = "Level: " + Integer.toString(l);

	}
	
	public void sety(double y) { this.y = y; }
	
	public void begin() {
		if(facingRight)	{
			x = -width;
			dx = 10;
		}
		else {
			x = GamePanel.WIDTH + width;
			dx = -10;
		}
		font = new Font("Arial", Font.PLAIN, 12);
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		if(facingRight) {
			// if still playing
			if(!done) {
				// if in the 2nd half of screen
				if(x >= STOPPOS - width / 2) {
					count++;
					// pause in center of screen
					if(count >= 120) done = true;
				}
				// if in the 1st half of the screen
				else {
					x += dx;
				}
			}
			// move off screen
			else {
				x -= dx;
				// if off screen, remove
				if(x < 0 - width) remove = true;
			}
		}
		else {
			// if still playing
			if(!done) {
				// if in the 2nd half of screen
				if(x <= (GamePanel.WIDTH - STOPPOS - width / 2)) {
					count++;
					// pause in center of screen
					if(count >= 120) done = true;
				}
				// if in the 1st half of the screen
				else {
					x += dx;
				}
			}
			// move off screen
			else {
				x -= dx;
				// if off screen, remove
				if(x > GamePanel.WIDTH) remove = true;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		// draw creature name
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString(name, (int)x - 10, (int)y);
		// draw image of creature
		if(facingRight)	g.drawImage(image, (int)x, (int)y, null);
		else g.drawImage(image, (int)x + width, (int)y, -width, height, null);
		//draw creature level
		g.drawString(levelString, (int)x - 10, (int)y + height + 10);
	}
	
}
