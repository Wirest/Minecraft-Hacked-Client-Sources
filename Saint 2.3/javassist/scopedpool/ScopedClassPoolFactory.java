package javassist.scopedpool;

import javassist.ClassPool;

public interface ScopedClassPoolFactory {
   ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository);

   ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository);
}
