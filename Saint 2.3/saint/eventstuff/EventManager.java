package saint.eventstuff;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import saint.utilities.ListManager;

public class EventManager extends ListManager {
   public void hook(Event event) {
      if (this.getContentList() != null) {
         try {
            Iterator var3 = this.getContentList().iterator();

            while(var3.hasNext()) {
               Listener listener = (Listener)var3.next();
               if (listener != null) {
                  listener.onEvent(event);
               }
            }
         } catch (ConcurrentModificationException var4) {
            var4.printStackTrace();
         } catch (NoSuchElementException var5) {
            var5.printStackTrace();
         }

      }
   }

   public void addListener(Listener listener) {
      if (!this.getContentList().contains(listener)) {
         this.getContentList().add(listener);
      }

   }

   public void removeListener(Listener listener) {
      if (this.getContentList().contains(listener)) {
         this.getContentList().remove(listener);
      }

   }

   public void setup() {
      this.contents = new CopyOnWriteArrayList();
   }
}
