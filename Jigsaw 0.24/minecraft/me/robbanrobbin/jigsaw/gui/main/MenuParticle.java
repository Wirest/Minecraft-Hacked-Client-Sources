package me.robbanrobbin.jigsaw.gui.main;

import java.util.ArrayList;
import java.util.Random;

public class MenuParticle {

	public double x;
	public double y;
	public double preX;
	public double preY;
	public double depth;

	public double motionX;
	public double motionY;

	public float alpha = 1f;

	public boolean remove = false;

	public boolean followMouse = false;
	
	public Random rand = new Random();
	
	public boolean alphaDecay = false;

	public MenuParticle(double x, double y, double depth, boolean followMouse) {
		this.x = x;
		this.y = y;
		this.depth = depth;
		this.followMouse = followMouse;
	}

	public void update(int mouseX, int mouseY, ArrayList<MenuParticle> particles) {
		if (followMouse) {
			float angle = (float) Math.toDegrees(Math.atan2(mouseY - y, mouseX - x));
			if (angle < 0) {
				angle += 360;
			}
			
			double mX = Math.cos(Math.toRadians(angle));
			double mY = Math.sin(Math.toRadians(angle));

			motionX += mX * 1;
			motionY += mY * 1;
			
		}

		this.preX = x;
		this.preY = y;
		x += motionX;
		y += motionY;

		motionY *= 0.985;
		motionX *= 0.985;
		if(alphaDecay) {
			this.alpha *= 0.995f;
			this.motionX *= 1.1;
			this.motionY *= 1.1;
		}

	}

	public MenuParticle addMotion(double x, double y) {
		this.motionX += x;
		this.motionY += y;
		return this;
	}

}
