// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.newdawn.slick.util.Log;
import org.lwjgl.BufferUtils;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class TextureImpl implements Texture
{
    protected static SGL GL;
    static Texture lastBind;
    private int target;
    private int textureID;
    private int height;
    private int width;
    private int texWidth;
    private int texHeight;
    private float widthRatio;
    private float heightRatio;
    private boolean alpha;
    private String ref;
    private String cacheName;
    private ReloadData reloadData;
    
    static {
        TextureImpl.GL = Renderer.get();
    }
    
    public static Texture getLastBind() {
        return TextureImpl.lastBind;
    }
    
    protected TextureImpl() {
    }
    
    public TextureImpl(final String ref, final int target, final int textureID) {
        this.target = target;
        this.ref = ref;
        this.textureID = textureID;
        TextureImpl.lastBind = this;
    }
    
    public void setCacheName(final String cacheName) {
        this.cacheName = cacheName;
    }
    
    @Override
    public boolean hasAlpha() {
        return this.alpha;
    }
    
    @Override
    public String getTextureRef() {
        return this.ref;
    }
    
    public void setAlpha(final boolean alpha) {
        this.alpha = alpha;
    }
    
    public static void bindNone() {
        TextureImpl.lastBind = null;
        TextureImpl.GL.glDisable(3553);
    }
    
    public static void unbind() {
        TextureImpl.lastBind = null;
    }
    
    @Override
    public void bind() {
        if (TextureImpl.lastBind != this) {
            TextureImpl.lastBind = this;
            TextureImpl.GL.glEnable(3553);
            TextureImpl.GL.glBindTexture(this.target, this.textureID);
        }
    }
    
    public void setHeight(final int height) {
        this.height = height;
        this.setHeight();
    }
    
    public void setWidth(final int width) {
        this.width = width;
        this.setWidth();
    }
    
    @Override
    public int getImageHeight() {
        return this.height;
    }
    
    @Override
    public int getImageWidth() {
        return this.width;
    }
    
    @Override
    public float getHeight() {
        return this.heightRatio;
    }
    
    @Override
    public float getWidth() {
        return this.widthRatio;
    }
    
    @Override
    public int getTextureHeight() {
        return this.texHeight;
    }
    
    @Override
    public int getTextureWidth() {
        return this.texWidth;
    }
    
    public void setTextureHeight(final int texHeight) {
        this.texHeight = texHeight;
        this.setHeight();
    }
    
    public void setTextureWidth(final int texWidth) {
        this.texWidth = texWidth;
        this.setWidth();
    }
    
    private void setHeight() {
        if (this.texHeight != 0) {
            this.heightRatio = this.height / (float)this.texHeight;
        }
    }
    
    private void setWidth() {
        if (this.texWidth != 0) {
            this.widthRatio = this.width / (float)this.texWidth;
        }
    }
    
    @Override
    public void release() {
        final IntBuffer texBuf = this.createIntBuffer(1);
        texBuf.put(this.textureID);
        texBuf.flip();
        TextureImpl.GL.glDeleteTextures(texBuf);
        if (TextureImpl.lastBind == this) {
            bindNone();
        }
        if (this.cacheName != null) {
            InternalTextureLoader.get().clear(this.cacheName);
        }
        else {
            InternalTextureLoader.get().clear(this.ref);
        }
    }
    
    @Override
    public int getTextureID() {
        return this.textureID;
    }
    
    public void setTextureID(final int textureID) {
        this.textureID = textureID;
    }
    
    protected IntBuffer createIntBuffer(final int size) {
        final ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());
        return temp.asIntBuffer();
    }
    
    @Override
    public byte[] getTextureData() {
        final ByteBuffer buffer = BufferUtils.createByteBuffer((this.hasAlpha() ? 4 : 3) * this.texWidth * this.texHeight);
        this.bind();
        TextureImpl.GL.glGetTexImage(3553, 0, this.hasAlpha() ? 6408 : 6407, 5121, buffer);
        final byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        buffer.clear();
        return data;
    }
    
    @Override
    public void setTextureFilter(final int textureFilter) {
        this.bind();
        TextureImpl.GL.glTexParameteri(this.target, 10241, textureFilter);
        TextureImpl.GL.glTexParameteri(this.target, 10240, textureFilter);
    }
    
    public void setTextureData(final int srcPixelFormat, final int componentCount, final int minFilter, final int magFilter, final ByteBuffer textureBuffer) {
        ReloadData.access$1(this.reloadData = new ReloadData((ReloadData)null), srcPixelFormat);
        ReloadData.access$2(this.reloadData, componentCount);
        ReloadData.access$3(this.reloadData, minFilter);
        ReloadData.access$4(this.reloadData, magFilter);
        ReloadData.access$5(this.reloadData, textureBuffer);
    }
    
    public void reload() {
        if (this.reloadData != null) {
            this.textureID = this.reloadData.reload();
        }
    }
    
    private class ReloadData
    {
        private int srcPixelFormat;
        private int componentCount;
        private int minFilter;
        private int magFilter;
        private ByteBuffer textureBuffer;
        
        public int reload() {
            Log.error("Reloading texture: " + TextureImpl.this.ref);
            return InternalTextureLoader.get().reload(TextureImpl.this, this.srcPixelFormat, this.componentCount, this.minFilter, this.magFilter, this.textureBuffer);
        }
        
        static /* synthetic */ void access$1(final ReloadData reloadData, final int srcPixelFormat) {
            reloadData.srcPixelFormat = srcPixelFormat;
        }
        
        static /* synthetic */ void access$2(final ReloadData reloadData, final int componentCount) {
            reloadData.componentCount = componentCount;
        }
        
        static /* synthetic */ void access$3(final ReloadData reloadData, final int minFilter) {
            reloadData.minFilter = minFilter;
        }
        
        static /* synthetic */ void access$4(final ReloadData reloadData, final int magFilter) {
            reloadData.magFilter = magFilter;
        }
        
        static /* synthetic */ void access$5(final ReloadData reloadData, final ByteBuffer textureBuffer) {
            reloadData.textureBuffer = textureBuffer;
        }
    }
}
