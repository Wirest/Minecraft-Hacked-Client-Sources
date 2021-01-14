package com.mentalfrostbyte.jello.event.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.mentalfrostbyte.jello.event.types.EventType2;

import net.minecraft.client.Minecraft;

public class EventMotion
implements Event
{
public double x;
public double y;
public double z;
//private Location location;
private float yaw;
private float pitch;
private boolean onGround;
private EventType2 type;
private boolean alwaysSend;


public EventMotion(/**Location location, */boolean onGround, float yaw, float pitch, EventType2 type)
{
 // this.location = location;
  this.onGround = onGround;
  this.type = type;
  this.yaw = yaw;
  this.pitch = pitch;
}

//public Location getLocation()
//{
//  return this.location;
//}

//public void setLocation(Location location)
//{
//  this.location = location;
//}
public void setAlwaysSend(boolean alwaysSend) {
    this.alwaysSend = alwaysSend;
}

public void setY(double y) {
    this.y = y;
}

public boolean isOnGround()
{
  return this.onGround;
}
public double getY() {
    return this.y;
}
public boolean shouldAlwaysSend() {
    return this.alwaysSend;
}

public void setOnGround(boolean onGround)
{
  this.onGround = onGround;
}

public EventType2 getType()
{
  return this.type;
}

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
    
}

