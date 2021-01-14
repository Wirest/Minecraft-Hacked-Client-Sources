package store.shadowclient.client.module.player;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", 0, Category.PLAYER);
		
		ArrayList<String> options = new ArrayList<>();
		options.add("Vanilla");
        options.add("NCP");
        options.add("AAC");
        
        Shadow.instance.settingsManager.rSetting(new Setting("FastEat Mode", this, "NCP", options));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		String mode = Shadow.instance.settingsManager.getSettingByName("FastEat Mode").getValString();
        this.setDisplayName("FastEat §7| " + mode);
        
        if(mode.equalsIgnoreCase("NCP")) {
        	mc.rightClickDelayTimer = 1;
    	    if (mc.thePlayer.isUsingItem()) {
    	      mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01D, mc.thePlayer.posZ, false));
    	    }
        }
        
        if(mode.equalsIgnoreCase("AAC")) {
        	if(Minecraft.getMinecraft().thePlayer.isEating()) {
				Minecraft.getMinecraft().timer.timerSpeed = 1.2092F;
			}else if (!Minecraft.getMinecraft().thePlayer.isEating()){
				Minecraft.getMinecraft().timer.timerSpeed = 1.0F;
			}
        }
        
        if(mode.equalsIgnoreCase("Vanilla")) {
        	if(mc.thePlayer.onGround && mc.thePlayer.getItemInUseDuration() >= 15 && mc.thePlayer.getItemInUse().getItem() instanceof ItemFood) {
				for (int i = 0; i <= 20; i++){
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
        	}
        }
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Minecraft.getMinecraft().thePlayer.stepHeight = 0.6f;
		Minecraft.getMinecraft().timer.timerSpeed = 1.0F;
		super.onDisable();
	}

}
