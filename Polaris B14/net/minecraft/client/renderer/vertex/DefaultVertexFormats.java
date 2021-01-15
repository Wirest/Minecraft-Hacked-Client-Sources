/*    */ package net.minecraft.client.renderer.vertex;
/*    */ 
/*    */ public class DefaultVertexFormats
/*    */ {
/*  5 */   public static final VertexFormat BLOCK = new VertexFormat();
/*  6 */   public static final VertexFormat ITEM = new VertexFormat();
/*  7 */   public static final VertexFormat OLDMODEL_POSITION_TEX_NORMAL = new VertexFormat();
/*  8 */   public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
/*  9 */   public static final VertexFormat POSITION = new VertexFormat();
/* 10 */   public static final VertexFormat POSITION_COLOR = new VertexFormat();
/* 11 */   public static final VertexFormat POSITION_TEX = new VertexFormat();
/* 12 */   public static final VertexFormat POSITION_NORMAL = new VertexFormat();
/* 13 */   public static final VertexFormat POSITION_TEX_COLOR = new VertexFormat();
/* 14 */   public static final VertexFormat POSITION_TEX_NORMAL = new VertexFormat();
/* 15 */   public static final VertexFormat POSITION_TEX_LMAP_COLOR = new VertexFormat();
/* 16 */   public static final VertexFormat POSITION_TEX_COLOR_NORMAL = new VertexFormat();
/* 17 */   public static final VertexFormatElement POSITION_3F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
/* 18 */   public static final VertexFormatElement COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
/* 19 */   public static final VertexFormatElement TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
/* 20 */   public static final VertexFormatElement TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
/* 21 */   public static final VertexFormatElement NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
/* 22 */   public static final VertexFormatElement PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);
/*    */   
/*    */   static
/*    */   {
/* 26 */     BLOCK.func_181721_a(POSITION_3F);
/* 27 */     BLOCK.func_181721_a(COLOR_4UB);
/* 28 */     BLOCK.func_181721_a(TEX_2F);
/* 29 */     BLOCK.func_181721_a(TEX_2S);
/* 30 */     ITEM.func_181721_a(POSITION_3F);
/* 31 */     ITEM.func_181721_a(COLOR_4UB);
/* 32 */     ITEM.func_181721_a(TEX_2F);
/* 33 */     ITEM.func_181721_a(NORMAL_3B);
/* 34 */     ITEM.func_181721_a(PADDING_1B);
/* 35 */     OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(POSITION_3F);
/* 36 */     OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(TEX_2F);
/* 37 */     OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(NORMAL_3B);
/* 38 */     OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(PADDING_1B);
/* 39 */     PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(POSITION_3F);
/* 40 */     PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2F);
/* 41 */     PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(COLOR_4UB);
/* 42 */     PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2S);
/* 43 */     POSITION.func_181721_a(POSITION_3F);
/* 44 */     POSITION_COLOR.func_181721_a(POSITION_3F);
/* 45 */     POSITION_COLOR.func_181721_a(COLOR_4UB);
/* 46 */     POSITION_TEX.func_181721_a(POSITION_3F);
/* 47 */     POSITION_TEX.func_181721_a(TEX_2F);
/* 48 */     POSITION_NORMAL.func_181721_a(POSITION_3F);
/* 49 */     POSITION_NORMAL.func_181721_a(NORMAL_3B);
/* 50 */     POSITION_NORMAL.func_181721_a(PADDING_1B);
/* 51 */     POSITION_TEX_COLOR.func_181721_a(POSITION_3F);
/* 52 */     POSITION_TEX_COLOR.func_181721_a(TEX_2F);
/* 53 */     POSITION_TEX_COLOR.func_181721_a(COLOR_4UB);
/* 54 */     POSITION_TEX_NORMAL.func_181721_a(POSITION_3F);
/* 55 */     POSITION_TEX_NORMAL.func_181721_a(TEX_2F);
/* 56 */     POSITION_TEX_NORMAL.func_181721_a(NORMAL_3B);
/* 57 */     POSITION_TEX_NORMAL.func_181721_a(PADDING_1B);
/* 58 */     POSITION_TEX_LMAP_COLOR.func_181721_a(POSITION_3F);
/* 59 */     POSITION_TEX_LMAP_COLOR.func_181721_a(TEX_2F);
/* 60 */     POSITION_TEX_LMAP_COLOR.func_181721_a(TEX_2S);
/* 61 */     POSITION_TEX_LMAP_COLOR.func_181721_a(COLOR_4UB);
/* 62 */     POSITION_TEX_COLOR_NORMAL.func_181721_a(POSITION_3F);
/* 63 */     POSITION_TEX_COLOR_NORMAL.func_181721_a(TEX_2F);
/* 64 */     POSITION_TEX_COLOR_NORMAL.func_181721_a(COLOR_4UB);
/* 65 */     POSITION_TEX_COLOR_NORMAL.func_181721_a(NORMAL_3B);
/* 66 */     POSITION_TEX_COLOR_NORMAL.func_181721_a(PADDING_1B);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\vertex\DefaultVertexFormats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */