package javassist.scopedpool;

import javassist.ClassPool;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory {
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
      return new ScopedClassPool(cl, src, repository, false);
   }

   public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository) {
      return new ScopedClassPool((ClassLoader)null, src, repository, true);
   }
}
