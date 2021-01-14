package com.sun.jna;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultTypeMapper
        implements TypeMapper {
    private List toNativeConverters = new ArrayList();
    private List fromNativeConverters = new ArrayList();

    private Class getAltClass(Class paramClass) {
        if (paramClass == Boolean.class) {
            return Boolean.TYPE;
        }
        if (paramClass == Boolean.TYPE) {
            return Boolean.class;
        }
        if (paramClass == Byte.class) {
            return Byte.TYPE;
        }
        if (paramClass == Byte.TYPE) {
            return Byte.class;
        }
        if (paramClass == Character.class) {
            return Character.TYPE;
        }
        if (paramClass == Character.TYPE) {
            return Character.class;
        }
        if (paramClass == Short.class) {
            return Short.TYPE;
        }
        if (paramClass == Short.TYPE) {
            return Short.class;
        }
        if (paramClass == Integer.class) {
            return Integer.TYPE;
        }
        if (paramClass == Integer.TYPE) {
            return Integer.class;
        }
        if (paramClass == Long.class) {
            return Long.TYPE;
        }
        if (paramClass == Long.TYPE) {
            return Long.class;
        }
        if (paramClass == Float.class) {
            return Float.TYPE;
        }
        if (paramClass == Float.TYPE) {
            return Float.class;
        }
        if (paramClass == Double.class) {
            return Double.TYPE;
        }
        if (paramClass == Double.TYPE) {
            return Double.class;
        }
        return null;
    }

    public void addToNativeConverter(Class paramClass, ToNativeConverter paramToNativeConverter) {
        this.toNativeConverters.add(new Entry(paramClass, paramToNativeConverter));
        Class localClass = getAltClass(paramClass);
        if (localClass != null) {
            this.toNativeConverters.add(new Entry(localClass, paramToNativeConverter));
        }
    }

    public void addFromNativeConverter(Class paramClass, FromNativeConverter paramFromNativeConverter) {
        this.fromNativeConverters.add(new Entry(paramClass, paramFromNativeConverter));
        Class localClass = getAltClass(paramClass);
        if (localClass != null) {
            this.fromNativeConverters.add(new Entry(localClass, paramFromNativeConverter));
        }
    }

    protected void addTypeConverter(Class paramClass, TypeConverter paramTypeConverter) {
        addFromNativeConverter(paramClass, paramTypeConverter);
        addToNativeConverter(paramClass, paramTypeConverter);
    }

    private Object lookupConverter(Class paramClass, List paramList) {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            Entry localEntry = (Entry) localIterator.next();
            if (localEntry.type.isAssignableFrom(paramClass)) {
                return localEntry.converter;
            }
        }
        return null;
    }

    public FromNativeConverter getFromNativeConverter(Class paramClass) {
        return (FromNativeConverter) lookupConverter(paramClass, this.fromNativeConverters);
    }

    public ToNativeConverter getToNativeConverter(Class paramClass) {
        return (ToNativeConverter) lookupConverter(paramClass, this.toNativeConverters);
    }

    private static class Entry {
        public Class type;
        public Object converter;

        public Entry(Class paramClass, Object paramObject) {
            this.type = paramClass;
            this.converter = paramObject;
        }
    }
}




