// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collections;
import java.util.StringTokenizer;
import joptsimple.internal.Strings;
import joptsimple.internal.Objects;
import joptsimple.internal.Reflection;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class ArgumentAcceptingOptionSpec<V> extends AbstractOptionSpec<V>
{
    private static final char NIL_VALUE_SEPARATOR = '\0';
    private boolean optionRequired;
    private final boolean argumentRequired;
    private ValueConverter<V> converter;
    private String argumentDescription;
    private String valueSeparator;
    private final List<V> defaultValues;
    
    ArgumentAcceptingOptionSpec(final String option, final boolean argumentRequired) {
        super(option);
        this.argumentDescription = "";
        this.valueSeparator = String.valueOf('\0');
        this.defaultValues = new ArrayList<V>();
        this.argumentRequired = argumentRequired;
    }
    
    ArgumentAcceptingOptionSpec(final Collection<String> options, final boolean argumentRequired, final String description) {
        super(options, description);
        this.argumentDescription = "";
        this.valueSeparator = String.valueOf('\0');
        this.defaultValues = new ArrayList<V>();
        this.argumentRequired = argumentRequired;
    }
    
    public final <T> ArgumentAcceptingOptionSpec<T> ofType(final Class<T> argumentType) {
        return this.withValuesConvertedBy((ValueConverter<T>)Reflection.findConverter((Class<T>)argumentType));
    }
    
    public final <T> ArgumentAcceptingOptionSpec<T> withValuesConvertedBy(final ValueConverter<T> aConverter) {
        if (aConverter == null) {
            throw new NullPointerException("illegal null converter");
        }
        this.converter = (ValueConverter<V>)aConverter;
        return (ArgumentAcceptingOptionSpec<T>)this;
    }
    
    public final ArgumentAcceptingOptionSpec<V> describedAs(final String description) {
        this.argumentDescription = description;
        return this;
    }
    
    public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(final char separator) {
        if (separator == '\0') {
            throw new IllegalArgumentException("cannot use U+0000 as separator");
        }
        this.valueSeparator = String.valueOf(separator);
        return this;
    }
    
    public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(final String separator) {
        if (separator.indexOf(0) != -1) {
            throw new IllegalArgumentException("cannot use U+0000 in separator");
        }
        this.valueSeparator = separator;
        return this;
    }
    
    public ArgumentAcceptingOptionSpec<V> defaultsTo(final V value, final V... values) {
        this.addDefaultValue(value);
        this.defaultsTo(values);
        return this;
    }
    
    public ArgumentAcceptingOptionSpec<V> defaultsTo(final V[] values) {
        for (final V each : values) {
            this.addDefaultValue(each);
        }
        return this;
    }
    
    public ArgumentAcceptingOptionSpec<V> required() {
        this.optionRequired = true;
        return this;
    }
    
    public boolean isRequired() {
        return this.optionRequired;
    }
    
    private void addDefaultValue(final V value) {
        Objects.ensureNotNull(value);
        this.defaultValues.add(value);
    }
    
    @Override
    final void handleOption(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions, final String detectedArgument) {
        if (Strings.isNullOrEmpty(detectedArgument)) {
            this.detectOptionArgument(parser, arguments, detectedOptions);
        }
        else {
            this.addArguments(detectedOptions, detectedArgument);
        }
    }
    
    protected void addArguments(final OptionSet detectedOptions, final String detectedArgument) {
        final StringTokenizer lexer = new StringTokenizer(detectedArgument, this.valueSeparator);
        if (!lexer.hasMoreTokens()) {
            detectedOptions.addWithArgument(this, detectedArgument);
        }
        else {
            while (lexer.hasMoreTokens()) {
                detectedOptions.addWithArgument(this, lexer.nextToken());
            }
        }
    }
    
    protected abstract void detectOptionArgument(final OptionParser p0, final ArgumentList p1, final OptionSet p2);
    
    @Override
    protected final V convert(final String argument) {
        return this.convertWith(this.converter, argument);
    }
    
    protected boolean canConvertArgument(final String argument) {
        final StringTokenizer lexer = new StringTokenizer(argument, this.valueSeparator);
        try {
            while (lexer.hasMoreTokens()) {
                this.convert(lexer.nextToken());
            }
            return true;
        }
        catch (OptionException ignored) {
            return false;
        }
    }
    
    protected boolean isArgumentOfNumberType() {
        return this.converter != null && Number.class.isAssignableFrom(this.converter.valueType());
    }
    
    public boolean acceptsArguments() {
        return true;
    }
    
    public boolean requiresArgument() {
        return this.argumentRequired;
    }
    
    public String argumentDescription() {
        return this.argumentDescription;
    }
    
    public String argumentTypeIndicator() {
        return this.argumentTypeIndicatorFrom(this.converter);
    }
    
    public List<V> defaultValues() {
        return Collections.unmodifiableList((List<? extends V>)this.defaultValues);
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!super.equals(that)) {
            return false;
        }
        final ArgumentAcceptingOptionSpec<?> other = (ArgumentAcceptingOptionSpec<?>)that;
        return this.requiresArgument() == other.requiresArgument();
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
    }
}
