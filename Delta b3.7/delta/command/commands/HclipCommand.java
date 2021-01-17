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

public class HclipCommand
implements ICommand {
    public String getDescription() {
        return "Vous t\u00e9l\u00e9porte horizontalement";
    }

    public String getName() {
        return "hclip";
    }

    public String[] getAliases() {
        String[] arrstring = new String[208 - 230 + 208 - 23 + -161];
        arrstring[203 - 401 + 341 - 334 + 191] = "horizontalclip";
        arrstring[253 - 471 + 332 - 226 + 113] = "hc";
        return arrstring;
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 87 - 147 + 125 + -64) {
            Integer n;
            block5: {
                n = Integer.parseInt(arrstring[122 - 156 + 132 + -98]);
                if (n != 0) break block5;
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre non nul.");
                return 135 - 245 + 241 - 103 + -27;
            }
            try {
                Class188._dramatic(n.intValue());
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.GRAY + "Vous avez \u00e9t\u00e9 t\u00e9l\u00e9port\u00e9 " + (Object)EnumChatFormatting.DARK_PURPLE + n + (Object)EnumChatFormatting.GRAY + " blocks en " + (Object)EnumChatFormatting.DARK_PURPLE + (n > 0 ? "avant" : "arri\u00e8re"));
            }
            catch (Exception exception) {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre valide.");
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return 260 - 403 + 391 + -247;
    }

    public String getHelp() {
        return "hclip <Nombre>";
    }
}

