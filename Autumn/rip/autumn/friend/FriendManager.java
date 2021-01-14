package rip.autumn.friend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class FriendManager {
   private static final List friends = new ArrayList();

   public void add(String name) {
      this.add(name, name);
   }

   public void add(String name, String alias) {
      friends.add(new Friend(name, alias));
   }

   public static boolean isFriend(String name) {
      Iterator var1 = friends.iterator();

      Friend friend;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         friend = (Friend)var1.next();
      } while(!friend.getUsername().equals(name));

      return true;
   }
}
