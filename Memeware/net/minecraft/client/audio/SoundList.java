package net.minecraft.client.audio;

import com.google.common.collect.Lists;

import java.util.List;

public class SoundList {
    private final List soundList = Lists.newArrayList();

    /**
     * if true it will override all the sounds from the resourcepacks loaded before
     */
    private boolean replaceExisting;
    private SoundCategory category;
    private static final String __OBFID = "CL_00001121";

    public List getSoundList() {
        return this.soundList;
    }

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    public void setReplaceExisting(boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    public void setSoundCategory(SoundCategory p_148571_1_) {
        this.category = p_148571_1_;
    }

    public static class SoundEntry {
        private String name;
        private float volume = 1.0F;
        private float pitch = 1.0F;
        private int field_148565_d = 1;
        private SoundList.SoundEntry.Type field_148566_e;
        private boolean field_148564_f;
        private static final String __OBFID = "CL_00001122";

        public SoundEntry() {
            this.field_148566_e = SoundList.SoundEntry.Type.FILE;
            this.field_148564_f = false;
        }

        public String getSoundEntryName() {
            return this.name;
        }

        public void setSoundEntryName(String p_148561_1_) {
            this.name = p_148561_1_;
        }

        public float getSoundEntryVolume() {
            return this.volume;
        }

        public void setSoundEntryVolume(float p_148553_1_) {
            this.volume = p_148553_1_;
        }

        public float getSoundEntryPitch() {
            return this.pitch;
        }

        public void setSoundEntryPitch(float p_148559_1_) {
            this.pitch = p_148559_1_;
        }

        public int getSoundEntryWeight() {
            return this.field_148565_d;
        }

        public void setSoundEntryWeight(int p_148554_1_) {
            this.field_148565_d = p_148554_1_;
        }

        public SoundList.SoundEntry.Type getSoundEntryType() {
            return this.field_148566_e;
        }

        public void setSoundEntryType(SoundList.SoundEntry.Type p_148562_1_) {
            this.field_148566_e = p_148562_1_;
        }

        public boolean isStreaming() {
            return this.field_148564_f;
        }

        public void setStreaming(boolean p_148557_1_) {
            this.field_148564_f = p_148557_1_;
        }

        public static enum Type {
            FILE("FILE", 0, "file"),
            SOUND_EVENT("SOUND_EVENT", 1, "event");
            private final String field_148583_c;

            private static final SoundList.SoundEntry.Type[] $VALUES = new SoundList.SoundEntry.Type[]{FILE, SOUND_EVENT};
            private static final String __OBFID = "CL_00001123";

            private Type(String p_i45109_1_, int p_i45109_2_, String p_i45109_3_) {
                this.field_148583_c = p_i45109_3_;
            }

            public static SoundList.SoundEntry.Type getType(String p_148580_0_) {
                SoundList.SoundEntry.Type[] var1 = values();
                int var2 = var1.length;

                for (int var3 = 0; var3 < var2; ++var3) {
                    SoundList.SoundEntry.Type var4 = var1[var3];

                    if (var4.field_148583_c.equals(p_148580_0_)) {
                        return var4;
                    }
                }

                return null;
            }
        }
    }
}
