package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry {
    private final Object value;
    private static final String __OBFID = "CL_00001877";

    public UserListEntry(Object p_i1146_1_) {
        this.value = p_i1146_1_;
    }

    protected UserListEntry(Object p_i1147_1_, JsonObject p_i1147_2_) {
        this.value = p_i1147_1_;
    }

    Object getValue() {
        return this.value;
    }

    boolean hasBanExpired() {
        return false;
    }

    protected void onSerialization(JsonObject data) {
    }
}
