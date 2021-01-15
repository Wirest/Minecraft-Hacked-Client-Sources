/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ public class SoundEventAccessor implements ISoundEventAccessor<SoundPoolEntry>
/*    */ {
/*    */   private final SoundPoolEntry entry;
/*    */   private final int weight;
/*    */   
/*    */   SoundEventAccessor(SoundPoolEntry entry, int weight)
/*    */   {
/* 10 */     this.entry = entry;
/* 11 */     this.weight = weight;
/*    */   }
/*    */   
/*    */   public int getWeight()
/*    */   {
/* 16 */     return this.weight;
/*    */   }
/*    */   
/*    */   public SoundPoolEntry cloneEntry()
/*    */   {
/* 21 */     return new SoundPoolEntry(this.entry);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundEventAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */