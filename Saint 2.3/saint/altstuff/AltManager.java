package saint.altstuff;

import java.util.ArrayList;
import saint.utilities.Alt;
import saint.utilities.ListManager;

public class AltManager extends ListManager {
   private Alt lastAlt;

   public Alt getLastAlt() {
      return this.lastAlt;
   }

   public void setLastAlt(Alt alt) {
      this.lastAlt = alt;
   }

   public void setup() {
      this.contents = new ArrayList();
   }
}
