/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command.commands;

import delta.Class188;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.util.EnumChatFormatting;

public class VclipCommand
implements ICommand {
    public String getName() {
        return "vclip";
    }

    public String[] getAliases() {
        String[] arrstring = new String[]{"verticalclip", "vc"};
        return arrstring;
    }

    public String getDescription() {
        return "Vous t\u00e9l\u00e9porte verticalement";
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 1) {
            Integer n = Integer.parseInt(arrstring[0]);
            if (n == 0) {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre non nul.");
                return false;
            }
            try {
                Class188._neighbor(n.intValue());
                this.printMessage(iCommandListener, "Vous avez \u00e9t\u00e9 t\u00e9l\u00e9port\u00e9 " + (Object)EnumChatFormatting.DARK_PURPLE + n + (Object)EnumChatFormatting.GRAY + " blocks vers le " + (Object)EnumChatFormatting.DARK_PURPLE + (n > 0 ? "haut" : "bas"));
            }
            catch (Exception exception) {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre valide.");
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return true;
    }

    public String getHelp() {
        return "vclip <Nombre>";
    }
}

