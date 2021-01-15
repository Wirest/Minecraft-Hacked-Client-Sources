package org.reflections;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.Utils;

public abstract class ReflectionUtils {
   public static boolean includeObject = false;
   private static List primitiveNames;
   private static List primitiveTypes;
   private static List primitiveDescriptors;

   public static Set getAllSuperTypes(Class type) {
      Set result = Sets.newHashSet();
      if (type != null && (includeObject || !type.equals(Object.class))) {
         result.add(type);
         result.addAll(getAllSuperTypes(type.getSuperclass()));
         Class[] arr$ = type.getInterfaces();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class ifc = arr$[i$];
            result.addAll(getAllSuperTypes(ifc));
         }
      }

      return result;
   }

   public static Set getAllSuperTypes(Class type, Predicate... predicates) {
      return filter(getAllSuperTypes(type), predicates);
   }

   public static Set getAllMethods(Class type, Predicate... predicates) {
      Set result = Sets.newHashSet();
      Iterator i$ = getAllSuperTypes(type).iterator();

      while(i$.hasNext()) {
         Class t = (Class)i$.next();
         result.addAll(getMethods(t, predicates));
      }

      return result;
   }

   public static Set getMethods(Class t, Predicate... predicates) {
      return filter((Object[])(t.isInterface() ? t.getMethods() : t.getDeclaredMethods()), predicates);
   }

   public static Set getAllConstructors(Class type, Predicate... predicates) {
      Set result = Sets.newHashSet();
      Iterator i$ = getAllSuperTypes(type).iterator();

      while(i$.hasNext()) {
         Class t = (Class)i$.next();
         result.addAll(getConstructors(t, predicates));
      }

      return result;
   }

   public static Set getConstructors(Class t, Predicate... predicates) {
      return filter((Object[])t.getDeclaredConstructors(), predicates);
   }

   public static Set getAllFields(Class type, Predicate... predicates) {
      Set result = Sets.newHashSet();
      Iterator i$ = getAllSuperTypes(type).iterator();

      while(i$.hasNext()) {
         Class t = (Class)i$.next();
         result.addAll(getFields(t, predicates));
      }

      return result;
   }

   public static Set getFields(Class type, Predicate... predicates) {
      return filter((Object[])type.getDeclaredFields(), predicates);
   }

   public static Set getAllAnnotations(AnnotatedElement type, Predicate... predicates) {
      Set result = Sets.newHashSet();
      if (type instanceof Class) {
         Iterator i$ = getAllSuperTypes((Class)type).iterator();

         while(i$.hasNext()) {
            Class t = (Class)i$.next();
            result.addAll(getAnnotations(t, predicates));
         }
      } else {
         result.addAll(getAnnotations(type, predicates));
      }

      return result;
   }

   public static Set getAnnotations(AnnotatedElement type, Predicate... predicates) {
      return filter((Object[])type.getDeclaredAnnotations(), predicates);
   }

   public static Set getAll(Set elements, Predicate... predicates) {
      return (Set)(Utils.isEmpty((Object[])predicates) ? elements : Sets.newHashSet(Iterables.filter(elements, Predicates.and(predicates))));
   }

   public static Predicate withName(final String name) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && input.getName().equals(name);
         }
      };
   }

   public static Predicate withPrefix(final String prefix) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && input.getName().startsWith(prefix);
         }
      };
   }

   public static Predicate withPattern(final String regex) {
      return new Predicate() {
         public boolean apply(@Nullable AnnotatedElement input) {
            return Pattern.matches(regex, input.toString());
         }
      };
   }

   public static Predicate withAnnotation(final Class annotation) {
      return new Predicate() {
         public boolean apply(@Nullable AnnotatedElement input) {
            return input != null && input.isAnnotationPresent(annotation);
         }
      };
   }

   public static Predicate withAnnotations(final Class... annotations) {
      return new Predicate() {
         public boolean apply(@Nullable AnnotatedElement input) {
            return input != null && Arrays.equals(annotations, input.getAnnotations());
         }
      };
   }

   public static Predicate withAnnotation(final Annotation annotation) {
      return new Predicate() {
         public boolean apply(@Nullable AnnotatedElement input) {
            return input != null && input.isAnnotationPresent(annotation.annotationType()) && ReflectionUtils.areAnnotationMembersMatching(input.getAnnotation(annotation.annotationType()), annotation);
         }
      };
   }

   public static Predicate withAnnotations(final Annotation... annotations) {
      return new Predicate() {
         public boolean apply(@Nullable AnnotatedElement input) {
            if (input != null) {
               Annotation[] inputAnnotations = input.getAnnotations();
               if (inputAnnotations.length == annotations.length) {
                  for(int i = 0; i < inputAnnotations.length; ++i) {
                     if (!ReflectionUtils.areAnnotationMembersMatching(inputAnnotations[i], annotations[i])) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }
      };
   }

   public static Predicate withParameters(final Class... types) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return Arrays.equals(ReflectionUtils.parameterTypes(input), types);
         }
      };
   }

   public static Predicate withParametersAssignableTo(final Class... types) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            if (input != null) {
               Class[] parameterTypes = ReflectionUtils.parameterTypes(input);
               if (parameterTypes.length == types.length) {
                  for(int i = 0; i < parameterTypes.length; ++i) {
                     if (!types[i].isAssignableFrom(parameterTypes[i])) {
                        return false;
                     }
                  }

                  return true;
               }
            }

            return false;
         }
      };
   }

   public static Predicate withParametersCount(final int count) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && ReflectionUtils.parameterTypes(input).length == count;
         }
      };
   }

   public static Predicate withAnyParameterAnnotation(final Class annotationClass) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && Iterables.any(ReflectionUtils.parameterAnnotationTypes(input), new Predicate() {
               public boolean apply(@Nullable Class input) {
                  return input.equals(annotationClass);
               }
            });
         }
      };
   }

   public static Predicate withAnyParameterAnnotation(final Annotation annotation) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && Iterables.any(ReflectionUtils.parameterAnnotations(input), new Predicate() {
               public boolean apply(@Nullable Annotation input) {
                  return ReflectionUtils.areAnnotationMembersMatching(annotation, input);
               }
            });
         }
      };
   }

   public static Predicate withType(final Class type) {
      return new Predicate() {
         public boolean apply(@Nullable Field input) {
            return input != null && input.getType().equals(type);
         }
      };
   }

   public static Predicate withTypeAssignableTo(final Class type) {
      return new Predicate() {
         public boolean apply(@Nullable Field input) {
            return input != null && type.isAssignableFrom(input.getType());
         }
      };
   }

   public static Predicate withReturnType(final Class type) {
      return new Predicate() {
         public boolean apply(@Nullable Method input) {
            return input != null && input.getReturnType().equals(type);
         }
      };
   }

   public static Predicate withReturnTypeAssignableTo(final Class type) {
      return new Predicate() {
         public boolean apply(@Nullable Method input) {
            return input != null && type.isAssignableFrom(input.getReturnType());
         }
      };
   }

   public static Predicate withModifier(final int mod) {
      return new Predicate() {
         public boolean apply(@Nullable Member input) {
            return input != null && (input.getModifiers() & mod) != 0;
         }
      };
   }

   public static Class forName(String typeName, ClassLoader... classLoaders) {
      if (getPrimitiveNames().contains(typeName)) {
         return (Class)getPrimitiveTypes().get(getPrimitiveNames().indexOf(typeName));
      } else {
         String type;
         if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            type = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");
            if (getPrimitiveNames().contains(type)) {
               type = (String)getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
            } else {
               type = "L" + type + ";";
            }

            type = array + type;
         } else {
            type = typeName;
         }

         ClassLoader[] arr$ = ClasspathHelper.classLoaders(classLoaders);
         int len$ = arr$.length;
         int i$ = 0;

         while(i$ < len$) {
            ClassLoader classLoader = arr$[i$];
            if (type.contains("[")) {
               try {
                  return Class.forName(type, false, classLoader);
               } catch (Throwable var9) {
               }
            }

            try {
               return classLoader.loadClass(type);
            } catch (Throwable var8) {
               ++i$;
            }
         }

         throw new ReflectionsException("could not get type for name " + typeName);
      }
   }

   public static List forNames(Iterable classes, ClassLoader... classLoaders) {
      List result = new ArrayList();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         String className = (String)i$.next();
         result.add(forName(className, classLoaders));
      }

      return result;
   }

   @Nonnull
   public static List names(Class... types) {
      List names = new ArrayList();
      if (types != null) {
         Class[] arr$ = types;
         int len$ = types.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class type = arr$[i$];
            names.add(type.getName());
         }
      }

      return names;
   }

   public static List names(Collection types) {
      List result = new ArrayList(types.size());
      Iterator i$ = types.iterator();

      while(i$.hasNext()) {
         Class type = (Class)i$.next();
         result.add(type.getName());
      }

      return result;
   }

   private static Class[] parameterTypes(Member member) {
      return member != null ? (member.getClass() == Method.class ? ((Method)member).getParameterTypes() : (member.getClass() == Constructor.class ? ((Constructor)member).getParameterTypes() : null)) : null;
   }

   private static Set parameterAnnotations(Member member) {
      Set result = Sets.newHashSet();
      Annotation[][] annotations = member instanceof Method ? ((Method)member).getParameterAnnotations() : (member instanceof Constructor ? ((Constructor)member).getParameterAnnotations() : (Annotation[][])null);
      Annotation[][] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation[] annotation = arr$[i$];
         Collections.addAll(result, annotation);
      }

      return result;
   }

   private static Set parameterAnnotationTypes(Member member) {
      Set result = Sets.newHashSet();
      Iterator i$ = parameterAnnotations(member).iterator();

      while(i$.hasNext()) {
         Annotation annotation = (Annotation)i$.next();
         result.add(annotation.annotationType());
      }

      return result;
   }

   private static void initPrimitives() {
      if (primitiveNames == null) {
         primitiveNames = Lists.newArrayList(new String[]{"boolean", "char", "byte", "short", "int", "long", "float", "double", "void"});
         primitiveTypes = Lists.newArrayList(new Class[]{Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE});
         primitiveDescriptors = Lists.newArrayList(new String[]{"Z", "C", "B", "S", "I", "J", "F", "D", "V"});
      }

   }

   private static List getPrimitiveNames() {
      initPrimitives();
      return primitiveNames;
   }

   private static List getPrimitiveTypes() {
      initPrimitives();
      return primitiveTypes;
   }

   private static List getPrimitiveDescriptors() {
      initPrimitives();
      return primitiveDescriptors;
   }

   private static Set filter(Object[] elements, Predicate... predicates) {
      return (Set)(Utils.isEmpty((Object[])predicates) ? Sets.newHashSet(elements) : Sets.filter(Sets.newHashSet(elements), Predicates.and(predicates)));
   }

   private static Set filter(Set elements, Predicate... predicates) {
      return Utils.isEmpty((Object[])predicates) ? elements : Sets.filter(elements, Predicates.and(predicates));
   }

   private static boolean areAnnotationMembersMatching(Annotation annotation1, Annotation annotation2) {
      if (annotation2 != null && annotation1.annotationType() == annotation2.annotationType()) {
         Method[] arr$ = annotation1.annotationType().getDeclaredMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];

            try {
               if (!method.invoke(annotation1).equals(method.invoke(annotation2))) {
                  return false;
               }
            } catch (Exception var7) {
               throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", method.getName(), annotation1.annotationType()), var7);
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
