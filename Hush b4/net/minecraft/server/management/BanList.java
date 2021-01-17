// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.net.SocketAddress;
import com.google.gson.JsonObject;
import java.io.File;

public class BanList extends UserList<String, IPBanEntry>
{
    public BanList(final File bansFile) {
        super(bansFile);
    }
    
    @Override
    protected UserListEntry<String> createEntry(final JsonObject entryData) {
        return new IPBanEntry(entryData);
    }
    
    public boolean isBanned(final SocketAddress address) {
        final String s = this.addressToString(address);
        return ((UserList<String, V>)this).hasEntry(s);
    }
    
    public IPBanEntry getBanEntry(final SocketAddress address) {
        final String s = this.addressToString(address);
        return this.getEntry(s);
    }
    
    private String addressToString(final SocketAddress address) {
        String s = address.toString();
        if (s.contains("/")) {
            s = s.substring(s.indexOf(47) + 1);
        }
        if (s.contains(":")) {
            s = s.substring(0, s.indexOf(58));
        }
        return s;
    }
}
