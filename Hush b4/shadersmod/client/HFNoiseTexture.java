// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class HFNoiseTexture
{
    public int texID;
    public int textureUnit;
    
    public HFNoiseTexture(final int width, final int height) {
        this.texID = GL11.glGenTextures();
        this.textureUnit = 15;
        final byte[] abyte = this.genHFNoiseImage(width, height);
        final ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
        bytebuffer.put(abyte);
        bytebuffer.flip();
        GlStateManager.bindTexture(this.texID);
        GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, bytebuffer);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        GlStateManager.bindTexture(0);
    }
    
    public int getID() {
        return this.texID;
    }
    
    public void destroy() {
        GlStateManager.deleteTexture(this.texID);
        this.texID = 0;
    }
    
    private int random(int seed) {
        seed ^= seed << 13;
        seed ^= seed >> 17;
        seed ^= seed << 5;
        return seed;
    }
    
    private byte random(final int x, final int y, final int z) {
        final int i = (this.random(x) + this.random(y * 19)) * this.random(z * 23) - z;
        return (byte)(this.random(i) % 128);
    }
    
    private byte[] genHFNoiseImage(final int width, final int height) {
        final byte[] abyte = new byte[width * height * 3];
        int i = 0;
        for (int j = 0; j < height; ++j) {
            for (int k = 0; k < width; ++k) {
                for (int l = 1; l < 4; ++l) {
                    abyte[i++] = this.random(k, j, l);
                }
            }
        }
        return abyte;
    }
}
