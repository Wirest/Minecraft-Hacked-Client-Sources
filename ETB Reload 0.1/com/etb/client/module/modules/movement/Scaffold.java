package com.etb.client.module.modules.movement;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.player.SafewalkEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.font.Fonts;
import com.etb.client.utils.value.impl.BooleanValue;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
    private List<Block> invalid;
    private TimerUtil timerMotion = new TimerUtil();
    private BlockData blockData;
    private BooleanValue Switch = new BooleanValue("Switch", true);
    private BooleanValue Hypixel = new BooleanValue("Hypixel", true);
    private BooleanValue tower = new BooleanValue("Tower", true);
    private BooleanValue keepy = new BooleanValue("KeepY", true);

    private int NoigaY;

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, new Color(255, 155, 255, 255).getRGB());
        addValues(Hypixel, Switch, tower, keepy);
        setDescription("Auto places blocks under you");
        invalid = Arrays.asList(Blocks.anvil, Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.gravel);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(Hypixel.isEnabled() ? "Hypixel" : "Normal");
        if (keepy.isEnabled()) {
            if ((!mc.thePlayer.isMoving() && mc.gameSettings.keyBindJump.isKeyDown()) || (mc.thePlayer.isCollidedVertically || mc.thePlayer.onGround)) {
                NoigaY = MathHelper.floor_double(mc.thePlayer.posY);
            }
        } else {
            NoigaY = MathHelper.floor_double(mc.thePlayer.posY);
        }
        if (event.isPre()) {
            blockData = null;
            event.setPitch(70.0f);
            if (!mc.thePlayer.isSneaking()) {
                final BlockPos blockBelow = new BlockPos(mc.thePlayer.posX, NoigaY - 1.0, mc.thePlayer.posZ);
                if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) {
                    blockData = getBlockData(blockBelow);
                    if (blockData != null) {
                        event.setYaw(aimAtLocation(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ(), blockData.face)[0]);
                    }
                }
            }
        } else {
            if (blockData != null) {
                if (getBlockCountHotbar() <= 0 || (!Switch.isEnabled() && mc.thePlayer.getCurrentEquippedItem() != null && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock))) {
                    return;
                }
                final int heldItem = mc.thePlayer.inventory.currentItem;
                if (Switch.isEnabled()) {
                    for (int i = 0; i < 9; ++i) {
                        if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0 && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                            break;
                        }
                    }
                }
                if (tower.isEnabled()) {
                    if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && (Minecraft.getMinecraft().thePlayer.moveForward == 0.0f && Minecraft.getMinecraft().thePlayer.moveStrafing == 0.0f) && tower.isEnabled()) {
                        Minecraft.getMinecraft().thePlayer.motionY = 0.42F;
                        Minecraft.getMinecraft().thePlayer.motionX = Minecraft.getMinecraft().thePlayer.motionZ = 0;
                        if (timerMotion.sleep(1500)) {
                            Minecraft.getMinecraft().thePlayer.motionY = -0.28;
                        }
                    } else {
                        timerMotion.reset();
                    }
                }
                if (Hypixel.getValue()) {
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockData.position, blockData.face, new Vec3(blockData.position.getX() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, blockData.position.getY() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, blockData.position.getZ() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9))) {
                    }
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                } else if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockData.position, blockData.face, new Vec3(blockData.position.getX() + Math.random(), blockData.position.getY() + Math.random(), blockData.position.getZ() + Math.random()))) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = heldItem));
            }
        }
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || invalid.contains(((ItemBlock) item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
    private int getBlockCountHotbar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || invalid.contains(((ItemBlock) item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
    @Subscribe
    public void onRender2D(Render2DEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);
        mc.fontRendererObj.drawStringWithShadow(Integer.toString(getBlockCount()), sr.getScaledWidth() / 2 + 1 - mc.fontRendererObj.getStringWidth(Integer.toString(getBlockCount())) / 2, sr.getScaledHeight() / 2 + 12, getBlockColor(getBlockCount()));
    }

    @Subscribe
    public void onSafewalk(SafewalkEvent event) {
        event.setCanceled(keepy.isEnabled() ? ((mc.gameSettings.keyBindJump.isKeyDown() || !mc.thePlayer.onGround) ? false : true) : true);
    }

    private BlockData getBlockData(BlockPos pos) {
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockData blockData = null;
        return blockData;
    }

    private int getBlockColor(int count) {
        float f = count;
        float f1 = 64;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }

    private float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
        EntitySnowball temp = new EntitySnowball(mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y - 2.7035252353;
        temp.posZ = z + 0.5;
        EntitySnowball entitySnowball = temp;
        entitySnowball.posX += facing.getDirectionVec().getX() * 0.25;
        EntitySnowball entitySnowball2 = temp;
        entitySnowball2.posY += facing.getDirectionVec().getY() * 0.25;
        EntitySnowball entitySnowball3 = temp;
        entitySnowball3.posZ += facing.getDirectionVec().getZ() * 0.25;
        return aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }

    private float[] aimAtLocation(double positionX, double positionY, double positionZ) {
        double x = positionX - mc.thePlayer.posX;
        double y = positionY - mc.thePlayer.posY;
        double z = positionZ - mc.thePlayer.posZ;
        double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[]{(float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, (float) (-(Math.atan2(y, distance) * 180.0 / 3.141592653589793))};
    }

    @Override
    public void onEnable() {
        if (mc.theWorld != null) {
            NoigaY = MathHelper.floor_double(mc.thePlayer.posY);
        }
    }

    private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}