// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

public interface ByteBufProcessor
{
    public static final ByteBufProcessor FIND_NUL = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value != 0;
        }
    };
    public static final ByteBufProcessor FIND_NON_NUL = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value == 0;
        }
    };
    public static final ByteBufProcessor FIND_CR = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value != 13;
        }
    };
    public static final ByteBufProcessor FIND_NON_CR = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value == 13;
        }
    };
    public static final ByteBufProcessor FIND_LF = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value != 10;
        }
    };
    public static final ByteBufProcessor FIND_NON_LF = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value == 10;
        }
    };
    public static final ByteBufProcessor FIND_CRLF = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value != 13 && value != 10;
        }
    };
    public static final ByteBufProcessor FIND_NON_CRLF = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value == 13 || value == 10;
        }
    };
    public static final ByteBufProcessor FIND_LINEAR_WHITESPACE = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value != 32 && value != 9;
        }
    };
    public static final ByteBufProcessor FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor() {
        @Override
        public boolean process(final byte value) throws Exception {
            return value == 32 || value == 9;
        }
    };
    
    boolean process(final byte p0) throws Exception;
}
