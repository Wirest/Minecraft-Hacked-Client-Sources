/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.AnvilConverterException;
/*    */ import net.minecraft.util.IProgressUpdate;
/*    */ import net.minecraft.world.storage.ISaveFormat;
/*    */ import net.minecraft.world.storage.SaveFormatComparator;
/*    */ 
/*    */ public class RealmsAnvilLevelStorageSource
/*    */ {
/*    */   private ISaveFormat levelStorageSource;
/*    */   
/*    */   public RealmsAnvilLevelStorageSource(ISaveFormat p_i1106_1_)
/*    */   {
/* 16 */     this.levelStorageSource = p_i1106_1_;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 21 */     return this.levelStorageSource.getName();
/*    */   }
/*    */   
/*    */   public boolean levelExists(String p_levelExists_1_)
/*    */   {
/* 26 */     return this.levelStorageSource.canLoadWorld(p_levelExists_1_);
/*    */   }
/*    */   
/*    */   public boolean convertLevel(String p_convertLevel_1_, IProgressUpdate p_convertLevel_2_)
/*    */   {
/* 31 */     return this.levelStorageSource.convertMapFormat(p_convertLevel_1_, p_convertLevel_2_);
/*    */   }
/*    */   
/*    */   public boolean requiresConversion(String p_requiresConversion_1_)
/*    */   {
/* 36 */     return this.levelStorageSource.isOldMapFormat(p_requiresConversion_1_);
/*    */   }
/*    */   
/*    */   public boolean isNewLevelIdAcceptable(String p_isNewLevelIdAcceptable_1_)
/*    */   {
/* 41 */     return this.levelStorageSource.func_154335_d(p_isNewLevelIdAcceptable_1_);
/*    */   }
/*    */   
/*    */   public boolean deleteLevel(String p_deleteLevel_1_)
/*    */   {
/* 46 */     return this.levelStorageSource.deleteWorldDirectory(p_deleteLevel_1_);
/*    */   }
/*    */   
/*    */   public boolean isConvertible(String p_isConvertible_1_)
/*    */   {
/* 51 */     return this.levelStorageSource.func_154334_a(p_isConvertible_1_);
/*    */   }
/*    */   
/*    */   public void renameLevel(String p_renameLevel_1_, String p_renameLevel_2_)
/*    */   {
/* 56 */     this.levelStorageSource.renameWorld(p_renameLevel_1_, p_renameLevel_2_);
/*    */   }
/*    */   
/*    */   public void clearAll()
/*    */   {
/* 61 */     this.levelStorageSource.flushCache();
/*    */   }
/*    */   
/*    */   public List<RealmsLevelSummary> getLevelList() throws AnvilConverterException
/*    */   {
/* 66 */     List<RealmsLevelSummary> list = Lists.newArrayList();
/*    */     
/* 68 */     for (SaveFormatComparator saveformatcomparator : this.levelStorageSource.getSaveList())
/*    */     {
/* 70 */       list.add(new RealmsLevelSummary(saveformatcomparator));
/*    */     }
/*    */     
/* 73 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\RealmsAnvilLevelStorageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */