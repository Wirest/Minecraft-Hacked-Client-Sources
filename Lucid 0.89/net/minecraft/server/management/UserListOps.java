package net.minecraft.server.management;

import java.io.File;
import java.util.Iterator;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOps extends UserList
{

    public UserListOps(File saveFile)
    {
        super(saveFile);
    }

    @Override
	protected UserListEntry createEntry(JsonObject entryData)
    {
        return new UserListOpsEntry(entryData);
    }

    @Override
	public String[] getKeys()
    {
        String[] var1 = new String[this.getValues().size()];
        int var2 = 0;
        UserListOpsEntry var4;

        for (Iterator var3 = this.getValues().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.getValue()).getName())
        {
            var4 = (UserListOpsEntry)var3.next();
        }

        return var1;
    }

    /**
     * Utility method to get a GameProfile's UUID in string form
     *  
     * @param gameProfile The GameProfile to get the UUID from
     */
    protected String getUUIDString(GameProfile gameProfile)
    {
        return gameProfile.getId().toString();
    }

    /**
     * Gets the GameProfile of based on the provided username.
     *  
     * @param username The username to match to a GameProfile
     */
    public GameProfile getGameProfileFromName(String username)
    {
        Iterator var2 = this.getValues().values().iterator();
        UserListOpsEntry var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (UserListOpsEntry)var2.next();
        }
        while (!username.equalsIgnoreCase(((GameProfile)var3.getValue()).getName()));

        return (GameProfile)var3.getValue();
    }

    /**
     * Gets the key value for the given object
     */
    @Override
	protected String getObjectKey(Object obj)
    {
        return this.getUUIDString((GameProfile)obj);
    }
}
