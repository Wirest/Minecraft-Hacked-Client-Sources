// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.event.EventTarget;
import java.util.List;
import net.minecraft.entity.Entity;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class LongJump extends Module
{
    private double moveSpeed;
    private double lastDist;
    public static int stage;
    @Option.Op(increment = 1.0, min = 4.0, max = 24.0)
    private double boost;
    
    public LongJump() {
        this.boost = 4.0;
    }
    
    @Override
    public void enable() {
        LongJump.stage = 0;
        super.enable();
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        final boolean setY = false;
        if (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) {
            if (LongJump.stage == 0) {
                this.moveSpeed = 1.0 + Speed.getBaseMoveSpeed() - 0.05;
            }
            else if (LongJump.stage == 1) {
                event.setY(ClientUtils.player().motionY = 0.42);
                this.moveSpeed *= 2.13;
            }
            else if (LongJump.stage == 2) {
                final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            ClientUtils.setMoveSpeed(event, this.moveSpeed = Math.max(Speed.getBaseMoveSpeed(), this.moveSpeed));
            final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
            final List collidingList2 = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, -0.4, 0.0));
            if (!ClientUtils.player().isCollidedVertically && (collidingList.size() > 0 || collidingList2.size() > 0)) {
                event.setY(ClientUtils.player().motionY = -1.0E-4);
            }
            ++LongJump.stage;
        }
        else if (LongJump.stage > 0) {
            this.disable();
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) {
                final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
                final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
                this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            }
            else {
                event.setCancelled(true);
            }
        }
    }
}
