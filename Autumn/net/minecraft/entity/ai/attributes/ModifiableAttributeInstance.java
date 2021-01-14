package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModifiableAttributeInstance implements IAttributeInstance {
   private final BaseAttributeMap attributeMap;
   private final IAttribute genericAttribute;
   private final Map mapByOperation = Maps.newHashMap();
   private final Map mapByName = Maps.newHashMap();
   private final Map mapByUUID = Maps.newHashMap();
   private double baseValue;
   private boolean needsUpdate = true;
   private double cachedValue;

   public ModifiableAttributeInstance(BaseAttributeMap attributeMapIn, IAttribute genericAttributeIn) {
      this.attributeMap = attributeMapIn;
      this.genericAttribute = genericAttributeIn;
      this.baseValue = genericAttributeIn.getDefaultValue();

      for(int i = 0; i < 3; ++i) {
         this.mapByOperation.put(i, Sets.newHashSet());
      }

   }

   public IAttribute getAttribute() {
      return this.genericAttribute;
   }

   public double getBaseValue() {
      return this.baseValue;
   }

   public void setBaseValue(double baseValue) {
      if (baseValue != this.getBaseValue()) {
         this.baseValue = baseValue;
         this.flagForUpdate();
      }

   }

   public Collection getModifiersByOperation(int operation) {
      return (Collection)this.mapByOperation.get(operation);
   }

   public Collection func_111122_c() {
      Set set = Sets.newHashSet();

      for(int i = 0; i < 3; ++i) {
         set.addAll(this.getModifiersByOperation(i));
      }

      return set;
   }

   public AttributeModifier getModifier(UUID uuid) {
      return (AttributeModifier)this.mapByUUID.get(uuid);
   }

   public boolean hasModifier(AttributeModifier modifier) {
      return this.mapByUUID.get(modifier.getID()) != null;
   }

   public void applyModifier(AttributeModifier modifier) {
      if (this.getModifier(modifier.getID()) != null) {
         throw new IllegalArgumentException("Modifier is already applied on this attribute!");
      } else {
         Set set = (Set)this.mapByName.get(modifier.getName());
         if (set == null) {
            set = Sets.newHashSet();
            this.mapByName.put(modifier.getName(), set);
         }

         ((Set)this.mapByOperation.get(modifier.getOperation())).add(modifier);
         ((Set)set).add(modifier);
         this.mapByUUID.put(modifier.getID(), modifier);
         this.flagForUpdate();
      }
   }

   protected void flagForUpdate() {
      this.needsUpdate = true;
      this.attributeMap.func_180794_a(this);
   }

   public void removeModifier(AttributeModifier modifier) {
      for(int i = 0; i < 3; ++i) {
         Set set = (Set)this.mapByOperation.get(i);
         set.remove(modifier);
      }

      Set set1 = (Set)this.mapByName.get(modifier.getName());
      if (set1 != null) {
         set1.remove(modifier);
         if (set1.isEmpty()) {
            this.mapByName.remove(modifier.getName());
         }
      }

      this.mapByUUID.remove(modifier.getID());
      this.flagForUpdate();
   }

   public void removeAllModifiers() {
      Collection collection = this.func_111122_c();
      if (collection != null) {
         Iterator var2 = Lists.newArrayList(collection).iterator();

         while(var2.hasNext()) {
            AttributeModifier attributemodifier = (AttributeModifier)var2.next();
            this.removeModifier(attributemodifier);
         }
      }

   }

   public double getAttributeValue() {
      if (this.needsUpdate) {
         this.cachedValue = this.computeValue();
         this.needsUpdate = false;
      }

      return this.cachedValue;
   }

   private double computeValue() {
      double d0 = this.getBaseValue();

      AttributeModifier attributemodifier;
      for(Iterator var3 = this.func_180375_b(0).iterator(); var3.hasNext(); d0 += attributemodifier.getAmount()) {
         attributemodifier = (AttributeModifier)var3.next();
      }

      double d1 = d0;

      Iterator var5;
      AttributeModifier attributemodifier2;
      for(var5 = this.func_180375_b(1).iterator(); var5.hasNext(); d1 += d0 * attributemodifier2.getAmount()) {
         attributemodifier2 = (AttributeModifier)var5.next();
      }

      for(var5 = this.func_180375_b(2).iterator(); var5.hasNext(); d1 *= 1.0D + attributemodifier2.getAmount()) {
         attributemodifier2 = (AttributeModifier)var5.next();
      }

      return this.genericAttribute.clampValue(d1);
   }

   private Collection func_180375_b(int p_180375_1_) {
      Set set = Sets.newHashSet(this.getModifiersByOperation(p_180375_1_));

      for(IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
         IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
         if (iattributeinstance != null) {
            set.addAll(iattributeinstance.getModifiersByOperation(p_180375_1_));
         }
      }

      return set;
   }
}
