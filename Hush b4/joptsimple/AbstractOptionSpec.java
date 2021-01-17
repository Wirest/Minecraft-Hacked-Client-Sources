// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Iterator;
import joptsimple.internal.ReflectionException;
import joptsimple.internal.Reflection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class AbstractOptionSpec<V> implements OptionSpec<V>, OptionDescriptor
{
    private final List<String> options;
    private final String description;
    private boolean forHelp;
    
    protected AbstractOptionSpec(final String option) {
        this(Collections.singletonList(option), "");
    }
    
    protected AbstractOptionSpec(final Collection<String> options, final String description) {
        this.options = new ArrayList<String>();
        this.arrangeOptions(options);
        this.description = description;
    }
    
    public final Collection<String> options() {
        return (Collection<String>)Collections.unmodifiableList((List<?>)this.options);
    }
    
    public final List<V> values(final OptionSet detectedOptions) {
        return detectedOptions.valuesOf((OptionSpec<V>)this);
    }
    
    public final V value(final OptionSet detectedOptions) {
        return detectedOptions.valueOf((OptionSpec<V>)this);
    }
    
    public String description() {
        return this.description;
    }
    
    public final AbstractOptionSpec<V> forHelp() {
        this.forHelp = true;
        return this;
    }
    
    public final boolean isForHelp() {
        return this.forHelp;
    }
    
    public boolean representsNonOptions() {
        return false;
    }
    
    protected abstract V convert(final String p0);
    
    protected V convertWith(final ValueConverter<V> converter, final String argument) {
        try {
            return Reflection.convertWith(converter, argument);
        }
        catch (ReflectionException ex) {
            throw new OptionArgumentConversionException(this.options(), argument, ex);
        }
        catch (ValueConversionException ex2) {
            throw new OptionArgumentConversionException(this.options(), argument, ex2);
        }
    }
    
    protected String argumentTypeIndicatorFrom(final ValueConverter<V> converter) {
        if (converter == null) {
            return null;
        }
        final String pattern = converter.valuePattern();
        return (pattern == null) ? converter.valueType().getName() : pattern;
    }
    
    abstract void handleOption(final OptionParser p0, final ArgumentList p1, final OptionSet p2, final String p3);
    
    private void arrangeOptions(final Collection<String> unarranged) {
        if (unarranged.size() == 1) {
            this.options.addAll(unarranged);
            return;
        }
        final List<String> shortOptions = new ArrayList<String>();
        final List<String> longOptions = new ArrayList<String>();
        for (final String each : unarranged) {
            if (each.length() == 1) {
                shortOptions.add(each);
            }
            else {
                longOptions.add(each);
            }
        }
        Collections.sort(shortOptions);
        Collections.sort(longOptions);
        this.options.addAll(shortOptions);
        this.options.addAll(longOptions);
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof AbstractOptionSpec)) {
            return false;
        }
        final AbstractOptionSpec<?> other = (AbstractOptionSpec<?>)that;
        return this.options.equals(other.options);
    }
    
    @Override
    public int hashCode() {
        return this.options.hashCode();
    }
    
    @Override
    public String toString() {
        return this.options.toString();
    }
}
