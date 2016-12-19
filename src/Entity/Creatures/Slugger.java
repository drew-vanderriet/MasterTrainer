package Entity.Creatures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import AI.Action;
import Entity.Animation;
import Entity.Creature;
import Handlers.Content;
import Handlers.Data;
import TileMap.TileMap;

public class Slugger extends Creature {
	
	// animations - based on sprite sheet
	private BufferedImage[] sprites;
	
	
	
	//=======================================
	// actions available to Slugger
	//=======================================
	// Action Decision Set indexes (Always Start from 7!)
	private static final int SLIME 			= 	7;

	// Static Parameters (Always include BLOCKCOST!):
	private static final int BLOCKCOST 		= 	50;

	private static final int SLIMERANGE 	= 	40;
	private static final int SLIMECOST 		= 	100;
	// Other Parameters:
	private int slimeDamage;

	//=======================================
	
	public Slugger(TileMap tm) {
		super(tm);
	}
	
	public void init() {
		
		super.init();
		// size of image and creature
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		dead = false;
		exploded = false;
		flinching = false;
		
//		type = Creature.PURPLE;
//		name = "Slugger";
		data.get(0).setData("Slugger");
		data.get(1).setData(Creature.PURPLE);
		
		// Creature's unique stats
		calStats();
		
		// adding Slugger's unique actions, 
		// always update block stats first!
		actionDecisionSet.get(Creature.D_BLOCK).setCost(BLOCKCOST);

		actionDecisionSet.add(new Action("slime", Action.MELEE, SLIMECOST, SLIMERANGE, slimeDamage));
		
		// load sprites
		sprites = Content.Slugger[0];
		
		animation = new Animation();
		animation.setFrames(sprites, 3);
		animation.setDelay(300);
		
		actionDecisionSet.get(D_RIGHT).setActive(true);
		actionDecisionSet.get(D_FACINGRIGHT).setActive(true);
		
	}
	
	//====================================================
	//-	-	-	-	-	Slugger's Stats	-	-	-	-	-
	//====================================================
	protected void calStats() {
		super.calStats();
		int strength = (int)data.get(STRENGTH).getData(Data.T_DOUBLE);
		// attributes
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		stopJumpSpeed = 0.3;
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		stopSpeed = 0.3;
		jumpStart = -3.5 - energy / 700;	// range: -1 - -5

		// attacks
		slimeDamage = 5 + (int)Math.round(strength) / 3;	// 1 - 50
	}
	//====================================================
	
	public boolean meleeAttack() {
		boolean attack = false;
		if(energy > actionDecisionSet.get(SLIME).getCost() && !actionDecisionSet.get(Creature.D_MELEE).isActive()) {
			//actionDecisionSet.get(SLIME).setActive(true);
			attack = true;
			// lose energy when scratching
			energy -= actionDecisionSet.get(SLIME).getCost();
			if(energy < 0) energy = 0;
			actionDecisionSet.get(SLIME).resetCounter();
		}
		return attack;
	}
	
	public void checkAttack(Creature c) {

		double tempDamage;

		// check scratch attack
		if(actionDecisionSet.get(SLIME).isActive() 
				&& actionDecisionSet.get(SLIME).getCounter() > 2 
				&& actionDecisionSet.get(SLIME).getCounter() < 4) {
			//critical hit!!
			tempDamage = checkCritical(actionDecisionSet.get(SLIME).getDamage());

			if(checkInRange(c, actionDecisionSet.get(SLIME).getRange())) {
				if(actionDecisionSet.get(D_FACINGRIGHT).isActive()) {
					// more damage from behind!
					if(c.getFacingRight()) tempDamage = tempDamage * BEHINDMULTI;
					// more damage from above!
					if(y < c.gety()) c.hit((int)(tempDamage * ABOVEMULTI), false);
					else c.hit((int)tempDamage, false);
				}
				else {
					// more damage from behind!
					if(!c.getFacingRight()) tempDamage = tempDamage * BEHINDMULTI;
					// more damage from above!
					if(y < c.gety()) c.hit((int)(tempDamage * ABOVEMULTI), true);
					else c.hit((int)tempDamage, true);					
				}
			}
		}

		// fireballs
		for(int j = 0; j < fireBalls.size(); j++) {
			if(fireBalls.get(j).intersects(c)) {
				if(fireBalls.get(j).getx() < c.getx()) c.hit(fireBalls.get(j).getDamage(), false);
				else c.hit(fireBalls.get(j).getDamage(), true);
				fireBalls.get(j).setHit();
				break;
			}
		}

	}

	private void actionStopped() {

		// check flinching has stopped
		if(flinching && flinchCounter > 40) flinching = false;

		// check attack has stopped
		if(currentAction == MELEE) {
			if(animation.hasPlayedOnce()) {
				actionDecisionSet.get(SLIME).setActive(false);
				actionDecisionSet.get(Creature.D_MELEE).setActive(false);
			}
		}

		// check if blocking has stopped
		if(currentAction == BLOCKING) {
			if(animation.hasPlayedOnce()) {
				actionDecisionSet.get(Creature.D_BLOCK).setActive(false);
			}
		}

		// check knockback has stopped
		if(currentAction == KNOCKBACK) {
			if(animation.hasPlayedOnce()) {
				knockback = false;
				falling = false;
			}
		}

	}

	protected void regenerate() {
		// updating energy
		super.regenerate();
		// updating everything else
		if(!isDead()) {
			jumpStart = -3.5 - energy / 700;	// range: -1 - -5
		}
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check actions have stopped
		actionStopped();
		// regenerate energy ability
		regenerate();
		
		
		//==================================
		//	-	-	Slugger Logic	-	-	
		//==================================
		// if hits wall, change direction
		if(actionDecisionSet.get(D_RIGHT).isActive() && dx == 0) {
			actionDecisionSet.get(D_RIGHT).setActive(false);
			actionDecisionSet.get(D_LEFT).setActive(true);
			actionDecisionSet.get(D_FACINGRIGHT).setActive(false);
		}
		else if(actionDecisionSet.get(D_LEFT).isActive() && dx == 0) {
			actionDecisionSet.get(D_RIGHT).setActive(true);
			actionDecisionSet.get(D_LEFT).setActive(false);
			actionDecisionSet.get(D_FACINGRIGHT).setActive(true);
		}
		//==================================
		
		super.update();
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();	// updating xmap and ymap
		
		drawDamage(g);
		
		// draw player
		if(flinching) {
			if(flinchCounter / 5 % 2 == 0) {
				return;
			}
		}
		
		if(notOnScreen()) return;
		
		super.draw(g);
		
	}

}









