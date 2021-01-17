// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import com.google.common.collect.ForwardingMultimap;
import java.util.Iterator;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import com.mojang.authlib.GameProfile;

public final class NBTUtil
{
    public static GameProfile readGameProfileFromNBT(final NBTTagCompound compound) {
        String s = null;
        String s2 = null;
        if (compound.hasKey("Name", 8)) {
            s = compound.getString("Name");
        }
        if (compound.hasKey("Id", 8)) {
            s2 = compound.getString("Id");
        }
        if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s2)) {
            return null;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(s2);
        }
        catch (Throwable var12) {
            uuid = null;
        }
        final GameProfile gameprofile = new GameProfile(uuid, s);
        if (compound.hasKey("Properties", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
            for (final String s3 : nbttagcompound.getKeySet()) {
                final NBTTagList nbttaglist = nbttagcompound.getTagList(s3, 10);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                    final String s4 = nbttagcompound2.getString("Value");
                    if (nbttagcompound2.hasKey("Signature", 8)) {
                        gameprofile.getProperties().put(s3, new Property(s3, s4, nbttagcompound2.getString("Signature")));
                    }
                    else {
                        gameprofile.getProperties().put(s3, new Property(s3, s4));
                    }
                }
            }
        }
        return gameprofile;
    }
    
    public static NBTTagCompound writeGameProfile(final NBTTagCompound tagCompound, final GameProfile profile) {
        if (!StringUtils.isNullOrEmpty(profile.getName())) {
            tagCompound.setString("Name", profile.getName());
        }
        if (profile.getId() != null) {
            tagCompound.setString("Id", profile.getId().toString());
        }
        if (!profile.getProperties().isEmpty()) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (final String s : ((ForwardingMultimap<String, V>)profile.getProperties()).keySet()) {
                final NBTTagList nbttaglist = new NBTTagList();
                for (final Property property : profile.getProperties().get(s)) {
                    final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                    nbttagcompound2.setString("Value", property.getValue());
                    if (property.hasSignature()) {
                        nbttagcompound2.setString("Signature", property.getSignature());
                    }
                    nbttaglist.appendTag(nbttagcompound2);
                }
                nbttagcompound.setTag(s, nbttaglist);
            }
            tagCompound.setTag("Properties", nbttagcompound);
        }
        return tagCompound;
    }
    
    public static boolean func_181123_a(final NBTBase p_181123_0_, final NBTBase p_181123_1_, final boolean p_181123_2_) {
        if (p_181123_0_ == p_181123_1_) {
            return true;
        }
        if (p_181123_0_ == null) {
            return true;
        }
        if (p_181123_1_ == null) {
            return false;
        }
        if (!p_181123_0_.getClass().equals(p_181123_1_.getClass())) {
            return false;
        }
        if (p_181123_0_ instanceof NBTTagCompound) {
            final NBTTagCompound nbttagcompound = (NBTTagCompound)p_181123_0_;
            final NBTTagCompound nbttagcompound2 = (NBTTagCompound)p_181123_1_;
            for (final String s : nbttagcompound.getKeySet()) {
                final NBTBase nbtbase1 = nbttagcompound.getTag(s);
                if (!func_181123_a(nbtbase1, nbttagcompound2.getTag(s), p_181123_2_)) {
                    return false;
                }
            }
            return true;
        }
        if (!(p_181123_0_ instanceof NBTTagList) || !p_181123_2_) {
            return p_181123_0_.equals(p_181123_1_);
        }
        final NBTTagList nbttaglist = (NBTTagList)p_181123_0_;
        final NBTTagList nbttaglist2 = (NBTTagList)p_181123_1_;
        if (nbttaglist.tagCount() == 0) {
            return nbttaglist2.tagCount() == 0;
        }
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTBase nbtbase2 = nbttaglist.get(i);
            boolean flag = false;
            for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
                if (func_181123_a(nbtbase2, nbttaglist2.get(j), p_181123_2_)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
}
