package de.iotacb.cu.core.peripherals;

import javax.vecmath.Vector2d;

public class MouseUtil {

	/**
	 * Returns true if the given mouse position is contained in the given bounds
	 * @param mouseX
	 * @param mouseY
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 * @return
	 */
	public static final boolean inBounds(final int mouseX, final int mouseY, final double posX, final double posY, final double width, final double height) {
		return (mouseX > posX && mouseX < posX + width) && (mouseY > posY && mouseY < posY + height);
	}
	
	/**
	 * Returns true if the given mouse position is contained in a circular range
	 * @param mouseX
	 * @param mouseY
	 * @param posX
	 * @param posY
	 * @param range
	 * @return
	 */
	public static final boolean inBounds(final int mouseX, final int mouseY, final double posX, final double posY, final double range) {
		final double distX = mouseX - posX;
		final double distY = mouseY - posY;
		return Math.sqrt(distX * distX + distY * distY) < range;
	}
	
}
