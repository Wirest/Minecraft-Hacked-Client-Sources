// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.command.commands;

import net.minecraft.entity.EntityLivingBase;
import me.aristhena.client.module.modules.combat.aura.Dick22;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.command.Com;
import me.aristhena.client.command.Command;

@Com(names = { "focus", "foc" })
public class Focus extends Command
{
    @Override
    public void runCommand(final String[] args) {
        if (args.length < 2) {
            ClientUtils.sendMessage(this.getHelp());
            return;
        }
        if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("c")) {
            Dick22.focusTarget = null;
            ClientUtils.sendMessage("Focus target cleared");
            return;
        }
        final String targetName = args[1];
        if (ClientUtils.world().getPlayerEntityByName(targetName) != null) {
            final EntityLivingBase target = Dick22.focusTarget = ClientUtils.world().getPlayerEntityByName(targetName);
            ClientUtils.sendMessage("Focus target set to " + target.getName());
        }
        else {
            ClientUtils.sendMessage("No player with the name of " + targetName);
        }
    }
    
    @Override
    public String getHelp() {
        return "Focus - (focus <foc> | clear <c>) [name].";
    }
}
