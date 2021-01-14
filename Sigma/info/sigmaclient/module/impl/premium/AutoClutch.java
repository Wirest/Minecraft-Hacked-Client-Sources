package info.sigmaclient.module.impl.premium;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.util.BlockData;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.List;

public class AutoClutch extends Module {
    private int distanceToTheGround = 5;
    private Timer timer = new Timer();
    private BlockData blockBelowData;
    private BlockPos lastBlockPos;
    private double fallStartY = 0;

    public AutoClutch() {
        super(new ModuleData(ModuleData.Type.Other, "AutoClutch", "Place a block under you when you are falling"));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate eu = (EventUpdate) event;
            if (!mc.thePlayer.onGround
                    && eu.isPre()
                    && mc.thePlayer.motionY > 0
                    && mc.gameSettings.keyBindJump.pressed
                    && lastBlockPos != null
                    && lastBlockPos.getX() == Math.floor(mc.thePlayer.posX)
                    && lastBlockPos.getZ() == Math.floor(mc.thePlayer.posZ)) {
                BlockPos blockBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                IBlockState blockState = mc.theWorld.getBlockState(blockBelow);
                if (blockState.getBlock() == Blocks.air && timer.delay(100)) {
                    blockBelowData = getBlockData(blockBelow);
                    if (blockBelowData != null) {
                        //Face in the center of the block
                        float[] rotations = MoveUtils.getRotationsBlock(blockBelowData.position, blockBelowData.face);
                        eu.setYaw(rotations[0]);
                        eu.setPitch(rotations[1]);
                    }
                }
            } else if (!mc.thePlayer.onGround && mc.thePlayer.motionY < 0) {
                if (fallStartY < mc.thePlayer.posY) {
                    fallStartY = mc.thePlayer.posY;
                }
                if (fallStartY - mc.thePlayer.posY > 2) {
                    if (eu.isPre()) {
                        //Get block based off of movement
                        double x = mc.thePlayer.posX;
                        double y = mc.thePlayer.posY - 1.5;
                        double z = mc.thePlayer.posZ;

                        //Checks if the block below is a valid block + timer delay
                        BlockPos blockBelow = new BlockPos(x, y, z);
                        IBlockState blockState = mc.theWorld.getBlockState(blockBelow);

                        IBlockState underBlockState = mc.theWorld.getBlockState(new BlockPos(x, y - 1, z));

                        if (!underBlockState.getBlock().isSolidFullCube() && !mc.thePlayer.isSneaking() && (blockState.getBlock() == Blocks.air ||
                                blockState.getBlock() == Blocks.snow_layer ||
                                blockState.getBlock() == Blocks.tallgrass) && timer.delay(100)) {
                            timer.reset();
                            //Grab the block data for the block below
                            lastBlockPos = blockBelow;
                            blockBelowData = getBlockData(blockBelow);
                            if (blockBelowData != null) {
                                //Face in the center of the block
                                float[] rotations = MoveUtils.getRotationsBlock(blockBelowData.position, blockBelowData.face);
                                eu.setYaw(rotations[0]);
                                eu.setPitch(rotations[1]);
                            }
                        }
                    }
                } else {
                    blockBelowData = null;
                }
            } else if (eu.isPre()) {
                fallStartY = 0;
                blockBelowData = null;
            }

            if (eu.isPost() && blockBelowData != null) {
                for (int i = 36; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        Item item = is.getItem();
                        if (item instanceof ItemBlock && !BlockUtils.getBlacklistedBlocks().contains(((ItemBlock) item).getBlock()) && !((ItemBlock) item).getBlock().getLocalizedName().toLowerCase().contains("chest")) {
                            mc.rightClickDelayTimer = 2;
                            int currentItem = mc.thePlayer.inventory.currentItem;

                            //Swap to block.
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                            mc.thePlayer.inventory.currentItem = i - 36;
                            mc.playerController.updateController();

                            //Caused a null pointer for some reason, will look into soon.
                            try {
                                if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockBelowData.position, blockBelowData.face, new Vec3(blockBelowData.position.getX(), blockBelowData.position.getY(), blockBelowData.position.getZ()))) {
                                    mc.thePlayer.swingItem();
                                }
                            } catch (Exception ignored) {

                            }
                            blockBelowData = null;

                            //Reset to current hand.
                            mc.thePlayer.inventory.currentItem = currentItem;
                            mc.playerController.updateController();
                            return;
                        }
                    }
                }
            }
        }
    }

    private BlockData getBlockData(BlockPos pos) {
        /*if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }*/
        if (!BlockUtils.getBlacklistedBlocks().contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!BlockUtils.getBlacklistedBlocks().contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!BlockUtils.getBlacklistedBlocks().contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!BlockUtils.getBlacklistedBlocks().contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        /*BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklistedBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }*/
        return null;
    }
}
