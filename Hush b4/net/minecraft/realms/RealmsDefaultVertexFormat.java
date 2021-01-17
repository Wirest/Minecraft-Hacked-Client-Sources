// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class RealmsDefaultVertexFormat
{
    public static final RealmsVertexFormat BLOCK;
    public static final RealmsVertexFormat BLOCK_NORMALS;
    public static final RealmsVertexFormat ENTITY;
    public static final RealmsVertexFormat PARTICLE;
    public static final RealmsVertexFormat POSITION;
    public static final RealmsVertexFormat POSITION_COLOR;
    public static final RealmsVertexFormat POSITION_TEX;
    public static final RealmsVertexFormat POSITION_NORMAL;
    public static final RealmsVertexFormat POSITION_TEX_COLOR;
    public static final RealmsVertexFormat POSITION_TEX_NORMAL;
    public static final RealmsVertexFormat POSITION_TEX2_COLOR;
    public static final RealmsVertexFormat POSITION_TEX_COLOR_NORMAL;
    public static final RealmsVertexFormatElement ELEMENT_POSITION;
    public static final RealmsVertexFormatElement ELEMENT_COLOR;
    public static final RealmsVertexFormatElement ELEMENT_UV0;
    public static final RealmsVertexFormatElement ELEMENT_UV1;
    public static final RealmsVertexFormatElement ELEMENT_NORMAL;
    public static final RealmsVertexFormatElement ELEMENT_PADDING;
    
    static {
        BLOCK = new RealmsVertexFormat(new VertexFormat());
        BLOCK_NORMALS = new RealmsVertexFormat(new VertexFormat());
        ENTITY = new RealmsVertexFormat(new VertexFormat());
        PARTICLE = new RealmsVertexFormat(new VertexFormat());
        POSITION = new RealmsVertexFormat(new VertexFormat());
        POSITION_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX = new RealmsVertexFormat(new VertexFormat());
        POSITION_NORMAL = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_NORMAL = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX2_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_COLOR_NORMAL = new RealmsVertexFormat(new VertexFormat());
        ELEMENT_POSITION = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        ELEMENT_COLOR = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
        ELEMENT_UV0 = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
        ELEMENT_UV1 = new RealmsVertexFormatElement(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2));
        ELEMENT_NORMAL = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
        ELEMENT_PADDING = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
        RealmsDefaultVertexFormat.BLOCK.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.BLOCK.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.BLOCK.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.BLOCK.addElement(RealmsDefaultVertexFormat.ELEMENT_UV1);
        RealmsDefaultVertexFormat.BLOCK_NORMALS.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.BLOCK_NORMALS.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.BLOCK_NORMALS.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.BLOCK_NORMALS.addElement(RealmsDefaultVertexFormat.ELEMENT_NORMAL);
        RealmsDefaultVertexFormat.BLOCK_NORMALS.addElement(RealmsDefaultVertexFormat.ELEMENT_PADDING);
        RealmsDefaultVertexFormat.ENTITY.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.ENTITY.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.ENTITY.addElement(RealmsDefaultVertexFormat.ELEMENT_NORMAL);
        RealmsDefaultVertexFormat.ENTITY.addElement(RealmsDefaultVertexFormat.ELEMENT_PADDING);
        RealmsDefaultVertexFormat.PARTICLE.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.PARTICLE.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.PARTICLE.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.PARTICLE.addElement(RealmsDefaultVertexFormat.ELEMENT_UV1);
        RealmsDefaultVertexFormat.POSITION.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.POSITION_TEX.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_TEX.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.POSITION_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_NORMAL);
        RealmsDefaultVertexFormat.POSITION_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_PADDING);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.POSITION_TEX_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_TEX_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.POSITION_TEX_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_NORMAL);
        RealmsDefaultVertexFormat.POSITION_TEX_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_PADDING);
        RealmsDefaultVertexFormat.POSITION_TEX2_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_TEX2_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.POSITION_TEX2_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_UV1);
        RealmsDefaultVertexFormat.POSITION_TEX2_COLOR.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_POSITION);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_UV0);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_COLOR);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_NORMAL);
        RealmsDefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.addElement(RealmsDefaultVertexFormat.ELEMENT_PADDING);
    }
}
