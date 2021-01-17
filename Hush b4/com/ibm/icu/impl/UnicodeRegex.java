// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.TreeMap;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import java.util.Map;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.text.ParsePosition;
import com.ibm.icu.text.UnicodeSet;
import java.util.Comparator;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.StringTransform;
import com.ibm.icu.util.Freezable;

public class UnicodeRegex implements Cloneable, Freezable<UnicodeRegex>, StringTransform
{
    private SymbolTable symbolTable;
    private static UnicodeRegex STANDARD;
    private String bnfCommentString;
    private String bnfVariableInfix;
    private String bnfLineSeparator;
    private Appendable log;
    private Comparator<Object> LongestFirst;
    
    public UnicodeRegex() {
        this.bnfCommentString = "#";
        this.bnfVariableInfix = "=";
        this.bnfLineSeparator = "\n";
        this.log = null;
        this.LongestFirst = new Comparator<Object>() {
            public int compare(final Object obj0, final Object obj1) {
                final String arg0 = obj0.toString();
                final String arg2 = obj1.toString();
                final int len0 = arg0.length();
                final int len2 = arg2.length();
                if (len0 != len2) {
                    return len2 - len0;
                }
                return arg0.compareTo(arg2);
            }
        };
    }
    
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    public UnicodeRegex setSymbolTable(final SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        return this;
    }
    
    public String transform(final String regex) {
        final StringBuilder result = new StringBuilder();
        final UnicodeSet temp = new UnicodeSet();
        final ParsePosition pos = new ParsePosition(0);
        int state = 0;
        for (int i = 0; i < regex.length(); ++i) {
            final char ch = regex.charAt(i);
            switch (state) {
                case 0: {
                    if (ch == '\\') {
                        if (UnicodeSet.resemblesPattern(regex, i)) {
                            i = this.processSet(regex, i, result, temp, pos);
                            continue;
                        }
                        state = 1;
                        break;
                    }
                    else {
                        if (ch == '[' && UnicodeSet.resemblesPattern(regex, i)) {
                            i = this.processSet(regex, i, result, temp, pos);
                            continue;
                        }
                        break;
                    }
                    break;
                }
                case 1: {
                    if (ch == 'Q') {
                        state = 1;
                        break;
                    }
                    state = 0;
                    break;
                }
                case 2: {
                    if (ch == '\\') {
                        state = 3;
                        break;
                    }
                    break;
                }
                case 3: {
                    if (ch == 'E') {
                        state = 0;
                    }
                    state = 2;
                    break;
                }
            }
            result.append(ch);
        }
        return result.toString();
    }
    
    public static String fix(final String regex) {
        return UnicodeRegex.STANDARD.transform(regex);
    }
    
    public static Pattern compile(final String regex) {
        return Pattern.compile(UnicodeRegex.STANDARD.transform(regex));
    }
    
    public static Pattern compile(final String regex, final int options) {
        return Pattern.compile(UnicodeRegex.STANDARD.transform(regex), options);
    }
    
    public String compileBnf(final String bnfLines) {
        return this.compileBnf(Arrays.asList(bnfLines.split("\\r\\n?|\\n")));
    }
    
    public String compileBnf(final List<String> lines) {
        final Map<String, String> variables = this.getVariables(lines);
        final Set<String> unused = new LinkedHashSet<String>(variables.keySet());
        for (int i = 0; i < 2; ++i) {
            for (final Map.Entry<String, String> entry : variables.entrySet()) {
                final String variable = entry.getKey();
                final String definition = entry.getValue();
                for (final Map.Entry<String, String> entry2 : variables.entrySet()) {
                    final String variable2 = entry2.getKey();
                    final String definition2 = entry2.getValue();
                    if (variable.equals(variable2)) {
                        continue;
                    }
                    final String altered2 = definition2.replace(variable, definition);
                    if (altered2.equals(definition2)) {
                        continue;
                    }
                    unused.remove(variable);
                    variables.put(variable2, altered2);
                    if (this.log == null) {
                        continue;
                    }
                    try {
                        this.log.append(variable2 + "=" + altered2 + ";");
                    }
                    catch (IOException e) {
                        throw (IllegalArgumentException)new IllegalArgumentException().initCause(e);
                    }
                }
            }
        }
        if (unused.size() != 1) {
            throw new IllegalArgumentException("Not a single root: " + unused);
        }
        return variables.get(unused.iterator().next());
    }
    
    public String getBnfCommentString() {
        return this.bnfCommentString;
    }
    
    public void setBnfCommentString(final String bnfCommentString) {
        this.bnfCommentString = bnfCommentString;
    }
    
    public String getBnfVariableInfix() {
        return this.bnfVariableInfix;
    }
    
    public void setBnfVariableInfix(final String bnfVariableInfix) {
        this.bnfVariableInfix = bnfVariableInfix;
    }
    
    public String getBnfLineSeparator() {
        return this.bnfLineSeparator;
    }
    
    public void setBnfLineSeparator(final String bnfLineSeparator) {
        this.bnfLineSeparator = bnfLineSeparator;
    }
    
    public static List<String> appendLines(final List<String> result, final String file, final String encoding) throws IOException {
        return appendLines(result, new FileInputStream(file), encoding);
    }
    
    public static List<String> appendLines(final List<String> result, final InputStream inputStream, final String encoding) throws UnsupportedEncodingException, IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, (encoding == null) ? "UTF-8" : encoding));
        while (true) {
            final String line = in.readLine();
            if (line == null) {
                break;
            }
            result.add(line);
        }
        return result;
    }
    
    public UnicodeRegex cloneAsThawed() {
        try {
            return (UnicodeRegex)this.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException();
        }
    }
    
    public UnicodeRegex freeze() {
        return this;
    }
    
    public boolean isFrozen() {
        return true;
    }
    
    private int processSet(final String regex, int i, final StringBuilder result, final UnicodeSet temp, final ParsePosition pos) {
        try {
            pos.setIndex(i);
            final UnicodeSet x = temp.clear().applyPattern(regex, pos, this.symbolTable, 0);
            x.complement().complement();
            result.append(x.toPattern(false));
            i = pos.getIndex() - 1;
            return i;
        }
        catch (Exception e) {
            throw (IllegalArgumentException)new IllegalArgumentException("Error in " + regex).initCause(e);
        }
    }
    
    private Map<String, String> getVariables(final List<String> lines) {
        final Map<String, String> variables = new TreeMap<String, String>(this.LongestFirst);
        String variable = null;
        final StringBuffer definition = new StringBuffer();
        int count = 0;
        for (String line : lines) {
            ++count;
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '\ufeff') {
                line = line.substring(1);
            }
            if (this.bnfCommentString != null) {
                final int hashPos = line.indexOf(this.bnfCommentString);
                if (hashPos >= 0) {
                    line = line.substring(0, hashPos);
                }
            }
            final String trimline = line.trim();
            if (trimline.length() == 0) {
                continue;
            }
            String linePart = line;
            if (linePart.trim().length() == 0) {
                continue;
            }
            final boolean terminated = trimline.endsWith(";");
            if (terminated) {
                linePart = linePart.substring(0, linePart.lastIndexOf(59));
            }
            final int equalsPos = linePart.indexOf(this.bnfVariableInfix);
            if (equalsPos >= 0) {
                if (variable != null) {
                    throw new IllegalArgumentException("Missing ';' before " + count + ") " + line);
                }
                variable = linePart.substring(0, equalsPos).trim();
                if (variables.containsKey(variable)) {
                    throw new IllegalArgumentException("Duplicate variable definition in " + line);
                }
                definition.append(linePart.substring(equalsPos + 1).trim());
            }
            else {
                if (variable == null) {
                    throw new IllegalArgumentException("Missing '=' at " + count + ") " + line);
                }
                definition.append(this.bnfLineSeparator).append(linePart);
            }
            if (!terminated) {
                continue;
            }
            variables.put(variable, definition.toString());
            variable = null;
            definition.setLength(0);
        }
        if (variable != null) {
            throw new IllegalArgumentException("Missing ';' at end");
        }
        return variables;
    }
    
    static {
        UnicodeRegex.STANDARD = new UnicodeRegex();
    }
}
