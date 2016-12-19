package Entity.Creatures;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import AI.Action;
import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Creature;
import Entity.FireBall;
import Handlers.Content;
import Handlers.Data;
import TileMap.TileMap;

public class GreenDragon extends Creature {

	// animations - based on sprite sheet
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			2, 8, 1, 2, 4, 2, 5, 2
	};
	// animation actions - based on sprite sheet
	private static final int A_IDLE = 0;
	private static final int A_WALKING = 1;
	private static final int A_JUMPING = 2;
	private static final int A_FALLING = 3;
	private static final int A_GLIDING = 4;
	private static final int A_FIREBALL = 5;
	private static final int A_SCRATCHING = 6;
	public static final int A_KNOCKBACK = 7;
	public static final int A_BLOCKING = 8;



	//=======================================
	// actions available to Green Dragon
	//=======================================
	// Action Decision Set indexes (Always Start from 7!)
	private static final int SCRATCH 		= 7;
	private static final int FIRE 			= 8;

	// Static Parameters (Always include BLOCKCOST!):
	private static final int BLOCKCOST 		= 	50;

	private static final int SCRATCHRANGE 	= 	40;
	private static final int SCRATCHCOST 	= 	100;
	private static final int FIRECOST		= 	250;
	// Other Parameters:
	private int scratchDamage;
	private int fireBallDamage; 

	//=======================================

	public GreenDragon(TileMap tm) {
		// calls the super class constructor = MapObject
		super(tm);
	}

	public void init() {

		//super.init();
		// size of image and creature
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		dead = false;
		exploded = false;
		flinching = false;

//		type = Creature.GREEN;
//		name = "Green Dragon";
		data.get(0).setData("Green Dragon");
		data.get(1).setData(Creature.GREEN);

		// Creature's unique stats
		calStats();
		fireBalls = new ArrayList<FireBall>();

		// adding Green Dragon's unique actions, 
		// always update block stats first!
		actionDecisionSet.get(Creature.D_BLOCK).setCost(BLOCKCOST);

		actionDecisionSet.add(new Action("scratch", Action.MELEE, SCRATCHCOST, SCRATCHRANGE, scratchDamage));
		actionDecisionSet.add(new Action("fire ball", Action.RANGE, FIRECOST, 0, fireBallDamage));


		actionDecisionSet.get(D_FACINGRIGHT).setActive(true);

		// load sprites from content
		sprites = new ArrayList<BufferedImage[]>();

		sprites.add(Content.GreenDragon[0]);
		sprites.add(Content.GreenDragon[1]);
		sprites.add(Content.GreenDragon[2]);
		sprites.add(Content.GreenDragon[3]);
		sprites.add(Content.GreenDragon[4]);
		sprites.add(Content.GreenDragon[5]);
		sprites.add(Content.GreenDragonScratch[6]);
		sprites.add(Content.GreenDragon[6]);

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
		animation.setDelay(32);

	}

	//====================================================
	//	-	-	-	Green Dragon's Stats	-	-	-	-
	//====================================================
	protected void calStats() {
		super.calStats();
		int speed = (int)data.get(SPEED).getData(Data.T_DOUBLE);
		int strength = (int)data.get(STRENGTH).getData(Data.T_DOUBLE);
		// attributes
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		maxGlideSpeed = 1.0;
		stopJumpSpeed = 0.3;

		moveSpeed = 0.1 + speed / 100; // 0.1 - 1.1
		maxSpeed = 0.5 + speed * 2.5 / 100; // 0.5 - 3
		stopSpeed = 0.3 + speed * 1.2 / 100; // 0.3 - 1.5
		jumpStart = -3.5 - energy / 700;	// range: -1 - -5

		// attacks
		scratchDamage = 5 + (int)Math.round(strength / 3);	// 1 - 50
		fireBallDamage = energy / 100 + (int)Math.round(strength / 8);	// 1 - 35
	}
	//====================================================

	public boolean meleeAttack() {
		boolean attack = false;
		if(energy > actionDecisionSet.get(SCRATCH).getCost() && !actionDecisionSet.get(Creature.D_MELEE).isActive()) {
			//actionDecisionSet.get(SCRATCH).setActive(true);
			attack = true;
			// lose energy when scratching
			energy -= actionDecisionSet.get(SCRATCH).getCost();
			if(energy < 0) energy = 0;
			actionDecisionSet.get(SCRATCH).resetCounter();
		}
		return attack;
	}

	public boolean rangeAttack(Creature c, int t) {
		boolean attack = false;
		if(energy > actionDecisionSet.get(FIRE).getCost() && !actionDecisionSet.get(Creature.D_RANGE).isActive()) {
			//actionDecisionSet.get(FIRE).setActive(true);
			attack = true;
			energy -= actionDecisionSet.get(FIRE).getCost();
			FireBall fb = new FireBall(tileMap, actionDecisionSet.get(D_FACINGRIGHT).isActive(), 
					t, (int)checkCritical(fireBallDamage));
			fb.setPosition(x, y);
			firingDown = fb.direction(c);
			fireBalls.add(fb);
		}
		return attack;
	}

	public void checkAttack(Creature c) {

		double tempDamage;

		// check scratch attack
		if(actionDecisionSet.get(SCRATCH).isActive() 
				&& actionDecisionSet.get(SCRATCH).getCounter() > 2 
				&& actionDecisionSet.get(SCRATCH).getCounter() < 4) {
			//critical hit!!
			tempDamage = checkCritical(actionDecisionSet.get(SCRATCH).getDamage());

			if(checkInRange(c, actionDecisionSet.get(SCRATCH).getRange())) {
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
				actionDecisionSet.get(SCRATCH).setActive(false);
				actionDecisionSet.get(Creature.D_MELEE).setActive(false);
			}
		}
		if(currentAction == RANGE) {
			if(animation.hasPlayedOnce()) {
				actionDecisionSet.get(FIRE).setActive(false);
				actionDecisionSet.get(Creature.D_RANGE).setActive(false);
				firingDown = false;
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
			fireBallDamage = energy / 100 + (int)Math.round(data.get(STRENGTH).getData(Data.T_DOUBLE) / 8);	// 1 - 35
			jumpStart = -3.5 - energy / 700;	// range: -1 - -5
		}
	}

	private void setAnimation() {

		if(actionDecisionSet.get(SCRATCH).isActive()) {
			if(currentAction != MELEE) {
				AudioPlayer.play("scratch");
				currentAction = MELEE;
				animation.setFrames(sprites.get(A_SCRATCHING), numFrames[A_SCRATCHING]);
				animation.setDelay(4);
				width = 60;
			} 
		}
		else if(actionDecisionSet.get(FIRE).isActive() || firingDown) {
			if(currentAction != RANGE) {
				AudioPlayer.play("fireball");
				currentAction = RANGE;
				animation.setFrames(sprites.get(A_FIREBALL), numFrames[A_FIREBALL]);
				animation.setDelay(8);
				width = 30;
			} 
		}
		else if(knockback) {
			if(currentAction != KNOCKBACK) {
				currentAction = KNOCKBACK;
				animation.setFrames(sprites.get(A_KNOCKBACK), numFrames[A_KNOCKBACK]);
				animation.setDelay(15);
				width = 30;
			} 
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(A_GLIDING), numFrames[A_GLIDING]);
					animation.setDelay(8);
					width = 30;
				} 
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(A_FALLING), numFrames[A_FALLING]);
				animation.setDelay(8);
				width = 30;
			} 
		}
		else if (dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(A_JUMPING), numFrames[A_JUMPING]);
				animation.setDelay(-1);
				width = 30;

			} 
		} 
		else if(actionDecisionSet.get(D_LEFT).isActive() || actionDecisionSet.get(D_RIGHT).isActive()) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(A_WALKING), numFrames[A_WALKING]);
				animation.setDelay(11 - (int)Math.round(data.get(SPEED).getData(Data.T_DOUBLE) / 8) / 150);
				width = 30;
			} 
		}
		else if(actionDecisionSet.get(Creature.D_BLOCK).isActive()) {
			if(currentAction != BLOCKING) {
				currentAction = BLOCKING;
				// Blocking uses jumping animation for now
				animation.setFrames(sprites.get(A_JUMPING), numFrames[A_JUMPING]);
				animation.setDelay(70);
				width = 30;
			} 
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(A_IDLE), numFrames[A_IDLE]);
				animation.setDelay(32);
				width = 30;
			}
		} 

	}

	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// Update Counters
		actionDecisionSet.get(SCRATCH).updateCounter();
		
		// check actions have stopped
		actionStopped();
		// regenerate energy ability
		regenerate();

		// fireball attack
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}

		// set animation
		setAnimation();

		super.update();

	}

	public void draw(Graphics2D g) {

		// setting map position is 1st thing to always
		// do in draw!!
		setMapPosition();

		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}

		drawDamage(g);

		// draw player
		if(flinching) {
			if(flinchCounter / 5 % 2 == 0) {
				return;
			}
		}

		// draw rotated FIREBALL image for firingDown
		if(firingDown && actionDecisionSet.get(D_FACINGRIGHT).isActive()) {
			// AffineTransform is used to manipulate images 
			AffineTransform at = new AffineTransform();
			// moves transform position to player position
			at.translate(
					x + xmap, 
					y + ymap
					);
			// rotate transform by 90 degrees
			at.rotate(Math.PI / 2);
			// corrects position after rotation
			// moves along original axis
			at.translate(
					- width / 2, 
					- height / 2
					);
			// draws the transform of the image in  
			// the position set by at.translate
			g.drawImage(
					animation.getImage(),
					at,
					null
					);
		}
		else if(firingDown) {
			AffineTransform at = new AffineTransform();
			at.translate(
					x + xmap, 
					y + ymap
					);
			at.rotate(Math.PI/2);
			at.translate(
					- width / 2, 
					- height / 2
					);
			// the getScaledInstance scales the transform to 
			// 1 in x, -1 in y, flipping the image vertically.
			// This transform flips along original image axis
			// and so even though the image is rotated already
			// it flips the image head and toe. 
			at.concatenate(AffineTransform.getScaleInstance(1, -1));
			// corrections the position, moving the transform
			// in along its original y-axis
			at.concatenate(
					AffineTransform.getTranslateInstance(
							0, 
							- height
							)
					);
			g.drawImage(
					animation.getImage(),
					at,
					null
					);
		}
		else super.draw(g);



	}


}
