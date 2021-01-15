package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList {
   private static final String __OBFID = "CL_00001873";

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
      String[] var1 = new String[this.getValues().size()];
      int var2 = 0;

      UserListBansEntry var4;
      for(Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.getValue()).getName()) {
         var4 = (UserListBansEntry)var3.next();
      }

      return var1;
   }

   protected String getProfileId(GameProfile profile) {
      return profile.getId().toString();
   }

   public GameProfile isUsernameBanned(String username) {
      Iterator var2 = this.getValues().values().iterator();

      while(var2.hasNext()) {
         UserListBansEntry var3 = (UserListBansEntry)var2.next();
         if (username.equalsIgnoreCase(((GameProfile)var3.getValue()).getName())) {
            return (GameProfile)var3.getValue();
         }
      }

      return null;
   }

   protected String getObjectKey(Object obj) {
      return this.getProfileId((GameProfile)obj);
   }
}
