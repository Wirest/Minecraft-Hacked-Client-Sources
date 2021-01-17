// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.File;
import com.mojang.authlib.GameProfile;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry>
{
    public UserListWhitelist(final File p_i1132_1_) {
        super(p_i1132_1_);
    }
    
    @Override
    protected UserListEntry<GameProfile> createEntry(final JsonObject entryData) {
        return new UserListWhitelistEntry(entryData);
    }
    
    @Override
    public String[] getKeys() {
        final String[] astring = new String[((UserList<K, UserListWhitelistEntry>)this).getValues().size()];
        int i = 0;
        for (final UserListWhitelistEntry userlistwhitelistentry : ((UserList<K, UserListWhitelistEntry>)this).getValues().values()) {
            astring[i++] = userlistwhitelistentry.getValue().getName();
        }
        return astring;
    }
    
    @Override
    protected String getObjectKey(final GameProfile obj) {
        return obj.getId().toString();
    }
    
    public GameProfile func_152706_a(final String p_152706_1_) {
        for (final UserListWhitelistEntry userlistwhitelistentry : ((UserList<K, UserListWhitelistEntry>)this).getValues().values()) {
            if (p_152706_1_.equalsIgnoreCase(userlistwhitelistentry.getValue().getName())) {
                return userlistwhitelistentry.getValue();
            }
        }
        return null;
    }
}
