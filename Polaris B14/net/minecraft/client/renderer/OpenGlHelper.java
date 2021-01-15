/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ import optfine.Config;
/*     */ import org.lwjgl.opengl.ARBFramebufferObject;
/*     */ import org.lwjgl.opengl.ARBMultitexture;
/*     */ import org.lwjgl.opengl.ARBShaderObjects;
/*     */ import org.lwjgl.opengl.ARBVertexBufferObject;
/*     */ import org.lwjgl.opengl.ARBVertexShader;
/*     */ import org.lwjgl.opengl.ContextCapabilities;
/*     */ import org.lwjgl.opengl.EXTBlendFuncSeparate;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.HardwareAbstractionLayer;
/*     */ import oshi.hardware.Processor;
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
/*     */ public class OpenGlHelper
/*     */ {
/*     */   public static boolean nvidia;
/*     */   public static boolean field_181063_b;
/*     */   public static int GL_FRAMEBUFFER;
/*     */   public static int GL_RENDERBUFFER;
/*     */   public static int GL_COLOR_ATTACHMENT0;
/*     */   public static int GL_DEPTH_ATTACHMENT;
/*     */   public static int GL_FRAMEBUFFER_COMPLETE;
/*     */   public static int GL_FB_INCOMPLETE_ATTACHMENT;
/*     */   public static int GL_FB_INCOMPLETE_MISS_ATTACH;
/*     */   public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
/*     */   public static int GL_FB_INCOMPLETE_READ_BUFFER;
/*     */   private static int framebufferType;
/*     */   public static boolean framebufferSupported;
/*     */   private static boolean shadersAvailable;
/*     */   private static boolean arbShaders;
/*     */   public static int GL_LINK_STATUS;
/*     */   public static int GL_COMPILE_STATUS;
/*     */   public static int GL_VERTEX_SHADER;
/*     */   public static int GL_FRAGMENT_SHADER;
/*     */   private static boolean arbMultitexture;
/*     */   public static int defaultTexUnit;
/*     */   public static int lightmapTexUnit;
/*     */   public static int GL_TEXTURE2;
/*     */   private static boolean arbTextureEnvCombine;
/*     */   public static int GL_COMBINE;
/*     */   public static int GL_INTERPOLATE;
/*     */   public static int GL_PRIMARY_COLOR;
/*     */   public static int GL_CONSTANT;
/*     */   public static int GL_PREVIOUS;
/*     */   public static int GL_COMBINE_RGB;
/*     */   public static int GL_SOURCE0_RGB;
/*     */   public static int GL_SOURCE1_RGB;
/*     */   public static int GL_SOURCE2_RGB;
/*     */   public static int GL_OPERAND0_RGB;
/*     */   public static int GL_OPERAND1_RGB;
/*     */   public static int GL_OPERAND2_RGB;
/*     */   public static int GL_COMBINE_ALPHA;
/*     */   public static int GL_SOURCE0_ALPHA;
/*     */   public static int GL_SOURCE1_ALPHA;
/*     */   public static int GL_SOURCE2_ALPHA;
/*     */   public static int GL_OPERAND0_ALPHA;
/*     */   public static int GL_OPERAND1_ALPHA;
/*     */   public static int GL_OPERAND2_ALPHA;
/*     */   private static boolean openGL14;
/*     */   public static boolean extBlendFuncSeparate;
/*     */   public static boolean openGL21;
/*     */   public static boolean shadersSupported;
/*  87 */   private static String logText = "";
/*     */   private static String field_183030_aa;
/*     */   public static boolean vboSupported;
/*     */   public static boolean field_181062_Q;
/*     */   private static boolean arbVbo;
/*     */   public static int GL_ARRAY_BUFFER;
/*     */   public static int GL_STATIC_DRAW;
/*     */   private static final String __OBFID = "CL_00001179";
/*  95 */   public static float lastBrightnessX = 0.0F;
/*  96 */   public static float lastBrightnessY = 0.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void initializeTextures()
/*     */   {
/* 103 */     Config.initDisplay();
/* 104 */     ContextCapabilities contextcapabilities = GLContext.getCapabilities();
/* 105 */     arbMultitexture = (contextcapabilities.GL_ARB_multitexture) && (!contextcapabilities.OpenGL13);
/* 106 */     arbTextureEnvCombine = (contextcapabilities.GL_ARB_texture_env_combine) && (!contextcapabilities.OpenGL13);
/*     */     
/* 108 */     if (arbMultitexture)
/*     */     {
/* 110 */       logText += "Using ARB_multitexture.\n";
/* 111 */       defaultTexUnit = 33984;
/* 112 */       lightmapTexUnit = 33985;
/* 113 */       GL_TEXTURE2 = 33986;
/*     */     }
/*     */     else
/*     */     {
/* 117 */       logText += "Using GL 1.3 multitexturing.\n";
/* 118 */       defaultTexUnit = 33984;
/* 119 */       lightmapTexUnit = 33985;
/* 120 */       GL_TEXTURE2 = 33986;
/*     */     }
/*     */     
/* 123 */     if (arbTextureEnvCombine)
/*     */     {
/* 125 */       logText += "Using ARB_texture_env_combine.\n";
/* 126 */       GL_COMBINE = 34160;
/* 127 */       GL_INTERPOLATE = 34165;
/* 128 */       GL_PRIMARY_COLOR = 34167;
/* 129 */       GL_CONSTANT = 34166;
/* 130 */       GL_PREVIOUS = 34168;
/* 131 */       GL_COMBINE_RGB = 34161;
/* 132 */       GL_SOURCE0_RGB = 34176;
/* 133 */       GL_SOURCE1_RGB = 34177;
/* 134 */       GL_SOURCE2_RGB = 34178;
/* 135 */       GL_OPERAND0_RGB = 34192;
/* 136 */       GL_OPERAND1_RGB = 34193;
/* 137 */       GL_OPERAND2_RGB = 34194;
/* 138 */       GL_COMBINE_ALPHA = 34162;
/* 139 */       GL_SOURCE0_ALPHA = 34184;
/* 140 */       GL_SOURCE1_ALPHA = 34185;
/* 141 */       GL_SOURCE2_ALPHA = 34186;
/* 142 */       GL_OPERAND0_ALPHA = 34200;
/* 143 */       GL_OPERAND1_ALPHA = 34201;
/* 144 */       GL_OPERAND2_ALPHA = 34202;
/*     */     }
/*     */     else
/*     */     {
/* 148 */       logText += "Using GL 1.3 texture combiners.\n";
/* 149 */       GL_COMBINE = 34160;
/* 150 */       GL_INTERPOLATE = 34165;
/* 151 */       GL_PRIMARY_COLOR = 34167;
/* 152 */       GL_CONSTANT = 34166;
/* 153 */       GL_PREVIOUS = 34168;
/* 154 */       GL_COMBINE_RGB = 34161;
/* 155 */       GL_SOURCE0_RGB = 34176;
/* 156 */       GL_SOURCE1_RGB = 34177;
/* 157 */       GL_SOURCE2_RGB = 34178;
/* 158 */       GL_OPERAND0_RGB = 34192;
/* 159 */       GL_OPERAND1_RGB = 34193;
/* 160 */       GL_OPERAND2_RGB = 34194;
/* 161 */       GL_COMBINE_ALPHA = 34162;
/* 162 */       GL_SOURCE0_ALPHA = 34184;
/* 163 */       GL_SOURCE1_ALPHA = 34185;
/* 164 */       GL_SOURCE2_ALPHA = 34186;
/* 165 */       GL_OPERAND0_ALPHA = 34200;
/* 166 */       GL_OPERAND1_ALPHA = 34201;
/* 167 */       GL_OPERAND2_ALPHA = 34202;
/*     */     }
/*     */     
/* 170 */     extBlendFuncSeparate = (contextcapabilities.GL_EXT_blend_func_separate) && (!contextcapabilities.OpenGL14);
/* 171 */     openGL14 = (contextcapabilities.OpenGL14) || (contextcapabilities.GL_EXT_blend_func_separate);
/* 172 */     framebufferSupported = (openGL14) && ((contextcapabilities.GL_ARB_framebuffer_object) || (contextcapabilities.GL_EXT_framebuffer_object) || (contextcapabilities.OpenGL30));
/*     */     
/* 174 */     if (framebufferSupported)
/*     */     {
/* 176 */       logText += "Using framebuffer objects because ";
/*     */       
/* 178 */       if (contextcapabilities.OpenGL30)
/*     */       {
/* 180 */         logText += "OpenGL 3.0 is supported and separate blending is supported.\n";
/* 181 */         framebufferType = 0;
/* 182 */         GL_FRAMEBUFFER = 36160;
/* 183 */         GL_RENDERBUFFER = 36161;
/* 184 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 185 */         GL_DEPTH_ATTACHMENT = 36096;
/* 186 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 187 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 188 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 189 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 190 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/* 192 */       else if (contextcapabilities.GL_ARB_framebuffer_object)
/*     */       {
/* 194 */         logText += "ARB_framebuffer_object is supported and separate blending is supported.\n";
/* 195 */         framebufferType = 1;
/* 196 */         GL_FRAMEBUFFER = 36160;
/* 197 */         GL_RENDERBUFFER = 36161;
/* 198 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 199 */         GL_DEPTH_ATTACHMENT = 36096;
/* 200 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 201 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 202 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 203 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 204 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/* 206 */       else if (contextcapabilities.GL_EXT_framebuffer_object)
/*     */       {
/* 208 */         logText += "EXT_framebuffer_object is supported.\n";
/* 209 */         framebufferType = 2;
/* 210 */         GL_FRAMEBUFFER = 36160;
/* 211 */         GL_RENDERBUFFER = 36161;
/* 212 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 213 */         GL_DEPTH_ATTACHMENT = 36096;
/* 214 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 215 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 216 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 217 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 218 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 223 */       logText += "Not using framebuffer objects because ";
/* 224 */       logText = logText + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
/* 225 */       logText = logText + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
/* 226 */       logText = logText + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
/* 227 */       logText = logText + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
/* 228 */       logText = logText + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
/*     */     }
/*     */     
/* 231 */     openGL21 = contextcapabilities.OpenGL21;
/* 232 */     shadersAvailable = (openGL21) || ((contextcapabilities.GL_ARB_vertex_shader) && (contextcapabilities.GL_ARB_fragment_shader) && (contextcapabilities.GL_ARB_shader_objects));
/* 233 */     logText = logText + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
/*     */     
/* 235 */     if (shadersAvailable)
/*     */     {
/* 237 */       if (contextcapabilities.OpenGL21)
/*     */       {
/* 239 */         logText += "OpenGL 2.1 is supported.\n";
/* 240 */         arbShaders = false;
/* 241 */         GL_LINK_STATUS = 35714;
/* 242 */         GL_COMPILE_STATUS = 35713;
/* 243 */         GL_VERTEX_SHADER = 35633;
/* 244 */         GL_FRAGMENT_SHADER = 35632;
/*     */       }
/*     */       else
/*     */       {
/* 248 */         logText += "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
/* 249 */         arbShaders = true;
/* 250 */         GL_LINK_STATUS = 35714;
/* 251 */         GL_COMPILE_STATUS = 35713;
/* 252 */         GL_VERTEX_SHADER = 35633;
/* 253 */         GL_FRAGMENT_SHADER = 35632;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 258 */       logText = logText + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
/* 259 */       logText = logText + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
/* 260 */       logText = logText + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
/* 261 */       logText = logText + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
/*     */     }
/*     */     
/* 264 */     shadersSupported = (framebufferSupported) && (shadersAvailable);
/* 265 */     String s = GL11.glGetString(7936).toLowerCase();
/* 266 */     nvidia = s.contains("nvidia");
/* 267 */     arbVbo = (!contextcapabilities.OpenGL15) && (contextcapabilities.GL_ARB_vertex_buffer_object);
/* 268 */     vboSupported = (contextcapabilities.OpenGL15) || (arbVbo);
/* 269 */     logText = logText + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
/*     */     
/* 271 */     if (vboSupported)
/*     */     {
/* 273 */       if (arbVbo)
/*     */       {
/* 275 */         logText += "ARB_vertex_buffer_object is supported.\n";
/* 276 */         GL_STATIC_DRAW = 35044;
/* 277 */         GL_ARRAY_BUFFER = 34962;
/*     */       }
/*     */       else
/*     */       {
/* 281 */         logText += "OpenGL 1.5 is supported.\n";
/* 282 */         GL_STATIC_DRAW = 35044;
/* 283 */         GL_ARRAY_BUFFER = 34962;
/*     */       }
/*     */     }
/*     */     
/* 287 */     field_181063_b = s.contains("ati");
/*     */     
/* 289 */     if (field_181063_b)
/*     */     {
/* 291 */       if (vboSupported)
/*     */       {
/* 293 */         field_181062_Q = true;
/*     */       }
/*     */       else
/*     */       {
/* 297 */         GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 303 */       Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
/* 304 */       field_183030_aa = String.format("%dx %s", new Object[] { Integer.valueOf(aprocessor.length), aprocessor[0] }).replaceAll("\\s+", " ");
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean areShadersSupported()
/*     */   {
/* 314 */     return shadersSupported;
/*     */   }
/*     */   
/*     */   public static String getLogText()
/*     */   {
/* 319 */     return logText;
/*     */   }
/*     */   
/*     */   public static int glGetProgrami(int program, int pname)
/*     */   {
/* 324 */     return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(program, pname) : GL20.glGetProgrami(program, pname);
/*     */   }
/*     */   
/*     */   public static void glAttachShader(int program, int shaderIn)
/*     */   {
/* 329 */     if (arbShaders)
/*     */     {
/* 331 */       ARBShaderObjects.glAttachObjectARB(program, shaderIn);
/*     */     }
/*     */     else
/*     */     {
/* 335 */       GL20.glAttachShader(program, shaderIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteShader(int p_153180_0_)
/*     */   {
/* 341 */     if (arbShaders)
/*     */     {
/* 343 */       ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
/*     */     }
/*     */     else
/*     */     {
/* 347 */       GL20.glDeleteShader(p_153180_0_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int glCreateShader(int type)
/*     */   {
/* 356 */     return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(type) : GL20.glCreateShader(type);
/*     */   }
/*     */   
/*     */   public static void glShaderSource(int shaderIn, ByteBuffer string)
/*     */   {
/* 361 */     if (arbShaders)
/*     */     {
/* 363 */       ARBShaderObjects.glShaderSourceARB(shaderIn, string);
/*     */     }
/*     */     else
/*     */     {
/* 367 */       GL20.glShaderSource(shaderIn, string);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glCompileShader(int shaderIn)
/*     */   {
/* 373 */     if (arbShaders)
/*     */     {
/* 375 */       ARBShaderObjects.glCompileShaderARB(shaderIn);
/*     */     }
/*     */     else
/*     */     {
/* 379 */       GL20.glCompileShader(shaderIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glGetShaderi(int shaderIn, int pname)
/*     */   {
/* 385 */     return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(shaderIn, pname) : GL20.glGetShaderi(shaderIn, pname);
/*     */   }
/*     */   
/*     */   public static String glGetShaderInfoLog(int shaderIn, int maxLength)
/*     */   {
/* 390 */     return arbShaders ? ARBShaderObjects.glGetInfoLogARB(shaderIn, maxLength) : GL20.glGetShaderInfoLog(shaderIn, maxLength);
/*     */   }
/*     */   
/*     */   public static String glGetProgramInfoLog(int program, int maxLength)
/*     */   {
/* 395 */     return arbShaders ? ARBShaderObjects.glGetInfoLogARB(program, maxLength) : GL20.glGetProgramInfoLog(program, maxLength);
/*     */   }
/*     */   
/*     */   public static void glUseProgram(int program)
/*     */   {
/* 400 */     if (arbShaders)
/*     */     {
/* 402 */       ARBShaderObjects.glUseProgramObjectARB(program);
/*     */     }
/*     */     else
/*     */     {
/* 406 */       GL20.glUseProgram(program);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glCreateProgram()
/*     */   {
/* 412 */     return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
/*     */   }
/*     */   
/*     */   public static void glDeleteProgram(int program)
/*     */   {
/* 417 */     if (arbShaders)
/*     */     {
/* 419 */       ARBShaderObjects.glDeleteObjectARB(program);
/*     */     }
/*     */     else
/*     */     {
/* 423 */       GL20.glDeleteProgram(program);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glLinkProgram(int program)
/*     */   {
/* 429 */     if (arbShaders)
/*     */     {
/* 431 */       ARBShaderObjects.glLinkProgramARB(program);
/*     */     }
/*     */     else
/*     */     {
/* 435 */       GL20.glLinkProgram(program);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glGetUniformLocation(int programObj, CharSequence name)
/*     */   {
/* 441 */     return arbShaders ? ARBShaderObjects.glGetUniformLocationARB(programObj, name) : GL20.glGetUniformLocation(programObj, name);
/*     */   }
/*     */   
/*     */   public static void glUniform1(int location, IntBuffer values)
/*     */   {
/* 446 */     if (arbShaders)
/*     */     {
/* 448 */       ARBShaderObjects.glUniform1ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 452 */       GL20.glUniform1(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform1i(int location, int v0)
/*     */   {
/* 458 */     if (arbShaders)
/*     */     {
/* 460 */       ARBShaderObjects.glUniform1iARB(location, v0);
/*     */     }
/*     */     else
/*     */     {
/* 464 */       GL20.glUniform1i(location, v0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform1(int location, FloatBuffer values)
/*     */   {
/* 470 */     if (arbShaders)
/*     */     {
/* 472 */       ARBShaderObjects.glUniform1ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 476 */       GL20.glUniform1(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform2(int location, IntBuffer values)
/*     */   {
/* 482 */     if (arbShaders)
/*     */     {
/* 484 */       ARBShaderObjects.glUniform2ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 488 */       GL20.glUniform2(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform2(int location, FloatBuffer values)
/*     */   {
/* 494 */     if (arbShaders)
/*     */     {
/* 496 */       ARBShaderObjects.glUniform2ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 500 */       GL20.glUniform2(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform3(int location, IntBuffer values)
/*     */   {
/* 506 */     if (arbShaders)
/*     */     {
/* 508 */       ARBShaderObjects.glUniform3ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 512 */       GL20.glUniform3(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform3(int location, FloatBuffer values)
/*     */   {
/* 518 */     if (arbShaders)
/*     */     {
/* 520 */       ARBShaderObjects.glUniform3ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 524 */       GL20.glUniform3(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform4(int location, IntBuffer values)
/*     */   {
/* 530 */     if (arbShaders)
/*     */     {
/* 532 */       ARBShaderObjects.glUniform4ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 536 */       GL20.glUniform4(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniform4(int location, FloatBuffer values)
/*     */   {
/* 542 */     if (arbShaders)
/*     */     {
/* 544 */       ARBShaderObjects.glUniform4ARB(location, values);
/*     */     }
/*     */     else
/*     */     {
/* 548 */       GL20.glUniform4(location, values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices)
/*     */   {
/* 554 */     if (arbShaders)
/*     */     {
/* 556 */       ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrices);
/*     */     }
/*     */     else
/*     */     {
/* 560 */       GL20.glUniformMatrix2(location, transpose, matrices);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices)
/*     */   {
/* 566 */     if (arbShaders)
/*     */     {
/* 568 */       ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrices);
/*     */     }
/*     */     else
/*     */     {
/* 572 */       GL20.glUniformMatrix3(location, transpose, matrices);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices)
/*     */   {
/* 578 */     if (arbShaders)
/*     */     {
/* 580 */       ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrices);
/*     */     }
/*     */     else
/*     */     {
/* 584 */       GL20.glUniformMatrix4(location, transpose, matrices);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glGetAttribLocation(int p_153164_0_, CharSequence p_153164_1_)
/*     */   {
/* 590 */     return arbShaders ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
/*     */   }
/*     */   
/*     */   public static int glGenBuffers()
/*     */   {
/* 595 */     return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
/*     */   }
/*     */   
/*     */   public static void glBindBuffer(int target, int buffer)
/*     */   {
/* 600 */     if (arbVbo)
/*     */     {
/* 602 */       ARBVertexBufferObject.glBindBufferARB(target, buffer);
/*     */     }
/*     */     else
/*     */     {
/* 606 */       GL15.glBindBuffer(target, buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glBufferData(int target, ByteBuffer data, int usage)
/*     */   {
/* 612 */     if (arbVbo)
/*     */     {
/* 614 */       ARBVertexBufferObject.glBufferDataARB(target, data, usage);
/*     */     }
/*     */     else
/*     */     {
/* 618 */       GL15.glBufferData(target, data, usage);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteBuffers(int buffer)
/*     */   {
/* 624 */     if (arbVbo)
/*     */     {
/* 626 */       ARBVertexBufferObject.glDeleteBuffersARB(buffer);
/*     */     }
/*     */     else
/*     */     {
/* 630 */       GL15.glDeleteBuffers(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean useVbo()
/*     */   {
/* 636 */     return !Config.isMultiTexture();
/*     */   }
/*     */   
/*     */   public static void glBindFramebuffer(int target, int framebufferIn)
/*     */   {
/* 641 */     if (framebufferSupported)
/*     */     {
/* 643 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 646 */         GL30.glBindFramebuffer(target, framebufferIn);
/* 647 */         break;
/*     */       
/*     */       case 1: 
/* 650 */         ARBFramebufferObject.glBindFramebuffer(target, framebufferIn);
/* 651 */         break;
/*     */       
/*     */       case 2: 
/* 654 */         EXTFramebufferObject.glBindFramebufferEXT(target, framebufferIn);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glBindRenderbuffer(int target, int renderbuffer)
/*     */   {
/* 661 */     if (framebufferSupported)
/*     */     {
/* 663 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 666 */         GL30.glBindRenderbuffer(target, renderbuffer);
/* 667 */         break;
/*     */       
/*     */       case 1: 
/* 670 */         ARBFramebufferObject.glBindRenderbuffer(target, renderbuffer);
/* 671 */         break;
/*     */       
/*     */       case 2: 
/* 674 */         EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteRenderbuffers(int renderbuffer)
/*     */   {
/* 681 */     if (framebufferSupported)
/*     */     {
/* 683 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 686 */         GL30.glDeleteRenderbuffers(renderbuffer);
/* 687 */         break;
/*     */       
/*     */       case 1: 
/* 690 */         ARBFramebufferObject.glDeleteRenderbuffers(renderbuffer);
/* 691 */         break;
/*     */       
/*     */       case 2: 
/* 694 */         EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteFramebuffers(int framebufferIn)
/*     */   {
/* 701 */     if (framebufferSupported)
/*     */     {
/* 703 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 706 */         GL30.glDeleteFramebuffers(framebufferIn);
/* 707 */         break;
/*     */       
/*     */       case 1: 
/* 710 */         ARBFramebufferObject.glDeleteFramebuffers(framebufferIn);
/* 711 */         break;
/*     */       
/*     */       case 2: 
/* 714 */         EXTFramebufferObject.glDeleteFramebuffersEXT(framebufferIn);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int glGenFramebuffers()
/*     */   {
/* 724 */     if (!framebufferSupported)
/*     */     {
/* 726 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 730 */     switch (framebufferType)
/*     */     {
/*     */     case 0: 
/* 733 */       return GL30.glGenFramebuffers();
/*     */     
/*     */     case 1: 
/* 736 */       return ARBFramebufferObject.glGenFramebuffers();
/*     */     
/*     */     case 2: 
/* 739 */       return EXTFramebufferObject.glGenFramebuffersEXT();
/*     */     }
/*     */     
/* 742 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int glGenRenderbuffers()
/*     */   {
/* 749 */     if (!framebufferSupported)
/*     */     {
/* 751 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 755 */     switch (framebufferType)
/*     */     {
/*     */     case 0: 
/* 758 */       return GL30.glGenRenderbuffers();
/*     */     
/*     */     case 1: 
/* 761 */       return ARBFramebufferObject.glGenRenderbuffers();
/*     */     
/*     */     case 2: 
/* 764 */       return EXTFramebufferObject.glGenRenderbuffersEXT();
/*     */     }
/*     */     
/* 767 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void glRenderbufferStorage(int target, int internalFormat, int width, int height)
/*     */   {
/* 774 */     if (framebufferSupported)
/*     */     {
/* 776 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 779 */         GL30.glRenderbufferStorage(target, internalFormat, width, height);
/* 780 */         break;
/*     */       
/*     */       case 1: 
/* 783 */         ARBFramebufferObject.glRenderbufferStorage(target, internalFormat, width, height);
/* 784 */         break;
/*     */       
/*     */       case 2: 
/* 787 */         EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glFramebufferRenderbuffer(int target, int attachment, int renderBufferTarget, int renderBuffer)
/*     */   {
/* 794 */     if (framebufferSupported)
/*     */     {
/* 796 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 799 */         GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/* 800 */         break;
/*     */       
/*     */       case 1: 
/* 803 */         ARBFramebufferObject.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/* 804 */         break;
/*     */       
/*     */       case 2: 
/* 807 */         EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glCheckFramebufferStatus(int target)
/*     */   {
/* 814 */     if (!framebufferSupported)
/*     */     {
/* 816 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 820 */     switch (framebufferType)
/*     */     {
/*     */     case 0: 
/* 823 */       return GL30.glCheckFramebufferStatus(target);
/*     */     
/*     */     case 1: 
/* 826 */       return ARBFramebufferObject.glCheckFramebufferStatus(target);
/*     */     
/*     */     case 2: 
/* 829 */       return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
/*     */     }
/*     */     
/* 832 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level)
/*     */   {
/* 839 */     if (framebufferSupported)
/*     */     {
/* 841 */       switch (framebufferType)
/*     */       {
/*     */       case 0: 
/* 844 */         GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/* 845 */         break;
/*     */       
/*     */       case 1: 
/* 848 */         ARBFramebufferObject.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/* 849 */         break;
/*     */       
/*     */       case 2: 
/* 852 */         EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setActiveTexture(int texture)
/*     */   {
/* 862 */     if (arbMultitexture)
/*     */     {
/* 864 */       ARBMultitexture.glActiveTextureARB(texture);
/*     */     }
/*     */     else
/*     */     {
/* 868 */       GL13.glActiveTexture(texture);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setClientActiveTexture(int texture)
/*     */   {
/* 877 */     if (arbMultitexture)
/*     */     {
/* 879 */       ARBMultitexture.glClientActiveTextureARB(texture);
/*     */     }
/*     */     else
/*     */     {
/* 883 */       GL13.glClientActiveTexture(texture);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setLightmapTextureCoords(int target, float p_77475_1_, float p_77475_2_)
/*     */   {
/* 892 */     if (arbMultitexture)
/*     */     {
/* 894 */       ARBMultitexture.glMultiTexCoord2fARB(target, p_77475_1_, p_77475_2_);
/*     */     }
/*     */     else
/*     */     {
/* 898 */       GL13.glMultiTexCoord2f(target, p_77475_1_, p_77475_2_);
/*     */     }
/*     */     
/* 901 */     if (target == lightmapTexUnit)
/*     */     {
/* 903 */       lastBrightnessX = p_77475_1_;
/* 904 */       lastBrightnessY = p_77475_2_;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glBlendFunc(int sFactorRGB, int dFactorRGB, int sfactorAlpha, int dfactorAlpha)
/*     */   {
/* 910 */     if (openGL14)
/*     */     {
/* 912 */       if (extBlendFuncSeparate)
/*     */       {
/* 914 */         EXTBlendFuncSeparate.glBlendFuncSeparateEXT(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
/*     */       }
/*     */       else
/*     */       {
/* 918 */         GL14.glBlendFuncSeparate(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 923 */       GL11.glBlendFunc(sFactorRGB, dFactorRGB);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isFramebufferEnabled()
/*     */   {
/* 929 */     return !Config.isFastRender();
/*     */   }
/*     */   
/*     */   public static String func_183029_j()
/*     */   {
/* 934 */     return field_183030_aa == null ? "<unknown>" : field_183030_aa;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\OpenGlHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */