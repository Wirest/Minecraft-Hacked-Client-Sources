package optifine;

import java.nio.ByteBuffer;
import java.util.Properties;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureAnimation
{
    private String srcTex = null;
    private String dstTex = null;
    ResourceLocation dstTexLoc = null;
    private int dstTextId = -1;
    private int dstX = 0;
    private int dstY = 0;
    private int frameWidth = 0;
    private int frameHeight = 0;
    private TextureAnimationFrame[] frames = null;
    private int activeFrame = 0;
    byte[] srcData = null;
    private ByteBuffer imageData = null;

    public TextureAnimation(String texFrom, byte[] srcData, String texTo, ResourceLocation locTexTo, int dstX, int dstY, int frameWidth, int frameHeight, Properties props, int durDef)
    {
        this.srcTex = texFrom;
        this.dstTex = texTo;
        this.dstTexLoc = locTexTo;
        this.dstX = dstX;
        this.dstY = dstY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        int frameLen = frameWidth * frameHeight * 4;

        if (srcData.length % frameLen != 0)
        {
            Config.warn("Invalid animated texture length: " + srcData.length + ", frameWidth: " + frameWidth + ", frameHeight: " + frameHeight);
        }

        this.srcData = srcData;
        int numFrames = srcData.length / frameLen;

        if (props.get("tile.0") != null)
        {
            for (int durationDefStr = 0; props.get("tile." + durationDefStr) != null; ++durationDefStr)
            {
                numFrames = durationDefStr + 1;
            }
        }

        String var21 = (String)props.get("duration");
        int durationDef = Config.parseInt(var21, durDef);
        this.frames = new TextureAnimationFrame[numFrames];

        for (int i = 0; i < this.frames.length; ++i)
        {
            String indexStr = (String)props.get("tile." + i);
            int index = Config.parseInt(indexStr, i);
            String durationStr = (String)props.get("duration." + i);
            int duration = Config.parseInt(durationStr, durationDef);
            TextureAnimationFrame frm = new TextureAnimationFrame(index, duration);
            this.frames[i] = frm;
        }
    }

    public boolean nextFrame()
    {
        if (this.frames.length <= 0)
        {
            return false;
        }
        else
        {
            if (this.activeFrame >= this.frames.length)
            {
                this.activeFrame = 0;
            }

            TextureAnimationFrame frame = this.frames[this.activeFrame];
            ++frame.counter;

            if (frame.counter < frame.duration)
            {
                return false;
            }
            else
            {
                frame.counter = 0;
                ++this.activeFrame;

                if (this.activeFrame >= this.frames.length)
                {
                    this.activeFrame = 0;
                }

                return true;
            }
        }
    }

    public int getActiveFrameIndex()
    {
        if (this.frames.length <= 0)
        {
            return 0;
        }
        else
        {
            if (this.activeFrame >= this.frames.length)
            {
                this.activeFrame = 0;
            }

            TextureAnimationFrame frame = this.frames[this.activeFrame];
            return frame.index;
        }
    }

    public int getFrameCount()
    {
        return this.frames.length;
    }

    public boolean updateTexture()
    {
        if (this.dstTextId < 0)
        {
            ITextureObject frameLen = TextureUtils.getTexture(this.dstTexLoc);

            if (frameLen == null)
            {
                return false;
            }

            this.dstTextId = frameLen.getGlTextureId();
        }

        if (this.imageData == null)
        {
            this.imageData = GLAllocation.createDirectByteBuffer(this.srcData.length);
            this.imageData.put(this.srcData);
            this.srcData = null;
        }

        if (!this.nextFrame())
        {
            return false;
        }
        else
        {
            int frameLen1 = this.frameWidth * this.frameHeight * 4;
            int imgNum = this.getActiveFrameIndex();
            int offset = frameLen1 * imgNum;

            if (offset + frameLen1 > this.imageData.capacity())
            {
                return false;
            }
            else
            {
                this.imageData.position(offset);
                GlStateManager.func_179144_i(this.dstTextId);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, this.dstX, this.dstY, this.frameWidth, this.frameHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                return true;
            }
        }
    }

    public String getSrcTex()
    {
        return this.srcTex;
    }

    public String getDstTex()
    {
        return this.dstTex;
    }

    public ResourceLocation getDstTexLoc()
    {
        return this.dstTexLoc;
    }
}
