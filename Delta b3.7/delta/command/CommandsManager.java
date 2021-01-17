/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  me.xtrm.delta.api.command.ICommandManager
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command;

import delta.command.commands.BindCommand;
import delta.command.commands.FriendCommand;
import delta.command.commands.HelpCommand;
import delta.command.commands.SpammerMsgCommand;
import delta.command.commands.ToggleCommand;
import delta.command.commands.VclipCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import me.xtrm.delta.api.command.ICommandManager;
import net.minecraft.util.EnumChatFormatting;

public class CommandsManager
implements ICommandManager {
    private List<ICommand> commands = new ArrayList<ICommand>();

    public List<ICommand> getCommands() {
        return this.commands;
    }

    public void runCommand(ICommandListener iCommandListener, String string) {
        iCommandListener.print((Object)EnumChatFormatting.GRAY + "> " + string);
        if (string.startsWith("./")) {
            string = string.replaceFirst("./", "");
        }
        if (string.equalsIgnoreCase("clear") || string.equalsIgnoreCase("cls")) {
            iCommandListener.clear();
            return;
        }
        if (string.equalsIgnoreCase("matrix")) {
            Random random = new Random();
            iCommandListener.clear();
            for (int i = 0; i < 20; ++i) {
                int n;
                StringBuilder stringBuilder;
                if (i == 9) {
                    stringBuilder = new StringBuilder();
                    for (n = 0; n < 20; ++n) {
                        stringBuilder.append(random.nextInt(2) == 1 ? (Object)EnumChatFormatting.GREEN + "1" : (Object)EnumChatFormatting.DARK_GREEN + "0");
                    }
                    stringBuilder.append((Object)EnumChatFormatting.RED + " HACKED ");
                    for (n = 0; n < 22; ++n) {
                        stringBuilder.append(random.nextInt(2) == 1 ? (Object)EnumChatFormatting.GREEN + "1" : (Object)EnumChatFormatting.DARK_GREEN + "0");
                    }
                    iCommandListener.print(stringBuilder.toString());
                    continue;
                }
                stringBuilder = new StringBuilder();
                for (n = 0; n < 49; ++n) {
                    stringBuilder.append(random.nextInt(2) == 1 ? (Object)EnumChatFormatting.GREEN + "1" : (Object)EnumChatFormatting.DARK_GREEN + "0");
                }
                iCommandListener.print((Object)EnumChatFormatting.DARK_GREEN + stringBuilder.toString());
            }
            return;
        }
        boolean n = false;
        String string2 = string.trim();
        boolean bl = string2.trim().contains(" ");
        String string3 = bl ? string2.split(" ")[0] : string2.trim();
        String[] arrstring = bl ? string2.substring(string3.length()).trim().split(" ") : new String[0];
        for (ICommand iCommand : this.commands) {
            if (iCommand.getName().trim().equalsIgnoreCase(string3.trim())) {
                n = true;
                if (iCommand.execute(iCommandListener, arrstring)) break;
                iCommandListener.print((Object)EnumChatFormatting.RED + "Une erreur s'est produite durant l'execution.");
                break;
            }
            for (String string4 : iCommand.getAliases()) {
                if (!string4.trim().equalsIgnoreCase(string3.trim())) continue;
                n = true;
                if (iCommand.execute(iCommandListener, arrstring)) break;
                iCommandListener.print((Object)EnumChatFormatting.RED + "Une erreur s'est produite durant l'execution.");
                break;
            }
            if (n) continue;
        }
        if (!n) {
            iCommandListener.print((Object)EnumChatFormatting.RED + "Commande inconnue. Tapez " + (Object)EnumChatFormatting.GOLD + "\"help\" " + (Object)EnumChatFormatting.RED + "pour l'aide");
        }
    }

    public void registerCommands() {
        this.commands.add(new BindCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ToggleCommand());
        this.commands.add(new VclipCommand());
        this.commands.add(new SpammerMsgCommand());
    }
}

