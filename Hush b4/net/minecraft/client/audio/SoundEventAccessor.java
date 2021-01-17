// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

public class SoundEventAccessor implements ISoundEventAccessor<SoundPoolEntry>
{
    private final SoundPoolEntry entry;
    private final int weight;
    
    SoundEventAccessor(final SoundPoolEntry entry, final int weight) {
        this.entry = entry;
        this.weight = weight;
    }
    
    @Override
    public int getWeight() {
        return this.weight;
    }
    
    @Override
    public SoundPoolEntry cloneEntry() {
        return new SoundPoolEntry(this.entry);
    }
}
