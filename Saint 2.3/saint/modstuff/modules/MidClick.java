package saint.modstuff.modules;

import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.MouseClicked;
import saint.modstuff.Module;
import saint.utilities.Logger;

public class MidClick extends Module {
   public MidClick() {
      super("MidClick");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof MouseClicked) {
         MouseClicked mouse = (MouseClicked)event;
         if (mouse.getButton() == 2 && !Objects.isNull(mc.objectMouseOver.entityHit)) {
            EntityPlayer player = (EntityPlayer)mc.objectMouseOver.entityHit;
            if (!Saint.getFriendManager().isFriend(StringUtils.stripControlCodes(player.getName()))) {
               Saint.getFriendManager().addFriend(player.getName(), player.getName());
               Logger.writeChat("Friend " + player.getName() + " added with the alias of " + player.getName() + ".");
               if (Saint.getFileManager().getFileUsingName("friendfile") != null) {
                  Saint.getFileManager().getFileUsingName("friendfile").saveFile();
               }
            } else {
               Saint.getFriendManager().removeFriend(player.getName());
               Logger.writeChat("Friend " + player.getName() + " removed.");
               if (Saint.getFileManager().getFileUsingName("friendfile") != null) {
                  Saint.getFileManager().getFileUsingName("friendfile").saveFile();
               }
            }
         }
      }

   }
}
