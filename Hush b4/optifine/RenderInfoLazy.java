// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class RenderInfoLazy
{
    private RenderChunk renderChunk;
    private RenderGlobal.ContainerLocalRenderInformation renderInfo;
    
    public RenderChunk getRenderChunk() {
        return this.renderChunk;
    }
    
    public void setRenderChunk(final RenderChunk p_setRenderChunk_1_) {
        this.renderChunk = p_setRenderChunk_1_;
        this.renderInfo = null;
    }
    
    public RenderGlobal.ContainerLocalRenderInformation getRenderInfo() {
        if (this.renderInfo == null) {
            this.renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this.renderChunk, null, 0);
        }
        return this.renderInfo;
    }
}
