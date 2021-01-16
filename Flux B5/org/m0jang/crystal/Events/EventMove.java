package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;
import org.m0jang.crystal.Utils.Location;

public class EventMove implements Event {
   public double x;
   public double y;
   public double z;
   private Location location;
   private boolean safeWalk;

   public EventMove(Location location, double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.location = location;
   }

   public double getX() {
      return this.x;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public boolean isSafeWalk() {
      return this.safeWalk;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public void setSafeWalk(boolean value) {
      this.safeWalk = value;
   }
}
