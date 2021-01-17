// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.win32;

import com.sun.jna.ToNativeConverter;
import com.sun.jna.FromNativeContext;
import com.sun.jna.WString;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.DefaultTypeMapper;

public class W32APITypeMapper extends DefaultTypeMapper
{
    public static final TypeMapper UNICODE;
    public static final TypeMapper ASCII;
    
    protected W32APITypeMapper(final boolean unicode) {
        if (unicode) {
            final TypeConverter stringConverter = new TypeConverter() {
                public Object toNative(final Object value, final ToNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    if (value instanceof String[]) {
                        return new StringArray((String[])value, true);
                    }
                    return new WString(value.toString());
                }
                
                public Object fromNative(final Object value, final FromNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    return value.toString();
                }
                
                public Class nativeType() {
                    return WString.class;
                }
            };
            this.addTypeConverter(String.class, stringConverter);
            this.addToNativeConverter(String[].class, stringConverter);
        }
        final TypeConverter booleanConverter = new TypeConverter() {
            public Object toNative(final Object value, final ToNativeContext context) {
                return new Integer(Boolean.TRUE.equals(value) ? 1 : 0);
            }
            
            public Object fromNative(final Object value, final FromNativeContext context) {
                return ((int)value != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
            
            public Class nativeType() {
                return Integer.class;
            }
        };
        this.addTypeConverter(Boolean.class, booleanConverter);
    }
    
    static {
        UNICODE = new W32APITypeMapper(true);
        ASCII = new W32APITypeMapper(false);
    }
}
