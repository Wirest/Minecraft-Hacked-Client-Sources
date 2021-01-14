package store.shadowclient.client.utils.particles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.Gui;

public class ParticleUtil {

	private final List<Particle> particles;
	private int width, height, count;
	
	public ParticleUtil(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.count = 150;
		this.particles = new ArrayList<Particle>();
		for (int count = 0; count <= this.count; ++count) {
			this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		}
	}
	
	public void drawParticles() {
		this.particles.forEach(particle -> particle.drawParticle());
	}
	
	public class Particle {
		
		private int xPosition, yPosition;
		
		public Particle(final int xPosition, final int yPosition) {
			this.xPosition = xPosition;
			this.yPosition = yPosition;
		}
		
		public void drawParticle() {
			++this.xPosition;
			++this.yPosition;
			final int particleSize = 3;
			
			if(this.xPosition > ParticleUtil.this.width) {
				this.xPosition = -particleSize;
			}
			
			if(this.yPosition > ParticleUtil.this.height) {
				this.yPosition = -particleSize;
			}
			
			Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + particleSize, this.yPosition + particleSize, novoline(300));
		}
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 50.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}
	
	public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}


