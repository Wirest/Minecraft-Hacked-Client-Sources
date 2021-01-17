/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command.commands;

import delta.Class93;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.util.EnumChatFormatting;

public class SpammerMsgCommand
implements ICommand {
    public String[] getAliases() {
        String[] arrstring = new String[189 - 307 + 58 - 44 + 107];
        arrstring[109 - 195 + 97 + -11] = "spam";
        arrstring[191 - 274 + 272 - 64 + -124] = "automsg";
        arrstring[252 - 408 + 143 + 15] = "autosay";
        return arrstring;
    }

    public String getDescription() {
        return "D\u00e9finit le message du Spammer";
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 0) {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. \n" + (Object)EnumChatFormatting.RED + "Utilisez: " + this.getHelp());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            Object object = arrstring;
            int n = ((String[])object).length;
            for (int i = 124 - 160 + 160 + -124; i < n; ++i) {
                String string = object[i];
                stringBuilder.append(string + " ");
            }
            object = stringBuilder.toString().trim();
            if (((String)object).length() > 99 - 118 + 30 + 219) {
                object = ((String)object).substring(149 - 237 + 31 + 57, 131 - 187 + 108 + 178);
            }
            if (((String)object).equalsIgnoreCase("off")) {
                this.printMessage(iCommandListener, "CustomMessage du Spammer desactiv\u00e9");
                Class93.basin$ = "off";
                return 251 - 451 + 78 + 123;
            }
            Class93.basin$ = object;
            this.printMessage(iCommandListener, "Message du Spammer d\u00e9finis en: ");
            this.printMessage(iCommandListener, "\"" + (String)object + "\"");
        }
        return 206 - 328 + 57 + 66;
    }

    public String getHelp() {
        return "\"spammer <Message>\" ou \"spammer off\"";
    }

    public String getName() {
        return "spammer";
    }
}

