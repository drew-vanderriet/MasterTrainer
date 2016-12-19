package AI;

import java.util.ArrayList;

import Entity.Creature;
import Handlers.Data;

public class DetermineAction {
	
	/*
	 * This class receives a set of options and info about the creature and  
	 * opponent and returns the action that it chooses.
	 * 
	 * =========================================================================
	 * TODO This class needs to know the options available to the creature.
	 * This proves to be difficult, as how the code is written, the coder needs to 
	 * know the options available and hard code them in.
	 * A more generic approach needs to be designed to cater for a wide variety of
	 * different attacks. A suggest to solve this is to categorize the different 
	 * possibilities of attacks, close; range ect. and design decision trees to 
	 * choose between the generic attacks. Then the creature can have info included
	 * in with the attack lists that show favourites in the possible attack options.
	 * This decision can simply loop through all attack options and make a roll for
	 * attack, and then select the attack with the highest roll.
	 */
	
	private ArrayList<Action> actions;
	private Creature c;
	private Strategy strategy;
	
	private int xDistance;
	private int yDistance;
	private boolean charge;
	private boolean onRight;
	private boolean above;
	
	public DetermineAction() {
		//strategy = new Strategy();
	}
	
	//=================================================
	//	-	-	-	-	-	Defense	-	-	-	-	-
	private void determineDefense() {
		// roll for each defense action, select the action with the highest roll
		actions.get(Creature.D_RIGHT).setActive(false);
		actions.get(Creature.D_LEFT).setActive(false);
		// first 4 actions are always movement
		for(int j = 4; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.DEFENSE) {
				// just selects 1st defense
				actions.get(j).setActive(true);
				break;
			}
		}
	}
	private boolean defenseCost() {
		for(int j = 4; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.DEFENSE) {
				// just selects 1st defense cost
				if(c.getEnergy() > actions.get(j).getCost()) return true;
			}
		}
		return false;
	}
	
	//=================================================
	//	-	-	-	-	Melee Attacks	-	-	-	-
	private void determineMelee() {
		actions.get(Creature.D_FACINGRIGHT).setActive(onRight);
		// roll for each melee action, select the action with the highest roll
		for(int j = 7; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.MELEE) {
				// just selects 1st melee
				if(c.meleeAttack()) {
					// activating selected attack 
					actions.get(j).setActive(true);
					// marking a melee attack as active.
					actions.get(Creature.D_MELEE).setActive(true);
				}
				break;
			}
		}
	}
	private boolean meleeCost() {
		for(int j = 7; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.MELEE) {
				// just selects 1st melee cost
				return (c.getEnergy() > actions.get(j).getCost());
			}
		}
		return false;
	}
	/**
	 * Returns true if: 
	 * xDistance is less than melee attack range * multi.
	 * Use multi = 1 for default check.
	 * @param multi
	 * @return
	 */
	private boolean meleeRange(int multi) {
		for(int j = 7; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.MELEE) {
				// just selects 1st melee range
				return (xDistance < actions.get(j).getRange() * multi);
			}
		}
		return false;
	}
	
	//=================================================
	//	-	-	-	-	Range Attacks	-	-	-	-
	private void determineRange(Creature o) {
		actions.get(Creature.D_FACINGRIGHT).setActive(onRight);
		//TODO loop through all actions and extract all range ones
		// roll for each range action, select the action with the highest roll
		for(int j = 7; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.RANGE) {
				// just selects 1st range
				if(c.rangeAttack(o, c.getType())) {
					// setting attack active
					actions.get(j).setActive(true);
					// setting range attack active
					actions.get(Creature.D_RANGE).setActive(true);
				}
				break;
			}
		}
	}
	private boolean rangeCost() {
		for(int j = 7; j < actions.size(); j++) {
			if(actions.get(j).getType() == Action.RANGE) {
				// just selects 1st range cost
				if(c.getEnergy() > actions.get(j).getCost()) return true;
			}
		}
		return false;
	}
	
	// probability functions to determine the
	// success of the attempted action
	private boolean tryJump() {		// requires speed and stamina
		return Math.random() * 100 < 40 + c.data.get(Creature.SPEED).getData(Data.T_DOUBLE) 
				&& Math.random() * 100 < 40 + c.getEnergy() / 10;
	}
	private boolean tryBlock() { 	// requires defense and strength
		return Math.random() * 100 < c.data.get(Creature.DEFENSE).getData(Data.T_DOUBLE) 
				&& Math.random() * 100 < c.data.get(Creature.STRENGTH).getData(Data.T_DOUBLE) && defenseCost();
	}
	private boolean tryScratch() {	// requires aggression and courage
		return Math.random() * 100 < 30 + c.data.get(Creature.AGGRESSION).getData(Data.T_INT) && 
				Math.random() * 100 < 30 + c.data.get(Creature.COURAGE).getData(Data.T_INT) && meleeCost();
	}
	private boolean tryFireball() {	// requires focus and aggression
		return Math.random() * 100 < 30 + c.data.get(Creature.FOCUS).getData(Data.T_DOUBLE) && 
				Math.random() * 100 < 30 + c.data.get(Creature.AGGRESSION).getData(Data.T_INT) && rangeCost();
	}
	private boolean tryCharge() {	// requires courage and speed
		return Math.random() * 100 < 40 + c.data.get(Creature.COURAGE).getData(Data.T_INT) && 
				Math.random() * 100 < 60 + c.data.get(Creature.SPEED).getData(Data.T_DOUBLE);
	}
	private boolean tryRetreat() {	// requires stamina and speed
		return Math.random() * 100 < 20 + c.data.get(Creature.STAMINA).getData(Data.T_DOUBLE) && 
				Math.random() * 100 < 20 + c.data.get(Creature.SPEED).getData(Data.T_DOUBLE)
				&& xDistance < 150;
	}
	private boolean tryIndependence() {	// requires independence
		return Math.random() * 100 < c.data.get(Creature.INDEPENDENCE).getData(Data.T_INT);
	}

	private boolean setDefense(Creature o) {
		// Defense
		//================================================
		/* Battle Logic:
		 * if opponent is attacking (fireball/scratch):
		 * choice: block, dodge, counter
		 */
		// check independence
		if(tryIndependence()) {
			// Creature's choice
			if(Math.random() * c.data.get(Creature.SPEED).getData(Data.T_DOUBLE) > Math.random() * c.data.get(Creature.DEFENSE).getData(Data.T_DOUBLE)) {
				// random() * 100 < stat determines the success of the action
				if(c.getCurrentAction() != Creature.JUMPING && c.getCurrentAction() != Creature.FALLING && tryJump()) {
					actions.get(Creature.D_JUMP).setActive(true);
					return true;
				}
			}
			else if(tryBlock()) {
				determineDefense();
				return true;
			}
			else if(meleeRange(1) && yDistance < c.getCHeight() * 3 && tryScratch()) {
				determineMelee();
			}
			else if(tryFireball() && (Math.random() * 100 < c.data.get(Creature.AGGRESSION).getData(Data.T_INT) - 30 || 
					c.getEnergy() > c.getMaxEnergy() / 2)) {
				determineRange(o);
			}
		}
		// Use strategies if available
		else if(strategy.getBlock()) {
			if(tryBlock()) {
				determineDefense();
				return true;
			}
		}
		else if(strategy.getDodge() || strategy.getCounter() || strategy.getRest()) {
			if(c.getCurrentAction() != Creature.JUMPING && c.getCurrentAction() != Creature.FALLING && tryJump()) {
				actions.get(Creature.D_JUMP).setActive(true);
				return true;
			}
			else if(strategy.getCounter()) {
				if(meleeRange(1) && yDistance < c.getCHeight() * 3 && tryScratch()) {
					determineMelee();
				}
				else if(tryFireball()) {
					determineRange(o);
				}
			}
		}
		return false;
	}
	
	private void moveLeft() {
		actions.get(Creature.D_RIGHT).setActive(false);
		actions.get(Creature.D_LEFT).setActive(true);
		actions.get(Creature.D_FACINGRIGHT).setActive(false);
	}
	private void moveRight() {
		actions.get(Creature.D_RIGHT).setActive(true);
		actions.get(Creature.D_LEFT).setActive(false);
		actions.get(Creature.D_FACINGRIGHT).setActive(true);
	}

	private void setMovement() {
		if(onRight) {
			if(tryRetreat()) {
				moveLeft();
			}
			else {
				moveRight();
			}
		}
		else {
			if(tryRetreat()) {
				moveRight();
			}
			else {
				moveRight();
			}
		}
	}

	public ArrayList<Action> Decide(Creature creature, Creature o) {
		/*
		 * Available Actions:
		 * ------------------
		 *  left,
			right,
			facingRight,
			jumping,
			blocking,
			scratching,
			firing,
			firingDown
		 */
		boolean defenseSet = false;
		c = creature;
		actions = c.getActionSet();
		strategy = c.getStrategy();

		//layout of battle field
		charge = false;
		xDistance = Math.abs(c.getx() - o.getx());
		yDistance = Math.abs(c.gety() - o.gety());
		if(c.getx() < o.getx()) onRight = true;
		else onRight = false;
		if(c.gety() <= o.gety()) above = false;
		else above = true;

		// Checking if a move is possible
		//==============================================
		// check move timer
		if(c.getMoveCounter() < (200 - c.data.get(Creature.SPEED).getData(Data.T_DOUBLE)) / 6) return actions;
		// check current action
		if(c.getCurrentAction() == Creature.MELEE || 
				c.getCurrentAction() == Creature.RANGE || c.getCurrentAction() == Creature.BLOCKING) return actions;
		if(c.isFlinching() || c.isDead()) return actions;
		if(o.isDead()) {
			actions.get(Creature.D_RIGHT).setActive(false);
			actions.get(Creature.D_LEFT).setActive(false);
			return actions;
		}

		// resetting move counter
		c.resetMoveCounter();
		// continue with last direction?
		if(Math.random() * 100 > c.data.get(Creature.STAMINA).getData(Data.T_DOUBLE) && Math.random() * 100 >  c.data.get(Creature.SPEED).getData(Data.T_DOUBLE)) {
			actions.get(Creature.D_RIGHT).setActive(false);
			actions.get(Creature.D_LEFT).setActive(false);		} 
		else {
			setMovement();
			// if opponent is above
			if(above) {
				if(c.getCurrentAction() != Creature.JUMPING && c.getCurrentAction() != Creature.FALLING && tryJump()) {
					actions.get(Creature.D_JUMP).setActive(true);				}
			}
		}

		// Defense
		//================================================
		// defend against immediate attacks
		if(o.getCurrentAction() == Creature.MELEE || o.getCurrentAction() == Creature.RANGE) {
			defenseSet = setDefense(o);
		}

		// defend against approaching fireballs
		if(!defenseSet) {
			for(int i = 0; i < o.fireBalls.size(); i++) {
				// fireball incoming from the left!
				if((o.fireBalls.get(i).getx() < c.getx() && o.fireBalls.get(i).getFacingRight()) ||
						// or fireball incoming from the right! 
						(o.fireBalls.get(i).getx() > c.getx() && !o.fireBalls.get(i).getFacingRight())) {
					defenseSet = setDefense(o);
				}
			}
		}

		if(defenseSet) return actions;
		//---------------------------------------------

		// Positioning and Attack
		//==============================================
		// continue retreating until for 30 counts
		if(c.getRetreatCounter() > 10) {
			c.setRetreat(false);

			if(!strategy.getBlock() && tryIndependence()) {
				if(c.getCurrentAction() != Creature.JUMPING && c.getCurrentAction() != Creature.FALLING && tryJump()) {
					actions.get(Creature.D_JUMP).setActive(true);				}
			}

			// opponent FAR
			// - - - - - - - - - - - 
			if(!meleeRange(1)) {
				// check independence
				if(tryIndependence()) {
					// Creature's choice:
					if(tryCharge()) charge = true;
					else c.setRetreat(tryRetreat());
					if(Math.random() * c.data.get(Creature.FOCUS).getData(Data.T_DOUBLE) > Math.random() * c.data.get(Creature.AGGRESSION).getData(Data.T_INT) 
							&& tryFireball() && (Math.random() * 100 < c.data.get(Creature.AGGRESSION).getData(Data.T_INT) - 30 
							|| c.getEnergy() > c.getMaxEnergy() / 2)) {
						determineRange(o);
					}
				}
				// use strategies if available
				else if(strategy.getClose()) {
					charge = tryCharge();
				}
				else if(strategy.getDistance() || strategy.getRest()) {
					c.setRetreat(meleeRange(3) && tryRetreat());
					if(!c.getRetreat() && strategy.getDistance()) {
						if(tryFireball()) {
							determineRange(o);
						}
					}
				}
				// if opponent is to the RIGHT
				// - - - - - - - - - - - - - - 
				if(onRight) {
					//actions.get(Creature.D_FACINGRIGHT).setActive(true);
					if(charge) {
						moveRight();
					}
					else if(c.getRetreat()) {
						c.resetRetreatCounter();
						moveLeft();
					}
					return actions;
				}
				// if opponent is to the LEFT
				// - - - - - - - - - - - - - - 
				else {
					//actions.get(Creature.D_FACINGRIGHT).setActive(false);
					if(charge) {
						moveLeft();
					}
					else if(c.getRetreat()) {
						c.resetRetreatCounter();
						moveRight();
					}
					return actions;
				}
			}
			// opponent CLOSE
			// - - - - - - - - - - - - - -
			else {
				// check independence
				if(tryIndependence()) {
					// Creature's choice:
					/* if opponent is close:
					 * scratch, fireball, block
					 */
					if(Math.random() * c.data.get(Creature.AGGRESSION).getData(Data.T_INT) > Math.random() * c.data.get(Creature.FOCUS).getData(Data.T_DOUBLE)) {
						if(tryFireball() && (Math.random() * 100 < c.data.get(Creature.AGGRESSION).getData(Data.T_INT) - 30 
								|| c.getEnergy() > c.getMaxEnergy() / 2)) {
							determineRange(o);
						}
					}
					else if(Math.random() * c.data.get(Creature.FOCUS).getData(Data.T_DOUBLE) > Math.random() * c.data.get(Creature.DEFENSE).getData(Data.T_DOUBLE)) { 
						if(tryScratch() && yDistance < c.getCHeight() * 3) {
							determineMelee();
						}
					}
					else if(Math.random() * c.data.get(Creature.DEFENSE).getData(Data.T_DOUBLE) > Math.random() * c.data.get(Creature.SPEED).getData(Data.T_DOUBLE)) {
						if(tryBlock()) {
							determineDefense();
						}
					}
					else if(Math.random() * c.data.get(Creature.SPEED).getData(Data.T_DOUBLE) > Math.random() * c.data.get(Creature.COURAGE).getData(Data.T_INT)) {
						c.setRetreat(tryRetreat());
					}
					else charge = tryCharge();

				}
				// use strategies if available
				else if(strategy.getClose()) {
					if(tryScratch() && yDistance < c.getCHeight() * 3) {
						determineMelee();
					}
					else charge = tryCharge();
				}
				else if(strategy.getDistance() || strategy.getRest()) {
					c.setRetreat(tryRetreat());
					if(!c.getRetreat() && strategy.getDistance()) {
						if(tryFireball()) {
							determineRange(o);
						}
					}
				}

				// if opponent is to the RIGHT
				// - - - - - - - - - - - - - - 
				if(onRight) {
					//actions.get(Creature.D_FACINGRIGHT).setActive(true);
					if(c.getRetreat()) {
						c.resetRetreatCounter();
						moveLeft();
					}
					return actions;
				}
				// if opponent is to the LEFT
				// - - - - - - - - - - - - - - 
				else {
					//actions.get(Creature.D_FACINGRIGHT).setActive(false);
					if(c.getRetreat()) {
						c.resetRetreatCounter();
						moveRight();
					}
					return actions;
				}
			}
		}		

		return actions;
	}
}
