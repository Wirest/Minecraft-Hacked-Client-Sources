package saint.valuestuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import saint.utilities.ListManager;
import saint.utilities.Logger;

public class ValueManager extends ListManager {
   public final Value getValueUsingName(String name) {
      Iterator var3 = this.contents.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         if (value.getValueName().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return null;
   }

   public void setup() {
      Logger.writeConsole("Starting to load up the values.");
      this.contents = new ArrayList();
      Collections.sort(this.contents, new Comparator() {
         public int compare(Value value1, Value value2) {
            return value1.getValueName().compareTo(value2.getValueName());
         }
      });
      Logger.writeConsole("Successfully loaded " + this.getContentList().size() + " values.");
   }
}
