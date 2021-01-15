/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class MemoryAttribute
/*     */   extends AbstractMemoryHttpData
/*     */   implements Attribute
/*     */ {
/*     */   public MemoryAttribute(String name)
/*     */   {
/*  33 */     this(name, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, Charset charset) {
/*  37 */     super(name, charset, 0L);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, String value) throws IOException {
/*  41 */     this(name, value, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, String value, Charset charset) throws IOException {
/*  45 */     super(name, charset, 0L);
/*  46 */     setValue(value);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/*  51 */     return InterfaceHttpData.HttpDataType.Attribute;
/*     */   }
/*     */   
/*     */   public String getValue()
/*     */   {
/*  56 */     return getByteBuf().toString(getCharset());
/*     */   }
/*     */   
/*     */   public void setValue(String value) throws IOException
/*     */   {
/*  61 */     if (value == null) {
/*  62 */       throw new NullPointerException("value");
/*     */     }
/*  64 */     byte[] bytes = value.getBytes(getCharset());
/*  65 */     checkSize(bytes.length);
/*  66 */     ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
/*  67 */     if (this.definedSize > 0L) {
/*  68 */       this.definedSize = buffer.readableBytes();
/*     */     }
/*  70 */     setContent(buffer);
/*     */   }
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException
/*     */   {
/*  75 */     int localsize = buffer.readableBytes();
/*  76 */     checkSize(this.size + localsize);
/*  77 */     if ((this.definedSize > 0L) && (this.definedSize < this.size + localsize)) {
/*  78 */       this.definedSize = (this.size + localsize);
/*     */     }
/*  80 */     super.addContent(buffer, last);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  85 */     return getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  90 */     if (!(o instanceof Attribute)) {
/*  91 */       return false;
/*     */     }
/*  93 */     Attribute attribute = (Attribute)o;
/*  94 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData other)
/*     */   {
/*  99 */     if (!(other instanceof Attribute)) {
/* 100 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + other.getHttpDataType());
/*     */     }
/*     */     
/* 103 */     return compareTo((Attribute)other);
/*     */   }
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 107 */     return getName().compareToIgnoreCase(o.getName());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 112 */     return getName() + '=' + getValue();
/*     */   }
/*     */   
/*     */   public Attribute copy()
/*     */   {
/* 117 */     MemoryAttribute attr = new MemoryAttribute(getName());
/* 118 */     attr.setCharset(getCharset());
/* 119 */     ByteBuf content = content();
/* 120 */     if (content != null) {
/*     */       try {
/* 122 */         attr.setContent(content.copy());
/*     */       } catch (IOException e) {
/* 124 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 127 */     return attr;
/*     */   }
/*     */   
/*     */   public Attribute duplicate()
/*     */   {
/* 132 */     MemoryAttribute attr = new MemoryAttribute(getName());
/* 133 */     attr.setCharset(getCharset());
/* 134 */     ByteBuf content = content();
/* 135 */     if (content != null) {
/*     */       try {
/* 137 */         attr.setContent(content.duplicate());
/*     */       } catch (IOException e) {
/* 139 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 142 */     return attr;
/*     */   }
/*     */   
/*     */   public Attribute retain()
/*     */   {
/* 147 */     super.retain();
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute retain(int increment)
/*     */   {
/* 153 */     super.retain(increment);
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch()
/*     */   {
/* 159 */     super.touch();
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch(Object hint)
/*     */   {
/* 165 */     super.touch(hint);
/* 166 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\MemoryAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */