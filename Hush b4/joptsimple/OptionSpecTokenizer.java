// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.NoSuchElementException;

class OptionSpecTokenizer
{
    private static final char POSIXLY_CORRECT_MARKER = '+';
    private static final char HELP_MARKER = '*';
    private String specification;
    private int index;
    
    OptionSpecTokenizer(final String specification) {
        if (specification == null) {
            throw new NullPointerException("null option specification");
        }
        this.specification = specification;
    }
    
    boolean hasMore() {
        return this.index < this.specification.length();
    }
    
    AbstractOptionSpec<?> next() {
        if (!this.hasMore()) {
            throw new NoSuchElementException();
        }
        final String optionCandidate = String.valueOf(this.specification.charAt(this.index));
        ++this.index;
        if ("W".equals(optionCandidate)) {
            final AbstractOptionSpec<?> spec = this.handleReservedForExtensionsToken();
            if (spec != null) {
                return spec;
            }
        }
        ParserRules.ensureLegalOption(optionCandidate);
        AbstractOptionSpec<?> spec;
        if (this.hasMore()) {
            boolean forHelp = false;
            if (this.specification.charAt(this.index) == '*') {
                forHelp = true;
                ++this.index;
            }
            spec = ((this.hasMore() && this.specification.charAt(this.index) == ':') ? this.handleArgumentAcceptingOption(optionCandidate) : new NoArgumentOptionSpec(optionCandidate));
            if (forHelp) {
                spec.forHelp();
            }
        }
        else {
            spec = new NoArgumentOptionSpec(optionCandidate);
        }
        return spec;
    }
    
    void configure(final OptionParser parser) {
        this.adjustForPosixlyCorrect(parser);
        while (this.hasMore()) {
            parser.recognize(this.next());
        }
    }
    
    private void adjustForPosixlyCorrect(final OptionParser parser) {
        if ('+' == this.specification.charAt(0)) {
            parser.posixlyCorrect(true);
            this.specification = this.specification.substring(1);
        }
    }
    
    private AbstractOptionSpec<?> handleReservedForExtensionsToken() {
        if (!this.hasMore()) {
            return new NoArgumentOptionSpec("W");
        }
        if (this.specification.charAt(this.index) == ';') {
            ++this.index;
            return new AlternativeLongOptionSpec();
        }
        return null;
    }
    
    private AbstractOptionSpec<?> handleArgumentAcceptingOption(final String candidate) {
        ++this.index;
        if (this.hasMore() && this.specification.charAt(this.index) == ':') {
            ++this.index;
            return new OptionalArgumentOptionSpec<Object>(candidate);
        }
        return new RequiredArgumentOptionSpec<Object>(candidate);
    }
}
