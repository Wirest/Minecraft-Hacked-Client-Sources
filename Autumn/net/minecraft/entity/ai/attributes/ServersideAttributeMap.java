package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap extends BaseAttributeMap {
   private final Set attributeInstanceSet = Sets.newHashSet();
   protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();

   public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute) {
      return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
   }

   public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName) {
      IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
      if (iattributeinstance == null) {
         iattributeinstance = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(attributeName);
      }

      return (ModifiableAttributeInstance)iattributeinstance;
   }

   public IAttributeInstance registerAttribute(IAttribute attribute) {
      IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
      if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
         this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
      }

      return iattributeinstance;
   }

   protected IAttributeInstance func_180376_c(IAttribute p_180376_1_) {
      return new ModifiableAttributeInstance(this, p_180376_1_);
   }

   public void func_180794_a(IAttributeInstance p_180794_1_) {
      if (p_180794_1_.getAttribute().getShouldWatch()) {
         this.attributeInstanceSet.add(p_180794_1_);
      }

      Iterator var2 = this.field_180377_c.get(p_180794_1_.getAttribute()).iterator();

      while(var2.hasNext()) {
         IAttribute iattribute = (IAttribute)var2.next();
         ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
         if (modifiableattributeinstance != null) {
            modifiableattributeinstance.flagForUpdate();
         }
      }

   }

   public Set getAttributeInstanceSet() {
      return this.attributeInstanceSet;
   }

   public Collection getWatchedAttributes() {
      Set set = Sets.newHashSet();
      Iterator var2 = this.getAllAttributes().iterator();

      while(var2.hasNext()) {
         IAttributeInstance iattributeinstance = (IAttributeInstance)var2.next();
         if (iattributeinstance.getAttribute().getShouldWatch()) {
            set.add(iattributeinstance);
         }
      }

      return set;
   }
}
