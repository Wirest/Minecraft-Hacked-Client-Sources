package org.newdawn.slick;

/**
 * Description of anything that can be drawn
 * 
 * @author kevin
 */
public interface Renderable {

	/**
	 * Draw this artefact at the given location
	 * 
	 * @param x The x coordinate to draw the artefact at
	 * @param y The y coordinate to draw the artefact at 
	 */
	public void draw(float x, float y);

	/**
	 * Draw this artefact at the given location
	 * 
	 * @param x The x coordinate to draw the artefact at
	 * @param y The y coordinate to draw the artefact at 
     * @param filter The color filter to apply when drawing
	 */
	public void draw(float x, float y, Color filter);

	/**
	 * Draw this artefact at the given location with the specified size
	 * 
	 * @param x The x coordinate to draw the artefact at
	 * @param y The y coordinate to draw the artefact at 
     * @param width The width to render the artefact at
     * @param height The width to render the artefact at
	 */
	public void draw(float x, float y, float width, float height);

	/**
	 * Draw this artefact at the given location with the specified size
	 * 
	 * @param x The x coordinate to draw the artefact at
	 * @param y The y coordinate to draw the artefact at 
     * @param width The width to render the artefact at
     * @param height The width to render the artefact at
     * @param filter The color filter to apply when drawing
	 */
	public void draw(float x, float y, float width, float height, Color filter);
	
}
