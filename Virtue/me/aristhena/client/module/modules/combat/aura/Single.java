// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat.aura;

import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import me.aristhena.utils.RotationUtils;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.modules.combat.Aura;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import me.aristhena.utils.Timer;
import net.minecraft.entity.EntityLivingBase;

public class Single extends AuraMode
{
    private EntityLivingBase target;
    private Timer timer;
    
    public Single(final String name, final boolean value, final Module module) {
        super(name, value, module);
        this.timer = new Timer();
    }
    
    @Override
    public boolean enable() {
        this.target = null;
        return super.enable();
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event) && event.getState().equals(Event.State.PRE)) {
            final Aura auraModule = (Aura)this.getModule();
            if (ClientUtils.player().isEntityAlive()) {
                this.loadEntity();
                if (this.target != null) {
                    final double x = this.target.posX - ClientUtils.player().posX;
                    final double z = this.target.posZ - ClientUtils.player().posZ;
                    final double h = ClientUtils.y() + ClientUtils.player().getEyeHeight() - (this.target.posY + this.target.getEyeHeight());
                    final double h2 = Math.sqrt(x * x + z * z);
                    final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
                    final float pitch = (float)(Math.atan2(h, h2) * 180.0 / 3.141592653589793);
                    final double xDist = RotationUtils.getDistanceBetweenAngles(yaw, ClientUtils.player().rotationYaw % 360.0f);
                    final double yDist = RotationUtils.getDistanceBetweenAngles(pitch, ClientUtils.player().rotationPitch % 360.0f);
                    final double dist = Math.sqrt(xDist * xDist + yDist * yDist);
                    if (dist > auraModule.degrees) {
                        this.target = null;
                    }
                }
                if (ClientUtils.mc().objectMouseOver.entityHit != null) {
                    final Entity hit = ClientUtils.mc().objectMouseOver.entityHit;
                    if (auraModule.isEntityValidType(hit)) {
                        this.target = (EntityLivingBase)hit;
                    }
                }
            }
            if (this.target != null && this.timer.delay((float)(1000.0 / auraModule.speed))) {
                if (auraModule.criticals) {
                    this.crit();
                }
                auraModule.attack(this.target);
                this.timer.reset();
            }
        }
        return true;
    }
    
    private void crit() {
        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.0624, ClientUtils.z(), true));
        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y(), ClientUtils.z(), false));
        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.11E-4, ClientUtils.z(), false));
        ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y(), ClientUtils.z(), false));
    }
    
    private void loadEntity() {
        final Aura auraModule = (Aura)this.getModule();
        EntityLivingBase loadEntity = null;
        double distance = 2.147483647E9;
        for (final Entity e : ClientUtils.loadedEntityList()) {
            if (e instanceof EntityLivingBase) {
                if (!auraModule.isEntityValid(e)) {
                    continue;
                }
                final double x = e.posX - ClientUtils.player().posX;
                final double z = e.posZ - ClientUtils.player().posZ;
                final double h = ClientUtils.player().posY + ClientUtils.player().getEyeHeight() - (e.posY + e.getEyeHeight());
                final double h2 = Math.sqrt(x * x + z * z);
                final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
                final float pitch = (float)(Math.atan2(h, h2) * 180.0 / 3.141592653589793);
                final double xDist = RotationUtils.getDistanceBetweenAngles(yaw, ClientUtils.player().rotationYaw % 360.0f);
                final double yDist = RotationUtils.getDistanceBetweenAngles(pitch, ClientUtils.player().rotationPitch % 360.0f);
                final double angleDistance = Math.sqrt(xDist * xDist + yDist * yDist);
                if (angleDistance > auraModule.degrees) {
                    continue;
                }
                if (ClientUtils.player().getDistanceToEntity(e) >= distance) {
                    continue;
                }
                loadEntity = (EntityLivingBase)e;
                distance = ClientUtils.player().getDistanceToEntity(e);
            }
        }
        this.target = loadEntity;
    }
}
