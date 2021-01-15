/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttpDataFactory
/*     */   implements HttpDataFactory
/*     */ {
/*     */   public static final long MINSIZE = 16384L;
/*     */   public static final long MAXSIZE = -1L;
/*     */   private final boolean useDisk;
/*     */   private final boolean checkSize;
/*     */   private long minSize;
/*  55 */   private long maxSize = -1L;
/*     */   
/*  57 */   private Charset charset = HttpConstants.DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory()
/*     */   {
/*  69 */     this.useDisk = false;
/*  70 */     this.checkSize = true;
/*  71 */     this.minSize = 16384L;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(Charset charset) {
/*  75 */     this();
/*  76 */     this.charset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory(boolean useDisk)
/*     */   {
/*  83 */     this.useDisk = useDisk;
/*  84 */     this.checkSize = false;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(boolean useDisk, Charset charset) {
/*  88 */     this(useDisk);
/*  89 */     this.charset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory(long minSize)
/*     */   {
/*  96 */     this.useDisk = false;
/*  97 */     this.checkSize = true;
/*  98 */     this.minSize = minSize;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(long minSize, Charset charset) {
/* 102 */     this(minSize);
/* 103 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public void setMaxLimit(long maxSize)
/*     */   {
/* 108 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<HttpData> getList(HttpRequest request)
/*     */   {
/* 115 */     List<HttpData> list = (List)this.requestFileDeleteMap.get(request);
/* 116 */     if (list == null) {
/* 117 */       list = new ArrayList();
/* 118 */       this.requestFileDeleteMap.put(request, list);
/*     */     }
/* 120 */     return list;
/*     */   }
/*     */   
/*     */   public Attribute createAttribute(HttpRequest request, String name)
/*     */   {
/* 125 */     if (this.useDisk) {
/* 126 */       Attribute attribute = new DiskAttribute(name, this.charset);
/* 127 */       attribute.setMaxSize(this.maxSize);
/* 128 */       List<HttpData> fileToDelete = getList(request);
/* 129 */       fileToDelete.add(attribute);
/* 130 */       return attribute;
/*     */     }
/* 132 */     if (this.checkSize) {
/* 133 */       Attribute attribute = new MixedAttribute(name, this.minSize, this.charset);
/* 134 */       attribute.setMaxSize(this.maxSize);
/* 135 */       List<HttpData> fileToDelete = getList(request);
/* 136 */       fileToDelete.add(attribute);
/* 137 */       return attribute;
/*     */     }
/* 139 */     MemoryAttribute attribute = new MemoryAttribute(name);
/* 140 */     attribute.setMaxSize(this.maxSize);
/* 141 */     return attribute;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void checkHttpDataSize(HttpData data)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       data.checkSize(data.length());
/*     */     } catch (IOException ignored) {
/* 151 */       throw new IllegalArgumentException("Attribute bigger than maxSize allowed");
/*     */     }
/*     */   }
/*     */   
/*     */   public Attribute createAttribute(HttpRequest request, String name, String value)
/*     */   {
/* 157 */     if (this.useDisk) {
/*     */       Attribute attribute;
/*     */       try {
/* 160 */         attribute = new DiskAttribute(name, value, this.charset);
/* 161 */         attribute.setMaxSize(this.maxSize);
/*     */       }
/*     */       catch (IOException e) {
/* 164 */         attribute = new MixedAttribute(name, value, this.minSize, this.charset);
/* 165 */         attribute.setMaxSize(this.maxSize);
/*     */       }
/* 167 */       checkHttpDataSize(attribute);
/* 168 */       List<HttpData> fileToDelete = getList(request);
/* 169 */       fileToDelete.add(attribute);
/* 170 */       return attribute;
/*     */     }
/* 172 */     if (this.checkSize) {
/* 173 */       Attribute attribute = new MixedAttribute(name, value, this.minSize, this.charset);
/* 174 */       attribute.setMaxSize(this.maxSize);
/* 175 */       checkHttpDataSize(attribute);
/* 176 */       List<HttpData> fileToDelete = getList(request);
/* 177 */       fileToDelete.add(attribute);
/* 178 */       return attribute;
/*     */     }
/*     */     try {
/* 181 */       MemoryAttribute attribute = new MemoryAttribute(name, value, this.charset);
/* 182 */       attribute.setMaxSize(this.maxSize);
/* 183 */       checkHttpDataSize(attribute);
/* 184 */       return attribute;
/*     */     } catch (IOException e) {
/* 186 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FileUpload createFileUpload(HttpRequest request, String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size)
/*     */   {
/* 194 */     if (this.useDisk) {
/* 195 */       FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
/*     */       
/* 197 */       fileUpload.setMaxSize(this.maxSize);
/* 198 */       checkHttpDataSize(fileUpload);
/* 199 */       List<HttpData> fileToDelete = getList(request);
/* 200 */       fileToDelete.add(fileUpload);
/* 201 */       return fileUpload;
/*     */     }
/* 203 */     if (this.checkSize) {
/* 204 */       FileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
/*     */       
/* 206 */       fileUpload.setMaxSize(this.maxSize);
/* 207 */       checkHttpDataSize(fileUpload);
/* 208 */       List<HttpData> fileToDelete = getList(request);
/* 209 */       fileToDelete.add(fileUpload);
/* 210 */       return fileUpload;
/*     */     }
/* 212 */     MemoryFileUpload fileUpload = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
/*     */     
/* 214 */     fileUpload.setMaxSize(this.maxSize);
/* 215 */     checkHttpDataSize(fileUpload);
/* 216 */     return fileUpload;
/*     */   }
/*     */   
/*     */   public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data)
/*     */   {
/* 221 */     if ((data instanceof HttpData)) {
/* 222 */       List<HttpData> fileToDelete = getList(request);
/* 223 */       fileToDelete.remove(data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanRequestHttpData(HttpRequest request)
/*     */   {
/* 229 */     List<HttpData> fileToDelete = (List)this.requestFileDeleteMap.remove(request);
/* 230 */     if (fileToDelete != null) {
/* 231 */       for (HttpData data : fileToDelete) {
/* 232 */         data.delete();
/*     */       }
/* 234 */       fileToDelete.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanAllHttpData()
/*     */   {
/* 240 */     Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
/* 241 */     while (i.hasNext()) {
/* 242 */       Map.Entry<HttpRequest, List<HttpData>> e = (Map.Entry)i.next();
/* 243 */       i.remove();
/*     */       
/* 245 */       List<HttpData> fileToDelete = (List)e.getValue();
/* 246 */       if (fileToDelete != null) {
/* 247 */         for (HttpData data : fileToDelete) {
/* 248 */           data.delete();
/*     */         }
/* 250 */         fileToDelete.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\DefaultHttpDataFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */