package nivia.commands.commands;

import nivia.Pandora;
import nivia.commands.Command;
import nivia.files.modulefiles.Modules;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.io.IOException;
import java.util.Objects;

public class Toggle extends Command{
	public Toggle(){
		super("toggle", "Toggles the client's modules", "Invalid Module." , false,  "t");
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		Module mod = Pandora.getModManager().getModbyName(arguments[1]);
		if(Objects.isNull(mod)) mod = Pandora.getModManager().getModByAlias(arguments[1]);
		if(Objects.isNull(mod)){
			Logger.logChat("Invalid Module.");
			return;
		}
		mod.Toggle();
		Logger.logToggleMessage(mod.getName(), mod.getState());
        try {
            Pandora.getFileManager().getFile(Modules.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
