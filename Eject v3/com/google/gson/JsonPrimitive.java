package com.google.gson;

import com.google.gson.internal.*;
import com.google.gson.internal.LazilyParsedNumber;

import java.math.BigDecimal;
import java.math.BigInteger;

.Gson.Preconditions;

public final class JsonPrimitive
        extends JsonElement {
    private static final Class<?>[] PRIMITIVE_TYPES = {Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    private Object value;

    public JsonPrimitive(Boolean paramBoolean) {
        setValue(paramBoolean);
    }

    public JsonPrimitive(Number paramNumber) {
        setValue(paramNumber);
    }

    public JsonPrimitive(String paramString) {
        setValue(paramString);
    }

    public JsonPrimitive(Character paramCharacter) {
        setValue(paramCharacter);
    }

    JsonPrimitive(Object paramObject) {
        setValue(paramObject);
    }

    private static boolean isPrimitiveOrString(Object paramObject) {
        if ((paramObject instanceof String)) {
            return true;
        }
        Class localClass1 = paramObject.getClass();
        for (Class localClass2 : PRIMITIVE_TYPES) {
            if (localClass2.isAssignableFrom(localClass1)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIntegral(JsonPrimitive paramJsonPrimitive) {
        if ((paramJsonPrimitive.value instanceof Number)) {
            Number localNumber = (Number) paramJsonPrimitive.value;
            return ((localNumber instanceof BigInteger)) || ((localNumber instanceof Long)) || ((localNumber instanceof Integer)) || ((localNumber instanceof Short)) || ((localNumber instanceof Byte));
        }
        return false;
    }

    JsonPrimitive deepCopy() {
        return this;
    }

    void setValue(Object paramObject) {
        if ((paramObject instanceof Character)) {
            char c = ((Character) paramObject).charValue();
            this.value = String.valueOf(c);
        } else {
      .Gson.Preconditions.checkArgument(((paramObject instanceof Number)) || (isPrimitiveOrString(paramObject)));
            this.value = paramObject;
        }
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    Boolean getAsBooleanWrapper() {
        return (Boolean) this.value;
    }

    public boolean getAsBoolean() {
        if (isBoolean()) {
            return getAsBooleanWrapper().booleanValue();
        }
        return Boolean.parseBoolean(getAsString());
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    public Number getAsNumber() {
        return (this.value instanceof String) ? new LazilyParsedNumber((String) this.value) : (Number) this.value;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        }
        if (isBoolean()) {
            return getAsBooleanWrapper().toString();
        }
        return (String) this.value;
    }

    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    public BigDecimal getAsBigDecimal() {
        return (this.value instanceof BigDecimal) ? (BigDecimal) this.value : new BigDecimal(this.value.toString());
    }

    public BigInteger getAsBigInteger() {
        return (this.value instanceof BigInteger) ? (BigInteger) this.value : new BigInteger(this.value.toString());
    }

    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    public char getAsCharacter() {
        return getAsString().charAt(0);
    }

    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        long l;
        if (isIntegral(this)) {
            l = getAsNumber().longValue();
            return (int) (l ^ l >>> 32);
        }
        if ((this.value instanceof Number)) {
            l = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) (l ^ l >>> 32);
        }
        return this.value.hashCode();
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject == null) || (getClass() != paramObject.getClass())) {
            return false;
        }
        JsonPrimitive localJsonPrimitive = (JsonPrimitive) paramObject;
        if (this.value == null) {
            return localJsonPrimitive.value == null;
        }
        if ((isIntegral(this)) && (isIntegral(localJsonPrimitive))) {
            return getAsNumber().longValue() == localJsonPrimitive.getAsNumber().longValue();
        }
        if (((this.value instanceof Number)) && ((localJsonPrimitive.value instanceof Number))) {
            double d1 = getAsNumber().doubleValue();
            double d2 = localJsonPrimitive.getAsNumber().doubleValue();
            return (d1 == d2) || ((Double.isNaN(d1)) && (Double.isNaN(d2)));
        }
        return this.value.equals(localJsonPrimitive.value);
    }
}




