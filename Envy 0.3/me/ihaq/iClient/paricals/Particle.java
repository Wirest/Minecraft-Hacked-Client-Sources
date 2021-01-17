package me.ihaq.iClient.paricals;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.ihaq.iClient.utils.RenderUtils;
import me.ihaq.iClient.utils.timer.Timer;
import me.ihaq.iClient.utils.timer.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Particle {

	public static final int EaZy = 198;

	

	private int x;
	private int y;
	private int k;
	private final int id;
	private float size;
	private final Color color;
	private boolean reset;
	private final Random random;
	private final Timer timer;
	private final int dir;

	private float realX;
	private float realY;

	public Particle(final int x, final int y, final int id) {
		random = new Random();
		timer = new Timer();
		this.x = x;
		this.y = y;
		realX = x;
		realY = y;
		this.id = id;
		color = new Color(0.9F, 0.9F, 0.9f, 1F);
		dir = (int) genRandom(0, 360);
		size = 1F;
	}

	public void render(final ArrayList<Particle> lastparticles, final int mouseX, final int mouseY) {

		final ScaledResolution scr = new ScaledResolution(Minecraft.getMinecraft());
		float xx = 10;
		float yy = 10;
		if (realX < -1 || realY < -1) {
			reset = true;
		} else if (realX > scr.getScaledWidth_double() + 1 || realY > scr.getScaledHeight_double() + 1) {
			reset = true;
		}

		xx = (float) (Math.cos((dir + 90.0f) * 3.141592653589793 / 180.0) * k * 0.6);
		yy = (float) (Math.sin((dir + 90.0f) * 3.141592653589793 / 180.0) * k * 0.6);

		timer.updateMS();
		if (timer.hasTimePassedM(20)) {

			++k;
			timer.updateLastMS();
		}
		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			final double d = Mouse.isButtonDown(0) ? 1 : Mouse.isButtonDown(1) ? -1 : 0;
			if (realX > mouseX) {
				x -= d;
			} else if (realX < mouseX) {
				x += d;
			}

			if (realY > mouseY) {
				y -= d;
			} else if (realY < mouseY) {
				y += d;
			}
		}
		final float drawX = this.x + xx;
		final float drawY = this.y + yy;
		realX = drawX;
		realY = drawY;

		if (lastparticles != null) {
			for (final Particle p : lastparticles) {
				if (p.equals(this)) {
					continue;
				}
				if (getDistanceTo(p) < 60) {
					if (p != null) {
						GL11.glPushMatrix();
						GL11.glEnable(GL_BLEND);
						GL11.glEnable(GL_LINE_SMOOTH);
						GL11.glDisable(GL_TEXTURE_2D);
						GL11.glColor4f(0.9F, 0.9F, 0.9F, 0.4F);
						GL11.glBegin(GL11.GL_LINE_LOOP);
						GL11.glVertex2d(drawX, drawY);
						GL11.glVertex2d(p.getX(), p.getY());
						GL11.glEnd();
						GL11.glEnable(GL_TEXTURE_2D);
						GL11.glDisable(GL_LINE_SMOOTH);
						GL11.glDisable(GL_BLEND);
						GL11.glPopMatrix();
					}
				}
			}
		}
		RenderUtils.drawCircle((int) realX, (int) realY, size, color.getRGB());
	}

	public boolean isReset() {
		return reset;
	}

	public float getX() {
		return realX;
	}

	public float getY() {
		return realY;
	}

	public int getID() {
		return id;
	}

	private double getDistanceTo(final Particle p) {
		return Math.abs(getX() - p.getX()) + Math.abs(getY() - p.getY());
	}

	private float genRandom(final float min, final float max) {
		return (float) (min + Math.random() * (max - min + 1.0f));
	}
}
