package org.m0jang.crystal.Utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Events.EventMove;

public class ClientUtils {
   public static Minecraft mc() {
      return Minecraft.getMinecraft();
   }

   public static EntityPlayerSP player() {
      mc();
      return Minecraft.thePlayer;
   }

   public static PlayerControllerMP playerController() {
      return mc().playerController;
   }

   public static WorldClient world() {
      mc();
      return Minecraft.theWorld;
   }

   public static List loadedEntityList() {
      List loadedList = new ArrayList(world().loadedEntityList);
      loadedList.remove(player());
      return loadedList;
   }

   public static GameSettings gamesettings() {
      mc();
      return Minecraft.gameSettings;
   }

   public static MovementInput movementInput() {
      return player().movementInput;
   }

   public static double x() {
      return player().posX;
   }

   public static void x(double x) {
      player().posX = x;
   }

   public static double y() {
      return player().posY;
   }

   public static void y(double y) {
      player().posY = y;
   }

   public static double z() {
      return player().posZ;
   }

   public static void z(double z) {
      player().posZ = z;
   }

   public static float yaw() {
      return player().rotationYaw;
   }

   public static void yaw(float yaw) {
      player().rotationYaw = yaw;
   }

   public static float pitch() {
      return player().rotationPitch;
   }

   public static void pitch(float pitch) {
      player().rotationPitch = pitch;
   }

   public static void setMoveSpeed(EventMove event, double speed) {
      movementInput();
      double forward = (double)MovementInput.moveForward;
      movementInput();
      double strafe = (double)MovementInput.moveStrafe;
      float yaw = yaw();
      if (forward == 0.0D && strafe == 0.0D) {
         event.setX(0.0D);
         event.setZ(0.0D);
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
         event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
      }

   }
}
