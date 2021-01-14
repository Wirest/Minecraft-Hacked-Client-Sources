package cedo.command.commands;

import cedo.Fan;
import cedo.command.Command;
import cedo.modules.Module;
import cedo.util.Logger;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "bind");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            Logger.ingameError(".bind <module> <key>"); //Change to using client messages
            return;
        }
        Module module = Fan.getModuleByName(args[0]);
        if (module == null) {
            Logger.ingameError("Couldn't find the module \"" + args[0] + "\"");
            return;
        }
        String keyString = args[1];
        int key = Keyboard.getKeyIndex(keyString.toUpperCase());

        if (keyString.equalsIgnoreCase("none")) {
            module.getKeyBind().setCode(key);
            Logger.ingameInfo("Reset keybind for" + module.getName());
            return;
        }
        if (key == 0) {
            module.getKeyBind().setCode(0);
            Logger.ingameInfo("Reset keybind for" + module.getName());
            return;
        }
        module.getKeyBind().setCode(key);
        Logger.ingameInfo(module.getName() + " has been bound to " + keyString.toUpperCase());
    }
}
