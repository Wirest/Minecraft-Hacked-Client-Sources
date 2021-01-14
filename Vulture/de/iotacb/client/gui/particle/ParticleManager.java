package de.iotacb.client.gui.particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.utilities.math.Vec;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.cu.core.math.MathUtil;
import io.netty.channel.rxtx.RxtxChannelConfig.Paritybit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class ParticleManager {

	private final List<Particle> particles;
	
	private final Vec mouse;
	
	private int alpha;
	
	private boolean fadeIn;
	
	public ParticleManager() {
		this.fadeIn = true;
		this.alpha = 0;
		this.particles = new ArrayList<Particle>();
		final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		for (int i = 0; i < (int)(sr.getScaledWidth() * .2F); i++) {
			this.particles.add(new Particle(sr.getScaledWidth(), sr.getScaledHeight()));
		}
		mouse = new Vec();
	}
	
	public final void draw(int mouseX, int mouseY) {
		if (!Client.INSTANCE.getModuleManager().getModuleByClass(ClickGui.class).getValueByName("ClickGuiParticles").getBooleanValue()) return;
		
		alpha = (int) AnimationUtil.slide(alpha, 0, 255, fadeIn ? .003 : .01, fadeIn);
		
		mouse.set(mouseX, mouseY);
//		this.particles.forEach(particle -> {
//			particle.update();
//			final double distanceMouse = particle.getPos().distance(mouse);
//			this.particles.forEach(particle2 -> {
//				if (distanceMouse > 255 || particle == particle2) return;
//				final double distanceParticle = particle.getPos().distance(particle2.getPos());
//				if (distanceParticle < 100) {
//					Client.RENDER2D.line(particle.getPos(), particle2.getPos(), new BetterColor(255, 255, 255, (int) MathHelper.clamp_double(255 - distanceMouse, 0, 100)));
//				}
//			});
//			particle.draw(mouseX, mouseY);
//		});
		
		this.particles.forEach(particle -> {
			particle.setAlpha(alpha);
			final double mouseDist = particle.getPos().distance(mouse);
			
			if (mouseDist < 100) {
				final double direction = particle.getPos().clone().sub(mouse).direction();
				particle.getPos().add(Math.cos(direction) * .6, Math.sin(direction) * .6);
			}
			particle.update();
			particle.draw(mouseX, mouseY);
			this.particles.forEach(particle2 -> {
				if (particle == particle2) return;
				final double maxDistance = 60;
				final double dist = particle.getPos().distance(particle2.getPos());
				if (dist < maxDistance) {
					Client.RENDER2D.line(particle.getPos(), particle2.getPos(), 1F, new BetterColor(Color.white).setAlpha((int) (alpha - (alpha * MathUtil.INSTANCE.toPercentage(dist, maxDistance)))));
				}
			});
		});
	}
	
	public void setFadeIn(boolean fadeIn) {
		this.fadeIn = fadeIn;
	}
	
}
