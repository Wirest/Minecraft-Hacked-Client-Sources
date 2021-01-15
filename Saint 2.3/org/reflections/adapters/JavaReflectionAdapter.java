package org.reflections.adapters;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.reflections.ReflectionUtils;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public class JavaReflectionAdapter implements MetadataAdapter {
   public List getFields(Class cls) {
      return Lists.newArrayList(cls.getDeclaredFields());
   }

   public List getMethods(Class cls) {
      List methods = Lists.newArrayList();
      methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
      methods.addAll(Arrays.asList(cls.getDeclaredConstructors()));
      return methods;
   }

   public String getMethodName(Member method) {
      return method instanceof Method ? method.getName() : (method instanceof Constructor ? "<init>" : null);
   }

   public List getParameterNames(Member member) {
      List result = Lists.newArrayList();
      Class[] parameterTypes = member instanceof Method ? ((Method)member).getParameterTypes() : (member instanceof Constructor ? ((Constructor)member).getParameterTypes() : null);
      if (parameterTypes != null) {
         Class[] arr$ = parameterTypes;
         int len$ = parameterTypes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class paramType = arr$[i$];
            String name = getName(paramType);
            result.add(name);
         }
      }

      return result;
   }

   public List getClassAnnotationNames(Class aClass) {
      return this.getAnnotationNames(aClass.getDeclaredAnnotations());
   }

   public List getFieldAnnotationNames(Field field) {
      return this.getAnnotationNames(field.getDeclaredAnnotations());
   }

   public List getMethodAnnotationNames(Member method) {
      Annotation[] annotations = method instanceof Method ? ((Method)method).getDeclaredAnnotations() : (method instanceof Constructor ? ((Constructor)method).getDeclaredAnnotations() : null);
      return this.getAnnotationNames(annotations);
   }

   public List getParameterAnnotationNames(Member method, int parameterIndex) {
      Annotation[][] annotations = method instanceof Method ? ((Method)method).getParameterAnnotations() : (method instanceof Constructor ? ((Constructor)method).getParameterAnnotations() : (Annotation[][])null);
      return this.getAnnotationNames(annotations != null ? annotations[parameterIndex] : null);
   }

   public String getReturnTypeName(Member method) {
      return ((Method)method).getReturnType().getName();
   }

   public String getFieldName(Field field) {
      return field.getName();
   }

   public Class getOfCreateClassObject(Vfs.File file) throws Exception {
      return this.getOfCreateClassObject(file, (ClassLoader[])null);
   }

   public Class getOfCreateClassObject(Vfs.File file, @Nullable ClassLoader... loaders) throws Exception {
      String name = file.getRelativePath().replace("/", ".").replace(".class", "");
      return ReflectionUtils.forName(name, loaders);
   }

   public String getMethodModifier(Member method) {
      return Modifier.toString(method.getModifiers());
   }

   public String getMethodKey(Class cls, Member method) {
      return this.getMethodName(method) + "(" + Joiner.on(", ").join(this.getParameterNames(method)) + ")";
   }

   public String getMethodFullKey(Class cls, Member method) {
      return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
   }

   public boolean isPublic(Object o) {
      Integer mod = o instanceof Class ? ((Class)o).getModifiers() : o instanceof Member ? ((Member)o).getModifiers() : null;
      return mod != null && Modifier.isPublic(mod);
   }

   public String getClassName(Class cls) {
      return cls.getName();
   }

   public String getSuperclassName(Class cls) {
      Class superclass = cls.getSuperclass();
      return superclass != null ? superclass.getName() : "";
   }

   public List getInterfacesNames(Class cls) {
      Class[] classes = cls.getInterfaces();
      List names = new ArrayList(classes != null ? classes.length : 0);
      if (classes != null) {
         Class[] arr$ = classes;
         int len$ = classes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class cls1 = arr$[i$];
            names.add(cls1.getName());
         }
      }

      return names;
   }

   private List getAnnotationNames(Annotation[] annotations) {
      List names = new ArrayList(annotations.length);
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         names.add(annotation.annotationType().getName());
      }

      return names;
   }

   public static String getName(Class type) {
      if (type.isArray()) {
         try {
            Class cl = type;

            int dim;
            for(dim = 0; cl.isArray(); cl = cl.getComponentType()) {
               ++dim;
            }

            return cl.getName() + Utils.repeat("[]", dim);
         } catch (Throwable var3) {
         }
      }

      return type.getName();
   }
}
