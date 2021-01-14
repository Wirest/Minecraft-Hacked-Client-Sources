package cn.kody.debug.mod.mods.MOVEMENT;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventBlockBounds;
import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
/**
 * 
 * @author ±»Íæ»µÁË
 *
 */
public class Jesus extends Mod{
    private int tick;
    int stage;
    int water;
    TimeHelper timer;
    public Value<String> mode;
    
    public Jesus() {
        super("Jesus", Category.MOVEMENT);
        this.timer = new TimeHelper();
        (this.mode = new Value<String>("Jesus", "Mode", 0)).addValue("NCP");
        this.mode.addValue("Dolphin");
    }
    @Override
    public void onEnable() {
        this.stage = 0;
        this.water = 0;
        super.onEnable();
    }
    
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.packet instanceof S08PacketPlayerPosLook) {
            this.stage = 0;
        }
    }
    
    @EventTarget
    public void onMotion(EventPreMotion eventMotion) {
        if (eventMotion.isPre()) {
            boolean shouldJesus = this.shouldJesus();
            if (this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isSneaking() && shouldJesus && !this.mc.gameSettings.keyBindJump.pressed) {
                this.mc.thePlayer.motionY = 0.09;
            }
            if (this.isOnLiquid(0.001) && this.mode.isCurrentMode("NCP") && this.isTotalOnLiquid(0.001) && this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater()) {
                double y = eventMotion.getY();
                double n;
                if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                    n = 1.0E-10;
                }
                else {
                    n = -1.0E-12;
                }
                eventMotion.setY(y + n);
            }
            if (this.mode.isCurrentMode("Dolphin")) {
                if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && shouldJesus) {
                    this.stage = 1;
                    this.timer.reset();
                }
                if (this.stage > 0 && !this.timer.delay(2500.0)) {
                    if ((this.mc.thePlayer.isCollidedVertically && !this.isOnGround(0.001)) || this.mc.thePlayer.isSneaking()) {
                        this.stage = -1;
                    }
                    EntityPlayerSP thePlayer = this.mc.thePlayer;
                    thePlayer.motionX *= 0.0;
                    EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                    thePlayer2.motionZ *= 0.0;
                    if (!this.isInLiquid() && !this.mc.thePlayer.isInWater()) {
                       PlayerUtil.setMotion(0.25 + this.getSpeedEffect() * 0.05);
                    }
                    double motionY = this.getMotionY(this.stage);
                    if (motionY != -999.0) {
                        this.mc.thePlayer.motionY = motionY;
                    }
                    ++this.stage;
                }
            }
        }
    }
    
    public int getSpeedEffect() {
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public double getMotionY(double n) {
        --n;
        final double[] array = { 0.5, 0.484, 0.468, 0.436, 0.404, 0.372, 0.34, 0.308, 0.276, 0.244, 0.212, 0.18, 0.166, 0.166, 0.156, 0.123, 0.135, 0.111, 0.086, 0.098, 0.073, 0.048, 0.06, 0.036, 0.0106, 0.015, 0.004, 0.004, 0.004, 0.004, -0.013, -0.045, -0.077, -0.109 };
        if (n < array.length && n >= 0.0) {
            return array[(int)n];
        }
        return -999.0;
    }
    
    public boolean isInLiquid() {
        if (this.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean b = false;
        final int p_i330_2_ = (int)this.mc.thePlayer.getEntityBoundingBox().minY;
        int i = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minX);
        while (i < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxX) + 1) {
            int j = MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().minZ);
            while (j < MathHelper.floor_double(this.mc.thePlayer.getEntityBoundingBox().maxZ) + 1) {
                final Block block = this.mc.theWorld.getBlockState(new BlockPos(i, p_i330_2_, j)).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    b = true;
                }
                ++j;
            }
            ++i;
        }
        return b;
    }
    
    public boolean isOnGround(final double n) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty();
    }
    
    @EventTarget
    public void onLiquid(EventBlockBounds e) {//sigma event
        int intValue = -1;
        if (e.getPos().getY() + 0.9 < this.mc.thePlayer.boundingBox.minY) {
            if (this.mc.theWorld.getBlockState(e.getPos()).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer) {
                intValue = (int)this.mc.theWorld.getBlockState(e.getPos()).getProperties().get((Object)BlockLiquid.LEVEL);
            }
            if (intValue <= 4) {
                final double p_i485_1_ = e.getPos().getX();
                final double p_i485_3_ = e.getPos().getY();
                final double p_i485_5_ = e.getPos().getZ();
                final double p_i485_7_ = e.getPos().getX() + 1;
                final double n = e.getPos().getY();
                double n2;
                if (this.mode.isCurrentMode("NCP")) {
                    n2 = 1.0;
                }
                else {
                    n2 = 0.999;
                }
                e.setBounds(new AxisAlignedBB(p_i485_1_, p_i485_3_, p_i485_5_, p_i485_7_, n + n2, e.getPos().getZ() + 1));
                e.setCancelled(this.shouldSetBoundingBox());
            }
        }
    }
    
    private boolean shouldSetBoundingBox() {
        boolean b;
        if (!this.mc.thePlayer.isSneaking() && this.mc.thePlayer.fallDistance < 12.0f) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    boolean shouldJesus() {
        final double posX = this.mc.thePlayer.posX;
        final double posY = this.mc.thePlayer.posY;
        final double posZ = this.mc.thePlayer.posZ;
        for (final BlockPos p_getBlockState_1_ : new ArrayList<BlockPos>(Arrays.asList(new BlockPos(posX + 0.3, posY, posZ + 0.3), new BlockPos(posX - 0.3, posY, posZ + 0.3), new BlockPos(posX + 0.3, posY, posZ - 0.3), new BlockPos(posX - 0.3, posY, posZ - 0.3)))) {
            if (!(this.mc.theWorld.getBlockState(p_getBlockState_1_).getBlock() instanceof BlockLiquid)) {
                continue;
            }
            else {
                if (this.mc.theWorld.getBlockState(p_getBlockState_1_).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer && (int)this.mc.theWorld.getBlockState(p_getBlockState_1_).getProperties().get((Object)BlockLiquid.LEVEL) <= 4) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean isOnLiquid(final double n) {
        boolean b = false;
        if (this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - n, this.mc.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
            b = true;
        }
        return b;
    }
    
    public boolean isTotalOnLiquid(final double n) {
        double minX = this.mc.thePlayer.boundingBox.minX;
        while (minX < this.mc.thePlayer.boundingBox.maxX) {
            double minZ = this.mc.thePlayer.boundingBox.minZ;
            while (minZ < this.mc.thePlayer.boundingBox.maxZ) {
                final Block block = this.mc.theWorld.getBlockState(new BlockPos(minX, this.mc.thePlayer.posY - n, minZ)).getBlock();
                if (!(block instanceof BlockGrass) && !(block instanceof BlockAir)) {
                    return false;
                }
                minZ += 0.009999999776482582;
            }
            minX += 0.009999999776482582;
        }
        return true;
    }
    
    
}
