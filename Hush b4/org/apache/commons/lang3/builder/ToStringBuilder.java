// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ObjectUtils;

public class ToStringBuilder implements Builder<String>
{
    private static volatile ToStringStyle defaultStyle;
    private final StringBuffer buffer;
    private final Object object;
    private final ToStringStyle style;
    
    public static ToStringStyle getDefaultStyle() {
        return ToStringBuilder.defaultStyle;
    }
    
    public static void setDefaultStyle(final ToStringStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("The style must not be null");
        }
        ToStringBuilder.defaultStyle = style;
    }
    
    public static String reflectionToString(final Object object) {
        return ReflectionToStringBuilder.toString(object);
    }
    
    public static String reflectionToString(final Object object, final ToStringStyle style) {
        return ReflectionToStringBuilder.toString(object, style);
    }
    
    public static String reflectionToString(final Object object, final ToStringStyle style, final boolean outputTransients) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, null);
    }
    
    public static <T> String reflectionToString(final T object, final ToStringStyle style, final boolean outputTransients, final Class<? super T> reflectUpToClass) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
    }
    
    public ToStringBuilder(final Object object) {
        this(object, null, null);
    }
    
    public ToStringBuilder(final Object object, final ToStringStyle style) {
        this(object, style, null);
    }
    
    public ToStringBuilder(final Object object, ToStringStyle style, StringBuffer buffer) {
        if (style == null) {
            style = getDefaultStyle();
        }
        if (buffer == null) {
            buffer = new StringBuffer(512);
        }
        this.buffer = buffer;
        (this.style = style).appendStart(buffer, this.object = object);
    }
    
    public ToStringBuilder append(final boolean value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final boolean[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final byte value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final byte[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final char value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final char[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final double value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final double[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final float value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final float[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final int value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final int[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final long value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final long[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final Object obj) {
        this.style.append(this.buffer, null, obj, null);
        return this;
    }
    
    public ToStringBuilder append(final Object[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final short value) {
        this.style.append(this.buffer, null, value);
        return this;
    }
    
    public ToStringBuilder append(final short[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final boolean value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final boolean[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final boolean[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final byte value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final byte[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final byte[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final char value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final char[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final char[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final double value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final double[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final double[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final float value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final float[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final float[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final int value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final int[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final int[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final long value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final long[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final long[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final Object obj) {
        this.style.append(this.buffer, fieldName, obj, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final Object obj, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, obj, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final Object[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final Object[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final short value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final short[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }
    
    public ToStringBuilder append(final String fieldName, final short[] array, final boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, fullDetail);
        return this;
    }
    
    public ToStringBuilder appendAsObjectToString(final Object srcObject) {
        ObjectUtils.identityToString(this.getStringBuffer(), srcObject);
        return this;
    }
    
    public ToStringBuilder appendSuper(final String superToString) {
        if (superToString != null) {
            this.style.appendSuper(this.buffer, superToString);
        }
        return this;
    }
    
    public ToStringBuilder appendToString(final String toString) {
        if (toString != null) {
            this.style.appendToString(this.buffer, toString);
        }
        return this;
    }
    
    public Object getObject() {
        return this.object;
    }
    
    public StringBuffer getStringBuffer() {
        return this.buffer;
    }
    
    public ToStringStyle getStyle() {
        return this.style;
    }
    
    @Override
    public String toString() {
        if (this.getObject() == null) {
            this.getStringBuffer().append(this.getStyle().getNullText());
        }
        else {
            this.style.appendEnd(this.getStringBuffer(), this.getObject());
        }
        return this.getStringBuffer().toString();
    }
    
    @Override
    public String build() {
        return this.toString();
    }
    
    static {
        ToStringBuilder.defaultStyle = ToStringStyle.DEFAULT_STYLE;
    }
}
