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
/*     */ 
/*     */ public class DiskAttribute
/*     */   extends AbstractDiskHttpData
/*     */   implements Attribute
/*     */ {
/*     */   public static String baseDirectory;
/*  33 */   public static boolean deleteOnExitTemporaryFile = true;
/*     */   
/*     */ 
/*     */   public static final String prefix = "Attr_";
/*     */   
/*     */   public static final String postfix = ".att";
/*     */   
/*     */ 
/*     */   public DiskAttribute(String name)
/*     */   {
/*  43 */     this(name, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, Charset charset) {
/*  47 */     super(name, charset, 0L);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, String value) throws IOException {
/*  51 */     this(name, value, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, String value, Charset charset) throws IOException {
/*  55 */     super(name, charset, 0L);
/*  56 */     setValue(value);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/*  61 */     return InterfaceHttpData.HttpDataType.Attribute;
/*     */   }
/*     */   
/*     */   public String getValue() throws IOException
/*     */   {
/*  66 */     byte[] bytes = get();
/*  67 */     return new String(bytes, getCharset());
/*     */   }
/*     */   
/*     */   public void setValue(String value) throws IOException
/*     */   {
/*  72 */     if (value == null) {
/*  73 */       throw new NullPointerException("value");
/*     */     }
/*  75 */     byte[] bytes = value.getBytes(getCharset());
/*  76 */     checkSize(bytes.length);
/*  77 */     ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
/*  78 */     if (this.definedSize > 0L) {
/*  79 */       this.definedSize = buffer.readableBytes();
/*     */     }
/*  81 */     setContent(buffer);
/*     */   }
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException
/*     */   {
/*  86 */     long newDefinedSize = this.size + buffer.readableBytes();
/*  87 */     checkSize(newDefinedSize);
/*  88 */     if ((this.definedSize > 0L) && (this.definedSize < newDefinedSize)) {
/*  89 */       this.definedSize = newDefinedSize;
/*     */     }
/*  91 */     super.addContent(buffer, last);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  96 */     return getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 101 */     if (!(o instanceof Attribute)) {
/* 102 */       return false;
/*     */     }
/* 104 */     Attribute attribute = (Attribute)o;
/* 105 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData o)
/*     */   {
/* 110 */     if (!(o instanceof Attribute)) {
/* 111 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
/*     */     }
/*     */     
/* 114 */     return compareTo((Attribute)o);
/*     */   }
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 118 */     return getName().compareToIgnoreCase(o.getName());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*     */     try {
/* 124 */       return getName() + '=' + getValue();
/*     */     } catch (IOException e) {
/* 126 */       return getName() + '=' + e;
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean deleteOnExit()
/*     */   {
/* 132 */     return deleteOnExitTemporaryFile;
/*     */   }
/*     */   
/*     */   protected String getBaseDirectory()
/*     */   {
/* 137 */     return baseDirectory;
/*     */   }
/*     */   
/*     */   protected String getDiskFilename()
/*     */   {
/* 142 */     return getName() + ".att";
/*     */   }
/*     */   
/*     */   protected String getPostfix()
/*     */   {
/* 147 */     return ".att";
/*     */   }
/*     */   
/*     */   protected String getPrefix()
/*     */   {
/* 152 */     return "Attr_";
/*     */   }
/*     */   
/*     */   public Attribute copy()
/*     */   {
/* 157 */     DiskAttribute attr = new DiskAttribute(getName());
/* 158 */     attr.setCharset(getCharset());
/* 159 */     ByteBuf content = content();
/* 160 */     if (content != null) {
/*     */       try {
/* 162 */         attr.setContent(content.copy());
/*     */       } catch (IOException e) {
/* 164 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 167 */     return attr;
/*     */   }
/*     */   
/*     */   public Attribute duplicate()
/*     */   {
/* 172 */     DiskAttribute attr = new DiskAttribute(getName());
/* 173 */     attr.setCharset(getCharset());
/* 174 */     ByteBuf content = content();
/* 175 */     if (content != null) {
/*     */       try {
/* 177 */         attr.setContent(content.duplicate());
/*     */       } catch (IOException e) {
/* 179 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 182 */     return attr;
/*     */   }
/*     */   
/*     */   public Attribute retain(int increment)
/*     */   {
/* 187 */     super.retain(increment);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute retain()
/*     */   {
/* 193 */     super.retain();
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch()
/*     */   {
/* 199 */     super.touch();
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch(Object hint)
/*     */   {
/* 205 */     super.touch(hint);
/* 206 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\DiskAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */