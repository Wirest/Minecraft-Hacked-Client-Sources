/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.Marker;
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
/*    */ public class BasicMarkerFactory
/*    */   implements IMarkerFactory
/*    */ {
/* 44 */   private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap();
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
/*    */   public Marker getMarker(String name)
/*    */   {
/* 63 */     if (name == null) {
/* 64 */       throw new IllegalArgumentException("Marker name cannot be null");
/*    */     }
/*    */     
/* 67 */     Marker marker = (Marker)this.markerMap.get(name);
/* 68 */     if (marker == null) {
/* 69 */       marker = new BasicMarker(name);
/* 70 */       Marker oldMarker = (Marker)this.markerMap.putIfAbsent(name, marker);
/* 71 */       if (oldMarker != null) {
/* 72 */         marker = oldMarker;
/*    */       }
/*    */     }
/* 75 */     return marker;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean exists(String name)
/*    */   {
/* 82 */     if (name == null) {
/* 83 */       return false;
/*    */     }
/* 85 */     return this.markerMap.containsKey(name);
/*    */   }
/*    */   
/*    */   public boolean detachMarker(String name) {
/* 89 */     if (name == null) {
/* 90 */       return false;
/*    */     }
/* 92 */     return this.markerMap.remove(name) != null;
/*    */   }
/*    */   
/*    */   public Marker getDetachedMarker(String name)
/*    */   {
/* 97 */     return new BasicMarker(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\BasicMarkerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */