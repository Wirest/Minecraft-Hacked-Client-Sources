package org.reflections.util;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.reflections.ReflectionUtils;
import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Utils {
   public static String repeat(String string, int times) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < times; ++i) {
         sb.append(string);
      }

      return sb.toString();
   }

   public static boolean isEmpty(String s) {
      return s == null || s.length() == 0;
   }

   public static boolean isEmpty(Object[] objects) {
      return objects == null || objects.length == 0;
   }

   public static File prepareFile(String filename) {
      File file = new File(filename);
      File parent = file.getAbsoluteFile().getParentFile();
      if (!parent.exists()) {
         parent.mkdirs();
      }

      return file;
   }

   public static Member getMemberFromDescriptor(String descriptor, ClassLoader... classLoaders) throws ReflectionsException {
      int p0 = descriptor.indexOf(40);
      String methodKey = descriptor.substring(0, p0);
      String methodParameters = descriptor.substring(p0 + 1, descriptor.length() - 1);
      int p1 = methodKey.lastIndexOf(46);
      String className = methodKey.substring(methodKey.lastIndexOf(32) + 1, p1);
      String methodName = methodKey.substring(p1 + 1);
      Class[] parameterTypes = null;
      if (!isEmpty(methodParameters)) {
         String[] parameterNames = methodParameters.split(",");
         List result = new ArrayList(parameterNames.length);
         String[] arr$ = parameterNames;
         int len$ = parameterNames.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String name = arr$[i$];
            result.add(ReflectionUtils.forName(name.trim()));
         }

         parameterTypes = (Class[])result.toArray(new Class[result.size()]);
      }

      Class aClass = ReflectionUtils.forName(className, classLoaders);

      try {
         return (Member)(isConstructor(descriptor) ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredMethod(methodName, parameterTypes));
      } catch (NoSuchMethodException var15) {
         throw new ReflectionsException("Can't resolve method named " + methodName, var15);
      }
   }

   public static Set getMethodsFromDescriptors(Collection annotatedWith, ClassLoader... classLoaders) {
      Set result = Sets.newHashSet();
      Iterator i$ = annotatedWith.iterator();

      while(i$.hasNext()) {
         String annotated = (String)i$.next();
         if (!isConstructor(annotated)) {
            Method member = (Method)getMemberFromDescriptor(annotated, classLoaders);
            if (member != null) {
               result.add(member);
            }
         }
      }

      return result;
   }

   public static Set getConstructorsFromDescriptors(Collection annotatedWith, ClassLoader... classLoaders) {
      Set result = Sets.newHashSet();
      Iterator i$ = annotatedWith.iterator();

      while(i$.hasNext()) {
         String annotated = (String)i$.next();
         if (isConstructor(annotated)) {
            Constructor member = (Constructor)getMemberFromDescriptor(annotated, classLoaders);
            if (member != null) {
               result.add(member);
            }
         }
      }

      return result;
   }

   public static boolean isConstructor(String fqn) {
      return fqn.contains("init>");
   }

   public static Field getFieldFromString(String field, ClassLoader... classLoaders) {
      String className = field.substring(0, field.lastIndexOf(46));
      String fieldName = field.substring(field.lastIndexOf(46) + 1);

      try {
         return ReflectionUtils.forName(className, classLoaders).getDeclaredField(fieldName);
      } catch (NoSuchFieldException var5) {
         throw new ReflectionsException("Can't resolve field named " + fieldName, var5);
      }
   }

   public static void close(InputStream closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   @Nullable
   public static Logger findLogger(Class aClass) {
      try {
         Class.forName("org.slf4j.impl.StaticLoggerBinder");
         return LoggerFactory.getLogger(aClass);
      } catch (Throwable var2) {
         return null;
      }
   }

   public static Set intersect(Collection ts1, Collection ts2) {
      Set result = Sets.newHashSet();
      Iterator i$ = ts1.iterator();

      while(i$.hasNext()) {
         Object t = i$.next();
         if (ts2.contains(t)) {
            result.add(t);
         }
      }

      return result;
   }
}
