package net.minecraft.client.resources.model;

import com.google.common.collect.Maps;

import java.util.Map;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.TRSRTransformation;
import optifine.Reflector;

public enum ModelRotation implements IModelState, ITransformation {
    X0_Y0("X0_Y0", 0, 0, 0),
    X0_Y90("X0_Y90", 1, 0, 90),
    X0_Y180("X0_Y180", 2, 0, 180),
    X0_Y270("X0_Y270", 3, 0, 270),
    X90_Y0("X90_Y0", 4, 90, 0),
    X90_Y90("X90_Y90", 5, 90, 90),
    X90_Y180("X90_Y180", 6, 90, 180),
    X90_Y270("X90_Y270", 7, 90, 270),
    X180_Y0("X180_Y0", 8, 180, 0),
    X180_Y90("X180_Y90", 9, 180, 90),
    X180_Y180("X180_Y180", 10, 180, 180),
    X180_Y270("X180_Y270", 11, 180, 270),
    X270_Y0("X270_Y0", 12, 270, 0),
    X270_Y90("X270_Y90", 13, 270, 90),
    X270_Y180("X270_Y180", 14, 270, 180),
    X270_Y270("X270_Y270", 15, 270, 270);
    private static final Map field_177546_q = Maps.newHashMap();
    private final int field_177545_r;
    private final Matrix4d field_177544_s;
    private final int field_177543_t;
    private final int field_177542_u;
    private static final String __OBFID = "CL_00002393";

    private static final ModelRotation[] $VALUES = new ModelRotation[]{X0_Y0, X0_Y90, X0_Y180, X0_Y270, X90_Y0, X90_Y90, X90_Y180, X90_Y270, X180_Y0, X180_Y90, X180_Y180, X180_Y270, X270_Y0, X270_Y90, X270_Y180, X270_Y270};

    private static int func_177521_b(int p_177521_0_, int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }

    private ModelRotation(String p_i46087_1_, int p_i46087_2_, int p_i46087_3_, int p_i46087_4_) {
        this.field_177545_r = func_177521_b(p_i46087_3_, p_i46087_4_);
        this.field_177544_s = new Matrix4d();
        Matrix4d var5 = new Matrix4d();
        var5.setIdentity();
        var5.setRotation(new AxisAngle4d(1.0D, 0.0D, 0.0D, (double) ((float) (-p_i46087_3_) * 0.017453292F)));
        this.field_177543_t = MathHelper.abs_int(p_i46087_3_ / 90);
        Matrix4d var6 = new Matrix4d();
        var6.setIdentity();
        var6.setRotation(new AxisAngle4d(0.0D, 1.0D, 0.0D, (double) ((float) (-p_i46087_4_) * 0.017453292F)));
        this.field_177542_u = MathHelper.abs_int(p_i46087_4_ / 90);
        this.field_177544_s.mul(var6, var5);
    }

    public Matrix4d func_177525_a() {
        return this.field_177544_s;
    }

    public EnumFacing func_177523_a(EnumFacing p_177523_1_) {
        EnumFacing var2 = p_177523_1_;
        int var3;

        for (var3 = 0; var3 < this.field_177543_t; ++var3) {
            var2 = var2.rotateAround(EnumFacing.Axis.X);
        }

        if (var2.getAxis() != EnumFacing.Axis.Y) {
            for (var3 = 0; var3 < this.field_177542_u; ++var3) {
                var2 = var2.rotateAround(EnumFacing.Axis.Y);
            }
        }

        return var2;
    }

    public int func_177520_a(EnumFacing facing, int vertexIndex) {
        int var3 = vertexIndex;

        if (facing.getAxis() == EnumFacing.Axis.X) {
            var3 = (vertexIndex + this.field_177543_t) % 4;
        }

        EnumFacing var4 = facing;

        for (int var5 = 0; var5 < this.field_177543_t; ++var5) {
            var4 = var4.rotateAround(EnumFacing.Axis.X);
        }

        if (var4.getAxis() == EnumFacing.Axis.Y) {
            var3 = (var3 + this.field_177542_u) % 4;
        }

        return var3;
    }

    public static ModelRotation func_177524_a(int p_177524_0_, int p_177524_1_) {
        return (ModelRotation) field_177546_q.get(Integer.valueOf(func_177521_b(MathHelper.func_180184_b(p_177524_0_, 360), MathHelper.func_180184_b(p_177524_1_, 360))));
    }

    public TRSRTransformation apply(IModelPart part) {
        return new TRSRTransformation(this.getMatrix());
    }

    public Matrix4f getMatrix() {
        return Reflector.ForgeHooksClient_getMatrix.exists() ? (Matrix4f) Reflector.call(Reflector.ForgeHooksClient_getMatrix, new Object[]{this}) : new Matrix4f(this.func_177525_a());
    }

    public EnumFacing rotate(EnumFacing facing) {
        return this.func_177523_a(facing);
    }

    public int rotate(EnumFacing facing, int vertexIndex) {
        return this.func_177520_a(facing, vertexIndex);
    }

    static {
        ModelRotation[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            ModelRotation var3 = var0[var2];
            field_177546_q.put(Integer.valueOf(var3.field_177545_r), var3);
        }
    }
}
