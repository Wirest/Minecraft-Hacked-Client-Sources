package saint.eventstuff.events;

import saint.eventstuff.Event;

public class DrawScreen extends Event {
   int posX;
   int posY;

   public DrawScreen(int posX, int posY) {
      this.posX = posX;
      this.posY = posY;
   }

   public DrawScreen() {
   }

   public int getPosX() {
      return this.posX;
   }

   public int getPosY() {
      return this.posY;
   }

   public void setPosX(int posX) {
      this.posX = posX;
   }

   public void setPosY(int posY) {
      this.posY = posY;
   }
}
