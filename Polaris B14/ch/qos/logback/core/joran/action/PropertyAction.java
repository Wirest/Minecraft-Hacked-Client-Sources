/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class PropertyAction
/*     */   extends Action
/*     */ {
/*     */   static final String RESOURCE_ATTRIBUTE = "resource";
/*  46 */   static String INVALID_ATTRIBUTES = "In <property> element, either the \"file\" attribute alone, or the \"resource\" element alone, or both the \"name\" and \"value\" attributes must be set.";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*     */   {
/*  58 */     if ("substitutionProperty".equals(localName)) {
/*  59 */       addWarn("[substitutionProperty] element has been deprecated. Please use the [property] element instead.");
/*     */     }
/*     */     
/*  62 */     String name = attributes.getValue("name");
/*  63 */     String value = attributes.getValue("value");
/*  64 */     String scopeStr = attributes.getValue("scope");
/*     */     
/*  66 */     ActionUtil.Scope scope = ActionUtil.stringToScope(scopeStr);
/*     */     
/*  68 */     if (checkFileAttributeSanity(attributes)) {
/*  69 */       String file = attributes.getValue("file");
/*  70 */       file = ec.subst(file);
/*     */       try {
/*  72 */         FileInputStream istream = new FileInputStream(file);
/*  73 */         loadAndSetProperties(ec, istream, scope);
/*     */       } catch (FileNotFoundException e) {
/*  75 */         addError("Could not find properties file [" + file + "].");
/*     */       } catch (IOException e1) {
/*  77 */         addError("Could not read properties file [" + file + "].", e1);
/*     */       }
/*  79 */     } else if (checkResourceAttributeSanity(attributes)) {
/*  80 */       String resource = attributes.getValue("resource");
/*  81 */       resource = ec.subst(resource);
/*  82 */       URL resourceURL = Loader.getResourceBySelfClassLoader(resource);
/*  83 */       if (resourceURL == null) {
/*  84 */         addError("Could not find resource [" + resource + "].");
/*     */       } else {
/*     */         try {
/*  87 */           InputStream istream = resourceURL.openStream();
/*  88 */           loadAndSetProperties(ec, istream, scope);
/*     */         } catch (IOException e) {
/*  90 */           addError("Could not read resource file [" + resource + "].", e);
/*     */         }
/*     */       }
/*  93 */     } else if (checkValueNameAttributesSanity(attributes)) {
/*  94 */       value = RegularEscapeUtil.basicEscape(value);
/*     */       
/*  96 */       value = value.trim();
/*  97 */       value = ec.subst(value);
/*  98 */       ActionUtil.setProperty(ec, name, value, scope);
/*     */     }
/*     */     else {
/* 101 */       addError(INVALID_ATTRIBUTES);
/*     */     }
/*     */   }
/*     */   
/*     */   void loadAndSetProperties(InterpretationContext ec, InputStream istream, ActionUtil.Scope scope) throws IOException
/*     */   {
/* 107 */     Properties props = new Properties();
/* 108 */     props.load(istream);
/* 109 */     istream.close();
/* 110 */     ActionUtil.setProperties(ec, props, scope);
/*     */   }
/*     */   
/*     */   boolean checkFileAttributeSanity(Attributes attributes) {
/* 114 */     String file = attributes.getValue("file");
/* 115 */     String name = attributes.getValue("name");
/* 116 */     String value = attributes.getValue("value");
/* 117 */     String resource = attributes.getValue("resource");
/*     */     
/* 119 */     return (!OptionHelper.isEmpty(file)) && (OptionHelper.isEmpty(name)) && (OptionHelper.isEmpty(value)) && (OptionHelper.isEmpty(resource));
/*     */   }
/*     */   
/*     */ 
/*     */   boolean checkResourceAttributeSanity(Attributes attributes)
/*     */   {
/* 125 */     String file = attributes.getValue("file");
/* 126 */     String name = attributes.getValue("name");
/* 127 */     String value = attributes.getValue("value");
/* 128 */     String resource = attributes.getValue("resource");
/*     */     
/* 130 */     return (!OptionHelper.isEmpty(resource)) && (OptionHelper.isEmpty(name)) && (OptionHelper.isEmpty(value)) && (OptionHelper.isEmpty(file));
/*     */   }
/*     */   
/*     */ 
/*     */   boolean checkValueNameAttributesSanity(Attributes attributes)
/*     */   {
/* 136 */     String file = attributes.getValue("file");
/* 137 */     String name = attributes.getValue("name");
/* 138 */     String value = attributes.getValue("value");
/* 139 */     String resource = attributes.getValue("resource");
/*     */     
/* 141 */     return (!OptionHelper.isEmpty(name)) && (!OptionHelper.isEmpty(value)) && (OptionHelper.isEmpty(file)) && (OptionHelper.isEmpty(resource));
/*     */   }
/*     */   
/*     */   public void end(InterpretationContext ec, String name) {}
/*     */   
/*     */   public void finish(InterpretationContext ec) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\PropertyAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */