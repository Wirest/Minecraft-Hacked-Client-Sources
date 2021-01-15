/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.spi.MarkerFactoryBinder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticMarkerBinder
/*    */   implements MarkerFactoryBinder
/*    */ {
/* 33 */   public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
/*    */   
/* 35 */   final IMarkerFactory markerFactory = new BasicMarkerFactory();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IMarkerFactory getMarkerFactory()
/*    */   {
/* 45 */     return this.markerFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMarkerFactoryClassStr()
/*    */   {
/* 53 */     return BasicMarkerFactory.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\impl\StaticMarkerBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */