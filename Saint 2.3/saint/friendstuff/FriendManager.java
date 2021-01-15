package saint.friendstuff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.comandstuff.commands.Ghost;
import saint.utilities.MapManager;

public final class FriendManager extends MapManager {
   public void addFriend(String name, String alias) {
      this.contents.put(name, alias);
   }

   public boolean isFriend(String name) {
      return this.contents.containsKey(StringUtils.stripControlCodes(name));
   }

   public void removeFriend(String name) {
      this.contents.remove(name);
   }

   public String replaceNames(String message, boolean color) {
      Ghost ghost = (Ghost)Saint.getCommandManager().getCommandUsingName("ghost");
      String name;
      if (!message.contains("§7[§9Saint§7]§r ")) {
         for(Iterator var5 = this.contents.keySet().iterator(); var5.hasNext(); message = message.replaceAll("(?i)" + name, Matcher.quoteReplacement(color ? "§6" + (String)this.contents.get(name) + "§r" : (String)this.contents.get(name)))) {
            name = (String)var5.next();
            if (color) {
               color = !Ghost.shouldGhost;
            }
         }
      }

      return message;
   }

   public void setup() {
      this.contents = new HashMap();
   }
}
