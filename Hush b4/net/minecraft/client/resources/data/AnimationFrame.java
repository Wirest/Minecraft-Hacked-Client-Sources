// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

public class AnimationFrame
{
    private final int frameIndex;
    private final int frameTime;
    
    public AnimationFrame(final int p_i1307_1_) {
        this(p_i1307_1_, -1);
    }
    
    public AnimationFrame(final int p_i1308_1_, final int p_i1308_2_) {
        this.frameIndex = p_i1308_1_;
        this.frameTime = p_i1308_2_;
    }
    
    public boolean hasNoTime() {
        return this.frameTime == -1;
    }
    
    public int getFrameTime() {
        return this.frameTime;
    }
    
    public int getFrameIndex() {
        return this.frameIndex;
    }
}
