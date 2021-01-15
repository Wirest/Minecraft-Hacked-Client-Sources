package nivia.commands.commands;

import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.util.Objects;

public class Visible extends Command {
	 public Visible() {
		 	super("Visible", "Toggles the module being visible on arraylist.", "Invalid Module.", false);
	    } 
	 
	 @Override
		public void execute(String commandName, String[] arguments) {
			Module mod = Pandora.getModManager().getModbyName(arguments[1]);
			if(Objects.isNull(mod)) mod = Pandora.getModManager().getModByAlias(arguments[1]);
			if(Objects.isNull(mod)){
				Logger.logChat("Invalid Module.");
				return;
			}
			mod.setVisible(!mod.isVisible());
			Logger.logChat(EnumChatFormatting.GOLD + mod.getName() + EnumChatFormatting.GRAY + " is " + (mod.isVisible() ? "\247anow" : "\247cno longer") + " \2477visible." );
		}
	 
}
