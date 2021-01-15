package net.minecraft.world.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DemoWorldManager extends ItemInWorldManager
{
    private boolean field_73105_c;
    private boolean demoTimeExpired;
    private int field_73104_e;
    private int field_73102_f;
    private static final String __OBFID = "CL_00001429";

    public DemoWorldManager(World worldIn)
    {
        super(worldIn);
    }

    public void updateBlockRemoving()
    {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        long var1 = this.theWorld.getTotalWorldTime();
        long var3 = var1 / 24000L + 1L;

        if (!this.field_73105_c && this.field_73102_f > 20)
        {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0F));
        }

        this.demoTimeExpired = var1 > 120500L;

        if (this.demoTimeExpired)
        {
            ++this.field_73104_e;
        }

        if (var1 % 24000L == 500L)
        {
            if (var3 <= 6L)
            {
                this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + var3, new Object[0]));
            }
        }
        else if (var3 == 1L)
        {
            if (var1 == 100L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0F));
            }
            else if (var1 == 175L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0F));
            }
            else if (var1 == 250L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0F));
            }
        }
        else if (var3 == 5L && var1 % 24000L == 22000L)
        {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
        }
    }

    /**
     * Sends a message to the player reminding them that this is the demo version
     */
    private void sendDemoReminder()
    {
        if (this.field_73104_e > 100)
        {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
            this.field_73104_e = 0;
        }
    }

    public void func_180784_a(BlockPos p_180784_1_, EnumFacing p_180784_2_)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
        }
        else
        {
            super.func_180784_a(p_180784_1_, p_180784_2_);
        }
    }

    public void func_180785_a(BlockPos p_180785_1_)
    {
        if (!this.demoTimeExpired)
        {
            super.func_180785_a(p_180785_1_);
        }
    }

    public boolean func_180237_b(BlockPos p_180237_1_)
    {
        return this.demoTimeExpired ? false : super.func_180237_b(p_180237_1_);
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer p_73085_1_, World worldIn, ItemStack p_73085_3_)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return false;
        }
        else
        {
            return super.tryUseItem(p_73085_1_, worldIn, p_73085_3_);
        }
    }

    public boolean func_180236_a(EntityPlayer p_180236_1_, World worldIn, ItemStack p_180236_3_, BlockPos p_180236_4_, EnumFacing p_180236_5_, float p_180236_6_, float p_180236_7_, float p_180236_8_)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return false;
        }
        else
        {
            return super.func_180236_a(p_180236_1_, worldIn, p_180236_3_, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
        }
    }
}
