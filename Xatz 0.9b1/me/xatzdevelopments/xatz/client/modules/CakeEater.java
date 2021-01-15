package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.TimerS;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;

public class CakeEater extends Module {
	
	 public static BlockPos blockBreaking;
	 private TimerS timer = new TimerS();
	 private double xPos, yPos, zPos, minx;      
	

	public CakeEater() {
		super("CakeEater", Keyboard.KEY_NONE, Category.MINIGAMES, "Eats cakes");
	}

	@Override
	public void onDisable() {
		blockBreaking = null;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		
		int radius = 5;
        for(int x = -radius; x < radius; x++){
            for(int y = radius; y > -radius; y--){
                    for(int z = -radius; z < radius; z++){
                            this.xPos = mc.thePlayer.posX + x;
                            this.yPos = mc.thePlayer.posY + y;
                            this.zPos = mc.thePlayer.posZ + z;
                           
                            BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
                            Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            
                            if(block instanceof BlockCake){
                            	minx = block.getBlockBoundsMinX();

                            	
                         			mc.thePlayer.swingItem();
                                                          
                            	 mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(blockPos, 1, mc.thePlayer.inventory.getCurrentItem(), 1f, 1f,1f));      
                            	 blockBreaking = blockPos;
                            	 return;
                            }
                    }
            	}
            }
        blockBreaking = null;
                
            
           super.onUpdate();
       }
                  
	
            public float[] getBlockRotations(double x, double y, double z) {
                double var4 = x - mc.thePlayer.posX + 0.5;
                double var5 = z - mc.thePlayer.posZ + 0.5;
                double var6 = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 1.0);
                double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
                float var8 = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
                return new float[]{var8, (float) (-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793))};
            }     
            
            
  private EnumFacing getFacingDirection(BlockPos pos) {
    EnumFacing direction = null;
    if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.UP;
    } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.DOWN;
    } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.EAST;
    } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.WEST;
    } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.SOUTH;
    } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
        direction = EnumFacing.NORTH;
    }
    MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
    if (rayResult != null && rayResult.getBlockPos() == pos) {
        return rayResult.sideHit;
    }
    return direction;
  }
  
  private boolean blockChecks(Block block) {
      return block == Blocks.cake;
  }
}
