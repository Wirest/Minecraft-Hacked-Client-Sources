package saint.clickgui.element;

public class Element {
   protected int x;
   protected int y;
   protected int width;
   protected int height;

   public void setLocation(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void drawScreen(int mouseX, int mouseY, float button) {
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }
}
