// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat.aura;

import me.aristhena.event.Event;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import me.aristhena.client.module.modules.movement.speed.Bhop;
import me.aristhena.client.module.modules.movement.Speed;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import me.aristhena.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import me.aristhena.utils.ClientUtils;
import net.minecraft.entity.Entity;
import me.aristhena.client.module.modules.movement.NoSlowdown;
import me.aristhena.client.module.modules.combat.Aura;
import me.aristhena.utils.StateManager;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import net.minecraft.entity.EntityLivingBase;

public class Juan extends AuraMode
{
    private boolean setupTick;
    private EntityLivingBase target;
    
    public Juan(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        if (super.enable()) {
            this.target = null;
        }
        return true;
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event)) {
            switch (event.getState()) {
                case PRE: {
                    StateManager.setOffsetLastPacketAura(false);
                    final Aura auraModule = (Aura)this.getModule();
                    final NoSlowdown noSlowdownModule = (NoSlowdown)new NoSlowdown().getInstance();
                    if (this.target == null || !auraModule.isEntityValid(this.target)) {
                        this.target = (this.getTargets().isEmpty() ? null : this.getTargets().get(0));
                    }
                    if (this.target != null) {
                        if (auraModule.autoblock && ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                            ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                            if (!noSlowdownModule.isEnabled() && auraModule.noslowdown) {
                                ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                        }
                        final float[] rotations = RotationUtils.getRotations(this.target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        if (this.setupTick) {
                            if (auraModule.criticals && ClientUtils.player().isCollidedVertically && this.bhopCheck()) {
                                event.setY(event.getY() + 0.07);
                                event.setGround(false);
                            }
                        }
                        else {
                            if (auraModule.criticals && ClientUtils.player().isCollidedVertically && this.bhopCheck()) {
                                event.setGround(false);
                                event.setAlwaysSend(true);
                            }
                            if (ClientUtils.player().fallDistance > 0.0f && ClientUtils.player().fallDistance < 0.66) {
                                event.setGround(true);
                            }
                        }
                    }
                    this.setupTick = !this.setupTick;
                    break;
                }
                case POST: {
                    final Aura auraModule = (Aura)this.getModule();
                    if (this.target == null) {
                        break;
                    }
                    if (ClientUtils.player().isBlocking()) {
                        ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    auraModule.attack(this.target);
                    if (ClientUtils.player().isBlocking()) {
                        ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
                        break;
                    }
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean bhopCheck() {
        if (new Speed().getInstance().isEnabled() && ((Speed)new Speed().getInstance()).bhop.getValue()) {
            if (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) {
                return false;
            }
            Bhop.stage = -4;
        }
        return true;
    }
    
    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity : ClientUtils.loadedEntityList()) {
            if (((Aura)this.getModule()).isEntityValid(entity)) {
                targets.add((EntityLivingBase)entity);
            }
        }
        targets.sort(new Comparator<EntityLivingBase>() {
            @Override
            public int compare(final EntityLivingBase target1, final EntityLivingBase target2) {
                return Math.round(target2.getHealth() - target1.getHealth());
            }
        });
        return targets;
    }
}
