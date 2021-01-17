// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map;

public class StrSubstitutor
{
    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX;
    public static final StrMatcher DEFAULT_SUFFIX;
    public static final StrMatcher DEFAULT_VALUE_DELIMITER;
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrMatcher valueDelimiterMatcher;
    private StrLookup<?> variableResolver;
    private boolean enableSubstitutionInVariables;
    
    public static <V> String replace(final Object source, final Map<String, V> valueMap) {
        return new StrSubstitutor((Map<String, V>)valueMap).replace(source);
    }
    
    public static <V> String replace(final Object source, final Map<String, V> valueMap, final String prefix, final String suffix) {
        return new StrSubstitutor((Map<String, V>)valueMap, prefix, suffix).replace(source);
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
    
    public static String replaceSystemProperties(final Object source) {
        return new StrSubstitutor(StrLookup.systemPropertiesLookup()).replace(source);
    }
    
    public StrSubstitutor() {
        this(null, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public <V> StrSubstitutor(final Map<String, V> valueMap) {
        this(StrLookup.mapLookup(valueMap), StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
    }
    
    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
    }
    
    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape, final String valueDelimiter) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
    }
    
    public StrSubstitutor(final StrLookup<?> variableResolver) {
        this(variableResolver, StrSubstitutor.DEFAULT_PREFIX, StrSubstitutor.DEFAULT_SUFFIX, '$');
    }
    
    public StrSubstitutor(final StrLookup<?> variableResolver, final String prefix, final String suffix, final char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
        this.setValueDelimiterMatcher(StrSubstitutor.DEFAULT_VALUE_DELIMITER);
    }
    
    public StrSubstitutor(final StrLookup<?> variableResolver, final String prefix, final String suffix, final char escape, final String valueDelimiter) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
        this.setValueDelimiter(valueDelimiter);
    }
    
    public StrSubstitutor(final StrLookup<?> variableResolver, final StrMatcher prefixMatcher, final StrMatcher suffixMatcher, final char escape) {
        this(variableResolver, prefixMatcher, suffixMatcher, escape, StrSubstitutor.DEFAULT_VALUE_DELIMITER);
    }
    
    public StrSubstitutor(final StrLookup<?> variableResolver, final StrMatcher prefixMatcher, final StrMatcher suffixMatcher, final char escape, final StrMatcher valueDelimiterMatcher) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
        this.setValueDelimiterMatcher(valueDelimiterMatcher);
    }
    
    public String replace(final String source) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(source);
        if (!this.substitute(buf, 0, source.length())) {
            return source;
        }
        return buf.toString();
    }
    
    public String replace(final String source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!this.substitute(buf, 0, length)) {
            return source.substring(offset, offset + length);
        }
        return buf.toString();
    }
    
    public String replace(final char[] source) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(source.length).append(source);
        this.substitute(buf, 0, source.length);
        return buf.toString();
    }
    
    public String replace(final char[] source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        this.substitute(buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final StringBuffer source) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(source.length()).append(source);
        this.substitute(buf, 0, buf.length());
        return buf.toString();
    }
    
    public String replace(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        this.substitute(buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final CharSequence source) {
        if (source == null) {
            return null;
        }
        return this.replace(source, 0, source.length());
    }
    
    public String replace(final CharSequence source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        this.substitute(buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final StrBuilder source) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(source.length()).append(source);
        this.substitute(buf, 0, buf.length());
        return buf.toString();
    }
    
    public String replace(final StrBuilder source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        this.substitute(buf, 0, length);
        return buf.toString();
    }
    
    public String replace(final Object source) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder().append(source);
        this.substitute(buf, 0, buf.length());
        return buf.toString();
    }
    
    public boolean replaceIn(final StringBuffer source) {
        return source != null && this.replaceIn(source, 0, source.length());
    }
    
    public boolean replaceIn(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!this.substitute(buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }
    
    public boolean replaceIn(final StringBuilder source) {
        return source != null && this.replaceIn(source, 0, source.length());
    }
    
    public boolean replaceIn(final StringBuilder source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!this.substitute(buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }
    
    public boolean replaceIn(final StrBuilder source) {
        return source != null && this.substitute(source, 0, source.length());
    }
    
    public boolean replaceIn(final StrBuilder source, final int offset, final int length) {
        return source != null && this.substitute(source, offset, length);
    }
    
    protected boolean substitute(final StrBuilder buf, final int offset, final int length) {
        return this.substitute(buf, offset, length, null) > 0;
    }
    
    private int substitute(final StrBuilder buf, final int offset, final int length, List<String> priorVariables) {
        final StrMatcher pfxMatcher = this.getVariablePrefixMatcher();
        final StrMatcher suffMatcher = this.getVariableSuffixMatcher();
        final char escape = this.getEscapeChar();
        final StrMatcher valueDelimMatcher = this.getValueDelimiterMatcher();
        final boolean substitutionInVariablesEnabled = this.isEnableSubstitutionInVariables();
        final boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd) {
            final int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                ++pos;
            }
            else if (pos > offset && chars[pos - 1] == escape) {
                buf.deleteCharAt(pos - 1);
                chars = buf.buffer;
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
                    if (substitutionInVariablesEnabled && (endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
                        ++nestedVarCount;
                        pos += endMatchLen;
                    }
                    else {
                        endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
                        if (endMatchLen == 0) {
                            ++pos;
                        }
                        else {
                            if (nestedVarCount == 0) {
                                String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                                if (substitutionInVariablesEnabled) {
                                    final StrBuilder bufName = new StrBuilder(varNameExpr);
                                    this.substitute(bufName, 0, bufName.length());
                                    varNameExpr = bufName.toString();
                                }
                                final int endPos;
                                pos = (endPos = pos + endMatchLen);
                                String varName = varNameExpr;
                                String varDefaultValue = null;
                                if (valueDelimMatcher != null) {
                                    final char[] varNameExprChars = varNameExpr.toCharArray();
                                    int valueDelimiterMatchLen = 0;
                                    for (int i = 0; i < varNameExprChars.length; ++i) {
                                        if (!substitutionInVariablesEnabled && pfxMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
                                            break;
                                        }
                                        if ((valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i)) != 0) {
                                            varName = varNameExpr.substring(0, i);
                                            varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                                            break;
                                        }
                                    }
                                }
                                if (priorVariables == null) {
                                    priorVariables = new ArrayList<String>();
                                    priorVariables.add(new String(chars, offset, length));
                                }
                                this.checkCyclicSubstitution(varName, priorVariables);
                                priorVariables.add(varName);
                                String varValue = this.resolveVariable(varName, buf, startPos, endPos);
                                if (varValue == null) {
                                    varValue = varDefaultValue;
                                }
                                if (varValue != null) {
                                    final int varLen = varValue.length();
                                    buf.replace(startPos, endPos, varValue);
                                    altered = true;
                                    int change = this.substitute(buf, startPos, varLen, priorVariables);
                                    change = change + varLen - (endPos - startPos);
                                    pos += change;
                                    bufEnd += change;
                                    lengthChange += change;
                                    chars = buf.buffer;
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
        final StrBuilder buf = new StrBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        buf.appendWithSeparators(priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }
    
    protected String resolveVariable(final String variableName, final StrBuilder buf, final int startPos, final int endPos) {
        final StrLookup<?> resolver = this.getVariableResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.lookup(variableName);
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
    
    public StrMatcher getValueDelimiterMatcher() {
        return this.valueDelimiterMatcher;
    }
    
    public StrSubstitutor setValueDelimiterMatcher(final StrMatcher valueDelimiterMatcher) {
        this.valueDelimiterMatcher = valueDelimiterMatcher;
        return this;
    }
    
    public StrSubstitutor setValueDelimiter(final char valueDelimiter) {
        return this.setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
    }
    
    public StrSubstitutor setValueDelimiter(final String valueDelimiter) {
        if (StringUtils.isEmpty(valueDelimiter)) {
            this.setValueDelimiterMatcher(null);
            return this;
        }
        return this.setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
    }
    
    public StrLookup<?> getVariableResolver() {
        return this.variableResolver;
    }
    
    public void setVariableResolver(final StrLookup<?> variableResolver) {
        this.variableResolver = variableResolver;
    }
    
    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVariables;
    }
    
    public void setEnableSubstitutionInVariables(final boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
    }
    
    static {
        DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
        DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
        DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
    }
}
