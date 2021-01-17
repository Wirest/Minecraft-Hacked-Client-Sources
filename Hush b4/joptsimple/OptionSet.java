// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Iterator;
import java.util.Collections;
import joptsimple.internal.Objects;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class OptionSet
{
    private final List<OptionSpec<?>> detectedSpecs;
    private final Map<String, AbstractOptionSpec<?>> detectedOptions;
    private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
    private final Map<String, AbstractOptionSpec<?>> recognizedSpecs;
    private final Map<String, List<?>> defaultValues;
    
    OptionSet(final Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
        this.detectedSpecs = new ArrayList<OptionSpec<?>>();
        this.detectedOptions = new HashMap<String, AbstractOptionSpec<?>>();
        this.optionsToArguments = new IdentityHashMap<AbstractOptionSpec<?>, List<String>>();
        this.defaultValues = defaultValues(recognizedSpecs);
        this.recognizedSpecs = recognizedSpecs;
    }
    
    public boolean hasOptions() {
        return !this.detectedOptions.isEmpty();
    }
    
    public boolean has(final String option) {
        return this.detectedOptions.containsKey(option);
    }
    
    public boolean has(final OptionSpec<?> option) {
        return this.optionsToArguments.containsKey(option);
    }
    
    public boolean hasArgument(final String option) {
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        return spec != null && this.hasArgument(spec);
    }
    
    public boolean hasArgument(final OptionSpec<?> option) {
        Objects.ensureNotNull(option);
        final List<String> values = this.optionsToArguments.get(option);
        return values != null && !values.isEmpty();
    }
    
    public Object valueOf(final String option) {
        Objects.ensureNotNull(option);
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        if (spec == null) {
            final List<?> defaults = this.defaultValuesFor(option);
            return defaults.isEmpty() ? null : defaults.get(0);
        }
        return this.valueOf(spec);
    }
    
    public <V> V valueOf(final OptionSpec<V> option) {
        Objects.ensureNotNull(option);
        final List<V> values = this.valuesOf(option);
        switch (values.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return values.get(0);
            }
            default: {
                throw new MultipleArgumentsForOptionException(option.options());
            }
        }
    }
    
    public List<?> valuesOf(final String option) {
        Objects.ensureNotNull(option);
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        return (spec == null) ? this.defaultValuesFor(option) : this.valuesOf(spec);
    }
    
    public <V> List<V> valuesOf(final OptionSpec<V> option) {
        Objects.ensureNotNull(option);
        final List<String> values = this.optionsToArguments.get(option);
        if (values == null || values.isEmpty()) {
            return (List<V>)this.defaultValueFor((OptionSpec<Object>)option);
        }
        final AbstractOptionSpec<V> spec = (AbstractOptionSpec<V>)(AbstractOptionSpec)option;
        final List<V> convertedValues = new ArrayList<V>();
        for (final String each : values) {
            convertedValues.add(spec.convert(each));
        }
        return Collections.unmodifiableList((List<? extends V>)convertedValues);
    }
    
    public List<OptionSpec<?>> specs() {
        final List<OptionSpec<?>> specs = this.detectedSpecs;
        specs.remove(this.detectedOptions.get("[arguments]"));
        return Collections.unmodifiableList((List<? extends OptionSpec<?>>)specs);
    }
    
    public Map<OptionSpec<?>, List<?>> asMap() {
        final Map<OptionSpec<?>, List<?>> map = new HashMap<OptionSpec<?>, List<?>>();
        for (final AbstractOptionSpec<?> spec : this.recognizedSpecs.values()) {
            if (!spec.representsNonOptions()) {
                map.put(spec, this.valuesOf(spec));
            }
        }
        return Collections.unmodifiableMap((Map<? extends OptionSpec<?>, ? extends List<?>>)map);
    }
    
    public List<?> nonOptionArguments() {
        return Collections.unmodifiableList((List<?>)this.valuesOf(this.detectedOptions.get("[arguments]")));
    }
    
    void add(final AbstractOptionSpec<?> spec) {
        this.addWithArgument(spec, null);
    }
    
    void addWithArgument(final AbstractOptionSpec<?> spec, final String argument) {
        this.detectedSpecs.add(spec);
        for (final String each : spec.options()) {
            this.detectedOptions.put(each, spec);
        }
        List<String> optionArguments = this.optionsToArguments.get(spec);
        if (optionArguments == null) {
            optionArguments = new ArrayList<String>();
            this.optionsToArguments.put(spec, optionArguments);
        }
        if (argument != null) {
            optionArguments.add(argument);
        }
    }
    
    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || !this.getClass().equals(that.getClass())) {
            return false;
        }
        final OptionSet other = (OptionSet)that;
        final Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
        final Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(other.optionsToArguments);
        return this.detectedOptions.equals(other.detectedOptions) && thisOptionsToArguments.equals(otherOptionsToArguments);
    }
    
    @Override
    public int hashCode() {
        final Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
        return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
    }
    
    private <V> List<V> defaultValuesFor(final String option) {
        if (this.defaultValues.containsKey(option)) {
            return (List<V>)this.defaultValues.get(option);
        }
        return Collections.emptyList();
    }
    
    private <V> List<V> defaultValueFor(final OptionSpec<V> option) {
        return this.defaultValuesFor(option.options().iterator().next());
    }
    
    private static Map<String, List<?>> defaultValues(final Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
        final Map<String, List<?>> defaults = new HashMap<String, List<?>>();
        for (final Map.Entry<String, AbstractOptionSpec<?>> each : recognizedSpecs.entrySet()) {
            defaults.put(each.getKey(), each.getValue().defaultValues());
        }
        return defaults;
    }
}
