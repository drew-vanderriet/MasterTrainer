package GameState;

import Audio.AudioPlayer;


public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	private PauseState pauseState;
	private boolean paused;
	
	public static final int NUMGAMESTATES = 3;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int NEWGAME = 2;

	public GameStateManager() {
		
		AudioPlayer.init();
		
		gameStates = new GameState[NUMGAMESTATES];
		pauseState = new PauseState(this);
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		switch(state) {
		case MENUSTATE: 
			gameStates[state] = new MenuState(this);
			break;
		case NEWGAME: 
			gameStates[state] = new Level1State(this, true);
			break;
		case LEVEL1STATE: 
			gameStates[state] = new Level1State(this, false);
			break;
		default: 
			System.out.println("Load Game State error:\n"
				+ "Invalid game state received.");
			break;
		}	
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		
		//gameStates[currentState].init();
	}
	
	public void setPaused(boolean b) { paused = b; }

	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		
		if(gameStates[currentState] != null) {
			try {
				gameStates[currentState].update();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(paused) {
			pauseState.draw(g);
			return;
		}
		
		if(gameStates[currentState] != null) {
			try {
				gameStates[currentState].draw(g);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
