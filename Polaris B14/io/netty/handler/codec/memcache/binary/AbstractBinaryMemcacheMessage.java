/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.memcache.AbstractMemcacheObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBinaryMemcacheMessage
/*     */   extends AbstractMemcacheObject
/*     */   implements BinaryMemcacheMessage
/*     */ {
/*     */   private String key;
/*     */   private ByteBuf extras;
/*     */   private byte magic;
/*     */   private byte opcode;
/*     */   private short keyLength;
/*     */   private byte extrasLength;
/*     */   private byte dataType;
/*     */   private int totalBodyLength;
/*     */   private int opaque;
/*     */   private long cas;
/*     */   
/*     */   protected AbstractBinaryMemcacheMessage(String key, ByteBuf extras)
/*     */   {
/*  54 */     this.key = key;
/*  55 */     this.extras = extras;
/*     */   }
/*     */   
/*     */   public String key()
/*     */   {
/*  60 */     return this.key;
/*     */   }
/*     */   
/*     */   public ByteBuf extras()
/*     */   {
/*  65 */     return this.extras;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setKey(String key)
/*     */   {
/*  70 */     this.key = key;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setExtras(ByteBuf extras)
/*     */   {
/*  76 */     this.extras = extras;
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public byte magic()
/*     */   {
/*  82 */     return this.magic;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setMagic(byte magic)
/*     */   {
/*  87 */     this.magic = magic;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public long cas()
/*     */   {
/*  93 */     return this.cas;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setCas(long cas)
/*     */   {
/*  98 */     this.cas = cas;
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public int opaque()
/*     */   {
/* 104 */     return this.opaque;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setOpaque(int opaque)
/*     */   {
/* 109 */     this.opaque = opaque;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public int totalBodyLength()
/*     */   {
/* 115 */     return this.totalBodyLength;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setTotalBodyLength(int totalBodyLength)
/*     */   {
/* 120 */     this.totalBodyLength = totalBodyLength;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public byte dataType()
/*     */   {
/* 126 */     return this.dataType;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setDataType(byte dataType)
/*     */   {
/* 131 */     this.dataType = dataType;
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public byte extrasLength()
/*     */   {
/* 137 */     return this.extrasLength;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setExtrasLength(byte extrasLength)
/*     */   {
/* 142 */     this.extrasLength = extrasLength;
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public short keyLength()
/*     */   {
/* 148 */     return this.keyLength;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setKeyLength(short keyLength)
/*     */   {
/* 153 */     this.keyLength = keyLength;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public byte opcode()
/*     */   {
/* 159 */     return this.opcode;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage setOpcode(byte opcode)
/*     */   {
/* 164 */     this.opcode = opcode;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/* 170 */     if (this.extras != null) {
/* 171 */       return this.extras.refCnt();
/*     */     }
/* 173 */     return 1;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage retain()
/*     */   {
/* 178 */     if (this.extras != null) {
/* 179 */       this.extras.retain();
/*     */     }
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage retain(int increment)
/*     */   {
/* 186 */     if (this.extras != null) {
/* 187 */       this.extras.retain(increment);
/*     */     }
/* 189 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 194 */     if (this.extras != null) {
/* 195 */       return this.extras.release();
/*     */     }
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 202 */     if (this.extras != null) {
/* 203 */       return this.extras.release(decrement);
/*     */     }
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage touch()
/*     */   {
/* 210 */     return touch(null);
/*     */   }
/*     */   
/*     */   public BinaryMemcacheMessage touch(Object hint)
/*     */   {
/* 215 */     if (this.extras != null) {
/* 216 */       this.extras.touch(hint);
/*     */     }
/* 218 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */