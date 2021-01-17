// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound
{
    ResourceLocation getSoundLocation();
    
    boolean canRepeat();
    
    int getRepeatDelay();
    
    float getVolume();
    
    float getPitch();
    
    float getXPosF();
    
    float getYPosF();
    
    float getZPosF();
    
    AttenuationType getAttenuationType();
    
    public enum AttenuationType
    {
        NONE("NONE", 0, 0), 
        LINEAR("LINEAR", 1, 2);
        
        private final int type;
        
        private AttenuationType(final String name, final int ordinal, final int typeIn) {
            this.type = typeIn;
        }
        
        public int getTypeInt() {
            return this.type;
        }
    }
}
