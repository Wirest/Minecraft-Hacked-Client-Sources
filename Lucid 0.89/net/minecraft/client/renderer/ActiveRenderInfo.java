package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ActiveRenderInfo
{
    /** The current GL viewport */
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    
    /** The current GL modelview matrix */
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    
    /** The current GL projection matrix */
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    
    /** The computed view object coordinates */
    private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
    public static Vec3 position = new Vec3(0.0D, 0.0D, 0.0D);
    
    /** The X component of the entity's yaw rotation */
    private static float rotationX;
    
    /** The combined X and Z components of the entity's pitch rotation */
    private static float rotationXZ;
    
    /** The Z component of the entity's yaw rotation */
    private static float rotationZ;
    
    /**
     * The Y component (scaled along the Z axis) of the entity's pitch rotation
     */
    private static float rotationYZ;
    
    /**
     * The Y component (scaled along the X axis) of the entity's pitch rotation
     */
    private static float rotationXY;
    
    /**
     * Updates the current render info and camera location based on entity look angles and 1st/3rd person view mode
     */
    public static void updateRenderInfo(EntityPlayer entityplayerIn, boolean p_74583_1_)
    {
	GlStateManager.getFloat(2982, MODELVIEW);
	GlStateManager.getFloat(2983, PROJECTION);
	GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
	float var2 = (VIEWPORT.get(0) + VIEWPORT.get(2)) / 2;
	float var3 = (VIEWPORT.get(1) + VIEWPORT.get(3)) / 2;
	GLU.gluUnProject(var2, var3, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
	position = new Vec3(OBJECTCOORDS.get(0), OBJECTCOORDS.get(1), OBJECTCOORDS.get(2));
	int var4 = p_74583_1_ ? 1 : 0;
	float var5 = entityplayerIn.rotationPitch;
	float var6 = entityplayerIn.rotationYaw;
	rotationX = MathHelper.cos(var6 * (float) Math.PI / 180.0F) * (1 - var4 * 2);
	rotationZ = MathHelper.sin(var6 * (float) Math.PI / 180.0F) * (1 - var4 * 2);
	rotationYZ = -rotationZ * MathHelper.sin(var5 * (float) Math.PI / 180.0F) * (1 - var4 * 2);
	rotationXY = rotationX * MathHelper.sin(var5 * (float) Math.PI / 180.0F) * (1 - var4 * 2);
	rotationXZ = MathHelper.cos(var5 * (float) Math.PI / 180.0F);
    }
    
    public static Vec3 projectViewFromEntity(Entity p_178806_0_, double p_178806_1_)
    {
	double var3 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
	double var5 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
	double var7 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
	double var9 = var3 + position.xCoord;
	double var11 = var5 + position.yCoord;
	double var13 = var7 + position.zCoord;
	return new Vec3(var9, var11, var13);
    }
    
    public static Block getBlockAtEntityViewpoint(World worldIn, Entity p_180786_1_, float p_180786_2_)
    {
	Vec3 var3 = projectViewFromEntity(p_180786_1_, p_180786_2_);
	BlockPos var4 = new BlockPos(var3);
	IBlockState var5 = worldIn.getBlockState(var4);
	Block var6 = var5.getBlock();
	
	if (var6.getMaterial().isLiquid())
	{
	    float var7 = 0.0F;
	    
	    if (var5.getBlock() instanceof BlockLiquid)
	    {
		var7 = BlockLiquid.getLiquidHeightPercent(((Integer) var5.getValue(BlockLiquid.LEVEL)).intValue()) - 0.11111111F;
	    }
	    
	    float var8 = var4.getY() + 1 - var7;
	    
	    if (var3.yCoord >= var8)
	    {
		var6 = worldIn.getBlockState(var4.up()).getBlock();
	    }
	}
	
	return var6;
    }
    
    public static Vec3 getPosition()
    {
	return position;
    }
    
    public static float getRotationX()
    {
	return rotationX;
    }
    
    public static float getRotationXZ()
    {
	return rotationXZ;
    }
    
    public static float getRotationZ()
    {
	return rotationZ;
    }
    
    public static float getRotationYZ()
    {
	return rotationYZ;
    }
    
    public static float getRotationXY()
    {
	return rotationXY;
    }
}
