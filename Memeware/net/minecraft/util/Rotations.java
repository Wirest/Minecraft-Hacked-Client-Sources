package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
    protected final float field_179419_a;
    protected final float field_179417_b;
    protected final float field_179418_c;
    private static final String __OBFID = "CL_00002316";

    public Rotations(float p_i46009_1_, float p_i46009_2_, float p_i46009_3_) {
        this.field_179419_a = p_i46009_1_;
        this.field_179417_b = p_i46009_2_;
        this.field_179418_c = p_i46009_3_;
    }

    public Rotations(NBTTagList p_i46010_1_) {
        this.field_179419_a = p_i46010_1_.getFloat(0);
        this.field_179417_b = p_i46010_1_.getFloat(1);
        this.field_179418_c = p_i46010_1_.getFloat(2);
    }

    public NBTTagList func_179414_a() {
        NBTTagList var1 = new NBTTagList();
        var1.appendTag(new NBTTagFloat(this.field_179419_a));
        var1.appendTag(new NBTTagFloat(this.field_179417_b));
        var1.appendTag(new NBTTagFloat(this.field_179418_c));
        return var1;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof Rotations)) {
            return false;
        } else {
            Rotations var2 = (Rotations) p_equals_1_;
            return this.field_179419_a == var2.field_179419_a && this.field_179417_b == var2.field_179417_b && this.field_179418_c == var2.field_179418_c;
        }
    }

    public float func_179415_b() {
        return this.field_179419_a;
    }

    public float func_179416_c() {
        return this.field_179417_b;
    }

    public float func_179413_d() {
        return this.field_179418_c;
    }
}
