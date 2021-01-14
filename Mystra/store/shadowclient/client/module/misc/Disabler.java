package store.shadowclient.client.module.misc;

import java.util.ArrayList;
import java.util.Random;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.MathUtils;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

public class Disabler extends Module {

	public Disabler() {
		super("Disabler", 0, Category.MISC);
		
		ArrayList<String> options = new ArrayList<>();
        options.add("Watchdog");

        
        Shadow.instance.settingsManager.rSetting(new Setting("Disabler Mode", this, "Watchdog", options));
	}
	
	public void onUpdate(EventUpdate event) {
		String mode = Shadow.instance.settingsManager.getSettingByName("Disabler Mode").getValString();
		
		if(mode.equalsIgnoreCase("Watchdog") && Shadow.instance.moduleManager.getModuleByName("Fly").isToggled() || Shadow.instance.moduleManager.getModuleByName("Speed").isToggled() || Shadow.instance.moduleManager.getModuleByName("LongJump").isToggled() || mc.thePlayer.isSpectator()) {
			PlayerCapabilities playerCapabilities = new PlayerCapabilities();
	        playerCapabilities.isFlying = true;
	        playerCapabilities.allowFlying = true;
	        playerCapabilities.setFlySpeed((float)MathUtils.randomNumber(10.1D, 19.0D));
	        mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
		}
	}
	
	public final void onSendPacket(EventSendPacket event) {
		String mode = Shadow.instance.settingsManager.getSettingByName("Disabler Mode").getValString();
	      if (mode.equalsIgnoreCase("Watchdog")) {
	         if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
	            C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)event.getPacket();
	            mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(Integer.MAX_VALUE, packetConfirmTransaction.getUid(), false));
	            event.setCancelled(true);
	         }

	         if (event.getPacket() instanceof C00PacketKeepAlive) {
	            mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive(Integer.MIN_VALUE + (new Random()).nextInt(100)));
	            event.setCancelled(true);
	         }
	      }
	   }
}
