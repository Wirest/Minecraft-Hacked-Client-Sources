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
import me.aristhena.client.option.Option;

public class Latest extends SpeedMode
{
    @Op
    private boolean race;
    private double moveSpeed;
    private double lastDist;
    private double stage;
    
    public Latest(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        if (super.enable()) {
            if (ClientUtils.player() != null) {
                this.moveSpeed = Speed.getBaseMoveSpeed();
            }
            this.lastDist = 0.0;
            this.stage = 2.0;
        }
        return true;
    }
    
    @Override
    public boolean onMove(final MoveEvent event) {
        if (super.onMove(event) && (ClientUtils.player().onGround || this.stage == 3.0)) {
            if ((!ClientUtils.player().isCollidedHorizontally && ClientUtils.player().moveForward != 0.0f) || ClientUtils.player().moveStrafing != 0.0f) {
                if (this.stage == 2.0) {
                    this.moveSpeed *= 2.149;
                    this.stage = 3.0;
                }
                else if (this.stage == 3.0) {
                    this.stage = 2.0;
                    final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                    if (collidingList.size() > 0 || ClientUtils.player().isCollidedVertically) {
                        this.stage = 1.0;
                    }
                }
            }
            else {
                ClientUtils.mc().timer.timerSpeed = 1.0f;
            }
            ClientUtils.setMoveSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed()));
        }
        return true;
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event) && event.getState() == Event.State.PRE) {
            if (this.stage == 3.0) {
                event.setY(event.getY() + 0.4);
            }
            final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
            final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        return true;
    }
}
