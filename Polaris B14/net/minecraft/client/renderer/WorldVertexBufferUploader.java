/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class WorldVertexBufferUploader
/*     */ {
/*     */   private static final String __OBFID = "CL_00002567";
/*     */   
/*     */   public void func_181679_a(WorldRenderer p_181679_1_)
/*     */   {
/*  17 */     if (p_181679_1_.getVertexCount() > 0)
/*     */     {
/*  19 */       VertexFormat vertexformat = p_181679_1_.getVertexFormat();
/*  20 */       int i = vertexformat.getNextOffset();
/*  21 */       java.nio.ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
/*  22 */       List list = vertexformat.getElements();
/*  23 */       boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
/*  24 */       boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
/*     */       
/*  26 */       for (int j = 0; j < list.size(); j++)
/*     */       {
/*  28 */         VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
/*  29 */         VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
/*     */         
/*  31 */         if (flag)
/*     */         {
/*  33 */           Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, new Object[] { vertexformatelement, Integer.valueOf(i), bytebuffer });
/*     */         }
/*     */         else
/*     */         {
/*  37 */           int l = vertexformatelement.getType().getGlConstant();
/*  38 */           int k = vertexformatelement.getIndex();
/*  39 */           bytebuffer.position(vertexformat.func_181720_d(j));
/*     */           
/*  41 */           switch (WorldVertexBufferUploader.1.field_178958_a[vertexformatelement$enumusage.ordinal()])
/*     */           {
/*     */           case 1: 
/*  44 */             GL11.glVertexPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
/*  45 */             GL11.glEnableClientState(32884);
/*  46 */             break;
/*     */           
/*     */           case 2: 
/*  49 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k);
/*  50 */             GL11.glTexCoordPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
/*  51 */             GL11.glEnableClientState(32888);
/*  52 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/*  53 */             break;
/*     */           
/*     */           case 3: 
/*  56 */             GL11.glColorPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
/*  57 */             GL11.glEnableClientState(32886);
/*  58 */             break;
/*     */           
/*     */           case 4: 
/*  61 */             GL11.glNormalPointer(l, i, bytebuffer);
/*  62 */             GL11.glEnableClientState(32885);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*  67 */       if (p_181679_1_.isMultiTexture())
/*     */       {
/*  69 */         p_181679_1_.drawMultiTexture();
/*     */       }
/*     */       else
/*     */       {
/*  73 */         GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
/*     */       }
/*     */       
/*  76 */       int i1 = 0;
/*     */       
/*  78 */       for (int k1 = list.size(); i1 < k1; i1++)
/*     */       {
/*  80 */         VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
/*  81 */         VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
/*     */         
/*  83 */         if (flag1)
/*     */         {
/*  85 */           Reflector.callVoid(vertexformatelement$enumusage1, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, new Object[] { vertexformatelement1, Integer.valueOf(i), bytebuffer });
/*     */         }
/*     */         else
/*     */         {
/*  89 */           int j1 = vertexformatelement1.getIndex();
/*     */           
/*  91 */           switch (WorldVertexBufferUploader.1.field_178958_a[vertexformatelement$enumusage1.ordinal()])
/*     */           {
/*     */           case 1: 
/*  94 */             GL11.glDisableClientState(32884);
/*  95 */             break;
/*     */           
/*     */           case 2: 
/*  98 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j1);
/*  99 */             GL11.glDisableClientState(32888);
/* 100 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 101 */             break;
/*     */           
/*     */           case 3: 
/* 104 */             GL11.glDisableClientState(32886);
/* 105 */             GlStateManager.resetColor();
/* 106 */             break;
/*     */           
/*     */           case 4: 
/* 109 */             GL11.glDisableClientState(32885);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/* 115 */     p_181679_1_.reset();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\WorldVertexBufferUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */