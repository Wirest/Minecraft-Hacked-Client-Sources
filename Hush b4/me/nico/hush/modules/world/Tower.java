// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.world;

import net.minecraft.client.network.NetHandlerPlayClient;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.init.Blocks;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.nico.hush.events.EventUpdate;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import me.nico.hush.modules.Module;

public class Tower extends Module
{
    public BlockPos blockPos;
    public EnumFacing facing;
    int slot;
    int i;
    
    public Tower() {
        super("Tower", "Tower", 14620696, 24, Category.WORLD);
        this.blockPos = null;
        this.facing = null;
        this.slot = 0;
        this.i = 0;
        final ArrayList<String> options = new ArrayList<String>();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer p = event.getPacket();
            C03PacketPlayer.pitch = 90.0f;
            event.setPacket(p);
        }
        final Minecraft mc = Tower.mc;
        if (Minecraft.thePlayer.inventory.getStackInSlot(this.i) != null) {
            final Minecraft mc2 = Tower.mc;
            if (Minecraft.thePlayer.inventory.getStackInSlot(this.i).getItem() instanceof ItemBlock) {
                this.slot = this.i;
            }
            else if (this.i < 8) {
                ++this.i;
            }
            else {
                this.i = 0;
            }
        }
        else if (this.i < 8) {
            ++this.i;
        }
        else {
            this.i = 0;
        }
        final Minecraft mc3 = Tower.mc;
        if (Minecraft.thePlayer.inventoryContainer.getSlot(this.slot).getHasStack()) {
            final Minecraft mc4 = Tower.mc;
            if (!(Minecraft.thePlayer.inventoryContainer.getSlot(this.slot).getStack().getItem() instanceof ItemBlock)) {
                if (this.i < 8) {
                    ++this.i;
                }
                else {
                    this.i = 0;
                }
            }
        }
        else if (this.i < 8) {
            ++this.i;
        }
        else {
            this.i = 0;
        }
        final Minecraft mc5 = Tower.mc;
        if (Minecraft.thePlayer.onGround) {
            final Minecraft mc6 = Tower.mc;
            Minecraft.thePlayer.jump();
        }
        else {
            this.placeBlock();
        }
    }
    
    public void placeBlock() {
        final float motionSpeed = 0.3f;
        final Minecraft mc = Tower.mc;
        if (Minecraft.thePlayer.isSneaking()) {
            final Minecraft mc2 = Tower.mc;
            Minecraft.thePlayer.motionY = motionSpeed;
            final Minecraft mc3 = Tower.mc;
            final double x = Minecraft.thePlayer.posX;
            final Minecraft mc4 = Tower.mc;
            final double y = Minecraft.thePlayer.posY - 1.0;
            final Minecraft mc5 = Tower.mc;
            final double posZ = Minecraft.thePlayer.posZ;
        }
        final Minecraft mc6 = Tower.mc;
        final double posX = Minecraft.thePlayer.posX;
        final Minecraft mc7 = Tower.mc;
        final double y2 = Minecraft.thePlayer.posY - 1.0;
        final Minecraft mc8 = Tower.mc;
        final BlockPos pos = new BlockPos(posX, y2, Minecraft.thePlayer.posZ);
        final Minecraft mc9 = Tower.mc;
        final ItemStack is = Minecraft.thePlayer.getCurrentEquippedItem();
        if (Tower.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
            this.setBlockData(pos);
            if (this.blockPos != null && this.facing != null) {
                final Minecraft mc10 = Tower.mc;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
                final PlayerControllerMP playerController = Tower.mc.playerController;
                final Minecraft mc11 = Tower.mc;
                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                final WorldClient theWorld = Tower.mc.theWorld;
                final Minecraft mc12 = Tower.mc;
                playerController.onPlayerRightClick(thePlayer, theWorld, Minecraft.thePlayer.inventoryContainer.getSlot(this.slot).getStack(), this.blockPos, this.facing, new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()));
            }
            else {
                final PlayerControllerMP playerController2 = Tower.mc.playerController;
                final Minecraft mc13 = Tower.mc;
                final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                final WorldClient theWorld2 = Tower.mc.theWorld;
                final Minecraft mc14 = Tower.mc;
                playerController2.onPlayerRightClick(thePlayer2, theWorld2, Minecraft.thePlayer.getHeldItem(), this.blockPos, this.facing, new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()));
            }
        }
    }
    
    public void setBlockData(final BlockPos block) {
        if (Tower.mc.theWorld.getBlockState(block.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.blockPos = block.add(0, -1, 0);
            this.facing = EnumFacing.UP;
        }
        if (Tower.mc.theWorld.getBlockState(block.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.blockPos = block.add(-1, 0, 0);
            this.facing = EnumFacing.EAST;
        }
        if (Tower.mc.theWorld.getBlockState(block.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.blockPos = block.add(1, 0, 0);
            this.facing = EnumFacing.WEST;
        }
        if (Tower.mc.theWorld.getBlockState(block.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.blockPos = block.add(0, 0, -1);
            this.facing = EnumFacing.SOUTH;
        }
        if (Tower.mc.theWorld.getBlockState(block.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.blockPos = block.add(0, 0, 1);
            this.facing = EnumFacing.NORTH;
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        Tower.mc.timer.timerSpeed = 1.0f;
        final Minecraft mc = Tower.mc;
        final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
        final Minecraft mc2 = Tower.mc;
        sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }
}
