package nivia.commands.commands;

import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.files.modulefiles.Modules;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.io.IOException;
import java.util.Objects;

public class Rename extends Command{
    public Rename(){
        super("Rename", "Changes a module's display name.", Logger.LogExecutionFail("Usage, Options:", new String[]{ "mod", "list", "clear"}) , false , "name", "tag");
    }

    @Override
    public void execute(String commandName, String[] arguments) {
        if(arguments[1].equalsIgnoreCase("clear")){
            for(Module mod : Pandora.getModManager().mods) {
                if(mod.getName().equalsIgnoreCase(mod.getTag())) continue;
                mod.setTag(mod.getName());
            }
            Logger.logChat("Reset names.");
            return;
        }
        if(arguments[1].equalsIgnoreCase("list")){
            for(Module mod : Pandora.getModManager().mods) {
                if (mod.getName().equalsIgnoreCase(mod.getTag())) continue;
                Logger.logChat(String.format("%s: %s%s", mod.getName(), EnumChatFormatting.AQUA, mod.getTag()));
            }
            return;
        }
        Module mod = Pandora.getModManager().getModbyName(arguments[1]);
        if(Objects.isNull(mod)) mod = Pandora.getModManager().getModByAlias(arguments[1]);
        if(Objects.isNull(mod)){
            Logger.logChat("Invalid Module.");
            return;
        }
        String newName = "";
        try{ newName = arguments[2]; } catch (Exception e){ Logger.LogExecutionFail("tag."); return;}
        try{ if(!arguments[3].isEmpty()) newName = arguments[2] + " " + arguments[3]; } catch (Exception e) {}
        try{ if(!arguments[4].isEmpty()) newName = arguments[2] + " " + arguments[3] + " " + arguments[4]; } catch (Exception e) {}
        try{ if(!arguments[5].isEmpty()) newName = arguments[2] + " " + arguments[3] + " " + arguments[4] + " " + arguments[5]; } catch (Exception e) {}
        try{ if(!arguments[6].isEmpty()) newName = arguments[2] + " " + arguments[3] + " " + arguments[4] + " " + arguments[5] + " " + arguments[6]; } catch (Exception e) {}
        mod.setTag(newName);
        Logger.logChat("Set \247b" + mod.getName()+ " \2477new name to \247b" + mod.getTag());
        try {
            Pandora.getFileManager().getFile(Modules.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
