// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;

class OptionalArgumentOptionSpec<V> extends ArgumentAcceptingOptionSpec<V>
{
    OptionalArgumentOptionSpec(final String option) {
        super(option, false);
    }
    
    OptionalArgumentOptionSpec(final Collection<String> options, final String description) {
        super(options, false, description);
    }
    
    @Override
    protected void detectOptionArgument(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions) {
        if (arguments.hasMore()) {
            final String nextArgument = arguments.peek();
            if (!parser.looksLikeAnOption(nextArgument)) {
                this.handleOptionArgument(parser, detectedOptions, arguments);
            }
            else if (this.isArgumentOfNumberType() && this.canConvertArgument(nextArgument)) {
                this.addArguments(detectedOptions, arguments.next());
            }
            else {
                detectedOptions.add(this);
            }
        }
        else {
            detectedOptions.add(this);
        }
    }
    
    private void handleOptionArgument(final OptionParser parser, final OptionSet detectedOptions, final ArgumentList arguments) {
        if (parser.posixlyCorrect()) {
            detectedOptions.add(this);
            parser.noMoreOptions();
        }
        else {
            this.addArguments(detectedOptions, arguments.next());
        }
    }
}
