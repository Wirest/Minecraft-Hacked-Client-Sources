// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T>
{
    private final T value;
    
    public UserListEntry(final T p_i1146_1_) {
        this.value = p_i1146_1_;
    }
    
    protected UserListEntry(final T p_i1147_1_, final JsonObject p_i1147_2_) {
        this.value = p_i1147_1_;
    }
    
    T getValue() {
        return this.value;
    }
    
    boolean hasBanExpired() {
        return false;
    }
    
    protected void onSerialization(final JsonObject data) {
    }
}
