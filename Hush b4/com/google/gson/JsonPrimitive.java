// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.$Gson$Preconditions;

public final class JsonPrimitive extends JsonElement
{
    private static final Class<?>[] PRIMITIVE_TYPES;
    private Object value;
    
    public JsonPrimitive(final Boolean bool) {
        this.setValue(bool);
    }
    
    public JsonPrimitive(final Number number) {
        this.setValue(number);
    }
    
    public JsonPrimitive(final String string) {
        this.setValue(string);
    }
    
    public JsonPrimitive(final Character c) {
        this.setValue(c);
    }
    
    JsonPrimitive(final Object primitive) {
        this.setValue(primitive);
    }
    
    @Override
    JsonPrimitive deepCopy() {
        return this;
    }
    
    void setValue(final Object primitive) {
        if (primitive instanceof Character) {
            final char c = (char)primitive;
            this.value = String.valueOf(c);
        }
        else {
            $Gson$Preconditions.checkArgument(primitive instanceof Number || isPrimitiveOrString(primitive));
            this.value = primitive;
        }
    }
    
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }
    
    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean)this.value;
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper();
        }
        return Boolean.parseBoolean(this.getAsString());
    }
    
    public boolean isNumber() {
        return this.value instanceof Number;
    }
    
    @Override
    public Number getAsNumber() {
        return (this.value instanceof String) ? new LazilyParsedNumber((String)this.value) : this.value;
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper().toString();
        }
        return (String)this.value;
    }
    
    @Override
    public double getAsDouble() {
        return this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        return (BigDecimal)((this.value instanceof BigDecimal) ? this.value : new BigDecimal(this.value.toString()));
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        return (BigInteger)((this.value instanceof BigInteger) ? this.value : new BigInteger(this.value.toString()));
    }
    
    @Override
    public float getAsFloat() {
        return this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
    }
    
    @Override
    public long getAsLong() {
        return this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
    }
    
    @Override
    public short getAsShort() {
        return this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
    }
    
    @Override
    public int getAsInt() {
        return this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
    }
    
    @Override
    public byte getAsByte() {
        return this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
    }
    
    @Override
    public char getAsCharacter() {
        return this.getAsString().charAt(0);
    }
    
    private static boolean isPrimitiveOrString(final Object target) {
        if (target instanceof String) {
            return true;
        }
        final Class<?> classOfPrimitive = target.getClass();
        for (final Class<?> standardPrimitive : JsonPrimitive.PRIMITIVE_TYPES) {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            final long value = this.getAsNumber().longValue();
            return (int)(value ^ value >>> 32);
        }
        if (this.value instanceof Number) {
            final long value = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(value ^ value >>> 32);
        }
        return this.value.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final JsonPrimitive other = (JsonPrimitive)obj;
        if (this.value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return this.getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (this.value instanceof Number && other.value instanceof Number) {
            final double a = this.getAsNumber().doubleValue();
            final double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return this.value.equals(other.value);
    }
    
    private static boolean isIntegral(final JsonPrimitive primitive) {
        if (primitive.value instanceof Number) {
            final Number number = (Number)primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
    
    static {
        PRIMITIVE_TYPES = new Class[] { Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
    }
}
