/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class206;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Class196 {
    private static Map<String, Method> straight$ = new HashMap<String, Method>();

    public static Method _google(Class<?> class_, String string, Class<?> class_2, Class ... arrclass) {
        if (straight$.containsKey(string)) {
            return straight$.get(string);
        }
        Method[] arrmethod = class_.getDeclaredMethods();
        int n = arrmethod.length;
        for (int i = 113 - 155 + 54 + -12; i < n; ++i) {
            Method method = arrmethod[i];
            if (method.getReturnType() != class_2) continue;
            if (arrclass != null && arrclass.length != 0) {
                if (method.getParameterTypes().length != arrclass.length) continue;
                int n2 = 110 - 215 + 22 + 84;
                for (int j = 56 - 72 + 24 + -8; j < arrclass.length; ++j) {
                    if (method.getParameterTypes()[j] == arrclass[j]) continue;
                    n2 = 217 - 352 + 152 - 151 + 134;
                }
                if (n2 == 0) continue;
                straight$.putIfAbsent(string, method);
                try {
                    Class206._debian(method);
                }
                catch (ReflectiveOperationException reflectiveOperationException) {
                    reflectiveOperationException.printStackTrace();
                }
                return method;
            }
            straight$.putIfAbsent(string, method);
            try {
                Class206._debian(method);
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                reflectiveOperationException.printStackTrace();
            }
            return method;
        }
        return null;
    }
}

