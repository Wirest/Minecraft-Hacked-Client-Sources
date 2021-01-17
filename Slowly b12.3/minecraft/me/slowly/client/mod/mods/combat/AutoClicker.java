/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class AutoClicker
extends Mod {
    private Value<Double> delay = new Value<Double>("AutoClicker_Delay", 100.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> random = new Value<Boolean>("AutoClicker_Random", true);
    private Random rand = new Random();
    private long delayCount;

    public AutoClicker() {
        super("AutoClicker", Mod.Category.COMBAT, Colors.YELLOW.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int delayNeed = this.delay.getValueState().intValue();
        if (this.random.getValueState().booleanValue()) {
            delayNeed += this.rand.nextInt(80) - 40;
        }
        if (System.currentTimeMillis() - this.delayCount >= (long)delayNeed) {
            if (Mouse.isButtonDown((int)0) && this.mc.currentScreen == null) {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null) {
                    this.mc.thePlayer.swingItem();
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                } else {
                    this.mc.thePlayer.swingItem();
                }
                this.delayCount = System.currentTimeMillis();
            }
            if (Mouse.isButtonDown((int)1) && this.mc.currentScreen == null) {
                this.mc.rightClickMouse();
                this.delayCount = System.currentTimeMillis();
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoClicker Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AutoClicker Enable", ClientNotification.Type.SUCCESS);
    }
}

