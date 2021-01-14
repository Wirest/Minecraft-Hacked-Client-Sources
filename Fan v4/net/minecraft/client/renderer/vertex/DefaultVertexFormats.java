package net.minecraft.client.renderer.vertex;

import java.lang.reflect.Field;
import net.minecraft.src.Config;
import net.optifine.reflect.ReflectorClass;
import net.optifine.reflect.ReflectorField;
import net.optifine.shaders.SVertexFormat;

public class DefaultVertexFormats
{
    public static VertexFormat BLOCK = new VertexFormat();
    public static VertexFormat ITEM = new VertexFormat();
    private static final VertexFormat BLOCK_VANILLA = BLOCK;
    private static final VertexFormat ITEM_VANILLA = ITEM;
    public static ReflectorClass Attributes = new ReflectorClass("net.minecraftforge.client.model.Attributes");
    public static ReflectorField Attributes_DEFAULT_BAKED_FORMAT = new ReflectorField(Attributes, "DEFAULT_BAKED_FORMAT");
    private static final VertexFormat FORGE_BAKED = SVertexFormat.duplicate((VertexFormat)getFieldValue(Attributes_DEFAULT_BAKED_FORMAT));
    public static final VertexFormat field_181703_c = new VertexFormat();
    public static final VertexFormat field_181704_d = new VertexFormat();
    public static final VertexFormat field_181705_e = new VertexFormat();
    public static final VertexFormat field_181706_f = new VertexFormat();
    public static final VertexFormat field_181707_g = new VertexFormat();
    public static final VertexFormat field_181708_h = new VertexFormat();
    public static final VertexFormat field_181709_i = new VertexFormat();
    public static final VertexFormat field_181710_j = new VertexFormat();
    public static final VertexFormat field_181711_k = new VertexFormat();
    public static final VertexFormat field_181712_l = new VertexFormat();
    public static final VertexFormatElement field_181713_m = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
    public static final VertexFormatElement field_181714_n = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
    public static final VertexFormatElement field_181715_o = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
    public static final VertexFormatElement field_181716_p = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
    public static final VertexFormatElement field_181717_q = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
    public static final VertexFormatElement field_181718_r = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);

    public static void updateVertexFormats()
    {
        if (Config.isShaders())
        {
            BLOCK = SVertexFormat.makeDefVertexFormatBlock();
            ITEM = SVertexFormat.makeDefVertexFormatItem();

            if (Attributes_DEFAULT_BAKED_FORMAT.exists())
            {
                SVertexFormat.setDefBakedFormat((VertexFormat)Attributes_DEFAULT_BAKED_FORMAT.getValue());
            }
        }
        else
        {
            BLOCK = BLOCK_VANILLA;
            ITEM = ITEM_VANILLA;

            if (Attributes_DEFAULT_BAKED_FORMAT.exists())
            {
                SVertexFormat.copy(FORGE_BAKED, (VertexFormat)Attributes_DEFAULT_BAKED_FORMAT.getValue());
            }
        }
    }

    public static Object getFieldValue(ReflectorField p_getFieldValue_0_)
    {
        try
        {
            Field field = p_getFieldValue_0_.getTargetField();

            if (field == null)
            {
                return null;
            }
            else
            {
                Object object = field.get(null);
                return object;
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
            return null;
        }
    }

    static
    {
        BLOCK.func_181721_a(field_181713_m);
        BLOCK.func_181721_a(field_181714_n);
        BLOCK.func_181721_a(field_181715_o);
        BLOCK.func_181721_a(field_181716_p);
        ITEM.func_181721_a(field_181713_m);
        ITEM.func_181721_a(field_181714_n);
        ITEM.func_181721_a(field_181715_o);
        ITEM.func_181721_a(field_181717_q);
        ITEM.func_181721_a(field_181718_r);
        field_181703_c.func_181721_a(field_181713_m);
        field_181703_c.func_181721_a(field_181715_o);
        field_181703_c.func_181721_a(field_181717_q);
        field_181703_c.func_181721_a(field_181718_r);
        field_181704_d.func_181721_a(field_181713_m);
        field_181704_d.func_181721_a(field_181715_o);
        field_181704_d.func_181721_a(field_181714_n);
        field_181704_d.func_181721_a(field_181716_p);
        field_181705_e.func_181721_a(field_181713_m);
        field_181706_f.func_181721_a(field_181713_m);
        field_181706_f.func_181721_a(field_181714_n);
        field_181707_g.func_181721_a(field_181713_m);
        field_181707_g.func_181721_a(field_181715_o);
        field_181708_h.func_181721_a(field_181713_m);
        field_181708_h.func_181721_a(field_181717_q);
        field_181708_h.func_181721_a(field_181718_r);
        field_181709_i.func_181721_a(field_181713_m);
        field_181709_i.func_181721_a(field_181715_o);
        field_181709_i.func_181721_a(field_181714_n);
        field_181710_j.func_181721_a(field_181713_m);
        field_181710_j.func_181721_a(field_181715_o);
        field_181710_j.func_181721_a(field_181717_q);
        field_181710_j.func_181721_a(field_181718_r);
        field_181711_k.func_181721_a(field_181713_m);
        field_181711_k.func_181721_a(field_181715_o);
        field_181711_k.func_181721_a(field_181716_p);
        field_181711_k.func_181721_a(field_181714_n);
        field_181712_l.func_181721_a(field_181713_m);
        field_181712_l.func_181721_a(field_181715_o);
        field_181712_l.func_181721_a(field_181714_n);
        field_181712_l.func_181721_a(field_181717_q);
        field_181712_l.func_181721_a(field_181718_r);
    }
}
