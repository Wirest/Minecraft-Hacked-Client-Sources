
package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.option.BlockSelectionSetting;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Breaker extends Module {
    public Breaker() {
        super("BlockFucker", 0, Module.Category.MISC);
        ArrayList<Block> defaultList = new ArrayList<>();
        defaultList.add(Blocks.cake);
        defaultList.add(Blocks.bed);
        defaultList.add(Blocks.wool);
        defaultList.add(Blocks.end_stone);
        this.addBlockSelection("Target Blocks", defaultList);
        this.addBoolean("Cake Check", true);
    }

    @Handler
    public Consumer<UpdateEvent> onEvent = (event) -> {
        final int radius = 5;
        BlockSelectionSetting setting = (BlockSelectionSetting) Memeware.INSTANCE.getSettingsManager().getSettingByMod(this.getClass(), "Target Blocks");
        for (int x = -radius; x < radius; ++x) {
            for (int y2 = radius; y2 > -radius; --y2) {
                for (int z = -radius; z < radius; ++z) {
                    final int xPos = (int) this.mc.thePlayer.posX + x;
                    final int yPos = (int) this.mc.thePlayer.posY + y2;
                    final int zPos = (int) this.mc.thePlayer.posZ + z;
                    final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    final Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block == Blocks.cake || block == Blocks.end_stone) {
                        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    }
                }
            }
        }
    };

    private void breakBlock(BlockPos blockPos) {
        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
    }

    private int pickaxeSlot() {
        for (int j = 0; j < 8; ++j) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[j];
            if (stack != null && stack.getItem() instanceof ItemPickaxe) {
                return j;
            }
        }
        return -10;
    }

    private boolean cakeBesideOrAt(BlockPos blockPos) {
        for (EnumFacing face : new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN}) {
            if (mc.theWorld.getBlockState(blockPos.offset(face)).getBlock() == Blocks.cake) {
                return true;
            }
        }
        return mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.cake;
    }

    private boolean cakeCovered(BlockPos blockPos) {
        for (EnumFacing face : new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP}) {
            if (mc.theWorld.getBlockState(blockPos.offset(face)).getBlock() == Blocks.air) {
                return false;
            }
        }
        return true;
    }
}
