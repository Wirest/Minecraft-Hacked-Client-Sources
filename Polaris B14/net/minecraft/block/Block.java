/*      */ package net.minecraft.block;
/*      */ 
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.material.MapColor;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.BlockState;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.creativetab.CreativeTabs;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemBlock;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ObjectIntIdentityMap;
/*      */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.Explosion;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import rip.jutting.polaris.event.events.EventBoundingBox;
/*      */ import rip.jutting.polaris.event.events.EventCollide;
/*      */ import rip.jutting.polaris.event.events.EventEntityCollision;
/*      */ import rip.jutting.polaris.module.render.XRay;
/*      */ import rip.jutting.polaris.utils.Location;
/*      */ 
/*      */ public class Block
/*      */ {
/*   46 */   private static final ResourceLocation AIR_ID = new ResourceLocation("air");
/*   47 */   public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blockRegistry = new RegistryNamespacedDefaultedByKey(AIR_ID);
/*   48 */   public static final ObjectIntIdentityMap BLOCK_STATE_IDS = new ObjectIntIdentityMap();
/*      */   private CreativeTabs displayOnCreativeTab;
/*   50 */   public static final SoundType soundTypeStone = new SoundType("stone", 1.0F, 1.0F);
/*      */   
/*      */ 
/*   53 */   public static final SoundType soundTypeWood = new SoundType("wood", 1.0F, 1.0F);
/*      */   
/*      */ 
/*   56 */   public static final SoundType soundTypeGravel = new SoundType("gravel", 1.0F, 1.0F);
/*   57 */   public static final SoundType soundTypeGrass = new SoundType("grass", 1.0F, 1.0F);
/*   58 */   public static final SoundType soundTypePiston = new SoundType("stone", 1.0F, 1.0F);
/*   59 */   public static final SoundType soundTypeMetal = new SoundType("stone", 1.0F, 1.5F);
/*   60 */   public static final SoundType soundTypeGlass = new SoundType("stone", 1.0F, 1.0F)
/*      */   {
/*      */     public String getBreakSound()
/*      */     {
/*   64 */       return "dig.glass";
/*      */     }
/*      */     
/*      */     public String getPlaceSound() {
/*   68 */       return "step.stone";
/*      */     }
/*      */   };
/*   71 */   public static final SoundType soundTypeCloth = new SoundType("cloth", 1.0F, 1.0F);
/*   72 */   public static final SoundType soundTypeSand = new SoundType("sand", 1.0F, 1.0F);
/*   73 */   public static final SoundType soundTypeSnow = new SoundType("snow", 1.0F, 1.0F);
/*   74 */   public static final SoundType soundTypeLadder = new SoundType("ladder", 1.0F, 1.0F)
/*      */   {
/*      */     public String getBreakSound()
/*      */     {
/*   78 */       return "dig.wood";
/*      */     }
/*      */   };
/*   81 */   public static final SoundType soundTypeAnvil = new SoundType("anvil", 0.3F, 1.0F)
/*      */   {
/*      */     public String getBreakSound()
/*      */     {
/*   85 */       return "dig.stone";
/*      */     }
/*      */     
/*      */     public String getPlaceSound() {
/*   89 */       return "random.anvil_land";
/*      */     }
/*      */   };
/*   92 */   public static final SoundType SLIME_SOUND = new SoundType("slime", 1.0F, 1.0F)
/*      */   {
/*      */     public String getBreakSound()
/*      */     {
/*   96 */       return "mob.slime.big";
/*      */     }
/*      */     
/*      */     public String getPlaceSound() {
/*  100 */       return "mob.slime.big";
/*      */     }
/*      */     
/*      */     public String getStepSound() {
/*  104 */       return "mob.slime.small";
/*      */     }
/*      */   };
/*      */   
/*      */   protected boolean fullBlock;
/*      */   
/*      */   protected int lightOpacity;
/*      */   
/*      */   protected boolean translucent;
/*      */   
/*      */   protected int lightValue;
/*      */   
/*      */   protected boolean useNeighborBrightness;
/*      */   
/*      */   protected float blockHardness;
/*      */   
/*      */   protected float blockResistance;
/*      */   
/*      */   protected boolean enableStats;
/*      */   
/*      */   protected boolean needsRandomTick;
/*      */   
/*      */   protected boolean isBlockContainer;
/*      */   
/*      */   protected double minX;
/*      */   
/*      */   protected double minY;
/*      */   
/*      */   protected double minZ;
/*      */   
/*      */   protected double maxX;
/*      */   
/*      */   protected double maxY;
/*      */   
/*      */   protected double maxZ;
/*      */   
/*      */   public SoundType stepSound;
/*      */   
/*      */   public float blockParticleGravity;
/*      */   
/*      */   protected final Material blockMaterial;
/*      */   
/*      */   protected final MapColor field_181083_K;
/*      */   
/*      */   public float slipperiness;
/*      */   
/*      */   protected final BlockState blockState;
/*      */   
/*      */   private IBlockState defaultBlockState;
/*      */   
/*      */   private String unlocalizedName;
/*      */   
/*      */ 
/*      */   public static int getIdFromBlock(Block blockIn)
/*      */   {
/*  159 */     return blockRegistry.getIDForObject(blockIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getStateId(IBlockState state)
/*      */   {
/*  167 */     Block block = state.getBlock();
/*  168 */     return getIdFromBlock(block) + (block.getMetaFromState(state) << 12);
/*      */   }
/*      */   
/*      */   public static Block getBlockById(int id)
/*      */   {
/*  173 */     return (Block)blockRegistry.getObjectById(id);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static IBlockState getStateById(int id)
/*      */   {
/*  181 */     int i = id & 0xFFF;
/*  182 */     int j = id >> 12 & 0xF;
/*  183 */     return getBlockById(i).getStateFromMeta(j);
/*      */   }
/*      */   
/*      */   public static Block getBlockFromItem(Item itemIn)
/*      */   {
/*  188 */     return (itemIn instanceof ItemBlock) ? ((ItemBlock)itemIn).getBlock() : null;
/*      */   }
/*      */   
/*      */   public static Block getBlockFromName(String name)
/*      */   {
/*  193 */     ResourceLocation resourcelocation = new ResourceLocation(name);
/*      */     
/*  195 */     if (blockRegistry.containsKey(resourcelocation))
/*      */     {
/*  197 */       return (Block)blockRegistry.getObject(resourcelocation);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  203 */       return (Block)blockRegistry.getObjectById(Integer.parseInt(name));
/*      */     }
/*      */     catch (NumberFormatException var3) {}
/*      */     
/*  207 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isFullBlock()
/*      */   {
/*  214 */     return this.fullBlock;
/*      */   }
/*      */   
/*      */   public int getLightOpacity()
/*      */   {
/*  219 */     return this.lightOpacity;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTranslucent()
/*      */   {
/*  227 */     return this.translucent;
/*      */   }
/*      */   
/*      */   public int getLightValue()
/*      */   {
/*  232 */     return this.lightValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseNeighborBrightness()
/*      */   {
/*  240 */     return this.useNeighborBrightness;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Material getMaterial()
/*      */   {
/*  248 */     return this.blockMaterial;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapColor getMapColor(IBlockState state)
/*      */   {
/*  256 */     return this.field_181083_K;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IBlockState getStateFromMeta(int meta)
/*      */   {
/*  264 */     return getDefaultState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMetaFromState(IBlockState state)
/*      */   {
/*  272 */     if ((state != null) && (!state.getPropertyNames().isEmpty()))
/*      */     {
/*  274 */       throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
/*      */     }
/*      */     
/*      */ 
/*  278 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*      */   {
/*  288 */     return state;
/*      */   }
/*      */   
/*      */   public Block(Material p_i46399_1_, MapColor p_i46399_2_)
/*      */   {
/*  293 */     this.enableStats = true;
/*  294 */     this.stepSound = soundTypeStone;
/*  295 */     this.blockParticleGravity = 1.0F;
/*  296 */     this.slipperiness = 0.6F;
/*  297 */     this.blockMaterial = p_i46399_1_;
/*  298 */     this.field_181083_K = p_i46399_2_;
/*  299 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  300 */     this.fullBlock = isOpaqueCube();
/*  301 */     this.lightOpacity = (isOpaqueCube() ? 255 : 0);
/*  302 */     this.translucent = (!p_i46399_1_.blocksLight());
/*  303 */     this.blockState = createBlockState();
/*  304 */     setDefaultState(this.blockState.getBaseState());
/*      */   }
/*      */   
/*      */   protected Block(Material materialIn)
/*      */   {
/*  309 */     this(materialIn, materialIn.getMaterialMapColor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Block setStepSound(SoundType sound)
/*      */   {
/*  317 */     this.stepSound = sound;
/*  318 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Block setLightOpacity(int opacity)
/*      */   {
/*  326 */     this.lightOpacity = opacity;
/*  327 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Block setLightLevel(float value)
/*      */   {
/*  336 */     this.lightValue = ((int)(15.0F * value));
/*  337 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Block setResistance(float resistance)
/*      */   {
/*  345 */     this.blockResistance = (resistance * 3.0F);
/*  346 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBlockNormalCube()
/*      */   {
/*  354 */     return (this.blockMaterial.blocksMovement()) && (isFullCube());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNormalCube()
/*      */   {
/*  363 */     return (this.blockMaterial.isOpaque()) && (isFullCube()) && (!canProvidePower());
/*      */   }
/*      */   
/*      */   public boolean isVisuallyOpaque()
/*      */   {
/*  368 */     return (this.blockMaterial.blocksMovement()) && (isFullCube());
/*      */   }
/*      */   
/*      */   public boolean isFullCube()
/*      */   {
/*  373 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*      */   {
/*  378 */     return !this.blockMaterial.blocksMovement();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRenderType()
/*      */   {
/*  386 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReplaceable(World worldIn, BlockPos pos)
/*      */   {
/*  394 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Block setHardness(float hardness)
/*      */   {
/*  402 */     this.blockHardness = hardness;
/*      */     
/*  404 */     if (this.blockResistance < hardness * 5.0F)
/*      */     {
/*  406 */       this.blockResistance = (hardness * 5.0F);
/*      */     }
/*      */     
/*  409 */     return this;
/*      */   }
/*      */   
/*      */   protected Block setBlockUnbreakable()
/*      */   {
/*  414 */     setHardness(-1.0F);
/*  415 */     return this;
/*      */   }
/*      */   
/*      */   public float getBlockHardness(World worldIn, BlockPos pos)
/*      */   {
/*  420 */     return this.blockHardness;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Block setTickRandomly(boolean shouldTick)
/*      */   {
/*  428 */     this.needsRandomTick = shouldTick;
/*  429 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getTickRandomly()
/*      */   {
/*  438 */     return this.needsRandomTick;
/*      */   }
/*      */   
/*      */   public boolean hasTileEntity()
/*      */   {
/*  443 */     return this.isBlockContainer;
/*      */   }
/*      */   
/*      */   protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
/*      */   {
/*  448 */     this.minX = minX;
/*  449 */     this.minY = minY;
/*  450 */     this.minZ = minZ;
/*  451 */     this.maxX = maxX;
/*  452 */     this.maxY = maxY;
/*  453 */     this.maxZ = maxZ;
/*      */   }
/*      */   
/*      */   public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
/*  457 */     Block var3 = worldIn.getBlockState(pos).getBlock();
/*  458 */     int var4 = worldIn.getCombinedLight(pos, var3.getLightValue());
/*  459 */     if ((var4 == 0) && ((var3 instanceof BlockSlab))) {
/*  460 */       pos = pos.down();
/*  461 */       var3 = worldIn.getBlockState(pos).getBlock();
/*  462 */       return worldIn.getCombinedLight(pos, var3.getLightValue());
/*      */     }
/*  464 */     return var4;
/*      */   }
/*      */   
/*      */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/*  468 */     return (this == getBlockById(56)) || (this == getBlockById(73)) || (this == getBlockById(15)) || (this == getBlockById(129)) || (this == getBlockById(14)) || ((!XRay.enabled) && (((side == EnumFacing.DOWN) && (this.minY > 0.0D)) || ((side == EnumFacing.UP) && (this.maxY < 1.0D)) || ((side == EnumFacing.NORTH) && (this.minZ > 0.0D)) || ((side == EnumFacing.SOUTH) && (this.maxZ < 1.0D)) || ((side == EnumFacing.WEST) && (this.minX > 0.0D)) || ((side == EnumFacing.EAST) && (this.maxX < 1.0D)) || (!worldIn.getBlockState(pos).getBlock().isOpaqueCube())));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*      */   {
/*  476 */     return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
/*      */   }
/*      */   
/*      */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*      */   {
/*  481 */     return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*      */   {
/*  489 */     AxisAlignedBB axisalignedbb = getCollisionBoundingBox(worldIn, pos, state);
/*      */     
/*  491 */     EventEntityCollision event = new EventEntityCollision(collidingEntity, new Location(pos.getX(), pos.getY(), pos.getZ()), axisalignedbb, this);
/*  492 */     event.call();
/*  493 */     axisalignedbb = event.getBoundingBox();
/*      */     
/*  495 */     if (event.isCancelled()) {
/*  496 */       return;
/*      */     }
/*      */     
/*  499 */     EventBoundingBox eventt = new EventBoundingBox(this, pos, axisalignedbb, pos.getX(), pos.getY(), pos.getZ());
/*  500 */     if (collidingEntity == Minecraft.getMinecraft().thePlayer) {
/*  501 */       eventt.call();
/*      */     }
/*  503 */     axisalignedbb = eventt.getBoundingBox();
/*      */     
/*  505 */     if (eventt.isCancelled()) {
/*  506 */       return;
/*      */     }
/*      */     
/*  509 */     EventCollide eventCollide = new EventCollide(collidingEntity, pos.getX(), pos.getY(), pos.getZ(), axisalignedbb, this);
/*  510 */     eventCollide.call();
/*  511 */     axisalignedbb = eventCollide.getBoundingBox();
/*      */     
/*  513 */     if (eventCollide.isCancelled()) {
/*  514 */       return;
/*      */     }
/*  516 */     if ((axisalignedbb != null) && (mask.intersectsWith(axisalignedbb)))
/*      */     {
/*  518 */       list.add(axisalignedbb);
/*      */     }
/*      */   }
/*      */   
/*      */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*      */   {
/*  524 */     return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOpaqueCube()
/*      */   {
/*  532 */     return true;
/*      */   }
/*      */   
/*      */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
/*      */   {
/*  537 */     return isCollidable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCollidable()
/*      */   {
/*  545 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
/*      */   {
/*  553 */     updateTick(worldIn, pos, state, random);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int tickRate(World worldIn)
/*      */   {
/*  583 */     return 10;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public int quantityDropped(Random random)
/*      */   {
/*  599 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*      */   {
/*  607 */     return Item.getItemFromBlock(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos)
/*      */   {
/*  615 */     float f = getBlockHardness(worldIn, pos);
/*  616 */     return !playerIn.canHarvestBlock(this) ? playerIn.getToolDigEfficiency(this) / f / 100.0F : f < 0.0F ? 0.0F : playerIn.getToolDigEfficiency(this) / f / 30.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture)
/*      */   {
/*  624 */     dropBlockAsItemWithChance(worldIn, pos, state, 1.0F, forture);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*      */   {
/*  632 */     if (!worldIn.isRemote)
/*      */     {
/*  634 */       int i = quantityDroppedWithBonus(fortune, worldIn.rand);
/*      */       
/*  636 */       for (int j = 0; j < i; j++)
/*      */       {
/*  638 */         if (worldIn.rand.nextFloat() <= chance)
/*      */         {
/*  640 */           Item item = getItemDropped(state, worldIn.rand, fortune);
/*      */           
/*  642 */           if (item != null)
/*      */           {
/*  644 */             spawnAsEntity(worldIn, pos, new ItemStack(item, 1, damageDropped(state)));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack)
/*      */   {
/*  656 */     if ((!worldIn.isRemote) && (worldIn.getGameRules().getBoolean("doTileDrops")))
/*      */     {
/*  658 */       float f = 0.5F;
/*  659 */       double d0 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
/*  660 */       double d1 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
/*  661 */       double d2 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
/*  662 */       EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
/*  663 */       entityitem.setDefaultPickupDelay();
/*  664 */       worldIn.spawnEntityInWorld(entityitem);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount)
/*      */   {
/*  673 */     if (!worldIn.isRemote)
/*      */     {
/*  675 */       while (amount > 0)
/*      */       {
/*  677 */         int i = EntityXPOrb.getXPSplit(amount);
/*  678 */         amount -= i;
/*  679 */         worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int damageDropped(IBlockState state)
/*      */   {
/*  690 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getExplosionResistance(Entity exploder)
/*      */   {
/*  698 */     return this.blockResistance / 5.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
/*      */   {
/*  706 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  707 */     start = start.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
/*  708 */     end = end.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
/*  709 */     Vec3 vec3 = start.getIntermediateWithXValue(end, this.minX);
/*  710 */     Vec3 vec31 = start.getIntermediateWithXValue(end, this.maxX);
/*  711 */     Vec3 vec32 = start.getIntermediateWithYValue(end, this.minY);
/*  712 */     Vec3 vec33 = start.getIntermediateWithYValue(end, this.maxY);
/*  713 */     Vec3 vec34 = start.getIntermediateWithZValue(end, this.minZ);
/*  714 */     Vec3 vec35 = start.getIntermediateWithZValue(end, this.maxZ);
/*      */     
/*  716 */     if (!isVecInsideYZBounds(vec3))
/*      */     {
/*  718 */       vec3 = null;
/*      */     }
/*      */     
/*  721 */     if (!isVecInsideYZBounds(vec31))
/*      */     {
/*  723 */       vec31 = null;
/*      */     }
/*      */     
/*  726 */     if (!isVecInsideXZBounds(vec32))
/*      */     {
/*  728 */       vec32 = null;
/*      */     }
/*      */     
/*  731 */     if (!isVecInsideXZBounds(vec33))
/*      */     {
/*  733 */       vec33 = null;
/*      */     }
/*      */     
/*  736 */     if (!isVecInsideXYBounds(vec34))
/*      */     {
/*  738 */       vec34 = null;
/*      */     }
/*      */     
/*  741 */     if (!isVecInsideXYBounds(vec35))
/*      */     {
/*  743 */       vec35 = null;
/*      */     }
/*      */     
/*  746 */     Vec3 vec36 = null;
/*      */     
/*  748 */     if ((vec3 != null) && ((vec36 == null) || (start.squareDistanceTo(vec3) < start.squareDistanceTo(vec36))))
/*      */     {
/*  750 */       vec36 = vec3;
/*      */     }
/*      */     
/*  753 */     if ((vec31 != null) && ((vec36 == null) || (start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36))))
/*      */     {
/*  755 */       vec36 = vec31;
/*      */     }
/*      */     
/*  758 */     if ((vec32 != null) && ((vec36 == null) || (start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36))))
/*      */     {
/*  760 */       vec36 = vec32;
/*      */     }
/*      */     
/*  763 */     if ((vec33 != null) && ((vec36 == null) || (start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36))))
/*      */     {
/*  765 */       vec36 = vec33;
/*      */     }
/*      */     
/*  768 */     if ((vec34 != null) && ((vec36 == null) || (start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36))))
/*      */     {
/*  770 */       vec36 = vec34;
/*      */     }
/*      */     
/*  773 */     if ((vec35 != null) && ((vec36 == null) || (start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36))))
/*      */     {
/*  775 */       vec36 = vec35;
/*      */     }
/*      */     
/*  778 */     if (vec36 == null)
/*      */     {
/*  780 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  784 */     EnumFacing enumfacing = null;
/*      */     
/*  786 */     if (vec36 == vec3)
/*      */     {
/*  788 */       enumfacing = EnumFacing.WEST;
/*      */     }
/*      */     
/*  791 */     if (vec36 == vec31)
/*      */     {
/*  793 */       enumfacing = EnumFacing.EAST;
/*      */     }
/*      */     
/*  796 */     if (vec36 == vec32)
/*      */     {
/*  798 */       enumfacing = EnumFacing.DOWN;
/*      */     }
/*      */     
/*  801 */     if (vec36 == vec33)
/*      */     {
/*  803 */       enumfacing = EnumFacing.UP;
/*      */     }
/*      */     
/*  806 */     if (vec36 == vec34)
/*      */     {
/*  808 */       enumfacing = EnumFacing.NORTH;
/*      */     }
/*      */     
/*  811 */     if (vec36 == vec35)
/*      */     {
/*  813 */       enumfacing = EnumFacing.SOUTH;
/*      */     }
/*      */     
/*  816 */     return new MovingObjectPosition(vec36.addVector(pos.getX(), pos.getY(), pos.getZ()), enumfacing, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isVecInsideYZBounds(Vec3 point)
/*      */   {
/*  825 */     return point != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isVecInsideXZBounds(Vec3 point)
/*      */   {
/*  833 */     return point != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isVecInsideXYBounds(Vec3 point)
/*      */   {
/*  841 */     return point != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public EnumWorldBlockLayer getBlockLayer()
/*      */   {
/*  853 */     return EnumWorldBlockLayer.SOLID;
/*      */   }
/*      */   
/*      */   public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
/*      */   {
/*  858 */     return canPlaceBlockOnSide(worldIn, pos, side);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
/*      */   {
/*  866 */     return canPlaceBlockAt(worldIn, pos);
/*      */   }
/*      */   
/*      */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*      */   {
/*  871 */     return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable();
/*      */   }
/*      */   
/*      */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*      */   {
/*  876 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*      */   {
/*  892 */     return getStateFromMeta(meta);
/*      */   }
/*      */   
/*      */ 
/*      */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {}
/*      */   
/*      */ 
/*      */   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
/*      */   {
/*  901 */     return motion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMinX()
/*      */   {
/*  913 */     return this.minX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMaxX()
/*      */   {
/*  921 */     return this.maxX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMinY()
/*      */   {
/*  929 */     return this.minY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMaxY()
/*      */   {
/*  937 */     return this.maxY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMinZ()
/*      */   {
/*  945 */     return this.minZ;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final double getBlockBoundsMaxZ()
/*      */   {
/*  953 */     return this.maxZ;
/*      */   }
/*      */   
/*      */   public int getBlockColor()
/*      */   {
/*  958 */     return 16777215;
/*      */   }
/*      */   
/*      */   public int getRenderColor(IBlockState state)
/*      */   {
/*  963 */     return 16777215;
/*      */   }
/*      */   
/*      */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*      */   {
/*  968 */     return 16777215;
/*      */   }
/*      */   
/*      */   public final int colorMultiplier(IBlockAccess worldIn, BlockPos pos)
/*      */   {
/*  973 */     return colorMultiplier(worldIn, pos, 0);
/*      */   }
/*      */   
/*      */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*      */   {
/*  978 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canProvidePower()
/*      */   {
/*  986 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*      */   {
/*  998 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBlockBoundsForItemRender() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
/*      */   {
/* 1010 */     player.triggerAchievement(net.minecraft.stats.StatList.mineBlockStatArray[getIdFromBlock(this)]);
/* 1011 */     player.addExhaustion(0.025F);
/*      */     
/* 1013 */     if ((canSilkHarvest()) && (EnchantmentHelper.getSilkTouchModifier(player)))
/*      */     {
/* 1015 */       ItemStack itemstack = createStackedBlock(state);
/*      */       
/* 1017 */       if (itemstack != null)
/*      */       {
/* 1019 */         spawnAsEntity(worldIn, pos, itemstack);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1024 */       int i = EnchantmentHelper.getFortuneModifier(player);
/* 1025 */       dropBlockAsItem(worldIn, pos, state, i);
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean canSilkHarvest()
/*      */   {
/* 1031 */     return (isFullCube()) && (!this.isBlockContainer);
/*      */   }
/*      */   
/*      */   protected ItemStack createStackedBlock(IBlockState state)
/*      */   {
/* 1036 */     int i = 0;
/* 1037 */     Item item = Item.getItemFromBlock(this);
/*      */     
/* 1039 */     if ((item != null) && (item.getHasSubtypes()))
/*      */     {
/* 1041 */       i = getMetaFromState(state);
/*      */     }
/*      */     
/* 1044 */     return new ItemStack(item, 1, i);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int quantityDroppedWithBonus(int fortune, Random random)
/*      */   {
/* 1052 */     return quantityDropped(random);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean func_181623_g()
/*      */   {
/* 1064 */     return (!this.blockMaterial.isSolid()) && (!this.blockMaterial.isLiquid());
/*      */   }
/*      */   
/*      */   public Block setUnlocalizedName(String name)
/*      */   {
/* 1069 */     this.unlocalizedName = name;
/* 1070 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLocalizedName()
/*      */   {
/* 1078 */     return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUnlocalizedName()
/*      */   {
/* 1086 */     return "tile." + this.unlocalizedName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
/*      */   {
/* 1094 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getEnableStats()
/*      */   {
/* 1102 */     return this.enableStats;
/*      */   }
/*      */   
/*      */   protected Block disableStats()
/*      */   {
/* 1107 */     this.enableStats = false;
/* 1108 */     return this;
/*      */   }
/*      */   
/*      */   public int getMobilityFlag()
/*      */   {
/* 1113 */     return this.blockMaterial.getMaterialMobility();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getAmbientOcclusionLightValue()
/*      */   {
/* 1121 */     return isBlockNormalCube() ? 0.2F : 1.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
/*      */   {
/* 1129 */     entityIn.fall(fallDistance, 1.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onLanded(World worldIn, Entity entityIn)
/*      */   {
/* 1138 */     entityIn.motionY = 0.0D;
/*      */   }
/*      */   
/*      */   public Item getItem(World worldIn, BlockPos pos)
/*      */   {
/* 1143 */     return Item.getItemFromBlock(this);
/*      */   }
/*      */   
/*      */   public int getDamageValue(World worldIn, BlockPos pos)
/*      */   {
/* 1148 */     return damageDropped(worldIn.getBlockState(pos));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*      */   {
/* 1156 */     list.add(new ItemStack(itemIn, 1, 0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CreativeTabs getCreativeTabToDisplayOn()
/*      */   {
/* 1164 */     return this.displayOnCreativeTab;
/*      */   }
/*      */   
/*      */   public Block setCreativeTab(CreativeTabs tab)
/*      */   {
/* 1169 */     this.displayOnCreativeTab = tab;
/* 1170 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fillWithRain(World worldIn, BlockPos pos) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFlowerPot()
/*      */   {
/* 1189 */     return false;
/*      */   }
/*      */   
/*      */   public boolean requiresUpdates()
/*      */   {
/* 1194 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canDropFromExplosion(Explosion explosionIn)
/*      */   {
/* 1202 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isAssociatedBlock(Block other)
/*      */   {
/* 1207 */     return this == other;
/*      */   }
/*      */   
/*      */   public static boolean isEqualTo(Block blockIn, Block other)
/*      */   {
/* 1212 */     return blockIn == other;
/*      */   }
/*      */   
/*      */   public boolean hasComparatorInputOverride()
/*      */   {
/* 1217 */     return false;
/*      */   }
/*      */   
/*      */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*      */   {
/* 1222 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public IBlockState getStateForEntityRender(IBlockState state)
/*      */   {
/* 1230 */     return state;
/*      */   }
/*      */   
/*      */   protected BlockState createBlockState()
/*      */   {
/* 1235 */     return new BlockState(this, new IProperty[0]);
/*      */   }
/*      */   
/*      */   public BlockState getBlockState()
/*      */   {
/* 1240 */     return this.blockState;
/*      */   }
/*      */   
/*      */   protected final void setDefaultState(IBlockState state)
/*      */   {
/* 1245 */     this.defaultBlockState = state;
/*      */   }
/*      */   
/*      */   public final IBlockState getDefaultState()
/*      */   {
/* 1250 */     return this.defaultBlockState;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EnumOffsetType getOffsetType()
/*      */   {
/* 1258 */     return EnumOffsetType.NONE;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1263 */     return "Block{" + blockRegistry.getNameForObject(this) + "}";
/*      */   }
/*      */   
/*      */   public static void registerBlocks()
/*      */   {
/* 1268 */     registerBlock(0, AIR_ID, new BlockAir().setUnlocalizedName("air"));
/* 1269 */     registerBlock(1, "stone", new BlockStone().setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stone"));
/* 1270 */     registerBlock(2, "grass", new BlockGrass().setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("grass"));
/* 1271 */     registerBlock(3, "dirt", new BlockDirt().setHardness(0.5F).setStepSound(soundTypeGravel).setUnlocalizedName("dirt"));
/* 1272 */     Block block = new Block(Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
/* 1273 */     registerBlock(4, "cobblestone", block);
/* 1274 */     Block block1 = new BlockPlanks().setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("wood");
/* 1275 */     registerBlock(5, "planks", block1);
/* 1276 */     registerBlock(6, "sapling", new BlockSapling().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("sapling"));
/* 1277 */     registerBlock(7, "bedrock", new Block(Material.rock).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
/* 1278 */     registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.water).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
/* 1279 */     registerBlock(9, "water", new BlockStaticLiquid(Material.water).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
/* 1280 */     registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.lava).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
/* 1281 */     registerBlock(11, "lava", new BlockStaticLiquid(Material.lava).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
/* 1282 */     registerBlock(12, "sand", new BlockSand().setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("sand"));
/* 1283 */     registerBlock(13, "gravel", new BlockGravel().setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("gravel"));
/* 1284 */     registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreGold"));
/* 1285 */     registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreIron"));
/* 1286 */     registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreCoal"));
/* 1287 */     registerBlock(17, "log", new BlockOldLog().setUnlocalizedName("log"));
/* 1288 */     registerBlock(18, "leaves", new BlockOldLeaf().setUnlocalizedName("leaves"));
/* 1289 */     registerBlock(19, "sponge", new BlockSponge().setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("sponge"));
/* 1290 */     registerBlock(20, "glass", new BlockGlass(Material.glass, false).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("glass"));
/* 1291 */     registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreLapis"));
/* 1292 */     registerBlock(22, "lapis_block", new Block(Material.iron, MapColor.lapisColor).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
/* 1293 */     registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dispenser"));
/* 1294 */     Block block2 = new BlockSandStone().setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("sandStone");
/* 1295 */     registerBlock(24, "sandstone", block2);
/* 1296 */     registerBlock(25, "noteblock", new BlockNote().setHardness(0.8F).setUnlocalizedName("musicBlock"));
/* 1297 */     registerBlock(26, "bed", new BlockBed().setStepSound(soundTypeWood).setHardness(0.2F).setUnlocalizedName("bed").disableStats());
/* 1298 */     registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("goldenRail"));
/* 1299 */     registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("detectorRail"));
/* 1300 */     registerBlock(29, "sticky_piston", new BlockPistonBase(true).setUnlocalizedName("pistonStickyBase"));
/* 1301 */     registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0F).setUnlocalizedName("web"));
/* 1302 */     registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tallgrass"));
/* 1303 */     registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("deadbush"));
/* 1304 */     registerBlock(33, "piston", new BlockPistonBase(false).setUnlocalizedName("pistonBase"));
/* 1305 */     registerBlock(34, "piston_head", new BlockPistonExtension().setUnlocalizedName("pistonBase"));
/* 1306 */     registerBlock(35, "wool", new BlockColored(Material.cloth).setHardness(0.8F).setStepSound(soundTypeCloth).setUnlocalizedName("cloth"));
/* 1307 */     registerBlock(36, "piston_extension", new BlockPistonMoving());
/* 1308 */     registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower1"));
/* 1309 */     registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower2"));
/* 1310 */     Block block3 = new BlockMushroom().setHardness(0.0F).setStepSound(soundTypeGrass).setLightLevel(0.125F).setUnlocalizedName("mushroom");
/* 1311 */     registerBlock(39, "brown_mushroom", block3);
/* 1312 */     Block block4 = new BlockMushroom().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("mushroom");
/* 1313 */     registerBlock(40, "red_mushroom", block4);
/* 1314 */     registerBlock(41, "gold_block", new Block(Material.iron, MapColor.goldColor).setHardness(3.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.tabBlock));
/* 1315 */     registerBlock(42, "iron_block", new Block(Material.iron, MapColor.ironColor).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.tabBlock));
/* 1316 */     registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
/* 1317 */     registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
/* 1318 */     Block block5 = new Block(Material.rock, MapColor.redColor).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
/* 1319 */     registerBlock(45, "brick_block", block5);
/* 1320 */     registerBlock(46, "tnt", new BlockTNT().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tnt"));
/* 1321 */     registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5F).setStepSound(soundTypeWood).setUnlocalizedName("bookshelf"));
/* 1322 */     registerBlock(48, "mossy_cobblestone", new Block(Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
/* 1323 */     registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0F).setResistance(2000.0F).setStepSound(soundTypePiston).setUnlocalizedName("obsidian"));
/* 1324 */     registerBlock(50, "torch", new BlockTorch().setHardness(0.0F).setLightLevel(0.9375F).setStepSound(soundTypeWood).setUnlocalizedName("torch"));
/* 1325 */     registerBlock(51, "fire", new BlockFire().setHardness(0.0F).setLightLevel(1.0F).setStepSound(soundTypeCloth).setUnlocalizedName("fire").disableStats());
/* 1326 */     registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
/* 1327 */     registerBlock(53, "oak_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setUnlocalizedName("stairsWood"));
/* 1328 */     registerBlock(54, "chest", new BlockChest(0).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chest"));
/* 1329 */     registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
/* 1330 */     registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreDiamond"));
/* 1331 */     registerBlock(57, "diamond_block", new Block(Material.iron, MapColor.diamondColor).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.tabBlock));
/* 1332 */     registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("workbench"));
/* 1333 */     registerBlock(59, "wheat", new BlockCrops().setUnlocalizedName("crops"));
/* 1334 */     Block block6 = new BlockFarmland().setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
/* 1335 */     registerBlock(60, "farmland", block6);
/* 1336 */     registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
/* 1337 */     registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5F).setStepSound(soundTypePiston).setLightLevel(0.875F).setUnlocalizedName("furnace"));
/* 1338 */     registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
/* 1339 */     registerBlock(64, "wooden_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorOak").disableStats());
/* 1340 */     registerBlock(65, "ladder", new BlockLadder().setHardness(0.4F).setStepSound(soundTypeLadder).setUnlocalizedName("ladder"));
/* 1341 */     registerBlock(66, "rail", new BlockRail().setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("rail"));
/* 1342 */     registerBlock(67, "stone_stairs", new BlockStairs(block.getDefaultState()).setUnlocalizedName("stairsStone"));
/* 1343 */     registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
/* 1344 */     registerBlock(69, "lever", new BlockLever().setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("lever"));
/* 1345 */     registerBlock(70, "stone_pressure_plate", new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS).setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("pressurePlateStone"));
/* 1346 */     registerBlock(71, "iron_door", new BlockDoor(Material.iron).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
/* 1347 */     registerBlock(72, "wooden_pressure_plate", new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("pressurePlateWood"));
/* 1348 */     registerBlock(73, "redstone_ore", new BlockRedstoneOre(false).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
/* 1349 */     registerBlock(74, "lit_redstone_ore", new BlockRedstoneOre(true).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone"));
/* 1350 */     registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("notGate"));
/* 1351 */     registerBlock(76, "redstone_torch", new BlockRedstoneTorch(true).setHardness(0.0F).setLightLevel(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
/* 1352 */     registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("button"));
/* 1353 */     registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1F).setStepSound(soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
/* 1354 */     registerBlock(79, "ice", new BlockIce().setHardness(0.5F).setLightOpacity(3).setStepSound(soundTypeGlass).setUnlocalizedName("ice"));
/* 1355 */     registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2F).setStepSound(soundTypeSnow).setUnlocalizedName("snow"));
/* 1356 */     registerBlock(81, "cactus", new BlockCactus().setHardness(0.4F).setStepSound(soundTypeCloth).setUnlocalizedName("cactus"));
/* 1357 */     registerBlock(82, "clay", new BlockClay().setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("clay"));
/* 1358 */     registerBlock(83, "reeds", new BlockReed().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("reeds").disableStats());
/* 1359 */     registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("jukebox"));
/* 1360 */     registerBlock(85, "fence", new BlockFence(Material.wood, BlockPlanks.EnumType.OAK.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fence"));
/* 1361 */     Block block7 = new BlockPumpkin().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkin");
/* 1362 */     registerBlock(86, "pumpkin", block7);
/* 1363 */     registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4F).setStepSound(soundTypePiston).setUnlocalizedName("hellrock"));
/* 1364 */     registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("hellsand"));
/* 1365 */     registerBlock(89, "glowstone", new BlockGlowstone(Material.glass).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("lightgem"));
/* 1366 */     registerBlock(90, "portal", new BlockPortal().setHardness(-1.0F).setStepSound(soundTypeGlass).setLightLevel(0.75F).setUnlocalizedName("portal"));
/* 1367 */     registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0F).setStepSound(soundTypeWood).setLightLevel(1.0F).setUnlocalizedName("litpumpkin"));
/* 1368 */     registerBlock(92, "cake", new BlockCake().setHardness(0.5F).setStepSound(soundTypeCloth).setUnlocalizedName("cake").disableStats());
/* 1369 */     registerBlock(93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
/* 1370 */     registerBlock(94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
/* 1371 */     registerBlock(95, "stained_glass", new BlockStainedGlass(Material.glass).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("stainedGlass"));
/* 1372 */     registerBlock(96, "trapdoor", new BlockTrapDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
/* 1373 */     registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75F).setUnlocalizedName("monsterStoneEgg"));
/* 1374 */     Block block8 = new BlockStoneBrick().setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebricksmooth");
/* 1375 */     registerBlock(98, "stonebrick", block8);
/* 1376 */     registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.dirtColor, block3).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
/* 1377 */     registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.redColor, block4).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
/* 1378 */     registerBlock(101, "iron_bars", new BlockPane(Material.iron, true).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("fenceIron"));
/* 1379 */     registerBlock(102, "glass_pane", new BlockPane(Material.glass, false).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinGlass"));
/* 1380 */     Block block9 = new BlockMelon().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("melon");
/* 1381 */     registerBlock(103, "melon_block", block9);
/* 1382 */     registerBlock(104, "pumpkin_stem", new BlockStem(block7).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
/* 1383 */     registerBlock(105, "melon_stem", new BlockStem(block9).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
/* 1384 */     registerBlock(106, "vine", new BlockVine().setHardness(0.2F).setStepSound(soundTypeGrass).setUnlocalizedName("vine"));
/* 1385 */     registerBlock(107, "fence_gate", new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fenceGate"));
/* 1386 */     registerBlock(108, "brick_stairs", new BlockStairs(block5.getDefaultState()).setUnlocalizedName("stairsBrick"));
/* 1387 */     registerBlock(109, "stone_brick_stairs", new BlockStairs(block8.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)).setUnlocalizedName("stairsStoneBrickSmooth"));
/* 1388 */     registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("mycel"));
/* 1389 */     registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("waterlily"));
/* 1390 */     Block block10 = new BlockNetherBrick().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
/* 1391 */     registerBlock(112, "nether_brick", block10);
/* 1392 */     registerBlock(113, "nether_brick_fence", new BlockFence(Material.rock, MapColor.netherrackColor).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherFence"));
/* 1393 */     registerBlock(114, "nether_brick_stairs", new BlockStairs(block10.getDefaultState()).setUnlocalizedName("stairsNetherBrick"));
/* 1394 */     registerBlock(115, "nether_wart", new BlockNetherWart().setUnlocalizedName("netherStalk"));
/* 1395 */     registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable"));
/* 1396 */     registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5F).setLightLevel(0.125F).setUnlocalizedName("brewingStand"));
/* 1397 */     registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0F).setUnlocalizedName("cauldron"));
/* 1398 */     registerBlock(119, "end_portal", new BlockEndPortal(Material.portal).setHardness(-1.0F).setResistance(6000000.0F));
/* 1399 */     registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setStepSound(soundTypeGlass).setLightLevel(0.125F).setHardness(-1.0F).setUnlocalizedName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations));
/* 1400 */     registerBlock(121, "end_stone", new Block(Material.rock, MapColor.sandColor).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
/* 1401 */     registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setLightLevel(0.125F).setUnlocalizedName("dragonEgg"));
/* 1402 */     registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
/* 1403 */     registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight"));
/* 1404 */     registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
/* 1405 */     registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
/* 1406 */     registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("cocoa"));
/* 1407 */     registerBlock(128, "sandstone_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setUnlocalizedName("stairsSandStone"));
/* 1408 */     registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreEmerald"));
/* 1409 */     registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5F).setResistance(1000.0F).setStepSound(soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5F));
/* 1410 */     registerBlock(131, "tripwire_hook", new BlockTripWireHook().setUnlocalizedName("tripWireSource"));
/* 1411 */     registerBlock(132, "tripwire", new BlockTripWire().setUnlocalizedName("tripWire"));
/* 1412 */     registerBlock(133, "emerald_block", new Block(Material.iron, MapColor.emeraldColor).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.tabBlock));
/* 1413 */     registerBlock(134, "spruce_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setUnlocalizedName("stairsWoodSpruce"));
/* 1414 */     registerBlock(135, "birch_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setUnlocalizedName("stairsWoodBirch"));
/* 1415 */     registerBlock(136, "jungle_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setUnlocalizedName("stairsWoodJungle"));
/* 1416 */     registerBlock(137, "command_block", new BlockCommandBlock().setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("commandBlock"));
/* 1417 */     registerBlock(138, "beacon", new BlockBeacon().setUnlocalizedName("beacon").setLightLevel(1.0F));
/* 1418 */     registerBlock(139, "cobblestone_wall", new BlockWall(block).setUnlocalizedName("cobbleWall"));
/* 1419 */     registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("flowerPot"));
/* 1420 */     registerBlock(141, "carrots", new BlockCarrot().setUnlocalizedName("carrots"));
/* 1421 */     registerBlock(142, "potatoes", new BlockPotato().setUnlocalizedName("potatoes"));
/* 1422 */     registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("button"));
/* 1423 */     registerBlock(144, "skull", new BlockSkull().setHardness(1.0F).setStepSound(soundTypePiston).setUnlocalizedName("skull"));
/* 1424 */     registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0F).setStepSound(soundTypeAnvil).setResistance(2000.0F).setUnlocalizedName("anvil"));
/* 1425 */     registerBlock(146, "trapped_chest", new BlockChest(1).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chestTrap"));
/* 1426 */     registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 15, MapColor.goldColor).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_light"));
/* 1427 */     registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 150).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
/* 1428 */     registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
/* 1429 */     registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0F).setLightLevel(0.625F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
/* 1430 */     registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
/* 1431 */     registerBlock(152, "redstone_block", new BlockCompressedPowered(Material.iron, MapColor.tntColor).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.tabRedstone));
/* 1432 */     registerBlock(153, "quartz_ore", new BlockOre(MapColor.netherrackColor).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherquartz"));
/* 1433 */     registerBlock(154, "hopper", new BlockHopper().setHardness(3.0F).setResistance(8.0F).setStepSound(soundTypeMetal).setUnlocalizedName("hopper"));
/* 1434 */     Block block11 = new BlockQuartz().setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("quartzBlock");
/* 1435 */     registerBlock(155, "quartz_block", block11);
/* 1436 */     registerBlock(156, "quartz_stairs", new BlockStairs(block11.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setUnlocalizedName("stairsQuartz"));
/* 1437 */     registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("activatorRail"));
/* 1438 */     registerBlock(158, "dropper", new BlockDropper().setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dropper"));
/* 1439 */     registerBlock(159, "stained_hardened_clay", new BlockColored(Material.rock).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardenedStained"));
/* 1440 */     registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
/* 1441 */     registerBlock(161, "leaves2", new BlockNewLeaf().setUnlocalizedName("leaves"));
/* 1442 */     registerBlock(162, "log2", new BlockNewLog().setUnlocalizedName("log"));
/* 1443 */     registerBlock(163, "acacia_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setUnlocalizedName("stairsWoodAcacia"));
/* 1444 */     registerBlock(164, "dark_oak_stairs", new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setUnlocalizedName("stairsWoodDarkOak"));
/* 1445 */     registerBlock(165, "slime", new BlockSlime().setUnlocalizedName("slime").setStepSound(SLIME_SOUND));
/* 1446 */     registerBlock(166, "barrier", new BlockBarrier().setUnlocalizedName("barrier"));
/* 1447 */     registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.iron).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
/* 1448 */     registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("prismarine"));
/* 1449 */     registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.glass).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("seaLantern"));
/* 1450 */     registerBlock(170, "hay_block", new BlockHay().setHardness(0.5F).setStepSound(soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
/* 1451 */     registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1F).setStepSound(soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
/* 1452 */     registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardened"));
/* 1453 */     registerBlock(173, "coal_block", new Block(Material.rock, MapColor.blackColor).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
/* 1454 */     registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5F).setStepSound(soundTypeGlass).setUnlocalizedName("icePacked"));
/* 1455 */     registerBlock(175, "double_plant", new BlockDoublePlant());
/* 1456 */     registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
/* 1457 */     registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
/* 1458 */     registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
/* 1459 */     Block block12 = new BlockRedSandstone().setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("redSandStone");
/* 1460 */     registerBlock(179, "red_sandstone", block12);
/* 1461 */     registerBlock(180, "red_sandstone_stairs", new BlockStairs(block12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)).setUnlocalizedName("stairsRedSandStone"));
/* 1462 */     registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
/* 1463 */     registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
/* 1464 */     registerBlock(183, "spruce_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFenceGate"));
/* 1465 */     registerBlock(184, "birch_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFenceGate"));
/* 1466 */     registerBlock(185, "jungle_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.JUNGLE).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFenceGate"));
/* 1467 */     registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
/* 1468 */     registerBlock(187, "acacia_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.ACACIA).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
/* 1469 */     registerBlock(188, "spruce_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.SPRUCE.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFence"));
/* 1470 */     registerBlock(189, "birch_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.BIRCH.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFence"));
/* 1471 */     registerBlock(190, "jungle_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.JUNGLE.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFence"));
/* 1472 */     registerBlock(191, "dark_oak_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.DARK_OAK.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFence"));
/* 1473 */     registerBlock(192, "acacia_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.ACACIA.func_181070_c()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFence"));
/* 1474 */     registerBlock(193, "spruce_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
/* 1475 */     registerBlock(194, "birch_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
/* 1476 */     registerBlock(195, "jungle_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
/* 1477 */     registerBlock(196, "acacia_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
/* 1478 */     registerBlock(197, "dark_oak_door", new BlockDoor(Material.wood).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
/* 1479 */     blockRegistry.validateKey();
/*      */     boolean flag1;
/* 1481 */     for (Block block13 : blockRegistry)
/*      */     {
/* 1483 */       if (block13.blockMaterial == Material.air)
/*      */       {
/* 1485 */         block13.useNeighborBrightness = false;
/*      */       }
/*      */       else
/*      */       {
/* 1489 */         boolean flag = false;
/* 1490 */         flag1 = block13 instanceof BlockStairs;
/* 1491 */         boolean flag2 = block13 instanceof BlockSlab;
/* 1492 */         boolean flag3 = block13 == block6;
/* 1493 */         boolean flag4 = block13.translucent;
/* 1494 */         boolean flag5 = block13.lightOpacity == 0;
/*      */         
/* 1496 */         if ((flag1) || (flag2) || (flag3) || (flag4) || (flag5))
/*      */         {
/* 1498 */           flag = true;
/*      */         }
/*      */         
/* 1501 */         block13.useNeighborBrightness = flag;
/*      */       }
/*      */     }
/*      */     
/* 1505 */     for (??? = blockRegistry.iterator(); ???.hasNext(); 
/*      */         
/* 1507 */         flag1.hasNext())
/*      */     {
/* 1505 */       Block block14 = (Block)???.next();
/*      */       
/* 1507 */       flag1 = block14.getBlockState().getValidStates().iterator(); continue;IBlockState iblockstate = (IBlockState)flag1.next();
/*      */       
/* 1509 */       int i = blockRegistry.getIDForObject(block14) << 4 | block14.getMetaFromState(iblockstate);
/* 1510 */       BLOCK_STATE_IDS.put(iblockstate, i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void registerBlock(int id, ResourceLocation textualID, Block block_)
/*      */   {
/* 1517 */     blockRegistry.register(id, textualID, block_);
/*      */   }
/*      */   
/*      */   private static void registerBlock(int id, String textualID, Block block_)
/*      */   {
/* 1522 */     registerBlock(id, new ResourceLocation(textualID), block_);
/*      */   }
/*      */   
/*      */   public static enum EnumOffsetType
/*      */   {
/* 1527 */     NONE, 
/* 1528 */     XZ, 
/* 1529 */     XYZ;
/*      */   }
/*      */   
/*      */   public static class SoundType
/*      */   {
/*      */     public final String soundName;
/*      */     public final float volume;
/*      */     public final float frequency;
/*      */     
/*      */     public SoundType(String name, float volume, float frequency)
/*      */     {
/* 1540 */       this.soundName = name;
/* 1541 */       this.volume = volume;
/* 1542 */       this.frequency = frequency;
/*      */     }
/*      */     
/*      */     public float getVolume()
/*      */     {
/* 1547 */       return this.volume;
/*      */     }
/*      */     
/*      */     public float getFrequency()
/*      */     {
/* 1552 */       return this.frequency;
/*      */     }
/*      */     
/*      */     public String getBreakSound()
/*      */     {
/* 1557 */       return "dig." + this.soundName;
/*      */     }
/*      */     
/*      */     public String getStepSound()
/*      */     {
/* 1562 */       return "step." + this.soundName;
/*      */     }
/*      */     
/*      */     public String getPlaceSound()
/*      */     {
/* 1567 */       return getBreakSound();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */