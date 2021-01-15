package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldVertexBufferUploader {
   private static final String __OBFID = "CL_00002567";

   public void func_181679_a(WorldRenderer p_181679_1_) {
      if(p_181679_1_.func_178989_h() > 0) {
         VertexFormat vertexformat = p_181679_1_.func_178973_g();
         int i = vertexformat.func_177338_f();
         ByteBuffer bytebuffer = p_181679_1_.func_178966_f();
         List list = vertexformat.func_177343_g();
         boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
         boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();

         for(int j = 0; j < list.size(); ++j) {
            VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
            VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.func_177375_c();
            if(flag) {
               Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, new Object[]{vertexformat, Integer.valueOf(j), Integer.valueOf(i), bytebuffer});
            } else {
               int l = vertexformatelement.func_177367_b().func_177397_c();
               int k = vertexformatelement.func_177369_e();
               bytebuffer.position(vertexformat.func_181720_d(j));
               switch(WorldVertexBufferUploader.WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage.ordinal()]) {
               case 1:
                  GL11.glVertexPointer(vertexformatelement.func_177370_d(), l, i, bytebuffer);
                  GL11.glEnableClientState('\u8074');
                  break;
               case 2:
                  OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a + k);
                  GL11.glTexCoordPointer(vertexformatelement.func_177370_d(), l, i, bytebuffer);
                  GL11.glEnableClientState('\u8078');
                  OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
                  break;
               case 3:
                  GL11.glColorPointer(vertexformatelement.func_177370_d(), l, i, bytebuffer);
                  GL11.glEnableClientState('\u8076');
                  break;
               case 4:
                  GL11.glNormalPointer(l, i, bytebuffer);
                  GL11.glEnableClientState('\u8075');
               }
            }
         }

         if(p_181679_1_.isMultiTexture()) {
            p_181679_1_.drawMultiTexture();
         } else if(Config.isShaders()) {
            SVertexBuilder.drawArrays(p_181679_1_.func_178979_i(), 0, p_181679_1_.func_178989_h(), p_181679_1_);
         } else {
            GL11.glDrawArrays(p_181679_1_.func_178979_i(), 0, p_181679_1_.func_178989_h());
         }

         int i1 = 0;

         for(int k1 = list.size(); i1 < k1; ++i1) {
            VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
            VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.func_177375_c();
            if(flag1) {
               Reflector.callVoid(vertexformatelement$enumusage1, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, new Object[]{vertexformat, Integer.valueOf(i1), Integer.valueOf(i), bytebuffer});
            } else {
               int j1 = vertexformatelement1.func_177369_e();
               switch(WorldVertexBufferUploader.WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage1.ordinal()]) {
               case 1:
                  GL11.glDisableClientState('\u8074');
                  break;
               case 2:
                  OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a + j1);
                  GL11.glDisableClientState('\u8078');
                  OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
                  break;
               case 3:
                  GL11.glDisableClientState('\u8076');
                  GlStateManager.func_179117_G();
                  break;
               case 4:
                  GL11.glDisableClientState('\u8075');
               }
            }
         }
      }

      p_181679_1_.func_178965_a();
   }

   static final class WorldVertexBufferUploader$1 {
      static final int[] field_178958_a = new int[VertexFormatElement.EnumUsage.values().length];
      private static final String __OBFID = "CL_00002566";

      static {
         try {
            field_178958_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
