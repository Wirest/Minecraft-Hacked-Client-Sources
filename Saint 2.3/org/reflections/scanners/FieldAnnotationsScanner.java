package org.reflections.scanners;

import java.util.Iterator;
import java.util.List;

public class FieldAnnotationsScanner extends AbstractScanner {
   public void scan(Object cls) {
      String className = this.getMetadataAdapter().getClassName(cls);
      List fields = this.getMetadataAdapter().getFields(cls);
      Iterator i$ = fields.iterator();

      while(i$.hasNext()) {
         Object field = i$.next();
         List fieldAnnotations = this.getMetadataAdapter().getFieldAnnotationNames(field);
         Iterator i$ = fieldAnnotations.iterator();

         while(i$.hasNext()) {
            String fieldAnnotation = (String)i$.next();
            if (this.acceptResult(fieldAnnotation)) {
               String fieldName = this.getMetadataAdapter().getFieldName(field);
               this.getStore().put(fieldAnnotation, String.format("%s.%s", className, fieldName));
            }
         }
      }

   }
}
