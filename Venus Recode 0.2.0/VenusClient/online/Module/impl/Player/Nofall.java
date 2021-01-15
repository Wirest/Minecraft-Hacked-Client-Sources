package VenusClient.online.Module.impl.Player;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Nofall extends Module {

    public Nofall() {
        super("Nofall", "Nofall", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
        if (event.getType() == EventMotionUpdate.Type.PRE) {
            if (mc.thePlayer.fallDistance > 3.0F && isBlockUnder() && !Client.instance.moduleManager.getModuleByName("Fly").isEnabled() && (
                    mc.thePlayer.posY % 0.0625D != 0.0D || mc.thePlayer.posY % 0.015256D != 0.0D)) {
                mc.getNetHandler().addToSendQueueSilent((Packet) new C03PacketPlayer(true));
                mc.thePlayer.fallDistance = 0.0F;
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)(mc.thePlayer.posY - 1.0D); i > 0; ) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockAir) {
                i--;
                continue;
            }
            return true;
        }
        return false;
    }

}
