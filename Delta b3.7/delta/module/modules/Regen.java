/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 */
package delta.module.modules;

import delta.utils.MovementUtils;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        if (this.mc.thePlayer.onGround) {
            if (!this.getSetting("WhileMoving").getCheckValue() && MovementUtils.isMoving()) {
                return;
            }
            if (this.mc.thePlayer.getHealth() < 20.0f) {
                for (int i = 0; i < (int)this.getSetting("Frequence").getSliderValue(); ++i) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
                }
            }
        }
    }

    public Regen() {
        super("Regen", Category.Player);
        this.setDescription("Permet de r\u00e9g\u00e9nerer sa vie plus rapidement");
        this.addSetting(new Setting("WhileMoving", (IModule)this, true));
        this.addSetting(new Setting("Frequence", (IModule)this, 50.0, 1.0, 100.0, true));
    }
}

