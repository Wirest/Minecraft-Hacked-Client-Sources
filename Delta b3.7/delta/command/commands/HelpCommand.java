/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command.commands;

import delta.client.DeltaClient;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.util.EnumChatFormatting;

public class HelpCommand
implements ICommand {
    public String getName() {
        return "help";
    }

    public String getDescription() {
        return "Donne des informations sur les commandes";
    }

    public String getHelp() {
        return "\"help\" ou \"help <Commande>\"";
    }

    public String[] getAliases() {
        String[] arrstring = new String[143 - 169 + 154 + -125];
        arrstring[191 - 257 + 125 - 25 + -34] = "h";
        arrstring[77 - 135 + 71 + -12] = "hlp";
        arrstring[179 - 236 + 174 - 85 + -30] = "aide";
        return arrstring;
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 103 - 183 + 132 - 128 + 77) {
            int n;
            Object object2;
            String string = arrstring[224 - 317 + 27 + 66];
            String[] arrstring2 = null;
            for (Object object2 : DeltaClient.instance.managers.commandsManager.getCommands()) {
                if (object2.getName().equalsIgnoreCase(string)) {
                    arrstring2 = object2;
                }
                String[] arrstring3 = object2.getAliases();
                n = arrstring3.length;
                for (int i = 84 - 142 + 134 + -76; i < n; ++i) {
                    String string2 = arrstring3[i];
                    if (!string2.equalsIgnoreCase(string)) continue;
                    arrstring2 = object2;
                }
            }
            if (arrstring2 == null) {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, commande \"" + string + "\" inconnue.");
                return 179 - 308 + 193 + -63;
            }
            this.printMessage(iCommandListener, "Commande: " + (Object)EnumChatFormatting.DARK_PURPLE + arrstring2.getName());
            this.printMessage(iCommandListener, "Description: " + (Object)EnumChatFormatting.GRAY + arrstring2.getDescription());
            this.printMessage(iCommandListener, "Usage: " + (Object)EnumChatFormatting.GRAY + arrstring2.getHelp());
            StringBuilder stringBuilder = new StringBuilder();
            object2 = arrstring2.getAliases();
            int n2 = ((String[])object2).length;
            for (n = 266 - 344 + 27 - 1 + 52; n < n2; ++n) {
                String string3 = object2[n];
                stringBuilder.append((Object)EnumChatFormatting.DARK_PURPLE + string3);
                stringBuilder.append((Object)EnumChatFormatting.GRAY + ", ");
            }
            object2 = stringBuilder.toString();
            object2 = ((String)object2).substring(66 - 85 + 51 - 29 + -3, ((String)object2).length() - (52 - 96 + 39 + 7));
            this.printMessage(iCommandListener, "Aliases: " + (String)object2);
        } else if (arrstring.length == 0) {
            for (ICommand iCommand : DeltaClient.instance.managers.commandsManager.getCommands()) {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.DARK_PURPLE + iCommand.getName() + (Object)EnumChatFormatting.GRAY + " - " + iCommand.getDescription());
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return 158 - 260 + 183 - 130 + 50;
    }
}

