/*    */ package net.minecraft.entity.player;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerCapabilities
/*    */ {
/*    */   public boolean disableDamage;
/*    */   public boolean isFlying;
/*    */   public boolean allowFlying;
/*    */   public boolean isCreativeMode;
/* 22 */   public boolean allowEdit = true;
/* 23 */   private float flySpeed = 0.05F;
/* 24 */   private float walkSpeed = 0.1F;
/*    */   
/*    */   public void writeCapabilitiesToNBT(NBTTagCompound tagCompound)
/*    */   {
/* 28 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 29 */     nbttagcompound.setBoolean("invulnerable", this.disableDamage);
/* 30 */     nbttagcompound.setBoolean("flying", this.isFlying);
/* 31 */     nbttagcompound.setBoolean("mayfly", this.allowFlying);
/* 32 */     nbttagcompound.setBoolean("instabuild", this.isCreativeMode);
/* 33 */     nbttagcompound.setBoolean("mayBuild", this.allowEdit);
/* 34 */     nbttagcompound.setFloat("flySpeed", this.flySpeed);
/* 35 */     nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
/* 36 */     tagCompound.setTag("abilities", nbttagcompound);
/*    */   }
/*    */   
/*    */   public void readCapabilitiesFromNBT(NBTTagCompound tagCompound)
/*    */   {
/* 41 */     if (tagCompound.hasKey("abilities", 10))
/*    */     {
/* 43 */       NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
/* 44 */       this.disableDamage = nbttagcompound.getBoolean("invulnerable");
/* 45 */       this.isFlying = nbttagcompound.getBoolean("flying");
/* 46 */       this.allowFlying = nbttagcompound.getBoolean("mayfly");
/* 47 */       this.isCreativeMode = nbttagcompound.getBoolean("instabuild");
/*    */       
/* 49 */       if (nbttagcompound.hasKey("flySpeed", 99))
/*    */       {
/* 51 */         this.flySpeed = nbttagcompound.getFloat("flySpeed");
/* 52 */         this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
/*    */       }
/*    */       
/* 55 */       if (nbttagcompound.hasKey("mayBuild", 1))
/*    */       {
/* 57 */         this.allowEdit = nbttagcompound.getBoolean("mayBuild");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public float getFlySpeed()
/*    */   {
/* 64 */     return this.flySpeed;
/*    */   }
/*    */   
/*    */   public void setFlySpeed(float speed)
/*    */   {
/* 69 */     this.flySpeed = speed;
/*    */   }
/*    */   
/*    */   public float getWalkSpeed()
/*    */   {
/* 74 */     return this.walkSpeed;
/*    */   }
/*    */   
/*    */   public void setPlayerWalkSpeed(float speed)
/*    */   {
/* 79 */     this.walkSpeed = speed;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\player\PlayerCapabilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */