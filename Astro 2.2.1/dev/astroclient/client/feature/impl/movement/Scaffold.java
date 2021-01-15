package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.Timer;
import dev.astroclient.client.util.math.MathUtil;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.util.Arrays;
import java.util.List;

@Toggleable(label = "Scaffold", category = Category.MOVEMENT)
public class Scaffold extends ToggleableFeature {

    public NumberProperty<Integer> placeDelay = new NumberProperty<>("Place Delay", true, 50, 1, 0, 500, Type.MILLISECONDS);
    public BooleanProperty swing = new BooleanProperty("Swing", true, false);

    public BooleanProperty safeWalk = new BooleanProperty("Safe Walk", true, true);
    public BooleanProperty tower = new BooleanProperty("Tower", true, true);

    private int facing = MathUtil.getRandomInRange(2, 4);

    private double height;

    private int currentHeldItem;

    private Timer timer, itemTimer;

    private float keepYaw, keepPitch;

    public boolean placing, downwards;

    private static List<Block> invalidBlocks = Arrays.asList(
            Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser,
            Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand,
            Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
            Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2,
            Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars
    );

    public Scaffold() {
        timer = new Timer();
        itemTimer = new Timer();
    }

    public void onDisable() {
        mc.thePlayer.inventory.currentItem = currentHeldItem;
    }


    public void onEnable() {
        placing = false;
        currentHeldItem = mc.thePlayer.inventory.currentItem;
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        BlockData blockData = find(new Vec3(0, mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown() ? -1 : 0, 0));

        if (eventMotion.getEventType().equals(EventType.PRE)) {
            if (tower.getValue()) {
                if (getBlockSlot() != -1 && mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isMoving()) {
                    mc.thePlayer.motionZ = 0;
                    mc.thePlayer.motionX = 0;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        if (timer.hasReached(1500)) {
                            mc.thePlayer.motionY = -0.28;
                            timer.reset();
                        }
                    } else if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && blockData != null)
                        mc.thePlayer.motionY = .41955;
                }
            }
        } else {
            if (getBlockSlot() == -1)
                if (itemTimer.hasReached(150)) {
                    getBlocksFromInventory();
                    itemTimer.reset();
                }
        }

        if (mc.gameSettings.keyBindSneak.pressed) {
            if (!downwards) {
                mc.thePlayer.setSneaking(false);
                downwards = true;
            }
        } else
            downwards = false;

        if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && blockData != null) {
            placing = true;
            if (getBlockSlot() != -1) {
                switch (eventMotion.getEventType()) {
                    case PRE:
                        BlockPos sideBlock = blockData.position;
                        eventMotion.setYaw(keepYaw = getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[0] - 180);
                        eventMotion.setPitch(keepPitch = getBlockRotations(sideBlock.getX(), sideBlock.getY(), sideBlock.getZ(), blockData.face)[1] + 12);
                        break;
                    case POST:
                        eventMotion.setYaw(keepYaw);
                        eventMotion.setPitch(keepPitch);
                        mc.thePlayer.inventory.currentItem = getBlockSlot();
                        double hitvecx = (blockData.position.getX() + height) + MathUtil.getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetX() / facing);
                        double hitvecy = (blockData.position.getY() + height) + MathUtil.getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetY() / facing);
                        double hitvecz = (blockData.position.getZ() + height) + MathUtil.getRandomInRange(-.08,.29) + (blockData.face.getFrontOffsetZ() / facing);
                        Vec3 vec = new Vec3(hitvecx , hitvecy , hitvecz );
                        if (timer.hasReached(placeDelay.getValue())) {
                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockData.position, blockData.face, vec);
                            if (swing.getValue())
                                mc.thePlayer.swingItem();
                            else
                                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
                            timer.reset();
                        }
                        mc.thePlayer.inventory.currentItem = currentHeldItem;
                        break;
                }
            } else {
                eventMotion.setYaw(keepYaw);
                eventMotion.setPitch(keepPitch);
            }
        } else {
            placing = false;
            eventMotion.setYaw(keepYaw);
            eventMotion.setPitch(keepPitch);
        }
    }

    private void getBlocksFromInventory() {
        if (mc.currentScreen instanceof GuiChest)
            return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isValid(stack.getItem())) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                continue;
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isValid(stack.getItem()))
                return i - 36;
        }
        return -1;
    }

    public static boolean isValid(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        } else {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.getBlock();
            return !invalidBlocks.contains(block);
        }
    }

    public BlockData find(Vec3 offset3) {

        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(new Vec3(x, y, z).add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockData(offset, invert[facing.ordinal()]);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new BlockData(offset2, invert[facing.ordinal()]);
            }
        }
        return null;
    }

    private float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
        Entity temp = new EntitySnowball(mc.theWorld);
        temp.posX = (x + 0.5);
        temp.posY = (y + (height = 0.5));
        temp.posZ = (z + 0.5);
        return mc.thePlayer.canEntityBeSeen(temp) ? getAngles(temp) : getRotationToBlock(new BlockPos(x, y, z), facing);
    }

    private float[] getAngles(Entity e) {
        return new float[]{getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
    }

    private float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double yawToEntity;
        final double v = Math.toDegrees(Math.atan(deltaZ / deltaX));
        if ((deltaZ < 0) && (deltaX < 0)) {
            yawToEntity = 90 + v;
        } else {
            if ((deltaZ < 0) && (deltaX > 0.0D)) {
                yawToEntity = -90 + v;
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }

    private float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.thePlayer.posX;
        double deltaZ = entity.posZ - mc.thePlayer.posZ;
        double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4 - mc.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    public float[] getRotationToBlock(BlockPos pos, EnumFacing face) {
        double random = MathUtil.getRandomInRange(.45, .55);
        int ranface = MathUtil.getRandomInRange(2, 4);
        double xDiff = pos.getX() + (height = random) - mc.thePlayer.posX + face.getDirectionVec().getX() / (facing = ranface);
        double zDiff = pos.getZ() + (height = random) - mc.thePlayer.posZ + face.getDirectionVec().getZ() / (facing = ranface);
        double yDiff = pos.getY() - mc.thePlayer.posY - 1;
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) -Math.toDegrees(Math.atan2(xDiff, zDiff));
        float pitch = (float) -Math.toDegrees(Math.atan(yDiff / distance));

        return new float[]{Math.abs(yaw - mc.thePlayer.rotationYaw) < .1 ? mc.thePlayer.rotationYaw : yaw, Math.abs(pitch - mc.thePlayer.rotationPitch) < .1 ? mc.thePlayer.rotationPitch : pitch};
    }

    public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5, (double) position.getZ() + 0.5);
        return point.add(offset);
    }

    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).isVecInside(point))
                continue;
            return true;
        }
        return false;
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}
