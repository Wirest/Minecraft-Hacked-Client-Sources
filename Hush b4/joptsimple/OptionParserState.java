// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

abstract class OptionParserState
{
    static OptionParserState noMoreOptions() {
        return new OptionParserState() {
            @Override
            protected void handleArgument(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions) {
                parser.handleNonOptionArgument(arguments.next(), arguments, detectedOptions);
            }
        };
    }
    
    static OptionParserState moreOptions(final boolean posixlyCorrect) {
        return new OptionParserState() {
            @Override
            protected void handleArgument(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions) {
                final String candidate = arguments.next();
                try {
                    if (ParserRules.isOptionTerminator(candidate)) {
                        parser.noMoreOptions();
                        return;
                    }
                    if (ParserRules.isLongOptionToken(candidate)) {
                        parser.handleLongOptionToken(candidate, arguments, detectedOptions);
                        return;
                    }
                    if (ParserRules.isShortOptionToken(candidate)) {
                        parser.handleShortOptionToken(candidate, arguments, detectedOptions);
                        return;
                    }
                }
                catch (UnrecognizedOptionException e) {
                    if (!parser.doesAllowsUnrecognizedOptions()) {
                        throw e;
                    }
                }
                if (posixlyCorrect) {
                    parser.noMoreOptions();
                }
                parser.handleNonOptionArgument(candidate, arguments, detectedOptions);
            }
        };
    }
    
    protected abstract void handleArgument(final OptionParser p0, final ArgumentList p1, final OptionSet p2);
}
