package Entity.Effects;

import java.awt.Graphics2D;
import java.util.ArrayList;

import TileMap.TileMap;

public class Effects {
	
	protected TileMap tileMap;

	private ArrayList<Explosion> explosions;
	
	public static final int EXPLOSION = 0;
	public static final int BLOCK = 1;
	public static final int DEAD = 2;

	
	public Effects(TileMap tm) {
		tileMap = tm;
		explosions = new ArrayList<Explosion>();
		
	}
	
	public void addExplosion(int x, int y) {
		explosions.add(new Explosion(EXPLOSION, x, y));
	}
	public void addBlock(int x, int y) {
		explosions.add(new Explosion(BLOCK, x, y));
	}
	public void addDead(int x, int y) {
		explosions.add(new Explosion(DEAD, x, y));
	}
	
	public void update() {
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
			explosions.get(i).draw(g);
		}

	}

}
