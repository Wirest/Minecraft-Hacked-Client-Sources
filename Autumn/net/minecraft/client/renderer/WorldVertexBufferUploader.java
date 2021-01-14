package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optfine.Reflector;
import org.lwjgl.opengl.GL11;

public class WorldVertexBufferUploader {
   private static final String __OBFID = "CL_00002567";

   public void func_181679_a(WorldRenderer p_181679_1_) {
      if (p_181679_1_.getVertexCount() > 0) {
         VertexFormat vertexformat = p_181679_1_.getVertexFormat();
         int i = vertexformat.getNextOffset();
         ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
         List list = vertexformat.getElements();
         boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
         boolean flag1 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();

         int i1;
         int j1;
         for(i1 = 0; i1 < list.size(); ++i1) {
            VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(i1);
            VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
            if (flag) {
               Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, vertexformatelement, i, bytebuffer);
            } else {
               int l = vertexformatelement.getType().getGlConstant();
               j1 = vertexformatelement.getIndex();
               bytebuffer.position(vertexformat.func_181720_d(i1));
               switch(vertexformatelement$enumusage) {
               case POSITION:
                  GL11.glVertexPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                  GL11.glEnableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j1);
                  GL11.glTexCoordPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                  GL11.glEnableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GL11.glColorPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                  GL11.glEnableClientState(32886);
                  break;
               case NORMAL:
                  GL11.glNormalPointer(l, i, bytebuffer);
                  GL11.glEnableClientState(32885);
               }
            }
         }

         if (p_181679_1_.isMultiTexture()) {
            p_181679_1_.drawMultiTexture();
         } else {
            GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
         }

         i1 = 0;

         for(int k1 = list.size(); i1 < k1; ++i1) {
            VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
            VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
            if (flag1) {
               Reflector.callVoid(vertexformatelement$enumusage1, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, vertexformatelement1, i, bytebuffer);
            } else {
               j1 = vertexformatelement1.getIndex();
               switch(vertexformatelement$enumusage1) {
               case POSITION:
                  GL11.glDisableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j1);
                  GL11.glDisableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GL11.glDisableClientState(32886);
                  GlStateManager.resetColor();
                  break;
               case NORMAL:
                  GL11.glDisableClientState(32885);
               }
            }
         }
      }

      p_181679_1_.reset();
   }

   static final class WorldVertexBufferUploader$1 {
      static final int[] field_178958_a = new int[VertexFormatElement.EnumUsage.values().length];
      private static final String __OBFID = "CL_00002566";

      static {
         try {
            field_178958_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
