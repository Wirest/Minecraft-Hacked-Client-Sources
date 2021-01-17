/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.mods.world.ChestAura;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class ChestStealer
extends Mod {
    private Value<Double> delay = new Value<Double>("ChestStealer_Delay", 30.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> insant = new Value<Boolean>("ChestStealer_Insant", false);
    TimeHelper time = new TimeHelper();

    public ChestStealer() {
        super("ChestStealer", Mod.Category.MISCELLANEOUS, Colors.YELLOW.c);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("ChestStealer Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("ChestStealer Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(-2572328);
        if (this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest c = (ContainerChest)this.mc.thePlayer.openContainer;
            boolean empty = true;
            int i = 0;
            while (i < c.getLowerChestInventory().getSizeInventory()) {
                if (c.getLowerChestInventory().getStackInSlot(i) != null) {
                    empty = false;
                    if (this.insant.getValueState().booleanValue() || this.time.isDelayComplete(this.delay.getValueState().intValue())) {
                        this.mc.playerController.windowClick(c.windowId, i, 0, 1, this.mc.thePlayer);
                        this.time.reset();
                    }
                }
                ++i;
            }
            if (empty && ChestAura.lastBlock == null) {
                this.mc.thePlayer.closeScreen();
            }
        }
    }
}

