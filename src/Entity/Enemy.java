package Entity;

import java.awt.Graphics2D;

import TileMap.TileMap;

public class Enemy extends MapObject{
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int fire;
	protected int maxFire;
	protected int fireDamage;
	
	protected int strength;
	protected int speed;
	protected int aggresion;
	protected int stamina;
	protected int focus;
	protected int defence;
	protected int courage;
	protected int independence;
	
	protected boolean flinching;
	protected long flinchTimer;

	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void hit(int damage) {
		if (dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
	
}












