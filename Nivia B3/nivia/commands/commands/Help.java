package nivia.commands.commands;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Module;
import nivia.utils.Logger;
public class Help extends Command {
    public Help() {
        super("Help", "Displays help for the provided Module or Command", "Invalid Usage: -help (mod/cmd) (name)", false, "info");
    }
    @Override
    public void execute(String commandName, String[] arguments){
        String object = arguments[1];
        String input = arguments[2];
        switch (object.toLowerCase()) {
            case "m":case "mod":case "module":case "md":case "mods": {
                Module m = getModule(input);
                if(m == null) {
                    Logger.logChat("Invalid Module!");
                    break;
                }
                logValue("Module", m.getName());
                logValue("Keybind", Keyboard.getKeyName(m.getKeybind()));
                logValue("Category", m.getCategory().toString());
                logValue("Description", m.getDescription());
                logValue("Aliases", getAliases(m.getAlias()));
                break;
            }
            case "c":case "cmd":case "command":case "cm":case "cmds": {
                Command c = getCommand(input);
                if(c == null) {
                    Logger.logChat("Invalid Command!");
                    break;
                }
                logValue("Command", c.getName());
                logValue("Description", c.getDescription());
                logValue("Aliases", getAliases(c.getAliases()));
            }
            break;
            default:
                Logger.logChat(this.getError());
        }
    }
    public void logValue(String valueName, String value){
        Logger.logChat(String.format("%s: %s%s", valueName, EnumChatFormatting.GOLD, value));
    }

    public String getAliases(String[] aliases) {
        if(aliases.length == 0) return "None";
        final StringBuilder list = new StringBuilder();
        for(String alias : aliases){
            list.append(EnumChatFormatting.GOLD).append(alias).append("\2478, ");
        }
        return list.toString().substring(0, list.length() - 2);
    }

    public Module getModule(String name){
        Module m = Pandora.getModManager().getModbyName(name);
        if(m == null) m = Pandora.getModManager().getModByAlias(name);
        return m;
    }

    public Command getCommand(String name){
        Command c = Pandora.getCommandManager().getCommandbyName(name);
        if(c == null) c = Pandora.getCommandManager().getCommandByAlias(name);
        return c;
    }
}
