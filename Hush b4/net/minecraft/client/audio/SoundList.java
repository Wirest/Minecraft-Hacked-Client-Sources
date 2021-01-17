// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;

public class SoundList
{
    private final List<SoundEntry> soundList;
    private boolean replaceExisting;
    private SoundCategory category;
    
    public SoundList() {
        this.soundList = (List<SoundEntry>)Lists.newArrayList();
    }
    
    public List<SoundEntry> getSoundList() {
        return this.soundList;
    }
    
    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }
    
    public void setReplaceExisting(final boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }
    
    public SoundCategory getSoundCategory() {
        return this.category;
    }
    
    public void setSoundCategory(final SoundCategory soundCat) {
        this.category = soundCat;
    }
    
    public static class SoundEntry
    {
        private String name;
        private float volume;
        private float pitch;
        private int weight;
        private Type type;
        private boolean streaming;
        
        public SoundEntry() {
            this.volume = 1.0f;
            this.pitch = 1.0f;
            this.weight = 1;
            this.type = Type.FILE;
            this.streaming = false;
        }
        
        public String getSoundEntryName() {
            return this.name;
        }
        
        public void setSoundEntryName(final String nameIn) {
            this.name = nameIn;
        }
        
        public float getSoundEntryVolume() {
            return this.volume;
        }
        
        public void setSoundEntryVolume(final float volumeIn) {
            this.volume = volumeIn;
        }
        
        public float getSoundEntryPitch() {
            return this.pitch;
        }
        
        public void setSoundEntryPitch(final float pitchIn) {
            this.pitch = pitchIn;
        }
        
        public int getSoundEntryWeight() {
            return this.weight;
        }
        
        public void setSoundEntryWeight(final int weightIn) {
            this.weight = weightIn;
        }
        
        public Type getSoundEntryType() {
            return this.type;
        }
        
        public void setSoundEntryType(final Type typeIn) {
            this.type = typeIn;
        }
        
        public boolean isStreaming() {
            return this.streaming;
        }
        
        public void setStreaming(final boolean isStreaming) {
            this.streaming = isStreaming;
        }
        
        public enum Type
        {
            FILE("FILE", 0, "file"), 
            SOUND_EVENT("SOUND_EVENT", 1, "event");
            
            private final String field_148583_c;
            
            private Type(final String name, final int ordinal, final String p_i45109_3_) {
                this.field_148583_c = p_i45109_3_;
            }
            
            public static Type getType(final String p_148580_0_) {
                Type[] values;
                for (int length = (values = values()).length, i = 0; i < length; ++i) {
                    final Type soundlist$soundentry$type = values[i];
                    if (soundlist$soundentry$type.field_148583_c.equals(p_148580_0_)) {
                        return soundlist$soundentry$type;
                    }
                }
                return null;
            }
        }
    }
}
