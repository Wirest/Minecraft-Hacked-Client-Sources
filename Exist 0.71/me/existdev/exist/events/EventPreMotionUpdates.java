package me.existdev.exist.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventPreMotionUpdates extends EventCancellable implements Event {
   // $FF: synthetic field
   private float yaw;
   // $FF: synthetic field
   private float pitch;
   // $FF: synthetic field
   private double x;
   // $FF: synthetic field
   public double y;
   // $FF: synthetic field
   private double z;
   // $FF: synthetic field
   private boolean ground;
   // $FF: synthetic field
   EventPreMotionUpdates.EventType type;

   // $FF: synthetic method
   public EventPreMotionUpdates(float yaw, float pitch, double x, double y, double z, boolean ground, EventPreMotionUpdates.EventType type) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.x = x;
      this.y = y;
      this.z = z;
      this.ground = ground;
      this.type = type;
   }

   // $FF: synthetic method
   public EventPreMotionUpdates.EventType getType() {
      return this.type;
   }

   // $FF: synthetic method
   public float getYaw() {
      return this.yaw;
   }

   // $FF: synthetic method
   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   // $FF: synthetic method
   public float getPitch() {
      return this.pitch;
   }

   // $FF: synthetic method
   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   // $FF: synthetic method
   public double getX() {
      return this.x;
   }

   // $FF: synthetic method
   public void setX(double x) {
      this.x = x;
   }

   // $FF: synthetic method
   public double getY() {
      return this.y;
   }

   // $FF: synthetic method
   public void setY(double y) {
      this.y = y;
   }

   // $FF: synthetic method
   public double getZ() {
      return this.z;
   }

   // $FF: synthetic method
   public void setZ(double z) {
      this.z = z;
   }

   // $FF: synthetic method
   public boolean isGround() {
      return this.ground;
   }

   // $FF: synthetic method
   public void setGround(boolean ground) {
      this.ground = ground;
   }

   public static enum EventType {
      PRE,
      POST;
   }
}
