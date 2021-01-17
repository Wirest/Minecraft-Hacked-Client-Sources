/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AutoSpawn
extends Module {
    public AutoSpawn() {
        super("AutoSpawn", Category.Misc);
        this.setDescription("Retourne au spawn si la vie du joueur est basse");
        this.addSetting(new Setting("Minimum Health", (IModule)this, 4.0, 1.0, 20.0, true));
    }

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if ((double)this.mc.thePlayer.getHealth() < this.getSetting("Minimum Health").getSliderValue()) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(1000000.0, 1000.0, 1000.0, 1000000.0, true));
            this.toggle();
        }
    }
}

