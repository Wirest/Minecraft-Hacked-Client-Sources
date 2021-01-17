package me.ihaq.iClient.command.commands;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.command.Command;
import me.ihaq.iClient.utils.Colors;

public class Color extends Command {
	public Color() {
		super("Color", "<R,G,B> or <Rainbow>");
	}

	@Override
	public void run(final String message) {
		if (message.split(" ").length == 2) {
			String c = message.split(" ")[1];
			if(c.equalsIgnoreCase("rainbow")){
				Colors.rainbow =  !Colors.rainbow; 
			}else{
				if(Float.parseFloat(c.split(",")[0]) >= 0 && Float.parseFloat(c.split(",")[0]) <= 1 && Float.parseFloat(c.split(",")[1]) >= 0 && Float.parseFloat(c.split(",")[1]) <= 1 && Float.parseFloat(c.split(",")[2]) >= 0 && Float.parseFloat(c.split(",")[2]) <= 1){
					Colors.r = Float.parseFloat(c.split(",")[0]);
					Colors.g = Float.parseFloat(c.split(",")[1]);
					Colors.b = Float.parseFloat(c.split(",")[2]);
					Envy.FILE_MANAGER.saveFiles();
				}else{
					Envy.tellPlayer("Incorrect Syntax! Can't be greater then 1 or less then 0.");
				}
			}
		}else{
			Envy.tellPlayer("Incorrect Syntax! Color <R,G,B> or <Rainbow>");
		}
	}
}