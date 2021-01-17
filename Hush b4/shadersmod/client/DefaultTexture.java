// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.AbstractTexture;

public class DefaultTexture extends AbstractTexture
{
    public DefaultTexture() {
        this.loadTexture(null);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourcemanager) {
        final int[] aint = ShadersTex.createAIntImage(1, -1);
        ShadersTex.setupTexture(this.getMultiTexID(), aint, 1, 1, false, false);
    }
}
