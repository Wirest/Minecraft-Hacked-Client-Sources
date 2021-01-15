/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpdyFrameEncoder
/*     */ {
/*     */   private final int version;
/*     */   
/*     */   public SpdyFrameEncoder(SpdyVersion spdyVersion)
/*     */   {
/*  37 */     if (spdyVersion == null) {
/*  38 */       throw new NullPointerException("spdyVersion");
/*     */     }
/*  40 */     this.version = spdyVersion.getVersion();
/*     */   }
/*     */   
/*     */   private void writeControlFrameHeader(ByteBuf buffer, int type, byte flags, int length) {
/*  44 */     buffer.writeShort(this.version | 0x8000);
/*  45 */     buffer.writeShort(type);
/*  46 */     buffer.writeByte(flags);
/*  47 */     buffer.writeMedium(length);
/*     */   }
/*     */   
/*     */   public ByteBuf encodeDataFrame(ByteBufAllocator allocator, int streamId, boolean last, ByteBuf data) {
/*  51 */     byte flags = last ? 1 : 0;
/*  52 */     int length = data.readableBytes();
/*  53 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/*  54 */     frame.writeInt(streamId & 0x7FFFFFFF);
/*  55 */     frame.writeByte(flags);
/*  56 */     frame.writeMedium(length);
/*  57 */     frame.writeBytes(data, data.readerIndex(), length);
/*  58 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeSynStreamFrame(ByteBufAllocator allocator, int streamId, int associatedToStreamId, byte priority, boolean last, boolean unidirectional, ByteBuf headerBlock)
/*     */   {
/*  63 */     int headerBlockLength = headerBlock.readableBytes();
/*  64 */     byte flags = last ? 1 : 0;
/*  65 */     if (unidirectional) {
/*  66 */       flags = (byte)(flags | 0x2);
/*     */     }
/*  68 */     int length = 10 + headerBlockLength;
/*  69 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/*  70 */     writeControlFrameHeader(frame, 1, flags, length);
/*  71 */     frame.writeInt(streamId);
/*  72 */     frame.writeInt(associatedToStreamId);
/*  73 */     frame.writeShort((priority & 0xFF) << 13);
/*  74 */     frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
/*  75 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeSynReplyFrame(ByteBufAllocator allocator, int streamId, boolean last, ByteBuf headerBlock) {
/*  79 */     int headerBlockLength = headerBlock.readableBytes();
/*  80 */     byte flags = last ? 1 : 0;
/*  81 */     int length = 4 + headerBlockLength;
/*  82 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/*  83 */     writeControlFrameHeader(frame, 2, flags, length);
/*  84 */     frame.writeInt(streamId);
/*  85 */     frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
/*  86 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeRstStreamFrame(ByteBufAllocator allocator, int streamId, int statusCode) {
/*  90 */     byte flags = 0;
/*  91 */     int length = 8;
/*  92 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/*  93 */     writeControlFrameHeader(frame, 3, flags, length);
/*  94 */     frame.writeInt(streamId);
/*  95 */     frame.writeInt(statusCode);
/*  96 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeSettingsFrame(ByteBufAllocator allocator, SpdySettingsFrame spdySettingsFrame) {
/* 100 */     Set<Integer> ids = spdySettingsFrame.ids();
/* 101 */     int numSettings = ids.size();
/*     */     
/* 103 */     byte flags = spdySettingsFrame.clearPreviouslyPersistedSettings() ? 1 : 0;
/*     */     
/* 105 */     int length = 4 + 8 * numSettings;
/* 106 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/* 107 */     writeControlFrameHeader(frame, 4, flags, length);
/* 108 */     frame.writeInt(numSettings);
/* 109 */     for (Integer id : ids) {
/* 110 */       flags = 0;
/* 111 */       if (spdySettingsFrame.isPersistValue(id.intValue())) {
/* 112 */         flags = (byte)(flags | 0x1);
/*     */       }
/* 114 */       if (spdySettingsFrame.isPersisted(id.intValue())) {
/* 115 */         flags = (byte)(flags | 0x2);
/*     */       }
/* 117 */       frame.writeByte(flags);
/* 118 */       frame.writeMedium(id.intValue());
/* 119 */       frame.writeInt(spdySettingsFrame.getValue(id.intValue()));
/*     */     }
/* 121 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodePingFrame(ByteBufAllocator allocator, int id) {
/* 125 */     byte flags = 0;
/* 126 */     int length = 4;
/* 127 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/* 128 */     writeControlFrameHeader(frame, 6, flags, length);
/* 129 */     frame.writeInt(id);
/* 130 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeGoAwayFrame(ByteBufAllocator allocator, int lastGoodStreamId, int statusCode) {
/* 134 */     byte flags = 0;
/* 135 */     int length = 8;
/* 136 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/* 137 */     writeControlFrameHeader(frame, 7, flags, length);
/* 138 */     frame.writeInt(lastGoodStreamId);
/* 139 */     frame.writeInt(statusCode);
/* 140 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeHeadersFrame(ByteBufAllocator allocator, int streamId, boolean last, ByteBuf headerBlock) {
/* 144 */     int headerBlockLength = headerBlock.readableBytes();
/* 145 */     byte flags = last ? 1 : 0;
/* 146 */     int length = 4 + headerBlockLength;
/* 147 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/* 148 */     writeControlFrameHeader(frame, 8, flags, length);
/* 149 */     frame.writeInt(streamId);
/* 150 */     frame.writeBytes(headerBlock, headerBlock.readerIndex(), headerBlockLength);
/* 151 */     return frame;
/*     */   }
/*     */   
/*     */   public ByteBuf encodeWindowUpdateFrame(ByteBufAllocator allocator, int streamId, int deltaWindowSize) {
/* 155 */     byte flags = 0;
/* 156 */     int length = 8;
/* 157 */     ByteBuf frame = allocator.ioBuffer(8 + length).order(ByteOrder.BIG_ENDIAN);
/* 158 */     writeControlFrameHeader(frame, 9, flags, length);
/* 159 */     frame.writeInt(streamId);
/* 160 */     frame.writeInt(deltaWindowSize);
/* 161 */     return frame;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyFrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */