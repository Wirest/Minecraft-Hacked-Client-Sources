package net.minecraft.block.state;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockProperties
{
    Material getMaterial();

    boolean isFullBlock();

    boolean canEntitySpawn(Entity entityIn);

    int getLightOpacity();

    int getLightValue();

    boolean isTranslucent();

    boolean useNeighborBrightness();

    MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_);

    /**
     * Returns the blockstate with the given rotation. If inapplicable, returns itself.
     */
    IBlockState withRotation(Rotation rot);

    /**
     * Returns the blockstate mirrored in the given way. If inapplicable, returns itself.
     */
    IBlockState withMirror(Mirror mirrorIn);

    boolean isFullCube();

    boolean func_191057_i();

    EnumBlockRenderType getRenderType();

    int getPackedLightmapCoords(IBlockAccess source, BlockPos pos);

    float getAmbientOcclusionLightValue();

    boolean isBlockNormalCube();

    boolean isNormalCube();

    boolean canProvidePower();

    int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);

    boolean hasComparatorInputOverride();

    int getComparatorInputOverride(World worldIn, BlockPos pos);

    float getBlockHardness(World worldIn, BlockPos pos);

    float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos);

    int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);

    EnumPushReaction getMobilityFlag();

    IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos);

    AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos);

    boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing);

    boolean isOpaqueCube();

    @Nullable
    AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos);

    void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, Entity p_185908_5_, boolean p_185908_6_);

    AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos);

    RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end);

    boolean isFullyOpaque();

    Vec3d func_191059_e(IBlockAccess p_191059_1_, BlockPos p_191059_2_);

    boolean func_191058_s();

    BlockFaceShape func_193401_d(IBlockAccess p_193401_1_, BlockPos p_193401_2_, EnumFacing p_193401_3_);
}
