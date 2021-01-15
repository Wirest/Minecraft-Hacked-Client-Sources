package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListWhitelist extends UserList {
   private static final String __OBFID = "CL_00001871";

   public UserListWhitelist(File p_i1132_1_) {
      super(p_i1132_1_);
   }

   protected UserListEntry createEntry(JsonObject entryData) {
      return new UserListWhitelistEntry(entryData);
   }

   public String[] getKeys() {
      String[] var1 = new String[this.getValues().size()];
      int var2 = 0;

      UserListWhitelistEntry var4;
      for(Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.getValue()).getName()) {
         var4 = (UserListWhitelistEntry)var3.next();
      }

      return var1;
   }

   protected String func_152704_b(GameProfile p_152704_1_) {
      return p_152704_1_.getId().toString();
   }

   public GameProfile func_152706_a(String p_152706_1_) {
      Iterator var2 = this.getValues().values().iterator();

      while(var2.hasNext()) {
         UserListWhitelistEntry var3 = (UserListWhitelistEntry)var2.next();
         if (p_152706_1_.equalsIgnoreCase(((GameProfile)var3.getValue()).getName())) {
            return (GameProfile)var3.getValue();
         }
      }

      return null;
   }

   protected String getObjectKey(Object obj) {
      return this.func_152704_b((GameProfile)obj);
   }
}
