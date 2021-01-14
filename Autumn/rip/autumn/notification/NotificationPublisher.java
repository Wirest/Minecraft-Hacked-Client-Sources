package rip.autumn.notification;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.render.AnimationUtils;
import rip.autumn.utils.render.RenderUtils;
import rip.autumn.utils.render.Translate;

public final class NotificationPublisher {
   private static final List NOTIFICATIONS = new CopyOnWriteArrayList();

   public static void publish(ScaledResolution sr) {
      if (!NOTIFICATIONS.isEmpty()) {
         int srScaledHeight = sr.getScaledHeight();
         int scaledWidth = sr.getScaledWidth();
         int y = srScaledHeight - 30;
         Minecraft mc = Minecraft.getMinecraft();
         HUDMod hud = (HUDMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(HUDMod.class);
         FontRenderer fr = hud.defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;

         for(Iterator var7 = NOTIFICATIONS.iterator(); var7.hasNext(); y -= 30) {
            Notification notification = (Notification)var7.next();
            Translate translate = notification.getTranslate();
            int width = notification.getWidth();
            if (!notification.getTimer().elapsed((long)notification.getTime())) {
               notification.scissorBoxWidth = AnimationUtils.animate((double)width, notification.scissorBoxWidth, 0.1D);
               translate.interpolate((double)(scaledWidth - width), (double)y, 0.15D);
            } else {
               notification.scissorBoxWidth = AnimationUtils.animate(0.0D, notification.scissorBoxWidth, 0.1D);
               if (notification.scissorBoxWidth < 1.0D) {
                  NOTIFICATIONS.remove(notification);
               }

               y += 30;
            }

            float translateX = (float)translate.getX();
            float translateY = (float)translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtils.prepareScissorBox((float)((double)scaledWidth - notification.scissorBoxWidth), translateY, (float)scaledWidth, translateY + 30.0F);
            Gui.drawRect((double)translateX, (double)translateY, (double)scaledWidth, (double)(translateY + 30.0F), -1879048192);
            Gui.drawRect((double)translateX, (double)(translateY + 30.0F - 2.0F), (double)translateX + (double)width * ((double)((long)notification.getTime() - notification.getTimer().getElapsedTime()) / (double)notification.getTime()), (double)(translateY + 30.0F), notification.getType().getColor());
            fr.drawStringWithShadow(notification.getTitle(), translateX + 4.0F, translateY + 4.0F, -1);
            fr.drawStringWithShadow(notification.getContent(), translateX + 4.0F, translateY + 17.0F, -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
         }

      }
   }

   public static void queue(String title, String content, NotificationType type) {
      Minecraft mc = Minecraft.getMinecraft();
      HUDMod hud = (HUDMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(HUDMod.class);
      FontRenderer fr = hud.defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;
      NOTIFICATIONS.add(new Notification(title, content, type, fr));
   }
}
