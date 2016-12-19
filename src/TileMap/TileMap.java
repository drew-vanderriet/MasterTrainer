package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s) {
		
		try {
			
			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for (int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize, 
							0, 
							tileSize, 
							tileSize
						);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize, // if more than 2 rows, can use 2nd loop through rows
							tileSize,
							tileSize
						);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				
			}
				
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String s) {
		/* 1st line = num cols
		 * 2nd line = num rows
		 * rest = map
		 */
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			// width is width of map
			// height is height of map
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+"; 	// white space
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
					
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() {
		return tileSize;
	}
	public double getx() {
		return x;
	}
	public double gety() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross; 	// 0 or 1: int always rounds down
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public void setTween(double tween) {
		this.tween = tween;
	}
	
	/**Position is top left corner of screen in negative
	 * pixels for setting camera position.
	 * 
	 * Usual position set to: (set by gsm.update)
	 * /2 to center on player. 
	 * GamePanel.WIDTH(320) / 2 - player.getx(),
	 * GamePanel.HEIGHT(240) / 2 - player.gety()
	 * 
	 * player x/y positions are relative to map so
	 * usually large x >> 300 --> setPosition(X,Y)
	 * are usually negative.
	 * 
	 * tween is meant to create camera lag
	 * for smooth game flow, but below code
	 * is twitchy
	 * TODO solve tween twitch problem
	 */
	public void setPosition(double x, double y) {
		
		// calculates the difference between the new
		// and old position and then add's only a fraction
		// to the position, determined by tween
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		// if x/y are not negative they are maxxed 
		// at 0.
		fixBounds();
		
		/* Only draw what's on the screen
		 * Offset is the start position for the draw
		 * converting the pixel count into cols and rows
		 * and multiplying by negative for to convert 
		 * -x/y back into positives.
		*/
		colOffset = (int) - this.x / tileSize;
		rowOffset = (int) - this.y / tileSize;
		
	}
	
	/**
	 * xmin = GamePanel.WIDTH - width of map;
	 * xmax = 0;
	 * ymin = GamePanel.HEIGHT - height of map;
	 * ymax = 0;
	 * 
	 */
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;

	}
	
	/**
	 * TileMap.draw(Graphics2D g) works on a negative
	 * positioning system as the start of the map must
	 * be drawn on the left of the screen (left is 
	 * negative). The more too the right of the map
	 * you move, the more of the map must be painted
	 * off the screen to the left (larger negative
	 * number)
	 * 
	 * Offset prevents painting of map that is off
	 * screen.
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		
		// paints only what is on the screen
		// looping through the numRowsToDraw
		// and only painting them.
		for (
				int row = rowOffset;
				row < rowOffset + numRowsToDraw;
				row++) {
			
			if (row >= numRows) break;
			
			for (
					int col = colOffset;
					col < colOffset + numColsToDraw;
					col++) {
				
				if (col >= numCols) break;

				if (map[row][col] == 0) continue; 	// tileset position 0 is always 
													// transparent so no draw is required
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
							
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
			}
		}
		
	}

}


















