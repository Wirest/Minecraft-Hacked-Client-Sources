// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

public interface ISoundEventAccessor<T>
{
    int getWeight();
    
    T cloneEntry();
}
