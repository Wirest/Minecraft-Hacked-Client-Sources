package cheatware.module.combat;

import cheatware.event.EventTarget;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow", 0, Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if(mc.gameSettings.keyBindSprint.pressed && mc.thePlayer.movementInput.moveForward > 0 && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.setSprinting(true);
    	}
    	if(mc.thePlayer.isBlocking()) {
    		mc.playerController.syncCurrentPlayItem();
        	mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    	}
    }
}
