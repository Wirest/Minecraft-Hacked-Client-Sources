package rip.autumn.utils.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import rip.autumn.utils.factory.exception.FactoryException;

public final class ClassFactory {
   public static Object create(Class objectClass, Object... arguments) {
      Constructor constructor;
      try {
         List list = new ArrayList();
         Object[] var4 = arguments;
         int var5 = arguments.length;
         int var6 = 0;

         while(true) {
            if (var6 >= var5) {
               constructor = objectClass.getDeclaredConstructor((Class[])list.toArray(new Class[0]));
               break;
            }

            Object argument = var4[var6];
            Class aClass = argument.getClass();
            list.add(aClass);
            ++var6;
         }
      } catch (NoSuchMethodException var10) {
         var10.printStackTrace();
         throw new FactoryException("Constructor not found! - Cannot create class.");
      }

      if (!constructor.isAccessible()) {
         constructor.setAccessible(true);
      }

      try {
         return constructor.newInstance(arguments);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var9) {
         var9.printStackTrace();
         throw new FactoryException("An error occurred while instantiating the class!");
      }
   }
}
