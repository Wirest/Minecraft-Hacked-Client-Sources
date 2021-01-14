package store.shadowclient.client.utils.render;


import java.nio.*;

public class TextureData
{
    private int textureId;
    private int width;
    private int height;
    private ByteBuffer buffer;
    
    public TextureData(final int textureId, final int width, final int height, final ByteBuffer buffer) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }
    
    public int getTextureId() {
        return this.textureId;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public ByteBuffer getBuffer() {
        return this.buffer;
    }
}

