// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.stream;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.JsonReaderInternalAccess;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Closeable;

public class JsonReader implements Closeable
{
    private static final char[] NON_EXECUTE_PREFIX;
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private final Reader in;
    private boolean lenient;
    private final char[] buffer;
    private int pos;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int[] stack;
    private int stackSize;
    
    public JsonReader(final Reader in) {
        this.lenient = false;
        this.buffer = new char[1024];
        this.pos = 0;
        this.limit = 0;
        this.lineNumber = 0;
        this.lineStart = 0;
        this.peeked = 0;
        this.stack = new int[32];
        this.stackSize = 0;
        this.stack[this.stackSize++] = 6;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public final boolean isLenient() {
        return this.lenient;
    }
    
    public void beginArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 3) {
            this.push(1);
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void endArray() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 4) {
            --this.stackSize;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void beginObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 1) {
            this.push(3);
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void endObject() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 2) {
            --this.stackSize;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public boolean hasNext() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        return p != 2 && p != 4;
    }
    
    public JsonToken peek() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        switch (p) {
            case 1: {
                return JsonToken.BEGIN_OBJECT;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 12:
            case 13:
            case 14: {
                return JsonToken.NAME;
            }
            case 5:
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 8:
            case 9:
            case 10:
            case 11: {
                return JsonToken.STRING;
            }
            case 15:
            case 16: {
                return JsonToken.NUMBER;
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    private int doPeek() throws IOException {
        final int peekStack = this.stack[this.stackSize - 1];
        if (peekStack == 1) {
            this.stack[this.stackSize - 1] = 2;
        }
        else if (peekStack == 2) {
            final int c = this.nextNonWhitespace(true);
            switch (c) {
                case 93: {
                    return this.peeked = 4;
                }
                case 59: {
                    this.checkLenient();
                }
                case 44: {
                    break;
                }
                default: {
                    throw this.syntaxError("Unterminated array");
                }
            }
        }
        else if (peekStack == 3 || peekStack == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (peekStack == 5) {
                final int c = this.nextNonWhitespace(true);
                switch (c) {
                    case 125: {
                        return this.peeked = 2;
                    }
                    case 59: {
                        this.checkLenient();
                    }
                    case 44: {
                        break;
                    }
                    default: {
                        throw this.syntaxError("Unterminated object");
                    }
                }
            }
            final int c = this.nextNonWhitespace(true);
            switch (c) {
                case 34: {
                    return this.peeked = 13;
                }
                case 39: {
                    this.checkLenient();
                    return this.peeked = 12;
                }
                case 125: {
                    if (peekStack != 5) {
                        return this.peeked = 2;
                    }
                    throw this.syntaxError("Expected name");
                }
                default: {
                    this.checkLenient();
                    --this.pos;
                    if (this.isLiteral((char)c)) {
                        return this.peeked = 14;
                    }
                    throw this.syntaxError("Expected name");
                }
            }
        }
        else if (peekStack == 4) {
            this.stack[this.stackSize - 1] = 5;
            final int c = this.nextNonWhitespace(true);
            switch (c) {
                case 58: {
                    break;
                }
                case 61: {
                    this.checkLenient();
                    if ((this.pos < this.limit || this.fillBuffer(1)) && this.buffer[this.pos] == '>') {
                        ++this.pos;
                        break;
                    }
                    break;
                }
                default: {
                    throw this.syntaxError("Expected ':'");
                }
            }
        }
        else if (peekStack == 6) {
            if (this.lenient) {
                this.consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        }
        else if (peekStack == 7) {
            final int c = this.nextNonWhitespace(false);
            if (c == -1) {
                return this.peeked = 17;
            }
            this.checkLenient();
            --this.pos;
        }
        else if (peekStack == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
        final int c = this.nextNonWhitespace(true);
        switch (c) {
            case 93: {
                if (peekStack == 1) {
                    return this.peeked = 4;
                }
            }
            case 44:
            case 59: {
                if (peekStack == 1 || peekStack == 2) {
                    this.checkLenient();
                    --this.pos;
                    return this.peeked = 7;
                }
                throw this.syntaxError("Unexpected value");
            }
            case 39: {
                this.checkLenient();
                return this.peeked = 8;
            }
            case 34: {
                if (this.stackSize == 1) {
                    this.checkLenient();
                }
                return this.peeked = 9;
            }
            case 91: {
                return this.peeked = 3;
            }
            case 123: {
                return this.peeked = 1;
            }
            default: {
                --this.pos;
                if (this.stackSize == 1) {
                    this.checkLenient();
                }
                int result = this.peekKeyword();
                if (result != 0) {
                    return result;
                }
                result = this.peekNumber();
                if (result != 0) {
                    return result;
                }
                if (!this.isLiteral(this.buffer[this.pos])) {
                    throw this.syntaxError("Expected value");
                }
                this.checkLenient();
                return this.peeked = 10;
            }
        }
    }
    
    private int peekKeyword() throws IOException {
        char c = this.buffer[this.pos];
        String keyword;
        String keywordUpper;
        int peeking;
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = 5;
        }
        else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = 6;
        }
        else {
            if (c != 'n' && c != 'N') {
                return 0;
            }
            keyword = "null";
            keywordUpper = "NULL";
            peeking = 7;
        }
        final int length = keyword.length();
        for (int i = 1; i < length; ++i) {
            if (this.pos + i >= this.limit && !this.fillBuffer(i + 1)) {
                return 0;
            }
            c = this.buffer[this.pos + i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || this.fillBuffer(length + 1)) && this.isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        return this.peeked = peeking;
    }
    
    private int peekNumber() throws IOException {
        final char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        long value = 0L;
        boolean negative = false;
        boolean fitsInLong = true;
        int last = 0;
        int i = 0;
    Label_0372:
        while (true) {
            if (p + i == l) {
                if (i == buffer.length) {
                    return 0;
                }
                if (!this.fillBuffer(i + 1)) {
                    break;
                }
                p = this.pos;
                l = this.limit;
            }
            final char c = buffer[p + i];
            switch (c) {
                case '-': {
                    if (last == 0) {
                        negative = true;
                        last = 1;
                        break;
                    }
                    if (last == 5) {
                        last = 6;
                        break;
                    }
                    return 0;
                }
                case '+': {
                    if (last == 5) {
                        last = 6;
                        break;
                    }
                    return 0;
                }
                case 'E':
                case 'e': {
                    if (last == 2 || last == 4) {
                        last = 5;
                        break;
                    }
                    return 0;
                }
                case '.': {
                    if (last == 2) {
                        last = 3;
                        break;
                    }
                    return 0;
                }
                default: {
                    if (c < '0' || c > '9') {
                        if (!this.isLiteral(c)) {
                            break Label_0372;
                        }
                        return 0;
                    }
                    else {
                        if (last == 1 || last == 0) {
                            value = -(c - '0');
                            last = 2;
                            break;
                        }
                        if (last == 2) {
                            if (value == 0L) {
                                return 0;
                            }
                            final long newValue = value * 10L - (c - '0');
                            fitsInLong &= (value > -922337203685477580L || (value == -922337203685477580L && newValue < value));
                            value = newValue;
                            break;
                        }
                        else {
                            if (last == 3) {
                                last = 4;
                                break;
                            }
                            if (last == 5 || last == 6) {
                                last = 7;
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            ++i;
        }
        if (last == 2 && fitsInLong && (value != Long.MIN_VALUE || negative)) {
            this.peekedLong = (negative ? value : (-value));
            this.pos += i;
            return this.peeked = 15;
        }
        if (last == 2 || last == 4 || last == 7) {
            this.peekedNumberLength = i;
            return this.peeked = 16;
        }
        return 0;
    }
    
    private boolean isLiteral(final char c) throws IOException {
        switch (c) {
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\': {
                this.checkLenient();
            }
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}': {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public String nextName() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        String result;
        if (p == 14) {
            result = this.nextUnquotedValue();
        }
        else if (p == 12) {
            result = this.nextQuotedValue('\'');
        }
        else {
            if (p != 13) {
                throw new IllegalStateException("Expected a name but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            result = this.nextQuotedValue('\"');
        }
        this.peeked = 0;
        return result;
    }
    
    public String nextString() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        String result;
        if (p == 10) {
            result = this.nextUnquotedValue();
        }
        else if (p == 8) {
            result = this.nextQuotedValue('\'');
        }
        else if (p == 9) {
            result = this.nextQuotedValue('\"');
        }
        else if (p == 11) {
            result = this.peekedString;
            this.peekedString = null;
        }
        else if (p == 15) {
            result = Long.toString(this.peekedLong);
        }
        else {
            if (p != 16) {
                throw new IllegalStateException("Expected a string but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            result = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        this.peeked = 0;
        return result;
    }
    
    public boolean nextBoolean() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 5) {
            this.peeked = 0;
            return true;
        }
        if (p == 6) {
            this.peeked = 0;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void nextNull() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 7) {
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected null but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public double nextDouble() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            return (double)this.peekedLong;
        }
        if (p == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        else if (p == 8 || p == 9) {
            this.peekedString = this.nextQuotedValue((p == 8) ? '\'' : '\"');
        }
        else if (p == 10) {
            this.peekedString = this.nextUnquotedValue();
        }
        else if (p != 11) {
            throw new IllegalStateException("Expected a double but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peeked = 11;
        final double result = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = 0;
        return result;
    }
    
    public long nextLong() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 15) {
            this.peeked = 0;
            return this.peekedLong;
        }
        Label_0181: {
            if (p != 16) {
                if (p == 8 || p == 9) {
                    this.peekedString = this.nextQuotedValue((p == 8) ? '\'' : '\"');
                    try {
                        final long result = Long.parseLong(this.peekedString);
                        this.peeked = 0;
                        return result;
                    }
                    catch (NumberFormatException ignored) {
                        break Label_0181;
                    }
                }
                throw new IllegalStateException("Expected a long but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        this.peeked = 11;
        final double asDouble = Double.parseDouble(this.peekedString);
        final long result2 = (long)asDouble;
        if (result2 != asDouble) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = 0;
        return result2;
    }
    
    private String nextQuotedValue(final char quote) throws IOException {
        final char[] buffer = this.buffer;
        final StringBuilder builder = new StringBuilder();
        while (true) {
            int p = this.pos;
            int l = this.limit;
            int start = p;
            while (p < l) {
                final int c = buffer[p++];
                if (c == quote) {
                    builder.append(buffer, start, (this.pos = p) - start - 1);
                    return builder.toString();
                }
                if (c == 92) {
                    builder.append(buffer, start, (this.pos = p) - start - 1);
                    builder.append(this.readEscapeCharacter());
                    p = this.pos;
                    l = this.limit;
                    start = p;
                }
                else {
                    if (c != 10) {
                        continue;
                    }
                    ++this.lineNumber;
                    this.lineStart = p;
                }
            }
            builder.append(buffer, start, p - start);
            this.pos = p;
            if (!this.fillBuffer(1)) {
                throw this.syntaxError("Unterminated string");
            }
        }
    }
    
    private String nextUnquotedValue() throws IOException {
        StringBuilder builder = null;
        int i = 0;
        Label_0172: {
        Label_0168:
            while (true) {
                if (this.pos + i < this.limit) {
                    switch (this.buffer[this.pos + i]) {
                        case '#':
                        case '/':
                        case ';':
                        case '=':
                        case '\\': {
                            break Label_0168;
                        }
                        case '\t':
                        case '\n':
                        case '\f':
                        case '\r':
                        case ' ':
                        case ',':
                        case ':':
                        case '[':
                        case ']':
                        case '{':
                        case '}': {
                            break Label_0172;
                        }
                        default: {
                            ++i;
                            continue;
                        }
                    }
                }
                else if (i < this.buffer.length) {
                    if (this.fillBuffer(i + 1)) {
                        continue;
                    }
                    break Label_0172;
                }
                else {
                    if (builder == null) {
                        builder = new StringBuilder();
                    }
                    builder.append(this.buffer, this.pos, i);
                    this.pos += i;
                    i = 0;
                    if (!this.fillBuffer(1)) {
                        break Label_0172;
                    }
                    continue;
                }
            }
            this.checkLenient();
        }
        String result;
        if (builder == null) {
            result = new String(this.buffer, this.pos, i);
        }
        else {
            builder.append(this.buffer, this.pos, i);
            result = builder.toString();
        }
        this.pos += i;
        return result;
    }
    
    private void skipQuotedValue(final char quote) throws IOException {
        final char[] buffer = this.buffer;
        do {
            int p = this.pos;
            int l = this.limit;
            while (p < l) {
                final int c = buffer[p++];
                if (c == quote) {
                    this.pos = p;
                    return;
                }
                if (c == 92) {
                    this.pos = p;
                    this.readEscapeCharacter();
                    p = this.pos;
                    l = this.limit;
                }
                else {
                    if (c != 10) {
                        continue;
                    }
                    ++this.lineNumber;
                    this.lineStart = p;
                }
            }
            this.pos = p;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }
    
    private void skipUnquotedValue() throws IOException {
        do {
            int i = 0;
            while (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}': {
                        this.pos += i;
                        return;
                    }
                    default: {
                        ++i;
                        continue;
                    }
                }
            }
            this.pos += i;
        } while (this.fillBuffer(1));
    }
    
    public int nextInt() throws IOException {
        int p = this.peeked;
        if (p == 0) {
            p = this.doPeek();
        }
        if (p == 15) {
            final int result = (int)this.peekedLong;
            if (this.peekedLong != result) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            this.peeked = 0;
            return result;
        }
        else {
            Label_0248: {
                if (p != 16) {
                    if (p == 8 || p == 9) {
                        this.peekedString = this.nextQuotedValue((p == 8) ? '\'' : '\"');
                        try {
                            final int result = Integer.parseInt(this.peekedString);
                            this.peeked = 0;
                            return result;
                        }
                        catch (NumberFormatException ignored) {
                            break Label_0248;
                        }
                    }
                    throw new IllegalStateException("Expected an int but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 11;
            final double asDouble = Double.parseDouble(this.peekedString);
            final int result = (int)asDouble;
            if (result != asDouble) {
                throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            this.peekedString = null;
            this.peeked = 0;
            return result;
        }
    }
    
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }
    
    public void skipValue() throws IOException {
        int count = 0;
        do {
            int p = this.peeked;
            if (p == 0) {
                p = this.doPeek();
            }
            if (p == 3) {
                this.push(1);
                ++count;
            }
            else if (p == 1) {
                this.push(3);
                ++count;
            }
            else if (p == 4) {
                --this.stackSize;
                --count;
            }
            else if (p == 2) {
                --this.stackSize;
                --count;
            }
            else if (p == 14 || p == 10) {
                this.skipUnquotedValue();
            }
            else if (p == 8 || p == 12) {
                this.skipQuotedValue('\'');
            }
            else if (p == 9 || p == 13) {
                this.skipQuotedValue('\"');
            }
            else if (p == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (count != 0);
    }
    
    private void push(final int newTop) {
        if (this.stackSize == this.stack.length) {
            final int[] newStack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
            this.stack = newStack;
        }
        this.stack[this.stackSize++] = newTop;
    }
    
    private boolean fillBuffer(int minimum) throws IOException {
        final char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
        }
        else {
            this.limit = 0;
        }
        this.pos = 0;
        int total;
        while ((total = this.in.read(buffer, this.limit, buffer.length - this.limit)) != -1) {
            this.limit += total;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == '\ufeff') {
                ++this.pos;
                ++this.lineStart;
                ++minimum;
            }
            if (this.limit >= minimum) {
                return true;
            }
        }
        return false;
    }
    
    private int getLineNumber() {
        return this.lineNumber + 1;
    }
    
    private int getColumnNumber() {
        return this.pos - this.lineStart + 1;
    }
    
    private int nextNonWhitespace(final boolean throwOnEof) throws IOException {
        final char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        while (true) {
            if (p == l) {
                this.pos = p;
                if (!this.fillBuffer(1)) {
                    if (throwOnEof) {
                        throw new EOFException("End of input at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                    }
                    return -1;
                }
                else {
                    p = this.pos;
                    l = this.limit;
                }
            }
            final int c = buffer[p++];
            if (c == 10) {
                ++this.lineNumber;
                this.lineStart = p;
            }
            else {
                if (c == 32 || c == 13) {
                    continue;
                }
                if (c == 9) {
                    continue;
                }
                if (c == 47) {
                    if ((this.pos = p) == l) {
                        --this.pos;
                        final boolean charsLoaded = this.fillBuffer(2);
                        ++this.pos;
                        if (!charsLoaded) {
                            return c;
                        }
                    }
                    this.checkLenient();
                    final char peek = buffer[this.pos];
                    switch (peek) {
                        case '*': {
                            ++this.pos;
                            if (!this.skipTo("*/")) {
                                throw this.syntaxError("Unterminated comment");
                            }
                            p = this.pos + 2;
                            l = this.limit;
                            continue;
                        }
                        case '/': {
                            ++this.pos;
                            this.skipToEndOfLine();
                            p = this.pos;
                            l = this.limit;
                            continue;
                        }
                        default: {
                            return c;
                        }
                    }
                }
                else {
                    if (c != 35) {
                        this.pos = p;
                        return c;
                    }
                    this.pos = p;
                    this.checkLenient();
                    this.skipToEndOfLine();
                    p = this.pos;
                    l = this.limit;
                }
            }
        }
    }
    
    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }
    
    private void skipToEndOfLine() throws IOException {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            final char c = this.buffer[this.pos++];
            if (c == '\n') {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
            if (c == '\r') {
                break;
            }
        }
    }
    
    private boolean skipTo(final String toFind) throws IOException {
        while (this.pos + toFind.length() <= this.limit || this.fillBuffer(toFind.length())) {
            Label_0104: {
                if (this.buffer[this.pos] != '\n') {
                    for (int c = 0; c < toFind.length(); ++c) {
                        if (this.buffer[this.pos + c] != toFind.charAt(c)) {
                            break Label_0104;
                        }
                    }
                    return true;
                }
                ++this.lineNumber;
                this.lineStart = this.pos + 1;
            }
            ++this.pos;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
    }
    
    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        final char escaped = this.buffer[this.pos++];
        switch (escaped) {
            case 'u': {
                if (this.pos + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                char result = '\0';
                for (int i = this.pos, end = i + 4; i < end; ++i) {
                    final char c = this.buffer[i];
                    result <<= 4;
                    if (c >= '0' && c <= '9') {
                        result += (char)(c - '0');
                    }
                    else if (c >= 'a' && c <= 'f') {
                        result += (char)(c - 'a' + 10);
                    }
                    else {
                        if (c < 'A' || c > 'F') {
                            throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                        }
                        result += (char)(c - 'A' + 10);
                    }
                }
                this.pos += 4;
                return result;
            }
            case 't': {
                return '\t';
            }
            case 'b': {
                return '\b';
            }
            case 'n': {
                return '\n';
            }
            case 'r': {
                return '\r';
            }
            case 'f': {
                return '\f';
            }
            case '\n': {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
        }
        return escaped;
    }
    
    private IOException syntaxError(final String message) throws IOException {
        throw new MalformedJsonException(message + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    private void consumeNonExecutePrefix() throws IOException {
        this.nextNonWhitespace(true);
        --this.pos;
        if (this.pos + JsonReader.NON_EXECUTE_PREFIX.length > this.limit && !this.fillBuffer(JsonReader.NON_EXECUTE_PREFIX.length)) {
            return;
        }
        for (int i = 0; i < JsonReader.NON_EXECUTE_PREFIX.length; ++i) {
            if (this.buffer[this.pos + i] != JsonReader.NON_EXECUTE_PREFIX[i]) {
                return;
            }
        }
        this.pos += JsonReader.NON_EXECUTE_PREFIX.length;
    }
    
    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
            @Override
            public void promoteNameToValue(final JsonReader reader) throws IOException {
                if (reader instanceof JsonTreeReader) {
                    ((JsonTreeReader)reader).promoteNameToValue();
                    return;
                }
                int p = reader.peeked;
                if (p == 0) {
                    p = reader.doPeek();
                }
                if (p == 13) {
                    reader.peeked = 9;
                }
                else if (p == 12) {
                    reader.peeked = 8;
                }
                else {
                    if (p != 14) {
                        throw new IllegalStateException("Expected a name but was " + reader.peek() + " " + " at line " + reader.getLineNumber() + " column " + reader.getColumnNumber());
                    }
                    reader.peeked = 10;
                }
            }
        };
    }
}
