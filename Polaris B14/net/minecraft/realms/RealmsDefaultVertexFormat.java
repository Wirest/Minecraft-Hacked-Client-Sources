/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*    */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*    */ 
/*    */ public class RealmsDefaultVertexFormat
/*    */ {
/*  8 */   public static final RealmsVertexFormat BLOCK = new RealmsVertexFormat(new VertexFormat());
/*  9 */   public static final RealmsVertexFormat BLOCK_NORMALS = new RealmsVertexFormat(new VertexFormat());
/* 10 */   public static final RealmsVertexFormat ENTITY = new RealmsVertexFormat(new VertexFormat());
/* 11 */   public static final RealmsVertexFormat PARTICLE = new RealmsVertexFormat(new VertexFormat());
/* 12 */   public static final RealmsVertexFormat POSITION = new RealmsVertexFormat(new VertexFormat());
/* 13 */   public static final RealmsVertexFormat POSITION_COLOR = new RealmsVertexFormat(new VertexFormat());
/* 14 */   public static final RealmsVertexFormat POSITION_TEX = new RealmsVertexFormat(new VertexFormat());
/* 15 */   public static final RealmsVertexFormat POSITION_NORMAL = new RealmsVertexFormat(new VertexFormat());
/* 16 */   public static final RealmsVertexFormat POSITION_TEX_COLOR = new RealmsVertexFormat(new VertexFormat());
/* 17 */   public static final RealmsVertexFormat POSITION_TEX_NORMAL = new RealmsVertexFormat(new VertexFormat());
/* 18 */   public static final RealmsVertexFormat POSITION_TEX2_COLOR = new RealmsVertexFormat(new VertexFormat());
/* 19 */   public static final RealmsVertexFormat POSITION_TEX_COLOR_NORMAL = new RealmsVertexFormat(new VertexFormat());
/* 20 */   public static final RealmsVertexFormatElement ELEMENT_POSITION = new RealmsVertexFormatElement(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.FLOAT, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.POSITION, 3));
/* 21 */   public static final RealmsVertexFormatElement ELEMENT_COLOR = new RealmsVertexFormatElement(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.UBYTE, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.COLOR, 4));
/* 22 */   public static final RealmsVertexFormatElement ELEMENT_UV0 = new RealmsVertexFormatElement(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.FLOAT, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.UV, 2));
/* 23 */   public static final RealmsVertexFormatElement ELEMENT_UV1 = new RealmsVertexFormatElement(new VertexFormatElement(1, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.SHORT, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.UV, 2));
/* 24 */   public static final RealmsVertexFormatElement ELEMENT_NORMAL = new RealmsVertexFormatElement(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.BYTE, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.NORMAL, 3));
/* 25 */   public static final RealmsVertexFormatElement ELEMENT_PADDING = new RealmsVertexFormatElement(new VertexFormatElement(0, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType.BYTE, net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage.PADDING, 1));
/*    */   
/*    */   static
/*    */   {
/* 29 */     BLOCK.addElement(ELEMENT_POSITION);
/* 30 */     BLOCK.addElement(ELEMENT_COLOR);
/* 31 */     BLOCK.addElement(ELEMENT_UV0);
/* 32 */     BLOCK.addElement(ELEMENT_UV1);
/* 33 */     BLOCK_NORMALS.addElement(ELEMENT_POSITION);
/* 34 */     BLOCK_NORMALS.addElement(ELEMENT_COLOR);
/* 35 */     BLOCK_NORMALS.addElement(ELEMENT_UV0);
/* 36 */     BLOCK_NORMALS.addElement(ELEMENT_NORMAL);
/* 37 */     BLOCK_NORMALS.addElement(ELEMENT_PADDING);
/* 38 */     ENTITY.addElement(ELEMENT_POSITION);
/* 39 */     ENTITY.addElement(ELEMENT_UV0);
/* 40 */     ENTITY.addElement(ELEMENT_NORMAL);
/* 41 */     ENTITY.addElement(ELEMENT_PADDING);
/* 42 */     PARTICLE.addElement(ELEMENT_POSITION);
/* 43 */     PARTICLE.addElement(ELEMENT_UV0);
/* 44 */     PARTICLE.addElement(ELEMENT_COLOR);
/* 45 */     PARTICLE.addElement(ELEMENT_UV1);
/* 46 */     POSITION.addElement(ELEMENT_POSITION);
/* 47 */     POSITION_COLOR.addElement(ELEMENT_POSITION);
/* 48 */     POSITION_COLOR.addElement(ELEMENT_COLOR);
/* 49 */     POSITION_TEX.addElement(ELEMENT_POSITION);
/* 50 */     POSITION_TEX.addElement(ELEMENT_UV0);
/* 51 */     POSITION_NORMAL.addElement(ELEMENT_POSITION);
/* 52 */     POSITION_NORMAL.addElement(ELEMENT_NORMAL);
/* 53 */     POSITION_NORMAL.addElement(ELEMENT_PADDING);
/* 54 */     POSITION_TEX_COLOR.addElement(ELEMENT_POSITION);
/* 55 */     POSITION_TEX_COLOR.addElement(ELEMENT_UV0);
/* 56 */     POSITION_TEX_COLOR.addElement(ELEMENT_COLOR);
/* 57 */     POSITION_TEX_NORMAL.addElement(ELEMENT_POSITION);
/* 58 */     POSITION_TEX_NORMAL.addElement(ELEMENT_UV0);
/* 59 */     POSITION_TEX_NORMAL.addElement(ELEMENT_NORMAL);
/* 60 */     POSITION_TEX_NORMAL.addElement(ELEMENT_PADDING);
/* 61 */     POSITION_TEX2_COLOR.addElement(ELEMENT_POSITION);
/* 62 */     POSITION_TEX2_COLOR.addElement(ELEMENT_UV0);
/* 63 */     POSITION_TEX2_COLOR.addElement(ELEMENT_UV1);
/* 64 */     POSITION_TEX2_COLOR.addElement(ELEMENT_COLOR);
/* 65 */     POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_POSITION);
/* 66 */     POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_UV0);
/* 67 */     POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_COLOR);
/* 68 */     POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_NORMAL);
/* 69 */     POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_PADDING);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\RealmsDefaultVertexFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */