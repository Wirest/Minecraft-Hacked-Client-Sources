/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.Block.SoundType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity.Action;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*     */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*     */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*     */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*     */ import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
/*     */ import net.minecraft.stats.StatFileWriter;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ public class PlayerControllerMP
/*     */ {
/*     */   private final Minecraft mc;
/*     */   private final NetHandlerPlayClient netClientHandler;
/*  37 */   private BlockPos currentBlock = new BlockPos(-1, -1, -1);
/*     */   
/*     */ 
/*     */ 
/*     */   private ItemStack currentItemHittingBlock;
/*     */   
/*     */ 
/*     */ 
/*     */   public float curBlockDamageMP;
/*     */   
/*     */ 
/*     */ 
/*     */   private float stepSoundTickCounter;
/*     */   
/*     */ 
/*     */ 
/*     */   public int blockHitDelay;
/*     */   
/*     */ 
/*     */   private boolean isHittingBlock;
/*     */   
/*     */ 
/*  59 */   private WorldSettings.GameType currentGameType = WorldSettings.GameType.SURVIVAL;
/*     */   
/*     */   private int currentPlayerItem;
/*     */   
/*     */ 
/*     */   public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_)
/*     */   {
/*  66 */     this.mc = mcIn;
/*  67 */     this.netClientHandler = p_i45062_2_;
/*     */   }
/*     */   
/*     */   public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_)
/*     */   {
/*  72 */     if (!mcIn.theWorld.extinguishFire(mcIn.thePlayer, p_178891_2_, p_178891_3_))
/*     */     {
/*  74 */       p_178891_1_.onPlayerDestroyBlock(p_178891_2_, p_178891_3_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPlayerCapabilities(EntityPlayer p_78748_1_)
/*     */   {
/*  83 */     this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSpectator()
/*     */   {
/*  91 */     return this.currentGameType == WorldSettings.GameType.SPECTATOR;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGameType(WorldSettings.GameType p_78746_1_)
/*     */   {
/*  99 */     this.currentGameType = p_78746_1_;
/* 100 */     this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flipPlayer(EntityPlayer playerIn)
/*     */   {
/* 108 */     playerIn.rotationYaw = -180.0F;
/*     */   }
/*     */   
/*     */   public boolean shouldDrawHUD()
/*     */   {
/* 113 */     return this.currentGameType.isSurvivalOrAdventure();
/*     */   }
/*     */   
/*     */   public boolean processRightClickBlock(EntityPlayerSP p_178890_1_, WorldClient p_178890_2_, ItemStack p_178890_3_, BlockPos p_178890_4_, EnumFacing p_178890_5_, Vec3 p_178890_6_) {
/* 117 */     syncCurrentPlayItem();
/* 118 */     float var7 = (float)(p_178890_6_.xCoord - p_178890_4_.getX());
/* 119 */     float var8 = (float)(p_178890_6_.yCoord - p_178890_4_.getY());
/* 120 */     float var9 = (float)(p_178890_6_.zCoord - p_178890_4_.getZ());
/* 121 */     boolean var10 = false;
/* 122 */     if (!this.mc.theWorld.getWorldBorder().contains(p_178890_4_)) {
/* 123 */       return false;
/*     */     }
/* 125 */     if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
/* 126 */       IBlockState var11 = p_178890_2_.getBlockState(p_178890_4_);
/* 127 */       if (((!p_178890_1_.isSneaking()) || (p_178890_1_.getHeldItem() == null)) && (var11.getBlock().onBlockActivated(p_178890_2_, p_178890_4_, var11, p_178890_1_, p_178890_5_, var7, var8, var9))) {
/* 128 */         var10 = true;
/*     */       }
/* 130 */       if ((!var10) && (p_178890_3_ != null) && ((p_178890_3_.getItem() instanceof ItemBlock))) {
/* 131 */         ItemBlock var12 = (ItemBlock)p_178890_3_.getItem();
/* 132 */         if (!var12.canPlaceBlockOnSide(p_178890_2_, p_178890_4_, p_178890_5_, p_178890_1_, p_178890_3_)) {
/* 133 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 137 */     this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(p_178890_4_, p_178890_5_.getIndex(), p_178890_1_.inventory.getCurrentItem(), var7, var8, var9));
/* 138 */     if ((var10) || (this.currentGameType == WorldSettings.GameType.SPECTATOR)) {
/* 139 */       return true;
/*     */     }
/* 141 */     if (p_178890_3_ == null) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.currentGameType.isCreative()) {
/* 145 */       int var13 = p_178890_3_.getMetadata();
/* 146 */       int var14 = p_178890_3_.stackSize;
/* 147 */       boolean var15 = p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, var7, var8, var9);
/* 148 */       p_178890_3_.setItemDamage(var13);
/* 149 */       p_178890_3_.stackSize = var14;
/* 150 */       return var15;
/*     */     }
/* 152 */     return p_178890_3_.onItemUse(p_178890_1_, p_178890_2_, p_178890_4_, p_178890_5_, var7, var8, var9);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onPlayerDestroyBlock(BlockPos pos, EnumFacing side)
/*     */   {
/* 160 */     if (this.currentGameType.isAdventure())
/*     */     {
/* 162 */       if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/* 164 */         return false;
/*     */       }
/*     */       
/* 167 */       if (!this.mc.thePlayer.isAllowEdit())
/*     */       {
/* 169 */         Block block = this.mc.theWorld.getBlockState(pos).getBlock();
/* 170 */         ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
/*     */         
/* 172 */         if (itemstack == null)
/*     */         {
/* 174 */           return false;
/*     */         }
/*     */         
/* 177 */         if (!itemstack.canDestroy(block))
/*     */         {
/* 179 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 184 */     if ((this.currentGameType.isCreative()) && (this.mc.thePlayer.getHeldItem() != null) && ((this.mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemSword)))
/*     */     {
/* 186 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 190 */     World world = this.mc.theWorld;
/* 191 */     IBlockState iblockstate = world.getBlockState(pos);
/* 192 */     Block block1 = iblockstate.getBlock();
/*     */     
/* 194 */     if (block1.getMaterial() == Material.air)
/*     */     {
/* 196 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 200 */     world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
/* 201 */     boolean flag = world.setBlockToAir(pos);
/*     */     
/* 203 */     if (flag)
/*     */     {
/* 205 */       block1.onBlockDestroyedByPlayer(world, pos, iblockstate);
/*     */     }
/*     */     
/* 208 */     this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
/*     */     
/* 210 */     if (!this.currentGameType.isCreative())
/*     */     {
/* 212 */       ItemStack itemstack1 = this.mc.thePlayer.getCurrentEquippedItem();
/*     */       
/* 214 */       if (itemstack1 != null)
/*     */       {
/* 216 */         itemstack1.onBlockDestroyed(world, block1, pos, this.mc.thePlayer);
/*     */         
/* 218 */         if (itemstack1.stackSize == 0)
/*     */         {
/* 220 */           this.mc.thePlayer.destroyCurrentEquippedItem();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 225 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean clickBlock(BlockPos loc, EnumFacing face)
/*     */   {
/* 235 */     if (this.currentGameType.isAdventure())
/*     */     {
/* 237 */       if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */       {
/* 239 */         return false;
/*     */       }
/*     */       
/* 242 */       if (!this.mc.thePlayer.isAllowEdit())
/*     */       {
/* 244 */         Block block = this.mc.theWorld.getBlockState(loc).getBlock();
/* 245 */         ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
/*     */         
/* 247 */         if (itemstack == null)
/*     */         {
/* 249 */           return false;
/*     */         }
/*     */         
/* 252 */         if (!itemstack.canDestroy(block))
/*     */         {
/* 254 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 259 */     if (!this.mc.theWorld.getWorldBorder().contains(loc))
/*     */     {
/* 261 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 265 */     if (this.currentGameType.isCreative())
/*     */     {
/* 267 */       this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
/* 268 */       clickBlockCreative(this.mc, this, loc, face);
/* 269 */       this.blockHitDelay = 5;
/*     */     }
/* 271 */     else if ((!this.isHittingBlock) || (!isHittingPosition(loc)))
/*     */     {
/* 273 */       if (this.isHittingBlock)
/*     */       {
/* 275 */         this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
/*     */       }
/*     */       
/* 278 */       this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
/* 279 */       Block block1 = this.mc.theWorld.getBlockState(loc).getBlock();
/* 280 */       boolean flag = block1.getMaterial() != Material.air;
/*     */       
/* 282 */       if ((flag) && (this.curBlockDamageMP == 0.0F))
/*     */       {
/* 284 */         block1.onBlockClicked(this.mc.theWorld, loc, this.mc.thePlayer);
/*     */       }
/*     */       
/* 287 */       if ((flag) && (block1.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, loc) >= 1.0F))
/*     */       {
/* 289 */         onPlayerDestroyBlock(loc, face);
/*     */       }
/*     */       else
/*     */       {
/* 293 */         this.isHittingBlock = true;
/* 294 */         this.currentBlock = loc;
/* 295 */         this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
/* 296 */         this.curBlockDamageMP = 0.0F;
/* 297 */         this.stepSoundTickCounter = 0.0F;
/* 298 */         this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
/*     */       }
/*     */     }
/*     */     
/* 302 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetBlockRemoving()
/*     */   {
/* 311 */     if (this.isHittingBlock)
/*     */     {
/* 313 */       this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
/* 314 */       this.isHittingBlock = false;
/* 315 */       this.curBlockDamageMP = 0.0F;
/* 316 */       this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, -1);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
/*     */   {
/* 322 */     syncCurrentPlayItem();
/*     */     
/* 324 */     if (this.blockHitDelay > 0)
/*     */     {
/* 326 */       this.blockHitDelay -= 1;
/* 327 */       return true;
/*     */     }
/* 329 */     if ((this.currentGameType.isCreative()) && (this.mc.theWorld.getWorldBorder().contains(posBlock)))
/*     */     {
/* 331 */       this.blockHitDelay = 5;
/* 332 */       this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
/* 333 */       clickBlockCreative(this.mc, this, posBlock, directionFacing);
/* 334 */       return true;
/*     */     }
/* 336 */     if (isHittingPosition(posBlock))
/*     */     {
/* 338 */       Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();
/*     */       
/* 340 */       if (block.getMaterial() == Material.air)
/*     */       {
/* 342 */         this.isHittingBlock = false;
/* 343 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 347 */       this.curBlockDamageMP += block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, posBlock);
/*     */       
/* 349 */       if (this.stepSoundTickCounter % 4.0F == 0.0F)
/*     */       {
/* 351 */         this.mc.getSoundHandler().playSound(new net.minecraft.client.audio.PositionedSoundRecord(new net.minecraft.util.ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getFrequency() * 0.5F, posBlock.getX() + 0.5F, posBlock.getY() + 0.5F, posBlock.getZ() + 0.5F));
/*     */       }
/*     */       
/* 354 */       this.stepSoundTickCounter += 1.0F;
/*     */       
/* 356 */       if (this.curBlockDamageMP >= 1.0F)
/*     */       {
/* 358 */         this.isHittingBlock = false;
/* 359 */         this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
/* 360 */         onPlayerDestroyBlock(posBlock, directionFacing);
/* 361 */         this.curBlockDamageMP = 0.0F;
/* 362 */         this.stepSoundTickCounter = 0.0F;
/* 363 */         this.blockHitDelay = 5;
/*     */       }
/*     */       
/* 366 */       this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
/* 367 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 372 */     return clickBlock(posBlock, directionFacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBlockReachDistance()
/*     */   {
/* 381 */     return this.currentGameType.isCreative() ? 5.0F : 4.5F;
/*     */   }
/*     */   
/*     */   public void updateController()
/*     */   {
/* 386 */     syncCurrentPlayItem();
/*     */     
/* 388 */     if (this.netClientHandler.getNetworkManager().isChannelOpen())
/*     */     {
/* 390 */       this.netClientHandler.getNetworkManager().processReceivedPackets();
/*     */     }
/*     */     else
/*     */     {
/* 394 */       this.netClientHandler.getNetworkManager().checkDisconnected();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isHittingPosition(BlockPos pos)
/*     */   {
/* 400 */     ItemStack itemstack = this.mc.thePlayer.getHeldItem();
/* 401 */     boolean flag = (this.currentItemHittingBlock == null) && (itemstack == null);
/*     */     
/* 403 */     if ((this.currentItemHittingBlock != null) && (itemstack != null))
/*     */     {
/* 405 */       flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem()) && (ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock)) && ((itemstack.isItemStackDamageable()) || (itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata()));
/*     */     }
/*     */     
/* 408 */     return (pos.equals(this.currentBlock)) && (flag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void syncCurrentPlayItem()
/*     */   {
/* 416 */     int i = this.mc.thePlayer.inventory.currentItem;
/*     */     
/* 418 */     if (i != this.currentPlayerItem)
/*     */     {
/* 420 */       this.currentPlayerItem = i;
/* 421 */       this.netClientHandler.addToSendQueue(new net.minecraft.network.play.client.C09PacketHeldItemChange(this.currentPlayerItem));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec)
/*     */   {
/* 427 */     syncCurrentPlayItem();
/* 428 */     float f = (float)(hitVec.xCoord - hitPos.getX());
/* 429 */     float f1 = (float)(hitVec.yCoord - hitPos.getY());
/* 430 */     float f2 = (float)(hitVec.zCoord - hitPos.getZ());
/* 431 */     boolean flag = false;
/*     */     
/* 433 */     if (!this.mc.theWorld.getWorldBorder().contains(hitPos))
/*     */     {
/* 435 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 439 */     if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 441 */       IBlockState iblockstate = worldIn.getBlockState(hitPos);
/*     */       
/* 443 */       if (((!player.isSneaking()) || (player.getHeldItem() == null)) && (iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, player, side, f, f1, f2)))
/*     */       {
/* 445 */         flag = true;
/*     */       }
/*     */       
/* 448 */       if ((!flag) && (heldStack != null) && ((heldStack.getItem() instanceof ItemBlock)))
/*     */       {
/* 450 */         ItemBlock itemblock = (ItemBlock)heldStack.getItem();
/*     */         
/* 452 */         if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack))
/*     */         {
/* 454 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 459 */     this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f1, f2));
/*     */     
/* 461 */     if ((!flag) && (this.currentGameType != WorldSettings.GameType.SPECTATOR))
/*     */     {
/* 463 */       if (heldStack == null)
/*     */       {
/* 465 */         return false;
/*     */       }
/* 467 */       if (this.currentGameType.isCreative())
/*     */       {
/* 469 */         int i = heldStack.getMetadata();
/* 470 */         int j = heldStack.stackSize;
/* 471 */         boolean flag1 = heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
/* 472 */         heldStack.setItemDamage(i);
/* 473 */         heldStack.stackSize = j;
/* 474 */         return flag1;
/*     */       }
/*     */       
/*     */ 
/* 478 */       return heldStack.onItemUse(player, worldIn, hitPos, side, f, f1, f2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 483 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn)
/*     */   {
/* 493 */     if (this.currentGameType == WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 495 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 499 */     syncCurrentPlayItem();
/* 500 */     this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
/* 501 */     int i = itemStackIn.stackSize;
/* 502 */     ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
/*     */     
/* 504 */     if ((itemstack != itemStackIn) || ((itemstack != null) && (itemstack.stackSize != i)))
/*     */     {
/* 506 */       playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
/*     */       
/* 508 */       if (itemstack.stackSize == 0)
/*     */       {
/* 510 */         playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
/*     */       }
/*     */       
/* 513 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 517 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_)
/*     */   {
/* 524 */     return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void attackEntity(EntityPlayer playerIn, Entity targetEntity)
/*     */   {
/* 532 */     syncCurrentPlayItem();
/* 533 */     this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
/*     */     
/* 535 */     if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
/*     */     {
/* 537 */       playerIn.attackTargetEntityWithCurrentItem(targetEntity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity)
/*     */   {
/* 546 */     syncCurrentPlayItem();
/* 547 */     this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
/* 548 */     return (this.currentGameType != WorldSettings.GameType.SPECTATOR) && (playerIn.interactWith(targetEntity));
/*     */   }
/*     */   
/*     */   public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_)
/*     */   {
/* 553 */     syncCurrentPlayItem();
/* 554 */     Vec3 vec3 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
/* 555 */     this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, vec3));
/* 556 */     return (this.currentGameType != WorldSettings.GameType.SPECTATOR) && (p_178894_2_.interactAt(p_178894_1_, vec3));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn)
/*     */   {
/* 564 */     short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
/* 565 */     ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
/* 566 */     this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
/* 567 */     return itemstack;
/*     */   }
/*     */   
/*     */   public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int p_78753_4_, EntityPlayer playerIn, boolean packet) {
/* 571 */     short var6 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
/* 572 */     ItemStack var7 = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, p_78753_4_, playerIn);
/* 573 */     if (packet) {
/* 574 */       this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, p_78753_4_, var7, var6));
/*     */     }
/* 576 */     return var7;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendEnchantPacket(int p_78756_1_, int p_78756_2_)
/*     */   {
/* 585 */     this.netClientHandler.addToSendQueue(new net.minecraft.network.play.client.C11PacketEnchantItem(p_78756_1_, p_78756_2_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendSlotPacket(ItemStack itemStackIn, int slotId)
/*     */   {
/* 593 */     if (this.currentGameType.isCreative())
/*     */     {
/* 595 */       this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendPacketDropItem(ItemStack itemStackIn)
/*     */   {
/* 604 */     if ((this.currentGameType.isCreative()) && (itemStackIn != null))
/*     */     {
/* 606 */       this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onStoppedUsingItem(EntityPlayer playerIn)
/*     */   {
/* 612 */     syncCurrentPlayItem();
/* 613 */     this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
/* 614 */     playerIn.stopUsingItem();
/*     */   }
/*     */   
/*     */   public boolean gameIsSurvivalOrAdventure()
/*     */   {
/* 619 */     return this.currentGameType.isSurvivalOrAdventure();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNotCreative()
/*     */   {
/* 627 */     return !this.currentGameType.isCreative();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInCreativeMode()
/*     */   {
/* 635 */     return this.currentGameType.isCreative();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean extendedReach()
/*     */   {
/* 643 */     return this.currentGameType.isCreative();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRidingHorse()
/*     */   {
/* 651 */     return (this.mc.thePlayer.isRiding()) && ((this.mc.thePlayer.ridingEntity instanceof net.minecraft.entity.passive.EntityHorse));
/*     */   }
/*     */   
/*     */   public boolean isSpectatorMode()
/*     */   {
/* 656 */     return this.currentGameType == WorldSettings.GameType.SPECTATOR;
/*     */   }
/*     */   
/*     */   public WorldSettings.GameType getCurrentGameType()
/*     */   {
/* 661 */     return this.currentGameType;
/*     */   }
/*     */   
/*     */   public boolean func_181040_m()
/*     */   {
/* 666 */     return this.isHittingBlock;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\PlayerControllerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */