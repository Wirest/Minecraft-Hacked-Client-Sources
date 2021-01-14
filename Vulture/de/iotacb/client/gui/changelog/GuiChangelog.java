package de.iotacb.client.gui.changelog;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.Client;
import de.iotacb.client.gui.particle.ParticleManager;
import de.iotacb.client.utilities.render.BlurUtil;
import de.iotacb.client.utilities.render.Render2D;
import net.minecraft.client.gui.GuiScreen;

public class GuiChangelog extends GuiScreen {
	
	private final List<String> added = new ArrayList<String>();
	private final List<String> removed = new ArrayList<String>();
	private final List<String> changed = new ArrayList<String>();
	
	private ParticleManager pManager;
	
	@Override
	public void initGui() {
		this.pManager = new ParticleManager();
		clear();
		final String changelog = downloadText("https://raw.githubusercontent.com/iotacb/vulture-auto-configs/master/changelog");
		parseText(changelog);
		super.initGui();
	}
	

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(mouseX, mouseY);
		
		pManager.draw(mouseX, mouseY);
		
		Client.BLUR_UTIL.blur(width / 2 - 200, 0, 400, height, 5);
		
		Client.RENDER2D.rect(width / 2 - 200, 0, 400, height, new Color(20, 20, 20, 200));
		
		int yPosOffset = 100;
		
		if (added.size() > 0) {
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow("Added:", width / 2 - 180, yPosOffset, new Color(50, 250, 50));
			
			yPosOffset += Client.INSTANCE.getFontManager().getBigFont().getHeight("Added:");
			
			for (String line : added) {
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(line, width / 2 - 180, yPosOffset, new Color(50, 250, 50));
				yPosOffset += Client.INSTANCE.getFontManager().getDefaultFont().getHeight(line) + 2;
			}
			
			yPosOffset += 20;
		}
		
		if (changed.size() > 0) {
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow("Changed:", width / 2 - 180, yPosOffset, new Color(250, 150, 50));
			
			yPosOffset += Client.INSTANCE.getFontManager().getBigFont().getHeight("Changed:") + 5;
			
			for (String line : changed) {
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(line, width / 2 - 180, yPosOffset, new Color(250, 150, 50));
				yPosOffset += Client.INSTANCE.getFontManager().getDefaultFont().getHeight(line) + 2;
			}
			
			yPosOffset += 20;
		}
		
		if (removed.size() > 0) {
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow("Removed:", width / 2 - 180, yPosOffset, new Color(250, 50, 50));
			
			yPosOffset += Client.INSTANCE.getFontManager().getBigFont().getHeight("Removed:");
			
			for (String line : removed) {
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(line, width / 2 - 180, yPosOffset, new Color(250, 50, 50));
				yPosOffset += Client.INSTANCE.getFontManager().getDefaultFont().getHeight(line) + 2;
			}
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void clear() {
		added.clear();
		changed.clear();
		removed.clear();
	}
	
	private void parseText(String text) {
		if (text.isEmpty()) return;
		final String[] lines = text.split("\n");
		for (String line : lines) {
			StringType type = getTypeOfString(line);
			if (type == StringType.ADD) {
				added.add(line);
			} else if (type == StringType.REMOVE) {
				removed.add(line);
			} else if (type == StringType.CHANGE) {
				changed.add(line);
			}
		}
	}
	
	private String downloadText(String url) {
		if (url.isEmpty()) return "";
		String content = "";
		InputStream in;
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	private StringType getTypeOfString(String string) {
		StringType type = null;
		if (string.startsWith("+")) {
			type = StringType.ADD;
		} else if (string.startsWith("-")) {
			type = StringType.REMOVE;
		} else if (string.startsWith("~")) {
			type = StringType.CHANGE;
		}
		return type;
	}
	
	public enum StringType {
		ADD, REMOVE, CHANGE
	}
	
}
