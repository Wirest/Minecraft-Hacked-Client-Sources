package net.minecraft.client.renderer.block.model;

import javax.vecmath.Vector3f;

import net.minecraft.util.EnumFacing;

public class BlockPartRotation {
    public final Vector3f field_178344_a;
    public final EnumFacing.Axis field_178342_b;
    public final float field_178343_c;
    public final boolean field_178341_d;
    private static final String __OBFID = "CL_00002506";

    public BlockPartRotation(Vector3f p_i46229_1_, EnumFacing.Axis p_i46229_2_, float p_i46229_3_, boolean p_i46229_4_) {
        this.field_178344_a = p_i46229_1_;
        this.field_178342_b = p_i46229_2_;
        this.field_178343_c = p_i46229_3_;
        this.field_178341_d = p_i46229_4_;
    }
}
