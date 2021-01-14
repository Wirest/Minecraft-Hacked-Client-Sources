package net.minecraft.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes {
   private static final Logger logger = LogManager.getLogger();
   public static final IAttribute maxHealth = (new RangedAttribute((IAttribute)null, "generic.maxHealth", 20.0D, 0.0D, 1024.0D)).setDescription("Max Health").setShouldWatch(true);
   public static final IAttribute followRange = (new RangedAttribute((IAttribute)null, "generic.followRange", 32.0D, 0.0D, 2048.0D)).setDescription("Follow Range");
   public static final IAttribute knockbackResistance = (new RangedAttribute((IAttribute)null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).setDescription("Knockback Resistance");
   public static final IAttribute movementSpeed = (new RangedAttribute((IAttribute)null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 1024.0D)).setDescription("Movement Speed").setShouldWatch(true);
   public static final IAttribute attackDamage = new RangedAttribute((IAttribute)null, "generic.attackDamage", 2.0D, 0.0D, 2048.0D);

   public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap p_111257_0_) {
      NBTTagList nbttaglist = new NBTTagList();
      Iterator var2 = p_111257_0_.getAllAttributes().iterator();

      while(var2.hasNext()) {
         IAttributeInstance iattributeinstance = (IAttributeInstance)var2.next();
         nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
      }

      return nbttaglist;
   }

   private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance p_111261_0_) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      IAttribute iattribute = p_111261_0_.getAttribute();
      nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
      nbttagcompound.setDouble("Base", p_111261_0_.getBaseValue());
      Collection collection = p_111261_0_.func_111122_c();
      if (collection != null && !collection.isEmpty()) {
         NBTTagList nbttaglist = new NBTTagList();
         Iterator var5 = collection.iterator();

         while(var5.hasNext()) {
            AttributeModifier attributemodifier = (AttributeModifier)var5.next();
            if (attributemodifier.isSaved()) {
               nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
            }
         }

         nbttagcompound.setTag("Modifiers", nbttaglist);
      }

      return nbttagcompound;
   }

   private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier p_111262_0_) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setString("Name", p_111262_0_.getName());
      nbttagcompound.setDouble("Amount", p_111262_0_.getAmount());
      nbttagcompound.setInteger("Operation", p_111262_0_.getOperation());
      nbttagcompound.setLong("UUIDMost", p_111262_0_.getID().getMostSignificantBits());
      nbttagcompound.setLong("UUIDLeast", p_111262_0_.getID().getLeastSignificantBits());
      return nbttagcompound;
   }

   public static void func_151475_a(BaseAttributeMap p_151475_0_, NBTTagList p_151475_1_) {
      for(int i = 0; i < p_151475_1_.tagCount(); ++i) {
         NBTTagCompound nbttagcompound = p_151475_1_.getCompoundTagAt(i);
         IAttributeInstance iattributeinstance = p_151475_0_.getAttributeInstanceByName(nbttagcompound.getString("Name"));
         if (iattributeinstance != null) {
            applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
         } else {
            logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
         }
      }

   }

   private static void applyModifiersToAttributeInstance(IAttributeInstance p_111258_0_, NBTTagCompound p_111258_1_) {
      p_111258_0_.setBaseValue(p_111258_1_.getDouble("Base"));
      if (p_111258_1_.hasKey("Modifiers", 9)) {
         NBTTagList nbttaglist = p_111258_1_.getTagList("Modifiers", 10);

         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
            if (attributemodifier != null) {
               AttributeModifier attributemodifier1 = p_111258_0_.getModifier(attributemodifier.getID());
               if (attributemodifier1 != null) {
                  p_111258_0_.removeModifier(attributemodifier1);
               }

               p_111258_0_.applyModifier(attributemodifier);
            }
         }
      }

   }

   public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound p_111259_0_) {
      UUID uuid = new UUID(p_111259_0_.getLong("UUIDMost"), p_111259_0_.getLong("UUIDLeast"));

      try {
         return new AttributeModifier(uuid, p_111259_0_.getString("Name"), p_111259_0_.getDouble("Amount"), p_111259_0_.getInteger("Operation"));
      } catch (Exception var3) {
         logger.warn("Unable to create attribute: " + var3.getMessage());
         return null;
      }
   }
}
