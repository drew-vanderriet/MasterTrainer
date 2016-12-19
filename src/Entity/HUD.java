package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class HUD {
	
	private Creature player;
	private Creature creature;
	
	private BufferedImage image, imageblue;
	private Font font;
	
	public HUD(Creature p, Creature c) {
		player = p;
		creature = c;
		try {
			
			image = ImageIO.read(
				getClass().getResourceAsStream(
					"/HUD/hud.gif"
				)
			);
			imageblue = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/hudblue.gif"
					)
			);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		font = new Font("Arial", Font.PLAIN, 12);
		g.drawImage(image, 0, 10, null);
		g.drawImage(imageblue, GamePanel.WIDTH - imageblue.getWidth(), 10, null);

		g.setFont(font);
		g.setColor(Color.WHITE);

		// draw green player stats
		g.drawString(
			player.getHealth() + "/" + player.getMaxHealth(), 
			20, 
			25
		);
		g.drawString(
			player.getEnergy() / 100 + "/" + player.getMaxEnergy() / 100, 
			20, 
			45
		);
		
		// draw blue player stats
		g.drawString(
			creature.getHealth() + "/" + creature.getMaxHealth(), 
			GamePanel.WIDTH - 65, 
			25
		);
		g.drawString(
			creature.getEnergy() / 100 + "/" + creature.getMaxEnergy() / 100, 
			GamePanel.WIDTH - 65, 
			45
		);
		
	}

}













