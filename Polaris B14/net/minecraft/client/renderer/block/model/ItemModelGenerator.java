/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ 
/*     */ public class ItemModelGenerator
/*     */ {
/*  15 */   public static final List<String> LAYERS = Lists.newArrayList(new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });
/*     */   
/*     */   public ModelBlock makeItemModel(TextureMap textureMapIn, ModelBlock blockModel)
/*     */   {
/*  19 */     Map<String, String> map = Maps.newHashMap();
/*  20 */     List<BlockPart> list = Lists.newArrayList();
/*     */     
/*  22 */     for (int i = 0; i < LAYERS.size(); i++)
/*     */     {
/*  24 */       String s = (String)LAYERS.get(i);
/*     */       
/*  26 */       if (!blockModel.isTexturePresent(s)) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/*  31 */       String s1 = blockModel.resolveTextureName(s);
/*  32 */       map.put(s, s1);
/*  33 */       TextureAtlasSprite textureatlassprite = textureMapIn.getAtlasSprite(new ResourceLocation(s1).toString());
/*  34 */       list.addAll(func_178394_a(i, s, textureatlassprite));
/*     */     }
/*     */     
/*  37 */     if (list.isEmpty())
/*     */     {
/*  39 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  43 */     map.put("particle", blockModel.isTexturePresent("particle") ? blockModel.resolveTextureName("particle") : (String)map.get("layer0"));
/*  44 */     return new ModelBlock(list, map, false, false, blockModel.func_181682_g());
/*     */   }
/*     */   
/*     */ 
/*     */   private List<BlockPart> func_178394_a(int p_178394_1_, String p_178394_2_, TextureAtlasSprite p_178394_3_)
/*     */   {
/*  50 */     Map<EnumFacing, BlockPartFace> map = Maps.newHashMap();
/*  51 */     map.put(EnumFacing.SOUTH, new BlockPartFace(null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0)));
/*  52 */     map.put(EnumFacing.NORTH, new BlockPartFace(null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0)));
/*  53 */     List<BlockPart> list = Lists.newArrayList();
/*  54 */     list.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), map, null, true));
/*  55 */     list.addAll(func_178397_a(p_178394_3_, p_178394_2_, p_178394_1_));
/*  56 */     return list;
/*     */   }
/*     */   
/*     */   private List<BlockPart> func_178397_a(TextureAtlasSprite p_178397_1_, String p_178397_2_, int p_178397_3_)
/*     */   {
/*  61 */     float f = p_178397_1_.getIconWidth();
/*  62 */     float f1 = p_178397_1_.getIconHeight();
/*  63 */     List<BlockPart> list = Lists.newArrayList();
/*     */     
/*  65 */     for (Span itemmodelgenerator$span : func_178393_a(p_178397_1_))
/*     */     {
/*  67 */       float f2 = 0.0F;
/*  68 */       float f3 = 0.0F;
/*  69 */       float f4 = 0.0F;
/*  70 */       float f5 = 0.0F;
/*  71 */       float f6 = 0.0F;
/*  72 */       float f7 = 0.0F;
/*  73 */       float f8 = 0.0F;
/*  74 */       float f9 = 0.0F;
/*  75 */       float f10 = 0.0F;
/*  76 */       float f11 = 0.0F;
/*  77 */       float f12 = itemmodelgenerator$span.func_178385_b();
/*  78 */       float f13 = itemmodelgenerator$span.func_178384_c();
/*  79 */       float f14 = itemmodelgenerator$span.func_178381_d();
/*  80 */       SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.func_178383_a();
/*     */       
/*  82 */       switch (itemmodelgenerator$spanfacing)
/*     */       {
/*     */       case DOWN: 
/*  85 */         f6 = f12;
/*  86 */         f2 = f12;
/*  87 */         f4 = f7 = f13 + 1.0F;
/*  88 */         f8 = f14;
/*  89 */         f3 = f14;
/*  90 */         f9 = f14;
/*  91 */         f5 = f14;
/*  92 */         f10 = 16.0F / f;
/*  93 */         f11 = 16.0F / (f1 - 1.0F);
/*  94 */         break;
/*     */       
/*     */       case LEFT: 
/*  97 */         f9 = f14;
/*  98 */         f8 = f14;
/*  99 */         f6 = f12;
/* 100 */         f2 = f12;
/* 101 */         f4 = f7 = f13 + 1.0F;
/* 102 */         f3 = f14 + 1.0F;
/* 103 */         f5 = f14 + 1.0F;
/* 104 */         f10 = 16.0F / f;
/* 105 */         f11 = 16.0F / (f1 - 1.0F);
/* 106 */         break;
/*     */       
/*     */       case RIGHT: 
/* 109 */         f6 = f14;
/* 110 */         f2 = f14;
/* 111 */         f7 = f14;
/* 112 */         f4 = f14;
/* 113 */         f9 = f12;
/* 114 */         f3 = f12;
/* 115 */         f5 = f8 = f13 + 1.0F;
/* 116 */         f10 = 16.0F / (f - 1.0F);
/* 117 */         f11 = 16.0F / f1;
/* 118 */         break;
/*     */       
/*     */       case UP: 
/* 121 */         f7 = f14;
/* 122 */         f6 = f14;
/* 123 */         f2 = f14 + 1.0F;
/* 124 */         f4 = f14 + 1.0F;
/* 125 */         f9 = f12;
/* 126 */         f3 = f12;
/* 127 */         f5 = f8 = f13 + 1.0F;
/* 128 */         f10 = 16.0F / (f - 1.0F);
/* 129 */         f11 = 16.0F / f1;
/*     */       }
/*     */       
/* 132 */       float f15 = 16.0F / f;
/* 133 */       float f16 = 16.0F / f1;
/* 134 */       f2 *= f15;
/* 135 */       f4 *= f15;
/* 136 */       f3 *= f16;
/* 137 */       f5 *= f16;
/* 138 */       f3 = 16.0F - f3;
/* 139 */       f5 = 16.0F - f5;
/* 140 */       f6 *= f10;
/* 141 */       f7 *= f10;
/* 142 */       f8 *= f11;
/* 143 */       f9 *= f11;
/* 144 */       Map<EnumFacing, BlockPartFace> map = Maps.newHashMap();
/* 145 */       map.put(itemmodelgenerator$spanfacing.getFacing(), new BlockPartFace(null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[] { f6, f8, f7, f9 }, 0)));
/*     */       
/* 147 */       switch (itemmodelgenerator$spanfacing)
/*     */       {
/*     */       case DOWN: 
/* 150 */         list.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f4, f3, 8.5F), map, null, true));
/* 151 */         break;
/*     */       
/*     */       case LEFT: 
/* 154 */         list.add(new BlockPart(new Vector3f(f2, f5, 7.5F), new Vector3f(f4, f5, 8.5F), map, null, true));
/* 155 */         break;
/*     */       
/*     */       case RIGHT: 
/* 158 */         list.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f2, f5, 8.5F), map, null, true));
/* 159 */         break;
/*     */       
/*     */       case UP: 
/* 162 */         list.add(new BlockPart(new Vector3f(f4, f3, 7.5F), new Vector3f(f4, f5, 8.5F), map, null, true));
/*     */       }
/*     */       
/*     */     }
/* 166 */     return list;
/*     */   }
/*     */   
/*     */   private List<Span> func_178393_a(TextureAtlasSprite p_178393_1_)
/*     */   {
/* 171 */     int i = p_178393_1_.getIconWidth();
/* 172 */     int j = p_178393_1_.getIconHeight();
/* 173 */     List<Span> list = Lists.newArrayList();
/*     */     
/* 175 */     for (int k = 0; k < p_178393_1_.getFrameCount(); k++)
/*     */     {
/* 177 */       int[] aint = p_178393_1_.getFrameTextureData(k)[0];
/*     */       
/* 179 */       for (int l = 0; l < j; l++)
/*     */       {
/* 181 */         for (int i1 = 0; i1 < i; i1++)
/*     */         {
/* 183 */           boolean flag = !func_178391_a(aint, i1, l, i, j);
/* 184 */           func_178396_a(SpanFacing.UP, list, aint, i1, l, i, j, flag);
/* 185 */           func_178396_a(SpanFacing.DOWN, list, aint, i1, l, i, j, flag);
/* 186 */           func_178396_a(SpanFacing.LEFT, list, aint, i1, l, i, j, flag);
/* 187 */           func_178396_a(SpanFacing.RIGHT, list, aint, i1, l, i, j, flag);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 192 */     return list;
/*     */   }
/*     */   
/*     */   private void func_178396_a(SpanFacing p_178396_1_, List<Span> p_178396_2_, int[] p_178396_3_, int p_178396_4_, int p_178396_5_, int p_178396_6_, int p_178396_7_, boolean p_178396_8_)
/*     */   {
/* 197 */     boolean flag = (func_178391_a(p_178396_3_, p_178396_4_ + p_178396_1_.func_178372_b(), p_178396_5_ + p_178396_1_.func_178371_c(), p_178396_6_, p_178396_7_)) && (p_178396_8_);
/*     */     
/* 199 */     if (flag)
/*     */     {
/* 201 */       func_178395_a(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178395_a(List<Span> p_178395_1_, SpanFacing p_178395_2_, int p_178395_3_, int p_178395_4_)
/*     */   {
/* 207 */     Span itemmodelgenerator$span = null;
/*     */     
/* 209 */     for (Span itemmodelgenerator$span1 : p_178395_1_)
/*     */     {
/* 211 */       if (itemmodelgenerator$span1.func_178383_a() == p_178395_2_)
/*     */       {
/* 213 */         int i = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;
/*     */         
/* 215 */         if (itemmodelgenerator$span1.func_178381_d() == i)
/*     */         {
/* 217 */           itemmodelgenerator$span = itemmodelgenerator$span1;
/* 218 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 223 */     int j = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;
/* 224 */     int k = p_178395_2_.func_178369_d() ? p_178395_3_ : p_178395_4_;
/*     */     
/* 226 */     if (itemmodelgenerator$span == null)
/*     */     {
/* 228 */       p_178395_1_.add(new Span(p_178395_2_, k, j));
/*     */     }
/*     */     else
/*     */     {
/* 232 */       itemmodelgenerator$span.func_178382_a(k);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean func_178391_a(int[] p_178391_1_, int p_178391_2_, int p_178391_3_, int p_178391_4_, int p_178391_5_)
/*     */   {
/* 238 */     return (p_178391_1_[(p_178391_3_ * p_178391_4_ + p_178391_2_)] >> 24 & 0xFF) == 0;
/*     */   }
/*     */   
/*     */   static class Span
/*     */   {
/*     */     private final ItemModelGenerator.SpanFacing spanFacing;
/*     */     private int field_178387_b;
/*     */     private int field_178388_c;
/*     */     private final int field_178386_d;
/*     */     
/*     */     public Span(ItemModelGenerator.SpanFacing spanFacingIn, int p_i46216_2_, int p_i46216_3_)
/*     */     {
/* 250 */       this.spanFacing = spanFacingIn;
/* 251 */       this.field_178387_b = p_i46216_2_;
/* 252 */       this.field_178388_c = p_i46216_2_;
/* 253 */       this.field_178386_d = p_i46216_3_;
/*     */     }
/*     */     
/*     */     public void func_178382_a(int p_178382_1_)
/*     */     {
/* 258 */       if (p_178382_1_ < this.field_178387_b)
/*     */       {
/* 260 */         this.field_178387_b = p_178382_1_;
/*     */       }
/* 262 */       else if (p_178382_1_ > this.field_178388_c)
/*     */       {
/* 264 */         this.field_178388_c = p_178382_1_;
/*     */       }
/*     */     }
/*     */     
/*     */     public ItemModelGenerator.SpanFacing func_178383_a()
/*     */     {
/* 270 */       return this.spanFacing;
/*     */     }
/*     */     
/*     */     public int func_178385_b()
/*     */     {
/* 275 */       return this.field_178387_b;
/*     */     }
/*     */     
/*     */     public int func_178384_c()
/*     */     {
/* 280 */       return this.field_178388_c;
/*     */     }
/*     */     
/*     */     public int func_178381_d()
/*     */     {
/* 285 */       return this.field_178386_d;
/*     */     }
/*     */   }
/*     */   
/*     */   static enum SpanFacing
/*     */   {
/* 291 */     UP(EnumFacing.UP, 0, -1), 
/* 292 */     DOWN(EnumFacing.DOWN, 0, 1), 
/* 293 */     LEFT(EnumFacing.EAST, -1, 0), 
/* 294 */     RIGHT(EnumFacing.WEST, 1, 0);
/*     */     
/*     */     private final EnumFacing facing;
/*     */     private final int field_178373_f;
/*     */     private final int field_178374_g;
/*     */     
/*     */     private SpanFacing(EnumFacing facing, int p_i46215_4_, int p_i46215_5_)
/*     */     {
/* 302 */       this.facing = facing;
/* 303 */       this.field_178373_f = p_i46215_4_;
/* 304 */       this.field_178374_g = p_i46215_5_;
/*     */     }
/*     */     
/*     */     public EnumFacing getFacing()
/*     */     {
/* 309 */       return this.facing;
/*     */     }
/*     */     
/*     */     public int func_178372_b()
/*     */     {
/* 314 */       return this.field_178373_f;
/*     */     }
/*     */     
/*     */     public int func_178371_c()
/*     */     {
/* 319 */       return this.field_178374_g;
/*     */     }
/*     */     
/*     */     private boolean func_178369_d()
/*     */     {
/* 324 */       return (this == DOWN) || (this == UP);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\ItemModelGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */