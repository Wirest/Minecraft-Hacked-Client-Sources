package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.callables.EventCancellable;

public abstract class PlayerUpdateEvent
extends EventCancellable
{
protected float yaw;
protected float pitch;
protected boolean onGround;
protected boolean sneaking;

public float getYaw()
{
  return this.yaw;
}

public void setYaw(float yaw)
{
  this.yaw = yaw;
}

public float getPitch()
{
  return this.pitch;
}

public void setPitch(float pitch)
{
  this.pitch = pitch;
}

public boolean isOnGround()
{
  return this.onGround;
}

public void setOnGround(boolean onGround)
{
  this.onGround = onGround;
}

public boolean isSneaking()
{
  return this.sneaking;
}

public void setSneaking(boolean sneaking)
{
  this.sneaking = sneaking;
}

public Object getType() {
	return null;
}

}
