package net.minecraft.util;

public class Tuple {
    private Object a;
    private Object b;
    private static final String __OBFID = "CL_00001502";

    public Tuple(Object p_i1555_1_, Object p_i1555_2_) {
        this.a = p_i1555_1_;
        this.b = p_i1555_2_;
    }

    /**
     * Get the first Object in the Tuple
     */
    public Object getFirst() {
        return this.a;
    }

    /**
     * Get the second Object in the Tuple
     */
    public Object getSecond() {
        return this.b;
    }
}
