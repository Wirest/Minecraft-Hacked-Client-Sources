// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.base.Predicate;
import java.util.Random;
import com.google.common.collect.Maps;
import java.util.Map;

public enum EnumFacing implements IStringSerializable
{
    DOWN("DOWN", 0, "DOWN", 0, 0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)), 
    UP("UP", 1, "UP", 1, 1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)), 
    NORTH("NORTH", 2, "NORTH", 2, 2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)), 
    SOUTH("SOUTH", 3, "SOUTH", 3, 3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)), 
    WEST("WEST", 4, "WEST", 4, 4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)), 
    EAST("EAST", 5, "EAST", 5, 5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
    
    private final int index;
    private final int opposite;
    private final int horizontalIndex;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;
    private final Vec3i directionVec;
    public static final EnumFacing[] VALUES;
    private static final EnumFacing[] HORIZONTALS;
    private static final Map NAME_LOOKUP;
    private static final EnumFacing[] $VALUES;
    private static final String __OBFID = "CL_00001201";
    
    static {
        VALUES = new EnumFacing[6];
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        $VALUES = new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST };
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            EnumFacing.VALUES[enumfacing.index] = enumfacing;
            if (enumfacing.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }
            EnumFacing.NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
        }
    }
    
    private EnumFacing(final String name, final int ordinal, final String p_i17_3_, final int p_i17_4_, final int p_i17_5_, final int p_i17_6_, final int p_i17_7_, final String p_i17_8_, final AxisDirection p_i17_9_, final Axis p_i17_10_, final Vec3i p_i17_11_) {
        this.index = p_i17_5_;
        this.horizontalIndex = p_i17_7_;
        this.opposite = p_i17_6_;
        this.name = p_i17_8_;
        this.axis = p_i17_10_;
        this.axisDirection = p_i17_9_;
        this.directionVec = p_i17_11_;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }
    
    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }
    
    public EnumFacing getOpposite() {
        return EnumFacing.VALUES[this.opposite];
    }
    
    public EnumFacing rotateAround(final Axis axis) {
        switch (EnumFacing$1.field_179515_a[axis.ordinal()]) {
            case 1: {
                if (this != EnumFacing.WEST && this != EnumFacing.EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case 2: {
                if (this != EnumFacing.UP && this != EnumFacing.DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case 3: {
                if (this != EnumFacing.NORTH && this != EnumFacing.SOUTH) {
                    return this.rotateZ();
                }
                return this;
            }
            default: {
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
            }
        }
    }
    
    public EnumFacing rotateY() {
        switch (EnumFacing$1.field_179513_b[this.ordinal()]) {
            case 1: {
                return EnumFacing.EAST;
            }
            case 2: {
                return EnumFacing.SOUTH;
            }
            case 3: {
                return EnumFacing.WEST;
            }
            case 4: {
                return EnumFacing.NORTH;
            }
            default: {
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
            }
        }
    }
    
    private EnumFacing rotateX() {
        switch (EnumFacing$1.field_179513_b[this.ordinal()]) {
            case 1: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            }
            case 3: {
                return EnumFacing.UP;
            }
            case 5: {
                return EnumFacing.NORTH;
            }
            case 6: {
                return EnumFacing.SOUTH;
            }
        }
    }
    
    private EnumFacing rotateZ() {
        switch (EnumFacing$1.field_179513_b[this.ordinal()]) {
            case 2: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            }
            case 4: {
                return EnumFacing.UP;
            }
            case 5: {
                return EnumFacing.EAST;
            }
            case 6: {
                return EnumFacing.WEST;
            }
        }
    }
    
    public EnumFacing rotateYCCW() {
        switch (EnumFacing$1.field_179513_b[this.ordinal()]) {
            case 1: {
                return EnumFacing.WEST;
            }
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.EAST;
            }
            case 4: {
                return EnumFacing.SOUTH;
            }
            default: {
                throw new IllegalStateException("Unable to get CCW facing of " + this);
            }
        }
    }
    
    public int getFrontOffsetX() {
        return (this.axis == Axis.X) ? this.axisDirection.getOffset() : 0;
    }
    
    public int getFrontOffsetY() {
        return (this.axis == Axis.Y) ? this.axisDirection.getOffset() : 0;
    }
    
    public int getFrontOffsetZ() {
        return (this.axis == Axis.Z) ? this.axisDirection.getOffset() : 0;
    }
    
    public String getName2() {
        return this.name;
    }
    
    public Axis getAxis() {
        return this.axis;
    }
    
    public static EnumFacing byName(final String name) {
        return (name == null) ? null : EnumFacing.NAME_LOOKUP.get(name.toLowerCase());
    }
    
    public static EnumFacing getFront(final int index) {
        return EnumFacing.VALUES[MathHelper.abs_int(index % EnumFacing.VALUES.length)];
    }
    
    public static EnumFacing getHorizontal(final int p_176731_0_) {
        return EnumFacing.HORIZONTALS[MathHelper.abs_int(p_176731_0_ % EnumFacing.HORIZONTALS.length)];
    }
    
    public static EnumFacing fromAngle(final double angle) {
        return getHorizontal(MathHelper.floor_double(angle / 90.0 + 0.5) & 0x3);
    }
    
    public static EnumFacing random(final Random rand) {
        return values()[rand.nextInt(values().length)];
    }
    
    public static EnumFacing getFacingFromVector(final float p_176737_0_, final float p_176737_1_, final float p_176737_2_) {
        EnumFacing enumfacing = EnumFacing.NORTH;
        float f = Float.MIN_VALUE;
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing2 = values[i];
            final float f2 = p_176737_0_ * enumfacing2.directionVec.getX() + p_176737_1_ * enumfacing2.directionVec.getY() + p_176737_2_ * enumfacing2.directionVec.getZ();
            if (f2 > f) {
                f = f2;
                enumfacing = enumfacing2;
            }
        }
        return enumfacing;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public static EnumFacing func_181076_a(final AxisDirection p_181076_0_, final Axis p_181076_1_) {
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (enumfacing.getAxisDirection() == p_181076_0_ && enumfacing.getAxis() == p_181076_1_) {
                return enumfacing;
            }
        }
        throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
    }
    
    public Vec3i getDirectionVec() {
        return this.directionVec;
    }
    
    public enum Axis implements Predicate, IStringSerializable
    {
        X("X", 0, "X", 0, "x", Plane.HORIZONTAL), 
        Y("Y", 1, "Y", 1, "y", Plane.VERTICAL), 
        Z("Z", 2, "Z", 2, "z", Plane.HORIZONTAL);
        
        private static final Map NAME_LOOKUP;
        private final String name;
        private final Plane plane;
        private static final Axis[] $VALUES;
        private static final String __OBFID = "CL_00002321";
        
        static {
            NAME_LOOKUP = Maps.newHashMap();
            $VALUES = new Axis[] { Axis.X, Axis.Y, Axis.Z };
            Axis[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Axis enumfacing$axis = values[i];
                Axis.NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis);
            }
        }
        
        private Axis(final String name, final int ordinal, final String p_i14_3_, final int p_i14_4_, final String p_i14_5_, final Plane p_i14_6_) {
            this.name = p_i14_5_;
            this.plane = p_i14_6_;
        }
        
        public static Axis byName(final String name) {
            return (name == null) ? null : Axis.NAME_LOOKUP.get(name.toLowerCase());
        }
        
        public String getName2() {
            return this.name;
        }
        
        public boolean isVertical() {
            return this.plane == Plane.VERTICAL;
        }
        
        public boolean isHorizontal() {
            return this.plane == Plane.HORIZONTAL;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public boolean apply(final EnumFacing p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis() == this;
        }
        
        public Plane getPlane() {
            return this.plane;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public boolean apply(final Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }
    }
    
    public enum Plane implements Predicate, Iterable
    {
        HORIZONTAL("HORIZONTAL", 0, "HORIZONTAL", 0), 
        VERTICAL("VERTICAL", 1, "VERTICAL", 1);
        
        private static final Plane[] $VALUES;
        private static final String __OBFID = "CL_00002319";
        
        static {
            $VALUES = new Plane[] { Plane.HORIZONTAL, Plane.VERTICAL };
        }
        
        private Plane(final String name, final int ordinal, final String p_i16_3_, final int p_i16_4_) {
        }
        
        public EnumFacing[] facings() {
            switch (EnumFacing$1.field_179514_c[this.ordinal()]) {
                case 1: {
                    return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
                }
                case 2: {
                    return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
                }
                default: {
                    throw new Error("Someone's been tampering with the universe!");
                }
            }
        }
        
        public EnumFacing random(final Random rand) {
            final EnumFacing[] aenumfacing = this.facings();
            return aenumfacing[rand.nextInt(aenumfacing.length)];
        }
        
        public boolean apply(final EnumFacing p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this;
        }
        
        @Override
        public Iterator iterator() {
            return Iterators.forArray(this.facings());
        }
        
        @Override
        public boolean apply(final Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }
    }
    
    public enum AxisDirection
    {
        POSITIVE("POSITIVE", 0, "POSITIVE", 0, 1, "Towards positive"), 
        NEGATIVE("NEGATIVE", 1, "NEGATIVE", 1, -1, "Towards negative");
        
        private final int offset;
        private final String description;
        private static final AxisDirection[] $VALUES;
        private static final String __OBFID = "CL_00002320";
        
        static {
            $VALUES = new AxisDirection[] { AxisDirection.POSITIVE, AxisDirection.NEGATIVE };
        }
        
        private AxisDirection(final String name, final int ordinal, final String p_i15_3_, final int p_i15_4_, final int p_i15_5_, final String p_i15_6_) {
            this.offset = p_i15_5_;
            this.description = p_i15_6_;
        }
        
        public int getOffset() {
            return this.offset;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
    
    static final class EnumFacing$1
    {
        static final int[] field_179515_a;
        static final int[] field_179513_b;
        static final int[] field_179514_c;
        private static final String __OBFID = "CL_00002322";
        
        static {
            field_179514_c = new int[Plane.values().length];
            try {
                EnumFacing$1.field_179514_c[Plane.HORIZONTAL.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                EnumFacing$1.field_179514_c[Plane.VERTICAL.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            field_179513_b = new int[EnumFacing.values().length];
            try {
                EnumFacing$1.field_179513_b[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                EnumFacing$1.field_179513_b[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                EnumFacing$1.field_179513_b[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                EnumFacing$1.field_179513_b[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                EnumFacing$1.field_179513_b[EnumFacing.UP.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                EnumFacing$1.field_179513_b[EnumFacing.DOWN.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            field_179515_a = new int[Axis.values().length];
            try {
                EnumFacing$1.field_179515_a[Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                EnumFacing$1.field_179515_a[Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            try {
                EnumFacing$1.field_179515_a[Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
        }
    }
}
