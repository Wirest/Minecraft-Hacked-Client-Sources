package splash.client.modules.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 15:19, 31-May-20
 * Project: Client
 */

public class CakeEater extends Module {

    private Stopwatch stopwatch;

    public boolean isGameStarted;
    public CakeEater() {
        super("CakeEater", "Eats cakes.", ModuleCategory.MISC);
    	stopwatch = new Stopwatch();
    }

    @Override
    public void onEnable() {
    	stopwatch.reset();
    	super.onEnable();
    }

    @Collect
    public void onEventReceive(EventPacketReceive eventPacketReceive) {
        if(eventPacketReceive.getReceivedPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) eventPacketReceive.getReceivedPacket();

            if(!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
                String message = s02PacketChat.getChatComponent().getUnformattedText();

                if(message.contains("Game - Cake Wars Standard")) {
                    ClientLogger.printToMinecraft("Start count");
                    isGameStarted = true;
                }
            }
        }
    }
    
    @Collect
    public void onUpdate(EventPlayerUpdate e) {
        if(isGameStarted && stopwatch.delay(30000)) {
            Splash.getInstance().getNotificationManager().show(new Notification("Cake Timer", "You can now eat cakes", 1));
            stopwatch.reset();
        }
        if (!isGameStarted) {
	        final boolean breaking = false;
	        for (int radius = 7, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    final int xPos = (int)this.mc.thePlayer.posX + x;
	                    final int yPos = (int)this.mc.thePlayer.posY + y;
	                    final int zPos = (int)this.mc.thePlayer.posZ + z;
	                    final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
	                    final Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
	                    if ((block.getBlockState().getBlock() == Block.getBlockById(92) || block.getBlockState().getBlock() == Blocks.bed) && mc.thePlayer.ticksExisted % 3 == 0) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
	        }
        }
    }
}
