package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.Client;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.friendmanager.Friend;
import me.slowly.client.util.friendmanager.FriendManager;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.entity.player.EntityPlayer;

public class MidClickFriends extends Mod {
   private MouseInputHandler handler = new MouseInputHandler(2);

   public MidClickFriends() {
      super("MidClickFriends", Mod.Category.PLAYER, Colors.DARKMAGENTA.c);
   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      this.setColor(Colors.DARKGREEN.c);
      if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
         String name = this.mc.objectMouseOver.entityHit.getName();
         if (this.handler.canExcecute()) {
            if (FriendManager.isFriend(name)) {
               for(int i = 0; i < FriendManager.getFriends().size(); ++i) {
                  Friend f = (Friend)FriendManager.getFriends().get(i);
                  if (f.getName().equalsIgnoreCase(name)) {
                     FriendManager.getFriends().remove(i);
                  }
               }
            } else {
               FriendManager.getFriends().add(new Friend(name, name));
            }

            Client.getInstance().getFileUtil().saveFriends();
         }
      }

   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("MidClickFriends Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("MidClickFriends Enable", ClientNotification.Type.SUCCESS);
   }
}

