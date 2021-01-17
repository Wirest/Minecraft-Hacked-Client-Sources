package net.minecraft.client.util;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.JsonUtils;
import org.lwjgl.opengl.GL14;

public class JsonBlendingMode
{
    private static JsonBlendingMode field_148118_a = null;
    private final int field_148116_b;
    private final int field_148117_c;
    private final int field_148114_d;
    private final int field_148115_e;
    private final int field_148112_f;
    private final boolean field_148113_g;
    private final boolean field_148119_h;
    private static final String __OBFID = "CL_00001038";

    private JsonBlendingMode(boolean p_i45084_1_, boolean p_i45084_2_, int p_i45084_3_, int p_i45084_4_, int p_i45084_5_, int p_i45084_6_, int p_i45084_7_)
    {
        this.field_148113_g = p_i45084_1_;
        this.field_148116_b = p_i45084_3_;
        this.field_148114_d = p_i45084_4_;
        this.field_148117_c = p_i45084_5_;
        this.field_148115_e = p_i45084_6_;
        this.field_148119_h = p_i45084_2_;
        this.field_148112_f = p_i45084_7_;
    }

    public JsonBlendingMode()
    {
        this(false, true, 1, 0, 1, 0, 32774);
    }

    public JsonBlendingMode(int p_i45085_1_, int p_i45085_2_, int p_i45085_3_)
    {
        this(false, false, p_i45085_1_, p_i45085_2_, p_i45085_1_, p_i45085_2_, p_i45085_3_);
    }

    public JsonBlendingMode(int p_i45086_1_, int p_i45086_2_, int p_i45086_3_, int p_i45086_4_, int p_i45086_5_)
    {
        this(true, false, p_i45086_1_, p_i45086_2_, p_i45086_3_, p_i45086_4_, p_i45086_5_);
    }

    public void func_148109_a()
    {
        if (!this.equals(field_148118_a))
        {
            if (field_148118_a == null || this.field_148119_h != field_148118_a.func_148111_b())
            {
                field_148118_a = this;

                if (this.field_148119_h)
                {
                    GlStateManager.disableBlend();
                    return;
                }

                GlStateManager.enableBlend();
            }

            GL14.glBlendEquation(this.field_148112_f);

            if (this.field_148113_g)
            {
                GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
            }
            else
            {
                GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
            }
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof JsonBlendingMode))
        {
            return false;
        }
        else
        {
            JsonBlendingMode var2 = (JsonBlendingMode)p_equals_1_;
            return this.field_148112_f != var2.field_148112_f ? false : (this.field_148115_e != var2.field_148115_e ? false : (this.field_148114_d != var2.field_148114_d ? false : (this.field_148119_h != var2.field_148119_h ? false : (this.field_148113_g != var2.field_148113_g ? false : (this.field_148117_c != var2.field_148117_c ? false : this.field_148116_b == var2.field_148116_b)))));
        }
    }

    public int hashCode()
    {
        int var1 = this.field_148116_b;
        var1 = 31 * var1 + this.field_148117_c;
        var1 = 31 * var1 + this.field_148114_d;
        var1 = 31 * var1 + this.field_148115_e;
        var1 = 31 * var1 + this.field_148112_f;
        var1 = 31 * var1 + (this.field_148113_g ? 1 : 0);
        var1 = 31 * var1 + (this.field_148119_h ? 1 : 0);
        return var1;
    }

    public boolean func_148111_b()
    {
        return this.field_148119_h;
    }

    public static JsonBlendingMode func_148110_a(JsonObject p_148110_0_)
    {
        if (p_148110_0_ == null)
        {
            return new JsonBlendingMode();
        }
        else
        {
            int var1 = 32774;
            int var2 = 1;
            int var3 = 0;
            int var4 = 1;
            int var5 = 0;
            boolean var6 = true;
            boolean var7 = false;

            if (JsonUtils.jsonObjectFieldTypeIsString(p_148110_0_, "func"))
            {
                var1 = func_148108_a(p_148110_0_.get("func").getAsString());

                if (var1 != 32774)
                {
                    var6 = false;
                }
            }

            if (JsonUtils.jsonObjectFieldTypeIsString(p_148110_0_, "srcrgb"))
            {
                var2 = func_148107_b(p_148110_0_.get("srcrgb").getAsString());

                if (var2 != 1)
                {
                    var6 = false;
                }
            }

            if (JsonUtils.jsonObjectFieldTypeIsString(p_148110_0_, "dstrgb"))
            {
                var3 = func_148107_b(p_148110_0_.get("dstrgb").getAsString());

                if (var3 != 0)
                {
                    var6 = false;
                }
            }

            if (JsonUtils.jsonObjectFieldTypeIsString(p_148110_0_, "srcalpha"))
            {
                var4 = func_148107_b(p_148110_0_.get("srcalpha").getAsString());

                if (var4 != 1)
                {
                    var6 = false;
                }

                var7 = true;
            }

            if (JsonUtils.jsonObjectFieldTypeIsString(p_148110_0_, "dstalpha"))
            {
                var5 = func_148107_b(p_148110_0_.get("dstalpha").getAsString());

                if (var5 != 0)
                {
                    var6 = false;
                }

                var7 = true;
            }

            return var6 ? new JsonBlendingMode() : (var7 ? new JsonBlendingMode(var2, var3, var4, var5, var1) : new JsonBlendingMode(var2, var3, var1));
        }
    }

    private static int func_148108_a(String p_148108_0_)
    {
        String var1 = p_148108_0_.trim().toLowerCase();
        return var1.equals("add") ? 32774 : (var1.equals("subtract") ? 32778 : (var1.equals("reversesubtract") ? 32779 : (var1.equals("reverse_subtract") ? 32779 : (var1.equals("min") ? 32775 : (var1.equals("max") ? 32776 : 32774)))));
    }

    private static int func_148107_b(String p_148107_0_)
    {
        String var1 = p_148107_0_.trim().toLowerCase();
        var1 = var1.replaceAll("_", "");
        var1 = var1.replaceAll("one", "1");
        var1 = var1.replaceAll("zero", "0");
        var1 = var1.replaceAll("minus", "-");
        return var1.equals("0") ? 0 : (var1.equals("1") ? 1 : (var1.equals("srccolor") ? 768 : (var1.equals("1-srccolor") ? 769 : (var1.equals("dstcolor") ? 774 : (var1.equals("1-dstcolor") ? 775 : (var1.equals("srcalpha") ? 770 : (var1.equals("1-srcalpha") ? 771 : (var1.equals("dstalpha") ? 772 : (var1.equals("1-dstalpha") ? 773 : -1)))))))));
    }
}
