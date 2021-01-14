package com.mentalfrostbyte.jello.music.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureImage {

	public byte[] pixels;
	public Texture texture;
	public String location;
	
	public Texture getTexture() {
    	if (texture == null) {
    		if (pixels != null) {
    			try {
    				ByteArrayInputStream bias = new ByteArrayInputStream(pixels);
    				texture = TextureLoader.getTexture("JPG", bias);
    			} catch (Exception e) {
    			}
    		}
    	}
		return texture;
	}
	
	public void rectTexture(float x, float y, float w, float h) {
		Draw.rectTexture(x, y, w, h, this.getTexture());
	}
	
	public void rectTextureMasked(float x, float y, float w, float h, float mx, float my) {
		Draw.rectTextureMasked(x, y, w, h, this.getTexture(), mx, my);
	}
	public void rectTextureMasked2(float x, float y, float w, float h, float mx, float my) {
		Draw.rectTextureMasked2(x, y, w, h, this.getTexture(), mx, my);
	}
	public void rectTextureMaskedNotification(float x, float y, float w, float h, float mx, float my) {
		Draw.rectTextureMaskedNotification(x, y, w, h, this.getTexture(), mx, my);
	}
	public void rectTextureMaskedBanner(float x, float y, float w, float h, float mx, float my) {
		Draw.rectTextureMaskedBanner(x, y, w, h, this.getTexture(), mx, my);
	}
}
