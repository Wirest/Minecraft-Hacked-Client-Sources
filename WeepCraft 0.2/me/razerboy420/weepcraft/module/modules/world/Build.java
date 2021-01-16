/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.world;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPreMotionUpdates;
import darkmagician6.events.EventRightClick;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

@Module.Mod(category=Module.Category.WORLD, description="Build things", key=0, name="Build")
public class Build
extends Module {
    public static Value mode = new Value("build_Mode", "Swastika", new String[]{"Swastika", "Pole", "Penis"});

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        this.setDisplayName("Build [" + Build.mode.stringvalue + "]");
    }

    @EventTarget
    public void onR(EventRightClick event) {
        new Thread("Blocking thread dad"){

            @Override
            public void run() {
                try {
                    boolean overBlock;
                    Thread.sleep(100);
                    boolean bl = overBlock = Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && Wrapper.getPlayer().getHeldItemMainhand().getItem() instanceof ItemBlock;
                    if (overBlock) {
                        BlockPos pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX(), Wrapper.mc().objectMouseOver.getBlockPos().getY(), Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX(), Wrapper.mc().objectMouseOver.getBlockPos().getY() + 1, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX(), Wrapper.mc().objectMouseOver.getBlockPos().getY() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX(), Wrapper.mc().objectMouseOver.getBlockPos().getY() + 3, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 2, Wrapper.mc().objectMouseOver.getBlockPos().getY(), Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 1, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 1, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.EAST, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 1, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.EAST, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.EAST, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 3, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 4, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 1, Wrapper.mc().objectMouseOver.getBlockPos().getY(), Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() + 2, Wrapper.mc().objectMouseOver.getBlockPos().getY(), Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 1, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 4, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.WEST, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                        pos = new BlockPos(Wrapper.mc().objectMouseOver.getBlockPos().getX() - 2, Wrapper.mc().objectMouseOver.getBlockPos().getY() + 4, Wrapper.mc().objectMouseOver.getBlockPos().getZ());
                        Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.WEST, EnumHand.MAIN_HAND, 0.6f, 1.0f, 0.85f));
                    }
                }
                catch (Exception overBlock) {
                    // empty catch block
                }
            }
        }.start();
    }

    @EventTarget
    public void onPacket(EventPacketSent event) {
    }

}

