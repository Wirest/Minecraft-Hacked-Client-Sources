// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat.aura;

import me.aristhena.event.Event;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.modules.combat.Aura;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;

public class Vanilla extends AuraMode
{
    public Vanilla(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event)) {
            switch (event.getState()) {
                case PRE: {
                    final Aura auraModule = (Aura)this.getModule();
                    boolean targets = false;
                    for (final Entity entity : ClientUtils.loadedEntityList()) {
                        if (auraModule.isEntityValid(entity)) {
                            targets = true;
                        }
                    }
                    if (targets && auraModule.criticals) {
                        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.001, ClientUtils.z(), true));
                        event.setGround(false);
                        event.setAlwaysSend(true);
                    }
                    if (targets && auraModule.autoblock && ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                        ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                    }
                    for (final Entity entity : ClientUtils.loadedEntityList()) {
                        if (auraModule.isEntityValid(entity)) {
                            auraModule.attack((EntityLivingBase)entity);
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }
}
