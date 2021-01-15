package saint.irc;

import java.util.Date;
import net.minecraft.client.Minecraft;
import saint.Saint;

public class SendingThread extends Thread {
   private boolean sending = true;
   private Minecraft mc = Minecraft.getMinecraft();

   public SendingThread() {
      super("Saint Information Sending Thread");
   }

   public void run() {
      while(this.isSending()) {
         try {
            String username = this.mc.getSession().getUsername();
            int z = 0;
            int y = 0;
            int x = 0;
            String server = null;
            if (this.mc.thePlayer != null && this.mc.theWorld != null) {
               x = (int)this.mc.thePlayer.posX;
               y = (int)this.mc.thePlayer.posY;
               z = (int)this.mc.thePlayer.posZ;
               if (this.mc.getCurrentServerData() != null) {
                  server = this.mc.getCurrentServerData().serverIP;
               } else {
                  server = "Vanilla Single Player";
               }
            }

            SyncUtil.sendWebData(new SyncData(new Date(), username, ("Saint v" + Saint.getVersion()).replaceAll(" ", "%20"), x, y, z, server));
            Thread.sleep(20000L);
         } catch (Exception var6) {
            var6.printStackTrace();
            this.sending = false;
         }
      }

   }

   public boolean isSending() {
      return this.sending;
   }

   public void setSending(boolean sending) {
      this.sending = sending;
   }
}
