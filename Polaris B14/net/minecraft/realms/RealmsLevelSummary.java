/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import net.minecraft.world.storage.SaveFormatComparator;
/*    */ 
/*    */ public class RealmsLevelSummary implements Comparable<RealmsLevelSummary>
/*    */ {
/*    */   private SaveFormatComparator levelSummary;
/*    */   
/*    */   public RealmsLevelSummary(SaveFormatComparator p_i1109_1_)
/*    */   {
/* 11 */     this.levelSummary = p_i1109_1_;
/*    */   }
/*    */   
/*    */   public int getGameMode()
/*    */   {
/* 16 */     return this.levelSummary.getEnumGameType().getID();
/*    */   }
/*    */   
/*    */   public String getLevelId()
/*    */   {
/* 21 */     return this.levelSummary.getFileName();
/*    */   }
/*    */   
/*    */   public boolean hasCheats()
/*    */   {
/* 26 */     return this.levelSummary.getCheatsEnabled();
/*    */   }
/*    */   
/*    */   public boolean isHardcore()
/*    */   {
/* 31 */     return this.levelSummary.isHardcoreModeEnabled();
/*    */   }
/*    */   
/*    */   public boolean isRequiresConversion()
/*    */   {
/* 36 */     return this.levelSummary.requiresConversion();
/*    */   }
/*    */   
/*    */   public String getLevelName()
/*    */   {
/* 41 */     return this.levelSummary.getDisplayName();
/*    */   }
/*    */   
/*    */   public long getLastPlayed()
/*    */   {
/* 46 */     return this.levelSummary.getLastTimePlayed();
/*    */   }
/*    */   
/*    */   public int compareTo(SaveFormatComparator p_compareTo_1_)
/*    */   {
/* 51 */     return this.levelSummary.compareTo(p_compareTo_1_);
/*    */   }
/*    */   
/*    */   public long getSizeOnDisk()
/*    */   {
/* 56 */     return this.levelSummary.getSizeOnDisk();
/*    */   }
/*    */   
/*    */   public int compareTo(RealmsLevelSummary p_compareTo_1_)
/*    */   {
/* 61 */     return this.levelSummary.getLastTimePlayed() > p_compareTo_1_.getLastPlayed() ? -1 : this.levelSummary.getLastTimePlayed() < p_compareTo_1_.getLastPlayed() ? 1 : this.levelSummary.getFileName().compareTo(p_compareTo_1_.getLevelId());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\RealmsLevelSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */