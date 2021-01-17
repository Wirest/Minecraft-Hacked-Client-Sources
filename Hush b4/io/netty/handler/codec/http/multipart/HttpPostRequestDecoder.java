// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.IOException;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.ArrayList;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import java.util.List;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpRequest;

public class HttpPostRequestDecoder
{
    private static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
    private final HttpDataFactory factory;
    private final HttpRequest request;
    private final Charset charset;
    private boolean bodyToDecode;
    private boolean isLastChunk;
    private final List<InterfaceHttpData> bodyListHttpData;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private ByteBuf undecodedChunk;
    private boolean isMultipart;
    private int bodyListHttpDataRank;
    private String multipartDataBoundary;
    private String multipartMixedBoundary;
    private MultiPartStatus currentStatus;
    private Map<String, Attribute> currentFieldAttributes;
    private FileUpload currentFileUpload;
    private Attribute currentAttribute;
    private boolean destroyed;
    private int discardThreshold;
    
    public HttpPostRequestDecoder(final HttpRequest request) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostRequestDecoder(final HttpDataFactory factory, final HttpRequest request) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostRequestDecoder(final HttpDataFactory factory, final HttpRequest request, final Charset charset) throws ErrorDataDecoderException, IncompatibleDataDecoderException {
        this.bodyListHttpData = new ArrayList<InterfaceHttpData>();
        this.bodyMapHttpData = new TreeMap<String, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
        this.currentStatus = MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.request = request;
        final HttpMethod method = request.getMethod();
        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.PATCH)) {
            this.bodyToDecode = true;
        }
        this.charset = charset;
        this.factory = factory;
        final String contentType = this.request.headers().get("Content-Type");
        if (contentType != null) {
            this.checkMultipart(contentType);
        }
        else {
            this.isMultipart = false;
        }
        if (!this.bodyToDecode) {
            throw new IncompatibleDataDecoderException("No Body to decode");
        }
        if (request instanceof HttpContent) {
            this.offer((HttpContent)request);
        }
        else {
            this.undecodedChunk = Unpooled.buffer();
            this.parseBody();
        }
    }
    
    private void checkMultipart(final String contentType) throws ErrorDataDecoderException {
        final String[] headerContentType = splitHeaderContentType(contentType);
        if (headerContentType[0].toLowerCase().startsWith("multipart/form-data") && headerContentType[1].toLowerCase().startsWith("boundary")) {
            final String[] boundary = StringUtil.split(headerContentType[1], '=');
            if (boundary.length != 2) {
                throw new ErrorDataDecoderException("Needs a boundary value");
            }
            if (boundary[1].charAt(0) == '\"') {
                final String bound = boundary[1].trim();
                final int index = bound.length() - 1;
                if (bound.charAt(index) == '\"') {
                    boundary[1] = bound.substring(1, index);
                }
            }
            this.multipartDataBoundary = "--" + boundary[1];
            this.isMultipart = true;
            this.currentStatus = MultiPartStatus.HEADERDELIMITER;
        }
        else {
            this.isMultipart = false;
        }
    }
    
    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }
    
    public boolean isMultipart() {
        this.checkDestroyed();
        return this.isMultipart;
    }
    
    public void setDiscardThreshold(final int discardThreshold) {
        if (discardThreshold < 0) {
            throw new IllegalArgumentException("discardThreshold must be >= 0");
        }
        this.discardThreshold = discardThreshold;
    }
    
    public int getDiscardThreshold() {
        return this.discardThreshold;
    }
    
    public List<InterfaceHttpData> getBodyHttpDatas() throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }
    
    public List<InterfaceHttpData> getBodyHttpDatas(final String name) throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        return this.bodyMapHttpData.get(name);
    }
    
    public InterfaceHttpData getBodyHttpData(final String name) throws NotEnoughDataDecoderException {
        this.checkDestroyed();
        if (!this.isLastChunk) {
            throw new NotEnoughDataDecoderException();
        }
        final List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }
    
    public HttpPostRequestDecoder offer(final HttpContent content) throws ErrorDataDecoderException {
        this.checkDestroyed();
        final ByteBuf buf = content.content();
        if (this.undecodedChunk == null) {
            this.undecodedChunk = buf.copy();
        }
        else {
            this.undecodedChunk.writeBytes(buf);
        }
        if (content instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        this.parseBody();
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            this.undecodedChunk.discardReadBytes();
        }
        return this;
    }
    
    public boolean hasNext() throws EndOfDataDecoderException {
        this.checkDestroyed();
        if (this.currentStatus == MultiPartStatus.EPILOGUE && this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
            throw new EndOfDataDecoderException();
        }
        return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
    }
    
    public InterfaceHttpData next() throws EndOfDataDecoderException {
        this.checkDestroyed();
        if (this.hasNext()) {
            return this.bodyListHttpData.get(this.bodyListHttpDataRank++);
        }
        return null;
    }
    
    private void parseBody() throws ErrorDataDecoderException {
        if (this.currentStatus == MultiPartStatus.PREEPILOGUE || this.currentStatus == MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = MultiPartStatus.EPILOGUE;
            }
            return;
        }
        if (this.isMultipart) {
            this.parseBodyMultipart();
        }
        else {
            this.parseBodyAttributes();
        }
    }
    
    protected void addHttpData(final InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = new ArrayList<InterfaceHttpData>(1);
            this.bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        this.bodyListHttpData.add(data);
    }
    
    private void parseBodyAttributesStandard() throws ErrorDataDecoderException {
        int currentpos;
        int firstpos = currentpos = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.NOTSTARTED) {
            this.currentStatus = MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        try {
            while (this.undecodedChunk.isReadable() && contRead) {
                char read = (char)this.undecodedChunk.readUnsignedByte();
                ++currentpos;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        if (read == '=') {
                            this.currentStatus = MultiPartStatus.FIELD;
                            final int equalpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, key);
                            firstpos = currentpos;
                            continue;
                        }
                        if (read == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
                            (this.currentAttribute = this.factory.createAttribute(this.request, key)).setValue("");
                            this.addHttpData(this.currentAttribute);
                            this.currentAttribute = null;
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        continue;
                    }
                    case FIELD: {
                        if (read == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        if (read == '\r') {
                            if (!this.undecodedChunk.isReadable()) {
                                --currentpos;
                                continue;
                            }
                            read = (char)this.undecodedChunk.readUnsignedByte();
                            ++currentpos;
                            if (read == '\n') {
                                this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 2;
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                continue;
                            }
                            throw new ErrorDataDecoderException("Bad end of line");
                        }
                        else {
                            if (read == '\n') {
                                this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 1;
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                continue;
                            }
                            continue;
                        }
                        break;
                    }
                    default: {
                        contRead = false;
                        continue;
                    }
                }
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                final int ampersandpos = currentpos;
                if (ampersandpos > firstpos) {
                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                }
                else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                firstpos = currentpos;
                this.currentStatus = MultiPartStatus.EPILOGUE;
                this.undecodedChunk.readerIndex(firstpos);
                return;
            }
            if (contRead && this.currentAttribute != null) {
                if (this.currentStatus == MultiPartStatus.FIELD) {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
                    firstpos = currentpos;
                }
                this.undecodedChunk.readerIndex(firstpos);
            }
            else {
                this.undecodedChunk.readerIndex(firstpos);
            }
        }
        catch (ErrorDataDecoderException e) {
            this.undecodedChunk.readerIndex(firstpos);
            throw e;
        }
        catch (IOException e2) {
            this.undecodedChunk.readerIndex(firstpos);
            throw new ErrorDataDecoderException(e2);
        }
    }
    
    private void parseBodyAttributes() throws ErrorDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e3) {
            this.parseBodyAttributesStandard();
            return;
        }
        int currentpos;
        int firstpos = currentpos = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.NOTSTARTED) {
            this.currentStatus = MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        try {
        Label_0512:
            while (sao.pos < sao.limit) {
                char read = (char)(sao.bytes[sao.pos++] & 0xFF);
                ++currentpos;
                switch (this.currentStatus) {
                    case DISPOSITION: {
                        if (read == '=') {
                            this.currentStatus = MultiPartStatus.FIELD;
                            final int equalpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
                            this.currentAttribute = this.factory.createAttribute(this.request, key);
                            firstpos = currentpos;
                            continue;
                        }
                        if (read == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            final String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
                            (this.currentAttribute = this.factory.createAttribute(this.request, key)).setValue("");
                            this.addHttpData(this.currentAttribute);
                            this.currentAttribute = null;
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        continue;
                    }
                    case FIELD: {
                        if (read == '&') {
                            this.currentStatus = MultiPartStatus.DISPOSITION;
                            final int ampersandpos = currentpos - 1;
                            this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                            firstpos = currentpos;
                            contRead = true;
                            continue;
                        }
                        if (read == '\r') {
                            if (sao.pos < sao.limit) {
                                read = (char)(sao.bytes[sao.pos++] & 0xFF);
                                ++currentpos;
                                if (read == '\n') {
                                    this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                    final int ampersandpos = currentpos - 2;
                                    sao.setReadPosition(0);
                                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                    firstpos = currentpos;
                                    contRead = false;
                                    break Label_0512;
                                }
                                sao.setReadPosition(0);
                                throw new ErrorDataDecoderException("Bad end of line");
                            }
                            else {
                                if (sao.limit > 0) {
                                    --currentpos;
                                    continue;
                                }
                                continue;
                            }
                        }
                        else {
                            if (read == '\n') {
                                this.currentStatus = MultiPartStatus.PREEPILOGUE;
                                final int ampersandpos = currentpos - 1;
                                sao.setReadPosition(0);
                                this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                break Label_0512;
                            }
                            continue;
                        }
                        break;
                    }
                    default: {
                        sao.setReadPosition(0);
                        contRead = false;
                        break Label_0512;
                    }
                }
            }
            if (this.isLastChunk && this.currentAttribute != null) {
                final int ampersandpos = currentpos;
                if (ampersandpos > firstpos) {
                    this.setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
                }
                else if (!this.currentAttribute.isCompleted()) {
                    this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
                }
                firstpos = currentpos;
                this.currentStatus = MultiPartStatus.EPILOGUE;
                this.undecodedChunk.readerIndex(firstpos);
                return;
            }
            if (contRead && this.currentAttribute != null) {
                if (this.currentStatus == MultiPartStatus.FIELD) {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
                    firstpos = currentpos;
                }
                this.undecodedChunk.readerIndex(firstpos);
            }
            else {
                this.undecodedChunk.readerIndex(firstpos);
            }
        }
        catch (ErrorDataDecoderException e) {
            this.undecodedChunk.readerIndex(firstpos);
            throw e;
        }
        catch (IOException e2) {
            this.undecodedChunk.readerIndex(firstpos);
            throw new ErrorDataDecoderException(e2);
        }
    }
    
    private void setFinalBuffer(final ByteBuf buffer) throws ErrorDataDecoderException, IOException {
        this.currentAttribute.addContent(buffer, true);
        final String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
        this.currentAttribute.setValue(value);
        this.addHttpData(this.currentAttribute);
        this.currentAttribute = null;
    }
    
    private static String decodeAttribute(final String s, final Charset charset) throws ErrorDataDecoderException {
        try {
            return QueryStringDecoder.decodeComponent(s, charset);
        }
        catch (IllegalArgumentException e) {
            throw new ErrorDataDecoderException("Bad string: '" + s + '\'', e);
        }
    }
    
    private void parseBodyMultipart() throws ErrorDataDecoderException {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        for (InterfaceHttpData data = this.decodeMultipart(this.currentStatus); data != null; data = this.decodeMultipart(this.currentStatus)) {
            this.addHttpData(data);
            if (this.currentStatus == MultiPartStatus.PREEPILOGUE) {
                break;
            }
            if (this.currentStatus == MultiPartStatus.EPILOGUE) {
                break;
            }
        }
    }
    
    private InterfaceHttpData decodeMultipart(final MultiPartStatus state) throws ErrorDataDecoderException {
        switch (state) {
            case NOTSTARTED: {
                throw new ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case PREAMBLE: {
                throw new ErrorDataDecoderException("Should not be called with the current getStatus");
            }
            case HEADERDELIMITER: {
                return this.findMultipartDelimiter(this.multipartDataBoundary, MultiPartStatus.DISPOSITION, MultiPartStatus.PREEPILOGUE);
            }
            case DISPOSITION: {
                return this.findMultipartDisposition();
            }
            case FIELD: {
                Charset localCharset = null;
                final Attribute charsetAttribute = this.currentFieldAttributes.get("charset");
                if (charsetAttribute != null) {
                    try {
                        localCharset = Charset.forName(charsetAttribute.getValue());
                    }
                    catch (IOException e) {
                        throw new ErrorDataDecoderException(e);
                    }
                }
                final Attribute nameAttribute = this.currentFieldAttributes.get("name");
                if (this.currentAttribute == null) {
                    try {
                        this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
                    }
                    catch (NullPointerException e2) {
                        throw new ErrorDataDecoderException(e2);
                    }
                    catch (IllegalArgumentException e3) {
                        throw new ErrorDataDecoderException(e3);
                    }
                    catch (IOException e4) {
                        throw new ErrorDataDecoderException(e4);
                    }
                    if (localCharset != null) {
                        this.currentAttribute.setCharset(localCharset);
                    }
                }
                try {
                    this.loadFieldMultipart(this.multipartDataBoundary);
                }
                catch (NotEnoughDataDecoderException e5) {
                    return null;
                }
                final Attribute finalAttribute = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = MultiPartStatus.HEADERDELIMITER;
                return finalAttribute;
            }
            case FILEUPLOAD: {
                return this.getFileUpload(this.multipartDataBoundary);
            }
            case MIXEDDELIMITER: {
                return this.findMultipartDelimiter(this.multipartMixedBoundary, MultiPartStatus.MIXEDDISPOSITION, MultiPartStatus.HEADERDELIMITER);
            }
            case MIXEDDISPOSITION: {
                return this.findMultipartDisposition();
            }
            case MIXEDFILEUPLOAD: {
                return this.getFileUpload(this.multipartMixedBoundary);
            }
            case PREEPILOGUE: {
                return null;
            }
            case EPILOGUE: {
                return null;
            }
            default: {
                throw new ErrorDataDecoderException("Shouldn't reach here.");
            }
        }
    }
    
    void skipControlCharacters() throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e2) {
            try {
                this.skipControlCharactersStandard();
            }
            catch (IndexOutOfBoundsException e1) {
                throw new NotEnoughDataDecoderException(e1);
            }
            return;
        }
        while (sao.pos < sao.limit) {
            final char c = (char)(sao.bytes[sao.pos++] & 0xFF);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                sao.setReadPosition(1);
                return;
            }
        }
        throw new NotEnoughDataDecoderException("Access out of bounds");
    }
    
    void skipControlCharactersStandard() {
        char c;
        do {
            c = (char)this.undecodedChunk.readUnsignedByte();
        } while (Character.isISOControl(c) || Character.isWhitespace(c));
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
    }
    
    private InterfaceHttpData findMultipartDelimiter(final String delimiter, final MultiPartStatus dispositionStatus, final MultiPartStatus closeDelimiterStatus) throws ErrorDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            this.skipControlCharacters();
        }
        catch (NotEnoughDataDecoderException e1) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        this.skipOneLine();
        String newline;
        try {
            newline = this.readDelimiter(delimiter);
        }
        catch (NotEnoughDataDecoderException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
        if (newline.equals(delimiter)) {
            this.currentStatus = dispositionStatus;
            return this.decodeMultipart(dispositionStatus);
        }
        if (!newline.equals(delimiter + "--")) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new ErrorDataDecoderException("No Multipart delimiter found");
        }
        this.currentStatus = closeDelimiterStatus;
        if (this.currentStatus == MultiPartStatus.HEADERDELIMITER) {
            this.currentFieldAttributes = null;
            return this.decodeMultipart(MultiPartStatus.HEADERDELIMITER);
        }
        return null;
    }
    
    private InterfaceHttpData findMultipartDisposition() throws ErrorDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        if (this.currentStatus == MultiPartStatus.DISPOSITION) {
            this.currentFieldAttributes = new TreeMap<String, Attribute>(CaseIgnoringComparator.INSTANCE);
        }
        while (!this.skipOneLine()) {
            String newline;
            try {
                this.skipControlCharacters();
                newline = this.readLine();
            }
            catch (NotEnoughDataDecoderException e9) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
            final String[] contents = splitMultipartHeader(newline);
            if (contents[0].equalsIgnoreCase("Content-Disposition")) {
                boolean checkSecondArg;
                if (this.currentStatus == MultiPartStatus.DISPOSITION) {
                    checkSecondArg = contents[1].equalsIgnoreCase("form-data");
                }
                else {
                    checkSecondArg = (contents[1].equalsIgnoreCase("attachment") || contents[1].equalsIgnoreCase("file"));
                }
                if (!checkSecondArg) {
                    continue;
                }
                for (int i = 2; i < contents.length; ++i) {
                    final String[] values = StringUtil.split(contents[i], '=');
                    Attribute attribute;
                    try {
                        final String name = cleanString(values[0]);
                        String value = values[1];
                        if ("filename".equals(name)) {
                            value = value.substring(1, value.length() - 1);
                        }
                        else {
                            value = cleanString(value);
                        }
                        attribute = this.factory.createAttribute(this.request, name, value);
                    }
                    catch (NullPointerException e) {
                        throw new ErrorDataDecoderException(e);
                    }
                    catch (IllegalArgumentException e2) {
                        throw new ErrorDataDecoderException(e2);
                    }
                    this.currentFieldAttributes.put(attribute.getName(), attribute);
                }
            }
            else if (contents[0].equalsIgnoreCase("Content-Transfer-Encoding")) {
                Attribute attribute2;
                try {
                    attribute2 = this.factory.createAttribute(this.request, "Content-Transfer-Encoding", cleanString(contents[1]));
                }
                catch (NullPointerException e3) {
                    throw new ErrorDataDecoderException(e3);
                }
                catch (IllegalArgumentException e4) {
                    throw new ErrorDataDecoderException(e4);
                }
                this.currentFieldAttributes.put("Content-Transfer-Encoding", attribute2);
            }
            else if (contents[0].equalsIgnoreCase("Content-Length")) {
                Attribute attribute2;
                try {
                    attribute2 = this.factory.createAttribute(this.request, "Content-Length", cleanString(contents[1]));
                }
                catch (NullPointerException e3) {
                    throw new ErrorDataDecoderException(e3);
                }
                catch (IllegalArgumentException e4) {
                    throw new ErrorDataDecoderException(e4);
                }
                this.currentFieldAttributes.put("Content-Length", attribute2);
            }
            else {
                if (!contents[0].equalsIgnoreCase("Content-Type")) {
                    throw new ErrorDataDecoderException("Unknown Params: " + newline);
                }
                if (contents[1].equalsIgnoreCase("multipart/mixed")) {
                    if (this.currentStatus == MultiPartStatus.DISPOSITION) {
                        final String[] values2 = StringUtil.split(contents[2], '=');
                        this.multipartMixedBoundary = "--" + values2[1];
                        this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
                        return this.decodeMultipart(MultiPartStatus.MIXEDDELIMITER);
                    }
                    throw new ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
                }
                else {
                    for (int j = 1; j < contents.length; ++j) {
                        if (contents[j].toLowerCase().startsWith("charset")) {
                            final String[] values3 = StringUtil.split(contents[j], '=');
                            Attribute attribute3;
                            try {
                                attribute3 = this.factory.createAttribute(this.request, "charset", cleanString(values3[1]));
                            }
                            catch (NullPointerException e5) {
                                throw new ErrorDataDecoderException(e5);
                            }
                            catch (IllegalArgumentException e6) {
                                throw new ErrorDataDecoderException(e6);
                            }
                            this.currentFieldAttributes.put("charset", attribute3);
                        }
                        else {
                            Attribute attribute4;
                            try {
                                attribute4 = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[j]);
                            }
                            catch (NullPointerException e7) {
                                throw new ErrorDataDecoderException(e7);
                            }
                            catch (IllegalArgumentException e8) {
                                throw new ErrorDataDecoderException(e8);
                            }
                            this.currentFieldAttributes.put(attribute4.getName(), attribute4);
                        }
                    }
                }
            }
        }
        final Attribute filenameAttribute = this.currentFieldAttributes.get("filename");
        if (this.currentStatus == MultiPartStatus.DISPOSITION) {
            if (filenameAttribute != null) {
                this.currentStatus = MultiPartStatus.FILEUPLOAD;
                return this.decodeMultipart(MultiPartStatus.FILEUPLOAD);
            }
            this.currentStatus = MultiPartStatus.FIELD;
            return this.decodeMultipart(MultiPartStatus.FIELD);
        }
        else {
            if (filenameAttribute != null) {
                this.currentStatus = MultiPartStatus.MIXEDFILEUPLOAD;
                return this.decodeMultipart(MultiPartStatus.MIXEDFILEUPLOAD);
            }
            throw new ErrorDataDecoderException("Filename not found");
        }
    }
    
    protected InterfaceHttpData getFileUpload(final String delimiter) throws ErrorDataDecoderException {
        final Attribute encoding = this.currentFieldAttributes.get("Content-Transfer-Encoding");
        Charset localCharset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (encoding != null) {
            String code;
            try {
                code = encoding.getValue().toLowerCase();
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
            if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                localCharset = HttpPostBodyUtil.US_ASCII;
            }
            else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                localCharset = HttpPostBodyUtil.ISO_8859_1;
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
            }
            else {
                if (!code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                    throw new ErrorDataDecoderException("TransferEncoding Unknown: " + code);
                }
                mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
            }
        }
        final Attribute charsetAttribute = this.currentFieldAttributes.get("charset");
        if (charsetAttribute != null) {
            try {
                localCharset = Charset.forName(charsetAttribute.getValue());
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
        }
        if (this.currentFileUpload == null) {
            final Attribute filenameAttribute = this.currentFieldAttributes.get("filename");
            final Attribute nameAttribute = this.currentFieldAttributes.get("name");
            final Attribute contentTypeAttribute = this.currentFieldAttributes.get("Content-Type");
            if (contentTypeAttribute == null) {
                throw new ErrorDataDecoderException("Content-Type is absent but required");
            }
            final Attribute lengthAttribute = this.currentFieldAttributes.get("Content-Length");
            long size;
            try {
                size = ((lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L);
            }
            catch (IOException e2) {
                throw new ErrorDataDecoderException(e2);
            }
            catch (NumberFormatException e5) {
                size = 0L;
            }
            try {
                this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentTypeAttribute.getValue(), mechanism.value(), localCharset, size);
            }
            catch (NullPointerException e3) {
                throw new ErrorDataDecoderException(e3);
            }
            catch (IllegalArgumentException e4) {
                throw new ErrorDataDecoderException(e4);
            }
            catch (IOException e2) {
                throw new ErrorDataDecoderException(e2);
            }
        }
        try {
            this.readFileUploadByteMultipart(delimiter);
        }
        catch (NotEnoughDataDecoderException e6) {
            return null;
        }
        if (this.currentFileUpload.isCompleted()) {
            if (this.currentStatus == MultiPartStatus.FILEUPLOAD) {
                this.currentStatus = MultiPartStatus.HEADERDELIMITER;
                this.currentFieldAttributes = null;
            }
            else {
                this.currentStatus = MultiPartStatus.MIXEDDELIMITER;
                this.cleanMixedAttributes();
            }
            final FileUpload fileUpload = this.currentFileUpload;
            this.currentFileUpload = null;
            return fileUpload;
        }
        return null;
    }
    
    public void destroy() {
        this.checkDestroyed();
        this.cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
        for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); ++i) {
            this.bodyListHttpData.get(i).release();
        }
    }
    
    public void cleanFiles() {
        this.checkDestroyed();
        this.factory.cleanRequestHttpDatas(this.request);
    }
    
    public void removeHttpDataFromClean(final InterfaceHttpData data) {
        this.checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, data);
    }
    
    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove("charset");
        this.currentFieldAttributes.remove("Content-Length");
        this.currentFieldAttributes.remove("Content-Transfer-Encoding");
        this.currentFieldAttributes.remove("Content-Type");
        this.currentFieldAttributes.remove("filename");
    }
    
    private String readLineStandard() throws NotEnoughDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
                    if (nextByte == 10) {
                        this.undecodedChunk.skipBytes(1);
                        return line.toString(this.charset);
                    }
                    line.writeByte(13);
                }
                else {
                    if (nextByte == 10) {
                        return line.toString(this.charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
    }
    
    private String readLine() throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e2) {
            return this.readLineStandard();
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final ByteBuf line = Unpooled.buffer(64);
            while (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos < sao.limit) {
                        nextByte = sao.bytes[sao.pos++];
                        if (nextByte == 10) {
                            sao.setReadPosition(0);
                            return line.toString(this.charset);
                        }
                        final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                        --seekAheadOptimize.pos;
                        line.writeByte(13);
                    }
                    else {
                        line.writeByte(nextByte);
                    }
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return line.toString(this.charset);
                    }
                    line.writeByte(nextByte);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
    }
    
    private String readDelimiterStandard(final String delimiter) throws NotEnoughDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            final StringBuilder sb = new StringBuilder(64);
            int delimiterPos = 0;
            final int len = delimiter.length();
            while (this.undecodedChunk.isReadable() && delimiterPos < len) {
                final byte nextByte = this.undecodedChunk.readByte();
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (nextByte == 13) {
                    nextByte = this.undecodedChunk.readByte();
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 45) {
                            sb.append('-');
                            if (!this.undecodedChunk.isReadable()) {
                                return sb.toString();
                            }
                            nextByte = this.undecodedChunk.readByte();
                            if (nextByte == 13) {
                                nextByte = this.undecodedChunk.readByte();
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                this.undecodedChunk.readerIndex(readerIndex);
                                throw new NotEnoughDataDecoderException();
                            }
                            else {
                                if (nextByte == 10) {
                                    return sb.toString();
                                }
                                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                                return sb.toString();
                            }
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
    }
    
    private String readDelimiter(final String delimiter) throws NotEnoughDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e2) {
            return this.readDelimiterStandard(delimiter);
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        int delimiterPos = 0;
        final int len = delimiter.length();
        try {
            final StringBuilder sb = new StringBuilder(64);
            while (sao.pos < sao.limit && delimiterPos < len) {
                final byte nextByte = sao.bytes[sao.pos++];
                if (nextByte != delimiter.charAt(delimiterPos)) {
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new NotEnoughDataDecoderException();
                }
                ++delimiterPos;
                sb.append((char)nextByte);
            }
            if (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (nextByte == 13) {
                    if (sao.pos >= sao.limit) {
                        this.undecodedChunk.readerIndex(readerIndex);
                        throw new NotEnoughDataDecoderException();
                    }
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    this.undecodedChunk.readerIndex(readerIndex);
                    throw new NotEnoughDataDecoderException();
                }
                else {
                    if (nextByte == 10) {
                        sao.setReadPosition(0);
                        return sb.toString();
                    }
                    if (nextByte == 45) {
                        sb.append('-');
                        if (sao.pos < sao.limit) {
                            nextByte = sao.bytes[sao.pos++];
                            if (nextByte == 45) {
                                sb.append('-');
                                if (sao.pos >= sao.limit) {
                                    sao.setReadPosition(0);
                                    return sb.toString();
                                }
                                nextByte = sao.bytes[sao.pos++];
                                if (nextByte == 13) {
                                    if (sao.pos >= sao.limit) {
                                        this.undecodedChunk.readerIndex(readerIndex);
                                        throw new NotEnoughDataDecoderException();
                                    }
                                    nextByte = sao.bytes[sao.pos++];
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    this.undecodedChunk.readerIndex(readerIndex);
                                    throw new NotEnoughDataDecoderException();
                                }
                                else {
                                    if (nextByte == 10) {
                                        sao.setReadPosition(0);
                                        return sb.toString();
                                    }
                                    sao.setReadPosition(1);
                                    return sb.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e);
        }
        this.undecodedChunk.readerIndex(readerIndex);
        throw new NotEnoughDataDecoderException();
    }
    
    private void readFileUploadByteMultipartStandard(final String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        boolean newLine = true;
        int index = 0;
        int lastPosition = this.undecodedChunk.readerIndex();
        boolean found = false;
        while (this.undecodedChunk.isReadable()) {
            byte nextByte = this.undecodedChunk.readByte();
            if (newLine) {
                if (nextByte == delimiter.codePointAt(index)) {
                    ++index;
                    if (delimiter.length() == index) {
                        found = true;
                        break;
                    }
                    continue;
                }
                else {
                    newLine = false;
                    index = 0;
                    if (nextByte == 13) {
                        if (!this.undecodedChunk.isReadable()) {
                            continue;
                        }
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 2;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                            this.undecodedChunk.readerIndex(lastPosition);
                        }
                    }
                    else if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastPosition = this.undecodedChunk.readerIndex() - 1;
                    }
                    else {
                        lastPosition = this.undecodedChunk.readerIndex();
                    }
                }
            }
            else if (nextByte == 13) {
                if (!this.undecodedChunk.isReadable()) {
                    continue;
                }
                nextByte = this.undecodedChunk.readByte();
                if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastPosition = this.undecodedChunk.readerIndex() - 2;
                }
                else {
                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                    this.undecodedChunk.readerIndex(lastPosition);
                }
            }
            else if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastPosition = this.undecodedChunk.readerIndex() - 1;
            }
            else {
                lastPosition = this.undecodedChunk.readerIndex();
            }
        }
        final ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
        if (found) {
            try {
                this.currentFileUpload.addContent(buffer, true);
                this.undecodedChunk.readerIndex(lastPosition);
                return;
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
        }
        try {
            this.currentFileUpload.addContent(buffer, false);
            this.undecodedChunk.readerIndex(lastPosition);
            throw new NotEnoughDataDecoderException();
        }
        catch (IOException e) {
            throw new ErrorDataDecoderException(e);
        }
    }
    
    private void readFileUploadByteMultipart(final String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e2) {
            this.readFileUploadByteMultipartStandard(delimiter);
            return;
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        boolean newLine = true;
        int index = 0;
        int lastrealpos = sao.pos;
        boolean found = false;
        while (sao.pos < sao.limit) {
            byte nextByte = sao.bytes[sao.pos++];
            if (newLine) {
                if (nextByte == delimiter.codePointAt(index)) {
                    ++index;
                    if (delimiter.length() == index) {
                        found = true;
                        break;
                    }
                    continue;
                }
                else {
                    newLine = false;
                    index = 0;
                    if (nextByte == 13) {
                        if (sao.pos >= sao.limit) {
                            continue;
                        }
                        nextByte = sao.bytes[sao.pos++];
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastrealpos = sao.pos - 2;
                        }
                        else {
                            final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                            --seekAheadOptimize.pos;
                            lastrealpos = sao.pos;
                        }
                    }
                    else if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastrealpos = sao.pos - 1;
                    }
                    else {
                        lastrealpos = sao.pos;
                    }
                }
            }
            else if (nextByte == 13) {
                if (sao.pos >= sao.limit) {
                    continue;
                }
                nextByte = sao.bytes[sao.pos++];
                if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastrealpos = sao.pos - 2;
                }
                else {
                    final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize2 = sao;
                    --seekAheadOptimize2.pos;
                    lastrealpos = sao.pos;
                }
            }
            else if (nextByte == 10) {
                newLine = true;
                index = 0;
                lastrealpos = sao.pos - 1;
            }
            else {
                lastrealpos = sao.pos;
            }
        }
        final int lastPosition = sao.getReadPosition(lastrealpos);
        final ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
        if (found) {
            try {
                this.currentFileUpload.addContent(buffer, true);
                this.undecodedChunk.readerIndex(lastPosition);
                return;
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
        }
        try {
            this.currentFileUpload.addContent(buffer, false);
            this.undecodedChunk.readerIndex(lastPosition);
            throw new NotEnoughDataDecoderException();
        }
        catch (IOException e) {
            throw new ErrorDataDecoderException(e);
        }
    }
    
    private void loadFieldMultipartStandard(final String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            boolean newLine = true;
            int index = 0;
            int lastPosition = this.undecodedChunk.readerIndex();
            boolean found = false;
            while (this.undecodedChunk.isReadable()) {
                byte nextByte = this.undecodedChunk.readByte();
                if (newLine) {
                    if (nextByte == delimiter.codePointAt(index)) {
                        ++index;
                        if (delimiter.length() == index) {
                            found = true;
                            break;
                        }
                        continue;
                    }
                    else {
                        newLine = false;
                        index = 0;
                        if (nextByte == 13) {
                            if (this.undecodedChunk.isReadable()) {
                                nextByte = this.undecodedChunk.readByte();
                                if (nextByte == 10) {
                                    newLine = true;
                                    index = 0;
                                    lastPosition = this.undecodedChunk.readerIndex() - 2;
                                }
                                else {
                                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                                    this.undecodedChunk.readerIndex(lastPosition);
                                }
                            }
                            else {
                                lastPosition = this.undecodedChunk.readerIndex() - 1;
                            }
                        }
                        else if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex();
                        }
                    }
                }
                else if (nextByte == 13) {
                    if (this.undecodedChunk.isReadable()) {
                        nextByte = this.undecodedChunk.readByte();
                        if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastPosition = this.undecodedChunk.readerIndex() - 2;
                        }
                        else {
                            lastPosition = this.undecodedChunk.readerIndex() - 1;
                            this.undecodedChunk.readerIndex(lastPosition);
                        }
                    }
                    else {
                        lastPosition = this.undecodedChunk.readerIndex() - 1;
                    }
                }
                else if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastPosition = this.undecodedChunk.readerIndex() - 1;
                }
                else {
                    lastPosition = this.undecodedChunk.readerIndex();
                }
            }
            if (!found) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
                }
                catch (IOException e) {
                    throw new ErrorDataDecoderException(e);
                }
                this.undecodedChunk.readerIndex(lastPosition);
                throw new NotEnoughDataDecoderException();
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
            this.undecodedChunk.readerIndex(lastPosition);
        }
        catch (IndexOutOfBoundsException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e2);
        }
    }
    
    private void loadFieldMultipart(final String delimiter) throws NotEnoughDataDecoderException, ErrorDataDecoderException {
        HttpPostBodyUtil.SeekAheadOptimize sao;
        try {
            sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        }
        catch (HttpPostBodyUtil.SeekAheadNoBackArrayException e3) {
            this.loadFieldMultipartStandard(delimiter);
            return;
        }
        final int readerIndex = this.undecodedChunk.readerIndex();
        try {
            boolean newLine = true;
            int index = 0;
            int lastrealpos = sao.pos;
            boolean found = false;
            while (sao.pos < sao.limit) {
                byte nextByte = sao.bytes[sao.pos++];
                if (newLine) {
                    if (nextByte == delimiter.codePointAt(index)) {
                        ++index;
                        if (delimiter.length() == index) {
                            found = true;
                            break;
                        }
                        continue;
                    }
                    else {
                        newLine = false;
                        index = 0;
                        if (nextByte == 13) {
                            if (sao.pos >= sao.limit) {
                                continue;
                            }
                            nextByte = sao.bytes[sao.pos++];
                            if (nextByte == 10) {
                                newLine = true;
                                index = 0;
                                lastrealpos = sao.pos - 2;
                            }
                            else {
                                final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize = sao;
                                --seekAheadOptimize.pos;
                                lastrealpos = sao.pos;
                            }
                        }
                        else if (nextByte == 10) {
                            newLine = true;
                            index = 0;
                            lastrealpos = sao.pos - 1;
                        }
                        else {
                            lastrealpos = sao.pos;
                        }
                    }
                }
                else if (nextByte == 13) {
                    if (sao.pos >= sao.limit) {
                        continue;
                    }
                    nextByte = sao.bytes[sao.pos++];
                    if (nextByte == 10) {
                        newLine = true;
                        index = 0;
                        lastrealpos = sao.pos - 2;
                    }
                    else {
                        final HttpPostBodyUtil.SeekAheadOptimize seekAheadOptimize2 = sao;
                        --seekAheadOptimize2.pos;
                        lastrealpos = sao.pos;
                    }
                }
                else if (nextByte == 10) {
                    newLine = true;
                    index = 0;
                    lastrealpos = sao.pos - 1;
                }
                else {
                    lastrealpos = sao.pos;
                }
            }
            final int lastPosition = sao.getReadPosition(lastrealpos);
            if (!found) {
                try {
                    this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
                }
                catch (IOException e) {
                    throw new ErrorDataDecoderException(e);
                }
                this.undecodedChunk.readerIndex(lastPosition);
                throw new NotEnoughDataDecoderException();
            }
            try {
                this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
            }
            catch (IOException e) {
                throw new ErrorDataDecoderException(e);
            }
            this.undecodedChunk.readerIndex(lastPosition);
        }
        catch (IndexOutOfBoundsException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            throw new NotEnoughDataDecoderException(e2);
        }
    }
    
    private static String cleanString(final String field) {
        final StringBuilder sb = new StringBuilder(field.length());
        for (int i = 0; i < field.length(); ++i) {
            final char nextChar = field.charAt(i);
            if (nextChar == ':') {
                sb.append(32);
            }
            else if (nextChar == ',') {
                sb.append(32);
            }
            else if (nextChar == '=') {
                sb.append(32);
            }
            else if (nextChar == ';') {
                sb.append(32);
            }
            else if (nextChar == '\t') {
                sb.append(32);
            }
            else if (nextChar != '\"') {
                sb.append(nextChar);
            }
        }
        return sb.toString().trim();
    }
    
    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            nextByte = this.undecodedChunk.readByte();
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        else {
            if (nextByte == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
            return false;
        }
    }
    
    private static String[] splitHeaderContentType(final String sb) {
        final int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
        int aEnd = sb.indexOf(59);
        if (aEnd == -1) {
            return new String[] { sb, "" };
        }
        if (sb.charAt(aEnd - 1) == ' ') {
            --aEnd;
        }
        final int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
        final int bEnd = HttpPostBodyUtil.findEndOfString(sb);
        return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd) };
    }
    
    private static String[] splitMultipartHeader(final String sb) {
        final ArrayList<String> headers = new ArrayList<String>(1);
        int nameEnd;
        int nameStart;
        for (nameStart = (nameEnd = HttpPostBodyUtil.findNonWhitespace(sb, 0)); nameEnd < sb.length(); ++nameEnd) {
            final char ch = sb.charAt(nameEnd);
            if (ch == ':') {
                break;
            }
            if (Character.isWhitespace(ch)) {
                break;
            }
        }
        int colonEnd;
        for (colonEnd = nameEnd; colonEnd < sb.length(); ++colonEnd) {
            if (sb.charAt(colonEnd) == ':') {
                ++colonEnd;
                break;
            }
        }
        final int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
        final int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
        headers.add(sb.substring(nameStart, nameEnd));
        final String svalue = sb.substring(valueStart, valueEnd);
        String[] values;
        if (svalue.indexOf(59) >= 0) {
            values = StringUtil.split(svalue, ';');
        }
        else {
            values = StringUtil.split(svalue, ',');
        }
        for (final String value : values) {
            headers.add(value.trim());
        }
        final String[] array = new String[headers.size()];
        for (int i = 0; i < headers.size(); ++i) {
            array[i] = headers.get(i);
        }
        return array;
    }
    
    private enum MultiPartStatus
    {
        NOTSTARTED, 
        PREAMBLE, 
        HEADERDELIMITER, 
        DISPOSITION, 
        FIELD, 
        FILEUPLOAD, 
        MIXEDPREAMBLE, 
        MIXEDDELIMITER, 
        MIXEDDISPOSITION, 
        MIXEDFILEUPLOAD, 
        MIXEDCLOSEDELIMITER, 
        CLOSEDELIMITER, 
        PREEPILOGUE, 
        EPILOGUE;
    }
    
    public static class NotEnoughDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = -7846841864603865638L;
        
        public NotEnoughDataDecoderException() {
        }
        
        public NotEnoughDataDecoderException(final String msg) {
            super(msg);
        }
        
        public NotEnoughDataDecoderException(final Throwable cause) {
            super(cause);
        }
        
        public NotEnoughDataDecoderException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }
    
    public static class EndOfDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = 1336267941020800769L;
    }
    
    public static class ErrorDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = 5020247425493164465L;
        
        public ErrorDataDecoderException() {
        }
        
        public ErrorDataDecoderException(final String msg) {
            super(msg);
        }
        
        public ErrorDataDecoderException(final Throwable cause) {
            super(cause);
        }
        
        public ErrorDataDecoderException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }
    
    public static class IncompatibleDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = -953268047926250267L;
        
        public IncompatibleDataDecoderException() {
        }
        
        public IncompatibleDataDecoderException(final String msg) {
            super(msg);
        }
        
        public IncompatibleDataDecoderException(final Throwable cause) {
            super(cause);
        }
        
        public IncompatibleDataDecoderException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }
}
