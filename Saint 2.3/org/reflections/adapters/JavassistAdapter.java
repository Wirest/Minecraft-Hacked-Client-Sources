package org.reflections.adapters;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import org.reflections.ReflectionsException;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public class JavassistAdapter implements MetadataAdapter {
   public static boolean includeInvisibleTag = true;

   public List getFields(ClassFile cls) {
      return cls.getFields();
   }

   public List getMethods(ClassFile cls) {
      return cls.getMethods();
   }

   public String getMethodName(MethodInfo method) {
      return method.getName();
   }

   public List getParameterNames(MethodInfo method) {
      String descriptor = method.getDescriptor();
      descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
      return this.splitDescriptorToTypeNames(descriptor);
   }

   public List getClassAnnotationNames(ClassFile aClass) {
      return this.getAnnotationNames((AnnotationsAttribute)aClass.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)aClass.getAttribute("RuntimeInvisibleAnnotations") : null);
   }

   public List getFieldAnnotationNames(FieldInfo field) {
      return this.getAnnotationNames((AnnotationsAttribute)field.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)field.getAttribute("RuntimeInvisibleAnnotations") : null);
   }

   public List getMethodAnnotationNames(MethodInfo method) {
      return this.getAnnotationNames((AnnotationsAttribute)method.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)method.getAttribute("RuntimeInvisibleAnnotations") : null);
   }

   public List getParameterAnnotationNames(MethodInfo method, int parameterIndex) {
      List result = Lists.newArrayList();
      List parameterAnnotationsAttributes = Lists.newArrayList(new ParameterAnnotationsAttribute[]{(ParameterAnnotationsAttribute)method.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)method.getAttribute("RuntimeInvisibleParameterAnnotations")});
      if (parameterAnnotationsAttributes != null) {
         Iterator i$ = parameterAnnotationsAttributes.iterator();

         while(i$.hasNext()) {
            ParameterAnnotationsAttribute parameterAnnotationsAttribute = (ParameterAnnotationsAttribute)i$.next();
            if (parameterAnnotationsAttribute != null) {
               Annotation[][] annotations = parameterAnnotationsAttribute.getAnnotations();
               if (parameterIndex < annotations.length) {
                  Annotation[] annotation = annotations[parameterIndex];
                  result.addAll(this.getAnnotationNames(annotation));
               }
            }
         }
      }

      return result;
   }

   public String getReturnTypeName(MethodInfo method) {
      String descriptor = method.getDescriptor();
      descriptor = descriptor.substring(descriptor.lastIndexOf(")") + 1);
      return (String)this.splitDescriptorToTypeNames(descriptor).get(0);
   }

   public String getFieldName(FieldInfo field) {
      return field.getName();
   }

   public ClassFile getOfCreateClassObject(Vfs.File file) {
      InputStream inputStream = null;

      ClassFile var4;
      try {
         inputStream = file.openInputStream();
         DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
         var4 = new ClassFile(dis);
      } catch (IOException var8) {
         throw new ReflectionsException("could not create class file from " + file.getName(), var8);
      } finally {
         Utils.close(inputStream);
      }

      return var4;
   }

   public String getMethodModifier(MethodInfo method) {
      int accessFlags = method.getAccessFlags();
      return AccessFlag.isPrivate(accessFlags) ? "private" : (AccessFlag.isProtected(accessFlags) ? "protected" : (this.isPublic(accessFlags) ? "public" : ""));
   }

   public String getMethodKey(ClassFile cls, MethodInfo method) {
      return this.getMethodName(method) + "(" + Joiner.on(", ").join(this.getParameterNames(method)) + ")";
   }

   public String getMethodFullKey(ClassFile cls, MethodInfo method) {
      return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
   }

   public boolean isPublic(Object o) {
      Integer accessFlags = o instanceof ClassFile ? ((ClassFile)o).getAccessFlags() : (o instanceof FieldInfo ? ((FieldInfo)o).getAccessFlags() : o instanceof MethodInfo ? ((MethodInfo)o).getAccessFlags() : null);
      return accessFlags != null && AccessFlag.isPublic(accessFlags);
   }

   public String getClassName(ClassFile cls) {
      return cls.getName();
   }

   public String getSuperclassName(ClassFile cls) {
      return cls.getSuperclass();
   }

   public List getInterfacesNames(ClassFile cls) {
      return Arrays.asList(cls.getInterfaces());
   }

   private List getAnnotationNames(AnnotationsAttribute... annotationsAttributes) {
      List result = Lists.newArrayList();
      if (annotationsAttributes != null) {
         AnnotationsAttribute[] arr$ = annotationsAttributes;
         int len$ = annotationsAttributes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            AnnotationsAttribute annotationsAttribute = arr$[i$];
            if (annotationsAttribute != null) {
               Annotation[] arr$ = annotationsAttribute.getAnnotations();
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Annotation annotation = arr$[i$];
                  result.add(annotation.getTypeName());
               }
            }
         }
      }

      return result;
   }

   private List getAnnotationNames(Annotation[] annotations) {
      List result = Lists.newArrayList();
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         result.add(annotation.getTypeName());
      }

      return result;
   }

   private List splitDescriptorToTypeNames(String descriptors) {
      List result = Lists.newArrayList();
      if (descriptors != null && descriptors.length() != 0) {
         List indices = Lists.newArrayList();
         Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);

         while(iterator.hasNext()) {
            indices.add(iterator.next());
         }

         indices.add(descriptors.length());

         for(int i = 0; i < indices.size() - 1; ++i) {
            String s1 = Descriptor.toString(descriptors.substring((Integer)indices.get(i), (Integer)indices.get(i + 1)));
            result.add(s1);
         }
      }

      return result;
   }
}
