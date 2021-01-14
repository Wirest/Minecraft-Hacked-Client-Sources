package modification.commands;

import modification.extenders.Command;
import modification.extenders.Module;
import modification.main.Modification;

public final class Toggle
        extends Command {
    public Toggle(String paramString1, String paramString2) {
        super(paramString1, paramString2);
    }

    public void execute(String paramString, String[] paramArrayOfString) {
        if (paramArrayOfString.length == 2) {
            Module localModule = Modification.MODULE_MANAGER.checkModuleForName(paramArrayOfString[1]);
            if ((localModule == null) || (localModule.name.equals("IRC"))) {
                Modification.LOG_UTIL.sendChatMessage("§4Error: This modules does not exist");
                return;
            }
            localModule.toggle();
            Modification.LOG_UTIL.sendChatMessage("Module §8\"§f".concat(localModule.name).concat("§8\" §7was §".concat(localModule.enabled ? "aenabled" : "cdisabled").concat(" §7successfully")));
            return;
        }
        sendUsage();
    }
}




