// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Collections;
import java.util.Collection;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.$Gson$Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.internal.Excluder;

public final class GsonBuilder
{
    private Excluder excluder;
    private LongSerializationPolicy longSerializationPolicy;
    private FieldNamingStrategy fieldNamingPolicy;
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final List<TypeAdapterFactory> factories;
    private final List<TypeAdapterFactory> hierarchyFactories;
    private boolean serializeNulls;
    private String datePattern;
    private int dateStyle;
    private int timeStyle;
    private boolean complexMapKeySerialization;
    private boolean serializeSpecialFloatingPointValues;
    private boolean escapeHtmlChars;
    private boolean prettyPrinting;
    private boolean generateNonExecutableJson;
    
    public GsonBuilder() {
        this.excluder = Excluder.DEFAULT;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        this.instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        this.factories = new ArrayList<TypeAdapterFactory>();
        this.hierarchyFactories = new ArrayList<TypeAdapterFactory>();
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.escapeHtmlChars = true;
    }
    
    public GsonBuilder setVersion(final double ignoreVersionsAfter) {
        this.excluder = this.excluder.withVersion(ignoreVersionsAfter);
        return this;
    }
    
    public GsonBuilder excludeFieldsWithModifiers(final int... modifiers) {
        this.excluder = this.excluder.withModifiers(modifiers);
        return this;
    }
    
    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }
    
    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }
    
    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }
    
    public GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }
    
    public GsonBuilder disableInnerClassSerialization() {
        this.excluder = this.excluder.disableInnerClassSerialization();
        return this;
    }
    
    public GsonBuilder setLongSerializationPolicy(final LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }
    
    public GsonBuilder setFieldNamingPolicy(final FieldNamingPolicy namingConvention) {
        this.fieldNamingPolicy = namingConvention;
        return this;
    }
    
    public GsonBuilder setFieldNamingStrategy(final FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = fieldNamingStrategy;
        return this;
    }
    
    public GsonBuilder setExclusionStrategies(final ExclusionStrategy... strategies) {
        for (final ExclusionStrategy strategy : strategies) {
            this.excluder = this.excluder.withExclusionStrategy(strategy, true, true);
        }
        return this;
    }
    
    public GsonBuilder addSerializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, true, false);
        return this;
    }
    
    public GsonBuilder addDeserializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, false, true);
        return this;
    }
    
    public GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }
    
    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }
    
    public GsonBuilder setDateFormat(final String pattern) {
        this.datePattern = pattern;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int dateStyle, final int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder registerTypeAdapter(final Type type, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof InstanceCreator || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof InstanceCreator) {
            this.instanceCreators.put(type, (InstanceCreator<?>)typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer) {
            final TypeToken<?> typeToken = TypeToken.get(type);
            this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add(TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder registerTypeAdapterFactory(final TypeAdapterFactory factory) {
        this.factories.add(factory);
        return this;
    }
    
    public GsonBuilder registerTypeHierarchyAdapter(final Class<?> baseType, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof JsonDeserializer || typeAdapter instanceof JsonSerializer) {
            this.hierarchyFactories.add(0, TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add(TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }
    
    public Gson create() {
        final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
        factories.addAll(this.factories);
        Collections.reverse(factories);
        factories.addAll(this.hierarchyFactories);
        this.addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, factories);
        return new Gson(this.excluder, this.fieldNamingPolicy, this.instanceCreators, this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.serializeSpecialFloatingPointValues, this.longSerializationPolicy, factories);
    }
    
    private void addTypeAdaptersForDate(final String datePattern, final int dateStyle, final int timeStyle, final List<TypeAdapterFactory> factories) {
        DefaultDateTypeAdapter dateTypeAdapter;
        if (datePattern != null && !"".equals(datePattern.trim())) {
            dateTypeAdapter = new DefaultDateTypeAdapter(datePattern);
        }
        else {
            if (dateStyle == 2 || timeStyle == 2) {
                return;
            }
            dateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
        }
        factories.add(TreeTypeAdapter.newFactory(TypeToken.get((Class<?>)Date.class), dateTypeAdapter));
        factories.add(TreeTypeAdapter.newFactory(TypeToken.get((Class<?>)Timestamp.class), dateTypeAdapter));
        factories.add(TreeTypeAdapter.newFactory(TypeToken.get((Class<?>)java.sql.Date.class), dateTypeAdapter));
    }
}
