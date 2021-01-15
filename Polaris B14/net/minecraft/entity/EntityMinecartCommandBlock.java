/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartCommandBlock extends EntityMinecart
/*     */ {
/*  17 */   private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic()
/*     */   {
/*     */     public void updateCommand()
/*     */     {
/*  21 */       EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, getCommand());
/*  22 */       EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(getLastOutput()));
/*     */     }
/*     */     
/*     */     public int func_145751_f() {
/*  26 */       return 1;
/*     */     }
/*     */     
/*     */     public void func_145757_a(ByteBuf p_145757_1_) {
/*  30 */       p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
/*     */     }
/*     */     
/*     */     public BlockPos getPosition() {
/*  34 */       return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5D, EntityMinecartCommandBlock.this.posZ);
/*     */     }
/*     */     
/*     */     public Vec3 getPositionVector() {
/*  38 */       return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
/*     */     }
/*     */     
/*     */     public World getEntityWorld() {
/*  42 */       return EntityMinecartCommandBlock.this.worldObj;
/*     */     }
/*     */     
/*     */     public Entity getCommandSenderEntity() {
/*  46 */       return EntityMinecartCommandBlock.this;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*  51 */   private int activatorRailCooldown = 0;
/*     */   
/*     */   public EntityMinecartCommandBlock(World worldIn)
/*     */   {
/*  55 */     super(worldIn);
/*     */   }
/*     */   
/*     */   public EntityMinecartCommandBlock(World worldIn, double x, double y, double z)
/*     */   {
/*  60 */     super(worldIn, x, y, z);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  65 */     super.entityInit();
/*  66 */     getDataWatcher().addObject(23, "");
/*  67 */     getDataWatcher().addObject(24, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  75 */     super.readEntityFromNBT(tagCompund);
/*  76 */     this.commandBlockLogic.readDataFromNBT(tagCompund);
/*  77 */     getDataWatcher().updateObject(23, getCommandBlockLogic().getCommand());
/*  78 */     getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(getCommandBlockLogic().getLastOutput()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  86 */     super.writeEntityToNBT(tagCompound);
/*  87 */     this.commandBlockLogic.writeDataToNBT(tagCompound);
/*     */   }
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType()
/*     */   {
/*  92 */     return EntityMinecart.EnumMinecartType.COMMAND_BLOCK;
/*     */   }
/*     */   
/*     */   public IBlockState getDefaultDisplayTile()
/*     */   {
/*  97 */     return net.minecraft.init.Blocks.command_block.getDefaultState();
/*     */   }
/*     */   
/*     */   public CommandBlockLogic getCommandBlockLogic()
/*     */   {
/* 102 */     return this.commandBlockLogic;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
/*     */   {
/* 110 */     if ((receivingPower) && (this.ticksExisted - this.activatorRailCooldown >= 4))
/*     */     {
/* 112 */       getCommandBlockLogic().trigger(this.worldObj);
/* 113 */       this.activatorRailCooldown = this.ticksExisted;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer playerIn)
/*     */   {
/* 122 */     this.commandBlockLogic.tryOpenEditCommandBlock(playerIn);
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   public void onDataWatcherUpdate(int dataID)
/*     */   {
/* 128 */     super.onDataWatcherUpdate(dataID);
/*     */     
/* 130 */     if (dataID == 24)
/*     */     {
/*     */       try
/*     */       {
/* 134 */         this.commandBlockLogic.setLastOutput(IChatComponent.Serializer.jsonToComponent(getDataWatcher().getWatchableObjectString(24)));
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */ 
/*     */     }
/* 141 */     else if (dataID == 23)
/*     */     {
/* 143 */       this.commandBlockLogic.setCommand(getDataWatcher().getWatchableObjectString(23));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityMinecartCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */