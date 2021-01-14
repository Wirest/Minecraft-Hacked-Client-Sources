package de.iotacb.client.gui.particle;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.Client;
import de.iotacb.client.utilities.math.Vec;
import de.iotacb.client.utilities.render.DeltaUtil;

public class Particle {
	
	private final Vec pos;
	private final double width, height;

	private final double speedX, speedY;
	
	private final double size;
	
	private final double minSpeed = .1;
	private final double maxSpeed = .3;
	
	private int alpha;
	
	public Particle(double width, double height) {
		this.pos = Vec.random(0, width, 0, height);
		this.width = width;
		this.height = height;
		
		this.speedX = ThreadLocalRandom.current().nextBoolean() ? -ThreadLocalRandom.current().nextDouble(minSpeed, maxSpeed) : ThreadLocalRandom.current().nextDouble(minSpeed, maxSpeed);
		this.speedY = -ThreadLocalRandom.current().nextDouble(minSpeed, maxSpeed);
		
		this.size = ThreadLocalRandom.current().nextDouble(2F, 3F);
	}
	
	public final void draw(int mouseX, int mouseY) {
		Client.RENDER2D.circleCentered(pos.x, pos.y, size, new Color(255, 255, 255, alpha));
	}
	
	public final void update() {
		pos.add((speedX * (Client.DELTA_UTIL.deltaTime * .1F)), speedY * (Client.DELTA_UTIL.deltaTime * .1F));
		
//		if (pos.getX() < -60) pos.x = width + 60;
//		if (pos.getX() > width + 60) pos.x = -60;
//		if (pos.getY() < -60) pos.y = height + 10;
//		if (pos.getY() > height) pos.y = 0;
		
		if (pos.getX() < -60) pos.x = width + 60;
		if (pos.getX() > width + 60) pos.x = -60;
		if (pos.getY() < -30) pos.y = height + 60;
	}
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	public final Vec getPos() {
		return pos;
	}
}
