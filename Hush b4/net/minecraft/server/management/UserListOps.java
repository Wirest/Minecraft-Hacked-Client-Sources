// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.File;
import com.mojang.authlib.GameProfile;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry>
{
    public UserListOps(final File saveFile) {
        super(saveFile);
    }
    
    @Override
    protected UserListEntry<GameProfile> createEntry(final JsonObject entryData) {
        return new UserListOpsEntry(entryData);
    }
    
    @Override
    public String[] getKeys() {
        final String[] astring = new String[((UserList<K, UserListOpsEntry>)this).getValues().size()];
        int i = 0;
        for (final UserListOpsEntry userlistopsentry : ((UserList<K, UserListOpsEntry>)this).getValues().values()) {
            astring[i++] = userlistopsentry.getValue().getName();
        }
        return astring;
    }
    
    public boolean func_183026_b(final GameProfile p_183026_1_) {
        final UserListOpsEntry userlistopsentry = this.getEntry(p_183026_1_);
        return userlistopsentry != null && userlistopsentry.func_183024_b();
    }
    
    @Override
    protected String getObjectKey(final GameProfile obj) {
        return obj.getId().toString();
    }
    
    public GameProfile getGameProfileFromName(final String username) {
        for (final UserListOpsEntry userlistopsentry : ((UserList<K, UserListOpsEntry>)this).getValues().values()) {
            if (username.equalsIgnoreCase(userlistopsentry.getValue().getName())) {
                return userlistopsentry.getValue();
            }
        }
        return null;
    }
}
