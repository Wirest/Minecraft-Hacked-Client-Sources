package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.movement.Flight;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 22:23, 13-Jun-20
 * Project: Client
 */
public class AntiVoid extends Module {

    private boolean motion;
    private BlockPos lastSafePos;

    public AntiVoid() {
        super("AntiVoid", "Saves you from falling into void.", ModuleCategory.MOVEMENT);
    }

    @Collect
    public void onMove(EventPlayerUpdate event) {
 
        if(Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive())
            return;
        if (mc.thePlayer.onGround) {
        	lastSafePos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        }
        if(!isBlockUnder()) {
            if(mc.thePlayer.fallDistance > 2.9f) {
            	if (motion) {

                    mc.thePlayer.setPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ());
            		motion = false;
            	} else { 
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(lastSafePos.getX(), lastSafePos.getY(), lastSafePos.getZ(), true));
                    mc.thePlayer.fallDistance = 0;
            	}
            } else {
            	motion = true;
            }
        } else {
        	motion = true;
        }
    }

    public static boolean isBlockUnder() {
        if(Minecraft.getMinecraft().thePlayer.posY < 0)
            return false;
        for(int off = 0; off < (int)Minecraft.getMinecraft().thePlayer.posY+2; off += 2){
            AxisAlignedBB bb = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if(!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, bb).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
