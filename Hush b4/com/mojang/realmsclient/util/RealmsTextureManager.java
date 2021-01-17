// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

import java.util.HashMap;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.ARBMultitexture;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.RealmsScreen;
import java.util.Map;

public class RealmsTextureManager
{
    private static Map<String, RealmsTexture> textures;
    private static Boolean useMultitextureArb;
    public static int GL_TEXTURE0;
    
    public static void bindWorldTemplate(final String id, final String image) {
        if (image == null) {
            RealmsScreen.bind("textures/gui/presets/isles.png");
            return;
        }
        final int textureId = getTextureId(id, image);
        GL11.glBindTexture(3553, textureId);
    }
    
    public static int getTextureId(final String id, final String image) {
        int textureId;
        if (RealmsTextureManager.textures.containsKey(id)) {
            final RealmsTexture texture = RealmsTextureManager.textures.get(id);
            if (texture.image.equals(image)) {
                return texture.textureId;
            }
            GL11.glDeleteTextures(texture.textureId);
            textureId = texture.textureId;
        }
        else {
            textureId = GL11.glGenTextures();
        }
        IntBuffer buf = null;
        int width = 0;
        int height = 0;
        try {
            final InputStream in = new ByteArrayInputStream(new Base64().decode(image));
            BufferedImage img;
            try {
                img = ImageIO.read(in);
            }
            finally {
                IOUtils.closeQuietly(in);
            }
            width = img.getWidth();
            height = img.getHeight();
            final int[] data = new int[width * height];
            img.getRGB(0, 0, width, height, data, 0, width);
            buf = ByteBuffer.allocateDirect(4 * width * height).order(ByteOrder.nativeOrder()).asIntBuffer();
            buf.put(data);
            buf.flip();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (RealmsTextureManager.GL_TEXTURE0 == -1) {
            if (getUseMultiTextureArb()) {
                RealmsTextureManager.GL_TEXTURE0 = 33984;
            }
            else {
                RealmsTextureManager.GL_TEXTURE0 = 33984;
            }
        }
        glActiveTexture(RealmsTextureManager.GL_TEXTURE0);
        GL11.glBindTexture(3553, textureId);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, buf);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
        RealmsTextureManager.textures.put(id, new RealmsTexture(image, textureId));
        return textureId;
    }
    
    public static void glActiveTexture(final int texture) {
        if (getUseMultiTextureArb()) {
            ARBMultitexture.glActiveTextureARB(texture);
        }
        else {
            GL13.glActiveTexture(texture);
        }
    }
    
    public static boolean getUseMultiTextureArb() {
        if (RealmsTextureManager.useMultitextureArb == null) {
            final ContextCapabilities caps = GLContext.getCapabilities();
            RealmsTextureManager.useMultitextureArb = (caps.GL_ARB_multitexture && !caps.OpenGL13);
        }
        return RealmsTextureManager.useMultitextureArb;
    }
    
    static {
        RealmsTextureManager.textures = new HashMap<String, RealmsTexture>();
        RealmsTextureManager.GL_TEXTURE0 = -1;
    }
    
    public static class RealmsTexture
    {
        String image;
        int textureId;
        
        public RealmsTexture(final String image, final int textureId) {
            this.image = image;
            this.textureId = textureId;
        }
    }
}
