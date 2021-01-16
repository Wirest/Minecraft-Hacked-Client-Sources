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
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class MultiAura
extends AuraType {
    @Override
    public void onUpdate(EventPreMotionUpdates event) {
        this.gasTheJew();
    }

    public void gasTheJew() {
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            EntityLivingBase jew;
            if (!(o instanceof EntityLivingBase) || !this.isAttackable(jew = (EntityLivingBase)o) || !this.isInAttackRange(jew)) continue;
            ++this.tTicks;
            if (!this.isSwingable()) continue;
            Aura var10000 = ModuleManager.aura;
            if (Aura.crits.boolvalue) {
                this.crittheshit();
            }
            boolean justunblockedtheguyneededtobeattackedbutnocheatisgay = false;
            if (Wrapper.getPlayer().isActiveItemStackBlocking()) {
                this.unBlock();
                justunblockedtheguyneededtobeattackedbutnocheatisgay = true;
            }
            Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), jew);
            this.swing();
            if (justunblockedtheguyneededtobeattackedbutnocheatisgay) {
                this.block();
            }
            this.tTicks = 0;
        }
    }

    public boolean isSwingable() {
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

    public void gasThe(EntityLivingBase jew) {
    }

    @Override
    public void afterUpdate(EventPostMotionUpdates event) {
    }

    @Override
    public void onPacketOut(EventPacketSent event) {
    }
}

