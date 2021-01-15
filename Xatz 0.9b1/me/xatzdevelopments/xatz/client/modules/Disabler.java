package me.xatzdevelopments.xatz.client.modules;

import me.xatzdevelopments.xatz.client.events.PreMotionEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C18PacketSpectate;

public class Disabler extends Module {

	public Disabler() {
		super("Disabler", 0, Category.EXPLOITS, "Disables ghostly.live movement checks!");
	}
	
	public void onEnable() {
		Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, 
				"Please relog for the disableto properly work"));
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}
	
	public void onPre(PreMotionEvent event) {
		mc.getNetHandler().addToSendQueueSilent(new C0CPacketInput());
        mc.getNetHandler().addToSendQueueSilent(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
}

}
