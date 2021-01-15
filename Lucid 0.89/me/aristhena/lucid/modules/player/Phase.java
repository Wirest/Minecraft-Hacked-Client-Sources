/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovementInput
 *  net.minecraft.world.World
 */
package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.BoundingBoxEvent;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;

@Mod
public class Phase
extends Module {
    @Op
    private boolean vanilla;
    @Op
    private boolean spider;
    private Timer timer = new Timer();
    private int resetNext;
    
    @EventTarget
    private void onPreUpdate(UpdateEvent event) {
        if ((this.mc.thePlayer.isCollidedHorizontally) && (!this.mc.thePlayer.isOnLadder()))
        {
          float direction = this.mc.thePlayer.rotationYaw;
          double x = Math.cos((direction + 90) * Math.PI / 180) * 0.2D;
          double z = Math.sin((direction + 90) * Math.PI / 180) * 0.2D;
          this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
          for (int i = 1; i < 20; i++) {
        	  this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, 8.988465674311579E307D, this.mc.thePlayer.posZ, false));
          }
          this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
          return;
        }
    }

    @EventTarget
    private void onSetBB(BoundingBoxEvent event) {
        if (this.spider && this.isInsideBlock() && this.mc.gameSettings.keyBindJump.pressed || !this.isInsideBlock() && event.boundingBox != null && event.boundingBox.maxY > this.mc.thePlayer.boundingBox.minY && this.vanilla && this.mc.thePlayer.isSneaking()) {
            event.boundingBox = null;
        }
    }

    private boolean isInsideBlock() {
        int x = MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.minX);
        while (x < MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.maxX) + 1) {
            int y = MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.minY);
            while (y < MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.maxY) + 1) {
                int z = MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.minZ);
                while (z < MathHelper.floor_double((double)this.mc.thePlayer.boundingBox.maxZ) + 1) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox((World)this.mc.theWorld, new BlockPos(x, y, z), this.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
                        }
                        if (boundingBox != null && this.mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }
}

