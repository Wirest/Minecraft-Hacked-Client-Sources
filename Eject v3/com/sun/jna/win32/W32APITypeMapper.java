package com.sun.jna.win32;

import com.sun.jna.*;

public class W32APITypeMapper
        extends DefaultTypeMapper {
    public static final TypeMapper UNICODE = new W32APITypeMapper(true);
    public static final TypeMapper ASCII = new W32APITypeMapper(false);

    protected W32APITypeMapper(boolean paramBoolean) {
        if (paramBoolean) {
            localObject = new TypeConverter() {
                public Object toNative(Object paramAnonymousObject, ToNativeContext paramAnonymousToNativeContext) {
                    if (paramAnonymousObject == null) {
                        return null;
                    }
                    if ((paramAnonymousObject instanceof String[])) {
                        return new StringArray((String[]) paramAnonymousObject, true);
                    }
                    return new WString(paramAnonymousObject.toString());
                }

                public Object fromNative(Object paramAnonymousObject, FromNativeContext paramAnonymousFromNativeContext) {
                    if (paramAnonymousObject == null) {
                        return null;
                    }
                    return paramAnonymousObject.toString();
                }

                public Class nativeType() {
                    return WString.class;
                }
            };
            addTypeConverter(String.class, (TypeConverter) localObject);
            addToNativeConverter(new String[0].getClass(), (ToNativeConverter) localObject);
        }
        Object localObject = new TypeConverter() {
            public Object toNative(Object paramAnonymousObject, ToNativeContext paramAnonymousToNativeContext) {
                return new Integer(Boolean.TRUE.equals(paramAnonymousObject) ? 1 : 0);
            }

            public Object fromNative(Object paramAnonymousObject, FromNativeContext paramAnonymousFromNativeContext) {
                return ((Integer) paramAnonymousObject).intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
            }

            public Class nativeType() {
                return Integer.class;
            }
        };
        addTypeConverter(Boolean.class, (TypeConverter) localObject);
    }
}




