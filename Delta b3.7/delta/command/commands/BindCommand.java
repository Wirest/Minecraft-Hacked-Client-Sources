/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.util.EnumChatFormatting
 *  org.lwjgl.input.Keyboard
 */
package delta.command.commands;

import delta.client.DeltaClient;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand
implements ICommand {
    public String getName() {
        return "bind";
    }

    public String[] getAliases() {
        String[] arrstring = new String[]{"b", "bound"};
        return arrstring;
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 2) {
            String string = arrstring[0];
            String string2 = arrstring[1];
            IModule iModule = DeltaClient.instance.managers.modulesManager.getModule(string);
            if (iModule != null) {
                if (string2.equalsIgnoreCase("None")) {
                    iModule.setKey(0);
                    this.printMessage(iCommandListener, "Module " + (Object)EnumChatFormatting.DARK_PURPLE + iModule.getName() + (Object)EnumChatFormatting.GRAY + " unbind");
                } else {
                    int n = Keyboard.getKeyIndex((String)string2);
                    iModule.setKey(n);
                    this.printMessage(iCommandListener, "Module " + (Object)EnumChatFormatting.DARK_PURPLE + iModule.getName() + (Object)EnumChatFormatting.GRAY + " bind sur " + (Object)EnumChatFormatting.DARK_PURPLE + Keyboard.getKeyName((int)n));
                }
            } else {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, module \"" + string + "\" inconnu.");
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return true;
    }

    public String getDescription() {
        return "D\u00e9finis la touche d'un module";
    }

    public String getHelp() {
        return "\"bind <Module> <Key>\" ou \"bind <Module> None\"";
    }
}

