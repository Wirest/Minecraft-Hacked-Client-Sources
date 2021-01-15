/*    */ package io.netty.handler.codec.http.websocketx.extensions;
/*    */ 
/*    */ import io.netty.handler.codec.http.HttpHeaderNames;
/*    */ import io.netty.handler.codec.http.HttpHeaderValues;
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ import io.netty.handler.codec.http.HttpMessage;
/*    */ import io.netty.util.internal.StringUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WebSocketExtensionUtil
/*    */ {
/*    */   private static final char EXTENSION_SEPARATOR = ',';
/*    */   private static final char PARAMETER_SEPARATOR = ';';
/*    */   private static final char PARAMETER_EQUAL = '=';
/* 38 */   private static final Pattern PARAMETER = Pattern.compile("^([^=]+)(=[\\\"]?([^\\\"]+)[\\\"]?)?$");
/*    */   
/*    */   static boolean isWebsocketUpgrade(HttpMessage httpMessage) {
/* 41 */     if (httpMessage == null) {
/* 42 */       throw new NullPointerException("httpMessage");
/*    */     }
/* 44 */     return (httpMessage.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true)) && (httpMessage.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true));
/*    */   }
/*    */   
/*    */   public static List<WebSocketExtensionData> extractExtensions(String extensionHeader)
/*    */   {
/* 49 */     String[] rawExtensions = StringUtil.split(extensionHeader, ',');
/* 50 */     if (rawExtensions.length > 0) {
/* 51 */       List<WebSocketExtensionData> extensions = new ArrayList(rawExtensions.length);
/* 52 */       for (String rawExtension : rawExtensions) {
/* 53 */         String[] extensionParameters = StringUtil.split(rawExtension, ';');
/* 54 */         String name = extensionParameters[0].trim();
/*    */         Map<String, String> parameters;
/* 56 */         if (extensionParameters.length > 1) {
/* 57 */           Map<String, String> parameters = new HashMap(extensionParameters.length - 1);
/* 58 */           for (int i = 1; i < extensionParameters.length; i++) {
/* 59 */             String parameter = extensionParameters[i].trim();
/* 60 */             Matcher parameterMatcher = PARAMETER.matcher(parameter);
/* 61 */             if ((parameterMatcher.matches()) && (parameterMatcher.group(1) != null)) {
/* 62 */               parameters.put(parameterMatcher.group(1), parameterMatcher.group(3));
/*    */             }
/*    */           }
/*    */         } else {
/* 66 */           parameters = Collections.emptyMap();
/*    */         }
/* 68 */         extensions.add(new WebSocketExtensionData(name, parameters));
/*    */       }
/* 70 */       return extensions;
/*    */     }
/* 72 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   static String appendExtension(String currentHeaderValue, String extensionName, Map<String, String> extensionParameters)
/*    */   {
/* 79 */     StringBuilder newHeaderValue = new StringBuilder(currentHeaderValue != null ? currentHeaderValue.length() : 0 + extensionName.length() + 1);
/*    */     
/* 81 */     if ((currentHeaderValue != null) && (!currentHeaderValue.trim().isEmpty())) {
/* 82 */       newHeaderValue.append(currentHeaderValue);
/* 83 */       newHeaderValue.append(',');
/*    */     }
/* 85 */     newHeaderValue.append(extensionName);
/* 86 */     boolean isFirst = true;
/* 87 */     for (Map.Entry<String, String> extensionParameter : extensionParameters.entrySet()) {
/* 88 */       if (isFirst) {
/* 89 */         newHeaderValue.append(';');
/*    */       } else {
/* 91 */         isFirst = false;
/*    */       }
/* 93 */       newHeaderValue.append((String)extensionParameter.getKey());
/* 94 */       if (extensionParameter.getValue() != null) {
/* 95 */         newHeaderValue.append('=');
/* 96 */         newHeaderValue.append((String)extensionParameter.getValue());
/*    */       }
/*    */     }
/* 99 */     return newHeaderValue.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketExtensionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */