// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.Iterator;

public abstract class Union extends Structure
{
    private StructField activeField;
    StructField biggestField;
    
    protected Union() {
    }
    
    protected Union(final Pointer p) {
        super(p);
    }
    
    protected Union(final Pointer p, final int alignType) {
        super(p, alignType);
    }
    
    protected Union(final TypeMapper mapper) {
        super(mapper);
    }
    
    protected Union(final Pointer p, final int alignType, final TypeMapper mapper) {
        super(p, alignType, mapper);
    }
    
    public void setType(final Class type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                return;
            }
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }
    
    public void setType(final String fieldName) {
        this.ensureAllocated();
        final StructField f = this.fields().get(fieldName);
        if (f != null) {
            this.activeField = f;
            return;
        }
        throw new IllegalArgumentException("No field named " + fieldName + " in " + this);
    }
    
    public Object readField(final String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        return super.readField(fieldName);
    }
    
    public void writeField(final String fieldName) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName);
    }
    
    public void writeField(final String fieldName, final Object value) {
        this.ensureAllocated();
        this.setType(fieldName);
        super.writeField(fieldName, value);
    }
    
    public Object getTypedValue(final Class type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type == type) {
                this.activeField = f;
                this.read();
                return this.getField(this.activeField);
            }
        }
        throw new IllegalArgumentException("No field of type " + type + " in " + this);
    }
    
    public Object setTypedValue(final Object object) {
        final StructField f = this.findField(object.getClass());
        if (f != null) {
            this.setField(this.activeField = f, object);
            return this;
        }
        throw new IllegalArgumentException("No field of type " + object.getClass() + " in " + this);
    }
    
    private StructField findField(final Class type) {
        this.ensureAllocated();
        for (final StructField f : this.fields().values()) {
            if (f.type.isAssignableFrom(type)) {
                return f;
            }
        }
        return null;
    }
    
    void writeField(final StructField field) {
        if (field == this.activeField) {
            super.writeField(field);
        }
    }
    
    Object readField(final StructField field) {
        if (field == this.activeField || (!Structure.class.isAssignableFrom(field.type) && !String.class.isAssignableFrom(field.type) && !WString.class.isAssignableFrom(field.type))) {
            return super.readField(field);
        }
        return null;
    }
    
    int calculateSize(final boolean force, final boolean avoidFFIType) {
        int size = super.calculateSize(force, avoidFFIType);
        if (size != -1) {
            int fsize = 0;
            for (final StructField f : this.fields().values()) {
                f.offset = 0;
                if (f.size > fsize || (f.size == fsize && Structure.class.isAssignableFrom(f.type))) {
                    fsize = f.size;
                    this.biggestField = f;
                }
            }
            size = this.calculateAlignedSize(fsize);
            if (size > 0 && this instanceof ByValue && !avoidFFIType) {
                this.getTypeInfo();
            }
        }
        return size;
    }
    
    protected int getNativeAlignment(final Class type, final Object value, final boolean isFirstElement) {
        return super.getNativeAlignment(type, value, true);
    }
    
    Pointer getTypeInfo() {
        if (this.biggestField == null) {
            return null;
        }
        return super.getTypeInfo();
    }
}
