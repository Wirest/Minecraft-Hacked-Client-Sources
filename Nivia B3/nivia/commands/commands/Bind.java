package nivia.commands.commands;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.util.Objects;

public class Bind extends Command{
	public Bind(){
		super("Bind", "Binds the client's modules to a certain key", "Invalid Module.", false , "b", "binds");
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		  if(arguments[1].equalsIgnoreCase("clear")){
	            for(Module mod : Pandora.getModManager().mods)
	                mod.setKeybind(0);
	            Logger.logChat("Cleared binds.");
	            return;
	        }
	        if(arguments[1].equalsIgnoreCase("list")){
	            for(Module mod : Pandora.getModManager().mods)
	                if(mod.getKeybind() != 0)
	                Logger.logChat(String.format("%s: %s%s", mod.getName(), EnumChatFormatting.GOLD, Keyboard.getKeyName(mod.getKeybind())));
	            return;
	        }
			Module mod = Pandora.getModManager().getModbyName(arguments[1]);
			if(Objects.isNull(mod)) mod = Pandora.getModManager().getModByAlias(arguments[1]);
			if(Objects.isNull(mod)){
				Logger.logChat("Invalid Module.");
				return;
			}
			String bindKey = ""; 
			try{ bindKey = arguments[2]; } catch (Exception e){ Logger.LogExecutionFail("Key."); return;}		
			mod.setKeybind(Keyboard.getKeyIndex(bindKey.toUpperCase()));

			Logger.logChat("Set \2476" + mod.getName()+ " \2477keybind to \2476" + Keyboard.getKeyName(mod.getKeybind()));
		//new Notification("�bNivia", "�7Set �6" + mod.getName() + " �7keybind �7to �6" + Keyboard.getKeyName(mod.getKeybind()), 5000, Mode.INFO);
	}
}
