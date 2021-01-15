package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.util.QuadComparator;
import net.minecraft.src.Config;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;

public class WorldRenderer
{
    private ByteBuffer byteBuffer;
    private IntBuffer rawIntBuffer;
    private FloatBuffer rawFloatBuffer;
    private int vertexCount;
    private double textureU;
    private double textureV;
    private int brightness;
    private int color;
    private int rawBufferIndex;

    /** Boolean for whether this renderer needs to be updated or not */
    private boolean needsUpdate;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    private int byteIndex;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private int bufferSize;
    private EnumWorldBlockLayer blockLayer = null;
    private boolean[] drawnIcons = new boolean[256];
    private TextureAtlasSprite[] quadSprites = null;
    private TextureAtlasSprite quadSprite = null;

    public WorldRenderer(int bufferSizeIn)
    {
        this.bufferSize = bufferSizeIn;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        this.vertexFormat = new VertexFormat();
        this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
    }

    private void growBuffer(int bytes)
    {
        LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + this.bufferSize * 4 + " bytes, new size " + (this.bufferSize * 4 + bytes) + " bytes.");
        this.bufferSize += bytes / 4;
        ByteBuffer var2 = GLAllocation.createDirectByteBuffer(this.bufferSize * 4);
        this.rawIntBuffer.position(0);
        var2.asIntBuffer().put(this.rawIntBuffer);
        this.byteBuffer = var2;
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();

        if (this.quadSprites != null)
        {
            TextureAtlasSprite[] sprites = this.quadSprites;
            int quadSize = this.getBufferQuadSize();
            this.quadSprites = new TextureAtlasSprite[quadSize];
            System.arraycopy(sprites, 0, this.quadSprites, 0, Math.min(sprites.length, this.quadSprites.length));
        }
    }

    public WorldRenderer.State getVertexState(float p_178971_1_, float p_178971_2_, float p_178971_3_)
    {
        int[] var4 = new int[this.rawBufferIndex];
        PriorityQueue var5 = new PriorityQueue(this.rawBufferIndex, new QuadComparator(this.rawFloatBuffer, (float)(p_178971_1_ + this.xOffset), (float)(p_178971_2_ + this.yOffset), (float)(p_178971_3_ + this.zOffset), this.vertexFormat.getNextOffset() / 4));
        int var6 = this.vertexFormat.getNextOffset();
        TextureAtlasSprite[] quadSpritesSorted = null;
        int quadStep = this.vertexFormat.getNextOffset() / 4 * 4;

        if (this.quadSprites != null)
        {
            quadSpritesSorted = new TextureAtlasSprite[this.vertexCount / 4];
        }

        int var7;

        for (var7 = 0; var7 < this.rawBufferIndex; var7 += var6)
        {
            var5.add(Integer.valueOf(var7));
        }

        for (var7 = 0; !var5.isEmpty(); var7 += var6)
        {
            int var8 = ((Integer)var5.remove()).intValue();
            int indexQuad;

            for (indexQuad = 0; indexQuad < var6; ++indexQuad)
            {
                var4[var7 + indexQuad] = this.rawIntBuffer.get(var8 + indexQuad);
            }

            if (quadSpritesSorted != null)
            {
                indexQuad = var8 / quadStep;
                int indexQuadSorted = var7 / quadStep;
                quadSpritesSorted[indexQuadSorted] = this.quadSprites[indexQuad];
            }
        }

        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(var4);

        if (this.quadSprites != null)
        {
            System.arraycopy(quadSpritesSorted, 0, this.quadSprites, 0, quadSpritesSorted.length);
        }

        return new WorldRenderer.State(var4, this.rawBufferIndex, this.vertexCount, new VertexFormat(this.vertexFormat), quadSpritesSorted);
    }

    public void setVertexState(WorldRenderer.State state)
    {
        if (state.getRawBuffer().length > this.rawIntBuffer.capacity())
        {
            this.growBuffer(2097152);
        }

        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(state.getRawBuffer());
        this.rawBufferIndex = state.getRawBufferIndex();
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());

        if (state.stateQuadSprites != null)
        {
            if (this.quadSprites == null)
            {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }

            System.arraycopy(state.stateQuadSprites, 0, this.quadSprites, 0, state.stateQuadSprites.length);
        }
        else
        {
            this.quadSprites = null;
        }
    }

    public void reset()
    {
        this.vertexCount = 0;
        this.rawBufferIndex = 0;
        this.vertexFormat.clear();
        this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));

        if (this.blockLayer != null && Config.isMultiTexture())
        {
            if (this.quadSprites == null)
            {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }
        }
        else
        {
            this.quadSprites = null;
        }

        this.quadSprite = null;
    }

    public void startDrawingQuads()
    {
        this.startDrawing(7);
    }

    public void startDrawing(int mode)
    {
        if (this.isDrawing)
        {
            throw new IllegalStateException("Already building!");
        }
        else
        {
            this.isDrawing = true;
            this.reset();
            this.drawMode = mode;
            this.needsUpdate = false;
        }
    }

    public void setTextureUV(double u, double v)
    {
        if (!this.vertexFormat.hasElementOffset(0) && !this.vertexFormat.hasElementOffset(1))
        {
            VertexFormatElement var5 = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2);
            this.vertexFormat.setElement(var5);
        }

        this.textureU = u;
        this.textureV = v;
    }

    public void setBrightness(int brightnessIn)
    {
        if (!this.vertexFormat.hasElementOffset(1))
        {
            if (!this.vertexFormat.hasElementOffset(0))
            {
                this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
            }

            VertexFormatElement var2 = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUseage.UV, 2);
            this.vertexFormat.setElement(var2);
        }

        this.brightness = brightnessIn;
    }

    public void setColorOpaque_F(float red, float green, float blue)
    {
        this.setColorOpaque((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F));
    }

    public void setColorRGBA_F(float red, float green, float blue, float alpha)
    {
        this.setColorRGBA((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), (int)(alpha * 255.0F));
    }

    /**
     * Set the color.
     */
    public void setColorOpaque(int red, int green, int blue)
    {
        this.setColorRGBA(red, green, blue, 255);
    }

    public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_)
    {
        int var5 = (this.vertexCount - 4) * (this.vertexFormat.getNextOffset() / 4) + this.vertexFormat.getElementOffsetById(1) / 4;
        int var6 = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(var5, p_178962_1_);
        this.rawIntBuffer.put(var5 + var6, p_178962_2_);
        this.rawIntBuffer.put(var5 + var6 * 2, p_178962_3_);
        this.rawIntBuffer.put(var5 + var6 * 3, p_178962_4_);
    }

    public void putPosition(double x, double y, double z)
    {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.getNextOffset())
        {
            this.growBuffer(2097152);
        }

        int var7 = this.vertexFormat.getNextOffset() / 4;
        int var8 = (this.vertexCount - 4) * var7;

        for (int var9 = 0; var9 < 4; ++var9)
        {
            int var10 = var8 + var9 * var7;
            int var11 = var10 + 1;
            int var12 = var11 + 1;
            this.rawIntBuffer.put(var10, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var10))));
            this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var11))));
            this.rawIntBuffer.put(var12, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var12))));
        }
    }

    /**
     * Takes in the pass the call list is being requested for. Args: renderPass
     */
    private int getColorIndex(int p_78909_1_)
    {
        return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }

    public void putColorMultiplier(float red, float green, float blue, int p_178978_4_)
    {
        int var5 = this.getColorIndex(p_178978_4_);
        int var6 = this.rawIntBuffer.get(var5);
        int var7;
        int var8;
        int var9;

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            var7 = (int)((var6 & 255) * red);
            var8 = (int)((var6 >> 8 & 255) * green);
            var9 = (int)((var6 >> 16 & 255) * blue);
            var6 &= -16777216;
            var6 |= var9 << 16 | var8 << 8 | var7;
        }
        else
        {
            var7 = (int)((this.color >> 24 & 255) * red);
            var8 = (int)((this.color >> 16 & 255) * green);
            var9 = (int)((this.color >> 8 & 255) * blue);
            var6 &= 255;
            var6 |= var7 << 24 | var8 << 16 | var9 << 8;
        }

        if (this.needsUpdate)
        {
            var6 = -1;
        }

        this.rawIntBuffer.put(var5, var6);
    }

    private void putColor(int argb, int p_178988_2_)
    {
        int var3 = this.getColorIndex(p_178988_2_);
        int var4 = argb >> 16 & 255;
        int var5 = argb >> 8 & 255;
        int var6 = argb & 255;
        int var7 = argb >> 24 & 255;
        this.putColorRGBA(var3, var4, var5, var6, var7);
    }

    public void putColorRGB_F(float red, float green, float blue, int p_178994_4_)
    {
        int var5 = this.getColorIndex(p_178994_4_);
        int var6 = MathHelper.clamp_int((int)(red * 255.0F), 0, 255);
        int var7 = MathHelper.clamp_int((int)(green * 255.0F), 0, 255);
        int var8 = MathHelper.clamp_int((int)(blue * 255.0F), 0, 255);
        this.putColorRGBA(var5, var6, var7, var8, 255);
    }

    private void putColorRGBA(int index, int red, int p_178972_3_, int p_178972_4_, int p_178972_5_)
    {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            this.rawIntBuffer.put(index, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | red);
        }
        else
        {
            this.rawIntBuffer.put(index, red << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
        }
    }

    public void setColorRGBA(int red, int green, int blue, int alpha)
    {
        if (!this.needsUpdate)
        {
            if (red > 255)
            {
                red = 255;
            }

            if (green > 255)
            {
                green = 255;
            }

            if (blue > 255)
            {
                blue = 255;
            }

            if (alpha > 255)
            {
                alpha = 255;
            }

            if (red < 0)
            {
                red = 0;
            }

            if (green < 0)
            {
                green = 0;
            }

            if (blue < 0)
            {
                blue = 0;
            }

            if (alpha < 0)
            {
                alpha = 0;
            }

            if (!this.vertexFormat.hasColor())
            {
                VertexFormatElement var5 = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4);
                this.vertexFormat.setElement(var5);
            }

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            {
                this.color = alpha << 24 | blue << 16 | green << 8 | red;
            }
            else
            {
                this.color = red << 24 | green << 16 | blue << 8 | alpha;
            }
        }
    }

    public void setColorOpaque_B(byte red, byte green, byte blue)
    {
        this.setColorOpaque(red & 255, green & 255, blue & 255);
    }

    public void addVertexWithUV(double x, double y, double z, double u, double v)
    {
        if (this.quadSprite != null && this.quadSprites != null)
        {
            u = this.quadSprite.toSingleU((float)u);
            v = this.quadSprite.toSingleV((float)v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }

        this.setTextureUV(u, v);
        this.addVertex(x, y, z);
    }

    public void setVertexFormat(VertexFormat newFormat)
    {
        this.vertexFormat = new VertexFormat(newFormat);
    }

    public void addVertexData(int[] vertexData)
    {
        int var2 = this.vertexFormat.getNextOffset() / 4;
        this.vertexCount += vertexData.length / var2;
        this.rawIntBuffer.position(this.rawBufferIndex);
        this.rawIntBuffer.put(vertexData);
        this.rawBufferIndex += vertexData.length;
    }

    public void addVertex(double x, double y, double z)
    {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.getNextOffset())
        {
            this.growBuffer(2097152);
        }

        List var7 = this.vertexFormat.getElements();
        int listSize = var7.size();

        for (int i = 0; i < listSize; ++i)
        {
            VertexFormatElement var9 = (VertexFormatElement)var7.get(i);
            int var10 = var9.getOffset() >> 2;
            int var11 = this.rawBufferIndex + var10;

            switch (WorldRenderer.SwitchEnumUseage.VALUES[var9.getUsage().ordinal()])
            {
                case 1:
                    this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float)(x + this.xOffset)));
                    this.rawIntBuffer.put(var11 + 1, Float.floatToRawIntBits((float)(y + this.yOffset)));
                    this.rawIntBuffer.put(var11 + 2, Float.floatToRawIntBits((float)(z + this.zOffset)));
                    break;

                case 2:
                    this.rawIntBuffer.put(var11, this.color);
                    break;

                case 3:
                    if (var9.getIndex() == 0)
                    {
                        this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float)this.textureU));
                        this.rawIntBuffer.put(var11 + 1, Float.floatToRawIntBits((float)this.textureV));
                    }
                    else
                    {
                        this.rawIntBuffer.put(var11, this.brightness);
                    }

                    break;

                case 4:
                    this.rawIntBuffer.put(var11, this.normal);
            }
        }

        this.rawBufferIndex += this.vertexFormat.getNextOffset() >> 2;
        ++this.vertexCount;
    }

    public void setColorOpaque_I(int rgb)
    {
        int var2 = rgb >> 16 & 255;
        int var3 = rgb >> 8 & 255;
        int var4 = rgb & 255;
        this.setColorOpaque(var2, var3, var4);
    }

    public void setColorRGBA_I(int rgb, int alpha)
    {
        int var3 = rgb >> 16 & 255;
        int var4 = rgb >> 8 & 255;
        int var5 = rgb & 255;
        this.setColorRGBA(var3, var4, var5, alpha);
    }

    /**
     * Marks the current renderer data as dirty and needing to be updated.
     */
    public void markDirty()
    {
        this.needsUpdate = true;
    }

    public void setNormal(float x, float y, float z)
    {
        if (!this.vertexFormat.hasNormal())
        {
            VertexFormatElement var7 = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.NORMAL, 3);
            this.vertexFormat.setElement(var7);
            this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.PADDING, 1));
        }

        byte var71 = (byte)((int)(x * 127.0F));
        byte var5 = (byte)((int)(y * 127.0F));
        byte var6 = (byte)((int)(z * 127.0F));
        this.normal = var71 & 255 | (var5 & 255) << 8 | (var6 & 255) << 16;
    }

    public void putNormal(float x, float y, float z)
    {
        byte var4 = (byte)((int)(x * 127.0F));
        byte var5 = (byte)((int)(y * 127.0F));
        byte var6 = (byte)((int)(z * 127.0F));
        int var7 = this.vertexFormat.getNextOffset() >> 2;
        int var8 = (this.vertexCount - 4) * var7 + this.vertexFormat.getNormalOffset() / 4;
        this.normal = var4 & 255 | (var5 & 255) << 8 | (var6 & 255) << 16;
        this.rawIntBuffer.put(var8, this.normal);
        this.rawIntBuffer.put(var8 + var7, this.normal);
        this.rawIntBuffer.put(var8 + var7 * 2, this.normal);
        this.rawIntBuffer.put(var8 + var7 * 3, this.normal);
    }

    public void setTranslation(double x, double y, double z)
    {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }

    public int finishDrawing()
    {
        if (!this.isDrawing)
        {
            throw new IllegalStateException("Not building!");
        }
        else
        {
            this.isDrawing = false;

            if (this.vertexCount > 0)
            {
                this.byteBuffer.position(0);
                this.byteBuffer.limit(this.rawBufferIndex * 4);
            }

            this.byteIndex = this.rawBufferIndex * 4;
            return this.byteIndex;
        }
    }

    public int getByteIndex()
    {
        return this.byteIndex;
    }

    public ByteBuffer getByteBuffer()
    {
        return this.byteBuffer;
    }

    public VertexFormat getVertexFormat()
    {
        return this.vertexFormat;
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }

    public int getDrawMode()
    {
        return this.drawMode;
    }

    public void putColor4(int argb)
    {
        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.putColor(argb, var2 + 1);
        }
    }

    public void putColorRGB_F4(float red, float green, float blue)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            this.putColorRGB_F(red, green, blue, var4 + 1);
        }
    }

    public void putSprite(TextureAtlasSprite sprite)
    {
        if (this.quadSprites != null)
        {
            int countQuads = this.vertexCount / 4;
            this.quadSprites[countQuads - 1] = sprite;
        }
    }

    public void setSprite(TextureAtlasSprite sprite)
    {
        if (this.quadSprites != null)
        {
            this.quadSprite = sprite;
        }
    }

    public boolean isMultiTexture()
    {
        return this.quadSprites != null;
    }

    public void drawMultiTexture()
    {
        if (this.quadSprites != null)
        {
            int maxTextureIndex = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();

            if (this.drawnIcons.length <= maxTextureIndex)
            {
                this.drawnIcons = new boolean[maxTextureIndex + 1];
            }

            Arrays.fill(this.drawnIcons, false);
            int texSwitch = 0;
            int grassOverlayIndex = -1;
            int countQuads = this.vertexCount / 4;

            for (int i = 0; i < countQuads; ++i)
            {
                TextureAtlasSprite icon = this.quadSprites[i];

                if (icon != null)
                {
                    int iconIndex = icon.getIndexInMap();

                    if (!this.drawnIcons[iconIndex])
                    {
                        if (icon == TextureUtils.iconGrassSideOverlay)
                        {
                            if (grassOverlayIndex < 0)
                            {
                                grassOverlayIndex = i;
                            }
                        }
                        else
                        {
                            i = this.drawForIcon(icon, i) - 1;
                            ++texSwitch;

                            if (this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT)
                            {
                                this.drawnIcons[iconIndex] = true;
                            }
                        }
                    }
                }
            }

            if (grassOverlayIndex >= 0)
            {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, grassOverlayIndex);
                ++texSwitch;
            }

            if (texSwitch > 0)
            {
                TextureUtils.bindTexture(Config.getMinecraft().getTextureMapBlocks().getGlTextureId());
            }
        }
    }

    private int drawForIcon(TextureAtlasSprite sprite, int startQuadPos)
    {
        sprite.bindSpriteTexture();
        int firstRegionEnd = -1;
        int lastPos = -1;
        int countQuads = this.vertexCount / 4;

        for (int i = startQuadPos; i < countQuads; ++i)
        {
            TextureAtlasSprite ts = this.quadSprites[i];

            if (ts == sprite)
            {
                if (lastPos < 0)
                {
                    lastPos = i;
                }
            }
            else if (lastPos >= 0)
            {
                this.draw(lastPos, i);

                if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT)
                {
                    return i;
                }

                lastPos = -1;

                if (firstRegionEnd < 0)
                {
                    firstRegionEnd = i;
                }
            }
        }

        if (lastPos >= 0)
        {
            this.draw(lastPos, countQuads);
        }

        if (firstRegionEnd < 0)
        {
            firstRegionEnd = countQuads;
        }

        return firstRegionEnd;
    }

    private void draw(int startQuadVertex, int endQuadVertex)
    {
        int vxQuadCount = endQuadVertex - startQuadVertex;

        if (vxQuadCount > 0)
        {
            int startVertex = startQuadVertex * 4;
            int vxCount = vxQuadCount * 4;
            GL11.glDrawArrays(this.drawMode, startVertex, vxCount);
        }
    }

    public void setBlockLayer(EnumWorldBlockLayer blockLayer)
    {
        this.blockLayer = blockLayer;
    }

    private int getBufferQuadSize()
    {
        int quadSize = this.bufferSize * 4 / (this.vertexFormat.getNextOffset() * 4);
        return quadSize;
    }

    public class State
    {
        private final int[] stateRawBuffer;
        private final int stateRawBufferIndex;
        private final int stateVertexCount;
        private final VertexFormat stateVertexFormat;
        public TextureAtlasSprite[] stateQuadSprites;

        public State(int[] p_i46274_2_, int p_i46274_3_, int p_i46274_4_, VertexFormat p_i46274_5_, TextureAtlasSprite[] sprites)
        {
            this.stateRawBuffer = p_i46274_2_;
            this.stateRawBufferIndex = p_i46274_3_;
            this.stateVertexCount = p_i46274_4_;
            this.stateVertexFormat = p_i46274_5_;
            this.stateQuadSprites = sprites;
        }

        public int[] getRawBuffer()
        {
            return this.stateRawBuffer;
        }

        public int getRawBufferIndex()
        {
            return this.stateRawBufferIndex;
        }

        public int getVertexCount()
        {
            return this.stateVertexCount;
        }

        public VertexFormat getVertexFormat()
        {
            return this.stateVertexFormat;
        }
    }

    static final class SwitchEnumUseage
    {
        static final int[] VALUES = new int[VertexFormatElement.EnumUseage.values().length];

        static
        {
            try
            {
                VALUES[VertexFormatElement.EnumUseage.POSITION.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                VALUES[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                VALUES[VertexFormatElement.EnumUseage.UV.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                VALUES[VertexFormatElement.EnumUseage.NORMAL.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
