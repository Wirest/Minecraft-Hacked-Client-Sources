/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C01PacketChatMessage
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.IChatComponent
 */
package delta.utils;

import delta.utils.BoundingBox;
import delta.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class PlayerUtils {
    public static void mineBlock(BoundingBox class107) {
        boolean n;
        if (Wrapper.mc.thePlayer == null) {
            return;
        }
        double d = class107._company((Entity)Wrapper.mc.thePlayer);
        if (d > 5.0) {
            return;
        }
        BoundingBox class1072 = new BoundingBox(Math.floor(Wrapper.mc.thePlayer.posX), Math.floor(Wrapper.mc.thePlayer.posY), Math.floor(Wrapper.mc.thePlayer.posZ));
        boolean n2 = n = class1072._maria() == Blocks.lava || class1072._maria() == Blocks.flowing_lava;
        if (!n) {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(0, (int)class107._talented(), (int)class107._adelaide(), (int)class107._produce(), 1));
            if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(2, (int)class107._talented(), (int)class107._adelaide(), (int)class107._produce(), 1));
            }
        } else {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(0, (int)class107._talented(), (int)class107._adelaide(), (int)class107._produce(), 1));
            Wrapper.mc.thePlayer.swingItem();
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(2, (int)class107._talented(), (int)class107._adelaide(), (int)class107._produce(), 1));
        }
    }

    public static void sendChatMessage(String string) {
        if (Wrapper.mc.thePlayer != null) {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C01PacketChatMessage(string));
        }
    }

    public static void addChatMessage(String string) {
        if (Wrapper.mc.thePlayer != null) {
            Wrapper.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText((Object)EnumChatFormatting.DARK_PURPLE + "D " + (Object)EnumChatFormatting.GRAY + "> " + string));
        }
    }

    public static void _stamps(BoundingBox class107) {
        if (Wrapper.mc.thePlayer == null) {
            return;
        }
        Block block = class107._maria();
        float f = 1.0f;
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Wrapper.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.func_150997_a(block) <= f) continue;
            f = itemStack.func_150997_a(block);
            n = i;
        }
        if (n != -1) {
            Wrapper.mc.thePlayer.inventory.currentItem = n;
        }
    }
}

