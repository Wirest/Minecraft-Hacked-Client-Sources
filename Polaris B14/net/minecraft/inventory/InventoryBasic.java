/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class InventoryBasic implements IInventory
/*     */ {
/*     */   private String inventoryTitle;
/*     */   private int slotsCount;
/*     */   private ItemStack[] inventoryContents;
/*     */   private List<IInvBasic> field_70480_d;
/*     */   private boolean hasCustomName;
/*     */   
/*     */   public InventoryBasic(String title, boolean customName, int slotCount)
/*     */   {
/*  21 */     this.inventoryTitle = title;
/*  22 */     this.hasCustomName = customName;
/*  23 */     this.slotsCount = slotCount;
/*  24 */     this.inventoryContents = new ItemStack[slotCount];
/*     */   }
/*     */   
/*     */   public InventoryBasic(IChatComponent title, int slotCount)
/*     */   {
/*  29 */     this(title.getUnformattedText(), true, slotCount);
/*     */   }
/*     */   
/*     */   public void func_110134_a(IInvBasic p_110134_1_)
/*     */   {
/*  34 */     if (this.field_70480_d == null)
/*     */     {
/*  36 */       this.field_70480_d = Lists.newArrayList();
/*     */     }
/*     */     
/*  39 */     this.field_70480_d.add(p_110134_1_);
/*     */   }
/*     */   
/*     */   public void func_110132_b(IInvBasic p_110132_1_)
/*     */   {
/*  44 */     this.field_70480_d.remove(p_110132_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  52 */     return (index >= 0) && (index < this.inventoryContents.length) ? this.inventoryContents[index] : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/*  60 */     if (this.inventoryContents[index] != null)
/*     */     {
/*  62 */       if (this.inventoryContents[index].stackSize <= count)
/*     */       {
/*  64 */         ItemStack itemstack1 = this.inventoryContents[index];
/*  65 */         this.inventoryContents[index] = null;
/*  66 */         markDirty();
/*  67 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/*  71 */       ItemStack itemstack = this.inventoryContents[index].splitStack(count);
/*     */       
/*  73 */       if (this.inventoryContents[index].stackSize == 0)
/*     */       {
/*  75 */         this.inventoryContents[index] = null;
/*     */       }
/*     */       
/*  78 */       markDirty();
/*  79 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  84 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_174894_a(ItemStack stack)
/*     */   {
/*  90 */     ItemStack itemstack = stack.copy();
/*     */     
/*  92 */     for (int i = 0; i < this.slotsCount; i++)
/*     */     {
/*  94 */       ItemStack itemstack1 = getStackInSlot(i);
/*     */       
/*  96 */       if (itemstack1 == null)
/*     */       {
/*  98 */         setInventorySlotContents(i, itemstack);
/*  99 */         markDirty();
/* 100 */         return null;
/*     */       }
/*     */       
/* 103 */       if (ItemStack.areItemsEqual(itemstack1, itemstack))
/*     */       {
/* 105 */         int j = Math.min(getInventoryStackLimit(), itemstack1.getMaxStackSize());
/* 106 */         int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);
/*     */         
/* 108 */         if (k > 0)
/*     */         {
/* 110 */           itemstack1.stackSize += k;
/* 111 */           itemstack.stackSize -= k;
/*     */           
/* 113 */           if (itemstack.stackSize <= 0)
/*     */           {
/* 115 */             markDirty();
/* 116 */             return null;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     if (itemstack.stackSize != stack.stackSize)
/*     */     {
/* 124 */       markDirty();
/*     */     }
/*     */     
/* 127 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/* 135 */     if (this.inventoryContents[index] != null)
/*     */     {
/* 137 */       ItemStack itemstack = this.inventoryContents[index];
/* 138 */       this.inventoryContents[index] = null;
/* 139 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/* 143 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 152 */     this.inventoryContents[index] = stack;
/*     */     
/* 154 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 156 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 159 */     markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/* 167 */     return this.slotsCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 175 */     return this.inventoryTitle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 183 */     return this.hasCustomName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomName(String inventoryTitleIn)
/*     */   {
/* 191 */     this.hasCustomName = true;
/* 192 */     this.inventoryTitle = inventoryTitleIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 200 */     return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 208 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/* 217 */     if (this.field_70480_d != null)
/*     */     {
/* 219 */       for (int i = 0; i < this.field_70480_d.size(); i++)
/*     */       {
/* 221 */         ((IInvBasic)this.field_70480_d.get(i)).onInventoryChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 231 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void openInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack)
/*     */   {
/* 247 */     return true;
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 252 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 261 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 266 */     for (int i = 0; i < this.inventoryContents.length; i++)
/*     */     {
/* 268 */       this.inventoryContents[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\InventoryBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */