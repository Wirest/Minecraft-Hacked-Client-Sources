/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ 
/*     */ public class ShaderUniform
/*     */ {
/*  13 */   private static final Logger logger = ;
/*     */   private int uniformLocation;
/*     */   private final int uniformCount;
/*     */   private final int uniformType;
/*     */   private final IntBuffer uniformIntBuffer;
/*     */   private final FloatBuffer uniformFloatBuffer;
/*     */   private final String shaderName;
/*     */   private boolean dirty;
/*     */   private final ShaderManager shaderManager;
/*     */   
/*     */   public ShaderUniform(String name, int type, int count, ShaderManager manager)
/*     */   {
/*  25 */     this.shaderName = name;
/*  26 */     this.uniformCount = count;
/*  27 */     this.uniformType = type;
/*  28 */     this.shaderManager = manager;
/*     */     
/*  30 */     if (type <= 3)
/*     */     {
/*  32 */       this.uniformIntBuffer = BufferUtils.createIntBuffer(count);
/*  33 */       this.uniformFloatBuffer = null;
/*     */     }
/*     */     else
/*     */     {
/*  37 */       this.uniformIntBuffer = null;
/*  38 */       this.uniformFloatBuffer = BufferUtils.createFloatBuffer(count);
/*     */     }
/*     */     
/*  41 */     this.uniformLocation = -1;
/*  42 */     markDirty();
/*     */   }
/*     */   
/*     */   private void markDirty()
/*     */   {
/*  47 */     this.dirty = true;
/*     */     
/*  49 */     if (this.shaderManager != null)
/*     */     {
/*  51 */       this.shaderManager.markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static int parseType(String p_148085_0_)
/*     */   {
/*  57 */     int i = -1;
/*     */     
/*  59 */     if (p_148085_0_.equals("int"))
/*     */     {
/*  61 */       i = 0;
/*     */     }
/*  63 */     else if (p_148085_0_.equals("float"))
/*     */     {
/*  65 */       i = 4;
/*     */     }
/*  67 */     else if (p_148085_0_.startsWith("matrix"))
/*     */     {
/*  69 */       if (p_148085_0_.endsWith("2x2"))
/*     */       {
/*  71 */         i = 8;
/*     */       }
/*  73 */       else if (p_148085_0_.endsWith("3x3"))
/*     */       {
/*  75 */         i = 9;
/*     */       }
/*  77 */       else if (p_148085_0_.endsWith("4x4"))
/*     */       {
/*  79 */         i = 10;
/*     */       }
/*     */     }
/*     */     
/*  83 */     return i;
/*     */   }
/*     */   
/*     */   public void setUniformLocation(int p_148084_1_)
/*     */   {
/*  88 */     this.uniformLocation = p_148084_1_;
/*     */   }
/*     */   
/*     */   public String getShaderName()
/*     */   {
/*  93 */     return this.shaderName;
/*     */   }
/*     */   
/*     */   public void set(float p_148090_1_)
/*     */   {
/*  98 */     this.uniformFloatBuffer.position(0);
/*  99 */     this.uniformFloatBuffer.put(0, p_148090_1_);
/* 100 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(float p_148087_1_, float p_148087_2_)
/*     */   {
/* 105 */     this.uniformFloatBuffer.position(0);
/* 106 */     this.uniformFloatBuffer.put(0, p_148087_1_);
/* 107 */     this.uniformFloatBuffer.put(1, p_148087_2_);
/* 108 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(float p_148095_1_, float p_148095_2_, float p_148095_3_)
/*     */   {
/* 113 */     this.uniformFloatBuffer.position(0);
/* 114 */     this.uniformFloatBuffer.put(0, p_148095_1_);
/* 115 */     this.uniformFloatBuffer.put(1, p_148095_2_);
/* 116 */     this.uniformFloatBuffer.put(2, p_148095_3_);
/* 117 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(float p_148081_1_, float p_148081_2_, float p_148081_3_, float p_148081_4_)
/*     */   {
/* 122 */     this.uniformFloatBuffer.position(0);
/* 123 */     this.uniformFloatBuffer.put(p_148081_1_);
/* 124 */     this.uniformFloatBuffer.put(p_148081_2_);
/* 125 */     this.uniformFloatBuffer.put(p_148081_3_);
/* 126 */     this.uniformFloatBuffer.put(p_148081_4_);
/* 127 */     this.uniformFloatBuffer.flip();
/* 128 */     markDirty();
/*     */   }
/*     */   
/*     */   public void func_148092_b(float p_148092_1_, float p_148092_2_, float p_148092_3_, float p_148092_4_)
/*     */   {
/* 133 */     this.uniformFloatBuffer.position(0);
/*     */     
/* 135 */     if (this.uniformType >= 4)
/*     */     {
/* 137 */       this.uniformFloatBuffer.put(0, p_148092_1_);
/*     */     }
/*     */     
/* 140 */     if (this.uniformType >= 5)
/*     */     {
/* 142 */       this.uniformFloatBuffer.put(1, p_148092_2_);
/*     */     }
/*     */     
/* 145 */     if (this.uniformType >= 6)
/*     */     {
/* 147 */       this.uniformFloatBuffer.put(2, p_148092_3_);
/*     */     }
/*     */     
/* 150 */     if (this.uniformType >= 7)
/*     */     {
/* 152 */       this.uniformFloatBuffer.put(3, p_148092_4_);
/*     */     }
/*     */     
/* 155 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(int p_148083_1_, int p_148083_2_, int p_148083_3_, int p_148083_4_)
/*     */   {
/* 160 */     this.uniformIntBuffer.position(0);
/*     */     
/* 162 */     if (this.uniformType >= 0)
/*     */     {
/* 164 */       this.uniformIntBuffer.put(0, p_148083_1_);
/*     */     }
/*     */     
/* 167 */     if (this.uniformType >= 1)
/*     */     {
/* 169 */       this.uniformIntBuffer.put(1, p_148083_2_);
/*     */     }
/*     */     
/* 172 */     if (this.uniformType >= 2)
/*     */     {
/* 174 */       this.uniformIntBuffer.put(2, p_148083_3_);
/*     */     }
/*     */     
/* 177 */     if (this.uniformType >= 3)
/*     */     {
/* 179 */       this.uniformIntBuffer.put(3, p_148083_4_);
/*     */     }
/*     */     
/* 182 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(float[] p_148097_1_)
/*     */   {
/* 187 */     if (p_148097_1_.length < this.uniformCount)
/*     */     {
/* 189 */       logger.warn("Uniform.set called with a too-small value array (expected " + this.uniformCount + ", got " + p_148097_1_.length + "). Ignoring.");
/*     */     }
/*     */     else
/*     */     {
/* 193 */       this.uniformFloatBuffer.position(0);
/* 194 */       this.uniformFloatBuffer.put(p_148097_1_);
/* 195 */       this.uniformFloatBuffer.position(0);
/* 196 */       markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   public void set(float p_148094_1_, float p_148094_2_, float p_148094_3_, float p_148094_4_, float p_148094_5_, float p_148094_6_, float p_148094_7_, float p_148094_8_, float p_148094_9_, float p_148094_10_, float p_148094_11_, float p_148094_12_, float p_148094_13_, float p_148094_14_, float p_148094_15_, float p_148094_16_)
/*     */   {
/* 202 */     this.uniformFloatBuffer.position(0);
/* 203 */     this.uniformFloatBuffer.put(0, p_148094_1_);
/* 204 */     this.uniformFloatBuffer.put(1, p_148094_2_);
/* 205 */     this.uniformFloatBuffer.put(2, p_148094_3_);
/* 206 */     this.uniformFloatBuffer.put(3, p_148094_4_);
/* 207 */     this.uniformFloatBuffer.put(4, p_148094_5_);
/* 208 */     this.uniformFloatBuffer.put(5, p_148094_6_);
/* 209 */     this.uniformFloatBuffer.put(6, p_148094_7_);
/* 210 */     this.uniformFloatBuffer.put(7, p_148094_8_);
/* 211 */     this.uniformFloatBuffer.put(8, p_148094_9_);
/* 212 */     this.uniformFloatBuffer.put(9, p_148094_10_);
/* 213 */     this.uniformFloatBuffer.put(10, p_148094_11_);
/* 214 */     this.uniformFloatBuffer.put(11, p_148094_12_);
/* 215 */     this.uniformFloatBuffer.put(12, p_148094_13_);
/* 216 */     this.uniformFloatBuffer.put(13, p_148094_14_);
/* 217 */     this.uniformFloatBuffer.put(14, p_148094_15_);
/* 218 */     this.uniformFloatBuffer.put(15, p_148094_16_);
/* 219 */     markDirty();
/*     */   }
/*     */   
/*     */   public void set(Matrix4f p_148088_1_)
/*     */   {
/* 224 */     set(p_148088_1_.m00, p_148088_1_.m01, p_148088_1_.m02, p_148088_1_.m03, p_148088_1_.m10, p_148088_1_.m11, p_148088_1_.m12, p_148088_1_.m13, p_148088_1_.m20, p_148088_1_.m21, p_148088_1_.m22, p_148088_1_.m23, p_148088_1_.m30, p_148088_1_.m31, p_148088_1_.m32, p_148088_1_.m33);
/*     */   }
/*     */   
/*     */   public void upload()
/*     */   {
/* 229 */     if (!this.dirty) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 234 */     this.dirty = false;
/*     */     
/* 236 */     if (this.uniformType <= 3)
/*     */     {
/* 238 */       uploadInt();
/*     */     }
/* 240 */     else if (this.uniformType <= 7)
/*     */     {
/* 242 */       uploadFloat();
/*     */     }
/*     */     else
/*     */     {
/* 246 */       if (this.uniformType > 10)
/*     */       {
/* 248 */         logger.warn("Uniform.upload called, but type value (" + this.uniformType + ") is not " + "a valid type. Ignoring.");
/* 249 */         return;
/*     */       }
/*     */       
/* 252 */       uploadFloatMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   private void uploadInt()
/*     */   {
/* 258 */     switch (this.uniformType)
/*     */     {
/*     */     case 0: 
/* 261 */       OpenGlHelper.glUniform1(this.uniformLocation, this.uniformIntBuffer);
/* 262 */       break;
/*     */     
/*     */     case 1: 
/* 265 */       OpenGlHelper.glUniform2(this.uniformLocation, this.uniformIntBuffer);
/* 266 */       break;
/*     */     
/*     */     case 2: 
/* 269 */       OpenGlHelper.glUniform3(this.uniformLocation, this.uniformIntBuffer);
/* 270 */       break;
/*     */     
/*     */     case 3: 
/* 273 */       OpenGlHelper.glUniform4(this.uniformLocation, this.uniformIntBuffer);
/* 274 */       break;
/*     */     
/*     */     default: 
/* 277 */       logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + " not in the range of 1 to 4. Ignoring.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void uploadFloat()
/*     */   {
/* 283 */     switch (this.uniformType)
/*     */     {
/*     */     case 4: 
/* 286 */       OpenGlHelper.glUniform1(this.uniformLocation, this.uniformFloatBuffer);
/* 287 */       break;
/*     */     
/*     */     case 5: 
/* 290 */       OpenGlHelper.glUniform2(this.uniformLocation, this.uniformFloatBuffer);
/* 291 */       break;
/*     */     
/*     */     case 6: 
/* 294 */       OpenGlHelper.glUniform3(this.uniformLocation, this.uniformFloatBuffer);
/* 295 */       break;
/*     */     
/*     */     case 7: 
/* 298 */       OpenGlHelper.glUniform4(this.uniformLocation, this.uniformFloatBuffer);
/* 299 */       break;
/*     */     
/*     */     default: 
/* 302 */       logger.warn("Uniform.upload called, but count value (" + this.uniformCount + ") is " + "not in the range of 1 to 4. Ignoring.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void uploadFloatMatrix()
/*     */   {
/* 308 */     switch (this.uniformType)
/*     */     {
/*     */     case 8: 
/* 311 */       OpenGlHelper.glUniformMatrix2(this.uniformLocation, true, this.uniformFloatBuffer);
/* 312 */       break;
/*     */     
/*     */     case 9: 
/* 315 */       OpenGlHelper.glUniformMatrix3(this.uniformLocation, true, this.uniformFloatBuffer);
/* 316 */       break;
/*     */     
/*     */     case 10: 
/* 319 */       OpenGlHelper.glUniformMatrix4(this.uniformLocation, true, this.uniformFloatBuffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\ShaderUniform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */