package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private int delay;
	private int delayCount;
	
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames, int i) {
		this.frames = frames;
		currentFrame = 0;
		delayCount = 0;
		playedOnce = false;
		numFrames = i - 1;
	}
	
	public void setDelay(int d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }


	public void update() {
		
		if (delay == -1) return;
		
		delayCount++;
		
		if (delayCount > delay) {
			currentFrame++;
			delayCount = 0;
		}
		if(currentFrame > numFrames) {
			currentFrame = 0;
			playedOnce = true;
		}
		
	}
	
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }
	

}








