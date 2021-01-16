/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

@Module.Mod(category=Module.Category.AUTO, description="Switches to the best tool for a block", key=0, name="AutoTool")
public class AutoTool
extends Module {
    public static Value sword = new Value("autotool_Sword", true);

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        boolean overBlock;
        boolean bl = overBlock = Wrapper.mc().objectMouseOver != null && Wrapper.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && Wrapper.getSettings().keyBindAttack.pressed;
        if (overBlock) {
            int slot;
            Wrapper.getPlayer().inventory.currentItem = slot = AutoTool.getBestTool(Wrapper.getBlock(Wrapper.mc().objectMouseOver.getBlockPos()));
        }
    }

    @EventTarget
    public void onPacket(EventPacketSent event) {
        CPacketUseEntity goddamnpacket;
        if (event.getPacket() instanceof CPacketUseEntity && (goddamnpacket = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && AutoTool.getTheFuckingSwordYouDirtyCunt() != -1 && AutoTool.getTheFuckingSwordYouDirtyCunt() != Wrapper.getPlayer().inventory.currentItem) {
            Wrapper.getPlayer().inventory.currentItem = AutoTool.getTheFuckingSwordYouDirtyCunt();
        }
    }

    public static int getTheFuckingSwordYouDirtyCunt() {
        float best = -1.0f;
        int index = -1;
        int i = 0;
        while (i < 9) {
            ItemStack itemStack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                ItemSword sword = (ItemSword)itemStack.getItem();
                float damage = sword.attackDamage + (float)EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack) * 1.25f;
                if (damage > best) {
                    best = damage;
                    index = i;
                }
            }
            ++i;
        }
        return index;
    }

    public static int getBestTool(Block block) {
        float best = -1.0f;
        int slot = -1;
        int i = 0;
        while (i < 9) {
            float str;
            ItemStack itemStack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (itemStack != null && (str = itemStack.getItem().getStrVsBlock(itemStack, block.getDefaultState())) > best) {
                best = str;
                slot = i;
            }
            ++i;
        }
        if (slot == -1) {
            slot = 0;
        }
        return slot;
    }
}

