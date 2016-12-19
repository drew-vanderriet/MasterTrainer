package AI;

public class Strategy {
	
	private boolean distance;
	private boolean close;
	private boolean rest;
	private boolean block;
	private boolean dodge;
	private boolean counter;
	
	public boolean setDistance() { 
		if(distance) {
			distance = false;
			return distance;
		}
		else distance = true;
		close = false;
		rest = false;
		return distance;
	}
	public boolean setClose() { 
		if(close) {
			close = false;
			return close;
		}
		else close = true; 
		distance = false;
		rest = false;
		return close;
	}
	public boolean setRest() { 
		if(rest) {
			rest = false;
			return rest;
		}
		else rest = true;
		close = false;
		distance = false;
		return rest;
	}
	public boolean setBlock() { 
		if(block) {
			block = false;
			return block;
		}
		else block = true; 
		dodge = false;
		counter = false;
		return block;
	}
	public boolean setDodge() { 
		if(dodge) {
			dodge = false;
			return dodge;
		}
		else dodge = true; 
		block = false;
		counter = false;
		return dodge;
	}
	public boolean setCounter() { 
		if(counter) {
			counter = false;
			return counter;
		}
		else counter = true; 
		block = false;
		dodge = false;
		return counter;
	}
	
	public boolean getDistance() { return distance; }
	public boolean getClose() { return close; }
	public boolean getRest() { return rest; }
	public boolean getBlock() { return block; }
	public boolean getDodge() { return dodge; }
	public boolean getCounter() { return counter; }


}
