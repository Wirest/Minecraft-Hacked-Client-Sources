package me.slowly.client.mod.mods.combat;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.Mod.Category;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
public class KeepSprint extends Mod {
	public KeepSprint() {
		super("KeepSprint", Category.COMBAT, Colors.DARKBLUE.c);
	}
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("KeepSprint Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("KeepSprint Enable", ClientNotification.Type.SUCCESS);
    }
}


