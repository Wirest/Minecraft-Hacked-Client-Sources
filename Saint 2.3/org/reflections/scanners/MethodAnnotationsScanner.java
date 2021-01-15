package org.reflections.scanners;

import java.util.Iterator;

public class MethodAnnotationsScanner extends AbstractScanner {
   public void scan(Object cls) {
      Iterator i$ = this.getMetadataAdapter().getMethods(cls).iterator();

      while(i$.hasNext()) {
         Object method = i$.next();
         Iterator i$ = this.getMetadataAdapter().getMethodAnnotationNames(method).iterator();

         while(i$.hasNext()) {
            String methodAnnotation = (String)i$.next();
            if (this.acceptResult(methodAnnotation)) {
               this.getStore().put(methodAnnotation, this.getMetadataAdapter().getMethodFullKey(cls, method));
            }
         }
      }

   }
}
