// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.RegistrySimple;

public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessorComposite>
{
    private Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry;
    
    @Override
    protected Map<ResourceLocation, SoundEventAccessorComposite> createUnderlyingMap() {
        return this.soundRegistry = (Map<ResourceLocation, SoundEventAccessorComposite>)Maps.newHashMap();
    }
    
    public void registerSound(final SoundEventAccessorComposite p_148762_1_) {
        this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
    }
    
    public void clearMap() {
        this.soundRegistry.clear();
    }
}
