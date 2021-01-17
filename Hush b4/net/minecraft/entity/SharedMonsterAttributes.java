// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import java.util.UUID;
import java.util.Collection;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.ai.attributes.IAttribute;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes
{
    private static final Logger logger;
    public static final IAttribute maxHealth;
    public static final IAttribute followRange;
    public static final IAttribute knockbackResistance;
    public static final IAttribute movementSpeed;
    public static final IAttribute attackDamage;
    
    static {
        logger = LogManager.getLogger();
        maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, 1024.0).setDescription("Max Health").setShouldWatch(true);
        followRange = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
        knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0).setDescription("Knockback Resistance");
        movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071, 0.0, 1024.0).setDescription("Movement Speed").setShouldWatch(true);
        attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, 2048.0);
    }
    
    public static NBTTagList writeBaseAttributeMapToNBT(final BaseAttributeMap p_111257_0_) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final IAttributeInstance iattributeinstance : p_111257_0_.getAllAttributes()) {
            nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
        }
        return nbttaglist;
    }
    
    private static NBTTagCompound writeAttributeInstanceToNBT(final IAttributeInstance p_111261_0_) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final IAttribute iattribute = p_111261_0_.getAttribute();
        nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
        nbttagcompound.setDouble("Base", p_111261_0_.getBaseValue());
        final Collection<AttributeModifier> collection = p_111261_0_.func_111122_c();
        if (collection != null && !collection.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final AttributeModifier attributemodifier : collection) {
                if (attributemodifier.isSaved()) {
                    nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
                }
            }
            nbttagcompound.setTag("Modifiers", nbttaglist);
        }
        return nbttagcompound;
    }
    
    private static NBTTagCompound writeAttributeModifierToNBT(final AttributeModifier p_111262_0_) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Name", p_111262_0_.getName());
        nbttagcompound.setDouble("Amount", p_111262_0_.getAmount());
        nbttagcompound.setInteger("Operation", p_111262_0_.getOperation());
        nbttagcompound.setLong("UUIDMost", p_111262_0_.getID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", p_111262_0_.getID().getLeastSignificantBits());
        return nbttagcompound;
    }
    
    public static void func_151475_a(final BaseAttributeMap p_151475_0_, final NBTTagList p_151475_1_) {
        for (int i = 0; i < p_151475_1_.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = p_151475_1_.getCompoundTagAt(i);
            final IAttributeInstance iattributeinstance = p_151475_0_.getAttributeInstanceByName(nbttagcompound.getString("Name"));
            if (iattributeinstance != null) {
                applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
            }
            else {
                SharedMonsterAttributes.logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
            }
        }
    }
    
    private static void applyModifiersToAttributeInstance(final IAttributeInstance p_111258_0_, final NBTTagCompound p_111258_1_) {
        p_111258_0_.setBaseValue(p_111258_1_.getDouble("Base"));
        if (p_111258_1_.hasKey("Modifiers", 9)) {
            final NBTTagList nbttaglist = p_111258_1_.getTagList("Modifiers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
                if (attributemodifier != null) {
                    final AttributeModifier attributemodifier2 = p_111258_0_.getModifier(attributemodifier.getID());
                    if (attributemodifier2 != null) {
                        p_111258_0_.removeModifier(attributemodifier2);
                    }
                    p_111258_0_.applyModifier(attributemodifier);
                }
            }
        }
    }
    
    public static AttributeModifier readAttributeModifierFromNBT(final NBTTagCompound p_111259_0_) {
        final UUID uuid = new UUID(p_111259_0_.getLong("UUIDMost"), p_111259_0_.getLong("UUIDLeast"));
        try {
            return new AttributeModifier(uuid, p_111259_0_.getString("Name"), p_111259_0_.getDouble("Amount"), p_111259_0_.getInteger("Operation"));
        }
        catch (Exception exception) {
            SharedMonsterAttributes.logger.warn("Unable to create attribute: " + exception.getMessage());
            return null;
        }
    }
}
