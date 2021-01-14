package de.iotacb.client.file.files;

import java.io.File;

import de.iotacb.client.Client;
import de.iotacb.client.file.ClientFile;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.render.image.ExternalImageDrawer;
import net.minecraft.util.ResourceLocation;

public class ClientConfigFile extends ClientFile {

	public ClientConfigFile(String path) {
		super(path);
	}
	
	public void saveConfig() {
		final String cfg = "nameProtect:" + Client.INSTANCE.getNameProtectName() + 
				"\nclientColor:" + Client.INSTANCE.getClientColor().toString() + 
				"\nusername:" + Client.INSTANCE.getIrc().getUsername() + 
				"\npath#" + Client.INSTANCE.getBackgroundPath();
		saveFile(cfg);
	}
	
	public void readConfig() {
		final String content = loadFile();
		if (content.isEmpty()) return;
		final String[] lines = content.split("\n");
		for (String line : lines) {
			if (line.contains(":")) {
				final String[] args = line.split(":");
				if (args[0].equalsIgnoreCase("nameProtect"))  {
					Client.INSTANCE.setNameProtectName(args[1]);
				} else if (args[0].equalsIgnoreCase("clientColor")) {
					String[] colors = args[1].substring(1, args[1].length() - 1).split(",");
					final int red = Integer.valueOf(colors[0].trim());
					final int green = Integer.valueOf(colors[1].trim());
					final int blue = Integer.valueOf(colors[2].trim());
					Client.INSTANCE.setClientColor(new BetterColor(red, green, blue));
				} else if(args[0].equalsIgnoreCase("username")) {
					Client.INSTANCE.startIRC(args[1]);
				}
			}
			if (line.contains("#")) {
				final String[] args = line.split("#");
				if (args[0].equalsIgnoreCase("path")) {
					if (new File(args[1]).exists()) {
						Client.INSTANCE.setBackgroundPath(args[1]);
						Client.INSTANCE.setCustomBackground(new ExternalImageDrawer(new File(args[1])));
					}
				}
			}
		}
	}

}
