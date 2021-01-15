/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import net.minecraft.world.WorldSettings.GameType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaveFormatComparator
/*    */   implements Comparable<SaveFormatComparator>
/*    */ {
/*    */   private final String fileName;
/*    */   private final String displayName;
/*    */   private final long lastTimePlayed;
/*    */   private final long sizeOnDisk;
/*    */   private final boolean requiresConversion;
/*    */   private final WorldSettings.GameType theEnumGameType;
/*    */   private final boolean hardcore;
/*    */   private final boolean cheatsEnabled;
/*    */   
/*    */   public SaveFormatComparator(String fileNameIn, String displayNameIn, long lastTimePlayedIn, long sizeOnDiskIn, WorldSettings.GameType theEnumGameTypeIn, boolean requiresConversionIn, boolean hardcoreIn, boolean cheatsEnabledIn)
/*    */   {
/* 23 */     this.fileName = fileNameIn;
/* 24 */     this.displayName = displayNameIn;
/* 25 */     this.lastTimePlayed = lastTimePlayedIn;
/* 26 */     this.sizeOnDisk = sizeOnDiskIn;
/* 27 */     this.theEnumGameType = theEnumGameTypeIn;
/* 28 */     this.requiresConversion = requiresConversionIn;
/* 29 */     this.hardcore = hardcoreIn;
/* 30 */     this.cheatsEnabled = cheatsEnabledIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getFileName()
/*    */   {
/* 38 */     return this.fileName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDisplayName()
/*    */   {
/* 46 */     return this.displayName;
/*    */   }
/*    */   
/*    */   public long getSizeOnDisk()
/*    */   {
/* 51 */     return this.sizeOnDisk;
/*    */   }
/*    */   
/*    */   public boolean requiresConversion()
/*    */   {
/* 56 */     return this.requiresConversion;
/*    */   }
/*    */   
/*    */   public long getLastTimePlayed()
/*    */   {
/* 61 */     return this.lastTimePlayed;
/*    */   }
/*    */   
/*    */   public int compareTo(SaveFormatComparator p_compareTo_1_)
/*    */   {
/* 66 */     return this.lastTimePlayed > p_compareTo_1_.lastTimePlayed ? -1 : this.lastTimePlayed < p_compareTo_1_.lastTimePlayed ? 1 : this.fileName.compareTo(p_compareTo_1_.fileName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public WorldSettings.GameType getEnumGameType()
/*    */   {
/* 74 */     return this.theEnumGameType;
/*    */   }
/*    */   
/*    */   public boolean isHardcoreModeEnabled()
/*    */   {
/* 79 */     return this.hardcore;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean getCheatsEnabled()
/*    */   {
/* 87 */     return this.cheatsEnabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\SaveFormatComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */