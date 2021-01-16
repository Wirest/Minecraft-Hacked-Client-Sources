package me.existdev.exist.module;

import com.darkmagician6.eventapi.EventManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class Module {
   // $FF: synthetic field
   public static Minecraft mc = Minecraft.getMinecraft();
   // $FF: synthetic field
   private Module.Category category;
   // $FF: synthetic field
   private boolean toggled;
   // $FF: synthetic field
   private String name;
   // $FF: synthetic field
   private String displayname;
   // $FF: synthetic field
   private int bind;

   // $FF: synthetic method
   public Module(String name, int bind, Module.Category category) {
      this.name = name;
      this.displayname = name;
      this.bind = bind;
      this.category = category;
      this.toggled = false;
   }

   // $FF: synthetic method
   public void toggle() {
      this.toggled = !this.toggled;
      if(this.toggled) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }

   // $FF: synthetic method
   public void onUpdate() {
   }

   // $FF: synthetic method
   public void onRender() {
   }

   // $FF: synthetic method
   public void onEnable() {
      EventManager.register(this);
      mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
   }

   // $FF: synthetic method
   public void onDisable() {
      EventManager.unregister(this);
      mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.8F));
   }

   // $FF: synthetic method
   public void setSpeed(double speed) {
      EntityPlayerSP player = Minecraft.thePlayer;
      double yaw = (double)player.rotationYaw;
      boolean isMoving = player.moveForward != 0.0F || player.moveStrafing != 0.0F;
      boolean isMovingForward = player.moveForward > 0.0F;
      boolean isMovingBackward = player.moveForward < 0.0F;
      boolean isMovingRight = player.moveStrafing > 0.0F;
      boolean isMovingLeft = player.moveStrafing < 0.0F;
      boolean isMovingSideways = isMovingLeft || isMovingRight;
      boolean isMovingStraight = isMovingForward || isMovingBackward;
      if(isMoving) {
         if(isMovingForward && !isMovingSideways) {
            yaw += 0.0D;
         } else if(isMovingBackward && !isMovingSideways) {
            yaw += 180.0D;
         } else if(isMovingForward && isMovingLeft) {
            yaw += 45.0D;
         } else if(isMovingForward) {
            yaw -= 45.0D;
         } else if(!isMovingStraight && isMovingLeft) {
            yaw += 90.0D;
         } else if(!isMovingStraight && isMovingRight) {
            yaw -= 90.0D;
         } else if(isMovingBackward && isMovingLeft) {
            yaw += 135.0D;
         } else if(isMovingBackward) {
            yaw -= 135.0D;
         }

         yaw = Math.toRadians(yaw);
         player.motionX = -Math.sin(yaw) * speed;
         player.motionZ = Math.cos(yaw) * speed;
      }

   }

   // $FF: synthetic method
   public ArrayList getSettings() {
      ArrayList settings = new ArrayList();
      Field[] var5;
      int var4 = (var5 = this.getClass().getDeclaredFields()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Field field = var5[var3];

         try {
            field.setAccessible(true);
            Object e = field.get(this);
            if(e instanceof Setting) {
               settings.add((Setting)e);
            }
         } catch (IllegalAccessException var7) {
            var7.printStackTrace();
         }
      }

      return settings;
   }

   // $FF: synthetic method
   public String getName() {
      return this.name;
   }

   // $FF: synthetic method
   public void setName(String name) {
      this.name = name;
   }

   // $FF: synthetic method
   public String getDisplayName() {
      return this.displayname;
   }

   // $FF: synthetic method
   public void setDisplayName(String displayname) {
      this.displayname = displayname;
   }

   // $FF: synthetic method
   public boolean isToggled() {
      return this.toggled;
   }

   // $FF: synthetic method
   public void setToggled(boolean toggled) {
      this.toggled = toggled;
   }

   // $FF: synthetic method
   public Module.Category getCategory() {
      return this.category;
   }

   // $FF: synthetic method
   public int getBind() {
      return this.bind;
   }

   // $FF: synthetic method
   public void setBind(int bind) {
      this.bind = bind;
   }

   public static enum Category {
      Combat,
      Movement,
      Player,
      Render,
      World,
      Settings;
   }
}
