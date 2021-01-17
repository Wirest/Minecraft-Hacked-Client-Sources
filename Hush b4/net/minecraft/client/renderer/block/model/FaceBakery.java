// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Matrix4f;
import net.minecraft.client.renderer.EnumFaceDirection;
import shadersmod.client.Shaders;
import net.minecraft.util.MathHelper;
import optifine.Config;
import optifine.Reflector;
import net.minecraftforge.client.model.ITransformation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.util.vector.Vector3f;

public class FaceBakery
{
    private static final float field_178418_a;
    private static final float field_178417_b;
    private static final String __OBFID = "CL_00002490";
    
    static {
        field_178418_a = 1.0f / (float)Math.cos(0.39269909262657166) - 1.0f;
        field_178417_b = 1.0f / (float)Math.cos(0.7853981633974483) - 1.0f;
    }
    
    public BakedQuad makeBakedQuad(final Vector3f posFrom, final Vector3f posTo, final BlockPartFace face, final TextureAtlasSprite sprite, final EnumFacing facing, final ModelRotation modelRotationIn, final BlockPartRotation partRotation, final boolean uvLocked, final boolean shade) {
        final int[] aint = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        final EnumFacing enumfacing = getFacingFromVertexData(aint);
        if (uvLocked) {
            this.func_178409_a(aint, enumfacing, face.blockFaceUV, sprite);
        }
        if (partRotation == null) {
            this.applyFacing(aint, enumfacing);
        }
        return new BakedQuad(aint, face.tintIndex, enumfacing, sprite);
    }
    
    public BakedQuad makeBakedQuad(final Vector3f p_makeBakedQuad_1_, final Vector3f p_makeBakedQuad_2_, final BlockPartFace p_makeBakedQuad_3_, final TextureAtlasSprite p_makeBakedQuad_4_, final EnumFacing p_makeBakedQuad_5_, final ITransformation p_makeBakedQuad_6_, final BlockPartRotation p_makeBakedQuad_7_, final boolean p_makeBakedQuad_8_, final boolean p_makeBakedQuad_9_) {
        final int[] aint = this.makeQuadVertexData(p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, this.getPositionsDiv16(p_makeBakedQuad_1_, p_makeBakedQuad_2_), p_makeBakedQuad_6_, p_makeBakedQuad_7_, p_makeBakedQuad_8_, p_makeBakedQuad_9_);
        final EnumFacing enumfacing = getFacingFromVertexData(aint);
        if (p_makeBakedQuad_8_) {
            this.func_178409_a(aint, enumfacing, p_makeBakedQuad_3_.blockFaceUV, p_makeBakedQuad_4_);
        }
        if (p_makeBakedQuad_7_ == null) {
            this.applyFacing(aint, enumfacing);
        }
        if (Reflector.ForgeHooksClient_fillNormal.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_fillNormal, aint, enumfacing);
        }
        return new BakedQuad(aint, p_makeBakedQuad_3_.tintIndex, enumfacing, p_makeBakedQuad_4_);
    }
    
    private int[] makeQuadVertexData(final BlockPartFace p_makeQuadVertexData_1_, final TextureAtlasSprite p_makeQuadVertexData_2_, final EnumFacing p_makeQuadVertexData_3_, final float[] p_makeQuadVertexData_4_, final ITransformation p_makeQuadVertexData_5_, final BlockPartRotation p_makeQuadVertexData_6_, final boolean p_makeQuadVertexData_7_, final boolean p_makeQuadVertexData_8_) {
        int i = 28;
        if (Config.isShaders()) {
            i = 56;
        }
        final int[] aint = new int[i];
        for (int j = 0; j < 4; ++j) {
            this.fillVertexData(aint, j, p_makeQuadVertexData_3_, p_makeQuadVertexData_1_, p_makeQuadVertexData_4_, p_makeQuadVertexData_2_, p_makeQuadVertexData_5_, p_makeQuadVertexData_6_, p_makeQuadVertexData_7_, p_makeQuadVertexData_8_);
        }
        return aint;
    }
    
    private int getFaceShadeColor(final EnumFacing facing) {
        final float f = this.getFaceBrightness(facing);
        final int i = MathHelper.clamp_int((int)(f * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | i << 8 | i;
    }
    
    private float getFaceBrightness(final EnumFacing facing) {
        switch (FaceBakery$1.field_178400_a[facing.ordinal()]) {
            case 1: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel05;
                }
                return 0.5f;
            }
            case 2: {
                return 1.0f;
            }
            case 3:
            case 4: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel08;
                }
                return 0.8f;
            }
            case 5:
            case 6: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel06;
                }
                return 0.6f;
            }
            default: {
                return 1.0f;
            }
        }
    }
    
    private float[] getPositionsDiv16(final Vector3f pos1, final Vector3f pos2) {
        final float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0f;
        return afloat;
    }
    
    private void fillVertexData(final int[] p_fillVertexData_1_, final int p_fillVertexData_2_, final EnumFacing p_fillVertexData_3_, final BlockPartFace p_fillVertexData_4_, final float[] p_fillVertexData_5_, final TextureAtlasSprite p_fillVertexData_6_, final ITransformation p_fillVertexData_7_, final BlockPartRotation p_fillVertexData_8_, final boolean p_fillVertexData_9_, final boolean p_fillVertexData_10_) {
        final EnumFacing enumfacing = p_fillVertexData_7_.rotate(p_fillVertexData_3_);
        final int i = p_fillVertexData_10_ ? this.getFaceShadeColor(enumfacing) : -1;
        final EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(p_fillVertexData_3_).func_179025_a(p_fillVertexData_2_);
        final Vector3f vector3f = new Vector3f(p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179184_a], p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179182_b], p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179183_c]);
        this.func_178407_a(vector3f, p_fillVertexData_8_);
        final int j = this.rotateVertex(vector3f, p_fillVertexData_3_, p_fillVertexData_2_, p_fillVertexData_7_, p_fillVertexData_9_);
        this.storeVertexData(p_fillVertexData_1_, j, p_fillVertexData_2_, vector3f, i, p_fillVertexData_6_, p_fillVertexData_4_.blockFaceUV);
    }
    
    private void storeVertexData(final int[] faceData, final int storeIndex, final int vertexIndex, final Vector3f position, final int shadeColor, final TextureAtlasSprite sprite, final BlockFaceUV faceUV) {
        final int i = faceData.length / 4;
        final int j = storeIndex * i;
        faceData[j] = Float.floatToRawIntBits(position.x);
        faceData[j + 1] = Float.floatToRawIntBits(position.y);
        faceData[j + 2] = Float.floatToRawIntBits(position.z);
        faceData[j + 3] = shadeColor;
        faceData[j + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
        faceData[j + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
    }
    
    private void func_178407_a(final Vector3f p_178407_1_, final BlockPartRotation partRotation) {
        if (partRotation != null) {
            final Matrix4f matrix4f = this.getMatrixIdentity();
            final Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            switch (FaceBakery$1.field_178399_b[partRotation.axis.ordinal()]) {
                case 1: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(1.0f, 0.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(0.0f, 1.0f, 1.0f);
                    break;
                }
                case 2: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(0.0f, 1.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 0.0f, 1.0f);
                    break;
                }
                case 3: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(0.0f, 0.0f, 1.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 1.0f, 0.0f);
                    break;
                }
            }
            if (partRotation.rescale) {
                if (Math.abs(partRotation.angle) == 22.5f) {
                    vector3f.scale(FaceBakery.field_178418_a);
                }
                else {
                    vector3f.scale(FaceBakery.field_178417_b);
                }
                Vector3f.add(vector3f, new Vector3f(1.0f, 1.0f, 1.0f), vector3f);
            }
            else {
                vector3f.set(1.0f, 1.0f, 1.0f);
            }
            this.rotateScale(p_178407_1_, new Vector3f(partRotation.origin), matrix4f, vector3f);
        }
    }
    
    public int rotateVertex(final Vector3f position, final EnumFacing facing, final int vertexIndex, final ModelRotation modelRotationIn, final boolean uvLocked) {
        return this.rotateVertex(position, facing, vertexIndex, modelRotationIn, uvLocked);
    }
    
    public int rotateVertex(final Vector3f p_rotateVertex_1_, final EnumFacing p_rotateVertex_2_, final int p_rotateVertex_3_, final ITransformation p_rotateVertex_4_, final boolean p_rotateVertex_5_) {
        if (p_rotateVertex_4_ == ModelRotation.X0_Y0) {
            return p_rotateVertex_3_;
        }
        if (Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, p_rotateVertex_1_, p_rotateVertex_4_.getMatrix());
        }
        else {
            this.rotateScale(p_rotateVertex_1_, new Vector3f(0.5f, 0.5f, 0.5f), ((ModelRotation)p_rotateVertex_4_).getMatrix4d(), new Vector3f(1.0f, 1.0f, 1.0f));
        }
        return p_rotateVertex_4_.rotate(p_rotateVertex_2_, p_rotateVertex_3_);
    }
    
    private void rotateScale(final Vector3f position, final Vector3f rotationOrigin, final Matrix4f rotationMatrix, final Vector3f scale) {
        final Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0f);
        Matrix4f.transform(rotationMatrix, vector4f, vector4f);
        final Vector4f vector4f2 = vector4f;
        vector4f2.x *= scale.x;
        final Vector4f vector4f3 = vector4f;
        vector4f3.y *= scale.y;
        final Vector4f vector4f4 = vector4f;
        vector4f4.z *= scale.z;
        position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
    }
    
    private Matrix4f getMatrixIdentity() {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        return matrix4f;
    }
    
    public static EnumFacing getFacingFromVertexData(final int[] faceData) {
        final int i = faceData.length / 4;
        final int j = i * 2;
        final int k = i * 3;
        final Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
        final Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[i]), Float.intBitsToFloat(faceData[i + 1]), Float.intBitsToFloat(faceData[i + 2]));
        final Vector3f vector3f3 = new Vector3f(Float.intBitsToFloat(faceData[j]), Float.intBitsToFloat(faceData[j + 1]), Float.intBitsToFloat(faceData[j + 2]));
        final Vector3f vector3f4 = new Vector3f();
        final Vector3f vector3f5 = new Vector3f();
        final Vector3f vector3f6 = new Vector3f();
        Vector3f.sub(vector3f, vector3f2, vector3f4);
        Vector3f.sub(vector3f3, vector3f2, vector3f5);
        Vector3f.cross(vector3f5, vector3f4, vector3f6);
        final float f = (float)Math.sqrt(vector3f6.x * vector3f6.x + vector3f6.y * vector3f6.y + vector3f6.z * vector3f6.z);
        final Vector3f vector3f8 = vector3f6;
        vector3f8.x /= f;
        final Vector3f vector3f9 = vector3f6;
        vector3f9.y /= f;
        final Vector3f vector3f10 = vector3f6;
        vector3f10.z /= f;
        EnumFacing enumfacing = null;
        float f2 = 0.0f;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, l = 0; l < length; ++l) {
            final EnumFacing enumfacing2 = values[l];
            final Vec3i vec3i = enumfacing2.getDirectionVec();
            final Vector3f vector3f7 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            final float f3 = Vector3f.dot(vector3f6, vector3f7);
            if (f3 >= 0.0f && f3 > f2) {
                f2 = f3;
                enumfacing = enumfacing2;
            }
        }
        if (f2 < 0.719f) {
            if (enumfacing != EnumFacing.EAST && enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH && enumfacing != EnumFacing.SOUTH) {
                enumfacing = EnumFacing.UP;
            }
            else {
                enumfacing = EnumFacing.NORTH;
            }
        }
        return (enumfacing == null) ? EnumFacing.UP : enumfacing;
    }
    
    public void func_178409_a(final int[] p_178409_1_, final EnumFacing facing, final BlockFaceUV p_178409_3_, final TextureAtlasSprite p_178409_4_) {
        for (int i = 0; i < 4; ++i) {
            this.func_178401_a(i, p_178409_1_, facing, p_178409_3_, p_178409_4_);
        }
    }
    
    private void applyFacing(final int[] p_applyFacing_1_, final EnumFacing p_applyFacing_2_) {
        final int[] aint = new int[p_applyFacing_1_.length];
        System.arraycopy(p_applyFacing_1_, 0, aint, 0, p_applyFacing_1_.length);
        final float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0f;
        final int j = p_applyFacing_1_.length / 4;
        for (int k = 0; k < 4; ++k) {
            final int i = j * k;
            final float f1 = Float.intBitsToFloat(aint[i]);
            final float f2 = Float.intBitsToFloat(aint[i + 1]);
            final float f3 = Float.intBitsToFloat(aint[i + 2]);
            if (f1 < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
                afloat[EnumFaceDirection.Constants.WEST_INDEX] = f1;
            }
            if (f2 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
                afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f2;
            }
            if (f3 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f3;
            }
            if (f1 > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
                afloat[EnumFaceDirection.Constants.EAST_INDEX] = f1;
            }
            if (f2 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
                afloat[EnumFaceDirection.Constants.UP_INDEX] = f2;
            }
            if (f3 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f3;
            }
        }
        final EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_applyFacing_2_);
        for (int j2 = 0; j2 < 4; ++j2) {
            final int k2 = j * j2;
            final EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.func_179025_a(j2);
            final float f4 = afloat[enumfacedirection$vertexinformation.field_179184_a];
            final float f5 = afloat[enumfacedirection$vertexinformation.field_179182_b];
            final float f6 = afloat[enumfacedirection$vertexinformation.field_179183_c];
            p_applyFacing_1_[k2] = Float.floatToRawIntBits(f4);
            p_applyFacing_1_[k2 + 1] = Float.floatToRawIntBits(f5);
            p_applyFacing_1_[k2 + 2] = Float.floatToRawIntBits(f6);
            for (int l = 0; l < 4; ++l) {
                final int i2 = j * l;
                final float f7 = Float.intBitsToFloat(aint[i2]);
                final float f8 = Float.intBitsToFloat(aint[i2 + 1]);
                final float f9 = Float.intBitsToFloat(aint[i2 + 2]);
                if (MathHelper.epsilonEquals(f4, f7) && MathHelper.epsilonEquals(f5, f8) && MathHelper.epsilonEquals(f6, f9)) {
                    p_applyFacing_1_[k2 + 4] = aint[i2 + 4];
                    p_applyFacing_1_[k2 + 4 + 1] = aint[i2 + 4 + 1];
                }
            }
        }
    }
    
    private void func_178401_a(final int p_178401_1_, final int[] p_178401_2_, final EnumFacing facing, final BlockFaceUV p_178401_4_, final TextureAtlasSprite p_178401_5_) {
        final int i = p_178401_2_.length / 4;
        final int j = i * p_178401_1_;
        float f = Float.intBitsToFloat(p_178401_2_[j]);
        float f2 = Float.intBitsToFloat(p_178401_2_[j + 1]);
        float f3 = Float.intBitsToFloat(p_178401_2_[j + 2]);
        if (f < -0.1f || f >= 1.1f) {
            f -= MathHelper.floor_float(f);
        }
        if (f2 < -0.1f || f2 >= 1.1f) {
            f2 -= MathHelper.floor_float(f2);
        }
        if (f3 < -0.1f || f3 >= 1.1f) {
            f3 -= MathHelper.floor_float(f3);
        }
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (FaceBakery$1.field_178400_a[facing.ordinal()]) {
            case 1: {
                f4 = f * 16.0f;
                f5 = (1.0f - f3) * 16.0f;
                break;
            }
            case 2: {
                f4 = f * 16.0f;
                f5 = f3 * 16.0f;
                break;
            }
            case 3: {
                f4 = (1.0f - f) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 4: {
                f4 = f * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 5: {
                f4 = f3 * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case 6: {
                f4 = (1.0f - f3) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
        }
        final int k = p_178401_4_.func_178345_c(p_178401_1_) * i;
        p_178401_2_[k + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(f4));
        p_178401_2_[k + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(f5));
    }
    
    static final class FaceBakery$1
    {
        static final int[] field_178400_a;
        static final int[] field_178399_b;
        private static final String __OBFID = "CL_00002489";
        
        static {
            field_178399_b = new int[EnumFacing.Axis.values().length];
            try {
                FaceBakery$1.field_178399_b[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                FaceBakery$1.field_178399_b[EnumFacing.Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                FaceBakery$1.field_178399_b[EnumFacing.Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            field_178400_a = new int[EnumFacing.values().length];
            try {
                FaceBakery$1.field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                FaceBakery$1.field_178400_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                FaceBakery$1.field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                FaceBakery$1.field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                FaceBakery$1.field_178400_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                FaceBakery$1.field_178400_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
        }
    }
}
