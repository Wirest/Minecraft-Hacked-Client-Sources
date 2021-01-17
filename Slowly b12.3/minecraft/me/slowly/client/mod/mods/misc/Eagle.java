/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Eagle
extends Mod {
    private Value<String> mode = new Value("Eagle", "Mode", 0);

    public Eagle() {
        super("Eagle", Mod.Category.MISCELLANEOUS, Colors.DARKGREEN.c);
        this.mode.mode.add("Legit");
        this.mode.mode.add("Unlegit");
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Eagle Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Eagle Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.mode.isCurrentMode("Legit")) {
            ItemStack item = this.mc.thePlayer.getCurrentEquippedItem();
            if (item.getItem() instanceof ItemBlock) {
                this.mc.gameSettings.keyBindSneak.pressed = false;
                BlockPos BlockPos2 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(BlockPos2).getBlock() == Blocks.air) {
                    this.mc.gameSettings.keyBindSneak.pressed = true;
                }
            }
        } else {
            ItemStack item = this.mc.thePlayer.getCurrentEquippedItem();
            if (item.getItem() instanceof ItemBlock) {
                this.mc.gameSettings.keyBindSneak.pressed = false;
                BlockPos BlockPos3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(BlockPos3).getBlock() == Blocks.air) {
                    this.mc.rightClickDelayTimer = 0;
                    this.mc.gameSettings.keyBindSneak.pressed = true;
                } else if (this.mc.theWorld.getBlockState(BlockPos3).getBlock() == Blocks.air && this.mc.thePlayer.onGround) {
                    this.mc.rightClickDelayTimer = 4;
                }
            }
        }
    }
}

