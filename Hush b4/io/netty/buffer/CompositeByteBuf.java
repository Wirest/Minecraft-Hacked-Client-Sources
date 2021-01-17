// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.channels.ScatteringByteChannel;
import io.netty.util.internal.EmptyArrays;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.ListIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.util.List;
import io.netty.util.ResourceLeak;

public class CompositeByteBuf extends AbstractReferenceCountedByteBuf
{
    private final ResourceLeak leak;
    private final ByteBufAllocator alloc;
    private final boolean direct;
    private final List<Component> components;
    private final int maxNumComponents;
    private static final ByteBuffer FULL_BYTEBUFFER;
    private boolean freed;
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents) {
        super(Integer.MAX_VALUE);
        this.components = new ArrayList<Component>();
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.leak = CompositeByteBuf.leakDetector.open(this);
    }
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents, final ByteBuf... buffers) {
        super(Integer.MAX_VALUE);
        this.components = new ArrayList<Component>();
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.addComponents0(0, buffers);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = CompositeByteBuf.leakDetector.open(this);
    }
    
    public CompositeByteBuf(final ByteBufAllocator alloc, final boolean direct, final int maxNumComponents, final Iterable<ByteBuf> buffers) {
        super(Integer.MAX_VALUE);
        this.components = new ArrayList<Component>();
        if (alloc == null) {
            throw new NullPointerException("alloc");
        }
        if (maxNumComponents < 2) {
            throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
        }
        this.alloc = alloc;
        this.direct = direct;
        this.maxNumComponents = maxNumComponents;
        this.addComponents0(0, buffers);
        this.consolidateIfNeeded();
        this.setIndex(0, this.capacity());
        this.leak = CompositeByteBuf.leakDetector.open(this);
    }
    
    public CompositeByteBuf addComponent(final ByteBuf buffer) {
        this.addComponent0(this.components.size(), buffer);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponents(final ByteBuf... buffers) {
        this.addComponents0(this.components.size(), buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponents(final Iterable<ByteBuf> buffers) {
        this.addComponents0(this.components.size(), buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    public CompositeByteBuf addComponent(final int cIndex, final ByteBuf buffer) {
        this.addComponent0(cIndex, buffer);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponent0(final int cIndex, final ByteBuf buffer) {
        this.checkComponentIndex(cIndex);
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        final int readableBytes = buffer.readableBytes();
        if (readableBytes == 0) {
            return cIndex;
        }
        final Component c = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
        if (cIndex == this.components.size()) {
            this.components.add(c);
            if (cIndex == 0) {
                c.endOffset = readableBytes;
            }
            else {
                final Component prev = this.components.get(cIndex - 1);
                c.offset = prev.endOffset;
                c.endOffset = c.offset + readableBytes;
            }
        }
        else {
            this.components.add(cIndex, c);
            this.updateComponentOffsets(cIndex);
        }
        return cIndex;
    }
    
    public CompositeByteBuf addComponents(final int cIndex, final ByteBuf... buffers) {
        this.addComponents0(cIndex, buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponents0(int cIndex, final ByteBuf... buffers) {
        this.checkComponentIndex(cIndex);
        if (buffers == null) {
            throw new NullPointerException("buffers");
        }
        int readableBytes = 0;
        for (final ByteBuf b : buffers) {
            if (b == null) {
                break;
            }
            readableBytes += b.readableBytes();
        }
        if (readableBytes == 0) {
            return cIndex;
        }
        for (final ByteBuf b : buffers) {
            if (b == null) {
                break;
            }
            if (b.isReadable()) {
                cIndex = this.addComponent0(cIndex, b) + 1;
                final int size = this.components.size();
                if (cIndex > size) {
                    cIndex = size;
                }
            }
            else {
                b.release();
            }
        }
        return cIndex;
    }
    
    public CompositeByteBuf addComponents(final int cIndex, final Iterable<ByteBuf> buffers) {
        this.addComponents0(cIndex, buffers);
        this.consolidateIfNeeded();
        return this;
    }
    
    private int addComponents0(final int cIndex, Iterable<ByteBuf> buffers) {
        if (buffers == null) {
            throw new NullPointerException("buffers");
        }
        if (buffers instanceof ByteBuf) {
            return this.addComponent0(cIndex, (ByteBuf)buffers);
        }
        if (!(buffers instanceof Collection)) {
            final List<ByteBuf> list = new ArrayList<ByteBuf>();
            for (final ByteBuf b : buffers) {
                list.add(b);
            }
            buffers = list;
        }
        final Collection<ByteBuf> col = (Collection<ByteBuf>)(Collection)buffers;
        return this.addComponents0(cIndex, (ByteBuf[])col.toArray(new ByteBuf[col.size()]));
    }
    
    private void consolidateIfNeeded() {
        final int numComponents = this.components.size();
        if (numComponents > this.maxNumComponents) {
            final int capacity = this.components.get(numComponents - 1).endOffset;
            final ByteBuf consolidated = this.allocBuffer(capacity);
            for (int i = 0; i < numComponents; ++i) {
                final Component c = this.components.get(i);
                final ByteBuf b = c.buf;
                consolidated.writeBytes(b);
                c.freeIfNecessary();
            }
            final Component c2 = new Component(consolidated);
            c2.endOffset = c2.length;
            this.components.clear();
            this.components.add(c2);
        }
    }
    
    private void checkComponentIndex(final int cIndex) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", cIndex, this.components.size()));
        }
    }
    
    private void checkComponentIndex(final int cIndex, final int numComponents) {
        this.ensureAccessible();
        if (cIndex < 0 || cIndex + numComponents > this.components.size()) {
            throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", cIndex, numComponents, this.components.size()));
        }
    }
    
    private void updateComponentOffsets(int cIndex) {
        final int size = this.components.size();
        if (size <= cIndex) {
            return;
        }
        final Component c = this.components.get(cIndex);
        if (cIndex == 0) {
            c.offset = 0;
            c.endOffset = c.length;
            ++cIndex;
        }
        for (int i = cIndex; i < size; ++i) {
            final Component prev = this.components.get(i - 1);
            final Component cur = this.components.get(i);
            cur.offset = prev.endOffset;
            cur.endOffset = cur.offset + cur.length;
        }
    }
    
    public CompositeByteBuf removeComponent(final int cIndex) {
        this.checkComponentIndex(cIndex);
        this.components.remove(cIndex).freeIfNecessary();
        this.updateComponentOffsets(cIndex);
        return this;
    }
    
    public CompositeByteBuf removeComponents(final int cIndex, final int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        final List<Component> toRemove = this.components.subList(cIndex, cIndex + numComponents);
        for (final Component c : toRemove) {
            c.freeIfNecessary();
        }
        toRemove.clear();
        this.updateComponentOffsets(cIndex);
        return this;
    }
    
    public Iterator<ByteBuf> iterator() {
        this.ensureAccessible();
        final List<ByteBuf> list = new ArrayList<ByteBuf>(this.components.size());
        for (final Component c : this.components) {
            list.add(c.buf);
        }
        return list.iterator();
    }
    
    public List<ByteBuf> decompose(final int offset, final int length) {
        this.checkIndex(offset, length);
        if (length == 0) {
            return Collections.emptyList();
        }
        int componentId = this.toComponentIndex(offset);
        final List<ByteBuf> slice = new ArrayList<ByteBuf>(this.components.size());
        final Component firstC = this.components.get(componentId);
        final ByteBuf first = firstC.buf.duplicate();
        first.readerIndex(offset - firstC.offset);
        ByteBuf buf = first;
        int bytesToSlice = length;
        do {
            final int readableBytes = buf.readableBytes();
            if (bytesToSlice <= readableBytes) {
                buf.writerIndex(buf.readerIndex() + bytesToSlice);
                slice.add(buf);
                break;
            }
            slice.add(buf);
            bytesToSlice -= readableBytes;
            ++componentId;
            buf = this.components.get(componentId).buf.duplicate();
        } while (bytesToSlice > 0);
        for (int i = 0; i < slice.size(); ++i) {
            slice.set(i, slice.get(i).slice());
        }
        return slice;
    }
    
    @Override
    public boolean isDirect() {
        final int size = this.components.size();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (!this.components.get(i).buf.isDirect()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasArray() {
        return this.components.size() == 1 && this.components.get(0).buf.hasArray();
    }
    
    @Override
    public byte[] array() {
        if (this.components.size() == 1) {
            return this.components.get(0).buf.array();
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int arrayOffset() {
        if (this.components.size() == 1) {
            return this.components.get(0).buf.arrayOffset();
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.components.size() == 1 && this.components.get(0).buf.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress() {
        if (this.components.size() == 1) {
            return this.components.get(0).buf.memoryAddress();
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int capacity() {
        if (this.components.isEmpty()) {
            return 0;
        }
        return this.components.get(this.components.size() - 1).endOffset;
    }
    
    @Override
    public CompositeByteBuf capacity(final int newCapacity) {
        this.ensureAccessible();
        if (newCapacity < 0 || newCapacity > this.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int oldCapacity = this.capacity();
        if (newCapacity > oldCapacity) {
            final int paddingLength = newCapacity - oldCapacity;
            final int nComponents = this.components.size();
            if (nComponents < this.maxNumComponents) {
                final ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(this.components.size(), padding);
            }
            else {
                final ByteBuf padding = this.allocBuffer(paddingLength);
                padding.setIndex(0, paddingLength);
                this.addComponent0(this.components.size(), padding);
                this.consolidateIfNeeded();
            }
        }
        else if (newCapacity < oldCapacity) {
            int bytesToTrim = oldCapacity - newCapacity;
            final ListIterator<Component> i = this.components.listIterator(this.components.size());
            while (i.hasPrevious()) {
                final Component c = i.previous();
                if (bytesToTrim < c.length) {
                    final Component newC = new Component(c.buf.slice(0, c.length - bytesToTrim));
                    newC.offset = c.offset;
                    newC.endOffset = newC.offset + newC.length;
                    i.set(newC);
                    break;
                }
                bytesToTrim -= c.length;
                i.remove();
            }
            if (this.readerIndex() > newCapacity) {
                this.setIndex(newCapacity, newCapacity);
            }
            else if (this.writerIndex() > newCapacity) {
                this.writerIndex(newCapacity);
            }
        }
        return this;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.alloc;
    }
    
    @Override
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    public int numComponents() {
        return this.components.size();
    }
    
    public int maxNumComponents() {
        return this.maxNumComponents;
    }
    
    public int toComponentIndex(final int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            final int mid = low + high >>> 1;
            final Component c = this.components.get(mid);
            if (offset >= c.endOffset) {
                low = mid + 1;
            }
            else {
                if (offset >= c.offset) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        throw new Error("should not reach here");
    }
    
    public int toByteIndex(final int cIndex) {
        this.checkComponentIndex(cIndex);
        return this.components.get(cIndex).offset;
    }
    
    @Override
    public byte getByte(final int index) {
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        final Component c = this.findComponent(index);
        return c.buf.getByte(index - c.offset);
    }
    
    @Override
    protected short _getShort(final int index) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            return c.buf.getShort(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (short)((this._getByte(index) & 0xFF) << 8 | (this._getByte(index + 1) & 0xFF));
        }
        return (short)((this._getByte(index) & 0xFF) | (this._getByte(index + 1) & 0xFF) << 8);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            return c.buf.getUnsignedMedium(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 8 | (this._getByte(index + 2) & 0xFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getByte(index + 2) & 0xFF) << 16;
    }
    
    @Override
    protected int _getInt(final int index) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            return c.buf.getInt(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return (this._getShort(index) & 0xFFFF) << 16 | (this._getShort(index + 2) & 0xFFFF);
        }
        return (this._getShort(index) & 0xFFFF) | (this._getShort(index + 2) & 0xFFFF) << 16;
    }
    
    @Override
    protected long _getLong(final int index) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            return c.buf.getLong(index - c.offset);
        }
        if (this.order() == ByteOrder.BIG_ENDIAN) {
            return ((long)this._getInt(index) & 0xFFFFFFFFL) << 32 | ((long)this._getInt(index + 4) & 0xFFFFFFFFL);
        }
        return ((long)this._getInt(index) & 0xFFFFFFFFL) | ((long)this._getInt(index + 4) & 0xFFFFFFFFL) << 32;
    }
    
    @Override
    public CompositeByteBuf getBytes(int index, final byte[] dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(int index, final ByteBuffer dst) {
        final int limit = dst.limit();
        int length = dst.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = this.toComponentIndex(index);
        try {
            while (length > 0) {
                final Component c = this.components.get(i);
                final ByteBuf s = c.buf;
                final int adjustment = c.offset;
                final int localLength = Math.min(length, s.capacity() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                ++i;
            }
        }
        finally {
            dst.limit(limit);
        }
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(int index, final ByteBuf dst, int dstIndex, int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        final int count = this.nioBufferCount();
        if (count == 1) {
            return out.write(this.internalNioBuffer(index, length));
        }
        final long writtenBytes = out.write(this.nioBuffers(index, length));
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)writtenBytes;
    }
    
    @Override
    public CompositeByteBuf getBytes(int index, final OutputStream out, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, out, localLength);
            index += localLength;
        }
        return this;
    }
    
    @Override
    public CompositeByteBuf setByte(final int index, final int value) {
        final Component c = this.findComponent(index);
        c.buf.setByte(index - c.offset, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.setByte(index, value);
    }
    
    @Override
    public CompositeByteBuf setShort(final int index, final int value) {
        return (CompositeByteBuf)super.setShort(index, value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 2 <= c.endOffset) {
            c.buf.setShort(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setByte(index, (byte)(value >>> 8));
            this._setByte(index + 1, (byte)value);
        }
        else {
            this._setByte(index, (byte)value);
            this._setByte(index + 1, (byte)(value >>> 8));
        }
    }
    
    @Override
    public CompositeByteBuf setMedium(final int index, final int value) {
        return (CompositeByteBuf)super.setMedium(index, value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 3 <= c.endOffset) {
            c.buf.setMedium(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >> 8));
            this._setByte(index + 2, (byte)value);
        }
        else {
            this._setShort(index, (short)value);
            this._setByte(index + 2, (byte)(value >>> 16));
        }
    }
    
    @Override
    public CompositeByteBuf setInt(final int index, final int value) {
        return (CompositeByteBuf)super.setInt(index, value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        final Component c = this.findComponent(index);
        if (index + 4 <= c.endOffset) {
            c.buf.setInt(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setShort(index, (short)(value >>> 16));
            this._setShort(index + 2, (short)value);
        }
        else {
            this._setShort(index, (short)value);
            this._setShort(index + 2, (short)(value >>> 16));
        }
    }
    
    @Override
    public CompositeByteBuf setLong(final int index, final long value) {
        return (CompositeByteBuf)super.setLong(index, value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        final Component c = this.findComponent(index);
        if (index + 8 <= c.endOffset) {
            c.buf.setLong(index - c.offset, value);
        }
        else if (this.order() == ByteOrder.BIG_ENDIAN) {
            this._setInt(index, (int)(value >>> 32));
            this._setInt(index + 4, (int)value);
        }
        else {
            this._setInt(index, (int)value);
            this._setInt(index + 4, (int)(value >>> 32));
        }
    }
    
    @Override
    public CompositeByteBuf setBytes(int index, final byte[] src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
        }
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(int index, final ByteBuffer src) {
        final int limit = src.limit();
        int length = src.remaining();
        this.checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        int i = this.toComponentIndex(index);
        try {
            while (length > 0) {
                final Component c = this.components.get(i);
                final ByteBuf s = c.buf;
                final int adjustment = c.offset;
                final int localLength = Math.min(length, s.capacity() - (index - adjustment));
                src.limit(src.position() + localLength);
                s.setBytes(index - adjustment, src);
                index += localLength;
                length -= localLength;
                ++i;
            }
        }
        finally {
            src.limit(limit);
        }
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(int index, final ByteBuf src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (length == 0) {
            return this;
        }
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.setBytes(index - adjustment, src, srcIndex, localLength);
            index += localLength;
            srcIndex += localLength;
        }
        return this;
    }
    
    @Override
    public int setBytes(int index, final InputStream in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(EmptyArrays.EMPTY_BYTES);
        }
        int i = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            final int localLength = Math.min(length, s.capacity() - (index - adjustment));
            final int localReadBytes = s.setBytes(index - adjustment, in, localLength);
            if (localReadBytes < 0) {
                if (readBytes == 0) {
                    return -1;
                }
                break;
            }
            else if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                ++i;
            }
            else {
                index += localReadBytes;
                length -= localReadBytes;
                readBytes += localReadBytes;
            }
        } while (length > 0);
        return readBytes;
    }
    
    @Override
    public int setBytes(int index, final ScatteringByteChannel in, int length) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return in.read(CompositeByteBuf.FULL_BYTEBUFFER);
        }
        int i = this.toComponentIndex(index);
        int readBytes = 0;
        do {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            final int localLength = Math.min(length, s.capacity() - (index - adjustment));
            final int localReadBytes = s.setBytes(index - adjustment, in, localLength);
            if (localReadBytes == 0) {
                break;
            }
            if (localReadBytes < 0) {
                if (readBytes == 0) {
                    return -1;
                }
                break;
            }
            else if (localReadBytes == localLength) {
                index += localLength;
                length -= localLength;
                readBytes += localLength;
                ++i;
            }
            else {
                index += localReadBytes;
                length -= localReadBytes;
                readBytes += localReadBytes;
            }
        } while (length > 0);
        return readBytes;
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf dst = Unpooled.buffer(length);
        if (length != 0) {
            this.copyTo(index, length, this.toComponentIndex(index), dst);
        }
        return dst;
    }
    
    private void copyTo(int index, int length, final int componentId, final ByteBuf dst) {
        int dstIndex = 0;
        int localLength;
        for (int i = componentId; length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
        }
        dst.writerIndex(dst.capacity());
    }
    
    public ByteBuf component(final int cIndex) {
        return this.internalComponent(cIndex).duplicate();
    }
    
    public ByteBuf componentAtOffset(final int offset) {
        return this.internalComponentAtOffset(offset).duplicate();
    }
    
    public ByteBuf internalComponent(final int cIndex) {
        this.checkComponentIndex(cIndex);
        return this.components.get(cIndex).buf;
    }
    
    public ByteBuf internalComponentAtOffset(final int offset) {
        return this.findComponent(offset).buf;
    }
    
    private Component findComponent(final int offset) {
        this.checkIndex(offset);
        int low = 0;
        int high = this.components.size();
        while (low <= high) {
            final int mid = low + high >>> 1;
            final Component c = this.components.get(mid);
            if (offset >= c.endOffset) {
                low = mid + 1;
            }
            else {
                if (offset >= c.offset) {
                    return c;
                }
                high = mid - 1;
            }
        }
        throw new Error("should not reach here");
    }
    
    @Override
    public int nioBufferCount() {
        if (this.components.size() == 1) {
            return this.components.get(0).buf.nioBufferCount();
        }
        int count = 0;
        for (int componentsCount = this.components.size(), i = 0; i < componentsCount; ++i) {
            final Component c = this.components.get(i);
            count += c.buf.nioBufferCount();
        }
        return count;
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        if (this.components.size() == 1) {
            return this.components.get(0).buf.internalNioBuffer(index, length);
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        if (this.components.size() == 1) {
            final ByteBuf buf = this.components.get(0).buf;
            if (buf.nioBufferCount() == 1) {
                return this.components.get(0).buf.nioBuffer(index, length);
            }
        }
        final ByteBuffer merged = ByteBuffer.allocate(length).order(this.order());
        final ByteBuffer[] buffers = this.nioBuffers(index, length);
        for (int i = 0; i < buffers.length; ++i) {
            merged.put(buffers[i]);
        }
        merged.flip();
        return merged;
    }
    
    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        this.checkIndex(index, length);
        if (length == 0) {
            return EmptyArrays.EMPTY_BYTE_BUFFERS;
        }
        final List<ByteBuffer> buffers = new ArrayList<ByteBuffer>(this.components.size());
        int localLength;
        for (int i = this.toComponentIndex(index); length > 0; length -= localLength, ++i) {
            final Component c = this.components.get(i);
            final ByteBuf s = c.buf;
            final int adjustment = c.offset;
            localLength = Math.min(length, s.capacity() - (index - adjustment));
            switch (s.nioBufferCount()) {
                case 0: {
                    throw new UnsupportedOperationException();
                }
                case 1: {
                    buffers.add(s.nioBuffer(index - adjustment, localLength));
                    break;
                }
                default: {
                    Collections.addAll(buffers, s.nioBuffers(index - adjustment, localLength));
                    break;
                }
            }
            index += localLength;
        }
        return buffers.toArray(new ByteBuffer[buffers.size()]);
    }
    
    public CompositeByteBuf consolidate() {
        this.ensureAccessible();
        final int numComponents = this.numComponents();
        if (numComponents <= 1) {
            return this;
        }
        final Component last = this.components.get(numComponents - 1);
        final int capacity = last.endOffset;
        final ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i = 0; i < numComponents; ++i) {
            final Component c = this.components.get(i);
            final ByteBuf b = c.buf;
            consolidated.writeBytes(b);
            c.freeIfNecessary();
        }
        this.components.clear();
        this.components.add(new Component(consolidated));
        this.updateComponentOffsets(0);
        return this;
    }
    
    public CompositeByteBuf consolidate(final int cIndex, final int numComponents) {
        this.checkComponentIndex(cIndex, numComponents);
        if (numComponents <= 1) {
            return this;
        }
        final int endCIndex = cIndex + numComponents;
        final Component last = this.components.get(endCIndex - 1);
        final int capacity = last.endOffset - this.components.get(cIndex).offset;
        final ByteBuf consolidated = this.allocBuffer(capacity);
        for (int i = cIndex; i < endCIndex; ++i) {
            final Component c = this.components.get(i);
            final ByteBuf b = c.buf;
            consolidated.writeBytes(b);
            c.freeIfNecessary();
        }
        this.components.subList(cIndex + 1, endCIndex).clear();
        this.components.set(cIndex, new Component(consolidated));
        this.updateComponentOffsets(cIndex);
        return this;
    }
    
    public CompositeByteBuf discardReadComponents() {
        this.ensureAccessible();
        final int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        final int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (final Component c : this.components) {
                c.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        final int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i = 0; i < firstComponentId; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        this.components.subList(0, firstComponentId).clear();
        final Component first = this.components.get(0);
        final int offset = first.offset;
        this.updateComponentOffsets(0);
        this.setIndex(readerIndex - offset, writerIndex - offset);
        this.adjustMarkers(offset);
        return this;
    }
    
    @Override
    public CompositeByteBuf discardReadBytes() {
        this.ensureAccessible();
        final int readerIndex = this.readerIndex();
        if (readerIndex == 0) {
            return this;
        }
        final int writerIndex = this.writerIndex();
        if (readerIndex == writerIndex && writerIndex == this.capacity()) {
            for (final Component c : this.components) {
                c.freeIfNecessary();
            }
            this.components.clear();
            this.setIndex(0, 0);
            this.adjustMarkers(readerIndex);
            return this;
        }
        final int firstComponentId = this.toComponentIndex(readerIndex);
        for (int i = 0; i < firstComponentId; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        this.components.subList(0, firstComponentId).clear();
        Component c = this.components.get(0);
        final int adjustment = readerIndex - c.offset;
        if (adjustment == c.length) {
            this.components.remove(0);
        }
        else {
            final Component newC = new Component(c.buf.slice(adjustment, c.length - adjustment));
            this.components.set(0, newC);
        }
        this.updateComponentOffsets(0);
        this.setIndex(0, writerIndex - readerIndex);
        this.adjustMarkers(readerIndex);
        return this;
    }
    
    private ByteBuf allocBuffer(final int capacity) {
        if (this.direct) {
            return this.alloc().directBuffer(capacity);
        }
        return this.alloc().heapBuffer(capacity);
    }
    
    @Override
    public String toString() {
        String result = super.toString();
        result = result.substring(0, result.length() - 1);
        return result + ", components=" + this.components.size() + ')';
    }
    
    @Override
    public CompositeByteBuf readerIndex(final int readerIndex) {
        return (CompositeByteBuf)super.readerIndex(readerIndex);
    }
    
    @Override
    public CompositeByteBuf writerIndex(final int writerIndex) {
        return (CompositeByteBuf)super.writerIndex(writerIndex);
    }
    
    @Override
    public CompositeByteBuf setIndex(final int readerIndex, final int writerIndex) {
        return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
    }
    
    @Override
    public CompositeByteBuf clear() {
        return (CompositeByteBuf)super.clear();
    }
    
    @Override
    public CompositeByteBuf markReaderIndex() {
        return (CompositeByteBuf)super.markReaderIndex();
    }
    
    @Override
    public CompositeByteBuf resetReaderIndex() {
        return (CompositeByteBuf)super.resetReaderIndex();
    }
    
    @Override
    public CompositeByteBuf markWriterIndex() {
        return (CompositeByteBuf)super.markWriterIndex();
    }
    
    @Override
    public CompositeByteBuf resetWriterIndex() {
        return (CompositeByteBuf)super.resetWriterIndex();
    }
    
    @Override
    public CompositeByteBuf ensureWritable(final int minWritableBytes) {
        return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        return (CompositeByteBuf)super.getBytes(index, dst, length);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final byte[] dst) {
        return (CompositeByteBuf)super.getBytes(index, dst);
    }
    
    @Override
    public CompositeByteBuf setBoolean(final int index, final boolean value) {
        return (CompositeByteBuf)super.setBoolean(index, value);
    }
    
    @Override
    public CompositeByteBuf setChar(final int index, final int value) {
        return (CompositeByteBuf)super.setChar(index, value);
    }
    
    @Override
    public CompositeByteBuf setFloat(final int index, final float value) {
        return (CompositeByteBuf)super.setFloat(index, value);
    }
    
    @Override
    public CompositeByteBuf setDouble(final int index, final double value) {
        return (CompositeByteBuf)super.setDouble(index, value);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        return (CompositeByteBuf)super.setBytes(index, src, length);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final byte[] src) {
        return (CompositeByteBuf)super.setBytes(index, src);
    }
    
    @Override
    public CompositeByteBuf setZero(final int index, final int length) {
        return (CompositeByteBuf)super.setZero(index, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuffer dst) {
        return (CompositeByteBuf)super.readBytes(dst);
    }
    
    @Override
    public CompositeByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        return (CompositeByteBuf)super.readBytes(out, length);
    }
    
    @Override
    public CompositeByteBuf skipBytes(final int length) {
        return (CompositeByteBuf)super.skipBytes(length);
    }
    
    @Override
    public CompositeByteBuf writeBoolean(final boolean value) {
        return (CompositeByteBuf)super.writeBoolean(value);
    }
    
    @Override
    public CompositeByteBuf writeByte(final int value) {
        return (CompositeByteBuf)super.writeByte(value);
    }
    
    @Override
    public CompositeByteBuf writeShort(final int value) {
        return (CompositeByteBuf)super.writeShort(value);
    }
    
    @Override
    public CompositeByteBuf writeMedium(final int value) {
        return (CompositeByteBuf)super.writeMedium(value);
    }
    
    @Override
    public CompositeByteBuf writeInt(final int value) {
        return (CompositeByteBuf)super.writeInt(value);
    }
    
    @Override
    public CompositeByteBuf writeLong(final long value) {
        return (CompositeByteBuf)super.writeLong(value);
    }
    
    @Override
    public CompositeByteBuf writeChar(final int value) {
        return (CompositeByteBuf)super.writeChar(value);
    }
    
    @Override
    public CompositeByteBuf writeFloat(final float value) {
        return (CompositeByteBuf)super.writeFloat(value);
    }
    
    @Override
    public CompositeByteBuf writeDouble(final double value) {
        return (CompositeByteBuf)super.writeDouble(value);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuffer src) {
        return (CompositeByteBuf)super.writeBytes(src);
    }
    
    @Override
    public CompositeByteBuf writeZero(final int length) {
        return (CompositeByteBuf)super.writeZero(length);
    }
    
    @Override
    public CompositeByteBuf retain(final int increment) {
        return (CompositeByteBuf)super.retain(increment);
    }
    
    @Override
    public CompositeByteBuf retain() {
        return (CompositeByteBuf)super.retain();
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.nioBuffers(this.readerIndex(), this.readableBytes());
    }
    
    @Override
    public CompositeByteBuf discardSomeReadBytes() {
        return this.discardReadComponents();
    }
    
    @Override
    protected void deallocate() {
        if (this.freed) {
            return;
        }
        this.freed = true;
        for (int size = this.components.size(), i = 0; i < size; ++i) {
            this.components.get(i).freeIfNecessary();
        }
        if (this.leak != null) {
            this.leak.close();
        }
    }
    
    @Override
    public ByteBuf unwrap() {
        return null;
    }
    
    static {
        FULL_BYTEBUFFER = (ByteBuffer)ByteBuffer.allocate(1).position(1);
    }
    
    private static final class Component
    {
        final ByteBuf buf;
        final int length;
        int offset;
        int endOffset;
        
        Component(final ByteBuf buf) {
            this.buf = buf;
            this.length = buf.readableBytes();
        }
        
        void freeIfNecessary() {
            this.buf.release();
        }
    }
}
