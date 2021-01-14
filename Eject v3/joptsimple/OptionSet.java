package joptsimple;

import joptsimple.internal.Objects;

import java.util.*;
import java.util.Map.Entry;

public class OptionSet {
    private final List<OptionSpec<?>> detectedSpecs = new ArrayList();
    private final Map<String, AbstractOptionSpec<?>> detectedOptions = new HashMap();
    private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments = new IdentityHashMap();
    private final Map<String, AbstractOptionSpec<?>> recognizedSpecs;
    private final Map<String, List<?>> defaultValues;

    OptionSet(Map<String, AbstractOptionSpec<?>> paramMap) {
        this.defaultValues = defaultValues(paramMap);
        this.recognizedSpecs = paramMap;
    }

    private static Map<String, List<?>> defaultValues(Map<String, AbstractOptionSpec<?>> paramMap) {
        HashMap localHashMap = new HashMap();
        Iterator localIterator = paramMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            localHashMap.put(localEntry.getKey(), ((AbstractOptionSpec) localEntry.getValue()).defaultValues());
        }
        return localHashMap;
    }

    public boolean hasOptions() {
        return !this.detectedOptions.isEmpty();
    }

    public boolean has(String paramString) {
        return this.detectedOptions.containsKey(paramString);
    }

    public boolean has(OptionSpec<?> paramOptionSpec) {
        return this.optionsToArguments.containsKey(paramOptionSpec);
    }

    public boolean hasArgument(String paramString) {
        AbstractOptionSpec localAbstractOptionSpec = (AbstractOptionSpec) this.detectedOptions.get(paramString);
        return (localAbstractOptionSpec != null) && (hasArgument(localAbstractOptionSpec));
    }

    public boolean hasArgument(OptionSpec<?> paramOptionSpec) {
        Objects.ensureNotNull(paramOptionSpec);
        List localList = (List) this.optionsToArguments.get(paramOptionSpec);
        return (localList != null) && (!localList.isEmpty());
    }

    public Object valueOf(String paramString) {
        Objects.ensureNotNull(paramString);
        AbstractOptionSpec localAbstractOptionSpec = (AbstractOptionSpec) this.detectedOptions.get(paramString);
        if (localAbstractOptionSpec == null) {
            List localList = defaultValuesFor(paramString);
            return localList.isEmpty() ? null : localList.get(0);
        }
        return valueOf(localAbstractOptionSpec);
    }

    public <V> V valueOf(OptionSpec<V> paramOptionSpec) {
        Objects.ensureNotNull(paramOptionSpec);
        List localList = valuesOf(paramOptionSpec);
        switch (localList.size()) {
            case 0:
                return null;
            case 1:
                return (V) localList.get(0);
        }
        throw new MultipleArgumentsForOptionException(paramOptionSpec.options());
    }

    public List<?> valuesOf(String paramString) {
        Objects.ensureNotNull(paramString);
        AbstractOptionSpec localAbstractOptionSpec = (AbstractOptionSpec) this.detectedOptions.get(paramString);
        return localAbstractOptionSpec == null ? defaultValuesFor(paramString) : valuesOf(localAbstractOptionSpec);
    }

    public <V> List<V> valuesOf(OptionSpec<V> paramOptionSpec) {
        Objects.ensureNotNull(paramOptionSpec);
        List localList = (List) this.optionsToArguments.get(paramOptionSpec);
        if ((localList == null) || (localList.isEmpty())) {
            return defaultValueFor(paramOptionSpec);
        }
        AbstractOptionSpec localAbstractOptionSpec = (AbstractOptionSpec) paramOptionSpec;
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            localArrayList.add(localAbstractOptionSpec.convert(str));
        }
        return Collections.unmodifiableList(localArrayList);
    }

    public List<OptionSpec<?>> specs() {
        List localList = this.detectedSpecs;
        localList.remove(this.detectedOptions.get("[arguments]"));
        return Collections.unmodifiableList(localList);
    }

    public Map<OptionSpec<?>, List<?>> asMap() {
        HashMap localHashMap = new HashMap();
        Iterator localIterator = this.recognizedSpecs.values().iterator();
        while (localIterator.hasNext()) {
            AbstractOptionSpec localAbstractOptionSpec = (AbstractOptionSpec) localIterator.next();
            if (!localAbstractOptionSpec.representsNonOptions()) {
                localHashMap.put(localAbstractOptionSpec, valuesOf(localAbstractOptionSpec));
            }
        }
        return Collections.unmodifiableMap(localHashMap);
    }

    public List<?> nonOptionArguments() {
        return Collections.unmodifiableList(valuesOf((OptionSpec) this.detectedOptions.get("[arguments]")));
    }

    void add(AbstractOptionSpec<?> paramAbstractOptionSpec) {
        addWithArgument(paramAbstractOptionSpec, null);
    }

    void addWithArgument(AbstractOptionSpec<?> paramAbstractOptionSpec, String paramString) {
        this.detectedSpecs.add(paramAbstractOptionSpec);
        Object localObject = paramAbstractOptionSpec.options().iterator();
        while (((Iterator) localObject).hasNext()) {
            String str = (String) ((Iterator) localObject).next();
            this.detectedOptions.put(str, paramAbstractOptionSpec);
        }
        localObject = (List) this.optionsToArguments.get(paramAbstractOptionSpec);
        if (localObject == null) {
            localObject = new ArrayList();
            this.optionsToArguments.put(paramAbstractOptionSpec, localObject);
        }
        if (paramString != null) {
            ((List) localObject).add(paramString);
        }
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject == null) || (!getClass().equals(paramObject.getClass()))) {
            return false;
        }
        OptionSet localOptionSet = (OptionSet) paramObject;
        HashMap localHashMap1 = new HashMap(this.optionsToArguments);
        HashMap localHashMap2 = new HashMap(localOptionSet.optionsToArguments);
        return (this.detectedOptions.equals(localOptionSet.detectedOptions)) && (localHashMap1.equals(localHashMap2));
    }

    public int hashCode() {
        HashMap localHashMap = new HashMap(this.optionsToArguments);
        return this.detectedOptions.hashCode() + localHashMap.hashCode();
    }

    private <V> List<V> defaultValuesFor(String paramString) {
        if (this.defaultValues.containsKey(paramString)) {
            return (List) this.defaultValues.get(paramString);
        }
        return Collections.emptyList();
    }

    private <V> List<V> defaultValueFor(OptionSpec<V> paramOptionSpec) {
        return defaultValuesFor((String) paramOptionSpec.options().iterator().next());
    }
}




