package cheatware.module.player;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventPreMotionUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Keyboard.KEY_NONE, Category.PLAYER);
    }
    
    public static boolean isBlockUnder() {
    	Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayerSP player = mc.thePlayer;
        final WorldClient world = mc.theWorld;
        final AxisAlignedBB pBb = player.getEntityBoundingBox();
        final double height = player.posY + player.getEyeHeight();
        
        for (int offset = 0; offset < height; offset += 2) {
            if (!world.getCollidingBoundingBoxes(player, pBb.offset(0.0, -offset, 0.0)).isEmpty()) {
                return true;
            }
        }
        return false;
     }
    
    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
    	if(mc.thePlayer.fallDistance > 2 && !this.isBlockUnder()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ, mc.thePlayer.onGround));
    	}
    }
}
