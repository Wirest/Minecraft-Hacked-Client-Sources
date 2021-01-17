// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.handler.codec.TooLongFrameException;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.buffer.ByteBufUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.handler.codec.ReplayingDecoder;

public abstract class HttpObjectDecoder extends ReplayingDecoder<State>
{
    private final int maxInitialLineLength;
    private final int maxHeaderSize;
    private final int maxChunkSize;
    private final boolean chunkedSupported;
    protected final boolean validateHeaders;
    private final AppendableCharSequence seq;
    private final HeaderParser headerParser;
    private final LineParser lineParser;
    private HttpMessage message;
    private long chunkSize;
    private int headerSize;
    private long contentLength;
    
    protected HttpObjectDecoder() {
        this(4096, 8192, 8192, true);
    }
    
    protected HttpObjectDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean chunkedSupported) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
    }
    
    protected HttpObjectDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxChunkSize, final boolean chunkedSupported, final boolean validateHeaders) {
        super(State.SKIP_CONTROL_CHARS);
        this.seq = new AppendableCharSequence(128);
        this.headerParser = new HeaderParser(this.seq);
        this.lineParser = new LineParser(this.seq);
        this.contentLength = Long.MIN_VALUE;
        if (maxInitialLineLength <= 0) {
            throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength);
        }
        if (maxHeaderSize <= 0) {
            throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + maxHeaderSize);
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.maxInitialLineLength = maxInitialLineLength;
        this.maxHeaderSize = maxHeaderSize;
        this.maxChunkSize = maxChunkSize;
        this.chunkedSupported = chunkedSupported;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> out) throws Exception {
        Label_0768: {
            switch (this.state()) {
                case SKIP_CONTROL_CHARS: {
                    try {
                        skipControlCharacters(buffer);
                        this.checkpoint(State.READ_INITIAL);
                    }
                    finally {
                        this.checkpoint();
                    }
                }
                case READ_INITIAL: {
                    try {
                        final String[] initialLine = splitInitialLine(this.lineParser.parse(buffer));
                        if (initialLine.length < 3) {
                            this.checkpoint(State.SKIP_CONTROL_CHARS);
                            return;
                        }
                        this.message = this.createMessage(initialLine);
                        this.checkpoint(State.READ_HEADER);
                    }
                    catch (Exception e) {
                        out.add(this.invalidMessage(e));
                    }
                }
                case READ_HEADER: {
                    try {
                        final State nextState = this.readHeaders(buffer);
                        this.checkpoint(nextState);
                        if (nextState == State.READ_CHUNK_SIZE) {
                            if (!this.chunkedSupported) {
                                throw new IllegalArgumentException("Chunked messages not supported");
                            }
                            out.add(this.message);
                        }
                        else {
                            if (nextState == State.SKIP_CONTROL_CHARS) {
                                out.add(this.message);
                                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                                this.reset();
                                return;
                            }
                            final long contentLength = this.contentLength();
                            if (contentLength == 0L || (contentLength == -1L && this.isDecodingRequest())) {
                                out.add(this.message);
                                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                                this.reset();
                                return;
                            }
                            assert nextState == State.READ_VARIABLE_LENGTH_CONTENT;
                            out.add(this.message);
                            if (nextState == State.READ_FIXED_LENGTH_CONTENT) {
                                this.chunkSize = contentLength;
                            }
                        }
                    }
                    catch (Exception e) {
                        out.add(this.invalidMessage(e));
                    }
                }
                case READ_VARIABLE_LENGTH_CONTENT: {
                    final int toRead = Math.min(this.actualReadableBytes(), this.maxChunkSize);
                    if (toRead > 0) {
                        final ByteBuf content = ByteBufUtil.readBytes(ctx.alloc(), buffer, toRead);
                        if (buffer.isReadable()) {
                            out.add(new DefaultHttpContent(content));
                        }
                        else {
                            out.add(new DefaultLastHttpContent(content, this.validateHeaders));
                            this.reset();
                        }
                    }
                    else if (!buffer.isReadable()) {
                        out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                        this.reset();
                    }
                }
                case READ_FIXED_LENGTH_CONTENT: {
                    final int readLimit = this.actualReadableBytes();
                    if (readLimit == 0) {
                        return;
                    }
                    int toRead2 = Math.min(readLimit, this.maxChunkSize);
                    if (toRead2 > this.chunkSize) {
                        toRead2 = (int)this.chunkSize;
                    }
                    final ByteBuf content2 = ByteBufUtil.readBytes(ctx.alloc(), buffer, toRead2);
                    this.chunkSize -= toRead2;
                    if (this.chunkSize == 0L) {
                        out.add(new DefaultLastHttpContent(content2, this.validateHeaders));
                        this.reset();
                    }
                    else {
                        out.add(new DefaultHttpContent(content2));
                    }
                }
                case READ_CHUNK_SIZE: {
                    try {
                        final AppendableCharSequence line = this.lineParser.parse(buffer);
                        final int chunkSize = getChunkSize(line.toString());
                        this.chunkSize = chunkSize;
                        if (chunkSize == 0) {
                            this.checkpoint(State.READ_CHUNK_FOOTER);
                            return;
                        }
                        this.checkpoint(State.READ_CHUNKED_CONTENT);
                    }
                    catch (Exception e) {
                        out.add(this.invalidChunk(e));
                    }
                }
                case READ_CHUNKED_CONTENT: {
                    assert this.chunkSize <= 2147483647L;
                    final int toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
                    final HttpContent chunk = new DefaultHttpContent(ByteBufUtil.readBytes(ctx.alloc(), buffer, toRead));
                    this.chunkSize -= toRead;
                    out.add(chunk);
                    if (this.chunkSize == 0L) {
                        this.checkpoint(State.READ_CHUNK_DELIMITER);
                        break Label_0768;
                    }
                }
                case READ_CHUNK_DELIMITER: {
                    while (true) {
                        final byte next = buffer.readByte();
                        if (next == 13) {
                            if (buffer.readByte() == 10) {
                                this.checkpoint(State.READ_CHUNK_SIZE);
                                return;
                            }
                            continue;
                        }
                        else {
                            if (next == 10) {
                                this.checkpoint(State.READ_CHUNK_SIZE);
                                return;
                            }
                            this.checkpoint();
                        }
                    }
                    break;
                }
                case READ_CHUNK_FOOTER: {
                    try {
                        final LastHttpContent trailer = this.readTrailingHeaders(buffer);
                        out.add(trailer);
                        this.reset();
                    }
                    catch (Exception e) {
                        out.add(this.invalidChunk(e));
                    }
                }
                case BAD_MESSAGE: {
                    buffer.skipBytes(this.actualReadableBytes());
                    break;
                }
                case UPGRADED: {
                    final int readableBytes = this.actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(buffer.readBytes(this.actualReadableBytes()));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        this.decode(ctx, in, out);
        if (this.message != null) {
            final boolean prematureClosure = this.isDecodingRequest() || this.contentLength() > 0L;
            this.reset();
            if (!prematureClosure) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
        }
    }
    
    protected boolean isContentAlwaysEmpty(final HttpMessage msg) {
        if (msg instanceof HttpResponse) {
            final HttpResponse res = (HttpResponse)msg;
            final int code = res.getStatus().code();
            if (code >= 100 && code < 200) {
                return code != 101 || res.headers().contains("Sec-WebSocket-Accept");
            }
            switch (code) {
                case 204:
                case 205:
                case 304: {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void reset() {
        final HttpMessage message = this.message;
        this.message = null;
        this.contentLength = Long.MIN_VALUE;
        if (!this.isDecodingRequest()) {
            final HttpResponse res = (HttpResponse)message;
            if (res != null && res.getStatus().code() == 101) {
                this.checkpoint(State.UPGRADED);
                return;
            }
        }
        this.checkpoint(State.SKIP_CONTROL_CHARS);
    }
    
    private HttpMessage invalidMessage(final Exception cause) {
        this.checkpoint(State.BAD_MESSAGE);
        if (this.message != null) {
            this.message.setDecoderResult(DecoderResult.failure(cause));
        }
        else {
            (this.message = this.createInvalidMessage()).setDecoderResult(DecoderResult.failure(cause));
        }
        final HttpMessage ret = this.message;
        this.message = null;
        return ret;
    }
    
    private HttpContent invalidChunk(final Exception cause) {
        this.checkpoint(State.BAD_MESSAGE);
        final HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        this.message = null;
        return chunk;
    }
    
    private static void skipControlCharacters(final ByteBuf buffer) {
        char c;
        do {
            c = (char)buffer.readUnsignedByte();
        } while (Character.isISOControl(c) || Character.isWhitespace(c));
        buffer.readerIndex(buffer.readerIndex() - 1);
    }
    
    private State readHeaders(final ByteBuf buffer) {
        this.headerSize = 0;
        final HttpMessage message = this.message;
        final HttpHeaders headers = message.headers();
        AppendableCharSequence line = this.headerParser.parse(buffer);
        String name = null;
        String value = null;
        if (line.length() > 0) {
            headers.clear();
            do {
                final char firstChar = line.charAt(0);
                if (name != null && (firstChar == ' ' || firstChar == '\t')) {
                    value = value + ' ' + line.toString().trim();
                }
                else {
                    if (name != null) {
                        headers.add(name, value);
                    }
                    final String[] header = splitHeader(line);
                    name = header[0];
                    value = header[1];
                }
                line = this.headerParser.parse(buffer);
            } while (line.length() > 0);
            if (name != null) {
                headers.add(name, value);
            }
        }
        State nextState;
        if (this.isContentAlwaysEmpty(message)) {
            HttpHeaders.removeTransferEncodingChunked(message);
            nextState = State.SKIP_CONTROL_CHARS;
        }
        else if (HttpHeaders.isTransferEncodingChunked(message)) {
            nextState = State.READ_CHUNK_SIZE;
        }
        else if (this.contentLength() >= 0L) {
            nextState = State.READ_FIXED_LENGTH_CONTENT;
        }
        else {
            nextState = State.READ_VARIABLE_LENGTH_CONTENT;
        }
        return nextState;
    }
    
    private long contentLength() {
        if (this.contentLength == Long.MIN_VALUE) {
            this.contentLength = HttpHeaders.getContentLength(this.message, -1L);
        }
        return this.contentLength;
    }
    
    private LastHttpContent readTrailingHeaders(final ByteBuf buffer) {
        this.headerSize = 0;
        AppendableCharSequence line = this.headerParser.parse(buffer);
        String lastHeader = null;
        if (line.length() > 0) {
            final LastHttpContent trailer = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
            do {
                final char firstChar = line.charAt(0);
                if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                    final List<String> current = trailer.trailingHeaders().getAll(lastHeader);
                    if (!current.isEmpty()) {
                        final int lastPos = current.size() - 1;
                        final String newString = current.get(lastPos) + line.toString().trim();
                        current.set(lastPos, newString);
                    }
                }
                else {
                    final String[] header = splitHeader(line);
                    final String name = header[0];
                    if (!HttpHeaders.equalsIgnoreCase(name, "Content-Length") && !HttpHeaders.equalsIgnoreCase(name, "Transfer-Encoding") && !HttpHeaders.equalsIgnoreCase(name, "Trailer")) {
                        trailer.trailingHeaders().add(name, header[1]);
                    }
                    lastHeader = name;
                }
                line = this.headerParser.parse(buffer);
            } while (line.length() > 0);
            return trailer;
        }
        return LastHttpContent.EMPTY_LAST_CONTENT;
    }
    
    protected abstract boolean isDecodingRequest();
    
    protected abstract HttpMessage createMessage(final String[] p0) throws Exception;
    
    protected abstract HttpMessage createInvalidMessage();
    
    private static int getChunkSize(String hex) {
        hex = hex.trim();
        for (int i = 0; i < hex.length(); ++i) {
            final char c = hex.charAt(i);
            if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
                hex = hex.substring(0, i);
                break;
            }
        }
        return Integer.parseInt(hex, 16);
    }
    
    private static String[] splitInitialLine(final AppendableCharSequence sb) {
        final int aStart = findNonWhitespace(sb, 0);
        final int aEnd = findWhitespace(sb, aStart);
        final int bStart = findNonWhitespace(sb, aEnd);
        final int bEnd = findWhitespace(sb, bStart);
        final int cStart = findNonWhitespace(sb, bEnd);
        final int cEnd = findEndOfString(sb);
        return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), (cStart < cEnd) ? sb.substring(cStart, cEnd) : "" };
    }
    
    private static String[] splitHeader(final AppendableCharSequence sb) {
        int length;
        int nameEnd;
        int nameStart;
        for (length = sb.length(), nameStart = (nameEnd = findNonWhitespace(sb, 0)); nameEnd < length; ++nameEnd) {
            final char ch = sb.charAt(nameEnd);
            if (ch == ':') {
                break;
            }
            if (Character.isWhitespace(ch)) {
                break;
            }
        }
        int colonEnd;
        for (colonEnd = nameEnd; colonEnd < length; ++colonEnd) {
            if (sb.charAt(colonEnd) == ':') {
                ++colonEnd;
                break;
            }
        }
        final int valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            return new String[] { sb.substring(nameStart, nameEnd), "" };
        }
        final int valueEnd = findEndOfString(sb);
        return new String[] { sb.substring(nameStart, nameEnd), sb.substring(valueStart, valueEnd) };
    }
    
    private static int findNonWhitespace(final CharSequence sb, final int offset) {
        int result;
        for (result = offset; result < sb.length() && Character.isWhitespace(sb.charAt(result)); ++result) {}
        return result;
    }
    
    private static int findWhitespace(final CharSequence sb, final int offset) {
        int result;
        for (result = offset; result < sb.length() && !Character.isWhitespace(sb.charAt(result)); ++result) {}
        return result;
    }
    
    private static int findEndOfString(final CharSequence sb) {
        int result;
        for (result = sb.length(); result > 0 && Character.isWhitespace(sb.charAt(result - 1)); --result) {}
        return result;
    }
    
    enum State
    {
        SKIP_CONTROL_CHARS, 
        READ_INITIAL, 
        READ_HEADER, 
        READ_VARIABLE_LENGTH_CONTENT, 
        READ_FIXED_LENGTH_CONTENT, 
        READ_CHUNK_SIZE, 
        READ_CHUNKED_CONTENT, 
        READ_CHUNK_DELIMITER, 
        READ_CHUNK_FOOTER, 
        BAD_MESSAGE, 
        UPGRADED;
    }
    
    private final class HeaderParser implements ByteBufProcessor
    {
        private final AppendableCharSequence seq;
        
        HeaderParser(final AppendableCharSequence seq) {
            this.seq = seq;
        }
        
        public AppendableCharSequence parse(final ByteBuf buffer) {
            this.seq.reset();
            HttpObjectDecoder.this.headerSize = 0;
            final int i = buffer.forEachByte(this);
            buffer.readerIndex(i + 1);
            return this.seq;
        }
        
        @Override
        public boolean process(final byte value) throws Exception {
            final char nextByte = (char)value;
            HttpObjectDecoder.this.headerSize++;
            if (nextByte == '\r') {
                return true;
            }
            if (nextByte == '\n') {
                return false;
            }
            if (HttpObjectDecoder.this.headerSize >= HttpObjectDecoder.this.maxHeaderSize) {
                throw new TooLongFrameException("HTTP header is larger than " + HttpObjectDecoder.this.maxHeaderSize + " bytes.");
            }
            this.seq.append(nextByte);
            return true;
        }
    }
    
    private final class LineParser implements ByteBufProcessor
    {
        private final AppendableCharSequence seq;
        private int size;
        
        LineParser(final AppendableCharSequence seq) {
            this.seq = seq;
        }
        
        public AppendableCharSequence parse(final ByteBuf buffer) {
            this.seq.reset();
            this.size = 0;
            final int i = buffer.forEachByte(this);
            buffer.readerIndex(i + 1);
            return this.seq;
        }
        
        @Override
        public boolean process(final byte value) throws Exception {
            final char nextByte = (char)value;
            if (nextByte == '\r') {
                return true;
            }
            if (nextByte == '\n') {
                return false;
            }
            if (this.size >= HttpObjectDecoder.this.maxInitialLineLength) {
                throw new TooLongFrameException("An HTTP line is larger than " + HttpObjectDecoder.this.maxInitialLineLength + " bytes.");
            }
            ++this.size;
            this.seq.append(nextByte);
            return true;
        }
    }
}
