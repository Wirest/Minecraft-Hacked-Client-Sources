package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block
{
    /** ResourceLocation for the Air block */
    private static final ResourceLocation AIR_ID = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey blockRegistry = new RegistryNamespacedDefaultedByKey(AIR_ID);
    public static final ObjectIntIdentityMap BLOCK_STATE_IDS = new ObjectIntIdentityMap();
    private CreativeTabs displayOnCreativeTab;
    public static final Block.SoundType soundTypeStone = new Block.SoundType("stone", 1.0F, 1.0F);

    /** the wood sound type */
    public static final Block.SoundType soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);

    /** the gravel sound type */
    public static final Block.SoundType soundTypeGravel = new Block.SoundType("gravel", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeGrass = new Block.SoundType("grass", 1.0F, 1.0F);
    public static final Block.SoundType soundTypePiston = new Block.SoundType("stone", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeMetal = new Block.SoundType("stone", 1.0F, 1.5F);
    public static final Block.SoundType soundTypeGlass = new Block.SoundType("stone", 1.0F, 1.0F)
    {
        
        public String getBreakSound()
        {
            return "dig.glass";
        }
        public String getPlaceSound()
        {
            return "step.stone";
        }
    };
    public static final Block.SoundType soundTypeCloth = new Block.SoundType("cloth", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeSand = new Block.SoundType("sand", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeSnow = new Block.SoundType("snow", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeLadder = new Block.SoundType("ladder", 1.0F, 1.0F)
    {
        
        public String getBreakSound()
        {
            return "dig.wood";
        }
    };
    public static final Block.SoundType soundTypeAnvil = new Block.SoundType("anvil", 0.3F, 1.0F)
    {
        
        public String getBreakSound()
        {
            return "dig.stone";
        }
        public String getPlaceSound()
        {
            return "random.anvil_land";
        }
    };
    public static final Block.SoundType SLIME_SOUND = new Block.SoundType("slime", 1.0F, 1.0F)
    {
        
        public String getBreakSound()
        {
            return "mob.slime.big";
        }
        public String getPlaceSound()
        {
            return "mob.slime.big";
        }
        public String getStepSound()
        {
            return "mob.slime.small";
        }
    };
    protected boolean fullBlock;

    /** How much light is subtracted for going through this block */
    protected int lightOpacity;
    protected boolean translucent;

    /** Amount of light emitted */
    protected int lightValue;

    /**
     * Flag if block should use the brightest neighbor light value as its own
     */
    protected boolean useNeighborBrightness;

    /** Indicates how many hits it takes to break a block. */
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats = true;

    /**
     * Flags whether or not this block is of a type that needs random ticking. Ref-counted by ExtendedBlockStorage in
     * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    protected boolean needsRandomTick;

    /** true if the Block contains a Tile Entity */
    protected boolean isBlockContainer;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;

    /** Sound of stepping on the block */
    public Block.SoundType stepSound;
    public float blockParticleGravity;
    protected final Material blockMaterial;

    /**
     * Determines how much velocity is maintained while moving on top of this block
     */
    public float slipperiness;
    protected final BlockState blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;
    

    public static int getIdFromBlock(Block blockIn)
    {
        return blockRegistry.getIDForObject(blockIn);
    }

    /**
     * Get a unique ID for the given BlockState, containing both BlockID and metadata
     */
    public static int getStateId(IBlockState state)
    {
        return getIdFromBlock(state.getBlock()) + (state.getBlock().getMetaFromState(state) << 12);
    }

    public static Block getBlockById(int id)
    {
        return (Block)blockRegistry.getObjectById(id);
    }

    /**
     * Get a BlockState by it's ID (see getStateId)
     */
    public static IBlockState getStateById(int id)
    {
        int var1 = id & 4095;
        int var2 = id >> 12 & 15;
        return getBlockById(var1).getStateFromMeta(var2);
    }

    public static Block getBlockFromItem(Item itemIn)
    {
        return itemIn instanceof ItemBlock ? ((ItemBlock)itemIn).getBlock() : null;
    }

    public static Block getBlockFromName(String name)
    {
        ResourceLocation var1 = new ResourceLocation(name);

        if (blockRegistry.containsKey(var1))
        {
            return (Block)blockRegistry.getObject(var1);
        }
        else
        {
            try
            {
                return (Block)blockRegistry.getObjectById(Integer.parseInt(name));
            }
            catch (NumberFormatException var3)
            {
                return null;
            }
        }
    }

    public boolean isFullBlock()
    {
        return this.fullBlock;
    }

    public int getLightOpacity()
    {
        return this.lightOpacity;
    }

    public boolean isTranslucent()
    {
        return this.translucent;
    }

    public int getLightValue()
    {
        return this.lightValue;
    }

    /**
     * Should block use the brightest neighbor light value as its own
     */
    public boolean getUseNeighborBrightness()
    {
        return this.useNeighborBrightness;
    }

    /**
     * Get a material of block
     */
    public Material getMaterial()
    {
        return this.blockMaterial;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state)
    {
        return this.getMaterial().getMaterialMapColor();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        if (state != null && !state.getPropertyNames().isEmpty())
        {
            throw new IllegalArgumentException("Don\'t know how to convert " + state + " back into data...");
        }
        else
        {
            return 0;
        }
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;
    }

    protected Block(Material materialIn)
    {
        this.stepSound = soundTypeStone;
        this.blockParticleGravity = 1.0F;
        this.slipperiness = 0.6F;
        this.blockMaterial = materialIn;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.fullBlock = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
        this.translucent = !materialIn.blocksLight();
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
    }

    /**
     * Sets the footstep sound for the block. Returns the object for convenience in constructing.
     */
    protected Block setStepSound(Block.SoundType sound)
    {
        this.stepSound = sound;
        return this;
    }

    /**
     * Sets how much light is blocked going through this block. Returns the object for convenience in constructing.
     */
    protected Block setLightOpacity(int opacity)
    {
        this.lightOpacity = opacity;
        return this;
    }

    /**
     * Sets the light value that the block emits. Returns resulting block instance for constructing convenience. Args:
     * level
     */
    protected Block setLightLevel(float value)
    {
        this.lightValue = (int)(15.0F * value);
        return this;
    }

    /**
     * Sets the the blocks resistance to explosions. Returns the object for convenience in constructing.
     */
    protected Block setResistance(float resistance)
    {
        this.blockResistance = resistance * 3.0F;
        return this;
    }

    /**
     * Indicate if a material is a normal solid opaque cube
     */
    public boolean isSolidFullCube()
    {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean isNormalCube()
    {
        return this.blockMaterial.isOpaque() && this.isFullCube() && !this.canProvidePower();
    }

    public boolean isVisuallyOpaque()
    {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }

    public boolean isFullCube()
    {
        return true;
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return !this.blockMaterial.blocksMovement();
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    protected Block setHardness(float hardness)
    {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F)
        {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    protected Block setBlockUnbreakable()
    {
        this.setHardness(-1.0F);
        return this;
    }

    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        return this.blockHardness;
    }

    /**
     * Sets whether this block type will receive random update ticks
     */
    protected Block setTickRandomly(boolean shouldTick)
    {
        this.needsRandomTick = shouldTick;
        return this;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    public boolean getTickRandomly()
    {
        return this.needsRandomTick;
    }

    public boolean hasTileEntity()
    {
        return this.isBlockContainer;
    }

    protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
    {
        this.minX = (double)minX;
        this.minY = (double)minY;
        this.minZ = (double)minZ;
        this.maxX = (double)maxX;
        this.maxY = (double)maxY;
        this.maxZ = (double)maxZ;
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
    {
        Block var3 = worldIn.getBlockState(pos).getBlock();
        int var4 = worldIn.getCombinedLight(pos, var3.getLightValue());

        if (var4 == 0 && var3 instanceof BlockSlab)
        {
            pos = pos.offsetDown();
            var3 = worldIn.getBlockState(pos).getBlock();
            return worldIn.getCombinedLight(pos, var3.getLightValue());
        }
        else
        {
            return var4;
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN && this.minY > 0.0D ? true : (side == EnumFacing.UP && this.maxY < 1.0D ? true : (side == EnumFacing.NORTH && this.minZ > 0.0D ? true : (side == EnumFacing.SOUTH && this.maxZ < 1.0D ? true : (side == EnumFacing.WEST && this.minX > 0.0D ? true : (side == EnumFacing.EAST && this.maxX < 1.0D ? true : !worldIn.getBlockState(pos).getBlock().isOpaqueCube())))));
    }

    /**
     * Whether this Block is solid on the given Side
     */
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *  
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        AxisAlignedBB var7 = this.getCollisionBoundingBox(worldIn, pos, state);

        if (var7 != null && mask.intersectsWith(var7))
        {
            list.add(var7);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

    public boolean isOpaqueCube()
    {
        return true;
    }

    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_)
    {
        return this.isCollidable();
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return true;
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
        this.updateTick(worldIn, pos, state, random);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}

    /**
     * Called when a player destroys this Block
     */
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {}

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 10;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {}

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {}

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos)
    {
        float var4 = this.getBlockHardness(worldIn, pos);
        return var4 < 0.0F ? 0.0F : (!playerIn.canHarvestBlock(this) ? playerIn.func_180471_a(this) / var4 / 100.0F : playerIn.func_180471_a(this) / var4 / 30.0F);
    }

    /**
     * Spawn this Block's drops into the World as EntityItems
     *  
     * @param forture the level of the Fortune enchantment on the player's tool
     */
    public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture)
    {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0F, forture);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            int var6 = this.quantityDroppedWithBonus(fortune, worldIn.rand);

            for (int var7 = 0; var7 < var6; ++var7)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                    Item var8 = this.getItemDropped(state, worldIn.rand, fortune);

                    if (var8 != null)
                    {
                        spawnAsEntity(worldIn, pos, new ItemStack(var8, 1, this.damageDropped(state)));
                    }
                }
            }
        }
    }

    /**
     * Spawns the given ItemStack as an EntityItem into the World at the given position
     */
    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack)
    {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float var3 = 0.5F;
            double var4 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
            double var6 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
            double var8 = (double)(worldIn.rand.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
            EntityItem var10 = new EntityItem(worldIn, (double)pos.getX() + var4, (double)pos.getY() + var6, (double)pos.getZ() + var8, stack);
            var10.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(var10);
        }
    }

    /**
     * Spawns the given amount of experience into the World as XP orb entities
     *  
     * @param amount The amount of XP to spawn
     */
    protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount)
    {
        if (!worldIn.isRemote)
        {
            while (amount > 0)
            {
                int var4 = EntityXPOrb.getXPSplit(amount);
                amount -= var4;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, var4));
            }
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity exploder)
    {
        return this.blockResistance / 5.0F;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        start = start.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        end = end.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        Vec3 var5 = start.getIntermediateWithXValue(end, this.minX);
        Vec3 var6 = start.getIntermediateWithXValue(end, this.maxX);
        Vec3 var7 = start.getIntermediateWithYValue(end, this.minY);
        Vec3 var8 = start.getIntermediateWithYValue(end, this.maxY);
        Vec3 var9 = start.getIntermediateWithZValue(end, this.minZ);
        Vec3 var10 = start.getIntermediateWithZValue(end, this.maxZ);

        if (!this.isVecInsideYZBounds(var5))
        {
            var5 = null;
        }

        if (!this.isVecInsideYZBounds(var6))
        {
            var6 = null;
        }

        if (!this.isVecInsideXZBounds(var7))
        {
            var7 = null;
        }

        if (!this.isVecInsideXZBounds(var8))
        {
            var8 = null;
        }

        if (!this.isVecInsideXYBounds(var9))
        {
            var9 = null;
        }

        if (!this.isVecInsideXYBounds(var10))
        {
            var10 = null;
        }

        Vec3 var11 = null;

        if (var5 != null && (var11 == null || start.squareDistanceTo(var5) < start.squareDistanceTo(var11)))
        {
            var11 = var5;
        }

        if (var6 != null && (var11 == null || start.squareDistanceTo(var6) < start.squareDistanceTo(var11)))
        {
            var11 = var6;
        }

        if (var7 != null && (var11 == null || start.squareDistanceTo(var7) < start.squareDistanceTo(var11)))
        {
            var11 = var7;
        }

        if (var8 != null && (var11 == null || start.squareDistanceTo(var8) < start.squareDistanceTo(var11)))
        {
            var11 = var8;
        }

        if (var9 != null && (var11 == null || start.squareDistanceTo(var9) < start.squareDistanceTo(var11)))
        {
            var11 = var9;
        }

        if (var10 != null && (var11 == null || start.squareDistanceTo(var10) < start.squareDistanceTo(var11)))
        {
            var11 = var10;
        }

        if (var11 == null)
        {
            return null;
        }
        else
        {
            EnumFacing var12 = null;

            if (var11 == var5)
            {
                var12 = EnumFacing.WEST;
            }

            if (var11 == var6)
            {
                var12 = EnumFacing.EAST;
            }

            if (var11 == var7)
            {
                var12 = EnumFacing.DOWN;
            }

            if (var11 == var8)
            {
                var12 = EnumFacing.UP;
            }

            if (var11 == var9)
            {
                var12 = EnumFacing.NORTH;
            }

            if (var11 == var10)
            {
                var12 = EnumFacing.SOUTH;
            }

            return new MovingObjectPosition(var11.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), var12, pos);
        }
    }

    /**
     * Checks if a vector is within the Y and Z bounds of the block.
     */
    private boolean isVecInsideYZBounds(Vec3 point)
    {
        return point == null ? false : point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    /**
     * Checks if a vector is within the X and Z bounds of the block.
     */
    private boolean isVecInsideXZBounds(Vec3 point)
    {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ;
    }

    /**
     * Checks if a vector is within the X and Y bounds of the block.
     */
    private boolean isVecInsideXYBounds(Vec3 point)
    {
        return point == null ? false : point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY;
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {}

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.SOLID;
    }

    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
    {
        return this.canPlaceBlockOnSide(worldIn, pos, side);
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {}

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getStateFromMeta(meta);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {}

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
    {
        return motion;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {}

    /**
     * returns the block bounderies minX value
     */
    public final double getBlockBoundsMinX()
    {
        return this.minX;
    }

    /**
     * returns the block bounderies maxX value
     */
    public final double getBlockBoundsMaxX()
    {
        return this.maxX;
    }

    /**
     * returns the block bounderies minY value
     */
    public final double getBlockBoundsMinY()
    {
        return this.minY;
    }

    /**
     * returns the block bounderies maxY value
     */
    public final double getBlockBoundsMaxY()
    {
        return this.maxY;
    }

    /**
     * returns the block bounderies minZ value
     */
    public final double getBlockBoundsMinZ()
    {
        return this.minZ;
    }

    /**
     * returns the block bounderies maxZ value
     */
    public final double getBlockBoundsMaxZ()
    {
        return this.maxZ;
    }

    public int getBlockColor()
    {
        return 16777215;
    }

    public int getRenderColor(IBlockState state)
    {
        return 16777215;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return 16777215;
    }

    public final int colorMultiplier(IBlockAccess worldIn, BlockPos pos)
    {
        return this.colorMultiplier(worldIn, pos, 0);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return false;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {}

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return 0;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {}

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te)
    {
        playerIn.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
        playerIn.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(playerIn))
        {
            ItemStack var7 = this.createStackedBlock(state);

            if (var7 != null)
            {
                spawnAsEntity(worldIn, pos, var7);
            }
        }
        else
        {
            int var6 = EnchantmentHelper.getFortuneModifier(playerIn);
            this.dropBlockAsItem(worldIn, pos, state, var6);
        }
    }

    protected boolean canSilkHarvest()
    {
        return this.isFullCube() && !this.isBlockContainer;
    }

    protected ItemStack createStackedBlock(IBlockState state)
    {
        int var2 = 0;
        Item var3 = Item.getItemFromBlock(this);

        if (var3 != null && var3.getHasSubtypes())
        {
            var2 = this.getMetaFromState(state);
        }

        return new ItemStack(var3, 1, var2);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return this.quantityDropped(random);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}

    public Block setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
        return this;
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    public String getUnlocalizedName()
    {
        return "tile." + this.unlocalizedName;
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        return false;
    }

    /**
     * Return the state of blocks statistics flags - if the block is counted for mined and placed.
     */
    public boolean getEnableStats()
    {
        return this.enableStats;
    }

    protected Block disableStats()
    {
        this.enableStats = false;
        return this;
    }

    public int getMobilityFlag()
    {
        return this.blockMaterial.getMaterialMobility();
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    public float getAmbientOcclusionLightValue()
    {
        return this.isSolidFullCube() ? 0.2F : 1.0F;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     *  
     * @param fallDistance The distance the entity has fallen before landing
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        entityIn.fall(fallDistance, 1.0F);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        entityIn.motionY = 0.0D;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(this);
    }

    public int getDamageValue(World worldIn, BlockPos pos)
    {
        return this.damageDropped(worldIn.getBlockState(pos));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
    }

    /**
     * Returns the CreativeTab to display the given block on.
     */
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return this.displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs tab)
    {
        this.displayOnCreativeTab = tab;
        return this;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {}

    /**
     * Called similar to random ticks, but only when it is raining.
     */
    public void fillWithRain(World worldIn, BlockPos pos) {}

    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot()
    {
        return false;
    }

    public boolean requiresUpdates()
    {
        return true;
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return true;
    }

    public boolean isAssociatedBlock(Block other)
    {
        return this == other;
    }

    public static boolean isEqualTo(Block blockIn, Block other)
    {
        return blockIn != null && other != null ? (blockIn == other ? true : blockIn.isAssociatedBlock(other)) : false;
    }

    public boolean hasComparatorInputOverride()
    {
        return false;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        return 0;
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return state;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[0]);
    }

    public BlockState getBlockState()
    {
        return this.blockState;
    }

    protected final void setDefaultState(IBlockState state)
    {
        this.defaultBlockState = state;
    }

    public final IBlockState getDefaultState()
    {
        return this.defaultBlockState;
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.NONE;
    }

    public static void registerBlocks()
    {
        registerBlock(0, AIR_ID, (new BlockAir()).setUnlocalizedName("air"));
        registerBlock(1, "stone", (new BlockStone()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stone"));
        registerBlock(2, "grass", (new BlockGrass()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("grass"));
        registerBlock(3, "dirt", (new BlockDirt()).setHardness(0.5F).setStepSound(soundTypeGravel).setUnlocalizedName("dirt"));
        Block var0 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(4, "cobblestone", var0);
        Block var1 = (new BlockPlanks()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("wood");
        registerBlock(5, "planks", var1);
        registerBlock(6, "sapling", (new BlockSapling()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("sapling"));
        registerBlock(7, "bedrock", (new Block(Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(8, "flowing_water", (new BlockDynamicLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(9, "water", (new BlockStaticLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(10, "flowing_lava", (new BlockDynamicLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        registerBlock(11, "lava", (new BlockStaticLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        registerBlock(12, "sand", (new BlockSand()).setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("sand"));
        registerBlock(13, "gravel", (new BlockGravel()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("gravel"));
        registerBlock(14, "gold_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreGold"));
        registerBlock(15, "iron_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreIron"));
        registerBlock(16, "coal_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreCoal"));
        registerBlock(17, "log", (new BlockOldLog()).setUnlocalizedName("log"));
        registerBlock(18, "leaves", (new BlockOldLeaf()).setUnlocalizedName("leaves"));
        registerBlock(19, "sponge", (new BlockSponge()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("sponge"));
        registerBlock(20, "glass", (new BlockGlass(Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("glass"));
        registerBlock(21, "lapis_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreLapis"));
        registerBlock(22, "lapis_block", (new BlockCompressed(MapColor.lapisColor)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(23, "dispenser", (new BlockDispenser()).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dispenser"));
        Block var2 = (new BlockSandStone()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("sandStone");
        registerBlock(24, "sandstone", var2);
        registerBlock(25, "noteblock", (new BlockNote()).setHardness(0.8F).setUnlocalizedName("musicBlock"));
        registerBlock(26, "bed", (new BlockBed()).setStepSound(soundTypeWood).setHardness(0.2F).setUnlocalizedName("bed").disableStats());
        registerBlock(27, "golden_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("goldenRail"));
        registerBlock(28, "detector_rail", (new BlockRailDetector()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("detectorRail"));
        registerBlock(29, "sticky_piston", (new BlockPistonBase(true)).setUnlocalizedName("pistonStickyBase"));
        registerBlock(30, "web", (new BlockWeb()).setLightOpacity(1).setHardness(4.0F).setUnlocalizedName("web"));
        registerBlock(31, "tallgrass", (new BlockTallGrass()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tallgrass"));
        registerBlock(32, "deadbush", (new BlockDeadBush()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("deadbush"));
        registerBlock(33, "piston", (new BlockPistonBase(false)).setUnlocalizedName("pistonBase"));
        registerBlock(34, "piston_head", new BlockPistonExtension());
        registerBlock(35, "wool", (new BlockColored(Material.cloth)).setHardness(0.8F).setStepSound(soundTypeCloth).setUnlocalizedName("cloth"));
        registerBlock(36, "piston_extension", new BlockPistonMoving());
        registerBlock(37, "yellow_flower", (new BlockYellowFlower()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower1"));
        registerBlock(38, "red_flower", (new BlockRedFlower()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("flower2"));
        Block var3 = (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setLightLevel(0.125F).setUnlocalizedName("mushroom");
        registerBlock(39, "brown_mushroom", var3);
        Block var4 = (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("mushroom");
        registerBlock(40, "red_mushroom", var4);
        registerBlock(41, "gold_block", (new BlockCompressed(MapColor.goldColor)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockGold"));
        registerBlock(42, "iron_block", (new BlockCompressed(MapColor.ironColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockIron"));
        registerBlock(43, "double_stone_slab", (new BlockDoubleStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        registerBlock(44, "stone_slab", (new BlockHalfStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block var5 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(45, "brick_block", var5);
        registerBlock(46, "tnt", (new BlockTNT()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("tnt"));
        registerBlock(47, "bookshelf", (new BlockBookshelf()).setHardness(1.5F).setStepSound(soundTypeWood).setUnlocalizedName("bookshelf"));
        registerBlock(48, "mossy_cobblestone", (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(49, "obsidian", (new BlockObsidian()).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundTypePiston).setUnlocalizedName("obsidian"));
        registerBlock(50, "torch", (new BlockTorch()).setHardness(0.0F).setLightLevel(0.9375F).setStepSound(soundTypeWood).setUnlocalizedName("torch"));
        registerBlock(51, "fire", (new BlockFire()).setHardness(0.0F).setLightLevel(1.0F).setStepSound(soundTypeCloth).setUnlocalizedName("fire").disableStats());
        registerBlock(52, "mob_spawner", (new BlockMobSpawner()).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
        registerBlock(53, "oak_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));
        registerBlock(54, "chest", (new BlockChest(0)).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chest"));
        registerBlock(55, "redstone_wire", (new BlockRedstoneWire()).setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
        registerBlock(56, "diamond_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreDiamond"));
        registerBlock(57, "diamond_block", (new BlockCompressed(MapColor.diamondColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockDiamond"));
        registerBlock(58, "crafting_table", (new BlockWorkbench()).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("workbench"));
        registerBlock(59, "wheat", (new BlockCrops()).setUnlocalizedName("crops"));
        Block var6 = (new BlockFarmland()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
        registerBlock(60, "farmland", var6);
        registerBlock(61, "furnace", (new BlockFurnace(false)).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        registerBlock(62, "lit_furnace", (new BlockFurnace(true)).setHardness(3.5F).setStepSound(soundTypePiston).setLightLevel(0.875F).setUnlocalizedName("furnace"));
        registerBlock(63, "standing_sign", (new BlockStandingSign()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
        registerBlock(64, "wooden_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorOak").disableStats());
        registerBlock(65, "ladder", (new BlockLadder()).setHardness(0.4F).setStepSound(soundTypeLadder).setUnlocalizedName("ladder"));
        registerBlock(66, "rail", (new BlockRail()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("rail"));
        registerBlock(67, "stone_stairs", (new BlockStairs(var0.getDefaultState())).setUnlocalizedName("stairsStone"));
        registerBlock(68, "wall_sign", (new BlockWallSign()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("sign").disableStats());
        registerBlock(69, "lever", (new BlockLever()).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("lever"));
        registerBlock(70, "stone_pressure_plate", (new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS)).setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("pressurePlateStone"));
        registerBlock(71, "iron_door", (new BlockDoor(Material.iron)).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
        registerBlock(72, "wooden_pressure_plate", (new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("pressurePlateWood"));
        registerBlock(73, "redstone_ore", (new BlockRedstoneOre(false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreRedstone"));
        registerBlock(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("notGate"));
        registerBlock(76, "redstone_torch", (new BlockRedstoneTorch(true)).setHardness(0.0F).setLightLevel(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
        registerBlock(77, "stone_button", (new BlockButtonStone()).setHardness(0.5F).setStepSound(soundTypePiston).setUnlocalizedName("button"));
        registerBlock(78, "snow_layer", (new BlockSnow()).setHardness(0.1F).setStepSound(soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
        registerBlock(79, "ice", (new BlockIce()).setHardness(0.5F).setLightOpacity(3).setStepSound(soundTypeGlass).setUnlocalizedName("ice"));
        registerBlock(80, "snow", (new BlockSnowBlock()).setHardness(0.2F).setStepSound(soundTypeSnow).setUnlocalizedName("snow"));
        registerBlock(81, "cactus", (new BlockCactus()).setHardness(0.4F).setStepSound(soundTypeCloth).setUnlocalizedName("cactus"));
        registerBlock(82, "clay", (new BlockClay()).setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("clay"));
        registerBlock(83, "reeds", (new BlockReed()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("reeds").disableStats());
        registerBlock(84, "jukebox", (new BlockJukebox()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("jukebox"));
        registerBlock(85, "fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fence"));
        Block var7 = (new BlockPumpkin()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkin");
        registerBlock(86, "pumpkin", var7);
        registerBlock(87, "netherrack", (new BlockNetherrack()).setHardness(0.4F).setStepSound(soundTypePiston).setUnlocalizedName("hellrock"));
        registerBlock(88, "soul_sand", (new BlockSoulSand()).setHardness(0.5F).setStepSound(soundTypeSand).setUnlocalizedName("hellsand"));
        registerBlock(89, "glowstone", (new BlockGlowstone(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("lightgem"));
        registerBlock(90, "portal", (new BlockPortal()).setHardness(-1.0F).setStepSound(soundTypeGlass).setLightLevel(0.75F).setUnlocalizedName("portal"));
        registerBlock(91, "lit_pumpkin", (new BlockPumpkin()).setHardness(1.0F).setStepSound(soundTypeWood).setLightLevel(1.0F).setUnlocalizedName("litpumpkin"));
        registerBlock(92, "cake", (new BlockCake()).setHardness(0.5F).setStepSound(soundTypeCloth).setUnlocalizedName("cake").disableStats());
        registerBlock(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
        registerBlock(94, "powered_repeater", (new BlockRedstoneRepeater(true)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("diode").disableStats());
        registerBlock(95, "stained_glass", (new BlockStainedGlass(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("stainedGlass"));
        registerBlock(96, "trapdoor", (new BlockTrapDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
        registerBlock(97, "monster_egg", (new BlockSilverfish()).setHardness(0.75F).setUnlocalizedName("monsterStoneEgg"));
        Block var8 = (new BlockStoneBrick()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stonebricksmooth");
        registerBlock(98, "stonebrick", var8);
        registerBlock(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.wood, var3)).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        registerBlock(100, "red_mushroom_block", (new BlockHugeMushroom(Material.wood, var4)).setHardness(0.2F).setStepSound(soundTypeWood).setUnlocalizedName("mushroom"));
        registerBlock(101, "iron_bars", (new BlockPane(Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("fenceIron"));
        registerBlock(102, "glass_pane", (new BlockPane(Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinGlass"));
        Block var9 = (new BlockMelon()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("melon");
        registerBlock(103, "melon_block", var9);
        registerBlock(104, "pumpkin_stem", (new BlockStem(var7)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        registerBlock(105, "melon_stem", (new BlockStem(var9)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("pumpkinStem"));
        registerBlock(106, "vine", (new BlockVine()).setHardness(0.2F).setStepSound(soundTypeGrass).setUnlocalizedName("vine"));
        registerBlock(107, "fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("fenceGate"));
        registerBlock(108, "brick_stairs", (new BlockStairs(var5.getDefaultState())).setUnlocalizedName("stairsBrick"));
        registerBlock(109, "stone_brick_stairs", (new BlockStairs(var8.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.DEFAULT))).setUnlocalizedName("stairsStoneBrickSmooth"));
        registerBlock(110, "mycelium", (new BlockMycelium()).setHardness(0.6F).setStepSound(soundTypeGrass).setUnlocalizedName("mycel"));
        registerBlock(111, "waterlily", (new BlockLilyPad()).setHardness(0.0F).setStepSound(soundTypeGrass).setUnlocalizedName("waterlily"));
        Block var10 = (new BlockNetherBrick()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(112, "nether_brick", var10);
        registerBlock(113, "nether_brick_fence", (new BlockFence(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherFence"));
        registerBlock(114, "nether_brick_stairs", (new BlockStairs(var10.getDefaultState())).setUnlocalizedName("stairsNetherBrick"));
        registerBlock(115, "nether_wart", (new BlockNetherWart()).setUnlocalizedName("netherStalk"));
        registerBlock(116, "enchanting_table", (new BlockEnchantmentTable()).setHardness(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable"));
        registerBlock(117, "brewing_stand", (new BlockBrewingStand()).setHardness(0.5F).setLightLevel(0.125F).setUnlocalizedName("brewingStand"));
        registerBlock(118, "cauldron", (new BlockCauldron()).setHardness(2.0F).setUnlocalizedName("cauldron"));
        registerBlock(119, "end_portal", (new BlockEndPortal(Material.portal)).setHardness(-1.0F).setResistance(6000000.0F));
        registerBlock(120, "end_portal_frame", (new BlockEndPortalFrame()).setStepSound(soundTypeGlass).setLightLevel(0.125F).setHardness(-1.0F).setUnlocalizedName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations));
        registerBlock(121, "end_stone", (new Block(Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(122, "dragon_egg", (new BlockDragonEgg()).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setLightLevel(0.125F).setUnlocalizedName("dragonEgg"));
        registerBlock(123, "redstone_lamp", (new BlockRedstoneLight(false)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
        registerBlock(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("redstoneLight"));
        registerBlock(125, "double_wooden_slab", (new BlockDoubleWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        registerBlock(126, "wooden_slab", (new BlockHalfWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("woodSlab"));
        registerBlock(127, "cocoa", (new BlockCocoa()).setHardness(0.2F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("cocoa"));
        registerBlock(128, "sandstone_stairs", (new BlockStairs(var2.getDefaultState().withProperty(BlockSandStone.field_176297_a, BlockSandStone.EnumType.SMOOTH))).setUnlocalizedName("stairsSandStone"));
        registerBlock(129, "emerald_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("oreEmerald"));
        registerBlock(130, "ender_chest", (new BlockEnderChest()).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5F));
        registerBlock(131, "tripwire_hook", (new BlockTripWireHook()).setUnlocalizedName("tripWireSource"));
        registerBlock(132, "tripwire", (new BlockTripWire()).setUnlocalizedName("tripWire"));
        registerBlock(133, "emerald_block", (new BlockCompressed(MapColor.emeraldColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockEmerald"));
        registerBlock(134, "spruce_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.SPRUCE))).setUnlocalizedName("stairsWoodSpruce"));
        registerBlock(135, "birch_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.BIRCH))).setUnlocalizedName("stairsWoodBirch"));
        registerBlock(136, "jungle_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.JUNGLE))).setUnlocalizedName("stairsWoodJungle"));
        registerBlock(137, "command_block", (new BlockCommandBlock()).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("commandBlock"));
        registerBlock(138, "beacon", (new BlockBeacon()).setUnlocalizedName("beacon").setLightLevel(1.0F));
        registerBlock(139, "cobblestone_wall", (new BlockWall(var0)).setUnlocalizedName("cobbleWall"));
        registerBlock(140, "flower_pot", (new BlockFlowerPot()).setHardness(0.0F).setStepSound(soundTypeStone).setUnlocalizedName("flowerPot"));
        registerBlock(141, "carrots", (new BlockCarrot()).setUnlocalizedName("carrots"));
        registerBlock(142, "potatoes", (new BlockPotato()).setUnlocalizedName("potatoes"));
        registerBlock(143, "wooden_button", (new BlockButtonWood()).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("button"));
        registerBlock(144, "skull", (new BlockSkull()).setHardness(1.0F).setStepSound(soundTypePiston).setUnlocalizedName("skull"));
        registerBlock(145, "anvil", (new BlockAnvil()).setHardness(5.0F).setStepSound(soundTypeAnvil).setResistance(2000.0F).setUnlocalizedName("anvil"));
        registerBlock(146, "trapped_chest", (new BlockChest(1)).setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("chestTrap"));
        registerBlock(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted("gold_block", Material.iron, 15)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_light"));
        registerBlock(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted("iron_block", Material.iron, 150)).setHardness(0.5F).setStepSound(soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
        registerBlock(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).setHardness(0.0F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        registerBlock(150, "powered_comparator", (new BlockRedstoneComparator(true)).setHardness(0.0F).setLightLevel(0.625F).setStepSound(soundTypeWood).setUnlocalizedName("comparator").disableStats());
        registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
        registerBlock(152, "redstone_block", (new BlockCompressedPowered(MapColor.tntColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setUnlocalizedName("blockRedstone"));
        registerBlock(153, "quartz_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setUnlocalizedName("netherquartz"));
        registerBlock(154, "hopper", (new BlockHopper()).setHardness(3.0F).setResistance(8.0F).setStepSound(soundTypeMetal).setUnlocalizedName("hopper"));
        Block var11 = (new BlockQuartz()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("quartzBlock");
        registerBlock(155, "quartz_block", var11);
        registerBlock(156, "quartz_stairs", (new BlockStairs(var11.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.DEFAULT))).setUnlocalizedName("stairsQuartz"));
        registerBlock(157, "activator_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setUnlocalizedName("activatorRail"));
        registerBlock(158, "dropper", (new BlockDropper()).setHardness(3.5F).setStepSound(soundTypePiston).setUnlocalizedName("dropper"));
        registerBlock(159, "stained_hardened_clay", (new BlockColored(Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardenedStained"));
        registerBlock(160, "stained_glass_pane", (new BlockStainedGlassPane()).setHardness(0.3F).setStepSound(soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
        registerBlock(161, "leaves2", (new BlockNewLeaf()).setUnlocalizedName("leaves"));
        registerBlock(162, "log2", (new BlockNewLog()).setUnlocalizedName("log"));
        registerBlock(163, "acacia_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.ACACIA))).setUnlocalizedName("stairsWoodAcacia"));
        registerBlock(164, "dark_oak_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.DARK_OAK))).setUnlocalizedName("stairsWoodDarkOak"));
        registerBlock(165, "slime", (new BlockSlime()).setUnlocalizedName("slime").setStepSound(SLIME_SOUND));
        registerBlock(166, "barrier", (new BlockBarrier()).setUnlocalizedName("barrier"));
        registerBlock(167, "iron_trapdoor", (new BlockTrapDoor(Material.iron)).setHardness(5.0F).setStepSound(soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
        registerBlock(168, "prismarine", (new BlockPrismarine()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("prismarine"));
        registerBlock(169, "sea_lantern", (new BlockSeaLantern(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("seaLantern"));
        registerBlock(170, "hay_block", (new BlockHay()).setHardness(0.5F).setStepSound(soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(171, "carpet", (new BlockCarpet()).setHardness(0.1F).setStepSound(soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        registerBlock(172, "hardened_clay", (new BlockHardenedClay()).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setUnlocalizedName("clayHardened"));
        registerBlock(173, "coal_block", (new Block(Material.rock)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(174, "packed_ice", (new BlockPackedIce()).setHardness(0.5F).setStepSound(soundTypeGlass).setUnlocalizedName("icePacked"));
        registerBlock(175, "double_plant", new BlockDoublePlant());
        registerBlock(176, "standing_banner", (new BlockBanner.BlockBannerStanding()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        registerBlock(177, "wall_banner", (new BlockBanner.BlockBannerHanging()).setHardness(1.0F).setStepSound(soundTypeWood).setUnlocalizedName("banner").disableStats());
        registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        Block var12 = (new BlockRedSandstone()).setStepSound(soundTypePiston).setHardness(0.8F).setUnlocalizedName("redSandStone");
        registerBlock(179, "red_sandstone", var12);
        registerBlock(180, "red_sandstone_stairs", (new BlockStairs(var12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH))).setUnlocalizedName("stairsRedSandStone"));
        registerBlock(181, "double_stone_slab2", (new BlockDoubleStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        registerBlock(182, "stone_slab2", (new BlockHalfStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setUnlocalizedName("stoneSlab2"));
        registerBlock(183, "spruce_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFenceGate"));
        registerBlock(184, "birch_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFenceGate"));
        registerBlock(185, "jungle_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFenceGate"));
        registerBlock(186, "dark_oak_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
        registerBlock(187, "acacia_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
        registerBlock(188, "spruce_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("spruceFence"));
        registerBlock(189, "birch_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("birchFence"));
        registerBlock(190, "jungle_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("jungleFence"));
        registerBlock(191, "dark_oak_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("darkOakFence"));
        registerBlock(192, "acacia_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setUnlocalizedName("acaciaFence"));
        registerBlock(193, "spruce_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
        registerBlock(194, "birch_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
        registerBlock(195, "jungle_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
        registerBlock(196, "acacia_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
        registerBlock(197, "dark_oak_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
        blockRegistry.validateKey();
        Iterator var13 = blockRegistry.iterator();
        Block var14;

        while (var13.hasNext())
        {
            var14 = (Block)var13.next();

            if (var14.blockMaterial == Material.air)
            {
                var14.useNeighborBrightness = false;
            }
            else
            {
                boolean var15 = false;
                boolean var16 = var14 instanceof BlockStairs;
                boolean var17 = var14 instanceof BlockSlab;
                boolean var18 = var14 == var6;
                boolean var19 = var14.translucent;
                boolean var20 = var14.lightOpacity == 0;

                if (var16 || var17 || var18 || var19 || var20)
                {
                    var15 = true;
                }

                var14.useNeighborBrightness = var15;
            }
        }

        var13 = blockRegistry.iterator();

        while (var13.hasNext())
        {
            var14 = (Block)var13.next();
            Iterator var21 = var14.getBlockState().getValidStates().iterator();

            while (var21.hasNext())
            {
                IBlockState var22 = (IBlockState)var21.next();
                int var23 = blockRegistry.getIDForObject(var14) << 4 | var14.getMetaFromState(var22);
                BLOCK_STATE_IDS.put(var22, var23);
            }
        }
    }

    private static void registerBlock(int id, ResourceLocation textualID, Block block_)
    {
        blockRegistry.register(id, textualID, block_);
    }

    private static void registerBlock(int id, String textualID, Block block_)
    {
        registerBlock(id, new ResourceLocation(textualID), block_);
    }

    public static enum EnumOffsetType
    {
        NONE("NONE", 0),
        XZ("XZ", 1),
        XYZ("XYZ", 2);

        private static final Block.EnumOffsetType[] $VALUES = new Block.EnumOffsetType[]{NONE, XZ, XYZ};
        

        private EnumOffsetType(String p_i45733_1_, int p_i45733_2_) {}
    }

    public static class SoundType
    {
        public final String soundName;
        public final float volume;
        public final float frequency;
        

        public SoundType(String name, float volume, float frequency)
        {
            this.soundName = name;
            this.volume = volume;
            this.frequency = frequency;
        }

        public float getVolume()
        {
            return this.volume;
        }

        public float getFrequency()
        {
            return this.frequency;
        }

        public String getBreakSound()
        {
            return "dig." + this.soundName;
        }

        public String getStepSound()
        {
            return "step." + this.soundName;
        }

        public String getPlaceSound()
        {
            return this.getBreakSound();
        }
    }
}
