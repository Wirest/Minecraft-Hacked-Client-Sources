package net.minecraft.client.renderer;

import net.optifine.SmartAnimations;

public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    /** The static instance of the Tessellator. */
    private static final Tessellator instance = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return instance;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new WorldRenderer(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        if (this.worldRenderer.animatedSprites != null)
        {
            SmartAnimations.spritesRendered(this.worldRenderer.animatedSprites);
        }

        this.worldRenderer.finishDrawing();
        this.vboUploader.func_181679_a(this.worldRenderer);
    }

    public WorldRenderer getWorldRenderer()
    {
        return this.worldRenderer;
    }
}
