package saint.eventstuff.events;

import net.minecraft.entity.Entity;
import saint.eventstuff.Event;

public class Attack extends Event {
   Entity target;

   public Attack(Entity target) {
      this.target = target;
   }

   public Entity getTarget() {
      return this.target;
   }

   public void setTarget(Entity target) {
      this.target = target;
   }
}
