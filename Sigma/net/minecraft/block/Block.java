package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import info.sigmaclient.Client;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventBlockBounds;
import info.sigmaclient.module.impl.render.Xray;
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

public class Block {
    /**
     * ResourceLocation for the Air block
     */
    private static final ResourceLocation AIR_ID = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey blockRegistry = new RegistryNamespacedDefaultedByKey(Block.AIR_ID);
    public static final ObjectIntIdentityMap BLOCK_STATE_IDS = new ObjectIntIdentityMap();
    private CreativeTabs displayOnCreativeTab;
    public static final Block.SoundType soundTypeStone = new Block.SoundType("stone", 1.0F, 1.0F);

    /**
     * the wood sound type
     */
    public static final Block.SoundType soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);

    /**
     * the gravel sound type
     */
    public static final Block.SoundType soundTypeGravel = new Block.SoundType("gravel", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeGrass = new Block.SoundType("grass", 1.0F, 1.0F);
    public static final Block.SoundType soundTypePiston = new Block.SoundType("stone", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeMetal = new Block.SoundType("stone", 1.0F, 1.5F);
    public static final Block.SoundType soundTypeGlass = new Block.SoundType("stone", 1.0F, 1.0F) {
        private static final String __OBFID = "CL_00000200";

        @Override
        public String getBreakSound() {
            return "dig.glass";
        }

        @Override
        public String getPlaceSound() {
            return "step.stone";
        }
    };
    public static final Block.SoundType soundTypeCloth = new Block.SoundType("cloth", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeSand = new Block.SoundType("sand", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeSnow = new Block.SoundType("snow", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeLadder = new Block.SoundType("ladder", 1.0F, 1.0F) {
        private static final String __OBFID = "CL_00000201";

        @Override
        public String getBreakSound() {
            return "dig.wood";
        }
    };
    public static final Block.SoundType soundTypeAnvil = new Block.SoundType("anvil", 0.3F, 1.0F) {
        private static final String __OBFID = "CL_00000202";

        @Override
        public String getBreakSound() {
            return "dig.stone";
        }

        @Override
        public String getPlaceSound() {
            return "random.anvil_land";
        }
    };
    public static final Block.SoundType SLIME_SOUND = new Block.SoundType("slime", 1.0F, 1.0F) {
        private static final String __OBFID = "CL_00002133";

        @Override
        public String getBreakSound() {
            return "mob.slime.big";
        }

        @Override
        public String getPlaceSound() {
            return "mob.slime.big";
        }

        @Override
        public String getStepSound() {
            return "mob.slime.small";
        }
    };
    protected boolean fullBlock;

    /**
     * How much light is subtracted for going through this block
     */
    protected int lightOpacity;
    protected boolean translucent;

    /**
     * Amount of light emitted
     */
    protected int lightValue;

    /**
     * Flag if block should use the brightest neighbor light value as its own
     */
    protected boolean useNeighborBrightness;

    /**
     * Indicates how many hits it takes to break a block.
     */
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats = true;

    /**
     * Flags whether or not this block is of a type that needs random ticking.
     * Ref-counted by ExtendedBlockStorage in order to broadly cull a chunk from
     * the random chunk update list for efficiency's sake.
     */
    protected boolean needsRandomTick;

    /**
     * true if the Block contains a Tile Entity
     */
    protected boolean isBlockContainer;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;

    /**
     * Sound of stepping on the block
     */
    public Block.SoundType stepSound;
    public float blockParticleGravity;
    protected final Material blockMaterial;

    /**
     * Determines how much velocity is maintained while moving on top of this
     * block
     */
    public float slipperiness;
    protected final BlockState blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;
    private static final String __OBFID = "CL_00000199";

    public static int getIdFromBlock(Block blockIn) {
        return Block.blockRegistry.getIDForObject(blockIn);
    }

    /**
     * Get a unique ID for the given BlockState, containing both BlockID and
     * metadata
     */
    public static int getStateId(IBlockState state) {
        return Block.getIdFromBlock(state.getBlock()) + (state.getBlock().getMetaFromState(state) << 12);
    }

    public static Block getBlockById(int id) {
        return (Block) Block.blockRegistry.getObjectById(id);
    }

    /**
     * Get a BlockState by it's ID (see getStateId)
     */
    public static IBlockState getStateById(int id) {
        int var1 = id & 4095;
        int var2 = id >> 12 & 15;
        return Block.getBlockById(var1).getStateFromMeta(var2);
    }

    public static Block getBlockFromItem(Item itemIn) {
        return itemIn instanceof ItemBlock ? ((ItemBlock) itemIn).getBlock() : null;
    }

    public static Block getBlockFromName(String name) {
        ResourceLocation var1 = new ResourceLocation(name);

        if (Block.blockRegistry.containsKey(var1)) {
            return (Block) Block.blockRegistry.getObject(var1);
        } else {
            try {
                return (Block) Block.blockRegistry.getObjectById(Integer.parseInt(name));
            } catch (NumberFormatException var3) {
                return null;
            }
        }
    }

    public boolean isFullBlock() {
        return fullBlock;
    }

    public int getLightOpacity() {
        return lightOpacity;
    }

    public boolean isTranslucent() {
        return translucent;
    }

    public int getLightValue() {
        return lightValue;
    }

    /**
     * Should block use the brightest neighbor light value as its own
     */
    public boolean getUseNeighborBrightness() {
        return useNeighborBrightness;
    }

    /**
     * Get a material of block
     */
    public Material getMaterial() {
        return blockMaterial;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state) {
        return getMaterial().getMaterialMapColor();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        if (state != null && !state.getPropertyNames().isEmpty()) {
            throw new IllegalArgumentException("Don\'t know how to convert " + state + " back into data...");
        } else {
            return 0;
        }
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    protected Block(Material materialIn) {
        stepSound = Block.soundTypeStone;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        blockMaterial = materialIn;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        fullBlock = isOpaqueCube();
        lightOpacity = isOpaqueCube() ? 255 : 0;
        translucent = !materialIn.blocksLight();
        blockState = createBlockState();
        setDefaultState(blockState.getBaseState());
    }

    /**
     * Sets the footstep sound for the block. Returns the object for convenience
     * in constructing.
     */
    protected Block setStepSound(Block.SoundType sound) {
        stepSound = sound;
        return this;
    }

    /**
     * Sets how much light is blocked going through this block. Returns the
     * object for convenience in constructing.
     */
    public Block setLightOpacity(int opacity) {
        lightOpacity = opacity;
        return this;
    }

    /**
     * Sets the light value that the block emits. Returns resulting block
     * instance for constructing convenience. Args: level
     */
    protected Block setLightLevel(float value) {
        lightValue = (int) (15.0F * value);
        return this;
    }

    /**
     * Sets the the blocks resistance to explosions. Returns the object for
     * convenience in constructing.
     */
    protected Block setResistance(float resistance) {
        blockResistance = resistance * 3.0F;
        return this;
    }

    /**
     * Indicate if a material is a normal solid opaque cube
     */
    public boolean isSolidFullCube() {
        return blockMaterial.blocksMovement() && isFullCube();
    }

    public boolean isNormalCube() {
        return blockMaterial.isOpaque() && isFullCube() && !canProvidePower();
    }

    public boolean isVisuallyOpaque() {
        return blockMaterial.blocksMovement() && isFullCube();
    }

    public boolean isFullCube() {
        return true;
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return !blockMaterial.blocksMovement();
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 3;
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for
     * e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return false;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    protected Block setHardness(float hardness) {
        blockHardness = hardness;

        if (blockResistance < hardness * 5.0F) {
            blockResistance = hardness * 5.0F;
        }

        return this;
    }

    protected Block setBlockUnbreakable() {
        setHardness(-1.0F);
        return this;
    }

    public float getBlockHardness(World worldIn, BlockPos pos) {
        return blockHardness;
    }

    /**
     * Sets whether this block type will receive random update ticks
     */
    protected Block setTickRandomly(boolean shouldTick) {
        needsRandomTick = shouldTick;
        return this;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking.
     * Called for ref-counting purposes by ExtendedBlockStorage in order to
     * broadly cull a chunk from the random chunk update list for efficiency's
     * sake.
     */
    public boolean getTickRandomly() {
        return needsRandomTick;
    }

    public boolean hasTileEntity() {
        return isBlockContainer;
    }

    protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        Block var3 = worldIn.getBlockState(pos).getBlock();
        int level = worldIn.getCombinedLight(pos, var3.getLightValue());
        if (level == 0 && var3 instanceof BlockSlab) {
            pos = pos.offsetDown();
            var3 = worldIn.getBlockState(pos).getBlock();
            return worldIn.getCombinedLight(pos, var3.getLightValue());
        } else {
            return level;
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Xray x = (Xray) Client.getModuleManager().get(Xray.class);
        if (x.isEnabled() && !((Boolean) x.getSetting(Xray.CAVEFINDER).getValue())) {
            if (x.containsID(getIdFromBlock(this))) {
                return true;
            }
        }
        return side == EnumFacing.DOWN && minY > 0.0D || (side == EnumFacing.UP && maxY < 1.0D || (side == EnumFacing.NORTH && minZ > 0.0D || (side == EnumFacing.SOUTH && maxZ < 1.0D || (side == EnumFacing.WEST && minX > 0.0D || (side == EnumFacing.EAST && maxX < 1.0D || !worldIn.getBlockState(pos).getBlock().isOpaqueCube())))));
    }

    /**
     * Whether this Block is solid on the given Side
     */
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + maxY, pos.getZ() + maxZ);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the
     * given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        AxisAlignedBB var7 = getCollisionBoundingBox(worldIn, pos, state);
        EventBlockBounds ebb = (EventBlockBounds) EventSystem.getInstance(EventBlockBounds.class);
        ebb.fire(this, pos, var7);
        if (!ebb.isCancelled() && var7 != null && mask.intersectsWith(var7)) {
            list.add(var7);
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + maxY, pos.getZ() + maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return isCollidable();
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable() {
        return true;
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops
     * to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        updateTick(worldIn, pos, state, random);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    }

    /**
     * Called when a player destroys this Block
     */
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn) {
        return 10;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 1;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    /**
     * Get the hardness of this Block relative to the ability of the given
     * player
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos) {
        float var4 = getBlockHardness(worldIn, pos);
        return var4 < 0.0F ? 0.0F : (!playerIn.canHarvestBlock(this) ? playerIn.func_180471_a(this) / var4 / 100.0F : playerIn.func_180471_a(this) / var4 / 30.0F);
    }

    /**
     * Spawn this Block's drops into the World as EntityItems
     *
     * @param forture the level of the Fortune enchantment on the player's tool
     */
    public final void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture) {
        dropBlockAsItemWithChance(worldIn, pos, state, 1.0F, forture);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always,
     *                0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int var6 = quantityDroppedWithBonus(fortune, worldIn.rand);

            for (int var7 = 0; var7 < var6; ++var7) {
                if (worldIn.rand.nextFloat() <= chance) {
                    Item var8 = getItemDropped(state, worldIn.rand, fortune);

                    if (var8 != null) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(var8, 1, damageDropped(state)));
                    }
                }
            }
        }
    }

    /**
     * Spawns the given ItemStack as an EntityItem into the World at the given
     * position
     */
    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            float var3 = 0.5F;
            double var4 = worldIn.rand.nextFloat() * var3 + (1.0F - var3) * 0.5D;
            double var6 = worldIn.rand.nextFloat() * var3 + (1.0F - var3) * 0.5D;
            double var8 = worldIn.rand.nextFloat() * var3 + (1.0F - var3) * 0.5D;
            EntityItem var10 = new EntityItem(worldIn, pos.getX() + var4, pos.getY() + var6, pos.getZ() + var8, stack);
            var10.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(var10);
        }
    }

    /**
     * Spawns the given amount of experience into the World as XP orb entities
     *
     * @param amount The amount of XP to spawn
     */
    protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
        if (!worldIn.isRemote) {
            while (amount > 0) {
                int var4 = EntityXPOrb.getXPSplit(amount);
                amount -= var4;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, var4));
            }
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return 0;
    }

    /**
     * Returns how much this block can resist explosions from the passed in
     * entity.
     */
    public float getExplosionResistance(Entity exploder) {
        return blockResistance / 5.0F;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit.
     *
     * @param start The start vector
     * @param end   The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        setBlockBoundsBasedOnState(worldIn, pos);
        start = start.addVector((-pos.getX()), (-pos.getY()), (-pos.getZ()));
        end = end.addVector((-pos.getX()), (-pos.getY()), (-pos.getZ()));
        Vec3 var5 = start.getIntermediateWithXValue(end, minX);
        Vec3 var6 = start.getIntermediateWithXValue(end, maxX);
        Vec3 var7 = start.getIntermediateWithYValue(end, minY);
        Vec3 var8 = start.getIntermediateWithYValue(end, maxY);
        Vec3 var9 = start.getIntermediateWithZValue(end, minZ);
        Vec3 var10 = start.getIntermediateWithZValue(end, maxZ);

        if (!isVecInsideYZBounds(var5)) {
            var5 = null;
        }

        if (!isVecInsideYZBounds(var6)) {
            var6 = null;
        }

        if (!isVecInsideXZBounds(var7)) {
            var7 = null;
        }

        if (!isVecInsideXZBounds(var8)) {
            var8 = null;
        }

        if (!isVecInsideXYBounds(var9)) {
            var9 = null;
        }

        if (!isVecInsideXYBounds(var10)) {
            var10 = null;
        }

        Vec3 var11 = null;

        if (var5 != null && (var11 == null || start.squareDistanceTo(var5) < start.squareDistanceTo(var11))) {
            var11 = var5;
        }

        if (var6 != null && (var11 == null || start.squareDistanceTo(var6) < start.squareDistanceTo(var11))) {
            var11 = var6;
        }

        if (var7 != null && (var11 == null || start.squareDistanceTo(var7) < start.squareDistanceTo(var11))) {
            var11 = var7;
        }

        if (var8 != null && (var11 == null || start.squareDistanceTo(var8) < start.squareDistanceTo(var11))) {
            var11 = var8;
        }

        if (var9 != null && (var11 == null || start.squareDistanceTo(var9) < start.squareDistanceTo(var11))) {
            var11 = var9;
        }

        if (var10 != null && (var11 == null || start.squareDistanceTo(var10) < start.squareDistanceTo(var11))) {
            var11 = var10;
        }

        if (var11 == null) {
            return null;
        } else {
            EnumFacing var12 = null;

            if (var11 == var5) {
                var12 = EnumFacing.WEST;
            }

            if (var11 == var6) {
                var12 = EnumFacing.EAST;
            }

            if (var11 == var7) {
                var12 = EnumFacing.DOWN;
            }

            if (var11 == var8) {
                var12 = EnumFacing.UP;
            }

            if (var11 == var9) {
                var12 = EnumFacing.NORTH;
            }

            if (var11 == var10) {
                var12 = EnumFacing.SOUTH;
            }

            return new MovingObjectPosition(var11.addVector(pos.getX(), pos.getY(), pos.getZ()), var12, pos);
        }
    }

    /**
     * Checks if a vector is within the Y and Z bounds of the block.
     */
    private boolean isVecInsideYZBounds(Vec3 point) {
        return point == null ? false : point.yCoord >= minY && point.yCoord <= maxY && point.zCoord >= minZ && point.zCoord <= maxZ;
    }

    /**
     * Checks if a vector is within the X and Z bounds of the block.
     */
    private boolean isVecInsideXZBounds(Vec3 point) {
        return point == null ? false : point.xCoord >= minX && point.xCoord <= maxX && point.zCoord >= minZ && point.zCoord <= maxZ;
    }

    /**
     * Checks if a vector is within the X and Y bounds of the block.
     */
    private boolean isVecInsideXYBounds(Vec3 point) {
        return point == null ? false : point.xCoord >= minX && point.xCoord <= maxX && point.yCoord >= minY && point.yCoord <= maxY;
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    }

    public EnumWorldBlockLayer getBlockLayer() {
        Xray x = (Xray) Client.getModuleManager().get(Xray.class);
        if (x.isEnabled()) {
            return x.containsID(getIdFromBlock(this)) ? EnumWorldBlockLayer.SOLID : EnumWorldBlockLayer.TRANSLUCENT;
        }
        return EnumWorldBlockLayer.SOLID;
    }

    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
        return canPlaceBlockOnSide(worldIn, pos, side);
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the
     * block)
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getStateFromMeta(meta);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    }

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
    }

    /**
     * returns the block bounderies minX value
     */
    public final double getBlockBoundsMinX() {
        return minX;
    }

    /**
     * returns the block bounderies maxX value
     */
    public final double getBlockBoundsMaxX() {
        return maxX;
    }

    /**
     * returns the block bounderies minY value
     */
    public final double getBlockBoundsMinY() {
        return minY;
    }

    /**
     * returns the block bounderies maxY value
     */
    public final double getBlockBoundsMaxY() {
        return maxY;
    }

    /**
     * returns the block bounderies minZ value
     */
    public final double getBlockBoundsMinZ() {
        return minZ;
    }

    /**
     * returns the block bounderies maxZ value
     */
    public final double getBlockBoundsMaxZ() {
        return maxZ;
    }

    public int getBlockColor() {
        return 16777215;
    }

    public int getRenderColor(IBlockState state) {
        return 16777215;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return 16777215;
    }

    public final int colorMultiplier(IBlockAccess worldIn, BlockPos pos) {
        return this.colorMultiplier(worldIn, pos, 0);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    public boolean canProvidePower() {
        return false;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return 0;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        playerIn.addExhaustion(0.025F);

        if (canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(playerIn)) {
            ItemStack var7 = createStackedBlock(state);

            if (var7 != null) {
                Block.spawnAsEntity(worldIn, pos, var7);
            }
        } else {
            int var6 = EnchantmentHelper.getFortuneModifier(playerIn);
            dropBlockAsItem(worldIn, pos, state, var6);
        }
    }

    protected boolean canSilkHarvest() {
        return isFullCube() && !isBlockContainer;
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        int var2 = 0;
        Item var3 = Item.getItemFromBlock(this);

        if (var3 != null && var3.getHasSubtypes()) {
            var2 = getMetaFromState(state);
        }

        return new ItemStack(var3, 1, var2);
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return quantityDropped(random);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    }

    public Block setUnlocalizedName(String name) {
        unlocalizedName = name;
        return this;
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the
     * front.
     */
    public String getUnlocalizedName() {
        return "tile." + unlocalizedName;
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        return false;
    }

    /**
     * Return the state of blocks statistics flags - if the block is counted for
     * mined and placed.
     */
    public boolean getEnableStats() {
        return enableStats;
    }

    protected Block disableStats() {
        enableStats = false;
        return this;
    }

    public int getMobilityFlag() {
        return blockMaterial.getMaterialMobility();
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    public float getAmbientOcclusionLightValue() {
        if (Client.getModuleManager().isEnabled(Xray.class)) {
            return 1;
        }
        return isSolidFullCube() ? 0.2F : 1.0F;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     *
     * @param fallDistance The distance the entity has fallen before landing
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.fall(fallDistance, 1.0F);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update
     * motionY because the entity will not do that on its own
     */
    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.motionY = 0.0D;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(this);
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        return damageDropped(worldIn.getBlockState(pos));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }

    /**
     * Returns the CreativeTab to display the given block on.
     */
    public CreativeTabs getCreativeTabToDisplayOn() {
        return displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs tab) {
        displayOnCreativeTab = tab;
        return this;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
    }

    /**
     * Called similar to random ticks, but only when it is raining.
     */
    public void fillWithRain(World worldIn, BlockPos pos) {
    }

    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot() {
        return false;
    }

    public boolean requiresUpdates() {
        return true;
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return true;
    }

    public boolean isAssociatedBlock(Block other) {
        return this == other;
    }

    public static boolean isEqualTo(Block blockIn, Block other) {
        return blockIn != null && other != null ? (blockIn == other ? true : blockIn.isAssociatedBlock(other)) : false;
    }

    public boolean hasComparatorInputOverride() {
        return false;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return 0;
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity
     * (Minecarts, Endermen, ...)
     */
    public IBlockState getStateForEntityRender(IBlockState state) {
        return state;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[0]);
    }

    public BlockState getBlockState() {
        return blockState;
    }

    protected final void setDefaultState(IBlockState state) {
        defaultBlockState = state;
    }

    public final IBlockState getDefaultState() {
        return defaultBlockState;
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered
     * slightly offset.
     */
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.NONE;
    }

    public static void registerBlocks() {
        Block.registerBlock(0, Block.AIR_ID, (new BlockAir()).setUnlocalizedName("air"));
        Block.registerBlock(1, "stone", (new BlockStone()).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stone"));
        Block.registerBlock(2, "grass", (new BlockGrass()).setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("grass"));
        Block.registerBlock(3, "dirt", (new BlockDirt()).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setUnlocalizedName("dirt"));
        Block var0 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(4, "cobblestone", var0);
        Block var1 = (new BlockPlanks()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("wood");
        Block.registerBlock(5, "planks", var1);
        Block.registerBlock(6, "sapling", (new BlockSapling()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("sapling"));
        Block.registerBlock(7, "bedrock", (new Block(Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(8, "flowing_water", (new BlockDynamicLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(9, "water", (new BlockStaticLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        Block.registerBlock(10, "flowing_lava", (new BlockDynamicLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(11, "lava", (new BlockStaticLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        Block.registerBlock(12, "sand", (new BlockSand()).setHardness(0.5F).setStepSound(Block.soundTypeSand).setUnlocalizedName("sand"));
        Block.registerBlock(13, "gravel", (new BlockGravel()).setHardness(0.6F).setStepSound(Block.soundTypeGravel).setUnlocalizedName("gravel"));
        Block.registerBlock(14, "gold_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreGold"));
        Block.registerBlock(15, "iron_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreIron"));
        Block.registerBlock(16, "coal_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreCoal"));
        Block.registerBlock(17, "log", (new BlockOldLog()).setUnlocalizedName("log"));
        Block.registerBlock(18, "leaves", (new BlockOldLeaf()).setUnlocalizedName("leaves"));
        Block.registerBlock(19, "sponge", (new BlockSponge()).setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("sponge"));
        Block.registerBlock(20, "glass", (new BlockGlass(Material.glass, false)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("glass"));
        Block.registerBlock(21, "lapis_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreLapis"));
        Block.registerBlock(22, "lapis_block", (new BlockCompressed(MapColor.lapisColor)).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(23, "dispenser", (new BlockDispenser()).setHardness(3.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("dispenser"));
        Block var2 = (new BlockSandStone()).setStepSound(Block.soundTypePiston).setHardness(0.8F).setUnlocalizedName("sandStone");
        Block.registerBlock(24, "sandstone", var2);
        Block.registerBlock(25, "noteblock", (new BlockNote()).setHardness(0.8F).setUnlocalizedName("musicBlock"));
        Block.registerBlock(26, "bed", (new BlockBed()).setStepSound(Block.soundTypeWood).setHardness(0.2F).setUnlocalizedName("bed").disableStats());
        Block.registerBlock(27, "golden_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("goldenRail"));
        Block.registerBlock(28, "detector_rail", (new BlockRailDetector()).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("detectorRail"));
        Block.registerBlock(29, "sticky_piston", (new BlockPistonBase(true)).setUnlocalizedName("pistonStickyBase"));
        Block.registerBlock(30, "web", (new BlockWeb()).setLightOpacity(1).setHardness(4.0F).setUnlocalizedName("web"));
        Block.registerBlock(31, "tallgrass", (new BlockTallGrass()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("tallgrass"));
        Block.registerBlock(32, "deadbush", (new BlockDeadBush()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("deadbush"));
        Block.registerBlock(33, "piston", (new BlockPistonBase(false)).setUnlocalizedName("pistonBase"));
        Block.registerBlock(34, "piston_head", new BlockPistonExtension());
        Block.registerBlock(35, "wool", (new BlockColored(Material.cloth)).setHardness(0.8F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cloth"));
        Block.registerBlock(36, "piston_extension", new BlockPistonMoving());
        Block.registerBlock(37, "yellow_flower", (new BlockYellowFlower()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("flower1"));
        Block.registerBlock(38, "red_flower", (new BlockRedFlower()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("flower2"));
        Block var3 = (new BlockMushroom()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setLightLevel(0.125F).setUnlocalizedName("mushroom");
        Block.registerBlock(39, "brown_mushroom", var3);
        Block var4 = (new BlockMushroom()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("mushroom");
        Block.registerBlock(40, "red_mushroom", var4);
        Block.registerBlock(41, "gold_block", (new BlockCompressed(MapColor.goldColor)).setHardness(3.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockGold"));
        Block.registerBlock(42, "iron_block", (new BlockCompressed(MapColor.ironColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockIron"));
        Block.registerBlock(43, "double_stone_slab", (new BlockDoubleStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block.registerBlock(44, "stone_slab", (new BlockHalfStoneSlab()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab"));
        Block var5 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(45, "brick_block", var5);
        Block.registerBlock(46, "tnt", (new BlockTNT()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("tnt"));
        Block.registerBlock(47, "bookshelf", (new BlockBookshelf()).setHardness(1.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("bookshelf"));
        Block.registerBlock(48, "mossy_cobblestone", (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(49, "obsidian", (new BlockObsidian()).setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("obsidian"));
        Block.registerBlock(50, "torch", (new BlockTorch()).setHardness(0.0F).setLightLevel(0.9375F).setStepSound(Block.soundTypeWood).setUnlocalizedName("torch"));
        Block.registerBlock(51, "fire", (new BlockFire()).setHardness(0.0F).setLightLevel(1.0F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("fire").disableStats());
        Block.registerBlock(52, "mob_spawner", (new BlockMobSpawner()).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
        Block.registerBlock(53, "oak_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));
        Block.registerBlock(54, "chest", (new BlockChest(0)).setHardness(2.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("chest"));
        Block.registerBlock(55, "redstone_wire", (new BlockRedstoneWire()).setHardness(0.0F).setStepSound(Block.soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
        Block.registerBlock(56, "diamond_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreDiamond"));
        Block.registerBlock(57, "diamond_block", (new BlockCompressed(MapColor.diamondColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockDiamond"));
        Block.registerBlock(58, "crafting_table", (new BlockWorkbench()).setHardness(2.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("workbench"));
        Block.registerBlock(59, "wheat", (new BlockCrops()).setUnlocalizedName("crops"));
        Block var6 = (new BlockFarmland()).setHardness(0.6F).setStepSound(Block.soundTypeGravel).setUnlocalizedName("farmland");
        Block.registerBlock(60, "farmland", var6);
        Block.registerBlock(61, "furnace", (new BlockFurnace(false)).setHardness(3.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(62, "lit_furnace", (new BlockFurnace(true)).setHardness(3.5F).setStepSound(Block.soundTypePiston).setLightLevel(0.875F).setUnlocalizedName("furnace"));
        Block.registerBlock(63, "standing_sign", (new BlockStandingSign()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(64, "wooden_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorOak").disableStats());
        Block.registerBlock(65, "ladder", (new BlockLadder()).setHardness(0.4F).setStepSound(Block.soundTypeLadder).setUnlocalizedName("ladder"));
        Block.registerBlock(66, "rail", (new BlockRail()).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("rail"));
        Block.registerBlock(67, "stone_stairs", (new BlockStairs(var0.getDefaultState())).setUnlocalizedName("stairsStone"));
        Block.registerBlock(68, "wall_sign", (new BlockWallSign()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("sign").disableStats());
        Block.registerBlock(69, "lever", (new BlockLever()).setHardness(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("lever"));
        Block.registerBlock(70, "stone_pressure_plate", (new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS)).setHardness(0.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("pressurePlateStone"));
        Block.registerBlock(71, "iron_door", (new BlockDoor(Material.iron)).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
        Block.registerBlock(72, "wooden_pressure_plate", (new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING)).setHardness(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("pressurePlateWood"));
        Block.registerBlock(73, "redstone_ore", (new BlockRedstoneOre(false)).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRedstone"));
        Block.registerBlock(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("notGate"));
        Block.registerBlock(76, "redstone_torch", (new BlockRedstoneTorch(true)).setHardness(0.0F).setLightLevel(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(77, "stone_button", (new BlockButtonStone()).setHardness(0.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("button"));
        Block.registerBlock(78, "snow_layer", (new BlockSnow()).setHardness(0.1F).setStepSound(Block.soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
        Block.registerBlock(79, "ice", (new BlockIce()).setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass).setUnlocalizedName("ice"));
        Block.registerBlock(80, "snow", (new BlockSnowBlock()).setHardness(0.2F).setStepSound(Block.soundTypeSnow).setUnlocalizedName("snow"));
        Block.registerBlock(81, "cactus", (new BlockCactus()).setHardness(0.4F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cactus"));
        Block.registerBlock(82, "clay", (new BlockClay()).setHardness(0.6F).setStepSound(Block.soundTypeGravel).setUnlocalizedName("clay"));
        Block.registerBlock(83, "reeds", (new BlockReed()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("reeds").disableStats());
        Block.registerBlock(84, "jukebox", (new BlockJukebox()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("jukebox"));
        Block.registerBlock(85, "fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("fence"));
        Block var7 = (new BlockPumpkin()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkin");
        Block.registerBlock(86, "pumpkin", var7);
        Block.registerBlock(87, "netherrack", (new BlockNetherrack()).setHardness(0.4F).setStepSound(Block.soundTypePiston).setUnlocalizedName("hellrock"));
        Block.registerBlock(88, "soul_sand", (new BlockSoulSand()).setHardness(0.5F).setStepSound(Block.soundTypeSand).setUnlocalizedName("hellsand"));
        Block.registerBlock(89, "glowstone", (new BlockGlowstone(Material.glass)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("lightgem"));
        Block.registerBlock(90, "portal", (new BlockPortal()).setHardness(-1.0F).setStepSound(Block.soundTypeGlass).setLightLevel(0.75F).setUnlocalizedName("portal"));
        Block.registerBlock(91, "lit_pumpkin", (new BlockPumpkin()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setLightLevel(1.0F).setUnlocalizedName("litpumpkin"));
        Block.registerBlock(92, "cake", (new BlockCake()).setHardness(0.5F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cake").disableStats());
        Block.registerBlock(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(94, "powered_repeater", (new BlockRedstoneRepeater(true)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("diode").disableStats());
        Block.registerBlock(95, "stained_glass", (new BlockStainedGlass(Material.glass)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("stainedGlass"));
        Block.registerBlock(96, "trapdoor", (new BlockTrapDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
        Block.registerBlock(97, "monster_egg", (new BlockSilverfish()).setHardness(0.75F).setUnlocalizedName("monsterStoneEgg"));
        Block var8 = (new BlockStoneBrick()).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stonebricksmooth");
        Block.registerBlock(98, "stonebrick", var8);
        Block.registerBlock(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.wood, var3)).setHardness(0.2F).setStepSound(Block.soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(100, "red_mushroom_block", (new BlockHugeMushroom(Material.wood, var4)).setHardness(0.2F).setStepSound(Block.soundTypeWood).setUnlocalizedName("mushroom"));
        Block.registerBlock(101, "iron_bars", (new BlockPane(Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("fenceIron"));
        Block.registerBlock(102, "glass_pane", (new BlockPane(Material.glass, false)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("thinGlass"));
        Block var9 = (new BlockMelon()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("melon");
        Block.registerBlock(103, "melon_block", var9);
        Block.registerBlock(104, "pumpkin_stem", (new BlockStem(var7)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(105, "melon_stem", (new BlockStem(var9)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkinStem"));
        Block.registerBlock(106, "vine", (new BlockVine()).setHardness(0.2F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("vine"));
        Block.registerBlock(107, "fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("fenceGate"));
        Block.registerBlock(108, "brick_stairs", (new BlockStairs(var5.getDefaultState())).setUnlocalizedName("stairsBrick"));
        Block.registerBlock(109, "stone_brick_stairs", (new BlockStairs(var8.getDefaultState().withProperty(BlockStoneBrick.VARIANT_PROP, BlockStoneBrick.EnumType.DEFAULT))).setUnlocalizedName("stairsStoneBrickSmooth"));
        Block.registerBlock(110, "mycelium", (new BlockMycelium()).setHardness(0.6F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("mycel"));
        Block.registerBlock(111, "waterlily", (new BlockLilyPad()).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("waterlily"));
        Block var10 = (new BlockNetherBrick()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
        Block.registerBlock(112, "nether_brick", var10);
        Block.registerBlock(113, "nether_brick_fence", (new BlockFence(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherFence"));
        Block.registerBlock(114, "nether_brick_stairs", (new BlockStairs(var10.getDefaultState())).setUnlocalizedName("stairsNetherBrick"));
        Block.registerBlock(115, "nether_wart", (new BlockNetherWart()).setUnlocalizedName("netherStalk"));
        Block.registerBlock(116, "enchanting_table", (new BlockEnchantmentTable()).setHardness(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable"));
        Block.registerBlock(117, "brewing_stand", (new BlockBrewingStand()).setHardness(0.5F).setLightLevel(0.125F).setUnlocalizedName("brewingStand"));
        Block.registerBlock(118, "cauldron", (new BlockCauldron()).setHardness(2.0F).setUnlocalizedName("cauldron"));
        Block.registerBlock(119, "end_portal", (new BlockEndPortal(Material.portal)).setHardness(-1.0F).setResistance(6000000.0F));
        Block.registerBlock(120, "end_portal_frame", (new BlockEndPortalFrame()).setStepSound(Block.soundTypeGlass).setLightLevel(0.125F).setHardness(-1.0F).setUnlocalizedName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations));
        Block.registerBlock(121, "end_stone", (new Block(Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(122, "dragon_egg", (new BlockDragonEgg()).setHardness(3.0F).setResistance(15.0F).setStepSound(Block.soundTypePiston).setLightLevel(0.125F).setUnlocalizedName("dragonEgg"));
        Block.registerBlock(123, "redstone_lamp", (new BlockRedstoneLight(false)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
        Block.registerBlock(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("redstoneLight"));
        Block.registerBlock(125, "double_wooden_slab", (new BlockDoubleWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(126, "wooden_slab", (new BlockHalfWoodSlab()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("woodSlab"));
        Block.registerBlock(127, "cocoa", (new BlockCocoa()).setHardness(0.2F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("cocoa"));
        Block.registerBlock(128, "sandstone_stairs", (new BlockStairs(var2.getDefaultState().withProperty(BlockSandStone.field_176297_a, BlockSandStone.EnumType.SMOOTH))).setUnlocalizedName("stairsSandStone"));
        Block.registerBlock(129, "emerald_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreEmerald"));
        Block.registerBlock(130, "ender_chest", (new BlockEnderChest()).setHardness(22.5F).setResistance(1000.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5F));
        Block.registerBlock(131, "tripwire_hook", (new BlockTripWireHook()).setUnlocalizedName("tripWireSource"));
        Block.registerBlock(132, "tripwire", (new BlockTripWire()).setUnlocalizedName("tripWire"));
        Block.registerBlock(133, "emerald_block", (new BlockCompressed(MapColor.emeraldColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockEmerald"));
        Block.registerBlock(134, "spruce_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.SPRUCE))).setUnlocalizedName("stairsWoodSpruce"));
        Block.registerBlock(135, "birch_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.BIRCH))).setUnlocalizedName("stairsWoodBirch"));
        Block.registerBlock(136, "jungle_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.JUNGLE))).setUnlocalizedName("stairsWoodJungle"));
        Block.registerBlock(137, "command_block", (new BlockCommandBlock()).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("commandBlock"));
        Block.registerBlock(138, "beacon", (new BlockBeacon()).setUnlocalizedName("beacon").setLightLevel(1.0F));
        Block.registerBlock(139, "cobblestone_wall", (new BlockWall(var0)).setUnlocalizedName("cobbleWall"));
        Block.registerBlock(140, "flower_pot", (new BlockFlowerPot()).setHardness(0.0F).setStepSound(Block.soundTypeStone).setUnlocalizedName("flowerPot"));
        Block.registerBlock(141, "carrots", (new BlockCarrot()).setUnlocalizedName("carrots"));
        Block.registerBlock(142, "potatoes", (new BlockPotato()).setUnlocalizedName("potatoes"));
        Block.registerBlock(143, "wooden_button", (new BlockButtonWood()).setHardness(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("button"));
        Block.registerBlock(144, "skull", (new BlockSkull()).setHardness(1.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("skull"));
        Block.registerBlock(145, "anvil", (new BlockAnvil()).setHardness(5.0F).setStepSound(Block.soundTypeAnvil).setResistance(2000.0F).setUnlocalizedName("anvil"));
        Block.registerBlock(146, "trapped_chest", (new BlockChest(1)).setHardness(2.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("chestTrap"));
        Block.registerBlock(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted("gold_block", Material.iron, 15)).setHardness(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("weightedPlate_light"));
        Block.registerBlock(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted("iron_block", Material.iron, 150)).setHardness(0.5F).setStepSound(Block.soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
        Block.registerBlock(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).setHardness(0.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(150, "powered_comparator", (new BlockRedstoneComparator(true)).setHardness(0.0F).setLightLevel(0.625F).setStepSound(Block.soundTypeWood).setUnlocalizedName("comparator").disableStats());
        Block.registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
        Block.registerBlock(152, "redstone_block", (new BlockCompressedPowered(MapColor.tntColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockRedstone"));
        Block.registerBlock(153, "quartz_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherquartz"));
        Block.registerBlock(154, "hopper", (new BlockHopper()).setHardness(3.0F).setResistance(8.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("hopper"));
        Block var11 = (new BlockQuartz()).setStepSound(Block.soundTypePiston).setHardness(0.8F).setUnlocalizedName("quartzBlock");
        Block.registerBlock(155, "quartz_block", var11);
        Block.registerBlock(156, "quartz_stairs", (new BlockStairs(var11.getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.DEFAULT))).setUnlocalizedName("stairsQuartz"));
        Block.registerBlock(157, "activator_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("activatorRail"));
        Block.registerBlock(158, "dropper", (new BlockDropper()).setHardness(3.5F).setStepSound(Block.soundTypePiston).setUnlocalizedName("dropper"));
        Block.registerBlock(159, "stained_hardened_clay", (new BlockColored(Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("clayHardenedStained"));
        Block.registerBlock(160, "stained_glass_pane", (new BlockStainedGlassPane()).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
        Block.registerBlock(161, "leaves2", (new BlockNewLeaf()).setUnlocalizedName("leaves"));
        Block.registerBlock(162, "log2", (new BlockNewLog()).setUnlocalizedName("log"));
        Block.registerBlock(163, "acacia_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.ACACIA))).setUnlocalizedName("stairsWoodAcacia"));
        Block.registerBlock(164, "dark_oak_stairs", (new BlockStairs(var1.getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.DARK_OAK))).setUnlocalizedName("stairsWoodDarkOak"));
        Block.registerBlock(165, "slime", (new BlockSlime()).setUnlocalizedName("slime").setStepSound(Block.SLIME_SOUND));
        Block.registerBlock(166, "barrier", (new BlockBarrier()).setUnlocalizedName("barrier"));
        Block.registerBlock(167, "iron_trapdoor", (new BlockTrapDoor(Material.iron)).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
        Block.registerBlock(168, "prismarine", (new BlockPrismarine()).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("prismarine"));
        Block.registerBlock(169, "sea_lantern", (new BlockSeaLantern(Material.glass)).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setLightLevel(1.0F).setUnlocalizedName("seaLantern"));
        Block.registerBlock(170, "hay_block", (new BlockHay()).setHardness(0.5F).setStepSound(Block.soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(171, "carpet", (new BlockCarpet()).setHardness(0.1F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        Block.registerBlock(172, "hardened_clay", (new BlockHardenedClay()).setHardness(1.25F).setResistance(7.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("clayHardened"));
        Block.registerBlock(173, "coal_block", (new Block(Material.rock)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
        Block.registerBlock(174, "packed_ice", (new BlockPackedIce()).setHardness(0.5F).setStepSound(Block.soundTypeGlass).setUnlocalizedName("icePacked"));
        Block.registerBlock(175, "double_plant", new BlockDoublePlant());
        Block.registerBlock(176, "standing_banner", (new BlockBanner.BlockBannerStanding()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(177, "wall_banner", (new BlockBanner.BlockBannerHanging()).setHardness(1.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("banner").disableStats());
        Block.registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        Block var12 = (new BlockRedSandstone()).setStepSound(Block.soundTypePiston).setHardness(0.8F).setUnlocalizedName("redSandStone");
        Block.registerBlock(179, "red_sandstone", var12);
        Block.registerBlock(180, "red_sandstone_stairs", (new BlockStairs(var12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH))).setUnlocalizedName("stairsRedSandStone"));
        Block.registerBlock(181, "double_stone_slab2", (new BlockDoubleStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(182, "stone_slab2", (new BlockHalfStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab2"));
        Block.registerBlock(183, "spruce_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("spruceFenceGate"));
        Block.registerBlock(184, "birch_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("birchFenceGate"));
        Block.registerBlock(185, "jungle_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("jungleFenceGate"));
        Block.registerBlock(186, "dark_oak_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
        Block.registerBlock(187, "acacia_fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
        Block.registerBlock(188, "spruce_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("spruceFence"));
        Block.registerBlock(189, "birch_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("birchFence"));
        Block.registerBlock(190, "jungle_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("jungleFence"));
        Block.registerBlock(191, "dark_oak_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("darkOakFence"));
        Block.registerBlock(192, "acacia_fence", (new BlockFence(Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("acaciaFence"));
        Block.registerBlock(193, "spruce_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
        Block.registerBlock(194, "birch_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
        Block.registerBlock(195, "jungle_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
        Block.registerBlock(196, "acacia_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
        Block.registerBlock(197, "dark_oak_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
        Block.blockRegistry.validateKey();
        Iterator var13 = Block.blockRegistry.iterator();
        Block var14;

        while (var13.hasNext()) {
            var14 = (Block) var13.next();

            if (var14.blockMaterial == Material.air) {
                var14.useNeighborBrightness = false;
            } else {
                boolean var15 = false;
                boolean var16 = var14 instanceof BlockStairs;
                boolean var17 = var14 instanceof BlockSlab;
                boolean var18 = var14 == var6;
                boolean var19 = var14.translucent;
                boolean var20 = var14.lightOpacity == 0;

                if (var16 || var17 || var18 || var19 || var20) {
                    var15 = true;
                }

                var14.useNeighborBrightness = var15;
            }
        }

        var13 = Block.blockRegistry.iterator();

        while (var13.hasNext()) {
            var14 = (Block) var13.next();
            Iterator var21 = var14.getBlockState().getValidStates().iterator();

            while (var21.hasNext()) {
                IBlockState var22 = (IBlockState) var21.next();
                int var23 = Block.blockRegistry.getIDForObject(var14) << 4 | var14.getMetaFromState(var22);
                Block.BLOCK_STATE_IDS.put(var22, var23);
            }
        }
    }

    private static void registerBlock(int id, ResourceLocation textualID, Block block_) {
        Block.blockRegistry.register(id, textualID, block_);
    }

    private static void registerBlock(int id, String textualID, Block block_) {
        Block.registerBlock(id, new ResourceLocation(textualID), block_);
    }

    public static enum EnumOffsetType {
        NONE("NONE", 0), XZ("XZ", 1), XYZ("XYZ", 2);

        private static final Block.EnumOffsetType[] $VALUES = new Block.EnumOffsetType[]{NONE, XZ, XYZ};
        private static final String __OBFID = "CL_00002132";

        private EnumOffsetType(String p_i45733_1_, int p_i45733_2_) {
        }
    }

    public static class SoundType {
        public final String soundName;
        public final float volume;
        public final float frequency;
        private static final String __OBFID = "CL_00000203";

        public SoundType(String name, float volume, float frequency) {
            soundName = name;
            this.volume = volume;
            this.frequency = frequency;
        }

        public float getVolume() {
            return volume;
        }

        public float getFrequency() {
            return frequency;
        }

        public String getBreakSound() {
            return "dig." + soundName;
        }

        public String getStepSound() {
            return "step." + soundName;
        }

        public String getPlaceSound() {
            return getBreakSound();
        }
    }
}
