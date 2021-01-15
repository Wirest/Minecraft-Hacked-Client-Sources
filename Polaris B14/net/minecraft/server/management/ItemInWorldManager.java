/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.play.server.S23PacketBlockChange;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem.Action;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ 
/*     */ public class ItemInWorldManager
/*     */ {
/*     */   public World theWorld;
/*     */   public EntityPlayerMP thisPlayerMP;
/*  30 */   private WorldSettings.GameType gameType = WorldSettings.GameType.NOT_SET;
/*     */   
/*     */   private boolean isDestroyingBlock;
/*     */   
/*     */   private int initialDamage;
/*  35 */   private BlockPos field_180240_f = BlockPos.ORIGIN;
/*     */   
/*     */ 
/*     */   private int curblockDamage;
/*     */   
/*     */ 
/*     */   private boolean receivedFinishDiggingPacket;
/*     */   
/*  43 */   private BlockPos field_180241_i = BlockPos.ORIGIN;
/*     */   private int initialBlockDamage;
/*  45 */   private int durabilityRemainingOnBlock = -1;
/*     */   
/*     */   public ItemInWorldManager(World worldIn)
/*     */   {
/*  49 */     this.theWorld = worldIn;
/*     */   }
/*     */   
/*     */   public void setGameType(WorldSettings.GameType type)
/*     */   {
/*  54 */     this.gameType = type;
/*  55 */     type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
/*  56 */     this.thisPlayerMP.sendPlayerAbilities();
/*  57 */     this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
/*     */   }
/*     */   
/*     */   public WorldSettings.GameType getGameType()
/*     */   {
/*  62 */     return this.gameType;
/*     */   }
/*     */   
/*     */   public boolean survivalOrAdventure()
/*     */   {
/*  67 */     return this.gameType.isSurvivalOrAdventure();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCreative()
/*     */   {
/*  75 */     return this.gameType.isCreative();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initializeGameType(WorldSettings.GameType type)
/*     */   {
/*  83 */     if (this.gameType == WorldSettings.GameType.NOT_SET)
/*     */     {
/*  85 */       this.gameType = type;
/*     */     }
/*     */     
/*  88 */     setGameType(this.gameType);
/*     */   }
/*     */   
/*     */   public void updateBlockRemoving()
/*     */   {
/*  93 */     this.curblockDamage += 1;
/*     */     
/*  95 */     if (this.receivedFinishDiggingPacket)
/*     */     {
/*  97 */       int i = this.curblockDamage - this.initialBlockDamage;
/*  98 */       Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();
/*     */       
/* 100 */       if (block.getMaterial() == Material.air)
/*     */       {
/* 102 */         this.receivedFinishDiggingPacket = false;
/*     */       }
/*     */       else
/*     */       {
/* 106 */         float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (i + 1);
/* 107 */         int j = (int)(f * 10.0F);
/*     */         
/* 109 */         if (j != this.durabilityRemainingOnBlock)
/*     */         {
/* 111 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, j);
/* 112 */           this.durabilityRemainingOnBlock = j;
/*     */         }
/*     */         
/* 115 */         if (f >= 1.0F)
/*     */         {
/* 117 */           this.receivedFinishDiggingPacket = false;
/* 118 */           tryHarvestBlock(this.field_180241_i);
/*     */         }
/*     */       }
/*     */     }
/* 122 */     else if (this.isDestroyingBlock)
/*     */     {
/* 124 */       Block block1 = this.theWorld.getBlockState(this.field_180240_f).getBlock();
/*     */       
/* 126 */       if (block1.getMaterial() == Material.air)
/*     */       {
/* 128 */         this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
/* 129 */         this.durabilityRemainingOnBlock = -1;
/* 130 */         this.isDestroyingBlock = false;
/*     */       }
/*     */       else
/*     */       {
/* 134 */         int k = this.curblockDamage - this.initialDamage;
/* 135 */         float f1 = block1.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (k + 1);
/* 136 */         int l = (int)(f1 * 10.0F);
/*     */         
/* 138 */         if (l != this.durabilityRemainingOnBlock)
/*     */         {
/* 140 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, l);
/* 141 */           this.durabilityRemainingOnBlock = l;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockClicked(BlockPos pos, EnumFacing side)
/*     */   {
/* 153 */     if (isCreative())
/*     */     {
/* 155 */       if (!this.theWorld.extinguishFire(null, pos, side))
/*     */       {
/* 157 */         tryHarvestBlock(pos);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 162 */       Block block = this.theWorld.getBlockState(pos).getBlock();
/*     */       
/* 164 */       if (this.gameType.isAdventure())
/*     */       {
/* 166 */         if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */         {
/* 168 */           return;
/*     */         }
/*     */         
/* 171 */         if (!this.thisPlayerMP.isAllowEdit())
/*     */         {
/* 173 */           ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
/*     */           
/* 175 */           if (itemstack == null)
/*     */           {
/* 177 */             return;
/*     */           }
/*     */           
/* 180 */           if (!itemstack.canDestroy(block))
/*     */           {
/* 182 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 187 */       this.theWorld.extinguishFire(null, pos, side);
/* 188 */       this.initialDamage = this.curblockDamage;
/* 189 */       float f = 1.0F;
/*     */       
/* 191 */       if (block.getMaterial() != Material.air)
/*     */       {
/* 193 */         block.onBlockClicked(this.theWorld, pos, this.thisPlayerMP);
/* 194 */         f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos);
/*     */       }
/*     */       
/* 197 */       if ((block.getMaterial() != Material.air) && (f >= 1.0F))
/*     */       {
/* 199 */         tryHarvestBlock(pos);
/*     */       }
/*     */       else
/*     */       {
/* 203 */         this.isDestroyingBlock = true;
/* 204 */         this.field_180240_f = pos;
/* 205 */         int i = (int)(f * 10.0F);
/* 206 */         this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, i);
/* 207 */         this.durabilityRemainingOnBlock = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void blockRemoving(BlockPos pos)
/*     */   {
/* 214 */     if (pos.equals(this.field_180240_f))
/*     */     {
/* 216 */       int i = this.curblockDamage - this.initialDamage;
/* 217 */       Block block = this.theWorld.getBlockState(pos).getBlock();
/*     */       
/* 219 */       if (block.getMaterial() != Material.air)
/*     */       {
/* 221 */         float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos) * (i + 1);
/*     */         
/* 223 */         if (f >= 0.7F)
/*     */         {
/* 225 */           this.isDestroyingBlock = false;
/* 226 */           this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
/* 227 */           tryHarvestBlock(pos);
/*     */         }
/* 229 */         else if (!this.receivedFinishDiggingPacket)
/*     */         {
/* 231 */           this.isDestroyingBlock = false;
/* 232 */           this.receivedFinishDiggingPacket = true;
/* 233 */           this.field_180241_i = pos;
/* 234 */           this.initialBlockDamage = this.initialDamage;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cancelDestroyingBlock()
/*     */   {
/* 245 */     this.isDestroyingBlock = false;
/* 246 */     this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean removeBlock(BlockPos pos)
/*     */   {
/* 254 */     IBlockState iblockstate = this.theWorld.getBlockState(pos);
/* 255 */     iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, this.thisPlayerMP);
/* 256 */     boolean flag = this.theWorld.setBlockToAir(pos);
/*     */     
/* 258 */     if (flag)
/*     */     {
/* 260 */       iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate);
/*     */     }
/*     */     
/* 263 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean tryHarvestBlock(BlockPos pos)
/*     */   {
/* 271 */     if ((this.gameType.isCreative()) && (this.thisPlayerMP.getHeldItem() != null) && ((this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword)))
/*     */     {
/* 273 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 277 */     IBlockState iblockstate = this.theWorld.getBlockState(pos);
/* 278 */     TileEntity tileentity = this.theWorld.getTileEntity(pos);
/*     */     
/* 280 */     if (this.gameType.isAdventure())
/*     */     {
/* 282 */       if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/* 284 */         return false;
/*     */       }
/*     */       
/* 287 */       if (!this.thisPlayerMP.isAllowEdit())
/*     */       {
/* 289 */         ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
/*     */         
/* 291 */         if (itemstack == null)
/*     */         {
/* 293 */           return false;
/*     */         }
/*     */         
/* 296 */         if (!itemstack.canDestroy(iblockstate.getBlock()))
/*     */         {
/* 298 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 303 */     this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
/* 304 */     boolean flag1 = removeBlock(pos);
/*     */     
/* 306 */     if (isCreative())
/*     */     {
/* 308 */       this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, pos));
/*     */     }
/*     */     else
/*     */     {
/* 312 */       ItemStack itemstack1 = this.thisPlayerMP.getCurrentEquippedItem();
/* 313 */       boolean flag = this.thisPlayerMP.canHarvestBlock(iblockstate.getBlock());
/*     */       
/* 315 */       if (itemstack1 != null)
/*     */       {
/* 317 */         itemstack1.onBlockDestroyed(this.theWorld, iblockstate.getBlock(), pos, this.thisPlayerMP);
/*     */         
/* 319 */         if (itemstack1.stackSize == 0)
/*     */         {
/* 321 */           this.thisPlayerMP.destroyCurrentEquippedItem();
/*     */         }
/*     */       }
/*     */       
/* 325 */       if ((flag1) && (flag))
/*     */       {
/* 327 */         iblockstate.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, pos, iblockstate, tileentity);
/*     */       }
/*     */     }
/*     */     
/* 331 */     return flag1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack)
/*     */   {
/* 340 */     if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 342 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 346 */     int i = stack.stackSize;
/* 347 */     int j = stack.getMetadata();
/* 348 */     ItemStack itemstack = stack.useItemRightClick(worldIn, player);
/*     */     
/* 350 */     if ((itemstack != stack) || ((itemstack != null) && ((itemstack.stackSize != i) || (itemstack.getMaxItemUseDuration() > 0) || (itemstack.getMetadata() != j))))
/*     */     {
/* 352 */       player.inventory.mainInventory[player.inventory.currentItem] = itemstack;
/*     */       
/* 354 */       if (isCreative())
/*     */       {
/* 356 */         itemstack.stackSize = i;
/*     */         
/* 358 */         if (itemstack.isItemStackDamageable())
/*     */         {
/* 360 */           itemstack.setItemDamage(j);
/*     */         }
/*     */       }
/*     */       
/* 364 */       if (itemstack.stackSize == 0)
/*     */       {
/* 366 */         player.inventory.mainInventory[player.inventory.currentItem] = null;
/*     */       }
/*     */       
/* 369 */       if (!player.isUsingItem())
/*     */       {
/* 371 */         ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
/*     */       }
/*     */       
/* 374 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 378 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ)
/*     */   {
/* 388 */     if (this.gameType == WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 390 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 392 */       if ((tileentity instanceof ILockableContainer))
/*     */       {
/* 394 */         Block block = worldIn.getBlockState(pos).getBlock();
/* 395 */         ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;
/*     */         
/* 397 */         if (((ilockablecontainer instanceof TileEntityChest)) && ((block instanceof BlockChest)))
/*     */         {
/* 399 */           ilockablecontainer = ((BlockChest)block).getLockableContainer(worldIn, pos);
/*     */         }
/*     */         
/* 402 */         if (ilockablecontainer != null)
/*     */         {
/* 404 */           player.displayGUIChest(ilockablecontainer);
/* 405 */           return true;
/*     */         }
/*     */       }
/* 408 */       else if ((tileentity instanceof IInventory))
/*     */       {
/* 410 */         player.displayGUIChest((IInventory)tileentity);
/* 411 */         return true;
/*     */       }
/*     */       
/* 414 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 418 */     if ((!player.isSneaking()) || (player.getHeldItem() == null))
/*     */     {
/* 420 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 422 */       if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, side, offsetX, offsetY, offsetZ))
/*     */       {
/* 424 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 428 */     if (stack == null)
/*     */     {
/* 430 */       return false;
/*     */     }
/* 432 */     if (isCreative())
/*     */     {
/* 434 */       int j = stack.getMetadata();
/* 435 */       int i = stack.stackSize;
/* 436 */       boolean flag = stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
/* 437 */       stack.setItemDamage(j);
/* 438 */       stack.stackSize = i;
/* 439 */       return flag;
/*     */     }
/*     */     
/*     */ 
/* 443 */     return stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorld(WorldServer serverWorld)
/*     */   {
/* 453 */     this.theWorld = serverWorld;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\ItemInWorldManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */