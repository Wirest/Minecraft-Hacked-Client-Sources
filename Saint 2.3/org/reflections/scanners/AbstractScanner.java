package org.reflections.scanners;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import org.reflections.Configuration;
import org.reflections.ReflectionsException;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.vfs.Vfs;

public abstract class AbstractScanner implements Scanner {
   private Configuration configuration;
   private Multimap store;
   private Predicate resultFilter = Predicates.alwaysTrue();

   public boolean acceptsInput(String file) {
      return file.endsWith(".class");
   }

   public Object scan(Vfs.File file, Object classObject) {
      if (classObject == null) {
         try {
            classObject = this.configuration.getMetadataAdapter().getOfCreateClassObject(file);
         } catch (Exception var4) {
            throw new ReflectionsException("could not create class object from file " + file.getRelativePath());
         }
      }

      this.scan(classObject);
      return classObject;
   }

   public abstract void scan(Object var1);

   public Configuration getConfiguration() {
      return this.configuration;
   }

   public void setConfiguration(Configuration configuration) {
      this.configuration = configuration;
   }

   public Multimap getStore() {
      return this.store;
   }

   public void setStore(Multimap store) {
      this.store = store;
   }

   public Predicate getResultFilter() {
      return this.resultFilter;
   }

   public void setResultFilter(Predicate resultFilter) {
      this.resultFilter = resultFilter;
   }

   public Scanner filterResultsBy(Predicate filter) {
      this.setResultFilter(filter);
      return this;
   }

   public boolean acceptResult(String fqn) {
      return fqn != null && this.resultFilter.apply(fqn);
   }

   protected MetadataAdapter getMetadataAdapter() {
      return this.configuration.getMetadataAdapter();
   }

   public boolean equals(Object o) {
      return this == o || o != null && this.getClass() == o.getClass();
   }

   public int hashCode() {
      return this.getClass().hashCode();
   }
}
