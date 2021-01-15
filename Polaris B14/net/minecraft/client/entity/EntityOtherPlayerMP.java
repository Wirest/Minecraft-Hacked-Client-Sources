/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityOtherPlayerMP extends AbstractClientPlayer
/*     */ {
/*     */   private boolean isItemInUse;
/*     */   private int otherPlayerMPPosRotationIncrements;
/*     */   private double otherPlayerMPX;
/*     */   private double otherPlayerMPY;
/*     */   private double otherPlayerMPZ;
/*     */   private double otherPlayerMPYaw;
/*     */   private double otherPlayerMPPitch;
/*     */   
/*     */   public EntityOtherPlayerMP(World worldIn, GameProfile gameProfileIn)
/*     */   {
/*  24 */     super(worldIn, gameProfileIn);
/*  25 */     this.stepHeight = 0.0F;
/*  26 */     this.noClip = true;
/*  27 */     this.renderOffsetY = 0.25F;
/*  28 */     this.renderDistanceWeight = 10.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/*  36 */     return true;
/*     */   }
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
/*     */   {
/*  41 */     this.otherPlayerMPX = x;
/*  42 */     this.otherPlayerMPY = y;
/*  43 */     this.otherPlayerMPZ = z;
/*  44 */     this.otherPlayerMPYaw = yaw;
/*  45 */     this.otherPlayerMPPitch = pitch;
/*  46 */     this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  54 */     this.renderOffsetY = 0.0F;
/*  55 */     super.onUpdate();
/*  56 */     this.prevLimbSwingAmount = this.limbSwingAmount;
/*  57 */     double d0 = this.posX - this.prevPosX;
/*  58 */     double d1 = this.posZ - this.prevPosZ;
/*  59 */     float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;
/*     */     
/*  61 */     if (f > 1.0F)
/*     */     {
/*  63 */       f = 1.0F;
/*     */     }
/*     */     
/*  66 */     this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
/*  67 */     this.limbSwing += this.limbSwingAmount;
/*     */     
/*  69 */     if ((!this.isItemInUse) && (isEating()) && (this.inventory.mainInventory[this.inventory.currentItem] != null))
/*     */     {
/*  71 */       ItemStack itemstack = this.inventory.mainInventory[this.inventory.currentItem];
/*  72 */       setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], itemstack.getItem().getMaxItemUseDuration(itemstack));
/*  73 */       this.isItemInUse = true;
/*     */     }
/*  75 */     else if ((this.isItemInUse) && (!isEating()))
/*     */     {
/*  77 */       clearItemInUse();
/*  78 */       this.isItemInUse = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onLivingUpdate()
/*     */   {
/*  88 */     if (this.otherPlayerMPPosRotationIncrements > 0)
/*     */     {
/*  90 */       double d0 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
/*  91 */       double d1 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
/*  92 */       double d2 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
/*     */       
/*     */ 
/*  95 */       for (double d3 = this.otherPlayerMPYaw - this.rotationYaw; d3 < -180.0D; d3 += 360.0D) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 100 */       while (d3 >= 180.0D)
/*     */       {
/* 102 */         d3 -= 360.0D;
/*     */       }
/*     */       
/* 105 */       this.rotationYaw = ((float)(this.rotationYaw + d3 / this.otherPlayerMPPosRotationIncrements));
/* 106 */       this.rotationPitch = ((float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements));
/* 107 */       this.otherPlayerMPPosRotationIncrements -= 1;
/* 108 */       setPosition(d0, d1, d2);
/* 109 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */     }
/*     */     
/* 112 */     this.prevCameraYaw = this.cameraYaw;
/* 113 */     updateArmSwingProgress();
/* 114 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 115 */     float f = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
/*     */     
/* 117 */     if (f1 > 0.1F)
/*     */     {
/* 119 */       f1 = 0.1F;
/*     */     }
/*     */     
/* 122 */     if ((!this.onGround) || (getHealth() <= 0.0F))
/*     */     {
/* 124 */       f1 = 0.0F;
/*     */     }
/*     */     
/* 127 */     if ((this.onGround) || (getHealth() <= 0.0F))
/*     */     {
/* 129 */       f = 0.0F;
/*     */     }
/*     */     
/* 132 */     this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
/* 133 */     this.cameraPitch += (f - this.cameraPitch) * 0.8F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
/*     */   {
/* 141 */     if (slotIn == 0)
/*     */     {
/* 143 */       this.inventory.mainInventory[this.inventory.currentItem] = stack;
/*     */     }
/*     */     else
/*     */     {
/* 147 */       this.inventory.armorInventory[(slotIn - 1)] = stack;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addChatMessage(IChatComponent component)
/*     */   {
/* 156 */     Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*     */   {
/* 164 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos getPosition()
/*     */   {
/* 173 */     return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\entity\EntityOtherPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */