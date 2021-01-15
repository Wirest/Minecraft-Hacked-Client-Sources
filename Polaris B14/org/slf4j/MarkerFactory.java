/*    */ package org.slf4j;
/*    */ 
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.impl.StaticMarkerBinder;
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
/*    */ public class MarkerFactory
/*    */ {
/*    */   static IMarkerFactory markerFactory;
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 52 */       markerFactory = StaticMarkerBinder.SINGLETON.getMarkerFactory();
/*    */     } catch (NoClassDefFoundError e) {
/* 54 */       markerFactory = new BasicMarkerFactory();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 58 */       Util.report("Unexpected failure while binding MarkerFactory", e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Marker getMarker(String name)
/*    */   {
/* 71 */     return markerFactory.getMarker(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Marker getDetachedMarker(String name)
/*    */   {
/* 82 */     return markerFactory.getDetachedMarker(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static IMarkerFactory getIMarkerFactory()
/*    */   {
/* 94 */     return markerFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\MarkerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */