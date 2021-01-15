/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import optfine.Config;
/*     */ import optfine.TextureUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class WorldRenderer
/*     */ {
/*     */   private ByteBuffer byteBuffer;
/*     */   private IntBuffer rawIntBuffer;
/*     */   private ShortBuffer field_181676_c;
/*     */   private FloatBuffer rawFloatBuffer;
/*     */   private int vertexCount;
/*     */   private VertexFormatElement field_181677_f;
/*     */   private int field_181678_g;
/*     */   private boolean needsUpdate;
/*     */   private int drawMode;
/*     */   private double xOffset;
/*     */   private double yOffset;
/*     */   private double zOffset;
/*     */   private VertexFormat vertexFormat;
/*     */   private boolean isDrawing;
/*     */   private static final String __OBFID = "CL_00000942";
/*  41 */   private EnumWorldBlockLayer blockLayer = null;
/*  42 */   private boolean[] drawnIcons = new boolean['Ā'];
/*  43 */   private TextureAtlasSprite[] quadSprites = null;
/*  44 */   private TextureAtlasSprite quadSprite = null;
/*     */   
/*     */   public WorldRenderer(int bufferSizeIn)
/*     */   {
/*  48 */     this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
/*  49 */     this.rawIntBuffer = this.byteBuffer.asIntBuffer();
/*  50 */     this.field_181676_c = this.byteBuffer.asShortBuffer();
/*  51 */     this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
/*     */   }
/*     */   
/*     */   private void func_181670_b(int p_181670_1_)
/*     */   {
/*  56 */     if (p_181670_1_ > this.rawIntBuffer.remaining())
/*     */     {
/*  58 */       int i = this.byteBuffer.capacity();
/*  59 */       int j = i % 2097152;
/*  60 */       int k = j + (((this.rawIntBuffer.position() + p_181670_1_) * 4 - j) / 2097152 + 1) * 2097152;
/*  61 */       LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + i + " bytes, new size " + k + " bytes.");
/*  62 */       int l = this.rawIntBuffer.position();
/*  63 */       ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(k);
/*  64 */       this.byteBuffer.position(0);
/*  65 */       bytebuffer.put(this.byteBuffer);
/*  66 */       bytebuffer.rewind();
/*  67 */       this.byteBuffer = bytebuffer;
/*  68 */       this.rawFloatBuffer = this.byteBuffer.asFloatBuffer().asReadOnlyBuffer();
/*  69 */       this.rawIntBuffer = this.byteBuffer.asIntBuffer();
/*  70 */       this.rawIntBuffer.position(l);
/*  71 */       this.field_181676_c = this.byteBuffer.asShortBuffer();
/*  72 */       this.field_181676_c.position(l << 1);
/*     */       
/*  74 */       if (this.quadSprites != null)
/*     */       {
/*  76 */         TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
/*  77 */         int i1 = getBufferQuadSize();
/*  78 */         this.quadSprites = new TextureAtlasSprite[i1];
/*  79 */         System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, Math.min(atextureatlassprite.length, this.quadSprites.length));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_181674_a(float p_181674_1_, float p_181674_2_, float p_181674_3_)
/*     */   {
/*  86 */     int i = this.vertexCount / 4;
/*  87 */     float[] afloat = new float[i];
/*     */     
/*  89 */     for (int j = 0; j < i; j++)
/*     */     {
/*  91 */       afloat[j] = func_181665_a(this.rawFloatBuffer, (float)(p_181674_1_ + this.xOffset), (float)(p_181674_2_ + this.yOffset), (float)(p_181674_3_ + this.zOffset), this.vertexFormat.func_181719_f(), j * this.vertexFormat.getNextOffset());
/*     */     }
/*     */     
/*  94 */     Integer[] ainteger = new Integer[i];
/*     */     
/*  96 */     for (int k = 0; k < ainteger.length; k++)
/*     */     {
/*  98 */       ainteger[k] = Integer.valueOf(k);
/*     */     }
/*     */     
/* 101 */     Arrays.sort(ainteger, new WorldRenderer.1(this, afloat));
/* 102 */     BitSet bitset = new BitSet();
/* 103 */     int l = this.vertexFormat.getNextOffset();
/* 104 */     int[] aint = new int[l];
/*     */     
/* 106 */     for (int l1 = 0; (l1 = bitset.nextClearBit(l1)) < ainteger.length; l1++)
/*     */     {
/* 108 */       int i1 = ainteger[l1].intValue();
/*     */       
/* 110 */       if (i1 != l1)
/*     */       {
/* 112 */         this.rawIntBuffer.limit(i1 * l + l);
/* 113 */         this.rawIntBuffer.position(i1 * l);
/* 114 */         this.rawIntBuffer.get(aint);
/* 115 */         int j1 = i1;
/*     */         
/* 117 */         for (int k1 = ainteger[i1].intValue(); j1 != l1; k1 = ainteger[k1].intValue())
/*     */         {
/* 119 */           this.rawIntBuffer.limit(k1 * l + l);
/* 120 */           this.rawIntBuffer.position(k1 * l);
/* 121 */           IntBuffer intbuffer = this.rawIntBuffer.slice();
/* 122 */           this.rawIntBuffer.limit(j1 * l + l);
/* 123 */           this.rawIntBuffer.position(j1 * l);
/* 124 */           this.rawIntBuffer.put(intbuffer);
/* 125 */           bitset.set(j1);
/* 126 */           j1 = k1;
/*     */         }
/*     */         
/* 129 */         this.rawIntBuffer.limit(l1 * l + l);
/* 130 */         this.rawIntBuffer.position(l1 * l);
/* 131 */         this.rawIntBuffer.put(aint);
/*     */       }
/*     */       
/* 134 */       bitset.set(l1);
/*     */     }
/*     */     
/* 137 */     if (this.quadSprites != null)
/*     */     {
/* 139 */       TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.vertexCount / 4];
/* 140 */       int i2 = this.vertexFormat.func_181719_f() / 4 * 4;
/*     */       
/* 142 */       for (int j2 = 0; j2 < ainteger.length; j2++)
/*     */       {
/* 144 */         int k2 = ainteger[j2].intValue();
/* 145 */         atextureatlassprite[j2] = this.quadSprites[k2];
/*     */       }
/*     */       
/* 148 */       System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
/*     */     }
/*     */   }
/*     */   
/*     */   public State func_181672_a()
/*     */   {
/* 154 */     this.rawIntBuffer.rewind();
/* 155 */     int i = func_181664_j();
/* 156 */     this.rawIntBuffer.limit(i);
/* 157 */     int[] aint = new int[i];
/* 158 */     this.rawIntBuffer.get(aint);
/* 159 */     this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
/* 160 */     this.rawIntBuffer.position(i);
/* 161 */     TextureAtlasSprite[] atextureatlassprite = null;
/*     */     
/* 163 */     if (this.quadSprites != null)
/*     */     {
/* 165 */       int j = this.vertexCount / 4;
/* 166 */       atextureatlassprite = new TextureAtlasSprite[j];
/* 167 */       System.arraycopy(this.quadSprites, 0, atextureatlassprite, 0, j);
/*     */     }
/*     */     
/* 170 */     return new State(aint, new VertexFormat(this.vertexFormat), atextureatlassprite);
/*     */   }
/*     */   
/*     */   private int func_181664_j()
/*     */   {
/* 175 */     return this.vertexCount * this.vertexFormat.func_181719_f();
/*     */   }
/*     */   
/*     */   private static float func_181665_a(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_)
/*     */   {
/* 180 */     float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
/* 181 */     float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
/* 182 */     float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
/* 183 */     float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
/* 184 */     float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
/* 185 */     float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
/* 186 */     float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
/* 187 */     float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
/* 188 */     float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
/* 189 */     float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
/* 190 */     float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
/* 191 */     float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
/* 192 */     float f12 = (f + f3 + f6 + f9) * 0.25F - p_181665_1_;
/* 193 */     float f13 = (f1 + f4 + f7 + f10) * 0.25F - p_181665_2_;
/* 194 */     float f14 = (f2 + f5 + f8 + f11) * 0.25F - p_181665_3_;
/* 195 */     return f12 * f12 + f13 * f13 + f14 * f14;
/*     */   }
/*     */   
/*     */   public void setVertexState(State state)
/*     */   {
/* 200 */     this.rawIntBuffer.clear();
/* 201 */     func_181670_b(state.getRawBuffer().length);
/* 202 */     this.rawIntBuffer.put(state.getRawBuffer());
/* 203 */     this.vertexCount = state.getVertexCount();
/* 204 */     this.vertexFormat = new VertexFormat(state.getVertexFormat());
/*     */     
/* 206 */     if (state.stateQuadSprites != null)
/*     */     {
/* 208 */       if (this.quadSprites == null)
/*     */       {
/* 210 */         this.quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
/*     */       }
/*     */       
/* 213 */       TextureAtlasSprite[] atextureatlassprite = state.stateQuadSprites;
/* 214 */       System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
/*     */     }
/*     */     else
/*     */     {
/* 218 */       this.quadSprites = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset()
/*     */   {
/* 224 */     this.vertexCount = 0;
/* 225 */     this.field_181677_f = null;
/* 226 */     this.field_181678_g = 0;
/* 227 */     this.quadSprite = null;
/*     */   }
/*     */   
/*     */   public void startDrawingQuads()
/*     */   {
/* 232 */     startDrawing(7);
/*     */   }
/*     */   
/*     */   public void startDrawing(int p_178964_1_)
/*     */   {
/* 237 */     if (this.isDrawing)
/*     */     {
/* 239 */       throw new IllegalStateException("Already building!");
/*     */     }
/*     */     
/*     */ 
/* 243 */     this.isDrawing = true;
/* 244 */     reset();
/* 245 */     this.drawMode = p_178964_1_;
/* 246 */     this.needsUpdate = false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void begin(int p_181668_1_, VertexFormat p_181668_2_)
/*     */   {
/* 252 */     if (this.isDrawing)
/*     */     {
/* 254 */       throw new IllegalStateException("Already building!");
/*     */     }
/*     */     
/*     */ 
/* 258 */     this.isDrawing = true;
/* 259 */     reset();
/* 260 */     this.drawMode = p_181668_1_;
/* 261 */     this.vertexFormat = p_181668_2_;
/* 262 */     this.field_181677_f = p_181668_2_.getElement(this.field_181678_g);
/* 263 */     this.needsUpdate = false;
/* 264 */     this.byteBuffer.limit(this.byteBuffer.capacity());
/*     */     
/* 266 */     if (Config.isMultiTexture())
/*     */     {
/* 268 */       if ((this.blockLayer != null) && (this.quadSprites == null))
/*     */       {
/* 270 */         this.quadSprites = new TextureAtlasSprite[getBufferQuadSize()];
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 275 */       this.quadSprites = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public WorldRenderer tex(double p_181673_1_, double p_181673_3_)
/*     */   {
/* 282 */     if ((this.quadSprite != null) && (this.quadSprites != null))
/*     */     {
/* 284 */       p_181673_1_ = this.quadSprite.toSingleU((float)p_181673_1_);
/* 285 */       p_181673_3_ = this.quadSprite.toSingleV((float)p_181673_3_);
/* 286 */       this.quadSprites[(this.vertexCount / 4)] = this.quadSprite;
/*     */     }
/*     */     
/* 289 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
/*     */     
/* 291 */     switch (WorldRenderer.2.field_181661_a[this.field_181677_f.getType().ordinal()])
/*     */     {
/*     */     case 1: 
/* 294 */       this.byteBuffer.putFloat(i, (float)p_181673_1_);
/* 295 */       this.byteBuffer.putFloat(i + 4, (float)p_181673_3_);
/* 296 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 300 */       this.byteBuffer.putInt(i, (int)p_181673_1_);
/* 301 */       this.byteBuffer.putInt(i + 4, (int)p_181673_3_);
/* 302 */       break;
/*     */     
/*     */     case 4: 
/*     */     case 5: 
/* 306 */       this.byteBuffer.putShort(i, (short)(int)p_181673_3_);
/* 307 */       this.byteBuffer.putShort(i + 2, (short)(int)p_181673_1_);
/* 308 */       break;
/*     */     
/*     */     case 6: 
/*     */     case 7: 
/* 312 */       this.byteBuffer.put(i, (byte)(int)p_181673_3_);
/* 313 */       this.byteBuffer.put(i + 1, (byte)(int)p_181673_1_);
/*     */     }
/*     */     
/* 316 */     func_181667_k();
/* 317 */     return this;
/*     */   }
/*     */   
/*     */   public WorldRenderer lightmap(int p_181671_1_, int p_181671_2_)
/*     */   {
/* 322 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
/*     */     
/* 324 */     switch (WorldRenderer.2.field_181661_a[this.field_181677_f.getType().ordinal()])
/*     */     {
/*     */     case 1: 
/* 327 */       this.byteBuffer.putFloat(i, p_181671_1_);
/* 328 */       this.byteBuffer.putFloat(i + 4, p_181671_2_);
/* 329 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 333 */       this.byteBuffer.putInt(i, p_181671_1_);
/* 334 */       this.byteBuffer.putInt(i + 4, p_181671_2_);
/* 335 */       break;
/*     */     
/*     */     case 4: 
/*     */     case 5: 
/* 339 */       this.byteBuffer.putShort(i, (short)p_181671_2_);
/* 340 */       this.byteBuffer.putShort(i + 2, (short)p_181671_1_);
/* 341 */       break;
/*     */     
/*     */     case 6: 
/*     */     case 7: 
/* 345 */       this.byteBuffer.put(i, (byte)p_181671_2_);
/* 346 */       this.byteBuffer.put(i + 1, (byte)p_181671_1_);
/*     */     }
/*     */     
/* 349 */     func_181667_k();
/* 350 */     return this;
/*     */   }
/*     */   
/*     */   public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_)
/*     */   {
/* 355 */     int i = (this.vertexCount - 4) * this.vertexFormat.func_181719_f() + this.vertexFormat.getUvOffsetById(1) / 4;
/* 356 */     int j = this.vertexFormat.getNextOffset() >> 2;
/* 357 */     this.rawIntBuffer.put(i, p_178962_1_);
/* 358 */     this.rawIntBuffer.put(i + j, p_178962_2_);
/* 359 */     this.rawIntBuffer.put(i + j * 2, p_178962_3_);
/* 360 */     this.rawIntBuffer.put(i + j * 3, p_178962_4_);
/*     */   }
/*     */   
/*     */   public void putPosition(double x, double y, double z)
/*     */   {
/* 365 */     int i = this.vertexFormat.func_181719_f();
/* 366 */     int j = (this.vertexCount - 4) * i;
/*     */     
/* 368 */     for (int k = 0; k < 4; k++)
/*     */     {
/* 370 */       int l = j + k * i;
/* 371 */       int i1 = l + 1;
/* 372 */       int j1 = i1 + 1;
/* 373 */       this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
/* 374 */       this.rawIntBuffer.put(i1, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i1))));
/* 375 */       this.rawIntBuffer.put(j1, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j1))));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getColorIndex(int p_78909_1_)
/*     */   {
/* 384 */     return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
/*     */   }
/*     */   
/*     */   public void putColorMultiplier(float red, float green, float blue, int p_178978_4_)
/*     */   {
/* 389 */     int i = getColorIndex(p_178978_4_);
/* 390 */     int j = -1;
/*     */     
/* 392 */     if (!this.needsUpdate)
/*     */     {
/* 394 */       j = this.rawIntBuffer.get(i);
/*     */       
/* 396 */       if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
/*     */       {
/* 398 */         int k = (int)((j & 0xFF) * red);
/* 399 */         int l = (int)((j >> 8 & 0xFF) * green);
/* 400 */         int i1 = (int)((j >> 16 & 0xFF) * blue);
/* 401 */         j &= 0xFF000000;
/* 402 */         j = j | i1 << 16 | l << 8 | k;
/*     */       }
/*     */       else
/*     */       {
/* 406 */         int j1 = (int)((j >> 24 & 0xFF) * red);
/* 407 */         int k1 = (int)((j >> 16 & 0xFF) * green);
/* 408 */         int l1 = (int)((j >> 8 & 0xFF) * blue);
/* 409 */         j &= 0xFF;
/* 410 */         j = j | j1 << 24 | k1 << 16 | l1 << 8;
/*     */       }
/*     */     }
/*     */     
/* 414 */     this.rawIntBuffer.put(i, j);
/*     */   }
/*     */   
/*     */   public void func_178974_a(int p_178974_1_, int p_178974_2_)
/*     */   {
/* 419 */     int var3 = p_178974_1_ >> 16 & 0xFF;
/* 420 */     int var4 = p_178974_1_ >> 8 & 0xFF;
/* 421 */     int var5 = p_178974_1_ & 0xFF;
/* 422 */     color(var3, var4, var5, p_178974_2_);
/*     */   }
/*     */   
/*     */ 
/*     */   public void putColor(int argb, int p_178988_2_)
/*     */   {
/* 428 */     int i = getColorIndex(p_178988_2_);
/* 429 */     int j = argb >> 16 & 0xFF;
/* 430 */     int k = argb >> 8 & 0xFF;
/* 431 */     int l = argb & 0xFF;
/* 432 */     int i1 = argb >> 24 & 0xFF;
/* 433 */     putColorRGBA(i, j, k, l, i1);
/*     */   }
/*     */   
/*     */   public void putColorRGB_F(float red, float green, float blue, int p_178994_4_)
/*     */   {
/* 438 */     int i = getColorIndex(p_178994_4_);
/* 439 */     int j = MathHelper.clamp_int((int)(red * 255.0F), 0, 255);
/* 440 */     int k = MathHelper.clamp_int((int)(green * 255.0F), 0, 255);
/* 441 */     int l = MathHelper.clamp_int((int)(blue * 255.0F), 0, 255);
/* 442 */     putColorRGBA(i, j, k, l, 255);
/*     */   }
/*     */   
/*     */   private void putColorRGBA(int index, int red, int p_178972_3_, int p_178972_4_, int p_178972_5_)
/*     */   {
/* 447 */     if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
/*     */     {
/* 449 */       this.rawIntBuffer.put(index, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | red);
/*     */     }
/*     */     else
/*     */     {
/* 453 */       this.rawIntBuffer.put(index, red << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markDirty()
/*     */   {
/* 463 */     this.needsUpdate = true;
/*     */   }
/*     */   
/*     */   public WorldRenderer color(float p_181666_1_, float p_181666_2_, float p_181666_3_, float p_181666_4_)
/*     */   {
/* 468 */     return color((int)(p_181666_1_ * 255.0F), (int)(p_181666_2_ * 255.0F), (int)(p_181666_3_ * 255.0F), (int)(p_181666_4_ * 255.0F));
/*     */   }
/*     */   
/*     */   public WorldRenderer color(int p_181669_1_, int p_181669_2_, int p_181669_3_, int p_181669_4_)
/*     */   {
/* 473 */     if (this.needsUpdate)
/*     */     {
/* 475 */       return this;
/*     */     }
/*     */     
/*     */ 
/* 479 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
/*     */     
/* 481 */     switch (WorldRenderer.2.field_181661_a[this.field_181677_f.getType().ordinal()])
/*     */     {
/*     */     case 1: 
/* 484 */       this.byteBuffer.putFloat(i, p_181669_1_ / 255.0F);
/* 485 */       this.byteBuffer.putFloat(i + 4, p_181669_2_ / 255.0F);
/* 486 */       this.byteBuffer.putFloat(i + 8, p_181669_3_ / 255.0F);
/* 487 */       this.byteBuffer.putFloat(i + 12, p_181669_4_ / 255.0F);
/* 488 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 492 */       this.byteBuffer.putFloat(i, p_181669_1_);
/* 493 */       this.byteBuffer.putFloat(i + 4, p_181669_2_);
/* 494 */       this.byteBuffer.putFloat(i + 8, p_181669_3_);
/* 495 */       this.byteBuffer.putFloat(i + 12, p_181669_4_);
/* 496 */       break;
/*     */     
/*     */     case 4: 
/*     */     case 5: 
/* 500 */       this.byteBuffer.putShort(i, (short)p_181669_1_);
/* 501 */       this.byteBuffer.putShort(i + 2, (short)p_181669_2_);
/* 502 */       this.byteBuffer.putShort(i + 4, (short)p_181669_3_);
/* 503 */       this.byteBuffer.putShort(i + 6, (short)p_181669_4_);
/* 504 */       break;
/*     */     
/*     */     case 6: 
/*     */     case 7: 
/* 508 */       if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
/*     */       {
/* 510 */         this.byteBuffer.put(i, (byte)p_181669_1_);
/* 511 */         this.byteBuffer.put(i + 1, (byte)p_181669_2_);
/* 512 */         this.byteBuffer.put(i + 2, (byte)p_181669_3_);
/* 513 */         this.byteBuffer.put(i + 3, (byte)p_181669_4_);
/*     */       }
/*     */       else
/*     */       {
/* 517 */         this.byteBuffer.put(i, (byte)p_181669_4_);
/* 518 */         this.byteBuffer.put(i + 1, (byte)p_181669_3_);
/* 519 */         this.byteBuffer.put(i + 2, (byte)p_181669_2_);
/* 520 */         this.byteBuffer.put(i + 3, (byte)p_181669_1_);
/*     */       }
/*     */       break;
/*     */     }
/* 524 */     func_181667_k();
/* 525 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addVertexData(int[] vertexData)
/*     */   {
/* 531 */     func_181670_b(vertexData.length);
/* 532 */     this.rawIntBuffer.position(func_181664_j());
/* 533 */     this.rawIntBuffer.put(vertexData);
/* 534 */     this.vertexCount += vertexData.length / this.vertexFormat.func_181719_f();
/*     */   }
/*     */   
/*     */   public void endVertex()
/*     */   {
/* 539 */     this.vertexCount += 1;
/* 540 */     func_181670_b(this.vertexFormat.func_181719_f());
/*     */   }
/*     */   
/*     */   public WorldRenderer pos(double p_181662_1_, double p_181662_3_, double p_181662_5_)
/*     */   {
/* 545 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
/*     */     
/* 547 */     switch (WorldRenderer.2.field_181661_a[this.field_181677_f.getType().ordinal()])
/*     */     {
/*     */     case 1: 
/* 550 */       this.byteBuffer.putFloat(i, (float)(p_181662_1_ + this.xOffset));
/* 551 */       this.byteBuffer.putFloat(i + 4, (float)(p_181662_3_ + this.yOffset));
/* 552 */       this.byteBuffer.putFloat(i + 8, (float)(p_181662_5_ + this.zOffset));
/* 553 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 557 */       this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)(p_181662_1_ + this.xOffset)));
/* 558 */       this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(p_181662_3_ + this.yOffset)));
/* 559 */       this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(p_181662_5_ + this.zOffset)));
/* 560 */       break;
/*     */     
/*     */     case 4: 
/*     */     case 5: 
/* 564 */       this.byteBuffer.putShort(i, (short)(int)(p_181662_1_ + this.xOffset));
/* 565 */       this.byteBuffer.putShort(i + 2, (short)(int)(p_181662_3_ + this.yOffset));
/* 566 */       this.byteBuffer.putShort(i + 4, (short)(int)(p_181662_5_ + this.zOffset));
/* 567 */       break;
/*     */     
/*     */     case 6: 
/*     */     case 7: 
/* 571 */       this.byteBuffer.put(i, (byte)(int)(p_181662_1_ + this.xOffset));
/* 572 */       this.byteBuffer.put(i + 1, (byte)(int)(p_181662_3_ + this.yOffset));
/* 573 */       this.byteBuffer.put(i + 2, (byte)(int)(p_181662_5_ + this.zOffset));
/*     */     }
/*     */     
/* 576 */     func_181667_k();
/* 577 */     return this;
/*     */   }
/*     */   
/*     */   public void putNormal(float x, float y, float z)
/*     */   {
/* 582 */     int i = (byte)(int)(x * 127.0F) & 0xFF;
/* 583 */     int j = (byte)(int)(y * 127.0F) & 0xFF;
/* 584 */     int k = (byte)(int)(z * 127.0F) & 0xFF;
/* 585 */     int l = i | j << 8 | k << 16;
/* 586 */     int i1 = this.vertexFormat.getNextOffset() >> 2;
/* 587 */     int j1 = (this.vertexCount - 4) * i1 + this.vertexFormat.getNormalOffset() / 4;
/* 588 */     this.rawIntBuffer.put(j1, l);
/* 589 */     this.rawIntBuffer.put(j1 + i1, l);
/* 590 */     this.rawIntBuffer.put(j1 + i1 * 2, l);
/* 591 */     this.rawIntBuffer.put(j1 + i1 * 3, l);
/*     */   }
/*     */   
/*     */   private void func_181667_k()
/*     */   {
/* 596 */     this.field_181678_g += 1;
/* 597 */     this.field_181678_g %= this.vertexFormat.getElementCount();
/* 598 */     this.field_181677_f = this.vertexFormat.getElement(this.field_181678_g);
/*     */     
/* 600 */     if (this.field_181677_f.getUsage() == VertexFormatElement.EnumUsage.PADDING)
/*     */     {
/* 602 */       func_181667_k();
/*     */     }
/*     */   }
/*     */   
/*     */   public WorldRenderer normal(float p_181663_1_, float p_181663_2_, float p_181663_3_)
/*     */   {
/* 608 */     int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
/*     */     
/* 610 */     switch (WorldRenderer.2.field_181661_a[this.field_181677_f.getType().ordinal()])
/*     */     {
/*     */     case 1: 
/* 613 */       this.byteBuffer.putFloat(i, p_181663_1_);
/* 614 */       this.byteBuffer.putFloat(i + 4, p_181663_2_);
/* 615 */       this.byteBuffer.putFloat(i + 8, p_181663_3_);
/* 616 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 620 */       this.byteBuffer.putInt(i, (int)p_181663_1_);
/* 621 */       this.byteBuffer.putInt(i + 4, (int)p_181663_2_);
/* 622 */       this.byteBuffer.putInt(i + 8, (int)p_181663_3_);
/* 623 */       break;
/*     */     
/*     */     case 4: 
/*     */     case 5: 
/* 627 */       this.byteBuffer.putShort(i, (short)((int)p_181663_1_ * 32767 & 0xFFFF));
/* 628 */       this.byteBuffer.putShort(i + 2, (short)((int)p_181663_2_ * 32767 & 0xFFFF));
/* 629 */       this.byteBuffer.putShort(i + 4, (short)((int)p_181663_3_ * 32767 & 0xFFFF));
/* 630 */       break;
/*     */     
/*     */     case 6: 
/*     */     case 7: 
/* 634 */       this.byteBuffer.put(i, (byte)((int)p_181663_1_ * 127 & 0xFF));
/* 635 */       this.byteBuffer.put(i + 1, (byte)((int)p_181663_2_ * 127 & 0xFF));
/* 636 */       this.byteBuffer.put(i + 2, (byte)((int)p_181663_3_ * 127 & 0xFF));
/*     */     }
/*     */     
/* 639 */     func_181667_k();
/* 640 */     return this;
/*     */   }
/*     */   
/*     */   public void setTranslation(double x, double y, double z)
/*     */   {
/* 645 */     this.xOffset = x;
/* 646 */     this.yOffset = y;
/* 647 */     this.zOffset = z;
/*     */   }
/*     */   
/*     */   public void finishDrawing()
/*     */   {
/* 652 */     if (!this.isDrawing)
/*     */     {
/* 654 */       throw new IllegalStateException("Not building!");
/*     */     }
/*     */     
/*     */ 
/* 658 */     this.isDrawing = false;
/* 659 */     this.byteBuffer.position(0);
/* 660 */     this.byteBuffer.limit(func_181664_j() * 4);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 666 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   public VertexFormat getVertexFormat()
/*     */   {
/* 671 */     return this.vertexFormat;
/*     */   }
/*     */   
/*     */   public int getVertexCount()
/*     */   {
/* 676 */     return this.vertexCount;
/*     */   }
/*     */   
/*     */   public int getDrawMode()
/*     */   {
/* 681 */     return this.drawMode;
/*     */   }
/*     */   
/*     */   public void putColor4(int argb)
/*     */   {
/* 686 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 688 */       putColor(argb, i + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void putColorRGB_F4(float red, float green, float blue)
/*     */   {
/* 694 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 696 */       putColorRGB_F(red, green, blue, i + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void putSprite(TextureAtlasSprite p_putSprite_1_)
/*     */   {
/* 702 */     if (this.quadSprites != null)
/*     */     {
/* 704 */       int i = this.vertexCount / 4;
/* 705 */       this.quadSprites[(i - 1)] = p_putSprite_1_;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSprite(TextureAtlasSprite p_setSprite_1_)
/*     */   {
/* 711 */     if (this.quadSprites != null)
/*     */     {
/* 713 */       this.quadSprite = p_setSprite_1_;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMultiTexture()
/*     */   {
/* 719 */     return this.quadSprites != null;
/*     */   }
/*     */   
/*     */   public void drawMultiTexture()
/*     */   {
/* 724 */     if (this.quadSprites != null)
/*     */     {
/* 726 */       int i = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
/*     */       
/* 728 */       if (this.drawnIcons.length <= i)
/*     */       {
/* 730 */         this.drawnIcons = new boolean[i + 1];
/*     */       }
/*     */       
/* 733 */       Arrays.fill(this.drawnIcons, false);
/* 734 */       int j = 0;
/* 735 */       int k = -1;
/* 736 */       int l = this.vertexCount / 4;
/*     */       
/* 738 */       for (int i1 = 0; i1 < l; i1++)
/*     */       {
/* 740 */         TextureAtlasSprite textureatlassprite = this.quadSprites[i1];
/*     */         
/* 742 */         if (textureatlassprite != null)
/*     */         {
/* 744 */           int j1 = textureatlassprite.getIndexInMap();
/*     */           
/* 746 */           if (this.drawnIcons[j1] == 0)
/*     */           {
/* 748 */             if (textureatlassprite == TextureUtils.iconGrassSideOverlay)
/*     */             {
/* 750 */               if (k < 0)
/*     */               {
/* 752 */                 k = i1;
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 757 */               i1 = drawForIcon(textureatlassprite, i1) - 1;
/* 758 */               j++;
/*     */               
/* 760 */               if (this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT)
/*     */               {
/* 762 */                 this.drawnIcons[j1] = true;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 769 */       if (k >= 0)
/*     */       {
/* 771 */         drawForIcon(TextureUtils.iconGrassSideOverlay, k);
/* 772 */         j++;
/*     */       }
/*     */       
/* 775 */       if (j <= 0) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int drawForIcon(TextureAtlasSprite p_drawForIcon_1_, int p_drawForIcon_2_)
/*     */   {
/* 786 */     GL11.glBindTexture(3553, p_drawForIcon_1_.glSpriteTextureId);
/* 787 */     int i = -1;
/* 788 */     int j = -1;
/* 789 */     int k = this.vertexCount / 4;
/*     */     
/* 791 */     for (int l = p_drawForIcon_2_; l < k; l++)
/*     */     {
/* 793 */       TextureAtlasSprite textureatlassprite = this.quadSprites[l];
/*     */       
/* 795 */       if (textureatlassprite == p_drawForIcon_1_)
/*     */       {
/* 797 */         if (j < 0)
/*     */         {
/* 799 */           j = l;
/*     */         }
/*     */       }
/* 802 */       else if (j >= 0)
/*     */       {
/* 804 */         draw(j, l);
/*     */         
/* 806 */         if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT)
/*     */         {
/* 808 */           return l;
/*     */         }
/*     */         
/* 811 */         j = -1;
/*     */         
/* 813 */         if (i < 0)
/*     */         {
/* 815 */           i = l;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 820 */     if (j >= 0)
/*     */     {
/* 822 */       draw(j, k);
/*     */     }
/*     */     
/* 825 */     if (i < 0)
/*     */     {
/* 827 */       i = k;
/*     */     }
/*     */     
/* 830 */     return i;
/*     */   }
/*     */   
/*     */   private void draw(int p_draw_1_, int p_draw_2_)
/*     */   {
/* 835 */     int i = p_draw_2_ - p_draw_1_;
/*     */     
/* 837 */     if (i > 0)
/*     */     {
/* 839 */       int j = p_draw_1_ * 4;
/* 840 */       int k = i * 4;
/* 841 */       GL11.glDrawArrays(this.drawMode, j, k);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlockLayer(EnumWorldBlockLayer p_setBlockLayer_1_)
/*     */   {
/* 847 */     this.blockLayer = p_setBlockLayer_1_;
/*     */   }
/*     */   
/*     */   private int getBufferQuadSize()
/*     */   {
/* 852 */     int i = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.func_181719_f() * 4);
/* 853 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class State
/*     */   {
/*     */     private final int[] stateRawBuffer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final VertexFormat stateVertexFormat;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static final String __OBFID = "CL_00002568";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private TextureAtlasSprite[] stateQuadSprites;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public State(int[] p_i2_2_, VertexFormat p_i2_3_, TextureAtlasSprite[] p_i2_4_)
/*     */     {
/* 937 */       this.stateRawBuffer = p_i2_2_;
/* 938 */       this.stateVertexFormat = p_i2_3_;
/* 939 */       this.stateQuadSprites = p_i2_4_;
/*     */     }
/*     */     
/*     */     public State(int[] p_i46453_2_, VertexFormat p_i46453_3_)
/*     */     {
/* 944 */       this.stateRawBuffer = p_i46453_2_;
/* 945 */       this.stateVertexFormat = p_i46453_3_;
/*     */     }
/*     */     
/*     */     public int[] getRawBuffer()
/*     */     {
/* 950 */       return this.stateRawBuffer;
/*     */     }
/*     */     
/*     */     public int getVertexCount()
/*     */     {
/* 955 */       return this.stateRawBuffer.length / this.stateVertexFormat.func_181719_f();
/*     */     }
/*     */     
/*     */     public VertexFormat getVertexFormat()
/*     */     {
/* 960 */       return this.stateVertexFormat;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\WorldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */