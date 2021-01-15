package net.minecraft.nbt;

import java.util.Iterator;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.util.StringUtils;

public final class NBTUtil
{

    /**
     * Reads and returns a GameProfile that has been saved to the passed in NBTTagCompound
     */
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound)
    {
        String var1 = null;
        String var2 = null;

        if (compound.hasKey("Name", 8))
        {
            var1 = compound.getString("Name");
        }

        if (compound.hasKey("Id", 8))
        {
            var2 = compound.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(var1) && StringUtils.isNullOrEmpty(var2))
        {
            return null;
        }
        else
        {
            UUID var3;

            try
            {
                var3 = UUID.fromString(var2);
            }
            catch (Throwable var12)
            {
                var3 = null;
            }

            GameProfile var4 = new GameProfile(var3, var1);

            if (compound.hasKey("Properties", 10))
            {
                NBTTagCompound var5 = compound.getCompoundTag("Properties");
                Iterator var6 = var5.getKeySet().iterator();

                while (var6.hasNext())
                {
                    String var7 = (String)var6.next();
                    NBTTagList var8 = var5.getTagList(var7, 10);

                    for (int var9 = 0; var9 < var8.tagCount(); ++var9)
                    {
                        NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                        String var11 = var10.getString("Value");

                        if (var10.hasKey("Signature", 8))
                        {
                            var4.getProperties().put(var7, new Property(var7, var11, var10.getString("Signature")));
                        }
                        else
                        {
                            var4.getProperties().put(var7, new Property(var7, var11));
                        }
                    }
                }
            }

            return var4;
        }
    }

    /**
     * Writes a GameProfile to an NBTTagCompound.
     *  
     * @param tagCompound The NBTTagCompound to write the GameProfile to.
     * @param profile The game profile you are saving.
     */
    public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile)
    {
        if (!StringUtils.isNullOrEmpty(profile.getName()))
        {
            tagCompound.setString("Name", profile.getName());
        }

        if (profile.getId() != null)
        {
            tagCompound.setString("Id", profile.getId().toString());
        }

        if (!profile.getProperties().isEmpty())
        {
            NBTTagCompound var2 = new NBTTagCompound();
            Iterator var3 = profile.getProperties().keySet().iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                NBTTagList var5 = new NBTTagList();
                NBTTagCompound var8;

                for (Iterator var6 = profile.getProperties().get(var4).iterator(); var6.hasNext(); var5.appendTag(var8))
                {
                    Property var7 = (Property)var6.next();
                    var8 = new NBTTagCompound();
                    var8.setString("Value", var7.getValue());

                    if (var7.hasSignature())
                    {
                        var8.setString("Signature", var7.getSignature());
                    }
                }

                var2.setTag(var4, var5);
            }

            tagCompound.setTag("Properties", var2);
        }

        return tagCompound;
    }
}
