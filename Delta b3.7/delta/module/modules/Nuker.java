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
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 */
package delta.module.modules;

import delta.utils.BoundingBox;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class Nuker
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        int n = (int)this.getSetting("Radius").getSliderValue();
        for (int i = -n; i < n; ++i) {
            for (int j = -n; j < n; ++j) {
                for (int k = -n; k < n; ++k) {
                    BoundingBox boundingBox = new BoundingBox((Entity)this.mc.thePlayer).offset(i, j, k);
                    Block block = this.mc.theWorld.getBlock((int)boundingBox._talented(), (int)boundingBox._adelaide(), (int)boundingBox._produce());
                    if (block == null || block.getMaterial().isLiquid() || Block.getIdFromBlock((Block)block) == 0 || block == Blocks.bedrock) continue;
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(0, (int)boundingBox._talented(), (int)boundingBox._adelaide(), (int)boundingBox._produce(), 1));
                    if (this.mc.thePlayer.capabilities.isCreativeMode) continue;
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(2, (int)boundingBox._talented(), (int)boundingBox._adelaide(), (int)boundingBox._produce(), 1));
                }
            }
        }
    }

    public Nuker() {
        super("Nuker", Category.World);
        this.setDescription("Casse tout les blocks aux alentours (Requiert l'outil appropri\u00e9)");
        this.addSetting(new Setting("Radius", (IModule)this, 3.0, 1.0, 5.0, true));
    }
}

