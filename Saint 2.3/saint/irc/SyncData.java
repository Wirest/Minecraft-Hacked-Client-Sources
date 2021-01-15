package saint.irc;

import java.util.Date;

public class SyncData {
   private Date time;
   private String user;
   private String info;
   private int x;
   private int y;
   private int z;
   private String server;

   public SyncData(Date time, String user, String info, int x, int y, int z, String server) {
      this.time = time;
      this.user = user;
      this.info = info;
      this.x = x;
      this.y = y;
      this.z = z;
      this.server = server;
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

   public String getUser() {
      return this.user;
   }

   public String getInfo() {
      return this.info;
   }

   public Date getTime() {
      return this.time;
   }

   public String getServer() {
      return this.server;
   }
}
