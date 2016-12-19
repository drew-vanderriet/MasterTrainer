package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {
	
	private BufferedImage image;
	
	private double x = 0;
	private double y = 0;
	private double dx = 0;
	private double dy = 0;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		try {
			image =  ImageIO.read(
					getClass().getResourceAsStream(s)
					);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		// moves bg image to far right of screen when off screen
		if (GamePanel.WIDTH + x <= 0) {
			x = GamePanel.WIDTH;
		}
		
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image,  (int)x, (int)y, null); 	// brackets around int rounds x and y to whole integers
		
		// draws two bg images for continual flow
		if (x < 0) {
			g.drawImage(image, 
				(int)x + GamePanel.WIDTH,
				(int)y, null
			);
		}
		if (x > 0) {
			g.drawImage(image, 
				(int)x - GamePanel.WIDTH,
				(int)y,	null
			);
		}
	}

}
