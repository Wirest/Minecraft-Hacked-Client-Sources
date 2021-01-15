package nivia.commands.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import nivia.commands.Command;
import nivia.utils.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;



public class Coords extends Command {
    public Coords() {
        super("Coords", "Copies your current coordinates to clipboard.", null, false, "coordinates", "cords");
    }
    @Override
    public void execute(String commandName, String[] arguments){           
    	final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	final int x = MathHelper
    			.floor_double(Minecraft.getMinecraft().thePlayer.posX);
    	final int y = MathHelper
    			.floor_double(Minecraft.getMinecraft().thePlayer.posY);
    	final int z = MathHelper
    			.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
    	StringSelection coords = new StringSelection(x + " " + y + " " + z);
    	clipboard.setContents(coords, null);
    	Logger.logChat("Copied your coords to clipboard.");
    
    }
}
