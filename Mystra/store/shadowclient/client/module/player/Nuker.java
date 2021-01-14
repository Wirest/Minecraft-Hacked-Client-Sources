package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Nuker extends Module {

	public Nuker() {
		super("Nuker", 0, Category.PLAYER);
	}
	
	public static int size = 5;
	public static int sizeOther = Math.round(size / 2);
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.thePlayer.capabilities.isCreativeMode){
			for(int x = -size; x < size + sizeOther; x++){
				for(int z = -size; z < size + sizeOther; z++){
					for(int y = -size; y < size + sizeOther; y++){
						boolean shouldBreakBlock = true;
	        			int blockX = (int) (mc.thePlayer.posX + x);
	        			int blockY = (int) (mc.thePlayer.posY + y);
	        			int blockZ = (int) (mc.thePlayer.posZ + z);
	        			if (Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(blockX, blockY, blockZ)).getBlock()) != 0){
	        				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(blockX, blockY, blockZ), EnumFacing.UP));
	         			}
	 				}
	 			}
	 		}
		}
	}
}
