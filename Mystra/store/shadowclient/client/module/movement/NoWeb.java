package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoWeb extends Module {

    public NoWeb() {
        super("NoWeb", 0, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
    	if (mc.thePlayer.isInsideOfMaterial(Material.web)) {
    		mc.thePlayer.motionY = 0;
            if (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0) {
            	mc.thePlayer.setSpeed(0.10F);
                }
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY + (mc.gameSettings.keyBindJump.isPressed() ? 0.0625 : mc.gameSettings.keyBindSneak.isPressed() ? -0.0625 : 0), mc.thePlayer.posZ + mc.thePlayer.motionZ, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX,
                mc.theWorld.getHeight(), mc.thePlayer.posZ + mc.thePlayer.motionZ, true));
    	}
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }
    
    @Override
    public void onDisable() {
    	super.onDisable();
    }
}
