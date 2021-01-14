package com.google.gson;

import com.google.gson.internal.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

.Gson.Preconditions;

public final class GsonBuilder {
    private final Map<Type, InstanceCreator<?>> instanceCreators = new HashMap();
    private final List<TypeAdapterFactory> factories = new ArrayList();
    private final List<TypeAdapterFactory> hierarchyFactories = new ArrayList();
    private Excluder excluder = Excluder.DEFAULT;
    private LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    private FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
    private boolean serializeNulls;
    private String datePattern;
    private int dateStyle = 2;
    private int timeStyle = 2;
    private boolean complexMapKeySerialization;
    private boolean serializeSpecialFloatingPointValues;
    private boolean escapeHtmlChars = true;
    private boolean prettyPrinting;
    private boolean generateNonExecutableJson;

    public GsonBuilder setVersion(double paramDouble) {
        this.excluder = this.excluder.withVersion(paramDouble);
        return this;
    }

    public GsonBuilder excludeFieldsWithModifiers(int... paramVarArgs) {
        this.excluder = this.excluder.withModifiers(paramVarArgs);
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

    public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy paramLongSerializationPolicy) {
        this.longSerializationPolicy = paramLongSerializationPolicy;
        return this;
    }

    public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy paramFieldNamingPolicy) {
        this.fieldNamingPolicy = paramFieldNamingPolicy;
        return this;
    }

    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy paramFieldNamingStrategy) {
        this.fieldNamingPolicy = paramFieldNamingStrategy;
        return this;
    }

    public GsonBuilder setExclusionStrategies(ExclusionStrategy... paramVarArgs) {
        for (ExclusionStrategy localExclusionStrategy : paramVarArgs) {
            this.excluder = this.excluder.withExclusionStrategy(localExclusionStrategy, true, true);
        }
        return this;
    }

    public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy paramExclusionStrategy) {
        this.excluder = this.excluder.withExclusionStrategy(paramExclusionStrategy, true, false);
        return this;
    }

    public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy paramExclusionStrategy) {
        this.excluder = this.excluder.withExclusionStrategy(paramExclusionStrategy, false, true);
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

    public GsonBuilder setDateFormat(String paramString) {
        this.datePattern = paramString;
        return this;
    }

    public GsonBuilder setDateFormat(int paramInt) {
        this.dateStyle = paramInt;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder setDateFormat(int paramInt1, int paramInt2) {
        this.dateStyle = paramInt1;
        this.timeStyle = paramInt2;
        this.datePattern = null;
        return this;
    }

    public GsonBuilder registerTypeAdapter(Type paramType, Object paramObject) {
    .Gson.Preconditions.checkArgument(((paramObject instanceof JsonSerializer)) || ((paramObject instanceof JsonDeserializer)) || ((paramObject instanceof InstanceCreator)) || ((paramObject instanceof TypeAdapter)));
        if ((paramObject instanceof InstanceCreator)) {
            this.instanceCreators.put(paramType, (InstanceCreator) paramObject);
        }
        if (((paramObject instanceof JsonSerializer)) || ((paramObject instanceof JsonDeserializer))) {
            TypeToken localTypeToken = TypeToken.get(paramType);
            this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(localTypeToken, paramObject));
        }
        if ((paramObject instanceof TypeAdapter)) {
            this.factories.add(TypeAdapters.newFactory(TypeToken.get(paramType), (TypeAdapter) paramObject));
        }
        return this;
    }

    public GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory paramTypeAdapterFactory) {
        this.factories.add(paramTypeAdapterFactory);
        return this;
    }

    public GsonBuilder registerTypeHierarchyAdapter(Class<?> paramClass, Object paramObject) {
    .Gson.Preconditions.checkArgument(((paramObject instanceof JsonSerializer)) || ((paramObject instanceof JsonDeserializer)) || ((paramObject instanceof TypeAdapter)));
        if (((paramObject instanceof JsonDeserializer)) || ((paramObject instanceof JsonSerializer))) {
            this.hierarchyFactories.add(0, TreeTypeAdapter.newTypeHierarchyFactory(paramClass, paramObject));
        }
        if ((paramObject instanceof TypeAdapter)) {
            this.factories.add(TypeAdapters.newTypeHierarchyFactory(paramClass, (TypeAdapter) paramObject));
        }
        return this;
    }

    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    public Gson create() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.addAll(this.factories);
        Collections.reverse(localArrayList);
        localArrayList.addAll(this.hierarchyFactories);
        addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, localArrayList);
        return new Gson(this.excluder, this.fieldNamingPolicy, this.instanceCreators, this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.serializeSpecialFloatingPointValues, this.longSerializationPolicy, localArrayList);
    }

    private void addTypeAdaptersForDate(String paramString, int paramInt1, int paramInt2, List<TypeAdapterFactory> paramList) {
        DefaultDateTypeAdapter localDefaultDateTypeAdapter;
        if ((paramString != null) && (!"".equals(paramString.trim()))) {
            localDefaultDateTypeAdapter = new DefaultDateTypeAdapter(paramString);
        } else if ((paramInt1 != 2) && (paramInt2 != 2)) {
            localDefaultDateTypeAdapter = new DefaultDateTypeAdapter(paramInt1, paramInt2);
        } else {
            return;
        }
        paramList.add(TreeTypeAdapter.newFactory(TypeToken.get(java.util.Date.class), localDefaultDateTypeAdapter));
        paramList.add(TreeTypeAdapter.newFactory(TypeToken.get(Timestamp.class), localDefaultDateTypeAdapter));
        paramList.add(TreeTypeAdapter.newFactory(TypeToken.get(java.sql.Date.class), localDefaultDateTypeAdapter));
    }
}




