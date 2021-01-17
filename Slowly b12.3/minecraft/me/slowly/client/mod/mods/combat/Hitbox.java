package me.slowly.client.mod.mods.combat;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;

public class Hitbox extends Mod {
	
	 public static Value<Double> size = new Value<Double>("Hitbox_Size", Double.valueOf(0.1D), Double.valueOf(0.1D), Double.valueOf(1.0D), 0.1D);

	public Hitbox() {
		super("Hitbox", Category.COMBAT, Colors.DARKBLUE.c);
	}
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Hitbox Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Hitbox Enable", ClientNotification.Type.SUCCESS);
    }
}