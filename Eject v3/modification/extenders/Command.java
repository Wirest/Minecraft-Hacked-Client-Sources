package modification.extenders;

import modification.interfaces.MCHook;
import modification.main.Modification;
import modification.managers.CommandManager;

public abstract class Command
        implements MCHook {
    public final String name;
    public final String arguments;

    protected Command(String paramString1, String paramString2) {
        this.name = paramString1;
        this.arguments = paramString2;
        CommandManager.COMMANDS.add(this);
    }

    protected final void sendUsage() {
        Modification.LOG_UTIL.sendChatMessage("§4Usage: .".concat(this.name).concat(" ").concat(this.arguments));
    }

    public abstract void execute(String paramString, String[] paramArrayOfString);
}




