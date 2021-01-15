/*    */ package org.apache.log4j.xml;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import java.net.URL;
/*    */ import java.util.Properties;
/*    */ import javax.xml.parsers.FactoryConfigurationError;
/*    */ import org.apache.log4j.spi.Configurator;
/*    */ import org.apache.log4j.spi.LoggerRepository;
/*    */ import org.w3c.dom.Element;
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
/*    */ 
/*    */ 
/*    */ public class DOMConfigurator
/*    */   implements Configurator
/*    */ {
/*    */   public static void configure(Element element) {}
/*    */   
/*    */   public static void configure(String filename)
/*    */     throws FactoryConfigurationError
/*    */   {}
/*    */   
/*    */   public static void configure(URL url)
/*    */     throws FactoryConfigurationError
/*    */   {}
/*    */   
/*    */   public static void configureAndWatch(String configFilename) {}
/*    */   
/*    */   public static void configureAndWatch(String configFilename, long delay) {}
/*    */   
/*    */   public void doConfigure(Element element, LoggerRepository repository) {}
/*    */   
/*    */   public void doConfigure(InputStream inputStream, LoggerRepository repository)
/*    */     throws FactoryConfigurationError
/*    */   {}
/*    */   
/*    */   public void doConfigure(Reader reader, LoggerRepository repository)
/*    */     throws FactoryConfigurationError
/*    */   {}
/*    */   
/*    */   public void doConfigure(String filename, LoggerRepository repository) {}
/*    */   
/*    */   public void doConfigure(URL url, LoggerRepository repository) {}
/*    */   
/*    */   public static String subst(String value, Properties props)
/*    */   {
/* 63 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\xml\DOMConfigurator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */