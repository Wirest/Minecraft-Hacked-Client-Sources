/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemMapBase
/*    */   extends Item
/*    */ {
/*    */   public boolean isMap()
/*    */   {
/* 14 */     return true;
/*    */   }
/*    */   
/*    */   public Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player)
/*    */   {
/* 19 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemMapBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */