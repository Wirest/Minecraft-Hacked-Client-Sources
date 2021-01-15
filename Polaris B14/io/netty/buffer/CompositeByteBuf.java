/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ResourceLeak;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompositeByteBuf
/*      */   extends AbstractReferenceCountedByteBuf
/*      */ {
/*   42 */   private static final ByteBuffer EMPTY_NIO_BUFFER = Unpooled.EMPTY_BUFFER.nioBuffer();
/*      */   
/*      */   private final ResourceLeak leak;
/*      */   private final ByteBufAllocator alloc;
/*      */   private final boolean direct;
/*   47 */   private final List<Component> components = new ArrayList();
/*      */   private final int maxNumComponents;
/*      */   private boolean freed;
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents)
/*      */   {
/*   53 */     super(Integer.MAX_VALUE);
/*   54 */     if (alloc == null) {
/*   55 */       throw new NullPointerException("alloc");
/*      */     }
/*   57 */     this.alloc = alloc;
/*   58 */     this.direct = direct;
/*   59 */     this.maxNumComponents = maxNumComponents;
/*   60 */     this.leak = leakDetector.open(this);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf... buffers) {
/*   64 */     super(Integer.MAX_VALUE);
/*   65 */     if (alloc == null) {
/*   66 */       throw new NullPointerException("alloc");
/*      */     }
/*   68 */     if (maxNumComponents < 2) {
/*   69 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */     
/*      */ 
/*   73 */     this.alloc = alloc;
/*   74 */     this.direct = direct;
/*   75 */     this.maxNumComponents = maxNumComponents;
/*      */     
/*   77 */     addComponents0(0, buffers);
/*   78 */     consolidateIfNeeded();
/*   79 */     setIndex(0, capacity());
/*   80 */     this.leak = leakDetector.open(this);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, Iterable<ByteBuf> buffers)
/*      */   {
/*   85 */     super(Integer.MAX_VALUE);
/*   86 */     if (alloc == null) {
/*   87 */       throw new NullPointerException("alloc");
/*      */     }
/*   89 */     if (maxNumComponents < 2) {
/*   90 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */     
/*      */ 
/*   94 */     this.alloc = alloc;
/*   95 */     this.direct = direct;
/*   96 */     this.maxNumComponents = maxNumComponents;
/*   97 */     addComponents0(0, buffers);
/*   98 */     consolidateIfNeeded();
/*   99 */     setIndex(0, capacity());
/*  100 */     this.leak = leakDetector.open(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponent(ByteBuf buffer)
/*      */   {
/*  112 */     addComponent0(this.components.size(), buffer);
/*  113 */     consolidateIfNeeded();
/*  114 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponents(ByteBuf... buffers)
/*      */   {
/*  126 */     addComponents0(this.components.size(), buffers);
/*  127 */     consolidateIfNeeded();
/*  128 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers)
/*      */   {
/*  140 */     addComponents0(this.components.size(), buffers);
/*  141 */     consolidateIfNeeded();
/*  142 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer)
/*      */   {
/*  155 */     addComponent0(cIndex, buffer);
/*  156 */     consolidateIfNeeded();
/*  157 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponent0(int cIndex, ByteBuf buffer) {
/*  161 */     checkComponentIndex(cIndex);
/*      */     
/*  163 */     if (buffer == null) {
/*  164 */       throw new NullPointerException("buffer");
/*      */     }
/*      */     
/*  167 */     int readableBytes = buffer.readableBytes();
/*      */     
/*      */ 
/*  170 */     Component c = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
/*  171 */     if (cIndex == this.components.size()) {
/*  172 */       this.components.add(c);
/*  173 */       if (cIndex == 0) {
/*  174 */         c.endOffset = readableBytes;
/*      */       } else {
/*  176 */         Component prev = (Component)this.components.get(cIndex - 1);
/*  177 */         c.offset = prev.endOffset;
/*  178 */         c.endOffset = (c.offset + readableBytes);
/*      */       }
/*      */     } else {
/*  181 */       this.components.add(cIndex, c);
/*  182 */       if (readableBytes != 0) {
/*  183 */         updateComponentOffsets(cIndex);
/*      */       }
/*      */     }
/*  186 */     return cIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers)
/*      */   {
/*  199 */     addComponents0(cIndex, buffers);
/*  200 */     consolidateIfNeeded();
/*  201 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(int cIndex, ByteBuf... buffers) {
/*  205 */     checkComponentIndex(cIndex);
/*      */     
/*  207 */     if (buffers == null) {
/*  208 */       throw new NullPointerException("buffers");
/*      */     }
/*      */     
/*      */ 
/*  212 */     for (ByteBuf b : buffers) {
/*  213 */       if (b == null) {
/*      */         break;
/*      */       }
/*  216 */       cIndex = addComponent0(cIndex, b) + 1;
/*  217 */       int size = this.components.size();
/*  218 */       if (cIndex > size) {
/*  219 */         cIndex = size;
/*      */       }
/*      */     }
/*  222 */     return cIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers)
/*      */   {
/*  235 */     addComponents0(cIndex, buffers);
/*  236 */     consolidateIfNeeded();
/*  237 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(int cIndex, Iterable<ByteBuf> buffers) {
/*  241 */     if (buffers == null) {
/*  242 */       throw new NullPointerException("buffers");
/*      */     }
/*      */     
/*  245 */     if ((buffers instanceof ByteBuf))
/*      */     {
/*  247 */       return addComponent0(cIndex, (ByteBuf)buffers);
/*      */     }
/*      */     
/*  250 */     if (!(buffers instanceof Collection)) {
/*  251 */       List<ByteBuf> list = new ArrayList();
/*  252 */       for (ByteBuf b : buffers) {
/*  253 */         list.add(b);
/*      */       }
/*  255 */       buffers = list;
/*      */     }
/*      */     
/*  258 */     Collection<ByteBuf> col = (Collection)buffers;
/*  259 */     return addComponents0(cIndex, (ByteBuf[])col.toArray(new ByteBuf[col.size()]));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void consolidateIfNeeded()
/*      */   {
/*  269 */     int numComponents = this.components.size();
/*  270 */     if (numComponents > this.maxNumComponents) {
/*  271 */       int capacity = ((Component)this.components.get(numComponents - 1)).endOffset;
/*      */       
/*  273 */       ByteBuf consolidated = allocBuffer(capacity);
/*      */       
/*      */ 
/*  276 */       for (int i = 0; i < numComponents; i++) {
/*  277 */         Component c = (Component)this.components.get(i);
/*  278 */         ByteBuf b = c.buf;
/*  279 */         consolidated.writeBytes(b);
/*  280 */         c.freeIfNecessary();
/*      */       }
/*  282 */       Component c = new Component(consolidated);
/*  283 */       c.endOffset = c.length;
/*  284 */       this.components.clear();
/*  285 */       this.components.add(c);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkComponentIndex(int cIndex) {
/*  290 */     ensureAccessible();
/*  291 */     if ((cIndex < 0) || (cIndex > this.components.size())) {
/*  292 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", new Object[] { Integer.valueOf(cIndex), Integer.valueOf(this.components.size()) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void checkComponentIndex(int cIndex, int numComponents)
/*      */   {
/*  299 */     ensureAccessible();
/*  300 */     if ((cIndex < 0) || (cIndex + numComponents > this.components.size())) {
/*  301 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", new Object[] { Integer.valueOf(cIndex), Integer.valueOf(numComponents), Integer.valueOf(this.components.size()) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateComponentOffsets(int cIndex)
/*      */   {
/*  309 */     int size = this.components.size();
/*  310 */     if (size <= cIndex) {
/*  311 */       return;
/*      */     }
/*      */     
/*  314 */     Component c = (Component)this.components.get(cIndex);
/*  315 */     if (cIndex == 0) {
/*  316 */       c.offset = 0;
/*  317 */       c.endOffset = c.length;
/*  318 */       cIndex++;
/*      */     }
/*      */     
/*  321 */     for (int i = cIndex; i < size; i++) {
/*  322 */       Component prev = (Component)this.components.get(i - 1);
/*  323 */       Component cur = (Component)this.components.get(i);
/*  324 */       cur.offset = prev.endOffset;
/*  325 */       cur.endOffset = (cur.offset + cur.length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf removeComponent(int cIndex)
/*      */   {
/*  335 */     checkComponentIndex(cIndex);
/*  336 */     Component comp = (Component)this.components.remove(cIndex);
/*  337 */     comp.freeIfNecessary();
/*  338 */     if (comp.length > 0)
/*      */     {
/*  340 */       updateComponentOffsets(cIndex);
/*      */     }
/*  342 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf removeComponents(int cIndex, int numComponents)
/*      */   {
/*  352 */     checkComponentIndex(cIndex, numComponents);
/*      */     
/*  354 */     if (numComponents == 0) {
/*  355 */       return this;
/*      */     }
/*  357 */     List<Component> toRemove = this.components.subList(cIndex, cIndex + numComponents);
/*  358 */     boolean needsUpdate = false;
/*  359 */     for (Component c : toRemove) {
/*  360 */       if (c.length > 0) {
/*  361 */         needsUpdate = true;
/*      */       }
/*  363 */       c.freeIfNecessary();
/*      */     }
/*  365 */     toRemove.clear();
/*      */     
/*  367 */     if (needsUpdate)
/*      */     {
/*  369 */       updateComponentOffsets(cIndex);
/*      */     }
/*  371 */     return this;
/*      */   }
/*      */   
/*      */   public Iterator<ByteBuf> iterator() {
/*  375 */     ensureAccessible();
/*  376 */     List<ByteBuf> list = new ArrayList(this.components.size());
/*  377 */     for (Component c : this.components) {
/*  378 */       list.add(c.buf);
/*      */     }
/*  380 */     return list.iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ByteBuf> decompose(int offset, int length)
/*      */   {
/*  387 */     checkIndex(offset, length);
/*  388 */     if (length == 0) {
/*  389 */       return Collections.emptyList();
/*      */     }
/*      */     
/*  392 */     int componentId = toComponentIndex(offset);
/*  393 */     List<ByteBuf> slice = new ArrayList(this.components.size());
/*      */     
/*      */ 
/*  396 */     Component firstC = (Component)this.components.get(componentId);
/*  397 */     ByteBuf first = firstC.buf.duplicate();
/*  398 */     first.readerIndex(offset - firstC.offset);
/*      */     
/*  400 */     ByteBuf buf = first;
/*  401 */     int bytesToSlice = length;
/*      */     do {
/*  403 */       int readableBytes = buf.readableBytes();
/*  404 */       if (bytesToSlice <= readableBytes)
/*      */       {
/*  406 */         buf.writerIndex(buf.readerIndex() + bytesToSlice);
/*  407 */         slice.add(buf);
/*  408 */         break;
/*      */       }
/*      */       
/*  411 */       slice.add(buf);
/*  412 */       bytesToSlice -= readableBytes;
/*  413 */       componentId++;
/*      */       
/*      */ 
/*  416 */       buf = ((Component)this.components.get(componentId)).buf.duplicate();
/*      */     }
/*  418 */     while (bytesToSlice > 0);
/*      */     
/*      */ 
/*  421 */     for (int i = 0; i < slice.size(); i++) {
/*  422 */       slice.set(i, ((ByteBuf)slice.get(i)).slice());
/*      */     }
/*      */     
/*  425 */     return slice;
/*      */   }
/*      */   
/*      */   public boolean isDirect()
/*      */   {
/*  430 */     int size = this.components.size();
/*  431 */     if (size == 0) {
/*  432 */       return false;
/*      */     }
/*  434 */     for (int i = 0; i < size; i++) {
/*  435 */       if (!((Component)this.components.get(i)).buf.isDirect()) {
/*  436 */         return false;
/*      */       }
/*      */     }
/*  439 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasArray()
/*      */   {
/*  444 */     switch (this.components.size()) {
/*      */     case 0: 
/*  446 */       return true;
/*      */     case 1: 
/*  448 */       return ((Component)this.components.get(0)).buf.hasArray();
/*      */     }
/*  450 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte[] array()
/*      */   {
/*  456 */     switch (this.components.size()) {
/*      */     case 0: 
/*  458 */       return EmptyArrays.EMPTY_BYTES;
/*      */     case 1: 
/*  460 */       return ((Component)this.components.get(0)).buf.array();
/*      */     }
/*  462 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public int arrayOffset()
/*      */   {
/*  468 */     switch (this.components.size()) {
/*      */     case 0: 
/*  470 */       return 0;
/*      */     case 1: 
/*  472 */       return ((Component)this.components.get(0)).buf.arrayOffset();
/*      */     }
/*  474 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasMemoryAddress()
/*      */   {
/*  480 */     switch (this.components.size()) {
/*      */     case 0: 
/*  482 */       return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
/*      */     case 1: 
/*  484 */       return ((Component)this.components.get(0)).buf.hasMemoryAddress();
/*      */     }
/*  486 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public long memoryAddress()
/*      */   {
/*  492 */     switch (this.components.size()) {
/*      */     case 0: 
/*  494 */       return Unpooled.EMPTY_BUFFER.memoryAddress();
/*      */     case 1: 
/*  496 */       return ((Component)this.components.get(0)).buf.memoryAddress();
/*      */     }
/*  498 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public int capacity()
/*      */   {
/*  504 */     int numComponents = this.components.size();
/*  505 */     if (numComponents == 0) {
/*  506 */       return 0;
/*      */     }
/*  508 */     return ((Component)this.components.get(numComponents - 1)).endOffset;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf capacity(int newCapacity)
/*      */   {
/*  513 */     ensureAccessible();
/*  514 */     if ((newCapacity < 0) || (newCapacity > maxCapacity())) {
/*  515 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*      */     }
/*      */     
/*  518 */     int oldCapacity = capacity();
/*  519 */     if (newCapacity > oldCapacity) {
/*  520 */       int paddingLength = newCapacity - oldCapacity;
/*      */       
/*  522 */       int nComponents = this.components.size();
/*  523 */       if (nComponents < this.maxNumComponents) {
/*  524 */         ByteBuf padding = allocBuffer(paddingLength);
/*  525 */         padding.setIndex(0, paddingLength);
/*  526 */         addComponent0(this.components.size(), padding);
/*      */       } else {
/*  528 */         ByteBuf padding = allocBuffer(paddingLength);
/*  529 */         padding.setIndex(0, paddingLength);
/*      */         
/*      */ 
/*  532 */         addComponent0(this.components.size(), padding);
/*  533 */         consolidateIfNeeded();
/*      */       }
/*  535 */     } else if (newCapacity < oldCapacity) {
/*  536 */       int bytesToTrim = oldCapacity - newCapacity;
/*  537 */       for (ListIterator<Component> i = this.components.listIterator(this.components.size()); i.hasPrevious();) {
/*  538 */         Component c = (Component)i.previous();
/*  539 */         if (bytesToTrim >= c.length) {
/*  540 */           bytesToTrim -= c.length;
/*  541 */           i.remove();
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  546 */           Component newC = new Component(c.buf.slice(0, c.length - bytesToTrim));
/*  547 */           newC.offset = c.offset;
/*  548 */           newC.endOffset = (newC.offset + newC.length);
/*  549 */           i.set(newC);
/*      */         }
/*      */       }
/*      */       
/*  553 */       if (readerIndex() > newCapacity) {
/*  554 */         setIndex(newCapacity, newCapacity);
/*  555 */       } else if (writerIndex() > newCapacity) {
/*  556 */         writerIndex(newCapacity);
/*      */       }
/*      */     }
/*  559 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*  564 */     return this.alloc;
/*      */   }
/*      */   
/*      */   public ByteOrder order()
/*      */   {
/*  569 */     return ByteOrder.BIG_ENDIAN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int numComponents()
/*      */   {
/*  576 */     return this.components.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int maxNumComponents()
/*      */   {
/*  583 */     return this.maxNumComponents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int toComponentIndex(int offset)
/*      */   {
/*  590 */     checkIndex(offset);
/*      */     
/*  592 */     int low = 0; for (int high = this.components.size(); low <= high;) {
/*  593 */       int mid = low + high >>> 1;
/*  594 */       Component c = (Component)this.components.get(mid);
/*  595 */       if (offset >= c.endOffset) {
/*  596 */         low = mid + 1;
/*  597 */       } else if (offset < c.offset) {
/*  598 */         high = mid - 1;
/*      */       } else {
/*  600 */         return mid;
/*      */       }
/*      */     }
/*      */     
/*  604 */     throw new Error("should not reach here");
/*      */   }
/*      */   
/*      */   public int toByteIndex(int cIndex) {
/*  608 */     checkComponentIndex(cIndex);
/*  609 */     return ((Component)this.components.get(cIndex)).offset;
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  614 */     return _getByte(index);
/*      */   }
/*      */   
/*      */   protected byte _getByte(int index)
/*      */   {
/*  619 */     Component c = findComponent(index);
/*  620 */     return c.buf.getByte(index - c.offset);
/*      */   }
/*      */   
/*      */   protected short _getShort(int index)
/*      */   {
/*  625 */     Component c = findComponent(index);
/*  626 */     if (index + 2 <= c.endOffset)
/*  627 */       return c.buf.getShort(index - c.offset);
/*  628 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  629 */       return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*      */     }
/*  631 */     return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getUnsignedMedium(int index)
/*      */   {
/*  637 */     Component c = findComponent(index);
/*  638 */     if (index + 3 <= c.endOffset)
/*  639 */       return c.buf.getUnsignedMedium(index - c.offset);
/*  640 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  641 */       return (_getShort(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*      */     }
/*  643 */     return _getShort(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getInt(int index)
/*      */   {
/*  649 */     Component c = findComponent(index);
/*  650 */     if (index + 4 <= c.endOffset)
/*  651 */       return c.buf.getInt(index - c.offset);
/*  652 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  653 */       return (_getShort(index) & 0xFFFF) << 16 | _getShort(index + 2) & 0xFFFF;
/*      */     }
/*  655 */     return _getShort(index) & 0xFFFF | (_getShort(index + 2) & 0xFFFF) << 16;
/*      */   }
/*      */   
/*      */ 
/*      */   protected long _getLong(int index)
/*      */   {
/*  661 */     Component c = findComponent(index);
/*  662 */     if (index + 8 <= c.endOffset)
/*  663 */       return c.buf.getLong(index - c.offset);
/*  664 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  665 */       return (_getInt(index) & 0xFFFFFFFF) << 32 | _getInt(index + 4) & 0xFFFFFFFF;
/*      */     }
/*  667 */     return _getInt(index) & 0xFFFFFFFF | (_getInt(index + 4) & 0xFFFFFFFF) << 32;
/*      */   }
/*      */   
/*      */ 
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*      */   {
/*  673 */     checkDstIndex(index, length, dstIndex, dst.length);
/*  674 */     if (length == 0) {
/*  675 */       return this;
/*      */     }
/*      */     
/*  678 */     int i = toComponentIndex(index);
/*  679 */     while (length > 0) {
/*  680 */       Component c = (Component)this.components.get(i);
/*  681 */       ByteBuf s = c.buf;
/*  682 */       int adjustment = c.offset;
/*  683 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  684 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  685 */       index += localLength;
/*  686 */       dstIndex += localLength;
/*  687 */       length -= localLength;
/*  688 */       i++;
/*      */     }
/*  690 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuffer dst)
/*      */   {
/*  695 */     int limit = dst.limit();
/*  696 */     int length = dst.remaining();
/*      */     
/*  698 */     checkIndex(index, length);
/*  699 */     if (length == 0) {
/*  700 */       return this;
/*      */     }
/*      */     
/*  703 */     int i = toComponentIndex(index);
/*      */     try {
/*  705 */       while (length > 0) {
/*  706 */         Component c = (Component)this.components.get(i);
/*  707 */         ByteBuf s = c.buf;
/*  708 */         int adjustment = c.offset;
/*  709 */         int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  710 */         dst.limit(dst.position() + localLength);
/*  711 */         s.getBytes(index - adjustment, dst);
/*  712 */         index += localLength;
/*  713 */         length -= localLength;
/*  714 */         i++;
/*      */       }
/*      */     } finally {
/*  717 */       dst.limit(limit);
/*      */     }
/*  719 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  724 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/*  725 */     if (length == 0) {
/*  726 */       return this;
/*      */     }
/*      */     
/*  729 */     int i = toComponentIndex(index);
/*  730 */     while (length > 0) {
/*  731 */       Component c = (Component)this.components.get(i);
/*  732 */       ByteBuf s = c.buf;
/*  733 */       int adjustment = c.offset;
/*  734 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  735 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  736 */       index += localLength;
/*  737 */       dstIndex += localLength;
/*  738 */       length -= localLength;
/*  739 */       i++;
/*      */     }
/*  741 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length)
/*      */     throws IOException
/*      */   {
/*  747 */     int count = nioBufferCount();
/*  748 */     if (count == 1) {
/*  749 */       return out.write(internalNioBuffer(index, length));
/*      */     }
/*  751 */     long writtenBytes = out.write(nioBuffers(index, length));
/*  752 */     if (writtenBytes > 2147483647L) {
/*  753 */       return Integer.MAX_VALUE;
/*      */     }
/*  755 */     return (int)writtenBytes;
/*      */   }
/*      */   
/*      */ 
/*      */   public CompositeByteBuf getBytes(int index, OutputStream out, int length)
/*      */     throws IOException
/*      */   {
/*  762 */     checkIndex(index, length);
/*  763 */     if (length == 0) {
/*  764 */       return this;
/*      */     }
/*      */     
/*  767 */     int i = toComponentIndex(index);
/*  768 */     while (length > 0) {
/*  769 */       Component c = (Component)this.components.get(i);
/*  770 */       ByteBuf s = c.buf;
/*  771 */       int adjustment = c.offset;
/*  772 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  773 */       s.getBytes(index - adjustment, out, localLength);
/*  774 */       index += localLength;
/*  775 */       length -= localLength;
/*  776 */       i++;
/*      */     }
/*  778 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setByte(int index, int value)
/*      */   {
/*  783 */     Component c = findComponent(index);
/*  784 */     c.buf.setByte(index - c.offset, value);
/*  785 */     return this;
/*      */   }
/*      */   
/*      */   protected void _setByte(int index, int value)
/*      */   {
/*  790 */     setByte(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setShort(int index, int value)
/*      */   {
/*  795 */     return (CompositeByteBuf)super.setShort(index, value);
/*      */   }
/*      */   
/*      */   protected void _setShort(int index, int value)
/*      */   {
/*  800 */     Component c = findComponent(index);
/*  801 */     if (index + 2 <= c.endOffset) {
/*  802 */       c.buf.setShort(index - c.offset, value);
/*  803 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/*  804 */       _setByte(index, (byte)(value >>> 8));
/*  805 */       _setByte(index + 1, (byte)value);
/*      */     } else {
/*  807 */       _setByte(index, (byte)value);
/*  808 */       _setByte(index + 1, (byte)(value >>> 8));
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setMedium(int index, int value)
/*      */   {
/*  814 */     return (CompositeByteBuf)super.setMedium(index, value);
/*      */   }
/*      */   
/*      */   protected void _setMedium(int index, int value)
/*      */   {
/*  819 */     Component c = findComponent(index);
/*  820 */     if (index + 3 <= c.endOffset) {
/*  821 */       c.buf.setMedium(index - c.offset, value);
/*  822 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/*  823 */       _setShort(index, (short)(value >> 8));
/*  824 */       _setByte(index + 2, (byte)value);
/*      */     } else {
/*  826 */       _setShort(index, (short)value);
/*  827 */       _setByte(index + 2, (byte)(value >>> 16));
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setInt(int index, int value)
/*      */   {
/*  833 */     return (CompositeByteBuf)super.setInt(index, value);
/*      */   }
/*      */   
/*      */   protected void _setInt(int index, int value)
/*      */   {
/*  838 */     Component c = findComponent(index);
/*  839 */     if (index + 4 <= c.endOffset) {
/*  840 */       c.buf.setInt(index - c.offset, value);
/*  841 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/*  842 */       _setShort(index, (short)(value >>> 16));
/*  843 */       _setShort(index + 2, (short)value);
/*      */     } else {
/*  845 */       _setShort(index, (short)value);
/*  846 */       _setShort(index + 2, (short)(value >>> 16));
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setLong(int index, long value)
/*      */   {
/*  852 */     return (CompositeByteBuf)super.setLong(index, value);
/*      */   }
/*      */   
/*      */   protected void _setLong(int index, long value)
/*      */   {
/*  857 */     Component c = findComponent(index);
/*  858 */     if (index + 8 <= c.endOffset) {
/*  859 */       c.buf.setLong(index - c.offset, value);
/*  860 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/*  861 */       _setInt(index, (int)(value >>> 32));
/*  862 */       _setInt(index + 4, (int)value);
/*      */     } else {
/*  864 */       _setInt(index, (int)value);
/*  865 */       _setInt(index + 4, (int)(value >>> 32));
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*      */   {
/*  871 */     checkSrcIndex(index, length, srcIndex, src.length);
/*  872 */     if (length == 0) {
/*  873 */       return this;
/*      */     }
/*      */     
/*  876 */     int i = toComponentIndex(index);
/*  877 */     while (length > 0) {
/*  878 */       Component c = (Component)this.components.get(i);
/*  879 */       ByteBuf s = c.buf;
/*  880 */       int adjustment = c.offset;
/*  881 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  882 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/*  883 */       index += localLength;
/*  884 */       srcIndex += localLength;
/*  885 */       length -= localLength;
/*  886 */       i++;
/*      */     }
/*  888 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuffer src)
/*      */   {
/*  893 */     int limit = src.limit();
/*  894 */     int length = src.remaining();
/*      */     
/*  896 */     checkIndex(index, length);
/*  897 */     if (length == 0) {
/*  898 */       return this;
/*      */     }
/*      */     
/*  901 */     int i = toComponentIndex(index);
/*      */     try {
/*  903 */       while (length > 0) {
/*  904 */         Component c = (Component)this.components.get(i);
/*  905 */         ByteBuf s = c.buf;
/*  906 */         int adjustment = c.offset;
/*  907 */         int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  908 */         src.limit(src.position() + localLength);
/*  909 */         s.setBytes(index - adjustment, src);
/*  910 */         index += localLength;
/*  911 */         length -= localLength;
/*  912 */         i++;
/*      */       }
/*      */     } finally {
/*  915 */       src.limit(limit);
/*      */     }
/*  917 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*      */   {
/*  922 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/*  923 */     if (length == 0) {
/*  924 */       return this;
/*      */     }
/*      */     
/*  927 */     int i = toComponentIndex(index);
/*  928 */     while (length > 0) {
/*  929 */       Component c = (Component)this.components.get(i);
/*  930 */       ByteBuf s = c.buf;
/*  931 */       int adjustment = c.offset;
/*  932 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  933 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/*  934 */       index += localLength;
/*  935 */       srcIndex += localLength;
/*  936 */       length -= localLength;
/*  937 */       i++;
/*      */     }
/*  939 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException
/*      */   {
/*  944 */     checkIndex(index, length);
/*  945 */     if (length == 0) {
/*  946 */       return in.read(EmptyArrays.EMPTY_BYTES);
/*      */     }
/*      */     
/*  949 */     int i = toComponentIndex(index);
/*  950 */     int readBytes = 0;
/*      */     do
/*      */     {
/*  953 */       Component c = (Component)this.components.get(i);
/*  954 */       ByteBuf s = c.buf;
/*  955 */       int adjustment = c.offset;
/*  956 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  957 */       int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/*  958 */       if (localReadBytes < 0) {
/*  959 */         if (readBytes != 0) break;
/*  960 */         return -1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  966 */       if (localReadBytes == localLength) {
/*  967 */         index += localLength;
/*  968 */         length -= localLength;
/*  969 */         readBytes += localLength;
/*  970 */         i++;
/*      */       } else {
/*  972 */         index += localReadBytes;
/*  973 */         length -= localReadBytes;
/*  974 */         readBytes += localReadBytes;
/*      */       }
/*  976 */     } while (length > 0);
/*      */     
/*  978 */     return readBytes;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*      */   {
/*  983 */     checkIndex(index, length);
/*  984 */     if (length == 0) {
/*  985 */       return in.read(EMPTY_NIO_BUFFER);
/*      */     }
/*      */     
/*  988 */     int i = toComponentIndex(index);
/*  989 */     int readBytes = 0;
/*      */     do {
/*  991 */       Component c = (Component)this.components.get(i);
/*  992 */       ByteBuf s = c.buf;
/*  993 */       int adjustment = c.offset;
/*  994 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  995 */       int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/*      */       
/*  997 */       if (localReadBytes == 0) {
/*      */         break;
/*      */       }
/*      */       
/* 1001 */       if (localReadBytes < 0) {
/* 1002 */         if (readBytes != 0) break;
/* 1003 */         return -1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1009 */       if (localReadBytes == localLength) {
/* 1010 */         index += localLength;
/* 1011 */         length -= localLength;
/* 1012 */         readBytes += localLength;
/* 1013 */         i++;
/*      */       } else {
/* 1015 */         index += localReadBytes;
/* 1016 */         length -= localReadBytes;
/* 1017 */         readBytes += localReadBytes;
/*      */       }
/* 1019 */     } while (length > 0);
/*      */     
/* 1021 */     return readBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf copy(int index, int length)
/*      */   {
/* 1026 */     checkIndex(index, length);
/* 1027 */     ByteBuf dst = Unpooled.buffer(length);
/* 1028 */     if (length != 0) {
/* 1029 */       copyTo(index, length, toComponentIndex(index), dst);
/*      */     }
/* 1031 */     return dst;
/*      */   }
/*      */   
/*      */   private void copyTo(int index, int length, int componentId, ByteBuf dst) {
/* 1035 */     int dstIndex = 0;
/* 1036 */     int i = componentId;
/*      */     
/* 1038 */     while (length > 0) {
/* 1039 */       Component c = (Component)this.components.get(i);
/* 1040 */       ByteBuf s = c.buf;
/* 1041 */       int adjustment = c.offset;
/* 1042 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1043 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 1044 */       index += localLength;
/* 1045 */       dstIndex += localLength;
/* 1046 */       length -= localLength;
/* 1047 */       i++;
/*      */     }
/*      */     
/* 1050 */     dst.writerIndex(dst.capacity());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf component(int cIndex)
/*      */   {
/* 1060 */     return internalComponent(cIndex).duplicate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf componentAtOffset(int offset)
/*      */   {
/* 1070 */     return internalComponentAtOffset(offset).duplicate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf internalComponent(int cIndex)
/*      */   {
/* 1080 */     checkComponentIndex(cIndex);
/* 1081 */     return ((Component)this.components.get(cIndex)).buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf internalComponentAtOffset(int offset)
/*      */   {
/* 1091 */     return findComponent(offset).buf;
/*      */   }
/*      */   
/*      */   private Component findComponent(int offset) {
/* 1095 */     checkIndex(offset);
/*      */     
/* 1097 */     int low = 0; for (int high = this.components.size(); low <= high;) {
/* 1098 */       int mid = low + high >>> 1;
/* 1099 */       Component c = (Component)this.components.get(mid);
/* 1100 */       if (offset >= c.endOffset) {
/* 1101 */         low = mid + 1;
/* 1102 */       } else if (offset < c.offset) {
/* 1103 */         high = mid - 1;
/*      */       } else {
/* 1105 */         assert (c.length != 0);
/* 1106 */         return c;
/*      */       }
/*      */     }
/*      */     
/* 1110 */     throw new Error("should not reach here");
/*      */   }
/*      */   
/*      */   public int nioBufferCount()
/*      */   {
/* 1115 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1117 */       return 1;
/*      */     case 1: 
/* 1119 */       return ((Component)this.components.get(0)).buf.nioBufferCount();
/*      */     }
/* 1121 */     int count = 0;
/* 1122 */     int componentsCount = this.components.size();
/* 1123 */     for (int i = 0; i < componentsCount; i++) {
/* 1124 */       Component c = (Component)this.components.get(i);
/* 1125 */       count += c.buf.nioBufferCount();
/*      */     }
/* 1127 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer internalNioBuffer(int index, int length)
/*      */   {
/* 1133 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1135 */       return EMPTY_NIO_BUFFER;
/*      */     case 1: 
/* 1137 */       return ((Component)this.components.get(0)).buf.internalNioBuffer(index, length);
/*      */     }
/* 1139 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer nioBuffer(int index, int length)
/*      */   {
/* 1145 */     checkIndex(index, length);
/*      */     
/* 1147 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1149 */       return EMPTY_NIO_BUFFER;
/*      */     case 1: 
/* 1151 */       ByteBuf buf = ((Component)this.components.get(0)).buf;
/* 1152 */       if (buf.nioBufferCount() == 1) {
/* 1153 */         return ((Component)this.components.get(0)).buf.nioBuffer(index, length);
/*      */       }
/*      */       break;
/*      */     }
/* 1157 */     ByteBuffer merged = ByteBuffer.allocate(length).order(order());
/* 1158 */     ByteBuffer[] buffers = nioBuffers(index, length);
/*      */     
/* 1160 */     for (ByteBuffer buf : buffers) {
/* 1161 */       merged.put(buf);
/*      */     }
/*      */     
/* 1164 */     merged.flip();
/* 1165 */     return merged;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length)
/*      */   {
/* 1170 */     checkIndex(index, length);
/* 1171 */     if (length == 0) {
/* 1172 */       return new ByteBuffer[] { EMPTY_NIO_BUFFER };
/*      */     }
/*      */     
/* 1175 */     List<ByteBuffer> buffers = new ArrayList(this.components.size());
/* 1176 */     int i = toComponentIndex(index);
/* 1177 */     while (length > 0) {
/* 1178 */       Component c = (Component)this.components.get(i);
/* 1179 */       ByteBuf s = c.buf;
/* 1180 */       int adjustment = c.offset;
/* 1181 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1182 */       switch (s.nioBufferCount()) {
/*      */       case 0: 
/* 1184 */         throw new UnsupportedOperationException();
/*      */       case 1: 
/* 1186 */         buffers.add(s.nioBuffer(index - adjustment, localLength));
/* 1187 */         break;
/*      */       default: 
/* 1189 */         Collections.addAll(buffers, s.nioBuffers(index - adjustment, localLength));
/*      */       }
/*      */       
/* 1192 */       index += localLength;
/* 1193 */       length -= localLength;
/* 1194 */       i++;
/*      */     }
/*      */     
/* 1197 */     return (ByteBuffer[])buffers.toArray(new ByteBuffer[buffers.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf consolidate()
/*      */   {
/* 1204 */     ensureAccessible();
/* 1205 */     int numComponents = numComponents();
/* 1206 */     if (numComponents <= 1) {
/* 1207 */       return this;
/*      */     }
/*      */     
/* 1210 */     Component last = (Component)this.components.get(numComponents - 1);
/* 1211 */     int capacity = last.endOffset;
/* 1212 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1214 */     for (int i = 0; i < numComponents; i++) {
/* 1215 */       Component c = (Component)this.components.get(i);
/* 1216 */       ByteBuf b = c.buf;
/* 1217 */       consolidated.writeBytes(b);
/* 1218 */       c.freeIfNecessary();
/*      */     }
/*      */     
/* 1221 */     this.components.clear();
/* 1222 */     this.components.add(new Component(consolidated));
/* 1223 */     updateComponentOffsets(0);
/* 1224 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf consolidate(int cIndex, int numComponents)
/*      */   {
/* 1234 */     checkComponentIndex(cIndex, numComponents);
/* 1235 */     if (numComponents <= 1) {
/* 1236 */       return this;
/*      */     }
/*      */     
/* 1239 */     int endCIndex = cIndex + numComponents;
/* 1240 */     Component last = (Component)this.components.get(endCIndex - 1);
/* 1241 */     int capacity = last.endOffset - ((Component)this.components.get(cIndex)).offset;
/* 1242 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1244 */     for (int i = cIndex; i < endCIndex; i++) {
/* 1245 */       Component c = (Component)this.components.get(i);
/* 1246 */       ByteBuf b = c.buf;
/* 1247 */       consolidated.writeBytes(b);
/* 1248 */       c.freeIfNecessary();
/*      */     }
/*      */     
/* 1251 */     this.components.subList(cIndex + 1, endCIndex).clear();
/* 1252 */     this.components.set(cIndex, new Component(consolidated));
/* 1253 */     updateComponentOffsets(cIndex);
/* 1254 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf discardReadComponents()
/*      */   {
/* 1261 */     ensureAccessible();
/* 1262 */     int readerIndex = readerIndex();
/* 1263 */     if (readerIndex == 0) {
/* 1264 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1268 */     int writerIndex = writerIndex();
/* 1269 */     if ((readerIndex == writerIndex) && (writerIndex == capacity())) {
/* 1270 */       for (Component c : this.components) {
/* 1271 */         c.freeIfNecessary();
/*      */       }
/* 1273 */       this.components.clear();
/* 1274 */       setIndex(0, 0);
/* 1275 */       adjustMarkers(readerIndex);
/* 1276 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1280 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1281 */     for (int i = 0; i < firstComponentId; i++) {
/* 1282 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/* 1284 */     this.components.subList(0, firstComponentId).clear();
/*      */     
/*      */ 
/* 1287 */     Component first = (Component)this.components.get(0);
/* 1288 */     int offset = first.offset;
/* 1289 */     updateComponentOffsets(0);
/* 1290 */     setIndex(readerIndex - offset, writerIndex - offset);
/* 1291 */     adjustMarkers(offset);
/* 1292 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardReadBytes()
/*      */   {
/* 1297 */     ensureAccessible();
/* 1298 */     int readerIndex = readerIndex();
/* 1299 */     if (readerIndex == 0) {
/* 1300 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1304 */     int writerIndex = writerIndex();
/* 1305 */     if ((readerIndex == writerIndex) && (writerIndex == capacity())) {
/* 1306 */       for (Component c : this.components) {
/* 1307 */         c.freeIfNecessary();
/*      */       }
/* 1309 */       this.components.clear();
/* 1310 */       setIndex(0, 0);
/* 1311 */       adjustMarkers(readerIndex);
/* 1312 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1316 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1317 */     for (int i = 0; i < firstComponentId; i++) {
/* 1318 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/* 1320 */     this.components.subList(0, firstComponentId).clear();
/*      */     
/*      */ 
/* 1323 */     Component c = (Component)this.components.get(0);
/* 1324 */     int adjustment = readerIndex - c.offset;
/* 1325 */     if (adjustment == c.length)
/*      */     {
/* 1327 */       this.components.remove(0);
/*      */     } else {
/* 1329 */       Component newC = new Component(c.buf.slice(adjustment, c.length - adjustment));
/* 1330 */       this.components.set(0, newC);
/*      */     }
/*      */     
/*      */ 
/* 1334 */     updateComponentOffsets(0);
/* 1335 */     setIndex(0, writerIndex - readerIndex);
/* 1336 */     adjustMarkers(readerIndex);
/* 1337 */     return this;
/*      */   }
/*      */   
/*      */   private ByteBuf allocBuffer(int capacity) {
/* 1341 */     if (this.direct) {
/* 1342 */       return alloc().directBuffer(capacity);
/*      */     }
/* 1344 */     return alloc().heapBuffer(capacity);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1349 */     String result = super.toString();
/* 1350 */     result = result.substring(0, result.length() - 1);
/* 1351 */     return result + ", components=" + this.components.size() + ')';
/*      */   }
/*      */   
/*      */   private static final class Component {
/*      */     final ByteBuf buf;
/*      */     final int length;
/*      */     int offset;
/*      */     int endOffset;
/*      */     
/*      */     Component(ByteBuf buf) {
/* 1361 */       this.buf = buf;
/* 1362 */       this.length = buf.readableBytes();
/*      */     }
/*      */     
/*      */     void freeIfNecessary()
/*      */     {
/* 1367 */       this.buf.release();
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readerIndex(int readerIndex)
/*      */   {
/* 1373 */     return (CompositeByteBuf)super.readerIndex(readerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writerIndex(int writerIndex)
/*      */   {
/* 1378 */     return (CompositeByteBuf)super.writerIndex(writerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/* 1383 */     return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf clear()
/*      */   {
/* 1388 */     return (CompositeByteBuf)super.clear();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf markReaderIndex()
/*      */   {
/* 1393 */     return (CompositeByteBuf)super.markReaderIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf resetReaderIndex()
/*      */   {
/* 1398 */     return (CompositeByteBuf)super.resetReaderIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf markWriterIndex()
/*      */   {
/* 1403 */     return (CompositeByteBuf)super.markWriterIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf resetWriterIndex()
/*      */   {
/* 1408 */     return (CompositeByteBuf)super.resetWriterIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf ensureWritable(int minWritableBytes)
/*      */   {
/* 1413 */     return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/* 1418 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/* 1423 */     return (CompositeByteBuf)super.getBytes(index, dst, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst)
/*      */   {
/* 1428 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBoolean(int index, boolean value)
/*      */   {
/* 1433 */     return (CompositeByteBuf)super.setBoolean(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setChar(int index, int value)
/*      */   {
/* 1438 */     return (CompositeByteBuf)super.setChar(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setFloat(int index, float value)
/*      */   {
/* 1443 */     return (CompositeByteBuf)super.setFloat(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setDouble(int index, double value)
/*      */   {
/* 1448 */     return (CompositeByteBuf)super.setDouble(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/* 1453 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/* 1458 */     return (CompositeByteBuf)super.setBytes(index, src, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src)
/*      */   {
/* 1463 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setZero(int index, int length)
/*      */   {
/* 1468 */     return (CompositeByteBuf)super.setZero(index, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst)
/*      */   {
/* 1473 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/* 1478 */     return (CompositeByteBuf)super.readBytes(dst, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/* 1483 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst)
/*      */   {
/* 1488 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/* 1493 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuffer dst)
/*      */   {
/* 1498 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException
/*      */   {
/* 1503 */     return (CompositeByteBuf)super.readBytes(out, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf skipBytes(int length)
/*      */   {
/* 1508 */     return (CompositeByteBuf)super.skipBytes(length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBoolean(boolean value)
/*      */   {
/* 1513 */     return (CompositeByteBuf)super.writeBoolean(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeByte(int value)
/*      */   {
/* 1518 */     return (CompositeByteBuf)super.writeByte(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeShort(int value)
/*      */   {
/* 1523 */     return (CompositeByteBuf)super.writeShort(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeMedium(int value)
/*      */   {
/* 1528 */     return (CompositeByteBuf)super.writeMedium(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeInt(int value)
/*      */   {
/* 1533 */     return (CompositeByteBuf)super.writeInt(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeLong(long value)
/*      */   {
/* 1538 */     return (CompositeByteBuf)super.writeLong(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeChar(int value)
/*      */   {
/* 1543 */     return (CompositeByteBuf)super.writeChar(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeFloat(float value)
/*      */   {
/* 1548 */     return (CompositeByteBuf)super.writeFloat(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeDouble(double value)
/*      */   {
/* 1553 */     return (CompositeByteBuf)super.writeDouble(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src)
/*      */   {
/* 1558 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/* 1563 */     return (CompositeByteBuf)super.writeBytes(src, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/* 1568 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src)
/*      */   {
/* 1573 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/* 1578 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuffer src)
/*      */   {
/* 1583 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeZero(int length)
/*      */   {
/* 1588 */     return (CompositeByteBuf)super.writeZero(length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain(int increment)
/*      */   {
/* 1593 */     return (CompositeByteBuf)super.retain(increment);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain()
/*      */   {
/* 1598 */     return (CompositeByteBuf)super.retain();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch()
/*      */   {
/* 1603 */     if (this.leak != null) {
/* 1604 */       this.leak.record();
/*      */     }
/* 1606 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch(Object hint)
/*      */   {
/* 1611 */     if (this.leak != null) {
/* 1612 */       this.leak.record(hint);
/*      */     }
/* 1614 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/* 1619 */     return nioBuffers(readerIndex(), readableBytes());
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardSomeReadBytes()
/*      */   {
/* 1624 */     return discardReadComponents();
/*      */   }
/*      */   
/*      */   protected void deallocate()
/*      */   {
/* 1629 */     if (this.freed) {
/* 1630 */       return;
/*      */     }
/*      */     
/* 1633 */     this.freed = true;
/* 1634 */     int size = this.components.size();
/*      */     
/*      */ 
/* 1637 */     for (int i = 0; i < size; i++) {
/* 1638 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/*      */     
/* 1641 */     if (this.leak != null) {
/* 1642 */       this.leak.close();
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf unwrap()
/*      */   {
/* 1648 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\CompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */