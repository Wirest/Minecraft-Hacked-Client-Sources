package org.reflections.adapters;

import java.util.List;
import org.reflections.vfs.Vfs;

public interface MetadataAdapter {
   String getClassName(Object var1);

   String getSuperclassName(Object var1);

   List getInterfacesNames(Object var1);

   List getFields(Object var1);

   List getMethods(Object var1);

   String getMethodName(Object var1);

   List getParameterNames(Object var1);

   List getClassAnnotationNames(Object var1);

   List getFieldAnnotationNames(Object var1);

   List getMethodAnnotationNames(Object var1);

   List getParameterAnnotationNames(Object var1, int var2);

   String getReturnTypeName(Object var1);

   String getFieldName(Object var1);

   Object getOfCreateClassObject(Vfs.File var1) throws Exception;

   String getMethodModifier(Object var1);

   String getMethodKey(Object var1, Object var2);

   String getMethodFullKey(Object var1, Object var2);

   boolean isPublic(Object var1);
}
