// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;

public class XMLRecordWriter implements RecordWriter
{
    private Writer w;
    private List<String> nameStack;
    static final String NULL_NAME = "Null";
    private static final String INDENT = "    ";
    
    public XMLRecordWriter(final Writer w) {
        this.w = w;
        this.nameStack = new ArrayList<String>();
    }
    
    public boolean open(final String title) {
        this.newline();
        this.writeString("<" + title + ">");
        this.nameStack.add(title);
        return true;
    }
    
    public boolean close() {
        final int ix = this.nameStack.size() - 1;
        if (ix >= 0) {
            final String name = this.nameStack.remove(ix);
            this.newline();
            this.writeString("</" + name + ">");
            return true;
        }
        return false;
    }
    
    public void flush() {
        try {
            this.w.flush();
        }
        catch (IOException ex) {}
    }
    
    public void bool(final String name, final boolean value) {
        this.internalString(name, String.valueOf(value));
    }
    
    public void boolArray(final String name, final boolean[] values) {
        if (values != null) {
            final String[] stringValues = new String[values.length];
            for (int i = 0; i < values.length; ++i) {
                stringValues[i] = String.valueOf(values[i]);
            }
            this.stringArray(name, stringValues);
        }
    }
    
    private static String ctos(final char value) {
        if (value == '<') {
            return "&lt;";
        }
        if (value == '&') {
            return "&amp;";
        }
        return String.valueOf(value);
    }
    
    public void character(final String name, final char value) {
        if (value != '\uffff') {
            this.internalString(name, ctos(value));
        }
    }
    
    public void characterArray(final String name, final char[] values) {
        if (values != null) {
            final String[] stringValues = new String[values.length];
            for (int i = 0; i < values.length; ++i) {
                final char value = values[i];
                if (value == '\uffff') {
                    stringValues[i] = "Null";
                }
                else {
                    stringValues[i] = ctos(value);
                }
            }
            this.internalStringArray(name, stringValues);
        }
    }
    
    public void namedIndex(final String name, final String[] names, final int value) {
        if (value >= 0) {
            this.internalString(name, names[value]);
        }
    }
    
    public void namedIndexArray(final String name, final String[] names, final byte[] values) {
        if (values != null) {
            final String[] stringValues = new String[values.length];
            for (int i = 0; i < values.length; ++i) {
                final int value = values[i];
                if (value < 0) {
                    stringValues[i] = "Null";
                }
                else {
                    stringValues[i] = names[value];
                }
            }
            this.internalStringArray(name, stringValues);
        }
    }
    
    public static String normalize(final String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = null;
        boolean inWhitespace = false;
        char c = '\0';
        boolean special = false;
        for (int i = 0; i < str.length(); ++i) {
            c = str.charAt(i);
            if (UCharacter.isWhitespace(c)) {
                if (sb == null && (inWhitespace || c != ' ')) {
                    sb = new StringBuilder(str.substring(0, i));
                }
                if (inWhitespace) {
                    continue;
                }
                inWhitespace = true;
                special = false;
                c = ' ';
            }
            else {
                inWhitespace = false;
                special = (c == '<' || c == '&');
                if (special && sb == null) {
                    sb = new StringBuilder(str.substring(0, i));
                }
            }
            if (sb != null) {
                if (special) {
                    sb.append((c == '<') ? "&lt;" : "&amp;");
                }
                else {
                    sb.append(c);
                }
            }
        }
        if (sb != null) {
            return sb.toString();
        }
        return str;
    }
    
    private void internalString(final String name, final String normalizedValue) {
        if (normalizedValue != null) {
            this.newline();
            this.writeString("<" + name + ">" + normalizedValue + "</" + name + ">");
        }
    }
    
    private void internalStringArray(final String name, final String[] normalizedValues) {
        if (normalizedValues != null) {
            this.push(name + "List");
            for (int i = 0; i < normalizedValues.length; ++i) {
                String value = normalizedValues[i];
                if (value == null) {
                    value = "Null";
                }
                this.string(name, value);
            }
            this.pop();
        }
    }
    
    public void string(final String name, final String value) {
        this.internalString(name, normalize(value));
    }
    
    public void stringArray(final String name, final String[] values) {
        if (values != null) {
            this.push(name + "List");
            for (int i = 0; i < values.length; ++i) {
                String value = normalize(values[i]);
                if (value == null) {
                    value = "Null";
                }
                this.internalString(name, value);
            }
            this.pop();
        }
    }
    
    public void stringTable(final String name, final String[][] values) {
        if (values != null) {
            this.push(name + "Table");
            for (int i = 0; i < values.length; ++i) {
                final String[] rowValues = values[i];
                if (rowValues == null) {
                    this.internalString(name + "List", "Null");
                }
                else {
                    this.stringArray(name, rowValues);
                }
            }
            this.pop();
        }
    }
    
    private void push(final String name) {
        this.newline();
        this.writeString("<" + name + ">");
        this.nameStack.add(name);
    }
    
    private void pop() {
        final int ix = this.nameStack.size() - 1;
        final String name = this.nameStack.remove(ix);
        this.newline();
        this.writeString("</" + name + ">");
    }
    
    private void newline() {
        this.writeString("\n");
        for (int i = 0; i < this.nameStack.size(); ++i) {
            this.writeString("    ");
        }
    }
    
    private void writeString(final String str) {
        if (this.w != null) {
            try {
                this.w.write(str);
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                this.w = null;
            }
        }
    }
}
