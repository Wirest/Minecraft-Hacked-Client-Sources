package javassist.util.proxy;

import java.lang.reflect.Method;

public interface MethodFilter {
   boolean isHandled(Method m);
}
