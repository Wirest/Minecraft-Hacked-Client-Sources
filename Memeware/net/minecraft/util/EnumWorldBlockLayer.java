package net.minecraft.util;

public enum EnumWorldBlockLayer {
    SOLID("SOLID", 0, "Solid"),
    CUTOUT_MIPPED("CUTOUT_MIPPED", 1, "Mipped Cutout"),
    CUTOUT("CUTOUT", 2, "Cutout"),
    TRANSLUCENT("TRANSLUCENT", 3, "Translucent");
    private final String field_180338_e;

    private static final EnumWorldBlockLayer[] $VALUES = new EnumWorldBlockLayer[]{SOLID, CUTOUT_MIPPED, CUTOUT, TRANSLUCENT};
    private static final String __OBFID = "CL_00002152";

    private EnumWorldBlockLayer(String p_i45755_1_, int p_i45755_2_, String p_i45755_3_) {
        this.field_180338_e = p_i45755_3_;
    }

    public String toString() {
        return this.field_180338_e;
    }
}
