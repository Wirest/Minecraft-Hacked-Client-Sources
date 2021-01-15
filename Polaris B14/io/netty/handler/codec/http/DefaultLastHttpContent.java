/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Map.Entry;
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
/*     */ public class DefaultLastHttpContent
/*     */   extends DefaultHttpContent
/*     */   implements LastHttpContent
/*     */ {
/*     */   private final HttpHeaders trailingHeaders;
/*     */   private final boolean validateHeaders;
/*     */   
/*     */   public DefaultLastHttpContent()
/*     */   {
/*  33 */     this(Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */   public DefaultLastHttpContent(ByteBuf content) {
/*  37 */     this(content, true);
/*     */   }
/*     */   
/*     */   public DefaultLastHttpContent(ByteBuf content, boolean validateHeaders) {
/*  41 */     super(content);
/*  42 */     this.trailingHeaders = new TrailingHttpHeaders(validateHeaders);
/*  43 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */   
/*     */   public LastHttpContent copy()
/*     */   {
/*  48 */     DefaultLastHttpContent copy = new DefaultLastHttpContent(content().copy(), this.validateHeaders);
/*  49 */     copy.trailingHeaders().set(trailingHeaders());
/*  50 */     return copy;
/*     */   }
/*     */   
/*     */   public LastHttpContent duplicate()
/*     */   {
/*  55 */     DefaultLastHttpContent copy = new DefaultLastHttpContent(content().duplicate(), this.validateHeaders);
/*  56 */     copy.trailingHeaders().set(trailingHeaders());
/*  57 */     return copy;
/*     */   }
/*     */   
/*     */   public LastHttpContent retain(int increment)
/*     */   {
/*  62 */     super.retain(increment);
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public LastHttpContent retain()
/*     */   {
/*  68 */     super.retain();
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public LastHttpContent touch()
/*     */   {
/*  74 */     super.touch();
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public LastHttpContent touch(Object hint)
/*     */   {
/*  80 */     super.touch(hint);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders trailingHeaders()
/*     */   {
/*  86 */     return this.trailingHeaders;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  91 */     StringBuilder buf = new StringBuilder(super.toString());
/*  92 */     buf.append(StringUtil.NEWLINE);
/*  93 */     appendHeaders(buf);
/*     */     
/*     */ 
/*  96 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/*  97 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void appendHeaders(StringBuilder buf) {
/* 101 */     for (Map.Entry<CharSequence, CharSequence> e : trailingHeaders()) {
/* 102 */       buf.append((CharSequence)e.getKey());
/* 103 */       buf.append(": ");
/* 104 */       buf.append((CharSequence)e.getValue());
/* 105 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TrailingHttpHeaders extends DefaultHttpHeaders {
/*     */     private static final class TrailingHttpHeadersNameConverter extends DefaultHttpHeaders.HttpHeadersNameConverter {
/*     */       TrailingHttpHeadersNameConverter(boolean validate) {
/* 112 */         super();
/*     */       }
/*     */       
/*     */       public CharSequence convertName(CharSequence name)
/*     */       {
/* 117 */         name = super.convertName(name);
/* 118 */         if ((this.validate) && (
/* 119 */           (HttpHeaderNames.CONTENT_LENGTH.equalsIgnoreCase(name)) || (HttpHeaderNames.TRANSFER_ENCODING.equalsIgnoreCase(name)) || (HttpHeaderNames.TRAILER.equalsIgnoreCase(name))))
/*     */         {
/*     */ 
/* 122 */           throw new IllegalArgumentException("prohibited trailing header: " + name);
/*     */         }
/*     */         
/* 125 */         return name;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 130 */     private static final TrailingHttpHeadersNameConverter VALIDATE_NAME_CONVERTER = new TrailingHttpHeadersNameConverter(true);
/*     */     
/* 132 */     private static final TrailingHttpHeadersNameConverter NO_VALIDATE_NAME_CONVERTER = new TrailingHttpHeadersNameConverter(false);
/*     */     
/*     */     TrailingHttpHeaders(boolean validate) {
/* 135 */       super(validate ? VALIDATE_NAME_CONVERTER : NO_VALIDATE_NAME_CONVERTER, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultLastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */