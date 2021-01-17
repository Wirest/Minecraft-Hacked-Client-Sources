package me.slowly.client.mod.mods.world;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;

public class AutoL
extends Mod {
    public AutoL() {
        super("AutoL", Mod.Category.WORLD, Colors.GREEN.c);
    }
    

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoL Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AutoL Enable", ClientNotification.Type.SUCCESS);
    }
}


