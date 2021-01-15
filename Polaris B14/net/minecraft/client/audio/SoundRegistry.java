/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.RegistrySimple;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessorComposite>
/*    */ {
/*    */   private Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry;
/*    */   
/*    */   protected Map<ResourceLocation, SoundEventAccessorComposite> createUnderlyingMap()
/*    */   {
/* 14 */     this.soundRegistry = Maps.newHashMap();
/* 15 */     return this.soundRegistry;
/*    */   }
/*    */   
/*    */   public void registerSound(SoundEventAccessorComposite p_148762_1_)
/*    */   {
/* 20 */     putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void clearMap()
/*    */   {
/* 28 */     this.soundRegistry.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */