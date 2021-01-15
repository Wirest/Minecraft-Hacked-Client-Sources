package net.minecraft.client.audio;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.util.RegistrySimple;

public class SoundRegistry extends RegistrySimple
{
    /** Contains all registered sound */
    private Map soundRegistry;

    /**
     * Creates the Map we will use to map keys to their registered values.
     */
    @Override
	protected Map createUnderlyingMap()
    {
        this.soundRegistry = Maps.newHashMap();
        return this.soundRegistry;
    }

    public void registerSound(SoundEventAccessorComposite p_148762_1_)
    {
        this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
    }

    /**
     * Reset the underlying sound map (Called on resource manager reload)
     */
    public void clearMap()
    {
        this.soundRegistry.clear();
    }
}
