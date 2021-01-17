// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexFormatElement extends VertexFormatElement
{
    int sUsage;
    
    public SVertexFormatElement(final int sUsage, final EnumType type, final int count) {
        super(0, type, EnumUsage.PADDING, count);
        this.sUsage = sUsage;
    }
}
