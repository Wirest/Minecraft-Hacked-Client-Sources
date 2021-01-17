package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands;

import java.util.List;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;

public class ToggleCommand extends Command {

    public ToggleCommand(){
        super("Toggle", new String[]{"t"}, "Toggle modules.");
    }

    @Override
    public void onCommand(List<String> args){
        if(args.size() != 1){
            error("Use: Toggle [mod]");
            return;
        }

        Module module = SkyLine.getManagers().getModuleManager().getModuleFromName(args.get(0));

        if(module == null){
            error("Module '" + args.get(0) + "' not found.");
            return;
        }

        module.toggle();
        addChat("Toggled §8" + module.getName());
       
       
        
    }
}
