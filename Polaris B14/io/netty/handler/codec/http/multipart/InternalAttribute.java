/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ final class InternalAttribute
/*     */   extends AbstractReferenceCounted
/*     */   implements InterfaceHttpData
/*     */ {
/*  31 */   private final List<ByteBuf> value = new ArrayList();
/*     */   private final Charset charset;
/*     */   private int size;
/*     */   
/*     */   InternalAttribute(Charset charset) {
/*  36 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/*  41 */     return InterfaceHttpData.HttpDataType.InternalAttribute;
/*     */   }
/*     */   
/*     */   public void addValue(String value) {
/*  45 */     if (value == null) {
/*  46 */       throw new NullPointerException("value");
/*     */     }
/*  48 */     ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
/*  49 */     this.value.add(buf);
/*  50 */     this.size += buf.readableBytes();
/*     */   }
/*     */   
/*     */   public void addValue(String value, int rank) {
/*  54 */     if (value == null) {
/*  55 */       throw new NullPointerException("value");
/*     */     }
/*  57 */     ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
/*  58 */     this.value.add(rank, buf);
/*  59 */     this.size += buf.readableBytes();
/*     */   }
/*     */   
/*     */   public void setValue(String value, int rank) {
/*  63 */     if (value == null) {
/*  64 */       throw new NullPointerException("value");
/*     */     }
/*  66 */     ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
/*  67 */     ByteBuf old = (ByteBuf)this.value.set(rank, buf);
/*  68 */     if (old != null) {
/*  69 */       this.size -= old.readableBytes();
/*  70 */       old.release();
/*     */     }
/*  72 */     this.size += buf.readableBytes();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  77 */     return getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  82 */     if (!(o instanceof Attribute)) {
/*  83 */       return false;
/*     */     }
/*  85 */     Attribute attribute = (Attribute)o;
/*  86 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData o)
/*     */   {
/*  91 */     if (!(o instanceof InternalAttribute)) {
/*  92 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
/*     */     }
/*     */     
/*  95 */     return compareTo((InternalAttribute)o);
/*     */   }
/*     */   
/*     */   public int compareTo(InternalAttribute o) {
/*  99 */     return getName().compareToIgnoreCase(o.getName());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 104 */     StringBuilder result = new StringBuilder();
/* 105 */     for (ByteBuf elt : this.value) {
/* 106 */       result.append(elt.toString(this.charset));
/*     */     }
/* 108 */     return result.toString();
/*     */   }
/*     */   
/*     */   public int size() {
/* 112 */     return this.size;
/*     */   }
/*     */   
/*     */   public ByteBuf toByteBuf() {
/* 116 */     return Unpooled.compositeBuffer().addComponents(this.value).writerIndex(size()).readerIndex(0);
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 121 */     return "InternalAttribute";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void deallocate() {}
/*     */   
/*     */ 
/*     */   public InterfaceHttpData retain()
/*     */   {
/* 131 */     for (ByteBuf buf : this.value) {
/* 132 */       buf.retain();
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public InterfaceHttpData retain(int increment)
/*     */   {
/* 139 */     for (ByteBuf buf : this.value) {
/* 140 */       buf.retain(increment);
/*     */     }
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public InterfaceHttpData touch()
/*     */   {
/* 147 */     for (ByteBuf buf : this.value) {
/* 148 */       buf.touch();
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public InterfaceHttpData touch(Object hint)
/*     */   {
/* 155 */     for (ByteBuf buf : this.value) {
/* 156 */       buf.touch(hint);
/*     */     }
/* 158 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\InternalAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */