/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat.aura.types;

import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import java.util.List;
import java.util.Random;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.combat.aura.Aura;
import me.razerboy420.weepcraft.module.modules.combat.aura.AuraType;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class SingleAura
extends AuraType {
    @Override
    public void onUpdate(EventPreMotionUpdates event) {
        if (!(jew == null || this.isAttackable(jew) && this.isInAttackRange(jew))) {
            jew = null;
        }
        if (jew == null) {
            jew = this.getClosestNigger();
        }
        if (jew != null && this.isInAttackRange(this.getClosestNigger()) && this.isAttackable(jew)) {
            float o = 2.14748365E9f;
            Aura var10000 = ModuleManager.aura;
            if (Aura.lockview.boolvalue) {
                Wrapper.getPlayer().rotationYaw = this.getRots(jew, o, o)[0];
                Wrapper.getPlayer().rotationPitch = this.getRots(jew, o, o)[1];
            }
            this.face(jew);
            yaw = this.getRots(jew, o, o)[0];
            pitch = this.getRots(jew, o, o)[1];
        }
        if (Aura.block.boolvalue) {
            for (Object o1 : Wrapper.getWorld().loadedEntityList) {
                EntityLivingBase nig;
                if (!(o1 instanceof EntityLivingBase) || !this.isInBlockRange(nig = (EntityLivingBase)o1) || !this.isAttackable(nig)) continue;
                this.block();
                break;
            }
        }
        if (MathUtils.isAuraBlocking()) {
            this.unBlock();
        }
    }

    public boolean canIFuckingSwing() {
        if (Aura.autoaps.boolvalue) {
            if (Wrapper.getPlayer().getCooledAttackStrength(0.0f) == 1.0f) {
                return true;
            }
            return false;
        }
        Random r = new Random();
        boolean neg = r.nextBoolean();
        double var10000 = this.tTicks;
        Aura var10002 = ModuleManager.aura;
        if (var10000 >= 20.0 / ((double)Aura.delay.value.floatValue() + (neg ? this.random : - this.random))) {
            return true;
        }
        return false;
    }

    @Override
    public void afterUpdate(EventPostMotionUpdates event) {
        if (!this.isHealing()) {
            EntityLivingBase nig;
            if (Aura.block.boolvalue) {
                for (Object o : Wrapper.getWorld().loadedEntityList) {
                    if (!(o instanceof EntityLivingBase) || !this.isInBlockRange(nig = (EntityLivingBase)o) || !this.isAttackable(nig)) continue;
                    this.block();
                    break;
                }
            }
            if (jew != null && this.isInAttackRange(jew) && this.isAttackable(jew)) {
                ++this.tTicks;
                ModuleManager.aura.timer.update();
                if (ModuleManager.aura.timer.hasPassed(50)) {
                    Random o1 = new Random();
                    boolean neg1 = o1.nextBoolean();
                    double var10001 = Math.random();
                    Aura var10002 = ModuleManager.aura;
                    this.random = var10001 * (double)Aura.random.value.floatValue();
                    if (this.canIFuckingSwing()) {
                        this.gasThe(jew);
                        this.tTicks = 0;
                    }
                    ModuleManager.aura.timer.reset();
                }
            }
            if (Aura.block.boolvalue) {
                for (Object o : Wrapper.getWorld().loadedEntityList) {
                    if (!(o instanceof EntityLivingBase) || !this.isInBlockRange(nig = (EntityLivingBase)o) || !this.isAttackable(nig)) continue;
                    this.block();
                    return;
                }
            }
        }
    }

    @Override
    public void onPacketOut(EventPacketSent event) {
        if (!this.isHealing() && jew != null && this.isInAttackRange(jew) && this.isAttackable(jew)) {
            CPacketPlayer p;
            if (event.getPacket() instanceof CPacketPlayer) {
                p = (CPacketPlayer)event.getPacket();
                p.yaw = yaw;
                p.pitch = pitch;
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                p = (CPacketPlayer)event.getPacket();
                p.yaw = yaw;
                p.pitch = pitch;
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                p = (CPacketPlayer)event.getPacket();
                p.yaw = yaw;
                p.pitch = pitch;
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                p = (CPacketPlayer)event.getPacket();
                p.yaw = yaw;
                p.pitch = pitch;
            }
            if (event.getPacket() instanceof CPacketPlayer) {
                p = (CPacketPlayer)event.getPacket();
                p.yaw = yaw;
                p.pitch = pitch;
            }
        }
    }

    public void gasThe(EntityLivingBase jew) {
        Aura var10000 = ModuleManager.aura;
        if (Aura.crits.boolvalue) {
            this.crittheshit();
        }
        boolean justunblockedtheguyneededtobeattackedbutnocheatisgay = false;
        if (MathUtils.isAuraBlocking()) {
            this.unBlock();
            justunblockedtheguyneededtobeattackedbutnocheatisgay = true;
        }
        Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), jew);
        this.swing();
        if (justunblockedtheguyneededtobeattackedbutnocheatisgay) {
            this.block();
        }
    }

    public EntityLivingBase getClosestNigger() {
        EntityLivingBase close = null;
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            EntityLivingBase negro;
            if (!(o instanceof EntityLivingBase) || !this.isAttackable(negro = (EntityLivingBase)o) || !this.isInAttackRange(negro) || close != null && close.getDistanceToEntity(Wrapper.getPlayer()) <= negro.getDistanceToEntity(Wrapper.getPlayer())) continue;
            close = negro;
        }
        return close;
    }
}

