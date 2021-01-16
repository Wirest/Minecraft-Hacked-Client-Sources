package org.m0jang.crystal.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class FriendManager {
   private ArrayList friends = new ArrayList();

   public void addFriend(String name) {
      if (!this.getFriends().contains(name.toLowerCase())) {
         this.getFriends().add(name.toLowerCase());
         ChatUtils.sendMessageToPlayer(name + " added to friends");
      } else {
         ChatUtils.sendMessageToPlayer(name + " already found in friends");
      }

   }

   public void delFriend(String name) {
      if (this.getFriends().contains(name.toLowerCase())) {
         this.getFriends().remove(name.toLowerCase());
         ChatUtils.sendMessageToPlayer(name + " removed from friends");
      } else {
         ChatUtils.sendMessageToPlayer(name + " not found in friends");
      }

   }

   public ArrayList getFriends() {
      return this.friends;
   }

   public boolean isFriend(String name) {
      Iterator var3 = this.getFriends().iterator();

      while(var3.hasNext()) {
         String friend = (String)var3.next();
         if (friend.contains(name.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isTeam(EntityLivingBase entity) {
      return RenderUtils.getTeamColor(entity) == RenderUtils.getTeamColor(Minecraft.thePlayer);
   }
}
