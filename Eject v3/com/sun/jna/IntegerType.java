package com.sun.jna;

public abstract class IntegerType
        extends Number
        implements NativeMapped {
    private int size;
    private Number number;
    private boolean unsigned;
    private long value;

    public IntegerType(int paramInt) {
        this(paramInt, 0L, false);
    }

    public IntegerType(int paramInt, boolean paramBoolean) {
        this(paramInt, 0L, paramBoolean);
    }

    public IntegerType(int paramInt, long paramLong) {
        this(paramInt, paramLong, false);
    }

    public IntegerType(int paramInt, long paramLong, boolean paramBoolean) {
        this.size = paramInt;
        this.unsigned = paramBoolean;
        setValue(paramLong);
    }

    public void setValue(long paramLong) {
        long l1 = paramLong;
        this.value = paramLong;
        switch (this.size) {
            case 1:
                if (this.unsigned) {
                    this.value = (paramLong & 0xFF);
                }
                l1 = (byte) (int) paramLong;
                this.number = new Byte((byte) (int) paramLong);
                break;
            case 2:
                if (this.unsigned) {
                    this.value = (paramLong & 0xFFFF);
                }
                l1 = (short) (int) paramLong;
                this.number = new Short((short) (int) paramLong);
                break;
            case 4:
                if (this.unsigned) {
                    this.value = (paramLong & 0xFFFFFFFF);
                }
                l1 = (int) paramLong;
                this.number = new Integer((int) paramLong);
                break;
            case 8:
                this.number = new Long(paramLong);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                throw new IllegalArgumentException("Unsupported size: " + this.size);
        }
        if (this.size < 8) {
            long l2 = (1L << this.size * 8) - 1L ^ 0xFFFFFFFFFFFFFFFF;
            if (((paramLong < 0L) && (l1 != paramLong)) || ((paramLong >= 0L) && ((l2 & paramLong) != 0L))) {
                throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(paramLong) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + Long.toHexString(l2));
            }
        }
    }

    public Object toNative() {
        return this.number;
    }

    public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
        long l = paramObject == null ? 0L : ((Number) paramObject).longValue();
        try {
            IntegerType localIntegerType = (IntegerType) getClass().newInstance();
            localIntegerType.setValue(l);
            return localIntegerType;
        } catch (InstantiationException localInstantiationException) {
            throw new IllegalArgumentException("Can't instantiate " + getClass());
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
        }
    }

    public Class nativeType() {
        return this.number.getClass();
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        return this.number.floatValue();
    }

    public double doubleValue() {
        return this.number.doubleValue();
    }

    public boolean equals(Object paramObject) {
        return ((paramObject instanceof IntegerType)) && (this.number.equals(((IntegerType) paramObject).number));
    }

    public String toString() {
        return this.number.toString();
    }

    public int hashCode() {
        return this.number.hashCode();
    }
}




