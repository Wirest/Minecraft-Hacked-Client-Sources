package de.iotacb.client.utilities.render.image;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import de.iotacb.client.Client;
import de.iotacb.cu.core.render.RenderUtil;
import net.minecraft.client.gui.Gui;

public class ExternalImageDrawer {

	private BufferedImage bufferedImage;
	
	private int imageWidth, imageHeight, textureId;
	private ByteBuffer buffer;
	
	public ExternalImageDrawer(File file) {
		try {
			bufferedImage = ImageIO.read(file);
			if (bufferedImage == null) return;
			textureId = GL11.glGenTextures();
      		imageWidth = bufferedImage.getWidth();
			imageHeight = bufferedImage.getHeight();
			int[] pixels = new int[imageWidth * imageHeight];
			bufferedImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
			buffer = BufferUtils.createByteBuffer(imageWidth * imageHeight * 3);
			for (int pixel : pixels) {
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
			}
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(double posX, double posY, double width, double height) {
		if (buffer == null) return;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, imageWidth, imageHeight, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBegin(GL11.GL_QUADS);
        {
        	GL11.glTexCoord2d(0, 0);
        	GL11.glVertex2d(posX, posY);
        	GL11.glTexCoord2d(1, 0);
        	GL11.glVertex2d(posX + width, posY);
        	GL11.glTexCoord2d(1, 1);
        	GL11.glVertex2d(posX + width, posY + height);
        	GL11.glTexCoord2d(0, 1);
        	GL11.glVertex2d(posX, posY + height);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
}
