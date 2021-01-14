package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter
        implements Closeable, Flushable {
    private static final String[] REPLACEMENT_CHARS = new String[''];
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;

    static {
        for (int i = 0; i <= 31; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[]{Integer.valueOf(i)});
        }
        REPLACEMENT_CHARS[34] = "\\\"";
        REPLACEMENT_CHARS[92] = "\\\\";
        REPLACEMENT_CHARS[9] = "\\t";
        REPLACEMENT_CHARS[8] = "\\b";
        REPLACEMENT_CHARS[10] = "\\n";
        REPLACEMENT_CHARS[13] = "\\r";
        REPLACEMENT_CHARS[12] = "\\f";
        HTML_SAFE_REPLACEMENT_CHARS = (String[]) REPLACEMENT_CHARS.clone();
        HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
        HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }

    private final Writer out;
    private int[] stack = new int[32];
    private int stackSize = 0;
    private String indent;
    private String separator;
    private boolean lenient;
    private boolean htmlSafe;
    private String deferredName;
    private boolean serializeNulls;

    public JsonWriter(Writer paramWriter) {
        push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (paramWriter == null) {
            throw new NullPointerException("out == null");
        }
        this.out = paramWriter;
    }

    public final void setIndent(String paramString) {
        if (paramString.length() == 0) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = paramString;
            this.separator = ": ";
        }
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public final void setLenient(boolean paramBoolean) {
        this.lenient = paramBoolean;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public final void setHtmlSafe(boolean paramBoolean) {
        this.htmlSafe = paramBoolean;
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public final void setSerializeNulls(boolean paramBoolean) {
        this.serializeNulls = paramBoolean;
    }

    public JsonWriter beginArray()
            throws IOException {
        writeDeferredName();
        return open(1, "[");
    }

    public JsonWriter endArray()
            throws IOException {
        return close(1, 2, "]");
    }

    public JsonWriter beginObject()
            throws IOException {
        writeDeferredName();
        return open(3, "{");
    }

    public JsonWriter endObject()
            throws IOException {
        return close(3, 5, "}");
    }

    private JsonWriter open(int paramInt, String paramString)
            throws IOException {
        beforeValue(true);
        push(paramInt);
        this.out.write(paramString);
        return this;
    }

    private JsonWriter close(int paramInt1, int paramInt2, String paramString)
            throws IOException {
        int i = peek();
        if ((i != paramInt2) && (i != paramInt1)) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        }
        this.stackSize -= 1;
        if (i == paramInt2) {
            newline();
        }
        this.out.write(paramString);
        return this;
    }

    private void push(int paramInt) {
        if (this.stackSize == this.stack.length) {
            int[] arrayOfInt = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, arrayOfInt, 0, this.stackSize);
            this.stack = arrayOfInt;
        }
        int tmp49_46 = this.stackSize;
        this.stackSize = (tmp49_46 | 0x1);
        this.stack[tmp49_46] = paramInt;
    }

    private int peek() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.stack[(this.stackSize - 1)];
    }

    private void replaceTop(int paramInt) {
        this.stack[(this.stackSize - 1)] = paramInt;
    }

    public JsonWriter name(String paramString)
            throws IOException {
        if (paramString == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.deferredName = paramString;
        return this;
    }

    private void writeDeferredName()
            throws IOException {
        if (this.deferredName != null) {
            beforeName();
            string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter value(String paramString)
            throws IOException {
        if (paramString == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue(false);
        string(paramString);
        return this;
    }

    public JsonWriter nullValue()
            throws IOException {
        if (this.deferredName != null) {
            if (this.serializeNulls) {
                writeDeferredName();
            } else {
                this.deferredName = null;
                return this;
            }
        }
        beforeValue(false);
        this.out.write("null");
        return this;
    }

    public JsonWriter value(boolean paramBoolean)
            throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(paramBoolean ? "true" : "false");
        return this;
    }

    public JsonWriter value(double paramDouble)
            throws IOException {
        if ((Double.isNaN(paramDouble)) || (Double.isInfinite(paramDouble))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + paramDouble);
        }
        writeDeferredName();
        beforeValue(false);
        this.out.append(Double.toString(paramDouble));
        return this;
    }

    public JsonWriter value(long paramLong)
            throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(Long.toString(paramLong));
        return this;
    }

    public JsonWriter value(Number paramNumber)
            throws IOException {
        if (paramNumber == null) {
            return nullValue();
        }
        writeDeferredName();
        String str = paramNumber.toString();
        if ((!this.lenient) && ((str.equals("-Infinity")) || (str.equals("Infinity")) || (str.equals("NaN")))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + paramNumber);
        }
        beforeValue(false);
        this.out.append(str);
        return this;
    }

    public void flush()
            throws IOException {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }

    public void close()
            throws IOException {
        this.out.close();
        int i = this.stackSize;
        if ((i > 1) || ((i == 1) && (this.stack[(i - 1)] != 7))) {
            throw new IOException("Incomplete document");
        }
        this.stackSize = 0;
    }

    private void string(String paramString)
            throws IOException {
        String[] arrayOfString = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
        this.out.write("\"");
        int i = 0;
        int j = paramString.length();
        for (int k = 0; k < j; k++) {
            int m = paramString.charAt(k);
            String str;
            if (m < 128) {
                str = arrayOfString[m];
                if (str == null) {
                    continue;
                }
            } else if (m == 8232) {
                str = "\\u2028";
            } else {
                if (m != 8233) {
                    continue;
                }
                str = "\\u2029";
            }
            if (i < k) {
                this.out.write(paramString, i, k - i);
            }
            this.out.write(str);
            i = k | 0x1;
        }
        if (i < j) {
            this.out.write(paramString, i, j - i);
        }
        this.out.write("\"");
    }

    private void newline()
            throws IOException {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        int i = 1;
        int j = this.stackSize;
        while (i < j) {
            this.out.write(this.indent);
            i++;
        }
    }

    private void beforeName()
            throws IOException {
        int i = peek();
        if (i == 5) {
            this.out.write(44);
        } else if (i != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        newline();
        replaceTop(4);
    }

    private void beforeValue(boolean paramBoolean)
            throws IOException {
        switch (peek()) {
            case 7:
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            case 6:
                if ((!this.lenient) && (!paramBoolean)) {
                    throw new IllegalStateException("JSON must start with an array or an object.");
                }
                replaceTop(7);
                break;
            case 1:
                replaceTop(2);
                newline();
                break;
            case 2:
                this.out.append(',');
                newline();
                break;
            case 4:
                this.out.append(this.separator);
                replaceTop(5);
                break;
        }
        throw new IllegalStateException("Nesting problem.");
    }
}




