package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListWhitelist extends UserList {
   public UserListWhitelist(File p_i1132_1_) {
      super(p_i1132_1_);
   }

   protected UserListEntry createEntry(JsonObject entryData) {
      return new UserListWhitelistEntry(entryData);
   }

   public String[] getKeys() {
      String[] astring = new String[this.getValues().size()];
      int i = 0;

      UserListWhitelistEntry userlistwhitelistentry;
      for(Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); astring[i++] = ((GameProfile)userlistwhitelistentry.getValue()).getName()) {
         userlistwhitelistentry = (UserListWhitelistEntry)var3.next();
      }

      return astring;
   }

   protected String getObjectKey(GameProfile obj) {
      return obj.getId().toString();
   }

   public GameProfile func_152706_a(String p_152706_1_) {
      Iterator var2 = this.getValues().values().iterator();

      UserListWhitelistEntry userlistwhitelistentry;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         userlistwhitelistentry = (UserListWhitelistEntry)var2.next();
      } while(!p_152706_1_.equalsIgnoreCase(((GameProfile)userlistwhitelistentry.getValue()).getName()));

      return (GameProfile)userlistwhitelistentry.getValue();
   }
}
