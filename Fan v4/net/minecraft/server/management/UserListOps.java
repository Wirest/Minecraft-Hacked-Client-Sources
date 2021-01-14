package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry>
{
    public UserListOps(File saveFile)
    {
        super(saveFile);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject entryData)
    {
        return new UserListOpsEntry(entryData);
    }

    public String[] getKeys()
    {
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        for (UserListOpsEntry userlistopsentry : this.getValues().values())
        {
            astring[i++] = userlistopsentry.getValue().getName();
        }

        return astring;
    }

    public boolean func_183026_b(GameProfile p_183026_1_)
    {
        UserListOpsEntry userlistopsentry = this.getEntry(p_183026_1_);
        return userlistopsentry != null ? userlistopsentry.func_183024_b() : false;
    }

    /**
     * Gets the key value for the given object
     */
    protected String getObjectKey(GameProfile obj)
    {
        return obj.getId().toString();
    }

    /**
     * Gets the GameProfile of based on the provided username.
     *  
     * @param username The username to match to a GameProfile
     */
    public GameProfile getGameProfileFromName(String username)
    {
        for (UserListOpsEntry userlistopsentry : this.getValues().values())
        {
            if (username.equalsIgnoreCase(userlistopsentry.getValue().getName()))
            {
                return userlistopsentry.getValue();
            }
        }

        return null;
    }
}
