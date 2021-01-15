/*    */ package ch.qos.logback.core.spi;
/*    */ 
/*    */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
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
/*    */ public class CyclicBufferTracker<E>
/*    */   extends AbstractComponentTracker<CyclicBuffer<E>>
/*    */ {
/*    */   static final int DEFAULT_NUMBER_OF_BUFFERS = 64;
/*    */   static final int DEFAULT_BUFFER_SIZE = 256;
/* 30 */   int bufferSize = 256;
/*    */   
/*    */ 
/*    */   public CyclicBufferTracker()
/*    */   {
/* 35 */     setMaxComponents(64);
/*    */   }
/*    */   
/*    */   public int getBufferSize() {
/* 39 */     return this.bufferSize;
/*    */   }
/*    */   
/*    */   public void setBufferSize(int bufferSize) {
/* 43 */     this.bufferSize = bufferSize;
/*    */   }
/*    */   
/*    */   protected void processPriorToRemoval(CyclicBuffer<E> component)
/*    */   {
/* 48 */     component.clear();
/*    */   }
/*    */   
/*    */   protected CyclicBuffer<E> buildComponent(String key)
/*    */   {
/* 53 */     return new CyclicBuffer(this.bufferSize);
/*    */   }
/*    */   
/*    */   protected boolean isComponentStale(CyclicBuffer<E> eCyclicBuffer)
/*    */   {
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   List<String> liveKeysAsOrderedList()
/*    */   {
/* 63 */     return new ArrayList(this.liveMap.keySet());
/*    */   }
/*    */   
/*    */   List<String> lingererKeysAsOrderedList() {
/* 67 */     return new ArrayList(this.lingerersMap.keySet());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\CyclicBufferTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */