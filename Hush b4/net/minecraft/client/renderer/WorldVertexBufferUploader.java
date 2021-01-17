// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.List;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import shadersmod.client.SVertexBuilder;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optifine.Reflector;

public class WorldVertexBufferUploader
{
    private static final String __OBFID = "CL_00002567";
    
    public void func_181679_a(final WorldRenderer p_181679_1_) {
        if (p_181679_1_.getVertexCount() > 0) {
            final VertexFormat vertexformat = p_181679_1_.getVertexFormat();
            final int i = vertexformat.getNextOffset();
            final ByteBuffer bytebuffer = p_181679_1_.getByteBuffer();
            final List list = vertexformat.getElements();
            final boolean flag = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            final boolean flag2 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            for (int j = 0; j < list.size(); ++j) {
                final VertexFormatElement vertexformatelement = list.get(j);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                if (flag) {
                    Reflector.callVoid(vertexformatelement$enumusage, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, vertexformat, j, i, bytebuffer);
                }
                else {
                    final int l = vertexformatelement.getType().getGlConstant();
                    final int k = vertexformatelement.getIndex();
                    bytebuffer.position(vertexformat.func_181720_d(j));
                    switch (WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage.ordinal()]) {
                        case 1: {
                            GL11.glVertexPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                            GL11.glEnableClientState(32884);
                            break;
                        }
                        case 2: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k);
                            GL11.glTexCoordPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                            GL11.glEnableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case 3: {
                            GL11.glColorPointer(vertexformatelement.getElementCount(), l, i, bytebuffer);
                            GL11.glEnableClientState(32886);
                            break;
                        }
                        case 4: {
                            GL11.glNormalPointer(l, i, bytebuffer);
                            GL11.glEnableClientState(32885);
                            break;
                        }
                    }
                }
            }
            if (p_181679_1_.isMultiTexture()) {
                p_181679_1_.drawMultiTexture();
            }
            else if (Config.isShaders()) {
                SVertexBuilder.drawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount(), p_181679_1_);
            }
            else {
                GL11.glDrawArrays(p_181679_1_.getDrawMode(), 0, p_181679_1_.getVertexCount());
            }
            for (int i2 = 0, k2 = list.size(); i2 < k2; ++i2) {
                final VertexFormatElement vertexformatelement2 = list.get(i2);
                final VertexFormatElement.EnumUsage vertexformatelement$enumusage2 = vertexformatelement2.getUsage();
                if (flag2) {
                    Reflector.callVoid(vertexformatelement$enumusage2, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, vertexformat, i2, i, bytebuffer);
                }
                else {
                    final int j2 = vertexformatelement2.getIndex();
                    switch (WorldVertexBufferUploader$1.field_178958_a[vertexformatelement$enumusage2.ordinal()]) {
                        case 1: {
                            GL11.glDisableClientState(32884);
                            break;
                        }
                        case 2: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + j2);
                            GL11.glDisableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            break;
                        }
                        case 3: {
                            GL11.glDisableClientState(32886);
                            GlStateManager.resetColor();
                            break;
                        }
                        case 4: {
                            GL11.glDisableClientState(32885);
                            break;
                        }
                    }
                }
            }
        }
        p_181679_1_.reset();
    }
    
    static final class WorldVertexBufferUploader$1
    {
        static final int[] field_178958_a;
        private static final String __OBFID = "CL_00002566";
        
        static {
            field_178958_a = new int[VertexFormatElement.EnumUsage.values().length];
            try {
                WorldVertexBufferUploader$1.field_178958_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                WorldVertexBufferUploader$1.field_178958_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                WorldVertexBufferUploader$1.field_178958_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                WorldVertexBufferUploader$1.field_178958_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
