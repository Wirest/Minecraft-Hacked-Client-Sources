// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement.speed;

import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import java.util.List;
import net.minecraft.entity.Entity;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.module.modules.movement.Speed;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.Module;

public class GayHop extends SpeedMode
{
    private double moveSpeed;
    private double lastDist;
    public static int stage;
    
    public GayHop(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        if (super.enable()) {
            if (ClientUtils.player() != null) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            this.lastDist = 0.0;
            GayHop.stage = 1;
        }
        return true;
    }
    
    @Override
    public boolean onMove(final MoveEvent event) {
        if (super.onMove(event)) {
            if (ClientUtils.player().moveForward == 0.0f && ClientUtils.player().moveStrafing == 0.0f) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            if (GayHop.stage == 1 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                this.moveSpeed = 0.25 + Speed.getBaseMoveSpeed() - 0.01;
            }
            else if (GayHop.stage == 2 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                event.setY(ClientUtils.player().motionY = 0.4);
                this.moveSpeed *= 2.149;
            }
            else if (GayHop.stage == 3) {
                final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                if ((collidingList.size() > 0 || ClientUtils.player().isCollidedVertically) && GayHop.stage > 0) {
                    if (1.35 * Speed.getBaseMoveSpeed() - 0.01 > this.moveSpeed) {
                        GayHop.stage = 0;
                    }
                    else {
                        GayHop.stage = ((ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) ? 1 : 0);
                    }
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
            if (GayHop.stage > 0) {
                ClientUtils.setMoveSpeed(event, this.moveSpeed);
            }
            if (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) {
                ++GayHop.stage;
            }
        }
        return true;
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event) && event.getState() == Event.State.PRE) {
            final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
            final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        return true;
    }
}
