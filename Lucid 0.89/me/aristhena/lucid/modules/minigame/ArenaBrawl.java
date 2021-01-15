package me.aristhena.lucid.modules.minigame;

import me.aristhena.lucid.management.module.*;
import me.aristhena.lucid.management.option.*;
import me.aristhena.lucid.util.*;
import net.minecraft.tileentity.*;
import java.util.*;
import me.aristhena.lucid.eventapi.events.*;
import me.aristhena.lucid.eventapi.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;

@Mod
public class ArenaBrawl extends Module
{
    @Op
    private boolean nuker;
    private BlockPos keyPos;
    private BlockPos healPos;
    private BlockPos damagePos;
    private int keyRenderTime;
    private int healRenderTime;
    private int damageRenderTime;
    
    @EventTarget
    private void onPacketRecieve(final NametagRenderEvent event) {
        if (event.string.contains("MAGICAL KEY")) {
            this.keyRenderTime = 5;
            this.keyPos = event.entity.getPosition();
        }
        else if (event.string.contains("HEALING")) {
            this.healRenderTime = 5;
            this.healPos = event.entity.getPosition();
        }
        else if (event.string.contains("DOUBLE DAMAGE")) {
            this.damageRenderTime = 5;
            this.damagePos = event.entity.getPosition();
        }
    }
    
    @EventTarget(0)
    private void onRender3D(final Render3DEvent event) {
        if (this.keyRenderTime > 0) {
            RenderUtils.drawBeacon(this.keyPos, -3014659, -1932394499, event.partialTicks);
            --this.keyRenderTime;
        }
        if (this.healRenderTime > 0) {
            RenderUtils.drawBeacon(this.healPos, -16724992, -1946104832, event.partialTicks);
            --this.healRenderTime;
        }
        if (this.damageRenderTime > 0) {
            RenderUtils.drawBeacon(this.damagePos, -5046272, -1934426112, event.partialTicks);
            --this.damageRenderTime;
        }
        for (final Object o : this.mc.theWorld.loadedTileEntityList) {
        	final TileEntity entity = (TileEntity)o;
            if (entity instanceof TileEntityBrewingStand) {
                RenderUtils.drawBeacon(entity.getPos(), -16764160, -1946144000, event.partialTicks);
            }
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.state == Event.State.POST && this.nuker) {
            int y;
            for (int radius = y = 5; y >= 0; --y) {
                for (int x = -radius; x < radius; ++x) {
                    for (int z = -radius; z < radius; ++z) {
                        final double posX = (int)Math.floor(this.mc.thePlayer.posX) + x;
                        final double posY = (int)Math.floor(this.mc.thePlayer.posY) + y;
                        final double posZ = (int)Math.floor(this.mc.thePlayer.posZ) + z;
                        final BlockPos pos = new BlockPos(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY + y, this.mc.thePlayer.posZ + z);
                        if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLog || this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockMushroom) {
                            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.WEST));
                            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.WEST));
                            break;
                        }
                    }
                }
            }
            for (final Object o : this.mc.theWorld.loadedTileEntityList) {
            	final TileEntity entity = (TileEntity)o;
                if (entity instanceof TileEntityBrewingStand && this.mc.thePlayer.getDistance((double)entity.getPos().getX(), (double)entity.getPos().getY(), (double)entity.getPos().getZ()) < 5.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, entity.getPos(), EnumFacing.WEST));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, entity.getPos(), EnumFacing.WEST));
                }
            }
        }
    }
}
