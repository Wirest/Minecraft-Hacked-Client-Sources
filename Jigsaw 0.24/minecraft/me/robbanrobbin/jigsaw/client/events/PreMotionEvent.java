package me.robbanrobbin.jigsaw.client.events;

public class PreMotionEvent extends Event {

	public double x;
	public double y;
	public double z;

	public PreMotionEvent(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
