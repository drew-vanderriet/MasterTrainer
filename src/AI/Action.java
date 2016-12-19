package AI;

public class Action {

	// Object characteristics
	// Info for action
	private String name;
	private int type;
	private int cost;
	private int range;
	private int damage;
	private int counter;
	private int occure;
	private boolean active;
	
	
	// types of decisions
	public static final int MOVEMENT = 0;
	public static final int DEFENSE = 1;
	public static final int MELEE = 2;
	public static final int RANGE = 3;
	
	/**
	 * Creates an action object that can be performed by the 
	 * creature. 
	 * @param n - name
	 * @param t - type (Action.___)
	 * @param c - cost
	 * @param r - range
	 * @param d - damage
	 */
	public Action(String n, int t, int c, int r, int d) {
		name = n;
		type = t;
		cost = c;
		range = r;
		damage = d;
		active = false;
		occure = 0;
	}
	
	public boolean isActive() { return active; }
	public void setActive(boolean a) { 
		active = a;
		if(a) occure++;
		}
	public void setCost(int c) { cost = c; }
	public void updateCounter() { counter++; }
	public void resetCounter() { counter = 0; }
	
	public String getName() { return name; }
	public int getType() { return type; }
	public int getCost() { return cost; }
	public int getRange() { return range; }
	public int getDamage() { return damage; }
	public int getCounter() { return counter; }
	public int getOccurance() { return occure; }
	
}
