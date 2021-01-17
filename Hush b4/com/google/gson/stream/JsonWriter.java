// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.stream;

import java.io.IOException;
import java.io.Writer;
import java.io.Flushable;
import java.io.Closeable;

public class JsonWriter implements Closeable, Flushable
{
    private static final String[] REPLACEMENT_CHARS;
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private final Writer out;
    private int[] stack;
    private int stackSize;
    private String indent;
    private String separator;
    private boolean lenient;
    private boolean htmlSafe;
    private String deferredName;
    private boolean serializeNulls;
    
    public JsonWriter(final Writer out) {
        this.stack = new int[32];
        this.stackSize = 0;
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }
    
    public final void setIndent(final String indent) {
        if (indent.length() == 0) {
            this.indent = null;
            this.separator = ":";
        }
        else {
            this.indent = indent;
            this.separator = ": ";
        }
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public final void setHtmlSafe(final boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }
    
    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }
    
    public final void setSerializeNulls(final boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }
    
    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }
    
    public JsonWriter beginArray() throws IOException {
        this.writeDeferredName();
        return this.open(1, "[");
    }
    
    public JsonWriter endArray() throws IOException {
        return this.close(1, 2, "]");
    }
    
    public JsonWriter beginObject() throws IOException {
        this.writeDeferredName();
        return this.open(3, "{");
    }
    
    public JsonWriter endObject() throws IOException {
        return this.close(3, 5, "}");
    }
    
    private JsonWriter open(final int empty, final String openBracket) throws IOException {
        this.beforeValue(true);
        this.push(empty);
        this.out.write(openBracket);
        return this;
    }
    
    private JsonWriter close(final int empty, final int nonempty, final String closeBracket) throws IOException {
        final int context = this.peek();
        if (context != nonempty && context != empty) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        }
        --this.stackSize;
        if (context == nonempty) {
            this.newline();
        }
        this.out.write(closeBracket);
        return this;
    }
    
    private void push(final int newTop) {
        if (this.stackSize == this.stack.length) {
            final int[] newStack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
            this.stack = newStack;
        }
        this.stack[this.stackSize++] = newTop;
    }
    
    private int peek() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.stack[this.stackSize - 1];
    }
    
    private void replaceTop(final int topOfStack) {
        this.stack[this.stackSize - 1] = topOfStack;
    }
    
    public JsonWriter name(final String name) throws IOException {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.deferredName = name;
        return this;
    }
    
    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }
    
    public JsonWriter value(final String value) throws IOException {
        if (value == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue(false);
        this.string(value);
        return this;
    }
    
    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (!this.serializeNulls) {
                this.deferredName = null;
                return this;
            }
            this.writeDeferredName();
        }
        this.beforeValue(false);
        this.out.write("null");
        return this;
    }
    
    public JsonWriter value(final boolean value) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        this.out.write(value ? "true" : "false");
        return this;
    }
    
    public JsonWriter value(final double value) throws IOException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
        this.writeDeferredName();
        this.beforeValue(false);
        this.out.append((CharSequence)Double.toString(value));
        return this;
    }
    
    public JsonWriter value(final long value) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        this.out.write(Long.toString(value));
        return this;
    }
    
    public JsonWriter value(final Number value) throws IOException {
        if (value == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        final String string = value.toString();
        if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
        }
        this.beforeValue(false);
        this.out.append((CharSequence)string);
        return this;
    }
    
    public void flush() throws IOException {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }
    
    public void close() throws IOException {
        this.out.close();
        final int size = this.stackSize;
        if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
            throw new IOException("Incomplete document");
        }
        this.stackSize = 0;
    }
    
    private void string(final String value) throws IOException {
        final String[] replacements = this.htmlSafe ? JsonWriter.HTML_SAFE_REPLACEMENT_CHARS : JsonWriter.REPLACEMENT_CHARS;
        this.out.write("\"");
        int last = 0;
        final int length = value.length();
        for (int i = 0; i < length; ++i) {
            final char c = value.charAt(i);
            String replacement;
            if (c < '\u0080') {
                replacement = replacements[c];
                if (replacement == null) {
                    continue;
                }
            }
            else if (c == '\u2028') {
                replacement = "\\u2028";
            }
            else {
                if (c != '\u2029') {
                    continue;
                }
                replacement = "\\u2029";
            }
            if (last < i) {
                this.out.write(value, last, i - last);
            }
            this.out.write(replacement);
            last = i + 1;
        }
        if (last < length) {
            this.out.write(value, last, length - last);
        }
        this.out.write("\"");
    }
    
    private void newline() throws IOException {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        for (int i = 1, size = this.stackSize; i < size; ++i) {
            this.out.write(this.indent);
        }
    }
    
    private void beforeName() throws IOException {
        final int context = this.peek();
        if (context == 5) {
            this.out.write(44);
        }
        else if (context != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.newline();
        this.replaceTop(4);
    }
    
    private void beforeValue(final boolean root) throws IOException {
        switch (this.peek()) {
            case 7: {
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                if (!this.lenient && !root) {
                    throw new IllegalStateException("JSON must start with an array or an object.");
                }
                this.replaceTop(7);
                break;
            }
            case 1: {
                this.replaceTop(2);
                this.newline();
                break;
            }
            case 2: {
                this.out.append(',');
                this.newline();
                break;
            }
            case 4: {
                this.out.append((CharSequence)this.separator);
                this.replaceTop(5);
                break;
            }
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
        }
    }
    
    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        JsonWriter.REPLACEMENT_CHARS[34] = "\\\"";
        JsonWriter.REPLACEMENT_CHARS[92] = "\\\\";
        JsonWriter.REPLACEMENT_CHARS[9] = "\\t";
        JsonWriter.REPLACEMENT_CHARS[8] = "\\b";
        JsonWriter.REPLACEMENT_CHARS[10] = "\\n";
        JsonWriter.REPLACEMENT_CHARS[13] = "\\r";
        JsonWriter.REPLACEMENT_CHARS[12] = "\\f";
        (HTML_SAFE_REPLACEMENT_CHARS = JsonWriter.REPLACEMENT_CHARS.clone())[60] = "\\u003c";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }
}
