// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.util.Iterator;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import com.google.gson.annotations.Since;
import java.lang.reflect.Field;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import com.google.gson.ExclusionStrategy;
import java.util.List;
import com.google.gson.TypeAdapterFactory;

public final class Excluder implements TypeAdapterFactory, Cloneable
{
    private static final double IGNORE_VERSIONS = -1.0;
    public static final Excluder DEFAULT;
    private double version;
    private int modifiers;
    private boolean serializeInnerClasses;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies;
    private List<ExclusionStrategy> deserializationStrategies;
    
    public Excluder() {
        this.version = -1.0;
        this.modifiers = 136;
        this.serializeInnerClasses = true;
        this.serializationStrategies = Collections.emptyList();
        this.deserializationStrategies = Collections.emptyList();
    }
    
    @Override
    protected Excluder clone() {
        try {
            return (Excluder)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    public Excluder withVersion(final double ignoreVersionsAfter) {
        final Excluder result = this.clone();
        result.version = ignoreVersionsAfter;
        return result;
    }
    
    public Excluder withModifiers(final int... modifiers) {
        final Excluder result = this.clone();
        result.modifiers = 0;
        for (final int modifier : modifiers) {
            final Excluder excluder = result;
            excluder.modifiers |= modifier;
        }
        return result;
    }
    
    public Excluder disableInnerClassSerialization() {
        final Excluder result = this.clone();
        result.serializeInnerClasses = false;
        return result;
    }
    
    public Excluder excludeFieldsWithoutExposeAnnotation() {
        final Excluder result = this.clone();
        result.requireExpose = true;
        return result;
    }
    
    public Excluder withExclusionStrategy(final ExclusionStrategy exclusionStrategy, final boolean serialization, final boolean deserialization) {
        final Excluder result = this.clone();
        if (serialization) {
            (result.serializationStrategies = new ArrayList<ExclusionStrategy>(this.serializationStrategies)).add(exclusionStrategy);
        }
        if (deserialization) {
            (result.deserializationStrategies = new ArrayList<ExclusionStrategy>(this.deserializationStrategies)).add(exclusionStrategy);
        }
        return result;
    }
    
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<?> rawType = type.getRawType();
        final boolean skipSerialize = this.excludeClass(rawType, true);
        final boolean skipDeserialize = this.excludeClass(rawType, false);
        if (!skipSerialize && !skipDeserialize) {
            return null;
        }
        return new TypeAdapter<T>() {
            private TypeAdapter<T> delegate;
            
            @Override
            public T read(final JsonReader in) throws IOException {
                if (skipDeserialize) {
                    in.skipValue();
                    return null;
                }
                return this.delegate().read(in);
            }
            
            @Override
            public void write(final JsonWriter out, final T value) throws IOException {
                if (skipSerialize) {
                    out.nullValue();
                    return;
                }
                this.delegate().write(out, value);
            }
            
            private TypeAdapter<T> delegate() {
                final TypeAdapter<T> d = this.delegate;
                return (d != null) ? d : (this.delegate = gson.getDelegateAdapter(Excluder.this, type));
            }
        };
    }
    
    public boolean excludeField(final Field field, final boolean serialize) {
        if ((this.modifiers & field.getModifiers()) != 0x0) {
            return true;
        }
        if (this.version != -1.0 && !this.isValidVersion(field.getAnnotation(Since.class), field.getAnnotation(Until.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        Label_0112: {
            if (this.requireExpose) {
                final Expose annotation = field.getAnnotation(Expose.class);
                if (annotation != null) {
                    if (serialize) {
                        if (annotation.serialize()) {
                            break Label_0112;
                        }
                    }
                    else if (annotation.deserialize()) {
                        break Label_0112;
                    }
                }
                return true;
            }
        }
        if (!this.serializeInnerClasses && this.isInnerClass(field.getType())) {
            return true;
        }
        if (this.isAnonymousOrLocal(field.getType())) {
            return true;
        }
        final List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
        if (!list.isEmpty()) {
            final FieldAttributes fieldAttributes = new FieldAttributes(field);
            for (final ExclusionStrategy exclusionStrategy : list) {
                if (exclusionStrategy.shouldSkipField(fieldAttributes)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean excludeClass(final Class<?> clazz, final boolean serialize) {
        if (this.version != -1.0 && !this.isValidVersion(clazz.getAnnotation(Since.class), clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(clazz)) {
            return true;
        }
        if (this.isAnonymousOrLocal(clazz)) {
            return true;
        }
        final List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
        for (final ExclusionStrategy exclusionStrategy : list) {
            if (exclusionStrategy.shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isAnonymousOrLocal(final Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }
    
    private boolean isInnerClass(final Class<?> clazz) {
        return clazz.isMemberClass() && !this.isStatic(clazz);
    }
    
    private boolean isStatic(final Class<?> clazz) {
        return (clazz.getModifiers() & 0x8) != 0x0;
    }
    
    private boolean isValidVersion(final Since since, final Until until) {
        return this.isValidSince(since) && this.isValidUntil(until);
    }
    
    private boolean isValidSince(final Since annotation) {
        if (annotation != null) {
            final double annotationVersion = annotation.value();
            if (annotationVersion > this.version) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isValidUntil(final Until annotation) {
        if (annotation != null) {
            final double annotationVersion = annotation.value();
            if (annotationVersion <= this.version) {
                return false;
            }
        }
        return true;
    }
    
    static {
        DEFAULT = new Excluder();
    }
}
