package net.minecraft.client.renderer.block.model;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.EnumFaceing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.model.ITransformation;

public class FaceBakery
{
    private static final double field_178418_a = 1.0D / Math.cos(0.39269908169872414D) - 1.0D;
    private static final double field_178417_b = 1.0D / Math.cos((Math.PI / 4D)) - 1.0D;

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        int[] var10 = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        EnumFacing var11 = getFacingFromVertexData(var10);

        if (uvLocked)
        {
            this.func_178409_a(var10, var11, face.blockFaceUV, sprite);
        }

        if (partRotation == null)
        {
            this.func_178408_a(var10, var11);
        }

        return new BakedQuad(var10, face.tintIndex, var11, sprite);
    }

    private int[] makeQuadVertexData(BlockPartFace partFace, TextureAtlasSprite sprite, EnumFacing facing, float[] p_178405_4_, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        int[] var9 = new int[28];

        for (int var10 = 0; var10 < 4; ++var10)
        {
            this.fillVertexData(var9, var10, facing, partFace, p_178405_4_, sprite, modelRotationIn, partRotation, uvLocked, shade);
        }

        return var9;
    }

    private int getFaceShadeColor(EnumFacing facing)
    {
        float var2 = this.getFaceBrightness(facing);
        int var3 = MathHelper.clamp_int((int)(var2 * 255.0F), 0, 255);
        return -16777216 | var3 << 16 | var3 << 8 | var3;
    }

    private float getFaceBrightness(EnumFacing facing)
    {
        switch (FaceBakery.SwitchEnumFacing.field_178400_a[facing.ordinal()])
        {
            case 1:
                return 0.5F;

            case 2:
                return 1.0F;

            case 3:
            case 4:
                return 0.8F;

            case 5:
            case 6:
                return 0.6F;

            default:
                return 1.0F;
        }
    }

    private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2)
    {
        float[] var3 = new float[EnumFacing.values().length];
        var3[EnumFaceing.Constants.WEST_INDEX] = pos1.x / 16.0F;
        var3[EnumFaceing.Constants.DOWN_INDEX] = pos1.y / 16.0F;
        var3[EnumFaceing.Constants.NORTH_INDEX] = pos1.z / 16.0F;
        var3[EnumFaceing.Constants.EAST_INDEX] = pos2.x / 16.0F;
        var3[EnumFaceing.Constants.UP_INDEX] = pos2.y / 16.0F;
        var3[EnumFaceing.Constants.SOUTH_INDEX] = pos2.z / 16.0F;
        return var3;
    }

    private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        EnumFacing var11 = modelRotationIn.rotate(facing);
        int var12 = shade ? this.getFaceShadeColor(var11) : -1;
        EnumFaceing.VertexInformation var13 = EnumFaceing.getFacing(facing).func_179025_a(vertexIndex);
        Vector3d var14 = new Vector3d(p_178402_5_[var13.field_179184_a], p_178402_5_[var13.field_179182_b], p_178402_5_[var13.field_179183_c]);
        this.func_178407_a(var14, partRotation);
        int var15 = this.rotateVertex(var14, facing, vertexIndex, modelRotationIn, uvLocked);
        this.storeVertexData(faceData, var15, vertexIndex, var14, var12, sprite, partFace.blockFaceUV);
    }

    private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3d position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
    {
        int var8 = storeIndex * 7;
        faceData[var8] = Float.floatToRawIntBits((float)position.x);
        faceData[var8 + 1] = Float.floatToRawIntBits((float)position.y);
        faceData[var8 + 2] = Float.floatToRawIntBits((float)position.z);
        faceData[var8 + 3] = shadeColor;
        faceData[var8 + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
        faceData[var8 + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
    }

    private void func_178407_a(Vector3d p_178407_1_, BlockPartRotation partRotation)
    {
        if (partRotation != null)
        {
            Matrix4d var3 = this.getMatrixIdentity();
            Vector3d var4 = new Vector3d(0.0D, 0.0D, 0.0D);

            switch (FaceBakery.SwitchEnumFacing.field_178399_b[partRotation.axis.ordinal()])
            {
                case 1:
                    var3.mul(this.getMatrixRotation(new AxisAngle4d(1.0D, 0.0D, 0.0D, partRotation.angle * 0.017453292519943295D)));
                    var4.set(0.0D, 1.0D, 1.0D);
                    break;

                case 2:
                    var3.mul(this.getMatrixRotation(new AxisAngle4d(0.0D, 1.0D, 0.0D, partRotation.angle * 0.017453292519943295D)));
                    var4.set(1.0D, 0.0D, 1.0D);
                    break;

                case 3:
                    var3.mul(this.getMatrixRotation(new AxisAngle4d(0.0D, 0.0D, 1.0D, partRotation.angle * 0.017453292519943295D)));
                    var4.set(1.0D, 1.0D, 0.0D);
            }

            if (partRotation.rescale)
            {
                if (Math.abs(partRotation.angle) == 22.5F)
                {
                    var4.scale(field_178418_a);
                }
                else
                {
                    var4.scale(field_178417_b);
                }

                var4.add(new Vector3d(1.0D, 1.0D, 1.0D));
            }
            else
            {
                var4.set(new Vector3d(1.0D, 1.0D, 1.0D));
            }

            this.rotateScale(p_178407_1_, new Vector3d(partRotation.origin), var3, var4);
        }
    }

    public int rotateVertex(Vector3d position, EnumFacing facing, int vertexIndex, ITransformation modelRotationIn, boolean uvLocked)
    {
        if (modelRotationIn == ModelRotation.X0_Y0)
        {
            return vertexIndex;
        }
        else
        {
            this.rotateScale(position, new Vector3d(0.5D, 0.5D, 0.5D), new Matrix4d(modelRotationIn.getMatrix()), new Vector3d(1.0D, 1.0D, 1.0D));
            return modelRotationIn.rotate(facing, vertexIndex);
        }
    }

    private void rotateScale(Vector3d position, Vector3d rotationOrigin, Matrix4d rotationMatrix, Vector3d scale)
    {
        position.sub(rotationOrigin);
        rotationMatrix.transform(position);
        position.x *= scale.x;
        position.y *= scale.y;
        position.z *= scale.z;
        position.add(rotationOrigin);
    }

    private Matrix4d getMatrixRotation(AxisAngle4d p_178416_1_)
    {
        Matrix4d var2 = this.getMatrixIdentity();
        var2.setRotation(p_178416_1_);
        return var2;
    }

    private Matrix4d getMatrixIdentity()
    {
        Matrix4d var1 = new Matrix4d();
        var1.setIdentity();
        return var1;
    }

    public static EnumFacing getFacingFromVertexData(int[] faceData)
    {
        Vector3f var1 = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
        Vector3f var2 = new Vector3f(Float.intBitsToFloat(faceData[7]), Float.intBitsToFloat(faceData[8]), Float.intBitsToFloat(faceData[9]));
        Vector3f var3 = new Vector3f(Float.intBitsToFloat(faceData[14]), Float.intBitsToFloat(faceData[15]), Float.intBitsToFloat(faceData[16]));
        Vector3f var4 = new Vector3f();
        Vector3f var5 = new Vector3f();
        Vector3f var6 = new Vector3f();
        var4.sub(var1, var2);
        var5.sub(var3, var2);
        var6.cross(var5, var4);
        var6.normalize();
        EnumFacing var7 = null;
        float var8 = 0.0F;
        EnumFacing[] var9 = EnumFacing.values();
        int var10 = var9.length;

        for (int var11 = 0; var11 < var10; ++var11)
        {
            EnumFacing var12 = var9[var11];
            Vec3i var13 = var12.getDirectionVec();
            Vector3f var14 = new Vector3f(var13.getX(), var13.getY(), var13.getZ());
            float var15 = var6.dot(var14);

            if (var15 >= 0.0F && var15 > var8)
            {
                var8 = var15;
                var7 = var12;
            }
        }

        if (var8 < 0.719F)
        {
            if (var7 != EnumFacing.EAST && var7 != EnumFacing.WEST && var7 != EnumFacing.NORTH && var7 != EnumFacing.SOUTH)
            {
                var7 = EnumFacing.UP;
            }
            else
            {
                var7 = EnumFacing.NORTH;
            }
        }

        return var7 == null ? EnumFacing.UP : var7;
    }

    public void func_178409_a(int[] p_178409_1_, EnumFacing facing, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_)
    {
        for (int var5 = 0; var5 < 4; ++var5)
        {
            this.func_178401_a(var5, p_178409_1_, facing, p_178409_3_, p_178409_4_);
        }
    }

    private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_)
    {
        int[] var3 = new int[p_178408_1_.length];
        System.arraycopy(p_178408_1_, 0, var3, 0, p_178408_1_.length);
        float[] var4 = new float[EnumFacing.values().length];
        var4[EnumFaceing.Constants.WEST_INDEX] = 999.0F;
        var4[EnumFaceing.Constants.DOWN_INDEX] = 999.0F;
        var4[EnumFaceing.Constants.NORTH_INDEX] = 999.0F;
        var4[EnumFaceing.Constants.EAST_INDEX] = -999.0F;
        var4[EnumFaceing.Constants.UP_INDEX] = -999.0F;
        var4[EnumFaceing.Constants.SOUTH_INDEX] = -999.0F;
        int var6;
        float var9;

        for (int var17 = 0; var17 < 4; ++var17)
        {
            var6 = 7 * var17;
            float var18 = Float.intBitsToFloat(var3[var6]);
            float var19 = Float.intBitsToFloat(var3[var6 + 1]);
            var9 = Float.intBitsToFloat(var3[var6 + 2]);

            if (var18 < var4[EnumFaceing.Constants.WEST_INDEX])
            {
                var4[EnumFaceing.Constants.WEST_INDEX] = var18;
            }

            if (var19 < var4[EnumFaceing.Constants.DOWN_INDEX])
            {
                var4[EnumFaceing.Constants.DOWN_INDEX] = var19;
            }

            if (var9 < var4[EnumFaceing.Constants.NORTH_INDEX])
            {
                var4[EnumFaceing.Constants.NORTH_INDEX] = var9;
            }

            if (var18 > var4[EnumFaceing.Constants.EAST_INDEX])
            {
                var4[EnumFaceing.Constants.EAST_INDEX] = var18;
            }

            if (var19 > var4[EnumFaceing.Constants.UP_INDEX])
            {
                var4[EnumFaceing.Constants.UP_INDEX] = var19;
            }

            if (var9 > var4[EnumFaceing.Constants.SOUTH_INDEX])
            {
                var4[EnumFaceing.Constants.SOUTH_INDEX] = var9;
            }
        }

        EnumFaceing var171 = EnumFaceing.getFacing(p_178408_2_);

        for (var6 = 0; var6 < 4; ++var6)
        {
            int var181 = 7 * var6;
            EnumFaceing.VertexInformation var191 = var171.func_179025_a(var6);
            var9 = var4[var191.field_179184_a];
            float var10 = var4[var191.field_179182_b];
            float var11 = var4[var191.field_179183_c];
            p_178408_1_[var181] = Float.floatToRawIntBits(var9);
            p_178408_1_[var181 + 1] = Float.floatToRawIntBits(var10);
            p_178408_1_[var181 + 2] = Float.floatToRawIntBits(var11);

            for (int var12 = 0; var12 < 4; ++var12)
            {
                int var13 = 7 * var12;
                float var14 = Float.intBitsToFloat(var3[var13]);
                float var15 = Float.intBitsToFloat(var3[var13 + 1]);
                float var16 = Float.intBitsToFloat(var3[var13 + 2]);

                if (MathHelper.func_180185_a(var9, var14) && MathHelper.func_180185_a(var10, var15) && MathHelper.func_180185_a(var11, var16))
                {
                    p_178408_1_[var181 + 4] = var3[var13 + 4];
                    p_178408_1_[var181 + 4 + 1] = var3[var13 + 4 + 1];
                }
            }
        }
    }

    private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing facing, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_)
    {
        int var6 = 7 * p_178401_1_;
        float var7 = Float.intBitsToFloat(p_178401_2_[var6]);
        float var8 = Float.intBitsToFloat(p_178401_2_[var6 + 1]);
        float var9 = Float.intBitsToFloat(p_178401_2_[var6 + 2]);

        if (var7 < -0.1F || var7 >= 1.1F)
        {
            var7 -= MathHelper.floor_float(var7);
        }

        if (var8 < -0.1F || var8 >= 1.1F)
        {
            var8 -= MathHelper.floor_float(var8);
        }

        if (var9 < -0.1F || var9 >= 1.1F)
        {
            var9 -= MathHelper.floor_float(var9);
        }

        float var10 = 0.0F;
        float var11 = 0.0F;

        switch (FaceBakery.SwitchEnumFacing.field_178400_a[facing.ordinal()])
        {
            case 1:
                var10 = var7 * 16.0F;
                var11 = (1.0F - var9) * 16.0F;
                break;

            case 2:
                var10 = var7 * 16.0F;
                var11 = var9 * 16.0F;
                break;

            case 3:
                var10 = (1.0F - var7) * 16.0F;
                var11 = (1.0F - var8) * 16.0F;
                break;

            case 4:
                var10 = var7 * 16.0F;
                var11 = (1.0F - var8) * 16.0F;
                break;

            case 5:
                var10 = var9 * 16.0F;
                var11 = (1.0F - var8) * 16.0F;
                break;

            case 6:
                var10 = (1.0F - var9) * 16.0F;
                var11 = (1.0F - var8) * 16.0F;
        }

        int var12 = p_178401_4_.func_178345_c(p_178401_1_) * 7;
        p_178401_2_[var12 + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(var10));
        p_178401_2_[var12 + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(var11));
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_178400_a;
        static final int[] field_178399_b = new int[EnumFacing.Axis.values().length];

        static
        {
            try
            {
                field_178399_b[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                field_178399_b[EnumFacing.Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                field_178399_b[EnumFacing.Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            field_178400_a = new int[EnumFacing.values().length];

            try
            {
                field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_178400_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_178400_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_178400_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
