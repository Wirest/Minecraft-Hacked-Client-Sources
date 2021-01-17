// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.vertex;

import optifine.Reflector;
import shadersmod.client.SVertexFormat;
import optifine.Config;

public class DefaultVertexFormats
{
    public static VertexFormat BLOCK;
    public static VertexFormat ITEM;
    private static final VertexFormat BLOCK_VANILLA;
    private static final VertexFormat ITEM_VANILLA;
    public static final VertexFormat OLDMODEL_POSITION_TEX_NORMAL;
    public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP;
    public static final VertexFormat POSITION;
    public static final VertexFormat POSITION_COLOR;
    public static final VertexFormat POSITION_TEX;
    public static final VertexFormat POSITION_NORMAL;
    public static final VertexFormat POSITION_TEX_COLOR;
    public static final VertexFormat POSITION_TEX_NORMAL;
    public static final VertexFormat POSITION_TEX_LMAP_COLOR;
    public static final VertexFormat POSITION_TEX_COLOR_NORMAL;
    public static final VertexFormatElement POSITION_3F;
    public static final VertexFormatElement COLOR_4UB;
    public static final VertexFormatElement TEX_2F;
    public static final VertexFormatElement TEX_2S;
    public static final VertexFormatElement NORMAL_3B;
    public static final VertexFormatElement PADDING_1B;
    private static final String __OBFID = "CL_00002403";
    
    static {
        DefaultVertexFormats.BLOCK = new VertexFormat();
        DefaultVertexFormats.ITEM = new VertexFormat();
        BLOCK_VANILLA = DefaultVertexFormats.BLOCK;
        ITEM_VANILLA = DefaultVertexFormats.ITEM;
        OLDMODEL_POSITION_TEX_NORMAL = new VertexFormat();
        PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
        POSITION = new VertexFormat();
        POSITION_COLOR = new VertexFormat();
        POSITION_TEX = new VertexFormat();
        POSITION_NORMAL = new VertexFormat();
        POSITION_TEX_COLOR = new VertexFormat();
        POSITION_TEX_NORMAL = new VertexFormat();
        POSITION_TEX_LMAP_COLOR = new VertexFormat();
        POSITION_TEX_COLOR_NORMAL = new VertexFormat();
        POSITION_3F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
        COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
        TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
        TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
        NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
        PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);
        DefaultVertexFormats.BLOCK.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.BLOCK.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.BLOCK.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.BLOCK.func_181721_a(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.ITEM.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.ITEM.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.ITEM.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.ITEM.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.ITEM.func_181721_a(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.POSITION.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_COLOR.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_COLOR.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_NORMAL.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_NORMAL.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_NORMAL.func_181721_a(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.POSITION_TEX_COLOR.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_COLOR.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_COLOR.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_TEX_NORMAL.func_181721_a(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.func_181721_a(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.func_181721_a(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.func_181721_a(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.func_181721_a(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.func_181721_a(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.func_181721_a(DefaultVertexFormats.PADDING_1B);
    }
    
    public static void updateVertexFormats() {
        if (Config.isShaders()) {
            DefaultVertexFormats.BLOCK = SVertexFormat.makeDefVertexFormatBlock();
            DefaultVertexFormats.ITEM = SVertexFormat.makeDefVertexFormatItem();
        }
        else {
            DefaultVertexFormats.BLOCK = DefaultVertexFormats.BLOCK_VANILLA;
            DefaultVertexFormats.ITEM = DefaultVertexFormats.ITEM_VANILLA;
        }
        if (Reflector.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
            final VertexFormat vertexformat = DefaultVertexFormats.ITEM;
            final VertexFormat vertexformat2 = (VertexFormat)Reflector.getFieldValue(Reflector.Attributes_DEFAULT_BAKED_FORMAT);
            vertexformat2.clear();
            for (int i = 0; i < vertexformat.getElementCount(); ++i) {
                vertexformat2.func_181721_a(vertexformat.getElement(i));
            }
        }
    }
}
