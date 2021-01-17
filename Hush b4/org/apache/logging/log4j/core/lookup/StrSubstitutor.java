// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map;

public class StrSubstitutor
{
    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX;
    public static final StrMatcher DEFAULT_SUFFIX;
    private static final int BUF_SIZE = 256;
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrLookup variableResolver;
    private boolean enableSubstitutionInVariables;
    
    public StrSubstitutor() {
        this(null, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public StrSubstitutor(final Map<String, String> valueMap) {
        this(new MapLookup(valueMap), StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public StrSubstitutor(final Map<String, String> valueMap, final String prefix, final String suffix) {
        this(new MapLookup(valueMap), prefix, suffix, '$');
    }
    
    public StrSubstitutor(final Map<String, String> valueMap, final String prefix, final String suffix, final char escape) {
        this(new MapLookup(valueMap), prefix, suffix, escape);
    }
    
    public StrSubstitutor(final StrLookup variableResolver) {
        this(variableResolver, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public StrSubstitutor(final StrLookup variableResolver, final String prefix, final String suffix, final char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
    }
    
    public StrSubstitutor(final StrLookup variableResolver, final StrMatcher prefixMatcher, final StrMatcher suffixMatcher, final char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
    }
    
    public static String replace(final Object source, final Map<String, String> valueMap) {
        return new StrSubstitutor(valueMap).replace(source);
    }
    
    public static String replace(final Object source, final Map<String, String> valueMap, final String prefix, final String suffix) {
        return new StrSubstitutor(valueMap, prefix, suffix).replace(source);
    }
    
    public static String replace(final Object source, final Properties valueProperties) {
        if (valueProperties == null) {
            return source.toString();
        }
        final Map<String, String> valueMap = new HashMap<String, String>();
        final Enumeration<?> propNames = valueProperties.propertyNames();
        while (propNames.hasMoreElements()) {
            final String propName = (String)propNames.nextElement();
            final String propValue = valueProperties.getProperty(propName);
            valueMap.put(propName, propValue);
        }
        return replace(source, valueMap);
    }
    
    public String replace(final String source) {
        return this.replace(null, source);
    }
    
    public String replace(final LogEvent event, final String source) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(source);
        if (!this.substitute(event, buf, 0, source.length())) {
            return source;
        }
        return buf.toString();
    }
    
    public String replace(final String source, final int offset, final int length) {
        return this.replace(null, source, offset, length);
    }
    
    public String replace(final LogEvent event, final String source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        if (!this.substitute(event, buf, 0, length)) {
            return source.substring(offset, offset + length);
        }
        return buf.toString();
    }
    
    public String replace(final char[] source) {
        return this.replace(null, source);
    }
    
    public String replace(final LogEvent event, final char[] source) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(source.length).append(source);
        this.substitute(event, buf, 0, source.length);
        return buf.toString();
    }
    
    public String replace(final char[] source, final int offset, final int length) {
        return this.replace(null, source, offset, length);
    }
    
    public String replace(final LogEvent event, final char[] source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final StringBuffer source) {
        return this.replace(null, source);
    }
    
    public String replace(final LogEvent event, final StringBuffer source) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(source.length()).append(source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }
    
    public String replace(final StringBuffer source, final int offset, final int length) {
        return this.replace(null, source, offset, length);
    }
    
    public String replace(final LogEvent event, final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final StringBuilder source) {
        return this.replace(null, source);
    }
    
    public String replace(final LogEvent event, final StringBuilder source) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(source.length()).append((CharSequence)source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }
    
    public String replace(final StringBuilder source, final int offset, final int length) {
        return this.replace(null, source, offset, length);
    }
    
    public String replace(final LogEvent event, final StringBuilder source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final Object source) {
        return this.replace(null, source);
    }
    
    public String replace(final LogEvent event, final Object source) {
        if (source == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder().append(source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }
    
    public boolean replaceIn(final StringBuffer source) {
        return source != null && this.replaceIn(source, 0, source.length());
    }
    
    public boolean replaceIn(final StringBuffer source, final int offset, final int length) {
        return this.replaceIn(null, source, offset, length);
    }
    
    public boolean replaceIn(final LogEvent event, final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        final StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        if (!this.substitute(event, buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }
    
    public boolean replaceIn(final StringBuilder source) {
        return this.replaceIn(null, source);
    }
    
    public boolean replaceIn(final LogEvent event, final StringBuilder source) {
        return source != null && this.substitute(event, source, 0, source.length());
    }
    
    public boolean replaceIn(final StringBuilder source, final int offset, final int length) {
        return this.replaceIn(null, source, offset, length);
    }
    
    public boolean replaceIn(final LogEvent event, final StringBuilder source, final int offset, final int length) {
        return source != null && this.substitute(event, source, offset, length);
    }
    
    protected boolean substitute(final LogEvent event, final StringBuilder buf, final int offset, final int length) {
        return this.substitute(event, buf, offset, length, null) > 0;
    }
    
    private int substitute(final LogEvent event, final StringBuilder buf, final int offset, final int length, List<String> priorVariables) {
        final StrMatcher prefixMatcher = this.getVariablePrefixMatcher();
        final StrMatcher suffixMatcher = this.getVariableSuffixMatcher();
        final char escape = this.getEscapeChar();
        final boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = this.getChars(buf);
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd) {
            final int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                ++pos;
            }
            else if (pos > offset && chars[pos - 1] == escape) {
                buf.deleteCharAt(pos - 1);
                chars = this.getChars(buf);
                --lengthChange;
                altered = true;
                --bufEnd;
            }
            else {
                final int startPos = pos;
                pos += startMatchLen;
                int endMatchLen = 0;
                int nestedVarCount = 0;
                while (pos < bufEnd) {
                    if (this.isEnableSubstitutionInVariables() && (endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
                        ++nestedVarCount;
                        pos += endMatchLen;
                    }
                    else {
                        endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
                        if (endMatchLen == 0) {
                            ++pos;
                        }
                        else {
                            if (nestedVarCount == 0) {
                                String varName = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                                if (this.isEnableSubstitutionInVariables()) {
                                    final StringBuilder bufName = new StringBuilder(varName);
                                    this.substitute(event, bufName, 0, bufName.length());
                                    varName = bufName.toString();
                                }
                                final int endPos;
                                pos = (endPos = pos + endMatchLen);
                                if (priorVariables == null) {
                                    priorVariables = new ArrayList<String>();
                                    priorVariables.add(new String(chars, offset, length));
                                }
                                this.checkCyclicSubstitution(varName, priorVariables);
                                priorVariables.add(varName);
                                final String varValue = this.resolveVariable(event, varName, buf, startPos, endPos);
                                if (varValue != null) {
                                    final int varLen = varValue.length();
                                    buf.replace(startPos, endPos, varValue);
                                    altered = true;
                                    int change = this.substitute(event, buf, startPos, varLen, priorVariables);
                                    change += varLen - (endPos - startPos);
                                    pos += change;
                                    bufEnd += change;
                                    lengthChange += change;
                                    chars = this.getChars(buf);
                                }
                                priorVariables.remove(priorVariables.size() - 1);
                                break;
                            }
                            --nestedVarCount;
                            pos += endMatchLen;
                        }
                    }
                }
            }
        }
        if (top) {
            return altered ? 1 : 0;
        }
        return lengthChange;
    }
    
    private void checkCyclicSubstitution(final String varName, final List<String> priorVariables) {
        if (!priorVariables.contains(varName)) {
            return;
        }
        final StringBuilder buf = new StringBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        this.appendWithSeparators(buf, priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }
    
    protected String resolveVariable(final LogEvent event, final String variableName, final StringBuilder buf, final int startPos, final int endPos) {
        final StrLookup resolver = this.getVariableResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.lookup(event, variableName);
    }
    
    public char getEscapeChar() {
        return this.escapeChar;
    }
    
    public void setEscapeChar(final char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }
    
    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }
    
    public StrSubstitutor setVariablePrefixMatcher(final StrMatcher prefixMatcher) {
        if (prefixMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        }
        this.prefixMatcher = prefixMatcher;
        return this;
    }
    
    public StrSubstitutor setVariablePrefix(final char prefix) {
        return this.setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
    }
    
    public StrSubstitutor setVariablePrefix(final String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Variable prefix must not be null!");
        }
        return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
    }
    
    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }
    
    public StrSubstitutor setVariableSuffixMatcher(final StrMatcher suffixMatcher) {
        if (suffixMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        }
        this.suffixMatcher = suffixMatcher;
        return this;
    }
    
    public StrSubstitutor setVariableSuffix(final char suffix) {
        return this.setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
    }
    
    public StrSubstitutor setVariableSuffix(final String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Variable suffix must not be null!");
        }
        return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
    }
    
    public StrLookup getVariableResolver() {
        return this.variableResolver;
    }
    
    public void setVariableResolver(final StrLookup variableResolver) {
        this.variableResolver = variableResolver;
    }
    
    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVariables;
    }
    
    public void setEnableSubstitutionInVariables(final boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
    }
    
    private char[] getChars(final StringBuilder sb) {
        final char[] chars = new char[sb.length()];
        sb.getChars(0, sb.length(), chars, 0);
        return chars;
    }
    
    public void appendWithSeparators(final StringBuilder sb, final Iterable<?> iterable, String separator) {
        if (iterable != null) {
            separator = ((separator == null) ? "" : separator);
            final Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(separator);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "StrSubstitutor(" + this.variableResolver.toString() + ")";
    }
    
    static {
        DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
        DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    }
}
