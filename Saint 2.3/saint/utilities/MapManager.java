package saint.utilities;

import java.util.Map;

public abstract class MapManager {
   protected Map contents;

   public final Map getContents() {
      return this.contents;
   }

   public abstract void setup();
}
