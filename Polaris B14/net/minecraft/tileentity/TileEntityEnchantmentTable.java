/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IInteractionObject;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject
/*     */ {
/*     */   public int tickCount;
/*     */   public float pageFlip;
/*     */   public float pageFlipPrev;
/*     */   public float field_145932_k;
/*     */   public float field_145929_l;
/*     */   public float bookSpread;
/*     */   public float bookSpreadPrev;
/*     */   public float bookRotation;
/*     */   public float bookRotationPrev;
/*     */   public float field_145924_q;
/*  28 */   private static Random rand = new Random();
/*     */   private String customName;
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/*  33 */     super.writeToNBT(compound);
/*     */     
/*  35 */     if (hasCustomName())
/*     */     {
/*  37 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/*  43 */     super.readFromNBT(compound);
/*     */     
/*  45 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/*  47 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/*  56 */     this.bookSpreadPrev = this.bookSpread;
/*  57 */     this.bookRotationPrev = this.bookRotation;
/*  58 */     EntityPlayer entityplayer = this.worldObj.getClosestPlayer(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, 3.0D);
/*     */     
/*  60 */     if (entityplayer != null)
/*     */     {
/*  62 */       double d0 = entityplayer.posX - (this.pos.getX() + 0.5F);
/*  63 */       double d1 = entityplayer.posZ - (this.pos.getZ() + 0.5F);
/*  64 */       this.field_145924_q = ((float)MathHelper.func_181159_b(d1, d0));
/*  65 */       this.bookSpread += 0.1F;
/*     */       
/*  67 */       if ((this.bookSpread < 0.5F) || (rand.nextInt(40) == 0))
/*     */       {
/*  69 */         float f1 = this.field_145932_k;
/*     */         
/*     */         do
/*     */         {
/*  73 */           this.field_145932_k += rand.nextInt(4) - rand.nextInt(4);
/*     */         }
/*  75 */         while (f1 == this.field_145932_k);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  84 */       this.field_145924_q += 0.02F;
/*  85 */       this.bookSpread -= 0.1F;
/*     */     }
/*     */     
/*  88 */     while (this.bookRotation >= 3.1415927F)
/*     */     {
/*  90 */       this.bookRotation -= 6.2831855F;
/*     */     }
/*     */     
/*  93 */     while (this.bookRotation < -3.1415927F)
/*     */     {
/*  95 */       this.bookRotation += 6.2831855F;
/*     */     }
/*     */     
/*  98 */     while (this.field_145924_q >= 3.1415927F)
/*     */     {
/* 100 */       this.field_145924_q -= 6.2831855F;
/*     */     }
/*     */     
/* 103 */     while (this.field_145924_q < -3.1415927F)
/*     */     {
/* 105 */       this.field_145924_q += 6.2831855F;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 110 */     for (float f2 = this.field_145924_q - this.bookRotation; f2 >= 3.1415927F; f2 -= 6.2831855F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 115 */     while (f2 < -3.1415927F)
/*     */     {
/* 117 */       f2 += 6.2831855F;
/*     */     }
/*     */     
/* 120 */     this.bookRotation += f2 * 0.4F;
/* 121 */     this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0F, 1.0F);
/* 122 */     this.tickCount += 1;
/* 123 */     this.pageFlipPrev = this.pageFlip;
/* 124 */     float f = (this.field_145932_k - this.pageFlip) * 0.4F;
/* 125 */     float f3 = 0.2F;
/* 126 */     f = MathHelper.clamp_float(f, -f3, f3);
/* 127 */     this.field_145929_l += (f - this.field_145929_l) * 0.9F;
/* 128 */     this.pageFlip += this.field_145929_l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 136 */     return hasCustomName() ? this.customName : "container.enchant";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCustomName()
/*     */   {
/* 144 */     return (this.customName != null) && (this.customName.length() > 0);
/*     */   }
/*     */   
/*     */   public void setCustomName(String customNameIn)
/*     */   {
/* 149 */     this.customName = customNameIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 157 */     return hasCustomName() ? new ChatComponentText(getName()) : new net.minecraft.util.ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*     */   {
/* 162 */     return new net.minecraft.inventory.ContainerEnchantment(playerInventory, this.worldObj, this.pos);
/*     */   }
/*     */   
/*     */   public String getGuiID()
/*     */   {
/* 167 */     return "minecraft:enchanting_table";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityEnchantmentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */