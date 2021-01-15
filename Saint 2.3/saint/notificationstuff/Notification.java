package saint.notificationstuff;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Notification {
   public final String message;
   public final long addTime;
   public static final List notifications = new CopyOnWriteArrayList();

   public Notification(String message) {
      this.message = message;
      this.addTime = System.currentTimeMillis();
      notifications.add(this);
   }
}
