package nivia.utils;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CFont {
	public static final int IMAGE_WIDTH = 512;
	public static final int IMAGE_HEIGHT = 512;
	public static final int DEFAULT_CHAR_WIDTH = 8;
	public static final int DEFAULT_CHAR_HEIGHT = 8;
	protected CharData[] charData = new CharData[256];
	protected Font font;
	protected boolean antiAlias;
	protected boolean fractionalMetrics;
	protected int fontHeight = -1;
	protected int charOffset = 0;
	protected DynamicTexture tex;

	public CFont(Font font, boolean antiAlias, boolean fractionalMetrics){
		this.font = font;
		this.antiAlias = antiAlias;
		this.fractionalMetrics = fractionalMetrics;
		this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
	}

	protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars){
		BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
		try{
			return new DynamicTexture(img);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return null;
	}

	protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars){
		BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, 2);
		Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
		g.setFont(font);

		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, 512, 512);

		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		FontMetrics fontMetrics = g.getFontMetrics();

		int charHeight = 0;
		int positionX = 0;
		int positionY = 0;

		for(int i = 0; i < chars.length; i++){
			char ch = (char) i;
			CharData charData = new CharData();

			Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);

			charData.width = (dimensions.getBounds().width + 8);

			charData.height = dimensions.getBounds().height;

			if(positionX + charData.width >= 512){
				positionX = 0;
				positionY += charHeight;
				charHeight = 0;
			}

			if(charData.height > charHeight){
				charHeight = charData.height;
			}

			charData.storedX = positionX;
			charData.storedY = positionY;

			if(charData.height > this.fontHeight){
				this.fontHeight = charData.height;
			}

			chars[i] = charData;

			g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());

			positionX += charData.width;
		}
		return bufferedImage;
	}

	public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException{
		y += 1;

		try{
			drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight){
		float renderSRCX = srcX / 512.0F;
		float renderSRCY = srcY / 512.0F;
		float renderSRCWidth = srcWidth / 512.0F;
		float renderSRCHeight = srcHeight / 512.0F;
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d(x + width, y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d(x + width, y);
	}

	public void glColor(Color color){
		float red = color.getRed() / 255.0F;
		float green = color.getGreen() / 255.0F;
		float blue = color.getBlue() / 255.0F;
		float alpha = color.getAlpha() / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public int getStringHeight(String text){
		return getHeight();
	}

	public int getHeight(){
		return (this.fontHeight - 8) / 2;
	}

	public int getStringWidth(String text){
		int width = 0;
		for(char c : text.toCharArray()){
			if((c < this.charData.length) && (c >= 0))
				width += this.charData[c].width - 8 + this.charOffset;
		}
		return width / 2;
	}

	public boolean isAntiAlias(){
		return this.antiAlias;
	}

	public void setAntiAlias(boolean antiAlias){
		if(this.antiAlias != antiAlias){
			this.antiAlias = antiAlias;
			this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
		}
	}

	public boolean isFractionalMetrics(){
		return this.fractionalMetrics;
	}

	public void setFractionalMetrics(boolean fractionalMetrics){
		if(this.fractionalMetrics != fractionalMetrics){
			this.fractionalMetrics = fractionalMetrics;
			this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
		}
	}

	public Font getFont(){
		return this.font;
	}

	public void setFont(Font font){
		this.font = font;
		this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
	}

	protected class CharData {
		public int width;
		public int height;
		public int storedX;
		public int storedY;

		protected CharData(){
		}
	}
}