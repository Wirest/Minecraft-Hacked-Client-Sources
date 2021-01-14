package cn.kody.debug.mod.mods.WORLD;

import cn.kody.debug.events.EventPostMotion;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Scaffold
extends Mod {
    private BlockData blockData;
    private timeHelper time = new timeHelper();
    private timeHelper delay = new timeHelper();
    private timeHelper timer2 = new timeHelper();
    private Value<Boolean> tower = new Value<Boolean>("Scaffold_Tower", true);
    private Value<Boolean> movetower = new Value<Boolean>("Scaffold_MoveTower", false);
    private Value<Boolean> noSwing = new Value<Boolean>("Scaffold_NoSwing", true);
    private double olddelay;
    private BlockPos blockpos;
    private EnumFacing facing;
    private List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table, Blocks.snow_layer, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.cactus, Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch, Blocks.iron_trapdoor, Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate, Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine, Blocks.double_plant, Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
    public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button, Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
    private boolean rotated = false;
    private boolean should = false;
    int slot;
    public ItemStack currentlyHolding;
    static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];

    public Scaffold() {
        super("Scaffold", Category.WORLD);
        this.currentlyHolding = null;
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        double x = Minecraft.thePlayer.posX;
        double y = Minecraft.thePlayer.posY - 1.0;
        double z = Minecraft.thePlayer.posZ;
        BlockPos blockBelow = new BlockPos(x, y, z);
        if (Minecraft.thePlayer != null) {
            this.blockData = this.getBlockData(blockBelow, blacklistedBlocks);
            if (this.blockData == null) {
                this.blockData = this.getBlockData(blockBelow.offset(EnumFacing.DOWN), blacklistedBlocks);
            }
            if (this.mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air) {
                if (this.blockData != null) {
                    float[] rot = this.getRotationsBlock(BlockData.position, BlockData.face);
                    event.pitch = rot[1];
                    event.yaw = rot[0];
                }
                if (this.tower.getValueState().booleanValue()) {
                    if (this.movetower.getValueState().booleanValue()) {
                        if (this.mc.gameSettings.keyBindJump.pressed) {
                            if (this.isMoving2()) {
                                if (this.isOnGround(0.76) && !this.isOnGround(0.75) && Minecraft.thePlayer.motionY > 0.23 && Minecraft.thePlayer.motionY < 0.25) {
                                    Minecraft.thePlayer.motionY = (double)Math.round(Minecraft.thePlayer.posY) - Minecraft.thePlayer.posY;
                                }
                                if (this.isOnGround(1.0E-4)) {
                                    Minecraft.thePlayer.motionY = 0.42;
                                    Minecraft.thePlayer.motionX *= 0.9;
                                    Minecraft.thePlayer.motionZ *= 0.9;
                                } else if (Minecraft.thePlayer.posY >= (double)Math.round(Minecraft.thePlayer.posY) - 1.0E-4 && Minecraft.thePlayer.posY <= (double)Math.round(Minecraft.thePlayer.posY) + 1.0E-4) {
                                    Minecraft.thePlayer.motionY = 0.0;
                                }
                            } else {
                                Minecraft.thePlayer.motionX = 0.0;
                                Minecraft.thePlayer.motionZ = 0.0;
                                Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                                blockBelow = new BlockPos(x, y, z);
                                if (this.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                                    Minecraft.thePlayer.motionY = 0.4196;
                                    Minecraft.thePlayer.motionX *= 0.75;
                                    Minecraft.thePlayer.motionZ *= 0.75;
                                }
                            }
                        }
                    } else if (!this.isMoving2() && this.mc.gameSettings.keyBindJump.pressed) {
                        Minecraft.thePlayer.motionX = 0.0;
                        Minecraft.thePlayer.motionZ = 0.0;
                        Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                        blockBelow = new BlockPos(x, y, z);
                        if (this.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                            Minecraft.thePlayer.motionY = 0.4196;
                            Minecraft.thePlayer.motionX *= 0.75;
                            Minecraft.thePlayer.motionZ *= 0.75;
                        }
                    }
                }
            }
        }
    }

    public boolean isOnGround(double height) {
        if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, - height, 0.0)).isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isMoving2() {
        return Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f;
    }

    public float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - Minecraft.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - Minecraft.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    @EventTarget
    public void onSafe(EventPostMotion event) {
        int i;
        for (i = 36; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest") || this.blockData == null) continue;
            int currentItem = Minecraft.thePlayer.inventory.currentItem;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
            Minecraft.thePlayer.inventory.currentItem = i - 36;
            Minecraft.playerController.updateController();
            try {
                this.currentlyHolding = this.mc.thePlayer.inventory.getStackInSlot(i - 36);
                Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, this.mc.theWorld, Minecraft.thePlayer.getHeldItem(), BlockData.position, BlockData.face, new Vec3(BlockData.access$2(this.blockData)).addVector(0.5, 0.5, 0.5).add(new Vec3(BlockData.access$3(this.blockData).getDirectionVec()).scale(0.5)));
                if (this.noSwing.getValueState().booleanValue()) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                } else {
                    Minecraft.thePlayer.swingItem();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            Minecraft.thePlayer.inventory.currentItem = currentItem;
            Minecraft.playerController.updateController();
            return;
        }
        if (this.invCheck()) {
            for (i = 9; i < 36; ++i) {
                Item item;
                if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest")) continue;
                this.swap(i, 7);
                break;
            }
        }
    }

    public static float randomFloat(long seed) {
        seed = System.currentTimeMillis() + seed;
        return 0.3f + (float)new Random(seed).nextInt(70000000) / 1.0E8f + 1.458745E-8f;
    }

    protected void swap(int slot, int hotbarNum) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer);
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            Item item;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            return false;
        }
        return true;
    }

    private double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        if (heldStack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
        }
        return false;
    }

    private void setBlockAndFacing(BlockPos bp) {
        if (this.mc.theWorld.getBlockState(bp.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, -1, 0);
            this.facing = EnumFacing.UP;
        } else if (this.mc.theWorld.getBlockState(bp.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(-1, 0, 0);
            this.facing = EnumFacing.EAST;
        } else if (this.mc.theWorld.getBlockState(bp.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(1, 0, 0);
            this.facing = EnumFacing.WEST;
        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, -1);
            this.facing = EnumFacing.SOUTH;
        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, 1);
            this.facing = EnumFacing.NORTH;
        } else {
            bp = null;
            this.facing = null;
        }
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock) || itemStack.stackSize <= 0 || this.blacklisted.stream().anyMatch(e -> e.equals(((ItemBlock)itemStack.getItem()).getBlock()))) continue;
            return i - 36;
        }
        return -1;
    }

    private BlockData getBlockData(BlockPos pos, List list) {
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), Keyboard.isKeyDown((int)42) && Minecraft.thePlayer.onGround && Minecraft.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), Keyboard.isKeyDown((int)42) && Minecraft.thePlayer.onGround && Minecraft.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), Keyboard.isKeyDown((int)42) && Minecraft.thePlayer.onGround && Minecraft.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), Keyboard.isKeyDown((int)42) && Minecraft.thePlayer.onGround && Minecraft.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.NORTH, this.blockData);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        return null;
    }

    public boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable() && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }

    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.NORTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5);
        }
        if (face == EnumFacing.EAST) {
            return new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ());
        }
        if (face == EnumFacing.SOUTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5);
        }
        if (face == EnumFacing.WEST) {
            return new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ());
        }
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timer2.reset();
        this.currentlyHolding = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }

    static {
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }

    public class timeHelper {
        private long prevMS = 0L;

        public boolean delay(float milliSec) {
            return (float)(this.getTime() - this.prevMS) >= milliSec;
        }

        public void reset() {
            this.prevMS = this.getTime();
        }

        public long getTime() {
            return System.nanoTime() / 1000000L;
        }

        public long getDifference() {
            return this.getTime() - this.prevMS;
        }

        public void setDifference(long difference) {
            this.prevMS = this.getTime() - difference;
        }
    }

    private static class BlockData {
        public static BlockPos position;
        public static EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
            BlockData.position = position;
            BlockData.face = face;
        }

        private BlockPos getPosition() {
            return position;
        }

        private EnumFacing getFacing() {
            return face;
        }

        static BlockPos access$0(BlockData var0) {
            return var0.getPosition();
        }

        static EnumFacing access$1(BlockData var0) {
            return var0.getFacing();
        }

        static BlockPos access$2(BlockData var0) {
            return position;
        }

        static EnumFacing access$3(BlockData var0) {
            return face;
        }
    }

}

