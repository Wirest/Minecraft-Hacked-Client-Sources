package me.robbanrobbin.jigsaw.gui.animations;

public abstract class Animation {
	
	protected int time;
	protected boolean enabling;
	
	public void update() {
		if(enabling) {
			time++;
		}
		else {
			time--;
		}
		if(time < 0) {
			time = 0;
		}
		if(time > getMaxTime()) {
			time = getMaxTime();
		}
	}
	
	public void reset() {
		time = 0;
	}
	
	public int getMaxTime() {
		return 10;
	}
	
	public int getTime() {
		return time;
	}
	
	public void on() {
		enabling = true;
	}
	
	public void toggle() {
		enabling = !enabling;
	}
	
	public void toggle(boolean b) {
		enabling = b;
	}
	
	public void off() {
		enabling = false;
	}
	
	public boolean isEnabled() {
		return enabling;
	}
	
	public abstract void render();
	
}
