package cheatware.module.player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import cheatware.event.EventTarget;
import cheatware.event.events.EventPostMotionUpdate;
import cheatware.event.events.EventPreMotionUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.utils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class Scaffold extends Module
{
    private final List<Block> invalidBlocks;
    private final List<Block> validBlocks;
    private final BlockPos[] blockPositions;
    private final EnumFacing[] facings;
    private final TimerUtil timer;
    private final Random rng;
    private float[] angles;
    private boolean rotating;
    private int slot;
    
    public Scaffold() {
        super("Scaffold", Keyboard.KEY_X, Category.PLAYER);
        this.timer = new TimerUtil();
        this.invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, (Block)Blocks.chest, Blocks.dispenser, Blocks.air, (Block)Blocks.water, (Block)Blocks.lava, (Block)Blocks.flowing_water, (Block)Blocks.flowing_lava, (Block)Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, (Block)Blocks.stone_slab, (Block)Blocks.wooden_slab, (Block)Blocks.stone_slab2, (Block)Blocks.red_mushroom, (Block)Blocks.brown_mushroom, (Block)Blocks.yellow_flower, (Block)Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, (Block)Blocks.stained_glass_pane, Blocks.iron_bars, (Block)Blocks.cactus, Blocks.ladder, Blocks.web);
        this.validBlocks = Arrays.asList(Blocks.air, (Block)Blocks.water, (Block)Blocks.flowing_water, (Block)Blocks.lava, (Block)Blocks.flowing_lava);
        this.blockPositions = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        this.facings = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };
        this.rng = new Random();
        this.angles = new float[2];
    }
    
    public void onEnable() {
        super.onEnable();
        this.angles[0] = 999.0f;
        this.angles[1] = 999.0f;
        this.timer.reset();
    }
    
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onPre(final EventPreMotionUpdate event) {
        event.setPitch(90);
        
        mc.thePlayer.strafe();
        
    	if(!mc.thePlayer.isMoving()) mc.thePlayer.setVelocity(0, mc.thePlayer.motionY, 0);
    	
        final EntityPlayerSP player = this.mc.thePlayer;
        final WorldClient world = Minecraft.theWorld;
        double yDif = 1.0;
        BlockData data = null;
        for (double posY = player.posY - 1.0; posY > 0.0; --posY) {
            final BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
            if (newData != null) {
                yDif = player.posY - posY;
                if (yDif <= 3.0) {
                    data = newData;
                    break;
                }
            }
        }
        int slot = -1;
        int blockCount = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = player.inventory.getStackInSlot(i);
            if (itemStack != null) {
                final int stackSize = itemStack.stackSize;
                if (this.isValidItem(itemStack.getItem()) && stackSize > blockCount) {
                    blockCount = stackSize;
                    slot = i;
                }
            }
        }
        if (data != null && slot != -1) {
            final BlockPos pos = data.pos;
            final Block block = world.getBlockState(pos.offset(data.face)).getBlock();
            final Vec3 hitVec = this.getVec3(data);
            if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
                return;
            }
            if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            	player.setVelocity(0, player.motionY, 0);
                mc.timer.timerSpeed = 1.1F;
                if (this.timer.hasTimeElapsed(650L, true)) {
                    player.motionY = -0.28;
                } else {
                    player.motionY = 0.4;
                	
                }
            } else {
            	mc.timer.timerSpeed = 1;
            }
        }
    }
    
    @EventTarget
    public void onPost(final EventPostMotionUpdate event) {
        final EntityPlayerSP player = this.mc.thePlayer;
        final WorldClient world = Minecraft.theWorld;
        double yDif = 1.0;
        BlockData data = null;
        for (double posY = player.posY - 1.0; posY > 0.0; --posY) {
            final BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
            if (newData != null) {
                yDif = player.posY - posY;
                if (yDif <= 3.0) {
                    data = newData;
                    break;
                }
            }
        }
        if (data != null && this.slot != -1) {
            final BlockPos pos = data.pos;
            final Block block = world.getBlockState(pos.offset(data.face)).getBlock();
            final Vec3 hitVec = this.getVec3(data);
            if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
                return;
            }
            final int last = this.mc.thePlayer.inventory.currentItem;
            this.mc.thePlayer.inventory.currentItem = this.slot;
            if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, Minecraft.theWorld, this.mc.thePlayer.getCurrentEquippedItem(), pos, data.face, hitVec)) {
                this.mc.thePlayer.swingItem();
            }
            this.mc.thePlayer.inventory.currentItem = last;
        }
    }
    
    private boolean isBlockUnder(final double yOffset) {
        final EntityPlayerSP player = this.mc.thePlayer;
        return !this.validBlocks.contains(Minecraft.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
    }
    
    private Vec3 getVec3(final BlockData data) {
        final BlockPos pos = data.pos;
        final EnumFacing face = data.face;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += this.randomNumber(0.3, -0.3);
            z += this.randomNumber(0.3, -0.3);
        }
        else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }
    
    private double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        final BlockPos[] blockPositions = this.blockPositions;
        final EnumFacing[] facings = this.facings;
        final WorldClient world = Minecraft.theWorld;
        final BlockPos posBelow = new BlockPos(0, -1, 0);
        final List<Block> validBlocks = this.validBlocks;
        if (!validBlocks.contains(world.getBlockState(pos.add((Vec3i)posBelow)).getBlock())) {
            return new BlockData(pos.add((Vec3i)posBelow), EnumFacing.UP);
        }
        for (int i = 0, blockPositionsLength = blockPositions.length; i < blockPositionsLength; ++i) {
            final BlockPos blockPos = pos.add((Vec3i)blockPositions[i]);
            if (!validBlocks.contains(world.getBlockState(blockPos).getBlock())) {
                return new BlockData(blockPos, facings[i]);
            }
            for (int i2 = 0; i2 < blockPositionsLength; ++i2) {
                final BlockPos blockPos2 = pos.add((Vec3i)blockPositions[i2]);
                final BlockPos blockPos3 = blockPos.add((Vec3i)blockPositions[i2]);
                if (!validBlocks.contains(world.getBlockState(blockPos2).getBlock())) {
                    return new BlockData(blockPos2, facings[i2]);
                }
                if (!validBlocks.contains(world.getBlockState(blockPos3).getBlock())) {
                    return new BlockData(blockPos3, facings[i2]);
                }
            }
        }
        return null;
    }
    
    private boolean isValidItem(final Item item) {
        if (item instanceof ItemBlock) {
            final ItemBlock iBlock = (ItemBlock)item;
            final Block block = iBlock.getBlock();
            return !this.invalidBlocks.contains(block);
        }
        return false;
    }
    
    private static class BlockData
    {
        public final BlockPos pos;
        public final EnumFacing face;
        
        private BlockData(final BlockPos pos, final EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}
