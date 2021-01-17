// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import org.lwjgl.opengl.GL14;
import net.minecraft.client.renderer.GlStateManager;

public class JsonBlendingMode
{
    private static JsonBlendingMode field_148118_a;
    private final int field_148116_b;
    private final int field_148117_c;
    private final int field_148114_d;
    private final int field_148115_e;
    private final int field_148112_f;
    private final boolean field_148113_g;
    private final boolean field_148119_h;
    
    static {
        JsonBlendingMode.field_148118_a = null;
    }
    
    private JsonBlendingMode(final boolean p_i45084_1_, final boolean p_i45084_2_, final int p_i45084_3_, final int p_i45084_4_, final int p_i45084_5_, final int p_i45084_6_, final int p_i45084_7_) {
        this.field_148113_g = p_i45084_1_;
        this.field_148116_b = p_i45084_3_;
        this.field_148114_d = p_i45084_4_;
        this.field_148117_c = p_i45084_5_;
        this.field_148115_e = p_i45084_6_;
        this.field_148119_h = p_i45084_2_;
        this.field_148112_f = p_i45084_7_;
    }
    
    public JsonBlendingMode() {
        this(false, true, 1, 0, 1, 0, 32774);
    }
    
    public JsonBlendingMode(final int p_i45085_1_, final int p_i45085_2_, final int p_i45085_3_) {
        this(false, false, p_i45085_1_, p_i45085_2_, p_i45085_1_, p_i45085_2_, p_i45085_3_);
    }
    
    public JsonBlendingMode(final int p_i45086_1_, final int p_i45086_2_, final int p_i45086_3_, final int p_i45086_4_, final int p_i45086_5_) {
        this(true, false, p_i45086_1_, p_i45086_2_, p_i45086_3_, p_i45086_4_, p_i45086_5_);
    }
    
    public void func_148109_a() {
        if (!this.equals(JsonBlendingMode.field_148118_a)) {
            if (JsonBlendingMode.field_148118_a == null || this.field_148119_h != JsonBlendingMode.field_148118_a.func_148111_b()) {
                JsonBlendingMode.field_148118_a = this;
                if (this.field_148119_h) {
                    GlStateManager.disableBlend();
                    return;
                }
                GlStateManager.enableBlend();
            }
            GL14.glBlendEquation(this.field_148112_f);
            if (this.field_148113_g) {
                GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
            }
            else {
                GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
            }
        }
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof JsonBlendingMode)) {
            return false;
        }
        final JsonBlendingMode jsonblendingmode = (JsonBlendingMode)p_equals_1_;
        return this.field_148112_f == jsonblendingmode.field_148112_f && this.field_148115_e == jsonblendingmode.field_148115_e && this.field_148114_d == jsonblendingmode.field_148114_d && this.field_148119_h == jsonblendingmode.field_148119_h && this.field_148113_g == jsonblendingmode.field_148113_g && this.field_148117_c == jsonblendingmode.field_148117_c && this.field_148116_b == jsonblendingmode.field_148116_b;
    }
    
    @Override
    public int hashCode() {
        int i = this.field_148116_b;
        i = 31 * i + this.field_148117_c;
        i = 31 * i + this.field_148114_d;
        i = 31 * i + this.field_148115_e;
        i = 31 * i + this.field_148112_f;
        i = 31 * i + (this.field_148113_g ? 1 : 0);
        i = 31 * i + (this.field_148119_h ? 1 : 0);
        return i;
    }
    
    public boolean func_148111_b() {
        return this.field_148119_h;
    }
    
    public static JsonBlendingMode func_148110_a(final JsonObject p_148110_0_) {
        if (p_148110_0_ == null) {
            return new JsonBlendingMode();
        }
        int i = 32774;
        int j = 1;
        int k = 0;
        int l = 1;
        int i2 = 0;
        boolean flag = true;
        boolean flag2 = false;
        if (JsonUtils.isString(p_148110_0_, "func")) {
            i = func_148108_a(p_148110_0_.get("func").getAsString());
            if (i != 32774) {
                flag = false;
            }
        }
        if (JsonUtils.isString(p_148110_0_, "srcrgb")) {
            j = func_148107_b(p_148110_0_.get("srcrgb").getAsString());
            if (j != 1) {
                flag = false;
            }
        }
        if (JsonUtils.isString(p_148110_0_, "dstrgb")) {
            k = func_148107_b(p_148110_0_.get("dstrgb").getAsString());
            if (k != 0) {
                flag = false;
            }
        }
        if (JsonUtils.isString(p_148110_0_, "srcalpha")) {
            l = func_148107_b(p_148110_0_.get("srcalpha").getAsString());
            if (l != 1) {
                flag = false;
            }
            flag2 = true;
        }
        if (JsonUtils.isString(p_148110_0_, "dstalpha")) {
            i2 = func_148107_b(p_148110_0_.get("dstalpha").getAsString());
            if (i2 != 0) {
                flag = false;
            }
            flag2 = true;
        }
        return flag ? new JsonBlendingMode() : (flag2 ? new JsonBlendingMode(j, k, l, i2, i) : new JsonBlendingMode(j, k, i));
    }
    
    private static int func_148108_a(final String p_148108_0_) {
        final String s = p_148108_0_.trim().toLowerCase();
        return s.equals("add") ? 32774 : (s.equals("subtract") ? 32778 : (s.equals("reversesubtract") ? 32779 : (s.equals("reverse_subtract") ? 32779 : (s.equals("min") ? 32775 : (s.equals("max") ? 32776 : 32774)))));
    }
    
    private static int func_148107_b(final String p_148107_0_) {
        String s = p_148107_0_.trim().toLowerCase();
        s = s.replaceAll("_", "");
        s = s.replaceAll("one", "1");
        s = s.replaceAll("zero", "0");
        s = s.replaceAll("minus", "-");
        return s.equals("0") ? 0 : (s.equals("1") ? 1 : (s.equals("srccolor") ? 768 : (s.equals("1-srccolor") ? 769 : (s.equals("dstcolor") ? 774 : (s.equals("1-dstcolor") ? 775 : (s.equals("srcalpha") ? 770 : (s.equals("1-srcalpha") ? 771 : (s.equals("dstalpha") ? 772 : (s.equals("1-dstalpha") ? 773 : -1)))))))));
    }
}
