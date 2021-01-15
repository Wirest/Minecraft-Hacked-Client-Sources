/*    */ package net.minecraft.client.shader;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.util.JsonException;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ShaderLinkHelper
/*    */ {
/* 11 */   private static final Logger logger = ;
/*    */   private static ShaderLinkHelper staticShaderLinkHelper;
/*    */   
/*    */   public static void setNewStaticShaderLinkHelper()
/*    */   {
/* 16 */     staticShaderLinkHelper = new ShaderLinkHelper();
/*    */   }
/*    */   
/*    */   public static ShaderLinkHelper getStaticShaderLinkHelper()
/*    */   {
/* 21 */     return staticShaderLinkHelper;
/*    */   }
/*    */   
/*    */   public void deleteShader(ShaderManager p_148077_1_)
/*    */   {
/* 26 */     p_148077_1_.getFragmentShaderLoader().deleteShader(p_148077_1_);
/* 27 */     p_148077_1_.getVertexShaderLoader().deleteShader(p_148077_1_);
/* 28 */     OpenGlHelper.glDeleteProgram(p_148077_1_.getProgram());
/*    */   }
/*    */   
/*    */   public int createProgram() throws JsonException
/*    */   {
/* 33 */     int i = OpenGlHelper.glCreateProgram();
/*    */     
/* 35 */     if (i <= 0)
/*    */     {
/* 37 */       throw new JsonException("Could not create shader program (returned program ID " + i + ")");
/*    */     }
/*    */     
/*    */ 
/* 41 */     return i;
/*    */   }
/*    */   
/*    */   public void linkProgram(ShaderManager manager)
/*    */     throws IOException
/*    */   {
/* 47 */     manager.getFragmentShaderLoader().attachShader(manager);
/* 48 */     manager.getVertexShaderLoader().attachShader(manager);
/* 49 */     OpenGlHelper.glLinkProgram(manager.getProgram());
/* 50 */     int i = OpenGlHelper.glGetProgrami(manager.getProgram(), OpenGlHelper.GL_LINK_STATUS);
/*    */     
/* 52 */     if (i == 0)
/*    */     {
/* 54 */       logger.warn("Error encountered when linking program containing VS " + manager.getVertexShaderLoader().getShaderFilename() + " and FS " + manager.getFragmentShaderLoader().getShaderFilename() + ". Log output:");
/* 55 */       logger.warn(OpenGlHelper.glGetProgramInfoLog(manager.getProgram(), 32768));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\ShaderLinkHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */