// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement.speed;

import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import java.util.List;
import net.minecraft.entity.Entity;
import me.aristhena.client.module.modules.movement.Speed;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.module.Module;

public class Jump extends SpeedMode
{
    private static final double SPEED_BASE = 0.2873;
    private double moveSpeed;
    private double lastDist;
    private int stage;
    public static int settingUpTicks;
    
    public Jump(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        if (super.enable()) {
            this.stage = 0;
            Jump.settingUpTicks = 2;
        }
        return true;
    }
    
    @Override
    public boolean onMove(final MoveEvent event) {
        if (super.onMove(event)) {
            if (ClientUtils.player().isCollidedHorizontally || (ClientUtils.player().moveForward == 0.0f && ClientUtils.player().moveStrafing != 0.0f)) {
                this.stage = 0;
                Jump.settingUpTicks = 5;
            }
            else {
                if (Jump.settingUpTicks > 0 && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    this.moveSpeed = 0.09;
                    --Jump.settingUpTicks;
                }
                else if (this.stage == 1 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    this.moveSpeed = 1.0 + Speed.getBaseMoveSpeed() - 0.05;
                }
                else if (this.stage == 2 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    event.setY(ClientUtils.player().motionY = 0.415);
                    this.moveSpeed *= 2.13;
                }
                else if (this.stage == 3) {
                    final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
                ClientUtils.setMoveSpeed(event, this.moveSpeed);
                final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                final List collidingList2 = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, -0.4, 0.0));
                if (!ClientUtils.player().isCollidedVertically && (collidingList.size() > 0 || collidingList2.size() > 0) && this.stage > 10) {
                    if (this.stage >= 38) {
                        event.setY(ClientUtils.player().motionY = -0.4);
                        this.stage = 0;
                        Jump.settingUpTicks = 5;
                    }
                    else {
                        event.setY(ClientUtils.player().motionY = -0.001);
                    }
                }
                if (Jump.settingUpTicks <= 0 && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    ++this.stage;
                }
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
