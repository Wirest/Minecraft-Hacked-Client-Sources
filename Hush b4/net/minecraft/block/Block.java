// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.StatCollector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;

public class Block
{
    private static final ResourceLocation AIR_ID;
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blockRegistry;
    public static final ObjectIntIdentityMap BLOCK_STATE_IDS;
    private CreativeTabs displayOnCreativeTab;
    public static final SoundType soundTypeStone;
    public static final SoundType soundTypeWood;
    public static final SoundType soundTypeGravel;
    public static final SoundType soundTypeGrass;
    public static final SoundType soundTypePiston;
    public static final SoundType soundTypeMetal;
    public static final SoundType soundTypeGlass;
    public static final SoundType soundTypeCloth;
    public static final SoundType soundTypeSand;
    public static final SoundType soundTypeSnow;
    public static final SoundType soundTypeLadder;
    public static final SoundType soundTypeAnvil;
    public static final SoundType SLIME_SOUND;
    protected boolean fullBlock;
    protected int lightOpacity;
    protected boolean translucent;
    protected int lightValue;
    protected boolean useNeighborBrightness;
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats;
    protected boolean needsRandomTick;
    protected boolean isBlockContainer;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public SoundType stepSound;
    public float blockParticleGravity;
    protected final Material blockMaterial;
    protected final MapColor field_181083_K;
    public float slipperiness;
    protected final BlockState blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;
    
    static {
        AIR_ID = new ResourceLocation("air");
        blockRegistry = new RegistryNamespacedDefaultedByKey<ResourceLocation, Block>(Block.AIR_ID);
        BLOCK_STATE_IDS = new ObjectIntIdentityMap();
        soundTypeStone = new SoundType("stone", 1.0f, 1.0f);
        soundTypeWood = new SoundType("wood", 1.0f, 1.0f);
        soundTypeGravel = new SoundType("gravel", 1.0f, 1.0f);
        soundTypeGrass = new SoundType("grass", 1.0f, 1.0f);
        soundTypePiston = new SoundType("stone", 1.0f, 1.0f);
        soundTypeMetal = new SoundType("stone", 1.0f, 1.5f);
        soundTypeGlass = new SoundType(1.0f, 1.0f) {
            @Override
            public String getBreakSound() {
                return "dig.glass";
            }
            
            @Override
            public String getPlaceSound() {
                return "step.stone";
            }
        };
        soundTypeCloth = new SoundType("cloth", 1.0f, 1.0f);
        soundTypeSand = new SoundType("sand", 1.0f, 1.0f);
        soundTypeSnow = new SoundType("snow", 1.0f, 1.0f);
        soundTypeLadder = new SoundType(1.0f, 1.0f) {
            @Override
            public String getBreakSound() {
                return "dig.wood";
            }
        };
        soundTypeAnvil = new SoundType(0.3f, 1.0f) {
            @Override
            public String getBreakSound() {
                return "dig.stone";
            }
            
            @Override
            public String getPlaceSound() {
                return "random.anvil_land";
            }
        };
        SLIME_SOUND = new SoundType(1.0f, 1.0f) {
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
    }
    
    public static int getIdFromBlock(final Block blockIn) {
        return Block.blockRegistry.getIDForObject(blockIn);
    }
    
    public static int getStateId(final IBlockState state) {
        final Block block = state.getBlock();
        return getIdFromBlock(block) + (block.getMetaFromState(state) << 12);
    }
    
    public static Block getBlockById(final int id) {
        return Block.blockRegistry.getObjectById(id);
    }
    
    public static IBlockState getStateById(final int id) {
        final int i = id & 0xFFF;
        final int j = id >> 12 & 0xF;
        return getBlockById(i).getStateFromMeta(j);
    }
    
    public static Block getBlockFromItem(final Item itemIn) {
        return (itemIn instanceof ItemBlock) ? ((ItemBlock)itemIn).getBlock() : null;
    }
    
    public static Block getBlockFromName(final String name) {
        final ResourceLocation resourcelocation = new ResourceLocation(name);
        if (Block.blockRegistry.containsKey(resourcelocation)) {
            return Block.blockRegistry.getObject(resourcelocation);
        }
        try {
            return Block.blockRegistry.getObjectById(Integer.parseInt(name));
        }
        catch (NumberFormatException var3) {
            return null;
        }
    }
    
    public boolean isFullBlock() {
        return this.fullBlock;
    }
    
    public int getLightOpacity() {
        return this.lightOpacity;
    }
    
    public boolean isTranslucent() {
        return this.translucent;
    }
    
    public int getLightValue() {
        return this.lightValue;
    }
    
    public boolean getUseNeighborBrightness() {
        return this.useNeighborBrightness;
    }
    
    public Material getMaterial() {
        return this.blockMaterial;
    }
    
    public MapColor getMapColor(final IBlockState state) {
        return this.field_181083_K;
    }
    
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState();
    }
    
    public int getMetaFromState(final IBlockState state) {
        if (state != null && !state.getPropertyNames().isEmpty()) {
            throw new IllegalArgumentException("Don't know how to convert " + state + " back into data...");
        }
        return 0;
    }
    
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state;
    }
    
    public Block(final Material p_i46399_1_, final MapColor p_i46399_2_) {
        this.enableStats = true;
        this.stepSound = Block.soundTypeStone;
        this.blockParticleGravity = 1.0f;
        this.slipperiness = 0.6f;
        this.blockMaterial = p_i46399_1_;
        this.field_181083_K = p_i46399_2_;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.fullBlock = this.isOpaqueCube();
        this.lightOpacity = (this.isOpaqueCube() ? 255 : 0);
        this.translucent = !p_i46399_1_.blocksLight();
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
    }
    
    protected Block(final Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    protected Block setStepSound(final SoundType sound) {
        this.stepSound = sound;
        return this;
    }
    
    protected Block setLightOpacity(final int opacity) {
        this.lightOpacity = opacity;
        return this;
    }
    
    protected Block setLightLevel(final float value) {
        this.lightValue = (int)(15.0f * value);
        return this;
    }
    
    protected Block setResistance(final float resistance) {
        this.blockResistance = resistance * 3.0f;
        return this;
    }
    
    public boolean isBlockNormalCube() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }
    
    public boolean isNormalCube() {
        return this.blockMaterial.isOpaque() && this.isFullCube() && !this.canProvidePower();
    }
    
    public boolean isVisuallyOpaque() {
        return this.blockMaterial.blocksMovement() && this.isFullCube();
    }
    
    public boolean isFullCube() {
        return true;
    }
    
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return !this.blockMaterial.blocksMovement();
    }
    
    public int getRenderType() {
        return 3;
    }
    
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    protected Block setHardness(final float hardness) {
        this.blockHardness = hardness;
        if (this.blockResistance < hardness * 5.0f) {
            this.blockResistance = hardness * 5.0f;
        }
        return this;
    }
    
    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0f);
        return this;
    }
    
    public float getBlockHardness(final World worldIn, final BlockPos pos) {
        return this.blockHardness;
    }
    
    protected Block setTickRandomly(final boolean shouldTick) {
        this.needsRandomTick = shouldTick;
        return this;
    }
    
    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }
    
    public boolean hasTileEntity() {
        return this.isBlockContainer;
    }
    
    protected final void setBlockBounds(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public int getMixedBrightnessForBlock(final IBlockAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        final int i = worldIn.getCombinedLight(pos, block.getLightValue());
        if (i == 0 && block instanceof BlockSlab) {
            pos = pos.down();
            block = worldIn.getBlockState(pos).getBlock();
            return worldIn.getCombinedLight(pos, block.getLightValue());
        }
        return i;
    }
    
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return (side == EnumFacing.DOWN && this.minY > 0.0) || (side == EnumFacing.UP && this.maxY < 1.0) || (side == EnumFacing.NORTH && this.minZ > 0.0) || (side == EnumFacing.SOUTH && this.maxZ < 1.0) || (side == EnumFacing.WEST && this.minX > 0.0) || (side == EnumFacing.EAST && this.maxX < 1.0) || !worldIn.getBlockState(pos).getBlock().isOpaqueCube();
    }
    
    public boolean isBlockSolid(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial().isSolid();
    }
    
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }
    
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        final AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
        if (axisalignedbb != null && mask.intersectsWith(axisalignedbb)) {
            list.add(axisalignedbb);
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }
    
    public boolean isOpaqueCube() {
        return true;
    }
    
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return this.isCollidable();
    }
    
    public boolean isCollidable() {
        return true;
    }
    
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
        this.updateTick(worldIn, pos, state, random);
    }
    
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
    }
    
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
    }
    
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
    }
    
    public int tickRate(final World worldIn) {
        return 10;
    }
    
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
    }
    
    public int quantityDropped(final Random random) {
        return 1;
    }
    
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this);
    }
    
    public float getPlayerRelativeBlockHardness(final EntityPlayer playerIn, final World worldIn, final BlockPos pos) {
        final float f = this.getBlockHardness(worldIn, pos);
        return (f < 0.0f) ? 0.0f : (playerIn.canHarvestBlock(this) ? (playerIn.getToolDigEfficiency(this) / f / 30.0f) : (playerIn.getToolDigEfficiency(this) / f / 100.0f));
    }
    
    public final void dropBlockAsItem(final World worldIn, final BlockPos pos, final IBlockState state, final int forture) {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0f, forture);
    }
    
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            for (int i = this.quantityDroppedWithBonus(fortune, worldIn.rand), j = 0; j < i; ++j) {
                if (worldIn.rand.nextFloat() <= chance) {
                    final Item item = this.getItemDropped(state, worldIn.rand, fortune);
                    if (item != null) {
                        spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
                    }
                }
            }
        }
    }
    
    public static void spawnAsEntity(final World worldIn, final BlockPos pos, final ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
            final float f = 0.5f;
            final double d0 = worldIn.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final double d2 = worldIn.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final double d3 = worldIn.rand.nextFloat() * f + (1.0f - f) * 0.5;
            final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d2, pos.getZ() + d3, stack);
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
    
    protected void dropXpOnBlockBreak(final World worldIn, final BlockPos pos, int amount) {
        if (!worldIn.isRemote) {
            while (amount > 0) {
                final int i = EntityXPOrb.getXPSplit(amount);
                amount -= i;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i));
            }
        }
    }
    
    public int damageDropped(final IBlockState state) {
        return 0;
    }
    
    public float getExplosionResistance(final Entity exploder) {
        return this.blockResistance / 5.0f;
    }
    
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        start = start.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
        end = end.addVector(-pos.getX(), -pos.getY(), -pos.getZ());
        Vec3 vec3 = start.getIntermediateWithXValue(end, this.minX);
        Vec3 vec4 = start.getIntermediateWithXValue(end, this.maxX);
        Vec3 vec5 = start.getIntermediateWithYValue(end, this.minY);
        Vec3 vec6 = start.getIntermediateWithYValue(end, this.maxY);
        Vec3 vec7 = start.getIntermediateWithZValue(end, this.minZ);
        Vec3 vec8 = start.getIntermediateWithZValue(end, this.maxZ);
        if (!this.isVecInsideYZBounds(vec3)) {
            vec3 = null;
        }
        if (!this.isVecInsideYZBounds(vec4)) {
            vec4 = null;
        }
        if (!this.isVecInsideXZBounds(vec5)) {
            vec5 = null;
        }
        if (!this.isVecInsideXZBounds(vec6)) {
            vec6 = null;
        }
        if (!this.isVecInsideXYBounds(vec7)) {
            vec7 = null;
        }
        if (!this.isVecInsideXYBounds(vec8)) {
            vec8 = null;
        }
        Vec3 vec9 = null;
        if (vec3 != null && (vec9 == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec9))) {
            vec9 = vec3;
        }
        if (vec4 != null && (vec9 == null || start.squareDistanceTo(vec4) < start.squareDistanceTo(vec9))) {
            vec9 = vec4;
        }
        if (vec5 != null && (vec9 == null || start.squareDistanceTo(vec5) < start.squareDistanceTo(vec9))) {
            vec9 = vec5;
        }
        if (vec6 != null && (vec9 == null || start.squareDistanceTo(vec6) < start.squareDistanceTo(vec9))) {
            vec9 = vec6;
        }
        if (vec7 != null && (vec9 == null || start.squareDistanceTo(vec7) < start.squareDistanceTo(vec9))) {
            vec9 = vec7;
        }
        if (vec8 != null && (vec9 == null || start.squareDistanceTo(vec8) < start.squareDistanceTo(vec9))) {
            vec9 = vec8;
        }
        if (vec9 == null) {
            return null;
        }
        EnumFacing enumfacing = null;
        if (vec9 == vec3) {
            enumfacing = EnumFacing.WEST;
        }
        if (vec9 == vec4) {
            enumfacing = EnumFacing.EAST;
        }
        if (vec9 == vec5) {
            enumfacing = EnumFacing.DOWN;
        }
        if (vec9 == vec6) {
            enumfacing = EnumFacing.UP;
        }
        if (vec9 == vec7) {
            enumfacing = EnumFacing.NORTH;
        }
        if (vec9 == vec8) {
            enumfacing = EnumFacing.SOUTH;
        }
        return new MovingObjectPosition(vec9.addVector(pos.getX(), pos.getY(), pos.getZ()), enumfacing, pos);
    }
    
    private boolean isVecInsideYZBounds(final Vec3 point) {
        return point != null && (point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ);
    }
    
    private boolean isVecInsideXZBounds(final Vec3 point) {
        return point != null && (point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ);
    }
    
    private boolean isVecInsideXYBounds(final Vec3 point) {
        return point != null && (point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY);
    }
    
    public void onBlockDestroyedByExplosion(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
    }
    
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.SOLID;
    }
    
    public boolean canReplace(final World worldIn, final BlockPos pos, final EnumFacing side, final ItemStack stack) {
        return this.canPlaceBlockOnSide(worldIn, pos, side);
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return this.canPlaceBlockAt(worldIn, pos);
    }
    
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable();
    }
    
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return false;
    }
    
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final Entity entityIn) {
    }
    
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }
    
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
    }
    
    public Vec3 modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3 motion) {
        return motion;
    }
    
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
    }
    
    public final double getBlockBoundsMinX() {
        return this.minX;
    }
    
    public final double getBlockBoundsMaxX() {
        return this.maxX;
    }
    
    public final double getBlockBoundsMinY() {
        return this.minY;
    }
    
    public final double getBlockBoundsMaxY() {
        return this.maxY;
    }
    
    public final double getBlockBoundsMinZ() {
        return this.minZ;
    }
    
    public final double getBlockBoundsMaxZ() {
        return this.maxZ;
    }
    
    public int getBlockColor() {
        return 16777215;
    }
    
    public int getRenderColor(final IBlockState state) {
        return 16777215;
    }
    
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return 16777215;
    }
    
    public final int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos) {
        return this.colorMultiplier(worldIn, pos, 0);
    }
    
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return 0;
    }
    
    public boolean canProvidePower() {
        return false;
    }
    
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
    }
    
    public int getStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return 0;
    }
    
    public void setBlockBoundsForItemRender() {
    }
    
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        player.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
        player.addExhaustion(0.025f);
        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            final ItemStack itemstack = this.createStackedBlock(state);
            if (itemstack != null) {
                spawnAsEntity(worldIn, pos, itemstack);
            }
        }
        else {
            final int i = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
        }
    }
    
    protected boolean canSilkHarvest() {
        return this.isFullCube() && !this.isBlockContainer;
    }
    
    protected ItemStack createStackedBlock(final IBlockState state) {
        int i = 0;
        final Item item = Item.getItemFromBlock(this);
        if (item != null && item.getHasSubtypes()) {
            i = this.getMetaFromState(state);
        }
        return new ItemStack(item, 1, i);
    }
    
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return this.quantityDropped(random);
    }
    
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    }
    
    public boolean func_181623_g() {
        return !this.blockMaterial.isSolid() && !this.blockMaterial.isLiquid();
    }
    
    public Block setUnlocalizedName(final String name) {
        this.unlocalizedName = name;
        return this;
    }
    
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name");
    }
    
    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedName;
    }
    
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        return false;
    }
    
    public boolean getEnableStats() {
        return this.enableStats;
    }
    
    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }
    
    public int getMobilityFlag() {
        return this.blockMaterial.getMaterialMobility();
    }
    
    public float getAmbientOcclusionLightValue() {
        return this.isBlockNormalCube() ? 0.2f : 1.0f;
    }
    
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        entityIn.fall(fallDistance, 1.0f);
    }
    
    public void onLanded(final World worldIn, final Entity entityIn) {
        entityIn.motionY = 0.0;
    }
    
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(this);
    }
    
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return this.damageDropped(worldIn.getBlockState(pos));
    }
    
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }
    
    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }
    
    public Block setCreativeTab(final CreativeTabs tab) {
        this.displayOnCreativeTab = tab;
        return this;
    }
    
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
    }
    
    public void fillWithRain(final World worldIn, final BlockPos pos) {
    }
    
    public boolean isFlowerPot() {
        return false;
    }
    
    public boolean requiresUpdates() {
        return true;
    }
    
    public boolean canDropFromExplosion(final Explosion explosionIn) {
        return true;
    }
    
    public boolean isAssociatedBlock(final Block other) {
        return this == other;
    }
    
    public static boolean isEqualTo(final Block blockIn, final Block other) {
        return blockIn != null && other != null && (blockIn == other || blockIn.isAssociatedBlock(other));
    }
    
    public boolean hasComparatorInputOverride() {
        return false;
    }
    
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return 0;
    }
    
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return state;
    }
    
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[0]);
    }
    
    public BlockState getBlockState() {
        return this.blockState;
    }
    
    protected final void setDefaultState(final IBlockState state) {
        this.defaultBlockState = state;
    }
    
    public final IBlockState getDefaultState() {
        return this.defaultBlockState;
    }
    
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }
    
    @Override
    public String toString() {
        return "Block{" + Block.blockRegistry.getNameForObject(this) + "}";
    }
    
    public static void registerBlocks() {
        registerBlock(0, Block.AIR_ID, new BlockAir().setUnlocalizedName("air"));
        registerBlock(1, "stone", new BlockStone().setHardness(1.5f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stone"));
        registerBlock(2, "grass", new BlockGrass().setHardness(0.6f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("grass"));
        registerBlock(3, "dirt", new BlockDirt().setHardness(0.5f).setStepSound(Block.soundTypeGravel).setUnlocalizedName("dirt"));
        final Block block = new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(4, "cobblestone", block);
        final Block block2 = new BlockPlanks().setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("wood");
        registerBlock(5, "planks", block2);
        registerBlock(6, "sapling", new BlockSapling().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("sapling"));
        registerBlock(7, "bedrock", new Block(Material.rock).setBlockUnbreakable().setResistance(6000000.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.water).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(9, "water", new BlockStaticLiquid(Material.water).setHardness(100.0f).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.lava).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        registerBlock(11, "lava", new BlockStaticLiquid(Material.lava).setHardness(100.0f).setLightLevel(1.0f).setUnlocalizedName("lava").disableStats());
        registerBlock(12, "sand", new BlockSand().setHardness(0.5f).setStepSound(Block.soundTypeSand).setUnlocalizedName("sand"));
        registerBlock(13, "gravel", new BlockGravel().setHardness(0.6f).setStepSound(Block.soundTypeGravel).setUnlocalizedName("gravel"));
        registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreGold"));
        registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreIron"));
        registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreCoal"));
        registerBlock(17, "log", new BlockOldLog().setUnlocalizedName("log"));
        registerBlock(18, "leaves", new BlockOldLeaf().setUnlocalizedName("leaves"));
        registerBlock(19, "sponge", new BlockSponge().setHardness(0.6f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("sponge"));
        registerBlock(20, "glass", new BlockGlass(Material.glass, false).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("glass"));
        registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreLapis"));
        registerBlock(22, "lapis_block", new Block(Material.iron, MapColor.lapisColor).setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5f).setStepSound(Block.soundTypePiston).setUnlocalizedName("dispenser"));
        final Block block3 = new BlockSandStone().setStepSound(Block.soundTypePiston).setHardness(0.8f).setUnlocalizedName("sandStone");
        registerBlock(24, "sandstone", block3);
        registerBlock(25, "noteblock", new BlockNote().setHardness(0.8f).setUnlocalizedName("musicBlock"));
        registerBlock(26, "bed", new BlockBed().setStepSound(Block.soundTypeWood).setHardness(0.2f).setUnlocalizedName("bed").disableStats());
        registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("goldenRail"));
        registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("detectorRail"));
        registerBlock(29, "sticky_piston", new BlockPistonBase(true).setUnlocalizedName("pistonStickyBase"));
        registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0f).setUnlocalizedName("web"));
        registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("tallgrass"));
        registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("deadbush"));
        registerBlock(33, "piston", new BlockPistonBase(false).setUnlocalizedName("pistonBase"));
        registerBlock(34, "piston_head", new BlockPistonExtension().setUnlocalizedName("pistonBase"));
        registerBlock(35, "wool", new BlockColored(Material.cloth).setHardness(0.8f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cloth"));
        registerBlock(36, "piston_extension", new BlockPistonMoving());
        registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("flower1"));
        registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("flower2"));
        final Block block4 = new BlockMushroom().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setLightLevel(0.125f).setUnlocalizedName("mushroom");
        registerBlock(39, "brown_mushroom", block4);
        final Block block5 = new BlockMushroom().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("mushroom");
        registerBlock(40, "red_mushroom", block5);
        registerBlock(41, "gold_block", new Block(Material.iron, MapColor.goldColor).setHardness(3.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(42, "iron_block", new Block(Material.iron, MapColor.ironColor).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(43, "double_stone_slab", new BlockDoubleStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab"));
        registerBlock(44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab"));
        final Block block6 = new Block(Material.rock, MapColor.redColor).setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(45, "brick_block", block6);
        registerBlock(46, "tnt", new BlockTNT().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("tnt"));
        registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("bookshelf"));
        registerBlock(48, "mossy_cobblestone", new Block(Material.rock).setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0f).setResistance(2000.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("obsidian"));
        registerBlock(50, "torch", new BlockTorch().setHardness(0.0f).setLightLevel(0.9375f).setStepSound(Block.soundTypeWood).setUnlocalizedName("torch"));
        registerBlock(51, "fire", new BlockFire().setHardness(0.0f).setLightLevel(1.0f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("fire").disableStats());
        registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("mobSpawner").disableStats());
        registerBlock(53, "oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setUnlocalizedName("stairsWood"));
        registerBlock(54, "chest", new BlockChest(0).setHardness(2.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("chest"));
        registerBlock(55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0f).setStepSound(Block.soundTypeStone).setUnlocalizedName("redstoneDust").disableStats());
        registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreDiamond"));
        registerBlock(57, "diamond_block", new Block(Material.iron, MapColor.diamondColor).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("workbench"));
        registerBlock(59, "wheat", new BlockCrops().setUnlocalizedName("crops"));
        final Block block7 = new BlockFarmland().setHardness(0.6f).setStepSound(Block.soundTypeGravel).setUnlocalizedName("farmland");
        registerBlock(60, "farmland", block7);
        registerBlock(61, "furnace", new BlockFurnace(false).setHardness(3.5f).setStepSound(Block.soundTypePiston).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        registerBlock(62, "lit_furnace", new BlockFurnace(true).setHardness(3.5f).setStepSound(Block.soundTypePiston).setLightLevel(0.875f).setUnlocalizedName("furnace"));
        registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("sign").disableStats());
        registerBlock(64, "wooden_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorOak").disableStats());
        registerBlock(65, "ladder", new BlockLadder().setHardness(0.4f).setStepSound(Block.soundTypeLadder).setUnlocalizedName("ladder"));
        registerBlock(66, "rail", new BlockRail().setHardness(0.7f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("rail"));
        registerBlock(67, "stone_stairs", new BlockStairs(block.getDefaultState()).setUnlocalizedName("stairsStone"));
        registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("sign").disableStats());
        registerBlock(69, "lever", new BlockLever().setHardness(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("lever"));
        registerBlock(70, "stone_pressure_plate", new BlockPressurePlate(Material.rock, BlockPressurePlate.Sensitivity.MOBS).setHardness(0.5f).setStepSound(Block.soundTypePiston).setUnlocalizedName("pressurePlateStone"));
        registerBlock(71, "iron_door", new BlockDoor(Material.iron).setHardness(5.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("doorIron").disableStats());
        registerBlock(72, "wooden_pressure_plate", new BlockPressurePlate(Material.wood, BlockPressurePlate.Sensitivity.EVERYTHING).setHardness(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("pressurePlateWood"));
        registerBlock(73, "redstone_ore", new BlockRedstoneOre(false).setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(74, "lit_redstone_ore", new BlockRedstoneOre(true).setLightLevel(0.625f).setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRedstone"));
        registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("notGate"));
        registerBlock(76, "redstone_torch", new BlockRedstoneTorch(true).setHardness(0.0f).setLightLevel(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.tabRedstone));
        registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5f).setStepSound(Block.soundTypePiston).setUnlocalizedName("button"));
        registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1f).setStepSound(Block.soundTypeSnow).setUnlocalizedName("snow").setLightOpacity(0));
        registerBlock(79, "ice", new BlockIce().setHardness(0.5f).setLightOpacity(3).setStepSound(Block.soundTypeGlass).setUnlocalizedName("ice"));
        registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2f).setStepSound(Block.soundTypeSnow).setUnlocalizedName("snow"));
        registerBlock(81, "cactus", new BlockCactus().setHardness(0.4f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cactus"));
        registerBlock(82, "clay", new BlockClay().setHardness(0.6f).setStepSound(Block.soundTypeGravel).setUnlocalizedName("clay"));
        registerBlock(83, "reeds", new BlockReed().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("reeds").disableStats());
        registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("jukebox"));
        registerBlock(85, "fence", new BlockFence(Material.wood, BlockPlanks.EnumType.OAK.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("fence"));
        final Block block8 = new BlockPumpkin().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkin");
        registerBlock(86, "pumpkin", block8);
        registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4f).setStepSound(Block.soundTypePiston).setUnlocalizedName("hellrock"));
        registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5f).setStepSound(Block.soundTypeSand).setUnlocalizedName("hellsand"));
        registerBlock(89, "glowstone", new BlockGlowstone(Material.glass).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setLightLevel(1.0f).setUnlocalizedName("lightgem"));
        registerBlock(90, "portal", new BlockPortal().setHardness(-1.0f).setStepSound(Block.soundTypeGlass).setLightLevel(0.75f).setUnlocalizedName("portal"));
        registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0f).setStepSound(Block.soundTypeWood).setLightLevel(1.0f).setUnlocalizedName("litpumpkin"));
        registerBlock(92, "cake", new BlockCake().setHardness(0.5f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("cake").disableStats());
        registerBlock(93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("diode").disableStats());
        registerBlock(94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("diode").disableStats());
        registerBlock(95, "stained_glass", new BlockStainedGlass(Material.glass).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("stainedGlass"));
        registerBlock(96, "trapdoor", new BlockTrapDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("trapdoor").disableStats());
        registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75f).setUnlocalizedName("monsterStoneEgg"));
        final Block block9 = new BlockStoneBrick().setHardness(1.5f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stonebricksmooth");
        registerBlock(98, "stonebrick", block9);
        registerBlock(99, "brown_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.dirtColor, block4).setHardness(0.2f).setStepSound(Block.soundTypeWood).setUnlocalizedName("mushroom"));
        registerBlock(100, "red_mushroom_block", new BlockHugeMushroom(Material.wood, MapColor.redColor, block5).setHardness(0.2f).setStepSound(Block.soundTypeWood).setUnlocalizedName("mushroom"));
        registerBlock(101, "iron_bars", new BlockPane(Material.iron, true).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("fenceIron"));
        registerBlock(102, "glass_pane", new BlockPane(Material.glass, false).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("thinGlass"));
        final Block block10 = new BlockMelon().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("melon");
        registerBlock(103, "melon_block", block10);
        registerBlock(104, "pumpkin_stem", new BlockStem(block8).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkinStem"));
        registerBlock(105, "melon_stem", new BlockStem(block10).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("pumpkinStem"));
        registerBlock(106, "vine", new BlockVine().setHardness(0.2f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("vine"));
        registerBlock(107, "fence_gate", new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("fenceGate"));
        registerBlock(108, "brick_stairs", new BlockStairs(block6.getDefaultState()).setUnlocalizedName("stairsBrick"));
        registerBlock(109, "stone_brick_stairs", new BlockStairs(block9.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)).setUnlocalizedName("stairsStoneBrickSmooth"));
        registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("mycel"));
        registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("waterlily"));
        final Block block11 = new BlockNetherBrick().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.tabBlock);
        registerBlock(112, "nether_brick", block11);
        registerBlock(113, "nether_brick_fence", new BlockFence(Material.rock, MapColor.netherrackColor).setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherFence"));
        registerBlock(114, "nether_brick_stairs", new BlockStairs(block11.getDefaultState()).setUnlocalizedName("stairsNetherBrick"));
        registerBlock(115, "nether_wart", new BlockNetherWart().setUnlocalizedName("netherStalk"));
        registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0f).setResistance(2000.0f).setUnlocalizedName("enchantmentTable"));
        registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5f).setLightLevel(0.125f).setUnlocalizedName("brewingStand"));
        registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0f).setUnlocalizedName("cauldron"));
        registerBlock(119, "end_portal", new BlockEndPortal(Material.portal).setHardness(-1.0f).setResistance(6000000.0f));
        registerBlock(120, "end_portal_frame", new BlockEndPortalFrame().setStepSound(Block.soundTypeGlass).setLightLevel(0.125f).setHardness(-1.0f).setUnlocalizedName("endPortalFrame").setResistance(6000000.0f).setCreativeTab(CreativeTabs.tabDecorations));
        registerBlock(121, "end_stone", new Block(Material.rock, MapColor.sandColor).setHardness(3.0f).setResistance(15.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(122, "dragon_egg", new BlockDragonEgg().setHardness(3.0f).setResistance(15.0f).setStepSound(Block.soundTypePiston).setLightLevel(0.125f).setUnlocalizedName("dragonEgg"));
        registerBlock(123, "redstone_lamp", new BlockRedstoneLight(false).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone));
        registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("redstoneLight"));
        registerBlock(125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("woodSlab"));
        registerBlock(126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("woodSlab"));
        registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("cocoa"));
        registerBlock(128, "sandstone_stairs", new BlockStairs(block3.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setUnlocalizedName("stairsSandStone"));
        registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreEmerald"));
        registerBlock(130, "ender_chest", new BlockEnderChest().setHardness(22.5f).setResistance(1000.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("enderChest").setLightLevel(0.5f));
        registerBlock(131, "tripwire_hook", new BlockTripWireHook().setUnlocalizedName("tripWireSource"));
        registerBlock(132, "tripwire", new BlockTripWire().setUnlocalizedName("tripWire"));
        registerBlock(133, "emerald_block", new Block(Material.iron, MapColor.emeraldColor).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(134, "spruce_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setUnlocalizedName("stairsWoodSpruce"));
        registerBlock(135, "birch_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setUnlocalizedName("stairsWoodBirch"));
        registerBlock(136, "jungle_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setUnlocalizedName("stairsWoodJungle"));
        registerBlock(137, "command_block", new BlockCommandBlock().setBlockUnbreakable().setResistance(6000000.0f).setUnlocalizedName("commandBlock"));
        registerBlock(138, "beacon", new BlockBeacon().setUnlocalizedName("beacon").setLightLevel(1.0f));
        registerBlock(139, "cobblestone_wall", new BlockWall(block).setUnlocalizedName("cobbleWall"));
        registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0f).setStepSound(Block.soundTypeStone).setUnlocalizedName("flowerPot"));
        registerBlock(141, "carrots", new BlockCarrot().setUnlocalizedName("carrots"));
        registerBlock(142, "potatoes", new BlockPotato().setUnlocalizedName("potatoes"));
        registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("button"));
        registerBlock(144, "skull", new BlockSkull().setHardness(1.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("skull"));
        registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0f).setStepSound(Block.soundTypeAnvil).setResistance(2000.0f).setUnlocalizedName("anvil"));
        registerBlock(146, "trapped_chest", new BlockChest(1).setHardness(2.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("chestTrap"));
        registerBlock(147, "light_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 15, MapColor.goldColor).setHardness(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("weightedPlate_light"));
        registerBlock(148, "heavy_weighted_pressure_plate", new BlockPressurePlateWeighted(Material.iron, 150).setHardness(0.5f).setStepSound(Block.soundTypeWood).setUnlocalizedName("weightedPlate_heavy"));
        registerBlock(149, "unpowered_comparator", new BlockRedstoneComparator(false).setHardness(0.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("comparator").disableStats());
        registerBlock(150, "powered_comparator", new BlockRedstoneComparator(true).setHardness(0.0f).setLightLevel(0.625f).setStepSound(Block.soundTypeWood).setUnlocalizedName("comparator").disableStats());
        registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
        registerBlock(152, "redstone_block", new BlockCompressedPowered(Material.iron, MapColor.tntColor).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.tabRedstone));
        registerBlock(153, "quartz_ore", new BlockOre(MapColor.netherrackColor).setHardness(3.0f).setResistance(5.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("netherquartz"));
        registerBlock(154, "hopper", new BlockHopper().setHardness(3.0f).setResistance(8.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("hopper"));
        final Block block12 = new BlockQuartz().setStepSound(Block.soundTypePiston).setHardness(0.8f).setUnlocalizedName("quartzBlock");
        registerBlock(155, "quartz_block", block12);
        registerBlock(156, "quartz_stairs", new BlockStairs(block12.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setUnlocalizedName("stairsQuartz"));
        registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("activatorRail"));
        registerBlock(158, "dropper", new BlockDropper().setHardness(3.5f).setStepSound(Block.soundTypePiston).setUnlocalizedName("dropper"));
        registerBlock(159, "stained_hardened_clay", new BlockColored(Material.rock).setHardness(1.25f).setResistance(7.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("clayHardenedStained"));
        registerBlock(160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("thinStainedGlass"));
        registerBlock(161, "leaves2", new BlockNewLeaf().setUnlocalizedName("leaves"));
        registerBlock(162, "log2", new BlockNewLog().setUnlocalizedName("log"));
        registerBlock(163, "acacia_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setUnlocalizedName("stairsWoodAcacia"));
        registerBlock(164, "dark_oak_stairs", new BlockStairs(block2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setUnlocalizedName("stairsWoodDarkOak"));
        registerBlock(165, "slime", new BlockSlime().setUnlocalizedName("slime").setStepSound(Block.SLIME_SOUND));
        registerBlock(166, "barrier", new BlockBarrier().setUnlocalizedName("barrier"));
        registerBlock(167, "iron_trapdoor", new BlockTrapDoor(Material.iron).setHardness(5.0f).setStepSound(Block.soundTypeMetal).setUnlocalizedName("ironTrapdoor").disableStats());
        registerBlock(168, "prismarine", new BlockPrismarine().setHardness(1.5f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("prismarine"));
        registerBlock(169, "sea_lantern", new BlockSeaLantern(Material.glass).setHardness(0.3f).setStepSound(Block.soundTypeGlass).setLightLevel(1.0f).setUnlocalizedName("seaLantern"));
        registerBlock(170, "hay_block", new BlockHay().setHardness(0.5f).setStepSound(Block.soundTypeGrass).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        registerBlock(172, "hardened_clay", new BlockHardenedClay().setHardness(1.25f).setResistance(7.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("clayHardened"));
        registerBlock(173, "coal_block", new Block(Material.rock, MapColor.blackColor).setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.tabBlock));
        registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5f).setStepSound(Block.soundTypeGlass).setUnlocalizedName("icePacked"));
        registerBlock(175, "double_plant", new BlockDoublePlant());
        registerBlock(176, "standing_banner", new BlockBanner.BlockBannerStanding().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("banner").disableStats());
        registerBlock(177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("banner").disableStats());
        registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        final Block block13 = new BlockRedSandstone().setStepSound(Block.soundTypePiston).setHardness(0.8f).setUnlocalizedName("redSandStone");
        registerBlock(179, "red_sandstone", block13);
        registerBlock(180, "red_sandstone_stairs", new BlockStairs(block13.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)).setUnlocalizedName("stairsRedSandStone"));
        registerBlock(181, "double_stone_slab2", new BlockDoubleStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab2"));
        registerBlock(182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0f).setResistance(10.0f).setStepSound(Block.soundTypePiston).setUnlocalizedName("stoneSlab2"));
        registerBlock(183, "spruce_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.SPRUCE).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("spruceFenceGate"));
        registerBlock(184, "birch_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("birchFenceGate"));
        registerBlock(185, "jungle_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.JUNGLE).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("jungleFenceGate"));
        registerBlock(186, "dark_oak_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("darkOakFenceGate"));
        registerBlock(187, "acacia_fence_gate", new BlockFenceGate(BlockPlanks.EnumType.ACACIA).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("acaciaFenceGate"));
        registerBlock(188, "spruce_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.SPRUCE.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("spruceFence"));
        registerBlock(189, "birch_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.BIRCH.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("birchFence"));
        registerBlock(190, "jungle_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.JUNGLE.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("jungleFence"));
        registerBlock(191, "dark_oak_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.DARK_OAK.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("darkOakFence"));
        registerBlock(192, "acacia_fence", new BlockFence(Material.wood, BlockPlanks.EnumType.ACACIA.func_181070_c()).setHardness(2.0f).setResistance(5.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("acaciaFence"));
        registerBlock(193, "spruce_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorSpruce").disableStats());
        registerBlock(194, "birch_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorBirch").disableStats());
        registerBlock(195, "jungle_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorJungle").disableStats());
        registerBlock(196, "acacia_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorAcacia").disableStats());
        registerBlock(197, "dark_oak_door", new BlockDoor(Material.wood).setHardness(3.0f).setStepSound(Block.soundTypeWood).setUnlocalizedName("doorDarkOak").disableStats());
        Block.blockRegistry.validateKey();
        for (final Block block14 : Block.blockRegistry) {
            if (block14.blockMaterial == Material.air) {
                block14.useNeighborBrightness = false;
            }
            else {
                boolean flag = false;
                final boolean flag2 = block14 instanceof BlockStairs;
                final boolean flag3 = block14 instanceof BlockSlab;
                final boolean flag4 = block14 == block7;
                final boolean flag5 = block14.translucent;
                final boolean flag6 = block14.lightOpacity == 0;
                if (flag2 || flag3 || flag4 || flag5 || flag6) {
                    flag = true;
                }
                block14.useNeighborBrightness = flag;
            }
        }
        for (final Block block15 : Block.blockRegistry) {
            for (final IBlockState iblockstate : block15.getBlockState().getValidStates()) {
                final int i = Block.blockRegistry.getIDForObject(block15) << 4 | block15.getMetaFromState(iblockstate);
                Block.BLOCK_STATE_IDS.put(iblockstate, i);
            }
        }
    }
    
    private static void registerBlock(final int id, final ResourceLocation textualID, final Block block_) {
        Block.blockRegistry.register(id, textualID, block_);
    }
    
    private static void registerBlock(final int id, final String textualID, final Block block_) {
        registerBlock(id, new ResourceLocation(textualID), block_);
    }
    
    public enum EnumOffsetType
    {
        NONE("NONE", 0), 
        XZ("XZ", 1), 
        XYZ("XYZ", 2);
        
        private EnumOffsetType(final String name, final int ordinal) {
        }
    }
    
    public static class SoundType
    {
        public final String soundName;
        public final float volume;
        public final float frequency;
        
        public SoundType(final String name, final float volume, final float frequency) {
            this.soundName = name;
            this.volume = volume;
            this.frequency = frequency;
        }
        
        public float getVolume() {
            return this.volume;
        }
        
        public float getFrequency() {
            return this.frequency;
        }
        
        public String getBreakSound() {
            return "dig." + this.soundName;
        }
        
        public String getStepSound() {
            return "step." + this.soundName;
        }
        
        public String getPlaceSound() {
            return this.getBreakSound();
        }
    }
}
