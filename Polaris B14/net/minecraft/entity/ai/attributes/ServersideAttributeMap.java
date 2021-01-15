/*    */ package net.minecraft.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.management.LowerStringMap;
/*    */ 
/*    */ public class ServersideAttributeMap extends BaseAttributeMap
/*    */ {
/* 11 */   private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
/* 12 */   protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap();
/*    */   
/*    */   public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute)
/*    */   {
/* 16 */     return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
/*    */   }
/*    */   
/*    */   public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName)
/*    */   {
/* 21 */     IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
/*    */     
/* 23 */     if (iattributeinstance == null)
/*    */     {
/* 25 */       iattributeinstance = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(attributeName);
/*    */     }
/*    */     
/* 28 */     return (ModifiableAttributeInstance)iattributeinstance;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IAttributeInstance registerAttribute(IAttribute attribute)
/*    */   {
/* 36 */     IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
/*    */     
/* 38 */     if (((attribute instanceof RangedAttribute)) && (((RangedAttribute)attribute).getDescription() != null))
/*    */     {
/* 40 */       this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
/*    */     }
/*    */     
/* 43 */     return iattributeinstance;
/*    */   }
/*    */   
/*    */   protected IAttributeInstance func_180376_c(IAttribute p_180376_1_)
/*    */   {
/* 48 */     return new ModifiableAttributeInstance(this, p_180376_1_);
/*    */   }
/*    */   
/*    */   public void func_180794_a(IAttributeInstance p_180794_1_)
/*    */   {
/* 53 */     if (p_180794_1_.getAttribute().getShouldWatch())
/*    */     {
/* 55 */       this.attributeInstanceSet.add(p_180794_1_);
/*    */     }
/*    */     
/* 58 */     for (IAttribute iattribute : this.field_180377_c.get(p_180794_1_.getAttribute()))
/*    */     {
/* 60 */       ModifiableAttributeInstance modifiableattributeinstance = getAttributeInstance(iattribute);
/*    */       
/* 62 */       if (modifiableattributeinstance != null)
/*    */       {
/* 64 */         modifiableattributeinstance.flagForUpdate();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Set<IAttributeInstance> getAttributeInstanceSet()
/*    */   {
/* 71 */     return this.attributeInstanceSet;
/*    */   }
/*    */   
/*    */   public java.util.Collection<IAttributeInstance> getWatchedAttributes()
/*    */   {
/* 76 */     Set<IAttributeInstance> set = Sets.newHashSet();
/*    */     
/* 78 */     for (IAttributeInstance iattributeinstance : getAllAttributes())
/*    */     {
/* 80 */       if (iattributeinstance.getAttribute().getShouldWatch())
/*    */       {
/* 82 */         set.add(iattributeinstance);
/*    */       }
/*    */     }
/*    */     
/* 86 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\ServersideAttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */