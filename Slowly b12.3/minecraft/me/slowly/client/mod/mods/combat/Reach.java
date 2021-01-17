package me.slowly.client.mod.mods.combat;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;

public class Reach extends Mod {
	 
   public static boolean Reach;
	
	public static Value<Double> range = new Value("Reach_Range", Double.valueOf(4.2D), Double.valueOf(0.1D), Double.valueOf(8.0D), 0.1D);

	public Reach() {
           super("Reach", Category.COMBAT, Colors.BLACK.c);
	}
 
	@Override
    public void onEnable() {
        super.onEnable();
        Reach = true;
        ClientUtil.sendClientMessage("Reach Enable", ClientNotification.Type.SUCCESS);
        
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Reach = false;
        ClientUtil.sendClientMessage("Reach Disable", ClientNotification.Type.ERROR);
    }
    
}
