/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerDispenser;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityDispenser extends TileEntityLockable implements net.minecraft.inventory.IInventory
/*     */ {
/*  15 */   private static final Random RNG = new Random();
/*  16 */   private ItemStack[] stacks = new ItemStack[9];
/*     */   
/*     */ 
/*     */   protected String customName;
/*     */   
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/*  24 */     return 9;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack getStackInSlot(int index)
/*     */   {
/*  32 */     return this.stacks[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack decrStackSize(int index, int count)
/*     */   {
/*  40 */     if (this.stacks[index] != null)
/*     */     {
/*  42 */       if (this.stacks[index].stackSize <= count)
/*     */       {
/*  44 */         ItemStack itemstack1 = this.stacks[index];
/*  45 */         this.stacks[index] = null;
/*  46 */         markDirty();
/*  47 */         return itemstack1;
/*     */       }
/*     */       
/*     */ 
/*  51 */       ItemStack itemstack = this.stacks[index].splitStack(count);
/*     */       
/*  53 */       if (this.stacks[index].stackSize == 0)
/*     */       {
/*  55 */         this.stacks[index] = null;
/*     */       }
/*     */       
/*  58 */       markDirty();
/*  59 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  64 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack removeStackFromSlot(int index)
/*     */   {
/*  73 */     if (this.stacks[index] != null)
/*     */     {
/*  75 */       ItemStack itemstack = this.stacks[index];
/*  76 */       this.stacks[index] = null;
/*  77 */       return itemstack;
/*     */     }
/*     */     
/*     */ 
/*  81 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getDispenseSlot()
/*     */   {
/*  87 */     int i = -1;
/*  88 */     int j = 1;
/*     */     
/*  90 */     for (int k = 0; k < this.stacks.length; k++)
/*     */     {
/*  92 */       if ((this.stacks[k] != null) && (RNG.nextInt(j++) == 0))
/*     */       {
/*  94 */         i = k;
/*     */       }
/*     */     }
/*     */     
/*  98 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInventorySlotContents(int index, ItemStack stack)
/*     */   {
/* 106 */     this.stacks[index] = stack;
/*     */     
/* 108 */     if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
/*     */     {
/* 110 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/* 113 */     markDirty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int addItemStack(ItemStack stack)
/*     */   {
/* 122 */     for (int i = 0; i < this.stacks.length; i++)
/*     */     {
/* 124 */       if ((this.stacks[i] == null) || (this.stacks[i].getItem() == null))
/*     */       {
/* 126 */         setInventorySlotContents(i, stack);
/* 127 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 131 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 139 */     return hasCustomName() ? this.customName : "container.dispenser";
/*     */   }
/*     */   
/*     */   public void setCustomName(String customName)
/*     */   {
/* 144 */     this.customName = customName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 152 */     return this.customName != null;
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/* 157 */     super.readFromNBT(compound);
/* 158 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 159 */     this.stacks = new ItemStack[getSizeInventory()];
/*     */     
/* 161 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */     {
/* 163 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 164 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*     */       
/* 166 */       if ((j >= 0) && (j < this.stacks.length))
/*     */       {
/* 168 */         this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 172 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 174 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/* 180 */     super.writeToNBT(compound);
/* 181 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 183 */     for (int i = 0; i < this.stacks.length; i++)
/*     */     {
/* 185 */       if (this.stacks[i] != null)
/*     */       {
/* 187 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 188 */         nbttagcompound.setByte("Slot", (byte)i);
/* 189 */         this.stacks[i].writeToNBT(nbttagcompound);
/* 190 */         nbttaglist.appendTag(nbttagcompound);
/*     */       }
/*     */     }
/*     */     
/* 194 */     compound.setTag("Items", nbttaglist);
/*     */     
/* 196 */     if (hasCustomName())
/*     */     {
/* 198 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 207 */     return 64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer player)
/*     */   {
/* 215 */     return this.worldObj.getTileEntity(this.pos) == this;
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
/* 231 */     return true;
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 236 */     return "minecraft:dispenser";
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 241 */     return new ContainerDispenser(playerInventory, this);
/*     */   }
/*     */   
/*     */   public int getField(int id)
/*     */   {
/* 246 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setField(int id, int value) {}
/*     */   
/*     */ 
/*     */   public int getFieldCount()
/*     */   {
/* 255 */     return 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 260 */     for (int i = 0; i < this.stacks.length; i++)
/*     */     {
/* 262 */       this.stacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */