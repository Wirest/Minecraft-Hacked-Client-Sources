package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands;

import java.util.List;

import org.lwjgl.input.Keyboard;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.util.file.KeyBind;
import skyline.specc.SkyLine;

public class BindCommand extends Command {

	public BindCommand(){
		super("Bind", new String[]{"b"}, "Bind modules .");
	}

	@Override
	public void onCommand(List<String> args) {
		if(args.size() >= 2){
			if(args.get(0).equalsIgnoreCase("add")){
				if(args.size() == 3){
					String moduleName = args.get(1);
					String key = args.get(2);

					if(SkyLine.getManagers().getModuleManager().getModuleFromName(moduleName) == null){
						error("Module '" + moduleName + "' not found.");
						return;
					}

					if(Keyboard.getKeyIndex(key.toUpperCase()) == 0){
						error("Invalid key!");
						return;
					}

					Module module = SkyLine.getManagers().getModuleManager().getModuleFromName(moduleName);
					
					module.getData().setKeybind(new KeyBind(module.getName(), Keyboard.getKeyIndex(key.toUpperCase())));

					addChat("Set key for '" + module.getName() +"' to '" + key + "'");		
					
					SkyLine.getManagers().getModDataManager().save();
				}else{
					error("Usage : 'Bind add <mod> <key>' or 'Bind remove <mod>'");
				}
			}else if(args.get(0).equalsIgnoreCase("remove")){
				if(args.size() == 2){
					String moduleName = args.get(1);

					if(SkyLine.getManagers().getModuleManager().getModuleFromName(moduleName) == null){
						error("Module '" + moduleName + "' not found.");
						return;
					}

					Module module = SkyLine.getManagers().getModuleManager().getModuleFromName(moduleName);

					module.getData().setKeybind(new KeyBind(module.getName(), 0));

					addChat("Removed bind for '" + module.getName() + "'");
					
					SkyLine.getManagers().getModDataManager().save();
				}else{
					error("Usage : 'Bind add <mod> <key>' or 'Bind remove <mod>'");
				}
			}
		}else{
			error("Usage : 'Bind add <mod> <key>' or 'Bind remove <mod>'");
		}
	}

}