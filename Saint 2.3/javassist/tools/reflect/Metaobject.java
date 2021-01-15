package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Metaobject implements Serializable {
   protected ClassMetaobject classmetaobject;
   protected Metalevel baseobject;
   protected Method[] methods;

   public Metaobject(Object self, Object[] args) {
      this.baseobject = (Metalevel)self;
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
   }

   protected Metaobject() {
      this.baseobject = null;
      this.classmetaobject = null;
      this.methods = null;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.writeObject(this.baseobject);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.baseobject = (Metalevel)in.readObject();
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
   }

   public final ClassMetaobject getClassMetaobject() {
      return this.classmetaobject;
   }

   public final Object getObject() {
      return this.baseobject;
   }

   public final void setObject(Object self) {
      this.baseobject = (Metalevel)self;
      this.classmetaobject = this.baseobject._getClass();
      this.methods = this.classmetaobject.getReflectiveMethods();
      this.baseobject._setMetaobject(this);
   }

   public final String getMethodName(int identifier) {
      String mname = this.methods[identifier].getName();
      int j = 3;

      char c;
      do {
         c = mname.charAt(j++);
      } while(c >= '0' && '9' >= c);

      return mname.substring(j);
   }

   public final Class[] getParameterTypes(int identifier) {
      return this.methods[identifier].getParameterTypes();
   }

   public final Class getReturnType(int identifier) {
      return this.methods[identifier].getReturnType();
   }

   public Object trapFieldRead(String name) {
      Class jc = this.getClassMetaobject().getJavaClass();

      try {
         return jc.getField(name).get(this.getObject());
      } catch (NoSuchFieldException var4) {
         throw new RuntimeException(var4.toString());
      } catch (IllegalAccessException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public void trapFieldWrite(String name, Object value) {
      Class jc = this.getClassMetaobject().getJavaClass();

      try {
         jc.getField(name).set(this.getObject(), value);
      } catch (NoSuchFieldException var5) {
         throw new RuntimeException(var5.toString());
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
      try {
         return this.methods[identifier].invoke(this.getObject(), args);
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new CannotInvokeException(var5);
      }
   }
}
