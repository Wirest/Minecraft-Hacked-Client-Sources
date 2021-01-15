/*     */ package net.minecraft.init;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.BlockPumpkin;
/*     */ import net.minecraft.block.BlockSkull;
/*     */ import net.minecraft.block.BlockTNT;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.BehaviorProjectileDispense;
/*     */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.dispenser.IPosition;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBucket;
/*     */ import net.minecraft.item.ItemDye;
/*     */ import net.minecraft.item.ItemMonsterPlacer;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.LoggingPrintStream;
/*     */ import net.minecraft.util.RegistryDefaulted;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class Bootstrap
/*     */ {
/*  57 */   private static final PrintStream SYSOUT = System.out;
/*     */   
/*     */ 
/*  60 */   private static boolean alreadyRegistered = false;
/*  61 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isRegistered()
/*     */   {
/*  68 */     return alreadyRegistered;
/*     */   }
/*     */   
/*     */   static void registerDispenserBehaviors()
/*     */   {
/*  73 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense()
/*     */     {
/*     */       protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */       {
/*  77 */         EntityArrow entityarrow = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
/*  78 */         entityarrow.canBePickedUp = 1;
/*  79 */         return entityarrow;
/*     */       }
/*  81 */     });
/*  82 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense()
/*     */     {
/*     */       protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */       {
/*  86 */         return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
/*     */       }
/*  88 */     });
/*  89 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense()
/*     */     {
/*     */       protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */       {
/*  93 */         return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
/*     */       }
/*  95 */     });
/*  96 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense()
/*     */     {
/*     */       protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */       {
/* 100 */         return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
/*     */       }
/*     */       
/*     */       protected float func_82498_a() {
/* 104 */         return super.func_82498_a() * 0.5F;
/*     */       }
/*     */       
/*     */       protected float func_82500_b() {
/* 108 */         return super.func_82500_b() * 1.25F;
/*     */       }
/* 110 */     });
/* 111 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem()
/*     */     {
/* 113 */       private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
/*     */       
/*     */       public ItemStack dispense(IBlockSource source, final ItemStack stack) {
/* 116 */         ItemPotion.isSplash(stack.getMetadata()) ? new BehaviorProjectileDispense()
/*     */         {
/*     */           protected IProjectile getProjectileEntity(World worldIn, IPosition position)
/*     */           {
/* 120 */             return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
/*     */           }
/*     */           
/*     */           protected float func_82498_a() {
/* 124 */             return super.func_82498_a() * 0.5F;
/*     */           }
/*     */           
/*     */           protected float func_82500_b() {
/* 128 */             return super.func_82500_b() * 1.25F;
/*     */           }
/* 130 */         }.dispense(source, stack) : this.field_150843_b.dispense(source, stack);
/*     */       }
/* 132 */     });
/* 133 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem()
/*     */     {
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */       {
/* 137 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 138 */         double d0 = source.getX() + enumfacing.getFrontOffsetX();
/* 139 */         double d1 = source.getBlockPos().getY() + 0.2F;
/* 140 */         double d2 = source.getZ() + enumfacing.getFrontOffsetZ();
/* 141 */         Entity entity = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), d0, d1, d2);
/*     */         
/* 143 */         if (((entity instanceof EntityLivingBase)) && (stack.hasDisplayName()))
/*     */         {
/* 145 */           ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
/*     */         }
/*     */         
/* 148 */         stack.splitStack(1);
/* 149 */         return stack;
/*     */       }
/* 151 */     });
/* 152 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem()
/*     */     {
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */       {
/* 156 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 157 */         double d0 = source.getX() + enumfacing.getFrontOffsetX();
/* 158 */         double d1 = source.getBlockPos().getY() + 0.2F;
/* 159 */         double d2 = source.getZ() + enumfacing.getFrontOffsetZ();
/* 160 */         EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d1, d2, stack);
/* 161 */         source.getWorld().spawnEntityInWorld(entityfireworkrocket);
/* 162 */         stack.splitStack(1);
/* 163 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 167 */         source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
/*     */       }
/* 169 */     });
/* 170 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem()
/*     */     {
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */       {
/* 174 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 175 */         IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 176 */         double d0 = iposition.getX() + enumfacing.getFrontOffsetX() * 0.3F;
/* 177 */         double d1 = iposition.getY() + enumfacing.getFrontOffsetY() * 0.3F;
/* 178 */         double d2 = iposition.getZ() + enumfacing.getFrontOffsetZ() * 0.3F;
/* 179 */         World world = source.getWorld();
/* 180 */         Random random = world.rand;
/* 181 */         double d3 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetX();
/* 182 */         double d4 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetY();
/* 183 */         double d5 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetZ();
/* 184 */         world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
/* 185 */         stack.splitStack(1);
/* 186 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 190 */         source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
/*     */       }
/* 192 */     });
/* 193 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem()
/*     */     {
/* 195 */       private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
/*     */       
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 198 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 199 */         World world = source.getWorld();
/* 200 */         double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125F;
/* 201 */         double d1 = source.getY() + enumfacing.getFrontOffsetY() * 1.125F;
/* 202 */         double d2 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125F;
/* 203 */         BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/* 204 */         Material material = world.getBlockState(blockpos).getBlock().getMaterial();
/*     */         double d3;
/*     */         double d3;
/* 207 */         if (Material.water.equals(material))
/*     */         {
/* 209 */           d3 = 1.0D;
/*     */         }
/*     */         else
/*     */         {
/* 213 */           if ((!Material.air.equals(material)) || (!Material.water.equals(world.getBlockState(blockpos.down()).getBlock().getMaterial())))
/*     */           {
/* 215 */             return this.field_150842_b.dispense(source, stack);
/*     */           }
/*     */           
/* 218 */           d3 = 0.0D;
/*     */         }
/*     */         
/* 221 */         EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
/* 222 */         world.spawnEntityInWorld(entityboat);
/* 223 */         stack.splitStack(1);
/* 224 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 228 */         source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */       }
/* 230 */     });
/* 231 */     IBehaviorDispenseItem ibehaviordispenseitem = new BehaviorDefaultDispenseItem()
/*     */     {
/* 233 */       private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
/*     */       
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 236 */         ItemBucket itembucket = (ItemBucket)stack.getItem();
/* 237 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */         
/* 239 */         if (itembucket.tryPlaceContainedLiquid(source.getWorld(), blockpos))
/*     */         {
/* 241 */           stack.setItem(Items.bucket);
/* 242 */           stack.stackSize = 1;
/* 243 */           return stack;
/*     */         }
/*     */         
/*     */ 
/* 247 */         return this.field_150841_b.dispense(source, stack);
/*     */       }
/*     */       
/* 250 */     };
/* 251 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, ibehaviordispenseitem);
/* 252 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, ibehaviordispenseitem);
/* 253 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem()
/*     */     {
/* 255 */       private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
/*     */       
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 258 */         World world = source.getWorld();
/* 259 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 260 */         IBlockState iblockstate = world.getBlockState(blockpos);
/* 261 */         Block block = iblockstate.getBlock();
/* 262 */         Material material = block.getMaterial();
/*     */         Item item;
/*     */         Item item;
/* 265 */         if ((Material.water.equals(material)) && ((block instanceof BlockLiquid)) && (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0))
/*     */         {
/* 267 */           item = Items.water_bucket;
/*     */         }
/*     */         else
/*     */         {
/* 271 */           if ((!Material.lava.equals(material)) || (!(block instanceof BlockLiquid)) || (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() != 0))
/*     */           {
/* 273 */             return super.dispenseStack(source, stack);
/*     */           }
/*     */           
/* 276 */           item = Items.lava_bucket;
/*     */         }
/*     */         
/* 279 */         world.setBlockToAir(blockpos);
/*     */         
/* 281 */         if (--stack.stackSize == 0)
/*     */         {
/* 283 */           stack.setItem(item);
/* 284 */           stack.stackSize = 1;
/*     */         }
/* 286 */         else if (((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0)
/*     */         {
/* 288 */           this.field_150840_b.dispense(source, new ItemStack(item));
/*     */         }
/*     */         
/* 291 */         return stack;
/*     */       }
/* 293 */     });
/* 294 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem()
/*     */     {
/* 296 */       private boolean field_150839_b = true;
/*     */       
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 299 */         World world = source.getWorld();
/* 300 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */         
/* 302 */         if (world.isAirBlock(blockpos))
/*     */         {
/* 304 */           world.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */           
/* 306 */           if (stack.attemptDamageItem(1, world.rand))
/*     */           {
/* 308 */             stack.stackSize = 0;
/*     */           }
/*     */         }
/* 311 */         else if (world.getBlockState(blockpos).getBlock() == Blocks.tnt)
/*     */         {
/* 313 */           Blocks.tnt.onBlockDestroyedByPlayer(world, blockpos, Blocks.tnt.getDefaultState().withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
/* 314 */           world.setBlockToAir(blockpos);
/*     */         }
/*     */         else
/*     */         {
/* 318 */           this.field_150839_b = false;
/*     */         }
/*     */         
/* 321 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 325 */         if (this.field_150839_b)
/*     */         {
/* 327 */           source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */         }
/*     */         else
/*     */         {
/* 331 */           source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */         }
/*     */       }
/* 334 */     });
/* 335 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem()
/*     */     {
/* 337 */       private boolean field_150838_b = true;
/*     */       
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 340 */         if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(stack.getMetadata()))
/*     */         {
/* 342 */           World world = source.getWorld();
/* 343 */           BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/*     */           
/* 345 */           if (ItemDye.applyBonemeal(stack, world, blockpos))
/*     */           {
/* 347 */             if (!world.isRemote)
/*     */             {
/* 349 */               world.playAuxSFX(2005, blockpos, 0);
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 354 */             this.field_150838_b = false;
/*     */           }
/*     */           
/* 357 */           return stack;
/*     */         }
/*     */         
/*     */ 
/* 361 */         return super.dispenseStack(source, stack);
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source)
/*     */       {
/* 366 */         if (this.field_150838_b)
/*     */         {
/* 368 */           source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */         }
/*     */         else
/*     */         {
/* 372 */           source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */         }
/*     */       }
/* 375 */     });
/* 376 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem()
/*     */     {
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
/*     */       {
/* 380 */         World world = source.getWorld();
/* 381 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 382 */         EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, null);
/* 383 */         world.spawnEntityInWorld(entitytntprimed);
/* 384 */         world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
/* 385 */         stack.stackSize -= 1;
/* 386 */         return stack;
/*     */       }
/* 388 */     });
/* 389 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem()
/*     */     {
/* 391 */       private boolean field_179240_b = true;
/*     */       
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 394 */         World world = source.getWorld();
/* 395 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 396 */         BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/* 397 */         BlockSkull blockskull = Blocks.skull;
/*     */         
/* 399 */         if ((world.isAirBlock(blockpos)) && (blockskull.canDispenserPlace(world, blockpos, stack)))
/*     */         {
/* 401 */           if (!world.isRemote)
/*     */           {
/* 403 */             world.setBlockState(blockpos, blockskull.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
/* 404 */             TileEntity tileentity = world.getTileEntity(blockpos);
/*     */             
/* 406 */             if ((tileentity instanceof TileEntitySkull))
/*     */             {
/* 408 */               if (stack.getMetadata() == 3)
/*     */               {
/* 410 */                 GameProfile gameprofile = null;
/*     */                 
/* 412 */                 if (stack.hasTagCompound())
/*     */                 {
/* 414 */                   NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */                   
/* 416 */                   if (nbttagcompound.hasKey("SkullOwner", 10))
/*     */                   {
/* 418 */                     gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*     */                   }
/* 420 */                   else if (nbttagcompound.hasKey("SkullOwner", 8))
/*     */                   {
/* 422 */                     String s = nbttagcompound.getString("SkullOwner");
/*     */                     
/* 424 */                     if (!StringUtils.isNullOrEmpty(s))
/*     */                     {
/* 426 */                       gameprofile = new GameProfile(null, s);
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 
/* 431 */                 ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
/*     */               }
/*     */               else
/*     */               {
/* 435 */                 ((TileEntitySkull)tileentity).setType(stack.getMetadata());
/*     */               }
/*     */               
/* 438 */               ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
/* 439 */               Blocks.skull.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
/*     */             }
/*     */             
/* 442 */             stack.stackSize -= 1;
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 447 */           this.field_179240_b = false;
/*     */         }
/*     */         
/* 450 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 454 */         if (this.field_179240_b)
/*     */         {
/* 456 */           source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */         }
/*     */         else
/*     */         {
/* 460 */           source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */         }
/*     */       }
/* 463 */     });
/* 464 */     BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem()
/*     */     {
/* 466 */       private boolean field_179241_b = true;
/*     */       
/*     */       protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 469 */         World world = source.getWorld();
/* 470 */         BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
/* 471 */         BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.pumpkin;
/*     */         
/* 473 */         if ((world.isAirBlock(blockpos)) && (blockpumpkin.canDispenserPlace(world, blockpos)))
/*     */         {
/* 475 */           if (!world.isRemote)
/*     */           {
/* 477 */             world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
/*     */           }
/*     */           
/* 480 */           stack.stackSize -= 1;
/*     */         }
/*     */         else
/*     */         {
/* 484 */           this.field_179241_b = false;
/*     */         }
/*     */         
/* 487 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/* 491 */         if (this.field_179241_b)
/*     */         {
/* 493 */           source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */         }
/*     */         else
/*     */         {
/* 497 */           source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void register()
/*     */   {
/* 508 */     if (!alreadyRegistered)
/*     */     {
/* 510 */       alreadyRegistered = true;
/*     */       
/* 512 */       if (LOGGER.isDebugEnabled())
/*     */       {
/* 514 */         redirectOutputToLog();
/*     */       }
/*     */       
/* 517 */       Block.registerBlocks();
/* 518 */       BlockFire.init();
/* 519 */       Item.registerItems();
/* 520 */       StatList.init();
/* 521 */       registerDispenserBehaviors();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void redirectOutputToLog()
/*     */   {
/* 530 */     System.setErr(new LoggingPrintStream("STDERR", System.err));
/* 531 */     System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
/*     */   }
/*     */   
/*     */   public static void printToSYSOUT(String p_179870_0_)
/*     */   {
/* 536 */     SYSOUT.println(p_179870_0_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\init\Bootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */