package net.minecraft.entity.ai.attributes;

import net.minecraft.util.MathHelper;

public class RangedAttribute extends BaseAttribute {
    private final double minimumValue;
    private final double maximumValue;
    private String description;
    private static final String __OBFID = "CL_00001568";

    public RangedAttribute(IAttribute p_i45891_1_, String p_i45891_2_, double p_i45891_3_, double p_i45891_5_, double p_i45891_7_) {
        super(p_i45891_1_, p_i45891_2_, p_i45891_3_);
        this.minimumValue = p_i45891_5_;
        this.maximumValue = p_i45891_7_;

        if (p_i45891_5_ > p_i45891_7_) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        } else if (p_i45891_3_ < p_i45891_5_) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        } else if (p_i45891_3_ > p_i45891_7_) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public RangedAttribute setDescription(String p_111117_1_) {
        this.description = p_111117_1_;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public double clampValue(double p_111109_1_) {
        p_111109_1_ = MathHelper.clamp_double(p_111109_1_, this.minimumValue, this.maximumValue);
        return p_111109_1_;
    }
}
