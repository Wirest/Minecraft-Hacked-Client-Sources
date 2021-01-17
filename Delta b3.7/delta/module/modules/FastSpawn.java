/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package delta.module.modules;

import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastSpawn
extends Module {
    public FastSpawn() {
        super("FastSpawn", Category.Misc);
        this.setDescription("Retourne au spawn instentan\u00e9ment");
    }

    public void onEnable() {
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(1000000.0, 1000.0, 1000.0, 1000000.0, true));
        this.toggle();
        super.onEnable();
    }
}

