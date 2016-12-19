package Handlers;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

// this class loads resources on boot.
// spritesheets are taken from here.

public class Content {
	
	public static BufferedImage[][] GreenDragon = load("/Sprites/Creatures/greendragonblock.gif", 30, 30);
	public static BufferedImage[][] GreenDragonScratch = load("/Sprites/Creatures/greendragon.gif", 60, 30);
	public static BufferedImage[][] BlueDragon = load("/Sprites/Creatures/bluedragonblock.gif", 30, 30);
	public static BufferedImage[][] BlueDragonScratch = load("/Sprites/Creatures/bluedragon.gif", 60, 30);
	public static BufferedImage[][] Slugger = load("/Sprites/Creatures/slugger.gif", 30, 30);

	public static BufferedImage[][] YellowTrainer = load("/Sprites/Trainers/YellowTrainer.gif", 40, 40);
	public static BufferedImage[][] BrownTrainer = load("/Sprites/Trainers/BrownTrainer.gif", 40, 40);
	public static BufferedImage[][] BlueTrainer = load("/Sprites/Trainers/BlueTrainer.gif", 40, 40);
	public static BufferedImage[][] RedTrainer = load("/Sprites/Trainers/RedTrainer.gif", 40, 40);

	
	public static BufferedImage[][] Explosion = load("/Sprites/Effects/explosion.gif", 30, 30);
	public static BufferedImage[][] Block = load("/Sprites/Effects/block.gif", 30, 30);
	public static BufferedImage[][] Dead = load("/Sprites/Effects/dead.gif", 30, 30);
	public static BufferedImage[][] FireBall = load("/Sprites/Effects/fireball.gif", 30, 30);
	public static BufferedImage[][] FireBallBlue = load("/Sprites/Effects/fireballblue.gif", 30, 30);

	
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
}

