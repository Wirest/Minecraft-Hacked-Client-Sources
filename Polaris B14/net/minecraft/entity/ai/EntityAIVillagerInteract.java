/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIVillagerInteract extends EntityAIWatchClosest2
/*    */ {
/*    */   private int interactionDelay;
/*    */   private EntityVillager villager;
/*    */   
/*    */   public EntityAIVillagerInteract(EntityVillager villagerIn)
/*    */   {
/* 19 */     super(villagerIn, EntityVillager.class, 3.0F, 0.02F);
/* 20 */     this.villager = villagerIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void startExecuting()
/*    */   {
/* 28 */     super.startExecuting();
/*    */     
/* 30 */     if ((this.villager.canAbondonItems()) && ((this.closestEntity instanceof EntityVillager)) && (((EntityVillager)this.closestEntity).func_175557_cr()))
/*    */     {
/* 32 */       this.interactionDelay = 10;
/*    */     }
/*    */     else
/*    */     {
/* 36 */       this.interactionDelay = 0;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void updateTask()
/*    */   {
/* 45 */     super.updateTask();
/*    */     
/* 47 */     if (this.interactionDelay > 0)
/*    */     {
/* 49 */       this.interactionDelay -= 1;
/*    */       
/* 51 */       if (this.interactionDelay == 0)
/*    */       {
/* 53 */         InventoryBasic inventorybasic = this.villager.getVillagerInventory();
/*    */         
/* 55 */         for (int i = 0; i < inventorybasic.getSizeInventory(); i++)
/*    */         {
/* 57 */           ItemStack itemstack = inventorybasic.getStackInSlot(i);
/* 58 */           ItemStack itemstack1 = null;
/*    */           
/* 60 */           if (itemstack != null)
/*    */           {
/* 62 */             Item item = itemstack.getItem();
/*    */             
/* 64 */             if (((item == Items.bread) || (item == Items.potato) || (item == Items.carrot)) && (itemstack.stackSize > 3))
/*    */             {
/* 66 */               int l = itemstack.stackSize / 2;
/* 67 */               itemstack.stackSize -= l;
/* 68 */               itemstack1 = new ItemStack(item, l, itemstack.getMetadata());
/*    */             }
/* 70 */             else if ((item == Items.wheat) && (itemstack.stackSize > 5))
/*    */             {
/* 72 */               int j = itemstack.stackSize / 2 / 3 * 3;
/* 73 */               int k = j / 3;
/* 74 */               itemstack.stackSize -= j;
/* 75 */               itemstack1 = new ItemStack(Items.bread, k, 0);
/*    */             }
/*    */             
/* 78 */             if (itemstack.stackSize <= 0)
/*    */             {
/* 80 */               inventorybasic.setInventorySlotContents(i, null);
/*    */             }
/*    */           }
/*    */           
/* 84 */           if (itemstack1 != null)
/*    */           {
/* 86 */             double d0 = this.villager.posY - 0.30000001192092896D + this.villager.getEyeHeight();
/* 87 */             EntityItem entityitem = new EntityItem(this.villager.worldObj, this.villager.posX, d0, this.villager.posZ, itemstack1);
/* 88 */             float f = 0.3F;
/* 89 */             float f1 = this.villager.rotationYawHead;
/* 90 */             float f2 = this.villager.rotationPitch;
/* 91 */             entityitem.motionX = (-MathHelper.sin(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
/* 92 */             entityitem.motionZ = (MathHelper.cos(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F) * f);
/* 93 */             entityitem.motionY = (-MathHelper.sin(f2 / 180.0F * 3.1415927F) * f + 0.1F);
/* 94 */             entityitem.setDefaultPickupDelay();
/* 95 */             this.villager.worldObj.spawnEntityInWorld(entityitem);
/* 96 */             break;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIVillagerInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */