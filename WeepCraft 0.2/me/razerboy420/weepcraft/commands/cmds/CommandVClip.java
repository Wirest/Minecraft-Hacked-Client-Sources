/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

public class CommandVClip
extends Command {
    public CommandVClip() {
        super(new String[]{"up", "vclip"}, "Go up or down", "\".up <Amount>\"");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            Float amounttomovemyfatassthroughblocksbecauseicantbefuckedtobreakit = Float.valueOf(Float.parseFloat(args[1]));
            Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)amounttomovemyfatassthroughblocksbecauseicantbefuckedtobreakit.floatValue(), Wrapper.getPlayer().posZ);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

