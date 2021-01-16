package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventUpdate extends EventCancellable {
   public double x;
   public double y;
   public double z;
   public float yaw;
   public float pitch;
   public boolean onGround;
   public EventState state;
   public boolean alwaysSend;
   private double stepHeight;
   private boolean active;

   public EventUpdate(double x, double y, double z, float yaw, float pitch, boolean onGround) {
      this.y = y;
      this.z = z;
      this.x = x;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
      this.state = EventState.PRE;
   }

   public EventUpdate() {
      this.state = EventState.POST;
   }

   public double getStepHeight() {
      return this.stepHeight;
   }

   public boolean shouldAlwaysSend() {
      return this.alwaysSend;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setStepHeight(double stepHeight) {
      this.stepHeight = stepHeight;
   }

   public void setActive(boolean bypass) {
      this.active = bypass;
   }

   public void setAlwaysSend(boolean alwaysSend) {
      this.alwaysSend = alwaysSend;
   }
}
