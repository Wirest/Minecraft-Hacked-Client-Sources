package saint.eventstuff.events;

import net.minecraft.client.multiplayer.WorldClient;
import saint.eventstuff.Event;

public class WorldLoad extends Event {
   private final WorldClient world;

   public WorldLoad(WorldClient world) {
      this.world = world;
   }

   public WorldClient getWorld() {
      return this.world;
   }
}
