package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector on screen
	// center of object on map, top left corner of 
	// map is 0, 0
	protected double x;  
	protected double y;
	
	protected double dx; //direction of movement
	protected double dy;
	
	// dimensions - used for reading in from sprite sheets
	protected int width;
	protected int height;
	
	// collision box - size as seen in physics engine
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest; // next position
	protected double ydest; // next position
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement

	protected boolean up;
	protected boolean down;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;	// acceleration 
	protected double maxSpeed; 
	protected double stopSpeed;	// deceleration
	protected double fallSpeed;	// gravity
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth,
				(int)y - cheight,
				cwidth,
				cheight
			);
	}
	
	public void calculateCorners(double x, double y) {
		
		/* [x(center pixel pos) - cw(pixel width)/2] / pixel size of tile
		* = tile position of left side of character
		* 
		* -1 is for division clarification:
		* if object's right or bottom end is in pixel 30,
		* it is in the 1st tile, (tileSize = 30) not the 2nd
		* but / tileSize will = 1 (meant to be 0) hence -1.  
		*/
		int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        
        // frees object from blocked tiles if out of bounds
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
        
        /* 0 = pixels, 3x3 grid = tiles, x = object pixels
         * tl  tr
         * 000 000 000
         * 000 000 000
         * 00x xx0 000
         * bl  br
         * 00x xx0 000
         * 00x xx0 000
         * 00x xx0 000
         * 
         * returns type 0 = normal, 1 = blocked
         */
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        
        // true if tile is blocked
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
        
	}
	
	/**
	 * checkTileMapCollision is the main movement engine.
	 * this takes MapObject's dx and dy (movement info)
	 * and calculates whether they can be performed.
	 * If they can be performed, they are then performed.
	 * This updates xtemp and ytemp with the new positions
	 * of the MapObject
	 */
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;	// total pixel position / pixel size of tile
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		// saving x and y in temps
		xtemp = x;
		ytemp = y;
		
		// moving in the y direction (up/down)
		calculateCorners(x, ydest);
		if(dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				// currRow starts at 0, so to calculate pixel must +1
				// and subtract object height
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		// moving in the x direction (left/right)
		calculateCorners(xdest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest +1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return (int)width; }
	public int getHeight() { return (int)height; }
	public int getCWidth() { return (int)cwidth; }
	public int getCHeight() { return (int)cheight; }
	public boolean getFacingRight() { return facingRight;  }

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	/** notOnScreen() {
	 * return x + xmap + width < 0 ||
	 * x + xmap - width > GamePanel.WIDTH ||
	 * y + ymap + height < 0 ||
	 * y + ymap - height > GamePanel.HEIGHT;
	 * }
	 * 
	 * x - the position of the object relative to
	 * the top left corner of the map.
	 * xmap - the position of the top left corner of
	 * the map relative to top left corner of the
	 * screen.
	 * x + xmap - the position of the object relative
	 * to the top left corner of the screen.
	 * width and height are the sizes of the object 
	 * image.
	 */
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			// placing a negative width flips the image horizontally
			// x starting position is now the far end of the object
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
	}
	
}








