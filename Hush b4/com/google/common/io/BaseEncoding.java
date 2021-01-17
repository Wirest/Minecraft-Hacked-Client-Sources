// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import javax.annotation.Nullable;
import com.google.common.base.Ascii;
import java.util.Arrays;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import javax.annotation.CheckReturnValue;
import com.google.common.base.CharMatcher;
import java.io.InputStream;
import java.io.Reader;
import com.google.common.annotations.GwtIncompatible;
import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public abstract class BaseEncoding
{
    private static final BaseEncoding BASE64;
    private static final BaseEncoding BASE64_URL;
    private static final BaseEncoding BASE32;
    private static final BaseEncoding BASE32_HEX;
    private static final BaseEncoding BASE16;
    
    BaseEncoding() {
    }
    
    public String encode(final byte[] bytes) {
        return this.encode(Preconditions.checkNotNull(bytes), 0, bytes.length);
    }
    
    public final String encode(final byte[] bytes, final int off, final int len) {
        Preconditions.checkNotNull(bytes);
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        final GwtWorkarounds.CharOutput result = GwtWorkarounds.stringBuilderOutput(this.maxEncodedSize(len));
        final GwtWorkarounds.ByteOutput byteOutput = this.encodingStream(result);
        try {
            for (int i = 0; i < len; ++i) {
                byteOutput.write(bytes[off + i]);
            }
            byteOutput.close();
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)"impossible");
        }
        return result.toString();
    }
    
    @GwtIncompatible("Writer,OutputStream")
    public final OutputStream encodingStream(final Writer writer) {
        return GwtWorkarounds.asOutputStream(this.encodingStream(GwtWorkarounds.asCharOutput(writer)));
    }
    
    @GwtIncompatible("ByteSink,CharSink")
    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }
    
    private static byte[] extract(final byte[] result, final int length) {
        if (length == result.length) {
            return result;
        }
        final byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }
    
    public final byte[] decode(final CharSequence chars) {
        try {
            return this.decodeChecked(chars);
        }
        catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }
    
    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        chars = this.padding().trimTrailingFrom(chars);
        final GwtWorkarounds.ByteInput decodedInput = this.decodingStream(GwtWorkarounds.asCharInput(chars));
        final byte[] tmp = new byte[this.maxDecodedSize(chars.length())];
        int index = 0;
        try {
            for (int i = decodedInput.read(); i != -1; i = decodedInput.read()) {
                tmp[index++] = (byte)i;
            }
        }
        catch (DecodingException badInput) {
            throw badInput;
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
        return extract(tmp, index);
    }
    
    @GwtIncompatible("Reader,InputStream")
    public final InputStream decodingStream(final Reader reader) {
        return GwtWorkarounds.asInputStream(this.decodingStream(GwtWorkarounds.asCharInput(reader)));
    }
    
    @GwtIncompatible("ByteSource,CharSource")
    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }
    
    abstract int maxEncodedSize(final int p0);
    
    abstract GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput p0);
    
    abstract int maxDecodedSize(final int p0);
    
    abstract GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput p0);
    
    abstract CharMatcher padding();
    
    @CheckReturnValue
    public abstract BaseEncoding omitPadding();
    
    @CheckReturnValue
    public abstract BaseEncoding withPadChar(final char p0);
    
    @CheckReturnValue
    public abstract BaseEncoding withSeparator(final String p0, final int p1);
    
    @CheckReturnValue
    public abstract BaseEncoding upperCase();
    
    @CheckReturnValue
    public abstract BaseEncoding lowerCase();
    
    public static BaseEncoding base64() {
        return BaseEncoding.BASE64;
    }
    
    public static BaseEncoding base64Url() {
        return BaseEncoding.BASE64_URL;
    }
    
    public static BaseEncoding base32() {
        return BaseEncoding.BASE32;
    }
    
    public static BaseEncoding base32Hex() {
        return BaseEncoding.BASE32_HEX;
    }
    
    public static BaseEncoding base16() {
        return BaseEncoding.BASE16;
    }
    
    static GwtWorkarounds.CharInput ignoringInput(final GwtWorkarounds.CharInput delegate, final CharMatcher toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new GwtWorkarounds.CharInput() {
            @Override
            public int read() throws IOException {
                int readChar;
                do {
                    readChar = delegate.read();
                } while (readChar != -1 && toIgnore.matches((char)readChar));
                return readChar;
            }
            
            @Override
            public void close() throws IOException {
                delegate.close();
            }
        };
    }
    
    static GwtWorkarounds.CharOutput separatingOutput(final GwtWorkarounds.CharOutput delegate, final String separator, final int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new GwtWorkarounds.CharOutput() {
            int charsUntilSeparator = afterEveryChars;
            
            @Override
            public void write(final char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    for (int i = 0; i < separator.length(); ++i) {
                        delegate.write(separator.charAt(i));
                    }
                    this.charsUntilSeparator = afterEveryChars;
                }
                delegate.write(c);
                --this.charsUntilSeparator;
            }
            
            @Override
            public void flush() throws IOException {
                delegate.flush();
            }
            
            @Override
            public void close() throws IOException {
                delegate.close();
            }
        };
    }
    
    static {
        BASE64 = new StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
        BASE64_URL = new StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
        BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
        BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
        BASE16 = new StandardBaseEncoding("base16()", "0123456789ABCDEF", null);
    }
    
    public static final class DecodingException extends IOException
    {
        DecodingException(final String message) {
            super(message);
        }
        
        DecodingException(final Throwable cause) {
            super(cause);
        }
    }
    
    private static final class Alphabet extends CharMatcher
    {
        private final String name;
        private final char[] chars;
        final int mask;
        final int bitsPerChar;
        final int charsPerChunk;
        final int bytesPerChunk;
        private final byte[] decodabet;
        private final boolean[] validPadding;
        
        Alphabet(final String name, final char[] chars) {
            this.name = Preconditions.checkNotNull(name);
            this.chars = Preconditions.checkNotNull(chars);
            try {
                this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
            }
            catch (ArithmeticException e) {
                throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
            }
            final int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
            this.charsPerChunk = 8 / gcd;
            this.bytesPerChunk = this.bitsPerChar / gcd;
            this.mask = chars.length - 1;
            final byte[] decodabet = new byte[128];
            Arrays.fill(decodabet, (byte)(-1));
            for (int i = 0; i < chars.length; ++i) {
                final char c = chars[i];
                Preconditions.checkArgument(CharMatcher.ASCII.matches(c), "Non-ASCII character: %s", c);
                Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", c);
                decodabet[c] = (byte)i;
            }
            this.decodabet = decodabet;
            final boolean[] validPadding = new boolean[this.charsPerChunk];
            for (int j = 0; j < this.bytesPerChunk; ++j) {
                validPadding[IntMath.divide(j * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
            }
            this.validPadding = validPadding;
        }
        
        char encode(final int bits) {
            return this.chars[bits];
        }
        
        boolean isValidPaddingStartPosition(final int index) {
            return this.validPadding[index % this.charsPerChunk];
        }
        
        int decode(final char ch) throws IOException {
            if (ch > '\u007f' || this.decodabet[ch] == -1) {
                throw new DecodingException("Unrecognized character: " + ch);
            }
            return this.decodabet[ch];
        }
        
        private boolean hasLowerCase() {
            for (final char c : this.chars) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean hasUpperCase() {
            for (final char c : this.chars) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }
        
        Alphabet upperCase() {
            if (!this.hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasUpperCase(), (Object)"Cannot call upperCase() on a mixed-case alphabet");
            final char[] upperCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; ++i) {
                upperCased[i] = Ascii.toUpperCase(this.chars[i]);
            }
            return new Alphabet(this.name + ".upperCase()", upperCased);
        }
        
        Alphabet lowerCase() {
            if (!this.hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasLowerCase(), (Object)"Cannot call lowerCase() on a mixed-case alphabet");
            final char[] lowerCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; ++i) {
                lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
            }
            return new Alphabet(this.name + ".lowerCase()", lowerCased);
        }
        
        @Override
        public boolean matches(final char c) {
            return CharMatcher.ASCII.matches(c) && this.decodabet[c] != -1;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    static final class StandardBaseEncoding extends BaseEncoding
    {
        private final Alphabet alphabet;
        @Nullable
        private final Character paddingChar;
        private transient BaseEncoding upperCase;
        private transient BaseEncoding lowerCase;
        
        StandardBaseEncoding(final String name, final String alphabetChars, @Nullable final Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }
        
        StandardBaseEncoding(final Alphabet alphabet, @Nullable final Character paddingChar) {
            this.alphabet = Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar), "Padding character %s was already in alphabet", paddingChar);
            this.paddingChar = paddingChar;
        }
        
        @Override
        CharMatcher padding() {
            return (this.paddingChar == null) ? CharMatcher.NONE : CharMatcher.is(this.paddingChar);
        }
        
        @Override
        int maxEncodedSize(final int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }
        
        @Override
        GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput out) {
            Preconditions.checkNotNull(out);
            return new GwtWorkarounds.ByteOutput() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;
                
                @Override
                public void write(final byte b) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer |= (b & 0xFF);
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        final int charIndex = this.bitBuffer >> this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }
                
                @Override
                public void flush() throws IOException {
                    out.flush();
                }
                
                @Override
                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        final int charIndex = this.bitBuffer << StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar);
                                ++this.writtenChars;
                            }
                        }
                    }
                    out.close();
                }
            };
        }
        
        @Override
        int maxDecodedSize(final int chars) {
            return (int)((this.alphabet.bitsPerChar * (long)chars + 7L) / 8L);
        }
        
        @Override
        GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput reader) {
            Preconditions.checkNotNull(reader);
            return new GwtWorkarounds.ByteInput() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();
                
                @Override
                public int read() throws IOException {
                    while (true) {
                        final int readChar = reader.read();
                        if (readChar == -1) {
                            if (!this.hitPadding && !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                                throw new DecodingException("Invalid input length " + this.readChars);
                            }
                            return -1;
                        }
                        else {
                            ++this.readChars;
                            final char ch = (char)readChar;
                            if (this.paddingMatcher.matches(ch)) {
                                if (!this.hitPadding && (this.readChars == 1 || !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                                    throw new DecodingException("Padding cannot start at index " + this.readChars);
                                }
                                this.hitPadding = true;
                            }
                            else {
                                if (this.hitPadding) {
                                    throw new DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
                                }
                                this.bitBuffer <<= StandardBaseEncoding.this.alphabet.bitsPerChar;
                                this.bitBuffer |= StandardBaseEncoding.this.alphabet.decode(ch);
                                this.bitBufferLength += StandardBaseEncoding.this.alphabet.bitsPerChar;
                                if (this.bitBufferLength >= 8) {
                                    this.bitBufferLength -= 8;
                                    return this.bitBuffer >> this.bitBufferLength & 0xFF;
                                }
                                continue;
                            }
                        }
                    }
                }
                
                @Override
                public void close() throws IOException {
                    reader.close();
                }
            };
        }
        
        @Override
        public BaseEncoding omitPadding() {
            return (this.paddingChar == null) ? this : new StandardBaseEncoding(this.alphabet, null);
        }
        
        @Override
        public BaseEncoding withPadChar(final char padChar) {
            if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar == padChar)) {
                return this;
            }
            return new StandardBaseEncoding(this.alphabet, padChar);
        }
        
        @Override
        public BaseEncoding withSeparator(final String separator, final int afterEveryChars) {
            Preconditions.checkNotNull(separator);
            Preconditions.checkArgument(this.padding().or(this.alphabet).matchesNoneOf(separator), (Object)"Separator cannot contain alphabet or padding characters");
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }
        
        @Override
        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                final Alphabet upper = this.alphabet.upperCase();
                BaseEncoding baseEncoding;
                StandardBaseEncoding upperCase;
                if (upper == this.alphabet) {
                    baseEncoding = this;
                    upperCase = this;
                }
                else {
                    baseEncoding = (upperCase = new StandardBaseEncoding(upper, this.paddingChar));
                }
                this.upperCase = upperCase;
                result = baseEncoding;
            }
            return result;
        }
        
        @Override
        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                final Alphabet lower = this.alphabet.lowerCase();
                BaseEncoding baseEncoding;
                StandardBaseEncoding lowerCase;
                if (lower == this.alphabet) {
                    baseEncoding = this;
                    lowerCase = this;
                }
                else {
                    baseEncoding = (lowerCase = new StandardBaseEncoding(lower, this.paddingChar));
                }
                this.lowerCase = lowerCase;
                result = baseEncoding;
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                }
                else {
                    builder.append(".withPadChar(").append(this.paddingChar).append(')');
                }
            }
            return builder.toString();
        }
    }
    
    static final class SeparatedBaseEncoding extends BaseEncoding
    {
        private final BaseEncoding delegate;
        private final String separator;
        private final int afterEveryChars;
        private final CharMatcher separatorChars;
        
        SeparatedBaseEncoding(final BaseEncoding delegate, final String separator, final int afterEveryChars) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.separator = Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
            this.separatorChars = CharMatcher.anyOf(separator).precomputed();
        }
        
        @Override
        CharMatcher padding() {
            return this.delegate.padding();
        }
        
        @Override
        int maxEncodedSize(final int bytes) {
            final int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return unseparatedSize + this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
        }
        
        @Override
        GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput output) {
            return this.delegate.encodingStream(BaseEncoding.separatingOutput(output, this.separator, this.afterEveryChars));
        }
        
        @Override
        int maxDecodedSize(final int chars) {
            return this.delegate.maxDecodedSize(chars);
        }
        
        @Override
        GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput input) {
            return this.delegate.decodingStream(BaseEncoding.ignoringInput(input, this.separatorChars));
        }
        
        @Override
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public BaseEncoding withPadChar(final char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public BaseEncoding withSeparator(final String separator, final int afterEveryChars) {
            throw new UnsupportedOperationException("Already have a separator");
        }
        
        @Override
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public String toString() {
            return this.delegate.toString() + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
        }
    }
}
