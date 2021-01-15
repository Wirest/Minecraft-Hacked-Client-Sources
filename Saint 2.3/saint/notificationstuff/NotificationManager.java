package saint.notificationstuff;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import saint.Saint;
import saint.utilities.ListManager;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class NotificationManager extends ListManager {
   public boolean isDrawing;

   public void addNotification(String notification) {
      this.getContentList().add(new Notification(Saint.getFriendManager().replaceNames(notification, true)));
      if (this.getContentList().size() > 16) {
         this.getContentList().remove(this.getContentList().get(0));
      }

   }

   public boolean willDraw() {
      return Saint.getModuleManager().getModuleUsingName("notifications").isEnabled();
   }

   public void drawNotifications() {
      if (Saint.getModuleManager().getModuleUsingName("notifications").isEnabled() && this.getContentList().size() > 0) {
         int yPos = 1;
         ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
         Iterator var4 = this.getContentList().iterator();

         while(var4.hasNext()) {
            Notification not = (Notification)var4.next();
            if (System.currentTimeMillis() - not.addTime >= 7500L) {
               this.isDrawing = false;
               this.getContentList().remove(not);
            } else if (System.currentTimeMillis() - not.addTime >= 7250L) {
               this.isDrawing = true;
               RenderHelper.drawRect((float)scaledRes.getScaledWidth() + RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 1 - yPos), (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 18.0F, (float)(scaledRes.getScaledHeight() - 11 - yPos), 1080650089);
               RenderHelper.getNahrFont().drawString(not.message, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 15 - yPos), NahrFont.FontType.SHADOW_THICK, 1892930515, 1879048192);
               yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message));
            } else if (System.currentTimeMillis() - not.addTime >= 7000L) {
               this.isDrawing = true;
               RenderHelper.drawRect((float)scaledRes.getScaledWidth() + RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 1 - yPos), (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 18.0F, (float)(scaledRes.getScaledHeight() - 11 - yPos), 1349085545);
               RenderHelper.getNahrFont().drawString(not.message, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 15 - yPos), NahrFont.FontType.SHADOW_THICK, -2049715245, -2063597568);
               yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message));
            } else if (System.currentTimeMillis() - not.addTime >= 6750L) {
               this.isDrawing = true;
               RenderHelper.drawRect((float)scaledRes.getScaledWidth() + RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 1 - yPos), (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 18.0F, (float)(scaledRes.getScaledHeight() - 11 - yPos), 1617521001);
               RenderHelper.getNahrFont().drawString(not.message, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 15 - yPos), NahrFont.FontType.SHADOW_THICK, -1714170925, -1728053248);
               yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message));
            } else if (System.currentTimeMillis() - not.addTime >= 6500L) {
               this.isDrawing = true;
               RenderHelper.drawRect((float)scaledRes.getScaledWidth() + RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 1 - yPos), (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 18.0F, (float)(scaledRes.getScaledHeight() - 11 - yPos), 1885956457);
               RenderHelper.getNahrFont().drawString(not.message, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 15 - yPos), NahrFont.FontType.SHADOW_THICK, -573320237, -587202560);
               yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message));
            } else {
               this.isDrawing = true;
               RenderHelper.drawRect((float)scaledRes.getScaledWidth() + RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 1 - yPos), (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 18.0F, (float)(scaledRes.getScaledHeight() - 11 - yPos), -2140575383);
               RenderHelper.getNahrFont().drawString(not.message, (float)scaledRes.getScaledWidth() - RenderHelper.getNahrFont().getStringWidth(not.message) + 19.0F, (float)(scaledRes.getScaledHeight() - 15 - yPos), NahrFont.FontType.SHADOW_THICK, -2894893, -16777216);
               yPos = (int)((float)yPos + RenderHelper.getNahrFont().getStringHeight(not.message));
            }
         }
      }

   }

   public void addInfo(String msg) {
      Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
      msg = "§b[Saint]§r " + msg;
      Saint.getNotificationManager().addNotification(msg);
   }

   public void addWarn(String msg) {
      Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 0.1F);
      msg = "§c[Saint]§r " + msg;
      Saint.getNotificationManager().addNotification(msg);
   }

   public void setup() {
      this.contents = new ArrayList();
      Iterator var2 = Notification.notifications.iterator();

      while(var2.hasNext()) {
         Notification not = (Notification)var2.next();
         this.getContentList().add(not);
      }

   }
}
