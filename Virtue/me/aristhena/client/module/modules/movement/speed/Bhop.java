// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement.speed;

import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import java.util.List;
import net.minecraft.entity.Entity;
import me.aristhena.utils.MathUtils;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.module.modules.movement.Speed;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.Module;

public class Bhop extends SpeedMode
{
    private double moveSpeed;
    private double lastDist;
    public static int stage;
    
    public Bhop(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        if (super.enable()) {
            if (ClientUtils.player() != null) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            this.lastDist = 0.0;
            Bhop.stage = 1;
        }
        return true;
    }
    
    @Override
    public boolean onMove(final MoveEvent event) {
        if (super.onMove(event)) {
            if (ClientUtils.player().moveForward == 0.0f && ClientUtils.player().moveStrafing == 0.0f) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            if (MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                event.setY(ClientUtils.player().motionY = 0.31);
            }
            else if (MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.71, 3)) {
                event.setY(ClientUtils.player().motionY = 0.04);
            }
            else if (MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.75, 3)) {
                event.setY(ClientUtils.player().motionY = -0.2);
            }
            List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, -0.56, 0.0));
            if (collidingList.size() > 0 && MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.55, 3)) {
                event.setY(ClientUtils.player().motionY = -0.14);
            }
            if (Bhop.stage == 1 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                this.moveSpeed = 2.0 * Speed.getBaseMoveSpeed() - 0.01;
            }
            else if (Bhop.stage == 2 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                event.setY(ClientUtils.player().motionY = 0.4);
                this.moveSpeed *= 2.149;
            }
            else if (Bhop.stage == 3) {
                final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                if ((collidingList.size() > 0 || ClientUtils.player().isCollidedVertically) && Bhop.stage > 0) {
                    if (1.35 * Speed.getBaseMoveSpeed() - 0.01 > this.moveSpeed) {
                        Bhop.stage = 0;
                    }
                    else {
                        Bhop.stage = ((ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) ? 1 : 0);
                    }
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            if (Bhop.stage > 8) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
            if (Bhop.stage > 0) {
                ClientUtils.setMoveSpeed(event, this.moveSpeed);
            }
            if (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f) {
                ++Bhop.stage;
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
