package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat
{
    private static final Logger field_177357_a = LogManager.getLogger();
    private final List field_177355_b;
    private final List field_177356_c;
    private int field_177353_d;
    private int field_177354_e;
    private List field_177351_f;
    private int field_177352_g;
    private static final String __OBFID = "CL_00002401";

    public VertexFormat(VertexFormat p_i46097_1_)
    {
        this();

        for (int var2 = 0; var2 < p_i46097_1_.func_177345_h(); ++var2)
        {
            this.func_177349_a(p_i46097_1_.func_177348_c(var2));
        }

        this.field_177353_d = p_i46097_1_.func_177338_f();
    }

    public VertexFormat()
    {
        this.field_177355_b = Lists.newArrayList();
        this.field_177356_c = Lists.newArrayList();
        this.field_177353_d = 0;
        this.field_177354_e = -1;
        this.field_177351_f = Lists.newArrayList();
        this.field_177352_g = -1;
    }

    public void clear()
    {
        this.field_177355_b.clear();
        this.field_177356_c.clear();
        this.field_177354_e = -1;
        this.field_177351_f.clear();
        this.field_177352_g = -1;
        this.field_177353_d = 0;
    }

    public void func_177349_a(VertexFormatElement p_177349_1_)
    {
        if (p_177349_1_.func_177374_g() && this.func_177341_i())
        {
            field_177357_a.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
        }
        else
        {
            this.field_177355_b.add(p_177349_1_);
            this.field_177356_c.add(Integer.valueOf(this.field_177353_d));
            p_177349_1_.func_177371_a(this.field_177353_d);
            this.field_177353_d += p_177349_1_.func_177368_f();

            switch (VertexFormat.SwitchEnumUseage.field_177382_a[p_177349_1_.func_177375_c().ordinal()])
            {
                case 1:
                    this.field_177352_g = p_177349_1_.func_177373_a();
                    break;

                case 2:
                    this.field_177354_e = p_177349_1_.func_177373_a();
                    break;

                case 3:
                    this.field_177351_f.add(p_177349_1_.func_177369_e(), Integer.valueOf(p_177349_1_.func_177373_a()));
            }
        }
    }

    public boolean func_177350_b()
    {
        return this.field_177352_g >= 0;
    }

    public int func_177342_c()
    {
        return this.field_177352_g;
    }

    public boolean func_177346_d()
    {
        return this.field_177354_e >= 0;
    }

    public int func_177340_e()
    {
        return this.field_177354_e;
    }

    public boolean func_177347_a(int p_177347_1_)
    {
        return this.field_177351_f.size() - 1 >= p_177347_1_;
    }

    public int func_177344_b(int p_177344_1_)
    {
        return ((Integer)this.field_177351_f.get(p_177344_1_)).intValue();
    }

    public String toString()
    {
        String var1 = "format: " + this.field_177355_b.size() + " elements: ";

        for (int var2 = 0; var2 < this.field_177355_b.size(); ++var2)
        {
            var1 = var1 + ((VertexFormatElement)this.field_177355_b.get(var2)).toString();

            if (var2 != this.field_177355_b.size() - 1)
            {
                var1 = var1 + " ";
            }
        }

        return var1;
    }

    private boolean func_177341_i()
    {
        Iterator var1 = this.field_177355_b.iterator();
        VertexFormatElement var2;

        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            var2 = (VertexFormatElement)var1.next();
        }
        while (!var2.func_177374_g());

        return true;
    }

    public int func_177338_f()
    {
        return this.field_177353_d;
    }

    public List func_177343_g()
    {
        return this.field_177355_b;
    }

    public int func_177345_h()
    {
        return this.field_177355_b.size();
    }

    public VertexFormatElement func_177348_c(int p_177348_1_)
    {
        return (VertexFormatElement)this.field_177355_b.get(p_177348_1_);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            VertexFormat var2 = (VertexFormat)p_equals_1_;
            return this.field_177353_d != var2.field_177353_d ? false : (!this.field_177355_b.equals(var2.field_177355_b) ? false : this.field_177356_c.equals(var2.field_177356_c));
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int var1 = this.field_177355_b.hashCode();
        var1 = 31 * var1 + this.field_177356_c.hashCode();
        var1 = 31 * var1 + this.field_177353_d;
        return var1;
    }

    static final class SwitchEnumUseage
    {
        static final int[] field_177382_a = new int[VertexFormatElement.EnumUseage.values().length];
        private static final String __OBFID = "CL_00002400";

        static
        {
            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.NORMAL.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177382_a[VertexFormatElement.EnumUseage.UV.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
