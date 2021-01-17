package me.slowly.client.mod.mods.movement;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class Tower
extends Mod {
    private BlockData blockData;
    private TimeHelper timer = new TimeHelper();
    private Value<Double> delayValue = new Value<Double>("Tower_Delay", 50.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> silent = new Value<Boolean>("Tower_Silent", false);
    private Value<String> mode = new Value("Tower", "Mode", 0);
    private double count = 0.0;

    public Tower() {
        super("Tower", Mod.Category.WORLD, Colors.YELLOW.c);
        this.mode.addValue("AAC");
        this.mode.addValue("NCP");
        this.showValue = this.mode;
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.showValue = this.mode;
        if (this.mc.thePlayer == null) {
            return;
        }
        double blockBelow = -0.75;
        this.blockData = this.getBlockData(new BlockPos(this.mc.thePlayer).add(0.0, blockBelow, 0.0), 1);
        int block = this.getBlockItem();
        Random rand = new Random();
        Item item = this.mc.thePlayer.inventory.getStackInSlot(block).getItem();
        Vec3 hitVec = new Vec3(this.blockData.position).addVector(0.5, 0.5, 0.5).add(new Vec3(this.blockData.face.getDirectionVec()).scale(0.5));
		if (block != -1 && item != null && item instanceof ItemBlock) {
			if (this.silent.getValueState().booleanValue() || (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(block));
        }
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            event.pitch = 90.0f;
            if (this.mode.isCurrentMode("AAC")) {
                this.aac();
            } else if (this.mode.isCurrentMode("NCP")) {
                this.ncp();
            }
            double countMax = 0.02;
            this.count += 7.5E-5;
            if (this.count > countMax) {
                this.count = countMax;
            }
            if (this.mc.thePlayer.motionY < 0.0 && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + blockBelow, this.mc.thePlayer.posZ)).getBlock().getMaterial() == Material.air && !this.mc.thePlayer.onGround && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3(90.0, 90.0, 90.0))) {
                this.timer.reset();
                this.mc.thePlayer.swingItem();
            }
        }
        }
    }

    private int getBlockItem() {
        int block = -1;
        int i = 8;
        while (i >= 0) {
            if (this.mc.thePlayer.inventory.getStackInSlot(i) != null && this.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && (this.mc.thePlayer.getHeldItem() == this.mc.thePlayer.inventory.getStackInSlot(i) || this.silent.getValueState().booleanValue())) {
                block = i;
            }
            --i;
        }
        return block;
    }
	private void aac() {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
            this.mc.thePlayer.motionY = 0.395;
        }
        this.mc.thePlayer.motionY -= 0.002300000051036477;
    }

    private void ncp() {
        double blockBelow = -2.0;
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = 0.41999998688697815;
        }
        if (this.mc.thePlayer.motionY < 0.1 && !(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ).add(0.0, blockBelow, 0.0)).getBlock() instanceof BlockAir)) {
            this.mc.thePlayer.motionY = -10.0;
        }
    }

    public BlockData getBlockData(BlockPos pos, int i) {
        return this.mc.theWorld.getBlockState(pos.add(0, 0, i)).getBlock() != Blocks.air ? new BlockData(pos.add(0, 0, i), EnumFacing.NORTH) : (this.mc.theWorld.getBlockState(pos.add(0, 0, - i)).getBlock() != Blocks.air ? new BlockData(pos.add(0, 0, - i), EnumFacing.SOUTH) : (this.mc.theWorld.getBlockState(pos.add(i, 0, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(i, 0, 0), EnumFacing.WEST) : (this.mc.theWorld.getBlockState(pos.add(- i, 0, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(- i, 0, 0), EnumFacing.EAST) : (this.mc.theWorld.getBlockState(pos.add(0, - i, 0)).getBlock() != Blocks.air ? new BlockData(pos.add(0, - i, 0), EnumFacing.UP) : null))));
    }
    private void sendCurrentItem() {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
    }
    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        if (heldStack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
        }
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.timer.timerSpeed = 1.0f;
        this.count = 0.0;
        this.sendCurrentItem();
        this.mc.gameSettings.keyBindSneak.pressed = false;
        ClientUtil.sendClientMessage("Tower Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Tower Enable", ClientNotification.Type.SUCCESS);
    }


    public class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

}

