package org.reflections.scanners;

import com.google.common.base.Joiner;
import java.util.Iterator;

public class TypeElementsScanner extends AbstractScanner {
   private boolean includeFields = true;
   private boolean includeMethods = true;
   private boolean includeAnnotations = true;
   private boolean publicOnly = true;

   public void scan(Object cls) {
      String className = this.getMetadataAdapter().getClassName(cls);
      this.getStore().put(className, className);
      Iterator i$;
      Object method;
      String methodKey;
      if (this.includeFields) {
         i$ = this.getMetadataAdapter().getFields(cls).iterator();

         while(i$.hasNext()) {
            method = i$.next();
            methodKey = this.getMetadataAdapter().getFieldName(method);
            this.getStore().put(className, methodKey);
         }
      }

      if (this.includeMethods) {
         i$ = this.getMetadataAdapter().getMethods(cls).iterator();

         label36:
         while(true) {
            do {
               if (!i$.hasNext()) {
                  break label36;
               }

               method = i$.next();
            } while(this.publicOnly && !this.getMetadataAdapter().isPublic(method));

            methodKey = this.getMetadataAdapter().getMethodName(method) + "(" + Joiner.on(", ").join(this.getMetadataAdapter().getParameterNames(method)) + ")";
            this.getStore().put(className, methodKey);
         }
      }

      if (this.includeAnnotations) {
         i$ = this.getMetadataAdapter().getClassAnnotationNames(cls).iterator();

         while(i$.hasNext()) {
            method = i$.next();
            this.getStore().put(className, "@" + method);
         }
      }

   }

   public TypeElementsScanner includeFields() {
      return this.includeFields(true);
   }

   public TypeElementsScanner includeFields(boolean include) {
      this.includeFields = include;
      return this;
   }

   public TypeElementsScanner includeMethods() {
      return this.includeMethods(true);
   }

   public TypeElementsScanner includeMethods(boolean include) {
      this.includeMethods = include;
      return this;
   }

   public TypeElementsScanner publicOnly(boolean only) {
      this.publicOnly = only;
      return this;
   }

   public TypeElementsScanner publicOnly() {
      return this.publicOnly(true);
   }
}
