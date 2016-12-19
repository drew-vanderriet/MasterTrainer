package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import AI.Action;
import AI.Strategy;
import Audio.AudioPlayer;
import Entity.Effects.Effects;
import Handlers.Data;
import TileMap.TileMap;

public class Creature extends MapObject {
	
	public Strategy strategy;
	
	// Creature Stats
	protected int health;
	private static final int HEALTHMULTI = 2;
	
	private double healthUp;
	private double strengthUp;
	private double speedUp;
	private double staminaUp;
	private double defenseUp;
	private double focusUp;
	
	protected int level;
	
	protected int moveCounter;
	
	protected int levelUpXP;
	protected int upLevel;
	private static final double LEVELMULTI = 1.5;
	
	//load/save data
	public ArrayList<Data> data;
	
	// Directory for Data
	public static final int	NAME	= 0;
	public static final int	TYPE	= 1;
	public static final int	MAXHEALTH	= 2;
	public static final int	STRENGTH	= 3;
	public static final int	SPEED		= 4;
	public static final int	STAMINA		= 5;
	public static final int	FOCUS		= 6;
	public static final int	DEFENSE		= 7;
	public static final int	AGGRESSION	= 8;
	public static final int	COURAGE		= 9;
	public static final int	INDEPENDENCE = 10;
	public static final int	XP			= 11;
	
	protected boolean dead;
	protected boolean exploded;
	protected boolean retreat;
	protected int retreatCounter;
	
	// flinching
	protected boolean flinching;
	protected int flinchCounter;
	protected boolean knockback;

	// range
	protected int energy;
	protected int maxEnergy;
	protected boolean firingDown;
	public ArrayList<FireBall> fireBalls;

	
	protected static final double BEHINDMULTI = 1.3;
	protected static final double ABOVEMULTI = 1.2;
	
	protected int blockStrength;
	
	// gliding
	protected boolean gliding;
	protected double maxGlideSpeed;
	
	// Types
	public static final int GREEN = 0;
	public static final int BLUE = 1;
	public static final int PURPLE = 2;
	
	// bounce back during collision
	private static final int BOUNCE = 2;

	// orders for all creatures
	public static final int ORD_BLOCK = 0;
	public static final int ORD_DODGE = 1;
	public static final int ORD_COUNTER = 2;
	
	public static final int ORD_CLOSE = 3;
	public static final int ORD_RANGE = 4;
	public static final int ORD_REST = 5;

	// actions for combat and currentAction
	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int GLIDING = 4;
	public static final int RANGE = 5;
	public static final int MELEE = 6;
	public static final int BLOCKING = 7;
	public static final int KNOCKBACK = 8;
	
	protected ArrayList<Action> actionDecisionSet;
	// Action Decision Set Directory;
	public static final int D_LEFT = 0;
	public static final int D_RIGHT = 1;
	public static final int D_FACINGRIGHT = 2;
	public static final int D_JUMP = 3;
	public static final int D_BLOCK = 4;
	public static final int D_MELEE = 5;
	public static final int D_RANGE = 6;

	private int battleCounter = 0;

	// display damage
	private int dispDamage;
	private int dispHeight;
	private int dispDamageCounter;
	private int xpos;
	private int ypos;
	private boolean savePos;
	private boolean damageDisplayed;
	private boolean hurt;
	private Font damageFont;
	
	public Effects effects;

	public Creature(TileMap tm) {
		super(tm);
		effects = new Effects(tm);
		moveCounter = 0;
		retreat = false;
		strategy = new Strategy();
		
		damageFont = new Font("Arial", Font.PLAIN, 16);
		
		healthUp = 0;
		strengthUp = 0;
		speedUp = 0;
		staminaUp = 0;
		defenseUp = 0;
		focusUp = 0;

		// Add default movement actions:
		actionDecisionSet = new ArrayList<Action>();
		actionDecisionSet.add(new Action("left", Action.MOVEMENT, 0, 0, 0));
		actionDecisionSet.add(new Action("right", Action.MOVEMENT, 0, 0, 0));
		actionDecisionSet.add(new Action("facing right", Action.MOVEMENT, 0, 0, 0));
		actionDecisionSet.add(new Action("jump", Action.MOVEMENT, 0, 0, 0));
		actionDecisionSet.add(new Action("block", Action.DEFENSE, 0, 0, 0));
		actionDecisionSet.add(new Action("melee", Action.MELEE, 0, 0, 0));
		actionDecisionSet.add(new Action("range", Action.RANGE, 0, 0, 0));
		
		// adding default data
		addData();

	}
	
	private void addData() {
		// Add default data into data array list
		data = new ArrayList<Data>();
		// adding creature info
		data.add(new Data("name", Data.T_STRING));
		data.add(new Data("type", Data.T_INT));

		// adding creature stats
		data.add(new Data("health", Data.T_DOUBLE));
		data.add(new Data("strength", Data.T_DOUBLE));
		data.add(new Data("speed", Data.T_DOUBLE));
		data.add(new Data("stamina", Data.T_DOUBLE));
		data.add(new Data("focus", Data.T_DOUBLE));
		data.add(new Data("defense", Data.T_DOUBLE));
		data.add(new Data("aggression", Data.T_INT));
		data.add(new Data("courage", Data.T_INT));
		data.add(new Data("independence", Data.T_INT));
		data.add(new Data("XP", Data.T_INT));
		
	}
	
	public void init() {}
	public boolean isDead() { return dead; }
	public boolean isFlinching() { return flinching; }
	public void setExploded(boolean b) { exploded = b; }
	public boolean Exploded() {return exploded; }

	public int getHealth() { return health; }
	public int getMaxHealth() { return (int)Math.round(data.get(MAXHEALTH).getData(Data.T_DOUBLE)); }
	public int getEnergy() { return energy; }
	public int getMaxEnergy() { return maxEnergy; }
	public int getCurrentAction() { return currentAction; }
	public int getType() { return data.get(TYPE).getData(Data.T_INT); }
	public int getXP() { return data.get(XP).getData(Data.T_INT); }
	public Strategy getStrategy() { return strategy; }
	public ArrayList<Action> getActionSet() { return actionDecisionSet; }
	public ArrayList<Data> getData() { return data; }
	
	public int getBattleCounter() { return battleCounter; }
	public int getMoveCounter() { return moveCounter; }
	public int getRetreatCounter() { return retreatCounter; }
	public boolean getRetreat() { return retreat; }
	public void setRetreat(boolean r) { retreat = r; }
	public void resetMoveCounter() { moveCounter = 0; }
	public void resetRetreatCounter() { retreatCounter = 0; }
	
	public void setData(ArrayList<Data> info) { data = info; }
	
	public int getLevel() { 
		// calculate creature level
		level =  (int)(
				( (data.get(MAXHEALTH).getData(Data.T_DOUBLE) / HEALTHMULTI) 
						+ data.get(STRENGTH).getData(Data.T_DOUBLE) 
						+ data.get(SPEED).getData(Data.T_DOUBLE) 
						+ data.get(STAMINA).getData(Data.T_DOUBLE) 
						+ data.get(FOCUS).getData(Data.T_DOUBLE) 
						+ data.get(DEFENSE).getData(Data.T_DOUBLE)) / 6
				);
		return level; 
		}
	public int getLevelUpXP() {
		return levelUpXP = (int)(getLevel() * getLevel() * LEVELMULTI);
	}
	public String getName() { return data.get(NAME).getData(Data.T_STRING); }
	
	public String[] gainXP(int oppLevel) {
		int lvl = getLevel();
		int lvlupxp = getLevelUpXP();
		upLevel = 0;
		// adding new xp
		//xp = xp + oppLevel * oppLevel;
		data.get(XP).setData(data.get(XP).getData(Data.T_INT) + oppLevel * oppLevel);
		// checking if level up!
		while(data.get(XP).getData(Data.T_INT) >= lvlupxp) {
			// subtract xp
			//xp = xp - lvlupxp;
			data.get(XP).setData(data.get(XP).getData(Data.T_INT) - lvlupxp);
			// go up a level
			upLevel++;
			lvl++;
			// calculate new level up XP
			lvlupxp = lvl * lvl * 2;
		}
		// keeping track of creature's actions
		// Max Health 		- 	health % left at end of fight
		// stamina 			-	energy, jumping and fireball
		// speed 			-	moving
		// defense 			-	blocking
		// strength 		-	scratching
		// focus 			-	critical hits and fireball
		
//		healthUp = 100 - (health / getMaxHealth()) * 100;
//		strengthUp = actionDecisionSet.get(D_MELEE);
//		speedUp = speedUp + ;
//		staminaUp = staminaUp + ;
//		defenseUp = defenseUp + ;
//		focusUp = focusUp + ;
		
		// adding creature stats if level up!
		return levelUp(upLevel);
	}
	
	private String[] levelUp(int lvls) {
		// levling up gives 6 skill points based on performance.
		/* Stats are based on:
		 * Max Health 	- 	health
		 * stamina 		-	energy, jumping and fireball
		 * speed 		-	moving
		 * defense 		-	blocking
		 * strength 	-	scratching
		 * focus 		-	critical hits and fireball
		 * 
		 * therefore:
		 * WALKING 		= speed
		 * JUMPING 		= stamina and speed
		 * RANGE 		= stamina and focus
		 * MELEE	 	= strength
		 * BLOCKING 	= defense
		 * KNOCKBACK 	= max Health
		 * Hit 			= bonus to stats
		 * Critical 	= focus
		 * 
		 * To calculate level gains, points need to be awarded to each of
		 * the 6 skill areas after each battle. These points should be 
		 * standardized. Once a level up is reached, the 6 skill points 
		 * must be assigned to the skills according to their points.
		 * 
		 * eg. points are:
		 * Max Health 	- 	4
		 * stamina 		-	5
		 * speed 		-	3
		 * defense 		-	1
		 * strength 	-	2
		 * focus 		-	3
		 * 
		 * sum of points is: 18, which divided by 6 is 3 points = 1 skill up.
		 * therefore:
		 * Max Health 	- 	4 = 1.3 skill up = +1
		 * stamina 		-	5 = 1.6 skill up = +2
		 * speed 		-	3 = 1 skill up = +1
		 * defense 		-	1 = 0.3 skill up = +0
		 * strength 	-	2 = 0.6 skill up = +1
		 * focus 		-	3 = 1 skill up = +1
		 * 
		 * eg. points are:
		 * Max Health 	- 	8
		 * stamina 		-	5
		 * speed 		-	7
		 * defense 		-	3
		 * strength 	-	9
		 * focus 		-	2
		 * 
		 * sum of points is: 34, which divided by 6 is 5.6 points = 1 skill up.
		 * therefore:
		 * Max Health 	- 	8 = 1.4 skill up = +1
		 * stamina 		-	5 = 0.9 skill up = +1
		 * speed 		-	7 = 1.2 skill up = +1
		 * defense 		-	3 = 0.5 skill up = +1
		 * strength 	-	9 = 1.6 skill up = +2
		 * focus 		-	2 = 0.3 skill up = +0
		 */
		String[] info = {"",""};

		
		while(lvls > 0) {
			// using one level up
			lvls--;
			// adding creature stats
			//TODO allow player to choose what stats to gain/base growth on creatures performance.
//			maxHealth = maxHealth + 1 * HEALTHMULTI;
//			strength = strength + 1;
//			speed = speed + 1;
//			stamina = stamina + 1;
//			focus = focus + 1;
//			defense = defense + 1;
			
			data.get(MAXHEALTH).setData( data.get(MAXHEALTH).getData(Data.T_DOUBLE) + 1 * HEALTHMULTI );
			data.get(STRENGTH).setData( data.get(STRENGTH).getData(Data.T_DOUBLE) + 1 );
			data.get(SPEED).setData( data.get(SPEED).getData(Data.T_DOUBLE) + 1 );
			data.get(STAMINA).setData( data.get(STAMINA).getData(Data.T_DOUBLE) + 1 );
			data.get(FOCUS).setData( data.get(FOCUS).getData(Data.T_DOUBLE) + 1 );
			data.get(DEFENSE).setData( data.get(DEFENSE).getData(Data.T_DOUBLE) + 1 );

			
			//TODO show improvements on screen
			info[0] = "Level Up!!!";
			info[1] = "New Level: " + Integer.toString(getLevel());
		}
		return info;
	}
	
	public void setLevel(int[] levelSet) {
		// extracting creature data from array
		
		data.get(MAXHEALTH).setData( (double)levelSet[0] );
		
		data.get(STRENGTH).setData( (double)levelSet[1] );
		data.get(SPEED).setData( (double)levelSet[2] );
		data.get(STAMINA).setData( (double)levelSet[3] );
		data.get(FOCUS).setData( (double)levelSet[4] );
		data.get(DEFENSE).setData( (double)levelSet[5] );
		
		data.get(AGGRESSION).setData( levelSet[6] );
		data.get(COURAGE).setData( levelSet[7] );
		data.get(INDEPENDENCE).setData( levelSet[8] );
		
		data.get(XP).setData( levelSet[9] );
		// calculate creature stats from level info
		calStats();
		
	}
	
	public void randomLevel(int level, int levelRange, int skillRange) {
		int newLevel = level - levelRange + (int)(2 * levelRange * Math.random());
		// setting limits on levels
		if(newLevel < 1) { newLevel = 1; }
		if(newLevel > 100) { newLevel = 100;}
		
		data.get(MAXHEALTH).setData( (double)(
				HEALTHMULTI * (newLevel - skillRange + (int)(2 * skillRange * Math.random()))
				) );
		if(data.get(MAXHEALTH).getData(Data.T_DOUBLE) < 1) { data.get(MAXHEALTH).setData( (double) 1); }
		
		data.get(STRENGTH).setData( (double)(
				newLevel - skillRange + (int)(2 * skillRange * Math.random())
				) );
		if(data.get(STRENGTH).getData(Data.T_DOUBLE) < 1) { data.get(STRENGTH).setData( (double) 1); }
		data.get(SPEED).setData( (double)(
				newLevel - skillRange + (int)(2 * skillRange * Math.random())
				) );
		if(data.get(SPEED).getData(Data.T_DOUBLE) < 1) { data.get(SPEED).setData( (double) 1); }
		data.get(STAMINA).setData( (double)(
				newLevel - skillRange + (int)(2 * skillRange * Math.random())
				) );
		if(data.get(STAMINA).getData(Data.T_DOUBLE) < 1) { data.get(STAMINA).setData( (double) 1); }
		data.get(FOCUS).setData( (double)(
				newLevel - skillRange + (int)(2 * skillRange * Math.random())
				) );
		if(data.get(FOCUS).getData(Data.T_DOUBLE) < 1) { data.get(FOCUS).setData( (double) 1); }
		data.get(DEFENSE).setData( (double)(
				newLevel - skillRange + (int)(2 * skillRange * Math.random())
				) );
		if(data.get(DEFENSE).getData(Data.T_DOUBLE) < 1) { data.get(DEFENSE).setData( (double) 1); }
		
		// randomly assigning aggression, courage and independence values.
		data.get(AGGRESSION).setData( (int)(100 * Math.random()) );
		data.get(COURAGE).setData( (int)(100 * Math.random()) );
		data.get(INDEPENDENCE).setData( 70 + (int)(30 * Math.random()) ); // independence is high for new creatures
		
		data.get(XP).setData( 0 ); // setting XP of new creature to 0
		
		// calculate creature stats from level info
		calStats();
		
		//addData();
	}
	
	protected void calStats() {
		health = (int)Math.round(data.get(MAXHEALTH).getData(Data.T_DOUBLE));
		energy = maxEnergy = 500 + (int)Math.round(data.get(STAMINA).getData(Data.T_DOUBLE)) * 20; 	// 500 - 2500 total
		blockStrength = (int)Math.round(data.get(DEFENSE).getData(Data.T_DOUBLE));
		
		
		/* Stats are based on:
		 * Max Health 	- 	health
		 * stamina 		-	energy, jumping and fireball
		 * speed 		-	moving
		 * defense 		-	blocking
		 * strength 	-	scratching
		 * focus 		-	critical hits and fireball
		 * 
		 * therefore:
		 * WALKING 		= speed
		 * JUMPING 		= stamina and speed
		 * RANGE 		= stamina and focus
		 * MELEE 		= strength
		 * BLOCKING 	= defense
		 * KNOCKBACK 	= max Health
		 * Hit 			= bonus to stats
		 * Critical 	= focus
		 */
		
		
	}
	
	public void updateActions(ArrayList<Action> a) { 
		// update all actions in decision set
		actionDecisionSet = a;
		
		// update facingRight - compatibility with old code
		facingRight = actionDecisionSet.get(D_FACINGRIGHT).isActive();
		}

	
	public void checkCreatureCollision(Creature c) {
		
		// check creature collision
		if(x < c.getx()) {
			if(
					c.getx() > x &&
					c.getx() < x + width / 2 &&
					c.gety() > y - height / 2 &&
					c.gety() < y + height / 2
					) {
				// bounce away if contact
				dx -= BOUNCE;
				if(dx < -3) dx = -3;
				if(y < c.gety()) dy -= BOUNCE;
				if(dy < -5) dy = -5;
			}
		}
		else if(x > c.getx()) {
			if(
					c.getx() < x &&
					c.getx() > x - width / 2 &&
					c.gety() > y - height / 2 &&
					c.gety() < y + height / 2
					) {
				// bounce away if contact
				dx += BOUNCE;
				if(dx > 3) dx = 3;
				if(y < c.gety()) dy -= BOUNCE;
				if(dy < -5) dy = -5;
			}
		}
		else {
			if(
					c.gety() > y - height / 2 &&
					c.gety() < y + height / 2
					) {
				// bounce away if contact
				if(Math.random() < 0.5) {
					dx += BOUNCE;
					if(dx > 3) dx = 3;
				}
				else {
					dx -= BOUNCE;
					if(dx < -3) dx = -3;
				}
				if(y < c.gety()) dy -= BOUNCE;
				if(dy < -5) dy = -5;
			}
		}
	}

	protected double checkCritical(double damage) {
		//critical hit!!
		if(Math.random() * 400 < data.get(FOCUS).getData(Data.T_DOUBLE)) {
			AudioPlayer.play("critical");
			if(actionDecisionSet.get(D_FACINGRIGHT).isActive()) effects.addExplosion((int)(x + cwidth / 2), (int)y);
			else effects.addExplosion((int)(x - cwidth / 2), (int)y);
			
			return damage * 1.5;
		}
		else return damage;
	}
	
	protected boolean checkInRange(Creature c, int range) {
		boolean inRange = false;
		
		if(actionDecisionSet.get(D_FACINGRIGHT).isActive()) {
			if(
					c.getx() > x &&
					c.getx() < x + range &&
					c.gety() > y - height / 2 &&
					// if sitting on top of creature, can still hit!
					c.gety() < y + 10 + height / 2
					) {
				inRange = true;
			}
		}
		else {
			if(
					c.getx() < x &&
					c.getx() > x - range &&
					c.gety() > y - height / 2 &&
					// if sitting on top of creature, can still hit!
					c.gety() < y + 10 + height / 2
					) {
				inRange = true;					
			}
		}
		
		return inRange;
	}
	
	public void checkAttack(Creature c) {}

	public void hit(int damage, boolean knockLeft) {
		
		if (dead || flinchCounter < 40) return;
		// if blocking, reduce damage by block stat
		if(currentAction == BLOCKING && energy > actionDecisionSet.get(D_BLOCK).getCost()) {
			damage = (damage * (100 - blockStrength)) / 100; 
			// weaken defense with every hit
			blockStrength -= 2;
			energy -= actionDecisionSet.get(D_BLOCK).getCost();
			// set block animation
			AudioPlayer.play("block");
			if(knockLeft) effects.addBlock((int)(x + cwidth / 2), (int)y);
			else effects.addBlock((int)(x - cwidth / 2), (int)y);
		}
		else AudioPlayer.play("hit");
		health -= (int) damage;
		
		// Display damage
		if(damage > 0 && !hurt) {
			dispDamage = (int) damage;
			dispDamageCounter = 0;
			hurt = true;
		}
		
		if(health < 0) health = 0;
		if(health == 0) {
			dead = true;
			AudioPlayer.play("dead");
		}
		flinching = true;
		flinchCounter = 0;
		
		actionDecisionSet.get(D_LEFT).setActive(false);
		actionDecisionSet.get(D_RIGHT).setActive(false);
		if(currentAction == BLOCKING 
				&& energy + actionDecisionSet.get(D_BLOCK).getCost() > actionDecisionSet.get(D_BLOCK).getCost()) 
		{
			if(knockLeft) dx = -0.5 - damage / 10; // -0.5 - -3
			else dx = 0.5 + damage / 10;
		}
		else {
			if(knockLeft) dx = -0.5 - damage / 10; // -0.5 - -3
			else dx = 0.5 + damage / 10;
			dy = -1.5 - damage / 10;
			knockback = true;
			falling = true;
			actionDecisionSet.get(D_JUMP).setActive(false);
		}	
	}

	public boolean meleeAttack() { 
		return false; 
		}
	
	public boolean rangeAttack(Creature c, int t) { 
		return false; 
		}
	
	public void getNextPosition() {

		if(knockback) {
			dy += fallSpeed * 2;
			return;
		}

		// movement
		if(actionDecisionSet.get(D_LEFT).isActive()) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if (actionDecisionSet.get(D_RIGHT).isActive()) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking or blocking, except in air
		if(
				(currentAction == MELEE || currentAction == RANGE || 
				currentAction == BLOCKING) && !(actionDecisionSet.get(D_JUMP).isActive() || falling)) {
			dx = 0;
		}

		// jumping
		if(actionDecisionSet.get(D_JUMP).isActive() && !falling) {
			// using HashMap sfx to load
			// object jump
			AudioPlayer.play("jump");
			dy = jumpStart;
			falling = true;
		}

		// falling
		if(falling) {

			if(dy > 0 && gliding) {
				dy += fallSpeed * 0.1;
				if(dy > maxGlideSpeed) dy = maxGlideSpeed;
			}
			else dy += fallSpeed;

			if(dy > 0) actionDecisionSet.get(D_JUMP).setActive(false);
			// makes that the longer you hold down jump
			// the higher you jump
			if(dy < 0 && !actionDecisionSet.get(D_JUMP).isActive()) dy += stopJumpSpeed;

			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void drawDamage(Graphics2D g) {
		if(dead && !savePos) {
			savePos = true;
			xpos = (int)(x - 5);
			ypos = (int)(y);
		}
		else if(!savePos && !dead) {
			xpos = (int)(x - 5);
			ypos = (int)(y);
		}
		// display damage
		if(hurt && dispDamageCounter < 40) {

			if(damageDisplayed) {
				dispHeight++;
			}
			else  {
				dispHeight = 20;
				damageDisplayed = true;
			}

			g.setColor(Color.RED);
			g.setFont(damageFont);
			g.drawString(Integer.toString(dispDamage), xpos + (int)xmap, (int)(ypos + (int)ymap - dispHeight));
		}
		else {
			hurt = false;
			damageDisplayed = false;
			savePos = false;
		}
	}
	
	protected void regenerate() {
		if(!isDead()) {
			energy += 1;
			if(energy > maxEnergy) energy = maxEnergy;
		}
	}
	
	public void update() {

		// Update counters
		battleCounter++;
		flinchCounter++;
		moveCounter++;
		retreatCounter++;
		dispDamageCounter++;
		
		animation.update();
		
		effects.update();

	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
		effects.draw(g);
	}
	
	
}

