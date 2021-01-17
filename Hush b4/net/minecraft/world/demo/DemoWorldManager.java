// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.demo;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.World;
import net.minecraft.server.management.ItemInWorldManager;

public class DemoWorldManager extends ItemInWorldManager
{
    private boolean field_73105_c;
    private boolean demoTimeExpired;
    private int field_73104_e;
    private int field_73102_f;
    
    public DemoWorldManager(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        final long i = this.theWorld.getTotalWorldTime();
        final long j = i / 24000L + 1L;
        if (!this.field_73105_c && this.field_73102_f > 20) {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0f));
        }
        this.demoTimeExpired = (i > 120500L);
        if (this.demoTimeExpired) {
            ++this.field_73104_e;
        }
        if (i % 24000L == 500L) {
            if (j <= 6L) {
                this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + j, new Object[0]));
            }
        }
        else if (j == 1L) {
            if (i == 100L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0f));
            }
            else if (i == 175L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0f));
            }
            else if (i == 250L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0f));
            }
        }
        else if (j == 5L && i % 24000L == 22000L) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
        }
    }
    
    private void sendDemoReminder() {
        if (this.field_73104_e > 100) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
            this.field_73104_e = 0;
        }
    }
    
    @Override
    public void onBlockClicked(final BlockPos pos, final EnumFacing side) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        }
        else {
            super.onBlockClicked(pos, side);
        }
    }
    
    @Override
    public void blockRemoving(final BlockPos pos) {
        if (!this.demoTimeExpired) {
            super.blockRemoving(pos);
        }
    }
    
    @Override
    public boolean tryHarvestBlock(final BlockPos pos) {
        return !this.demoTimeExpired && super.tryHarvestBlock(pos);
    }
    
    @Override
    public boolean tryUseItem(final EntityPlayer player, final World worldIn, final ItemStack stack) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.tryUseItem(player, worldIn, stack);
    }
    
    @Override
    public boolean activateBlockOrUseItem(final EntityPlayer player, final World worldIn, final ItemStack stack, final BlockPos pos, final EnumFacing side, final float offsetX, final float offsetY, final float offsetZ) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.activateBlockOrUseItem(player, worldIn, stack, pos, side, offsetX, offsetY, offsetZ);
    }
}
