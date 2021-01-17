/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Random;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastConsume
extends Mod {
    private Value<String> mode = new Value("FastConsume", "Mode", 0);
    private boolean send;
    private TimeHelper timer = new TimeHelper();
    private Random random = new Random();

    public FastConsume() {
        super("FastConsume", Mod.Category.WORLD, Colors.RED.c);
        this.mode.mode.add("AAC");
        this.mode.mode.add("NCP");
        this.mode.mode.add("Vanilla");
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(-6697780);
        if (this.mc.thePlayer.isEating() && !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
            if (this.send && this.timer.isDelayComplete(1000 + this.random.nextInt(100))) {
                int i = 0;
                while (i < (this.mode.isCurrentMode("NCP") ? 17 : (this.mode.isCurrentMode("AAC") ? 6 : 50))) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    ++i;
                }
                this.send = false;
                this.timer.reset();
            }
        } else {
            this.send = true;
        }
    }

    @EventTarget
    public void onUpdate(EventPostMotion event) {
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("FastUse Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("FastUse Enable", ClientNotification.Type.SUCCESS);
    }
}

