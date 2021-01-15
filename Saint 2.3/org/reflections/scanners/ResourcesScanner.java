package org.reflections.scanners;

import org.reflections.vfs.Vfs;

public class ResourcesScanner extends AbstractScanner {
   public boolean acceptsInput(String file) {
      return !file.endsWith(".class");
   }

   public Object scan(Vfs.File file, Object classObject) {
      this.getStore().put(file.getName(), file.getRelativePath());
      return classObject;
   }

   public void scan(Object cls) {
      throw new UnsupportedOperationException();
   }
}
