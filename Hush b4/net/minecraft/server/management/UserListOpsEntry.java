// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOpsEntry extends UserListEntry<GameProfile>
{
    private final int field_152645_a;
    private final boolean field_183025_b;
    
    public UserListOpsEntry(final GameProfile p_i46492_1_, final int p_i46492_2_, final boolean p_i46492_3_) {
        super(p_i46492_1_);
        this.field_152645_a = p_i46492_2_;
        this.field_183025_b = p_i46492_3_;
    }
    
    public UserListOpsEntry(final JsonObject p_i1150_1_) {
        super(func_152643_b(p_i1150_1_), p_i1150_1_);
        this.field_152645_a = (p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0);
        this.field_183025_b = (p_i1150_1_.has("bypassesPlayerLimit") && p_i1150_1_.get("bypassesPlayerLimit").getAsBoolean());
    }
    
    public int getPermissionLevel() {
        return this.field_152645_a;
    }
    
    public boolean func_183024_b() {
        return this.field_183025_b;
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (this.getValue().getId() == null) ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
            data.addProperty("level", this.field_152645_a);
            data.addProperty("bypassesPlayerLimit", this.field_183025_b);
        }
    }
    
    private static GameProfile func_152643_b(final JsonObject p_152643_0_) {
        if (p_152643_0_.has("uuid") && p_152643_0_.has("name")) {
            final String s = p_152643_0_.get("uuid").getAsString();
            UUID uuid;
            try {
                uuid = UUID.fromString(s);
            }
            catch (Throwable var4) {
                return null;
            }
            return new GameProfile(uuid, p_152643_0_.get("name").getAsString());
        }
        return null;
    }
}
