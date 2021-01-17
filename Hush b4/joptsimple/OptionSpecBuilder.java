// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

public class OptionSpecBuilder extends NoArgumentOptionSpec
{
    private final OptionParser parser;
    
    OptionSpecBuilder(final OptionParser parser, final Collection<String> options, final String description) {
        super(options, description);
        this.parser = parser;
        this.attachToParser();
    }
    
    private void attachToParser() {
        this.parser.recognize(this);
    }
    
    public ArgumentAcceptingOptionSpec<String> withRequiredArg() {
        final ArgumentAcceptingOptionSpec<String> newSpec = new RequiredArgumentOptionSpec<String>(this.options(), this.description());
        this.parser.recognize(newSpec);
        return newSpec;
    }
    
    public ArgumentAcceptingOptionSpec<String> withOptionalArg() {
        final ArgumentAcceptingOptionSpec<String> newSpec = new OptionalArgumentOptionSpec<String>(this.options(), this.description());
        this.parser.recognize(newSpec);
        return newSpec;
    }
    
    public OptionSpecBuilder requiredIf(final String dependent, final String... otherDependents) {
        final List<String> dependents = this.validatedDependents(dependent, otherDependents);
        for (final String each : dependents) {
            this.parser.requiredIf(this.options(), each);
        }
        return this;
    }
    
    public OptionSpecBuilder requiredIf(final OptionSpec<?> dependent, final OptionSpec<?>... otherDependents) {
        this.parser.requiredIf(this.options(), dependent);
        for (final OptionSpec<?> each : otherDependents) {
            this.parser.requiredIf(this.options(), each);
        }
        return this;
    }
    
    public OptionSpecBuilder requiredUnless(final String dependent, final String... otherDependents) {
        final List<String> dependents = this.validatedDependents(dependent, otherDependents);
        for (final String each : dependents) {
            this.parser.requiredUnless(this.options(), each);
        }
        return this;
    }
    
    public OptionSpecBuilder requiredUnless(final OptionSpec<?> dependent, final OptionSpec<?>... otherDependents) {
        this.parser.requiredUnless(this.options(), dependent);
        for (final OptionSpec<?> each : otherDependents) {
            this.parser.requiredUnless(this.options(), each);
        }
        return this;
    }
    
    private List<String> validatedDependents(final String dependent, final String... otherDependents) {
        final List<String> dependents = new ArrayList<String>();
        dependents.add(dependent);
        Collections.addAll(dependents, otherDependents);
        for (final String each : dependents) {
            if (!this.parser.isRecognized(each)) {
                throw new UnconfiguredOptionException(each);
            }
        }
        return dependents;
    }
}
