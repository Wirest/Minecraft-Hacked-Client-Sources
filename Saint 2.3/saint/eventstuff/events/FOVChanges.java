package saint.eventstuff.events;

import saint.eventstuff.Event;

public class FOVChanges extends Event {
   private float fov;

   public FOVChanges(float fov) {
      this.fov = fov;
   }

   public float getFOV() {
      return this.fov;
   }

   public void setFOV(float fov) {
      this.fov = fov;
   }
}
