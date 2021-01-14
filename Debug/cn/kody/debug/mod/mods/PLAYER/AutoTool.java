/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.mod.mods.PLAYER;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class AutoTool
extends Mod {
    public AutoTool() {
        super("AutoTool", "Auto Tool", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.mc.gameSettings.keyBindAttack.pressed) {
            return;
        }
        if (this.mc.objectMouseOver == null) {
            return;
        }
        BlockPos pos = this.mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        this.updateTool(pos);
    }

    public void updateTool(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.getStrVsBlock(block) <= strength) continue;
            strength = itemStack.getStrVsBlock(block);
            bestItemIndex = i;
        }
        if (bestItemIndex != -1) {
            Minecraft.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }
}

