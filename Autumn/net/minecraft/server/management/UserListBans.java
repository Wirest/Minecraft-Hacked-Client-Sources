package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList {
   public UserListBans(File bansFile) {
      super(bansFile);
   }

   protected UserListEntry createEntry(JsonObject entryData) {
      return new UserListBansEntry(entryData);
   }

   public boolean isBanned(GameProfile profile) {
      return this.hasEntry(profile);
   }

   public String[] getKeys() {
      String[] astring = new String[this.getValues().size()];
      int i = 0;

      UserListBansEntry userlistbansentry;
      for(Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); astring[i++] = ((GameProfile)userlistbansentry.getValue()).getName()) {
         userlistbansentry = (UserListBansEntry)var3.next();
      }

      return astring;
   }

   protected String getObjectKey(GameProfile obj) {
      return obj.getId().toString();
   }

   public GameProfile isUsernameBanned(String username) {
      Iterator var2 = this.getValues().values().iterator();

      UserListBansEntry userlistbansentry;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         userlistbansentry = (UserListBansEntry)var2.next();
      } while(!username.equalsIgnoreCase(((GameProfile)userlistbansentry.getValue()).getName()));

      return (GameProfile)userlistbansentry.getValue();
   }
}
