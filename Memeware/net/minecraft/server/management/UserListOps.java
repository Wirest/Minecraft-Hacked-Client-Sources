package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList {
    private static final String __OBFID = "CL_00001879";

    public UserListOps(File p_i1152_1_) {
        super(p_i1152_1_);
    }

    protected UserListEntry createEntry(JsonObject entryData) {
        return new UserListOpsEntry(entryData);
    }

    public String[] getKeys() {
        String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        UserListOpsEntry var4;

        for (Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile) var4.getValue()).getName()) {
            var4 = (UserListOpsEntry) var3.next();
        }

        return var1;
    }

    protected String func_152699_b(GameProfile p_152699_1_) {
        return p_152699_1_.getId().toString();
    }

    /**
     * Gets the GameProfile of based on the provided username.
     */
    public GameProfile getGameProfileFromName(String p_152700_1_) {
        Iterator var2 = this.getValues().values().iterator();
        UserListOpsEntry var3;

        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (UserListOpsEntry) var2.next();
        }
        while (!p_152700_1_.equalsIgnoreCase(((GameProfile) var3.getValue()).getName()));

        return (GameProfile) var3.getValue();
    }

    /**
     * Gets the key value for the given object
     */
    protected String getObjectKey(Object obj) {
        return this.func_152699_b((GameProfile) obj);
    }
}
