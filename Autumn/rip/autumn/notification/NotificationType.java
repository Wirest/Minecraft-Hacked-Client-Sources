package rip.autumn.notification;

import java.awt.Color;

public enum NotificationType {
   SUCCESS((new Color(6348946)).getRGB()),
   INFO((new Color(6590631)).getRGB()),
   ERROR((new Color(16723759)).getRGB());

   private final int color;

   private NotificationType(int color) {
      this.color = color;
   }

   public final int getColor() {
      return this.color;
   }
}
