package store.shadowclient.client.module.movement;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class WaterSpeed extends Module {

	TimeHelper timer;
	
	public WaterSpeed() {
		super("FastSwim", 0, Category.MOVEMENT);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Speed", this, 1.2F, 1.1F, 1.25F, false));
		
	}
	
	@EventTarget
    public void onUpdate(EventUpdate event) {
		if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (isInLiquid() && this.timer.hasReached(1000.0)) {
            final EntityPlayerSP thePlayer = mc.thePlayer;
            thePlayer.motionX *= 1.0;
            final EntityPlayerSP thePlayer2 = mc.thePlayer;
            thePlayer2.motionZ *= 1.0;
            mc.thePlayer.motionY = 0.4;
            this.timer.reset();
        }
	}
	
	public static boolean isInLiquid() {
        boolean inLiquid = false;
        if (getBlockAtPosC(mc.thePlayer, 0.30000001192092896, 0.0, 0.30000001192092896).getMaterial().isLiquid() || getBlockAtPosC(mc.thePlayer, -0.30000001192092896, 0.0, -0.30000001192092896).getMaterial().isLiquid()) {
            inLiquid = true;
        }
        return inLiquid;
    }
	
	public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlockAtPos(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }
	
	public static Block getBlockAtPos(final BlockPos inBlockPos) {
        final IBlockState s = mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }
	 
	public static Block getBlock(final BlockPos pos) {
		Minecraft.getMinecraft();
		return mc.theWorld.getBlockState(pos).getBlock();
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
