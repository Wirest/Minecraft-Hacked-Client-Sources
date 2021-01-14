package de.iotacb.client.module.modules.render.compass;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.UFormat;

import de.iotacb.client.Client;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class Compass {

	private final List<Direction> directions;
	
	public Compass() {
		this.directions = new ArrayList<Direction>();
		
		directions.add(new Direction("N", 1));
		directions.add(new Direction("195", 2));
		directions.add(new Direction("210", 2));
		directions.add(new Direction("NE", 3));
		directions.add(new Direction("240", 2));
		directions.add(new Direction("255", 2));
		directions.add(new Direction("E", 1));
		directions.add(new Direction("285", 2));
		directions.add(new Direction("300", 2));
		directions.add(new Direction("SE", 3));
		directions.add(new Direction("330", 2));
		directions.add(new Direction("345", 2));
		directions.add(new Direction("S", 1));
		directions.add(new Direction("15", 2));
		directions.add(new Direction("30", 2));
		directions.add(new Direction("SW", 3));
		directions.add(new Direction("60", 2));
		directions.add(new Direction("75", 2));
		directions.add(new Direction("W", 1));
		directions.add(new Direction("105", 2));
		directions.add(new Direction("120", 2));
		directions.add(new Direction("NW", 3));
		directions.add(new Direction("150", 2));
		directions.add(new Direction("165", 2));
	}
	
	public void draw(ScaledResolution scaledResolution, double playerYaw) {
		final double screenCenter = scaledResolution.getScaledWidth() / 2;
		final double yaw = (playerYaw % 360) * 2 + 360 * 3;
		
		double count = 0;
		for (final Direction direction : directions) {
			final double position = screenCenter + (count * 30) - yaw;
			double resetPosition = 0;
			
			if (direction.getType() == 1) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 2) {
				resetPosition = position - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 3) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			}
			
			
			count++;
		}
		for (final Direction direction : directions) {
			final double position = screenCenter + (count * 30) - yaw;
			double resetPosition = 0;
			
			if (direction.getType() == 1) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 2) {
				resetPosition = position - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 3) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			}
			
			
			count++;
		}
		for (final Direction direction : directions) {
			final double position = screenCenter + (count * 30) - yaw;
			double resetPosition = 0;
			
			if (direction.getType() == 1) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 2) {
				resetPosition = position - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(direction.getDirection()) / 2.5;
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			} else if (direction.getType() == 3) {
				resetPosition = position - Client.INSTANCE.getFontManager().getBigFont().getWidth(direction.getDirection()) / 2;
				Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(direction.getDirection(), resetPosition, 35, color(scaledResolution, resetPosition));
			}
			count++;
		}
	}
	
	public Color color(ScaledResolution sr, double offset){
		final double offs = 255 - Math.abs(sr.getScaledWidth() / 2 - offset) * 1.8;
		final Color c = new BetterColor(Color.white).setAlpha((int) offs);
		return c;
	}
	
}
