/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.util.EnumChatFormatting
 */
package delta.command.commands;

import delta.client.DeltaClient;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.util.EnumChatFormatting;

public class ToggleCommand
implements ICommand {
    public String getHelp() {
        return "toggle <Module>";
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 1) {
            String string = arrstring[0];
            IModule iModule = DeltaClient.instance.managers.modulesManager.getModule(string);
            if (iModule != null) {
                iModule.toggle();
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.GRAY + "Module " + (Object)EnumChatFormatting.DARK_PURPLE + string + (iModule.isEnabled() ? (Object)EnumChatFormatting.GREEN + " activ\u00e9" : (Object)EnumChatFormatting.RED + " desactiv\u00e9"));
            } else {
                this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, module \"" + string + "\" inconnu.");
            }
        } else {
            this.printMessage(iCommandListener, (Object)EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + this.getHelp());
        }
        return true;
    }

    public String[] getAliases() {
        String[] aliases = new String[]{"t", "tgl", "togle"};
        return aliases;
    }

    public String getName() {
        return "toggle";
    }

    public String getDescription() {
        return "Active ou desactive un module";
    }
}

