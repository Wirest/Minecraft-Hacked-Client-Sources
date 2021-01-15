package nivia.commands.commands;

import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.files.modulefiles.Colors;
import nivia.modules.Module;
import nivia.modules.player.Fastplace;
import nivia.modules.render.GUI;
import nivia.utils.Logger;

import java.io.IOException;
import java.util.Objects;

public class Color extends Command{
    public Color(){
        super("Color", "Changes the specified module's color. Can use RGB or Hex", "Invalid Usage. Color [Mod] [R G B] or [HEX]", false , "c", "colour");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        if(arguments[1].equalsIgnoreCase("clear")){
            Logger.logChat("Reset color.");
            return;
        }
       GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
        try {
        	int RHex = Integer.valueOf(arguments[1]);
        	int G = Integer.valueOf(arguments[2]);
        	int B = Integer.valueOf(arguments[3]);
        	if(G > 255) G = 255;
        	if(B > 255) B = 255;
        	if(RHex > 255) RHex = 255;
        	java.awt.Color c = new java.awt.Color(RHex, G, B);
        		gui.tabGuiColor.setValue(c.getRGB());
        	
        } catch (Exception e){
            try {
                int RHex = Integer.parseInt(arguments[1], 16);
                java.awt.Color c = new java.awt.Color(RHex);
                gui.tabGuiColor.setValue(c.getRGB());
            } catch (Exception ex){
                Logger.logChat("Could not apply the specified color!");
                return;
            }
        }

        
        try {
            Pandora.getFileManager().getFile(Colors.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
