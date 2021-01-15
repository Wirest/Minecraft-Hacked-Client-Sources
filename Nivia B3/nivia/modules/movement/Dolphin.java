package nivia.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.utils.BlockUtils;
import nivia.utils.utils.Timer;

public class Dolphin extends Module {
	boolean getDown;
	private boolean wasWater = false;
	private int ticks = 0;
	
	private Timer timer = new Timer();
	public Dolphin() {
		super("Dolphin", 0, 0x75FF47, Category.MOVEMENT, "Leap 4.3 blocks whilst on water.",
				new String[] { "dolphin", "dolph" }, true);
	}


	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {
	    if (Helper.player().onGround || Helper.player().isOnLadder())
		wasWater = false;

	if (Helper.player().motionY > 0 && wasWater) {
		if (Helper.player().motionY <= 0.1127)
		    Helper.player().motionY *= 1.267;
		Helper.player().motionY += 0.05172;
	}

	if (isInLiquid() && !Helper.player().isSneaking()) {
		if (ticks < 3) {
			Helper.player().motionY = 0.13;
			ticks++;
			wasWater = false;
		} else {
		    Helper.player().motionY = 0.5;
			ticks = 0;
			wasWater = true;
			}
		}
	}
	
	public static boolean isInLiquid() {
	      AxisAlignedBB par1AxisAlignedBB = Wrapper.getPlayer().boundingBox.contract(0.001D, 0.001D, 0.001D);
	      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
	      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
	      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
	      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
	      int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
	      int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
	      new Vec3(0.0D, 0.0D, 0.0D);

	      for(int var12 = var4; var12 < var5; ++var12) {
	         for(int var13 = var6; var13 < var7; ++var13) {
	            for(int var14 = var8; var14 < var9; ++var14) {
	               Minecraft.getMinecraft();
	               Block var15 = BlockUtils.getBlock(var12, var13, var14);
	               if(var15 instanceof BlockLiquid) {
	                  return true;
	               }
	            }
	         }
	      }

	      return false;
	   }
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	protected void addCommand() {
	}
}
