/*    */ package net.minecraft.client.player.inventory;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.ILockableContainer;
/*    */ import net.minecraft.world.LockCode;
/*    */ 
/*    */ public class ContainerLocalMenu extends InventoryBasic implements ILockableContainer
/*    */ {
/*    */   private String guiID;
/* 16 */   private Map<Integer, Integer> field_174895_b = Maps.newHashMap();
/*    */   
/*    */   public ContainerLocalMenu(String id, IChatComponent title, int slotCount)
/*    */   {
/* 20 */     super(title, slotCount);
/* 21 */     this.guiID = id;
/*    */   }
/*    */   
/*    */   public int getField(int id)
/*    */   {
/* 26 */     return this.field_174895_b.containsKey(Integer.valueOf(id)) ? ((Integer)this.field_174895_b.get(Integer.valueOf(id))).intValue() : 0;
/*    */   }
/*    */   
/*    */   public void setField(int id, int value)
/*    */   {
/* 31 */     this.field_174895_b.put(Integer.valueOf(id), Integer.valueOf(value));
/*    */   }
/*    */   
/*    */   public int getFieldCount()
/*    */   {
/* 36 */     return this.field_174895_b.size();
/*    */   }
/*    */   
/*    */   public boolean isLocked()
/*    */   {
/* 41 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setLockCode(LockCode code) {}
/*    */   
/*    */ 
/*    */   public LockCode getLockCode()
/*    */   {
/* 50 */     return LockCode.EMPTY_CODE;
/*    */   }
/*    */   
/*    */   public String getGuiID()
/*    */   {
/* 55 */     return this.guiID;
/*    */   }
/*    */   
/*    */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*    */   {
/* 60 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\player\inventory\ContainerLocalMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */