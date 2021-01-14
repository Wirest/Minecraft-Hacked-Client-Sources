package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.management.LowerStringMap;

public abstract class BaseAttributeMap {
   protected final Map attributes = Maps.newHashMap();
   protected final Map attributesByName = new LowerStringMap();
   protected final Multimap field_180377_c = HashMultimap.create();

   public IAttributeInstance getAttributeInstance(IAttribute attribute) {
      return (IAttributeInstance)this.attributes.get(attribute);
   }

   public IAttributeInstance getAttributeInstanceByName(String attributeName) {
      return (IAttributeInstance)this.attributesByName.get(attributeName);
   }

   public IAttributeInstance registerAttribute(IAttribute attribute) {
      if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName())) {
         throw new IllegalArgumentException("Attribute is already registered!");
      } else {
         IAttributeInstance iattributeinstance = this.func_180376_c(attribute);
         this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
         this.attributes.put(attribute, iattributeinstance);

         for(IAttribute iattribute = attribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
            this.field_180377_c.put(iattribute, attribute);
         }

         return iattributeinstance;
      }
   }

   protected abstract IAttributeInstance func_180376_c(IAttribute var1);

   public Collection getAllAttributes() {
      return this.attributesByName.values();
   }

   public void func_180794_a(IAttributeInstance p_180794_1_) {
   }

   public void removeAttributeModifiers(Multimap p_111148_1_) {
      Iterator var2 = p_111148_1_.entries().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
         if (iattributeinstance != null) {
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
         }
      }

   }

   public void applyAttributeModifiers(Multimap p_111147_1_) {
      Iterator var2 = p_111147_1_.entries().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
         if (iattributeinstance != null) {
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
            iattributeinstance.applyModifier((AttributeModifier)entry.getValue());
         }
      }

   }
}
