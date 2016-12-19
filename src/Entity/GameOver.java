package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class GameOver {

public BufferedImage image;
	
	public int count;
	private boolean done;
	private boolean remove;
	
	private double x;
	private double y;
	private double dx;
	
	private int width;
	private int height;
	
	private String[] info;
	private String levelString;
	private String result;
	
	private Font font;
	private static final int STOPPOS = 80;

	
	public GameOver(BufferedImage image, String[] s, boolean w, int l) {
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		done = false;
		
		info = s;
		if(w) { result = "You Won!!!"; }
		else { result = "You Lost..."; }
		levelString = "Level: " + Integer.toString(l);

	}
	
	public void sety(double y) { this.y = y; }
	
	public void begin() {

		x = -width;
		dx = 10;

		font = new Font("Arial", Font.PLAIN, 12);
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {

		// if still playing
		if(!done) {
			// if in the 2nd half of screen
			if(x >= STOPPOS - width / 2) {
				count++;
				// pause in center of screen
				if(count >= 480) done = true;
			}
			// if in the 1st half of the screen
			else {
				x += dx;
			}
		}
		// move off screen
//		else {
//			x -= dx;
//			// if off screen, remove
//			if(x < 0 - width) remove = true;
//		}


	}
	
	public void draw(Graphics2D g) {
		// draw creature name
		g.setColor(Color.RED);
		g.setFont(font);
		g.drawString(result, (int)x - 10, (int)y);
		// draw image of creature
		g.drawImage(image, (int)x, (int)y, null);
		//draw creature level
		for(int i = 0; i < info.length; i++) {
		g.drawString(info[i], (int)x - 10, (int)y + height + 10 + 15 * i);
		}
	}
	
}
