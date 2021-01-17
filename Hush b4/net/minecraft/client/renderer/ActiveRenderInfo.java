// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo
{
    private static final IntBuffer VIEWPORT;
    private static final FloatBuffer MODELVIEW;
    private static final FloatBuffer PROJECTION;
    private static final FloatBuffer OBJECTCOORDS;
    private static Vec3 position;
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;
    
    static {
        VIEWPORT = GLAllocation.createDirectIntBuffer(16);
        MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
        PROJECTION = GLAllocation.createDirectFloatBuffer(16);
        OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
        ActiveRenderInfo.position = new Vec3(0.0, 0.0, 0.0);
    }
    
    public static void updateRenderInfo(final EntityPlayer entityplayerIn, final boolean p_74583_1_) {
        GlStateManager.getFloat(2982, ActiveRenderInfo.MODELVIEW);
        GlStateManager.getFloat(2983, ActiveRenderInfo.PROJECTION);
        GL11.glGetInteger(2978, ActiveRenderInfo.VIEWPORT);
        final float f = (float)((ActiveRenderInfo.VIEWPORT.get(0) + ActiveRenderInfo.VIEWPORT.get(2)) / 2);
        final float f2 = (float)((ActiveRenderInfo.VIEWPORT.get(1) + ActiveRenderInfo.VIEWPORT.get(3)) / 2);
        GLU.gluUnProject(f, f2, 0.0f, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT, ActiveRenderInfo.OBJECTCOORDS);
        ActiveRenderInfo.position = new Vec3(ActiveRenderInfo.OBJECTCOORDS.get(0), ActiveRenderInfo.OBJECTCOORDS.get(1), ActiveRenderInfo.OBJECTCOORDS.get(2));
        final int i = p_74583_1_ ? 1 : 0;
        final float f3 = entityplayerIn.rotationPitch;
        final float f4 = entityplayerIn.rotationYaw;
        ActiveRenderInfo.rotationX = MathHelper.cos(f4 * 3.1415927f / 180.0f) * (1 - i * 2);
        ActiveRenderInfo.rotationZ = MathHelper.sin(f4 * 3.1415927f / 180.0f) * (1 - i * 2);
        ActiveRenderInfo.rotationYZ = -ActiveRenderInfo.rotationZ * MathHelper.sin(f3 * 3.1415927f / 180.0f) * (1 - i * 2);
        ActiveRenderInfo.rotationXY = ActiveRenderInfo.rotationX * MathHelper.sin(f3 * 3.1415927f / 180.0f) * (1 - i * 2);
        ActiveRenderInfo.rotationXZ = MathHelper.cos(f3 * 3.1415927f / 180.0f);
    }
    
    public static Vec3 projectViewFromEntity(final Entity p_178806_0_, final double p_178806_1_) {
        final double d0 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
        final double d2 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
        final double d3 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
        final double d4 = d0 + ActiveRenderInfo.position.xCoord;
        final double d5 = d2 + ActiveRenderInfo.position.yCoord;
        final double d6 = d3 + ActiveRenderInfo.position.zCoord;
        return new Vec3(d4, d5, d6);
    }
    
    public static Block getBlockAtEntityViewpoint(final World worldIn, final Entity p_180786_1_, final float p_180786_2_) {
        final Vec3 vec3 = projectViewFromEntity(p_180786_1_, p_180786_2_);
        final BlockPos blockpos = new BlockPos(vec3);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.getMaterial().isLiquid()) {
            float f = 0.0f;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                f = BlockLiquid.getLiquidHeightPercent(iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL)) - 0.11111111f;
            }
            final float f2 = blockpos.getY() + 1 - f;
            if (vec3.yCoord >= f2) {
                block = worldIn.getBlockState(blockpos.up()).getBlock();
            }
        }
        return block;
    }
    
    public static Vec3 getPosition() {
        return ActiveRenderInfo.position;
    }
    
    public static float getRotationX() {
        return ActiveRenderInfo.rotationX;
    }
    
    public static float getRotationXZ() {
        return ActiveRenderInfo.rotationXZ;
    }
    
    public static float getRotationZ() {
        return ActiveRenderInfo.rotationZ;
    }
    
    public static float getRotationYZ() {
        return ActiveRenderInfo.rotationYZ;
    }
    
    public static float getRotationXY() {
        return ActiveRenderInfo.rotationXY;
    }
}
