/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown
extends Mod {
    public static Value<String> mode = new Value("NoSlow", "Mode", 0);
    public static Value<Boolean> auraSlowdown = new Value<Boolean>("NoSlow_AuraSlowdown", true);
    private static Value<Double> slow = new Value<Double>("NoSlow_SlowDown", 0.5, 0.1, 1.0, 0.05);

    public NoSlowdown() {
        super("NoSlow", Mod.Category.MOVEMENT, Colors.DARKRED.c);
        mode.addValue("AAC");
        mode.addValue("NCP");
        mode.addValue("Custom");
    }

    @EventTarget
    public void onPre(EventPreMotion pre) {
        this.setColor(-16765390);
        if (this.mc.thePlayer.isBlocking() && mode.isCurrentMode("NCP")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventTarget
    public void onPost(EventPostMotion post) {
        if (this.mc.thePlayer.isBlocking() && mode.isCurrentMode("NCP")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
        }
    }

    public static double getSlowness() {
        if (mode.isCurrentMode("AAC")) {
            return 0.75;
        }
        if (mode.isCurrentMode("NCP")) {
            return 1.0;
        }
        if (mode.isCurrentMode("Custom")) {
            return slow.getValueState();
        }
        return 1.0;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoSlowdown Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoSlowdown Enable", ClientNotification.Type.SUCCESS);
    }
}

