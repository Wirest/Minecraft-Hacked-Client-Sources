package saint.modstuff.modules.addons;

public final class Point {
   protected String name;
   protected String server;
   protected int x;
   protected int y;
   protected int z;

   public Point(String name, String server, int x, int y, int z) {
      this.name = name;
      this.server = server;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public String getName() {
      return this.name;
   }

   public String getServer() {
      return this.server;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public void setName(String name) {
      this.name = name;
   }
}
