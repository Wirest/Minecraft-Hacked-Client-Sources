package modification.commands;

import modification.extenders.Command;
import modification.extenders.Module;
import modification.files.ModuleFile;
import modification.main.Modification;
import org.lwjgl.input.Keyboard;

public final class Bind
        extends Command {
    public Bind(String paramString1, String paramString2) {
        super(paramString1, paramString2);
    }

    public void execute(String paramString, String[] paramArrayOfString) {
        if (paramArrayOfString.length == 3) {
            Module localModule = Modification.MODULE_MANAGER.checkModuleForName(paramArrayOfString[1].toLowerCase().replaceAll("clickgui", "gui"));
            if ((localModule == null) || (localModule.name.equals("IRC"))) {
                Modification.LOG_UTIL.sendChatMessage("§4Error: This modules does not exist");
                return;
            }
            int i = Keyboard.getKeyIndex(paramArrayOfString[2].toUpperCase());
            localModule.keyCode = i;
            Modification.FILE_MANAGER.update(ModuleFile.class);
            if (i == 0) {
                Modification.LOG_UTIL.sendChatMessage("Unbound module §8\"§f".concat(localModule.name).concat("§8\" §7successfully"));
                return;
            }
            Modification.LOG_UTIL.sendChatMessage("Bound module §8\"§f".concat(localModule.name).concat("§8\" §7to key §8\"§f").concat(paramArrayOfString[2].toUpperCase()).concat("§8\" §7successfully"));
            return;
        }
        sendUsage();
    }
}




