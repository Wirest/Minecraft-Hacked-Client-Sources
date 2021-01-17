// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.List;
import joptsimple.internal.Classes;
import java.util.Iterator;
import joptsimple.internal.Strings;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Map;
import joptsimple.internal.Rows;

public class BuiltinHelpFormatter implements HelpFormatter
{
    private final Rows nonOptionRows;
    private final Rows optionRows;
    
    BuiltinHelpFormatter() {
        this(80, 2);
    }
    
    public BuiltinHelpFormatter(final int desiredOverallWidth, final int desiredColumnSeparatorWidth) {
        this.nonOptionRows = new Rows(desiredOverallWidth * 2, 0);
        this.optionRows = new Rows(desiredOverallWidth, desiredColumnSeparatorWidth);
    }
    
    public String format(final Map<String, ? extends OptionDescriptor> options) {
        final Comparator<OptionDescriptor> comparator = new Comparator<OptionDescriptor>() {
            public int compare(final OptionDescriptor first, final OptionDescriptor second) {
                return first.options().iterator().next().compareTo((String)second.options().iterator().next());
            }
        };
        final Set<OptionDescriptor> sorted = new TreeSet<OptionDescriptor>(comparator);
        sorted.addAll(options.values());
        this.addRows(sorted);
        return this.formattedHelpOutput();
    }
    
    private String formattedHelpOutput() {
        final StringBuilder formatted = new StringBuilder();
        final String nonOptionDisplay = this.nonOptionRows.render();
        if (!Strings.isNullOrEmpty(nonOptionDisplay)) {
            formatted.append(nonOptionDisplay).append(Strings.LINE_SEPARATOR);
        }
        formatted.append(this.optionRows.render());
        return formatted.toString();
    }
    
    private void addRows(final Collection<? extends OptionDescriptor> options) {
        this.addNonOptionsDescription(options);
        if (options.isEmpty()) {
            this.optionRows.add("No options specified", "");
        }
        else {
            this.addHeaders(options);
            this.addOptions(options);
        }
        this.fitRowsToWidth();
    }
    
    private void addNonOptionsDescription(final Collection<? extends OptionDescriptor> options) {
        final OptionDescriptor nonOptions = this.findAndRemoveNonOptionsSpec(options);
        if (this.shouldShowNonOptionArgumentDisplay(nonOptions)) {
            this.nonOptionRows.add("Non-option arguments:", "");
            this.nonOptionRows.add(this.createNonOptionArgumentsDisplay(nonOptions), "");
        }
    }
    
    private boolean shouldShowNonOptionArgumentDisplay(final OptionDescriptor nonOptions) {
        return !Strings.isNullOrEmpty(nonOptions.description()) || !Strings.isNullOrEmpty(nonOptions.argumentTypeIndicator()) || !Strings.isNullOrEmpty(nonOptions.argumentDescription());
    }
    
    private String createNonOptionArgumentsDisplay(final OptionDescriptor nonOptions) {
        final StringBuilder buffer = new StringBuilder();
        this.maybeAppendOptionInfo(buffer, nonOptions);
        this.maybeAppendNonOptionsDescription(buffer, nonOptions);
        return buffer.toString();
    }
    
    private void maybeAppendNonOptionsDescription(final StringBuilder buffer, final OptionDescriptor nonOptions) {
        buffer.append((buffer.length() > 0 && !Strings.isNullOrEmpty(nonOptions.description())) ? " -- " : "").append(nonOptions.description());
    }
    
    private OptionDescriptor findAndRemoveNonOptionsSpec(final Collection<? extends OptionDescriptor> options) {
        final Iterator<? extends OptionDescriptor> it = options.iterator();
        while (it.hasNext()) {
            final OptionDescriptor next = (OptionDescriptor)it.next();
            if (next.representsNonOptions()) {
                it.remove();
                return next;
            }
        }
        throw new AssertionError((Object)"no non-options argument spec");
    }
    
    private void addHeaders(final Collection<? extends OptionDescriptor> options) {
        if (this.hasRequiredOption(options)) {
            this.optionRows.add("Option (* = required)", "Description");
            this.optionRows.add("---------------------", "-----------");
        }
        else {
            this.optionRows.add("Option", "Description");
            this.optionRows.add("------", "-----------");
        }
    }
    
    private boolean hasRequiredOption(final Collection<? extends OptionDescriptor> options) {
        for (final OptionDescriptor each : options) {
            if (each.isRequired()) {
                return true;
            }
        }
        return false;
    }
    
    private void addOptions(final Collection<? extends OptionDescriptor> options) {
        for (final OptionDescriptor each : options) {
            if (!each.representsNonOptions()) {
                this.optionRows.add(this.createOptionDisplay(each), this.createDescriptionDisplay(each));
            }
        }
    }
    
    private String createOptionDisplay(final OptionDescriptor descriptor) {
        final StringBuilder buffer = new StringBuilder(descriptor.isRequired() ? "* " : "");
        final Iterator<String> i = descriptor.options().iterator();
        while (i.hasNext()) {
            final String option = i.next();
            buffer.append((option.length() > 1) ? "--" : ParserRules.HYPHEN);
            buffer.append(option);
            if (i.hasNext()) {
                buffer.append(", ");
            }
        }
        this.maybeAppendOptionInfo(buffer, descriptor);
        return buffer.toString();
    }
    
    private void maybeAppendOptionInfo(final StringBuilder buffer, final OptionDescriptor descriptor) {
        final String indicator = this.extractTypeIndicator(descriptor);
        final String description = descriptor.argumentDescription();
        if (indicator != null || !Strings.isNullOrEmpty(description)) {
            this.appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument());
        }
    }
    
    private String extractTypeIndicator(final OptionDescriptor descriptor) {
        final String indicator = descriptor.argumentTypeIndicator();
        if (!Strings.isNullOrEmpty(indicator) && !String.class.getName().equals(indicator)) {
            return Classes.shortNameOf(indicator);
        }
        return null;
    }
    
    private void appendOptionHelp(final StringBuilder buffer, final String typeIndicator, final String description, final boolean required) {
        if (required) {
            this.appendTypeIndicator(buffer, typeIndicator, description, '<', '>');
        }
        else {
            this.appendTypeIndicator(buffer, typeIndicator, description, '[', ']');
        }
    }
    
    private void appendTypeIndicator(final StringBuilder buffer, final String typeIndicator, final String description, final char start, final char end) {
        buffer.append(' ').append(start);
        if (typeIndicator != null) {
            buffer.append(typeIndicator);
        }
        if (!Strings.isNullOrEmpty(description)) {
            if (typeIndicator != null) {
                buffer.append(": ");
            }
            buffer.append(description);
        }
        buffer.append(end);
    }
    
    private String createDescriptionDisplay(final OptionDescriptor descriptor) {
        final List<?> defaultValues = descriptor.defaultValues();
        if (defaultValues.isEmpty()) {
            return descriptor.description();
        }
        final String defaultValuesDisplay = this.createDefaultValuesDisplay(defaultValues);
        return (descriptor.description() + ' ' + Strings.surround("default: " + defaultValuesDisplay, '(', ')')).trim();
    }
    
    private String createDefaultValuesDisplay(final List<?> defaultValues) {
        return (defaultValues.size() == 1) ? defaultValues.get(0).toString() : defaultValues.toString();
    }
    
    private void fitRowsToWidth() {
        this.nonOptionRows.fitToWidth();
        this.optionRows.fitToWidth();
    }
}
