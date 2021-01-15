package saint.utilities;

import java.util.List;

public abstract class ListManager {
   protected List contents;

   public final List getContentList() {
      return this.contents;
   }

   public abstract void setup();
}
