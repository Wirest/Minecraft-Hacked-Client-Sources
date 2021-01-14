package de.iotacb.client.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.opengl.GL11;

import de.iotacb.client.Client;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.font.TTFFontRenderer;
import de.iotacb.client.utilities.render.font.TextureData;
import net.minecraft.client.renderer.GlStateManager;

public class FontManager {

	private final TTFFontRenderer defaultFontVerdana;
	private final TTFFontRenderer bigFontVerdana;
	
	private TTFFontRenderer defaultFontKenyan;
	private TTFFontRenderer bigFontKenyan;
	
	private TTFFontRenderer defaultFontBigNoodle;
	private TTFFontRenderer bigFontBigNoodle;
	
	private TTFFontRenderer defaultFontRoboto;
	private TTFFontRenderer bigFontRoboto;
	
	private TTFFontRenderer defaultFontComfortaa;
	private TTFFontRenderer bigFontComfortaa;
	
	private TTFFontRenderer defaultFontJetBrains;
	private TTFFontRenderer bigFontJetBrains;
	
	public FontManager() {
		final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
		final ConcurrentLinkedQueue<TextureData> textureDatas = new ConcurrentLinkedQueue<TextureData>();
		this.defaultFontVerdana = new TTFFontRenderer(executor, textureDatas, new Font("Verdana", Font.PLAIN, 18));
		this.bigFontVerdana = new TTFFontRenderer(executor, textureDatas, new Font("Verdana", Font.PLAIN, 48));
		try {
			this.defaultFontKenyan = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/kenyan coffee rg.ttf")).deriveFont(18F));
			this.bigFontKenyan = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/kenyan coffee rg.ttf")).deriveFont(48F));
			
			this.defaultFontBigNoodle = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/big_noodle_titling.ttf")).deriveFont(20F));
			this.bigFontBigNoodle = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/big_noodle_titling.ttf")).deriveFont(48F));
			
			this.defaultFontRoboto = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/Roboto-Regular.ttf")).deriveFont(18F));
			this.bigFontRoboto = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/Roboto-Regular.ttf")).deriveFont(48F));
		
			this.defaultFontComfortaa = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/comfortaa.ttf")).deriveFont(18F));
			this.bigFontComfortaa = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/comfortaa.ttf")).deriveFont(48F));
		
			this.defaultFontJetBrains = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/jetbrains.ttf")).deriveFont(18F));
			this.bigFontJetBrains = new TTFFontRenderer(executor, textureDatas, Font.createFont(Font.TRUETYPE_FONT, new File(Client.INSTANCE.getFileManager().getVultureFolder() + "/fonts/jetbrains.ttf")).deriveFont(48F));
		
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		if (defaultFontKenyan == null) defaultFontKenyan = defaultFontVerdana;
		if (defaultFontBigNoodle == null) defaultFontBigNoodle = defaultFontVerdana;
		if (defaultFontRoboto == null) defaultFontRoboto = defaultFontVerdana;
		if (defaultFontComfortaa == null) defaultFontComfortaa = defaultFontVerdana;
		if (defaultFontJetBrains == null) defaultFontJetBrains = defaultFontVerdana;
		
		if (bigFontKenyan == null) bigFontKenyan = bigFontVerdana;
		if (bigFontBigNoodle == null) bigFontBigNoodle = bigFontVerdana;
		if (bigFontRoboto == null) bigFontRoboto = bigFontVerdana;
		if (bigFontComfortaa == null) bigFontComfortaa = bigFontVerdana;
		if (bigFontJetBrains == null) bigFontJetBrains = bigFontVerdana;
		
		executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureDatas.isEmpty()) {
            	final TextureData textureData = textureDatas.poll();
                GlStateManager.bindTexture(textureData.getTextureId());

                // Sets the texture parameter stuff.
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Uploads the texture to opengl.
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }
	}
	
	public TTFFontRenderer getDefaultFont() {
		return getFont(false);
	}
	
	public TTFFontRenderer getBigFont() {
		return getFont(true);
	}
	
	
	/**
	 * Is cancer crashed aber anders i dunno y
	 */
	private TTFFontRenderer getFont(final boolean big) {
		final String fontName = Client.INSTANCE.getClientFont().toLowerCase();
		if (big) {
			switch (fontName) {
			case "kenyan":
				return bigFontKenyan;
			case "big noodle":
				return bigFontKenyan;
			case "roboto":
				return bigFontKenyan;
			case "comfortaa":
				return bigFontComfortaa;
			case "jetbrains":
				return bigFontJetBrains;
			default:
				return bigFontVerdana;
			}
		} else {
			switch (fontName) {
			case "kenyan":
				return defaultFontKenyan;
			case "big noodle":
				return defaultFontBigNoodle;
			case "roboto":
				return defaultFontRoboto;
			case "comfortaa":
				return defaultFontComfortaa;
			case "jetbrains":
				return defaultFontJetBrains;
			default:
				return defaultFontVerdana;
			}
		}
	}
	
}
