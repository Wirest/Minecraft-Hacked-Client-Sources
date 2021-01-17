// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.File;
import com.mojang.authlib.GameProfile;

public class UserListBans extends UserList<GameProfile, UserListBansEntry>
{
    public UserListBans(final File bansFile) {
        super(bansFile);
    }
    
    @Override
    protected UserListEntry<GameProfile> createEntry(final JsonObject entryData) {
        return new UserListBansEntry(entryData);
    }
    
    public boolean isBanned(final GameProfile profile) {
        return ((UserList<GameProfile, V>)this).hasEntry(profile);
    }
    
    @Override
    public String[] getKeys() {
        final String[] astring = new String[((UserList<K, UserListBansEntry>)this).getValues().size()];
        int i = 0;
        for (final UserListBansEntry userlistbansentry : ((UserList<K, UserListBansEntry>)this).getValues().values()) {
            astring[i++] = userlistbansentry.getValue().getName();
        }
        return astring;
    }
    
    @Override
    protected String getObjectKey(final GameProfile obj) {
        return obj.getId().toString();
    }
    
    public GameProfile isUsernameBanned(final String username) {
        for (final UserListBansEntry userlistbansentry : ((UserList<K, UserListBansEntry>)this).getValues().values()) {
            if (username.equalsIgnoreCase(userlistbansentry.getValue().getName())) {
                return userlistbansentry.getValue();
            }
        }
        return null;
    }
}
