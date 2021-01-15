// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat.aura;

import me.aristhena.event.Event;
import java.util.Iterator;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import me.aristhena.client.module.modules.movement.speed.Bhop;
import me.aristhena.client.module.modules.movement.Speed;
import me.aristhena.utils.RotationUtils;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.modules.movement.NoSlowdown;
import me.aristhena.client.module.modules.combat.Aura;
import me.aristhena.utils.StateManager;
import me.aristhena.event.events.UpdateEvent;
import java.util.ArrayList;
import me.aristhena.client.module.Module;
import me.aristhena.utils.Timer;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;

public class Switch extends AuraMode
{
    private boolean setupTick;
    private boolean switchingTargets;
    private List<EntityLivingBase> targets;
    private int index;
    private Timer timer;
    public static Timer potTimer;
    
    static {
        Switch.potTimer = new Timer();
    }
    
    public Switch(final String name, final boolean value, final Module module) {
        super(name, value, module);
        this.targets = new ArrayList<EntityLivingBase>();
        this.timer = new Timer();
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event)) {
            switch (event.getState()) {
                case PRE: {
                    StateManager.setOffsetLastPacketAura(false);
                    final Aura auraModule = (Aura)this.getModule();
                    final NoSlowdown noSlowdownModule = (NoSlowdown)new NoSlowdown().getInstance();
                    if (this.timer.delay(300.0f)) {
                        this.targets = this.getTargets();
                    }
                    if (this.index >= this.targets.size()) {
                        this.index = 0;
                    }
                    if (this.targets.size() > 0) {
                        final EntityLivingBase target = this.targets.get(this.index);
                        if (target != null) {
                            if (auraModule.autoblock && ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                                if (!noSlowdownModule.isEnabled() && auraModule.noslowdown) {
                                    ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                }
                            }
                            final float[] rotations = RotationUtils.getRotations(target);
                            event.setYaw(rotations[0]);
                            event.setPitch(rotations[1]);
                        }
                        if (this.setupTick) {
                            if (this.targets.size() > 0 && auraModule.criticals && ClientUtils.player().isCollidedVertically && this.bhopCheck()) {
                                StateManager.setOffsetLastPacketAura(true);
                                event.setY(event.getY() + 0.07);
                                event.setGround(false);
                            }
                            if (this.timer.delay(300.0f)) {
                                this.incrementIndex();
                                this.switchingTargets = true;
                                this.timer.reset();
                            }
                        }
                        else {
                            if (this.targets.size() > 0 && auraModule.criticals && ClientUtils.player().isCollidedVertically && this.bhopCheck()) {
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
                    if (this.setupTick && this.targets.size() > 0 && this.targets.get(this.index) != null && this.targets.size() > 0) {
                        final EntityLivingBase target = this.targets.get(this.index);
                        if (ClientUtils.player().isBlocking()) {
                            ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        for (int i = 0; i < 1; ++i) {
                            if (Switch.potTimer.delay(500.0f)) {
                                auraModule.attack(target);
                            }
                        }
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
    
    private void incrementIndex() {
        ++this.index;
        if (this.index >= this.targets.size()) {
            this.index = 0;
        }
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
