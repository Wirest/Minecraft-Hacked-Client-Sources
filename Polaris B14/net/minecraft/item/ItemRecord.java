/*    */ package net.minecraft.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.block.BlockJukebox;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.StatCollector;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemRecord extends Item
/*    */ {
/* 19 */   private static final Map<String, ItemRecord> RECORDS = ;
/*    */   
/*    */   public final String recordName;
/*    */   
/*    */ 
/*    */   protected ItemRecord(String name)
/*    */   {
/* 26 */     this.recordName = name;
/* 27 */     this.maxStackSize = 1;
/* 28 */     setCreativeTab(CreativeTabs.tabMisc);
/* 29 */     RECORDS.put("records." + name, this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 37 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*    */     
/* 39 */     if ((iblockstate.getBlock() == Blocks.jukebox) && (!((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue()))
/*    */     {
/* 41 */       if (worldIn.isRemote)
/*    */       {
/* 43 */         return true;
/*    */       }
/*    */       
/*    */ 
/* 47 */       ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, iblockstate, stack);
/* 48 */       worldIn.playAuxSFXAtEntity(null, 1005, pos, Item.getIdFromItem(this));
/* 49 */       stack.stackSize -= 1;
/* 50 */       playerIn.triggerAchievement(StatList.field_181740_X);
/* 51 */       return true;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 56 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*    */   {
/* 65 */     tooltip.add(getRecordNameLocal());
/*    */   }
/*    */   
/*    */   public String getRecordNameLocal()
/*    */   {
/* 70 */     return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public EnumRarity getRarity(ItemStack stack)
/*    */   {
/* 78 */     return EnumRarity.RARE;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ItemRecord getRecord(String name)
/*    */   {
/* 86 */     return (ItemRecord)RECORDS.get(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */