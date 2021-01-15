package javassist;

public abstract class CtMember {
   CtMember next;
   protected CtClass declaringClass;

   protected CtMember(CtClass clazz) {
      this.declaringClass = clazz;
      this.next = null;
   }

   final CtMember next() {
      return this.next;
   }

   void nameReplaced() {
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(this.getClass().getName());
      buffer.append("@");
      buffer.append(Integer.toHexString(this.hashCode()));
      buffer.append("[");
      buffer.append(Modifier.toString(this.getModifiers()));
      this.extendToString(buffer);
      buffer.append("]");
      return buffer.toString();
   }

   protected abstract void extendToString(StringBuffer buffer);

   public CtClass getDeclaringClass() {
      return this.declaringClass;
   }

   public boolean visibleFrom(CtClass clazz) {
      int mod = this.getModifiers();
      if (Modifier.isPublic(mod)) {
         return true;
      } else if (Modifier.isPrivate(mod)) {
         return clazz == this.declaringClass;
      } else {
         String declName = this.declaringClass.getPackageName();
         String fromName = clazz.getPackageName();
         boolean visible;
         if (declName == null) {
            visible = fromName == null;
         } else {
            visible = declName.equals(fromName);
         }

         return !visible && Modifier.isProtected(mod) ? clazz.subclassOf(this.declaringClass) : visible;
      }
   }

   public abstract int getModifiers();

   public abstract void setModifiers(int mod);

   public abstract boolean hasAnnotation(Class clz);

   public abstract Object getAnnotation(Class clz) throws ClassNotFoundException;

   public abstract Object[] getAnnotations() throws ClassNotFoundException;

   public abstract Object[] getAvailableAnnotations();

   public abstract String getName();

   public abstract String getSignature();

   public abstract String getGenericSignature();

   public abstract void setGenericSignature(String sig);

   public abstract byte[] getAttribute(String name);

   public abstract void setAttribute(String name, byte[] data);

   static class Cache extends CtMember {
      private CtMember methodTail = this;
      private CtMember consTail = this;
      private CtMember fieldTail = this;

      protected void extendToString(StringBuffer buffer) {
      }

      public boolean hasAnnotation(Class clz) {
         return false;
      }

      public Object getAnnotation(Class clz) throws ClassNotFoundException {
         return null;
      }

      public Object[] getAnnotations() throws ClassNotFoundException {
         return null;
      }

      public byte[] getAttribute(String name) {
         return null;
      }

      public Object[] getAvailableAnnotations() {
         return null;
      }

      public int getModifiers() {
         return 0;
      }

      public String getName() {
         return null;
      }

      public String getSignature() {
         return null;
      }

      public void setAttribute(String name, byte[] data) {
      }

      public void setModifiers(int mod) {
      }

      public String getGenericSignature() {
         return null;
      }

      public void setGenericSignature(String sig) {
      }

      Cache(CtClassType decl) {
         super(decl);
         this.fieldTail.next = this;
      }

      CtMember methodHead() {
         return this;
      }

      CtMember lastMethod() {
         return this.methodTail;
      }

      CtMember consHead() {
         return this.methodTail;
      }

      CtMember lastCons() {
         return this.consTail;
      }

      CtMember fieldHead() {
         return this.consTail;
      }

      CtMember lastField() {
         return this.fieldTail;
      }

      void addMethod(CtMember method) {
         method.next = this.methodTail.next;
         this.methodTail.next = method;
         if (this.methodTail == this.consTail) {
            this.consTail = method;
            if (this.methodTail == this.fieldTail) {
               this.fieldTail = method;
            }
         }

         this.methodTail = method;
      }

      void addConstructor(CtMember cons) {
         cons.next = this.consTail.next;
         this.consTail.next = cons;
         if (this.consTail == this.fieldTail) {
            this.fieldTail = cons;
         }

         this.consTail = cons;
      }

      void addField(CtMember field) {
         field.next = this;
         this.fieldTail.next = field;
         this.fieldTail = field;
      }

      static int count(CtMember head, CtMember tail) {
         int n;
         for(n = 0; head != tail; head = head.next) {
            ++n;
         }

         return n;
      }

      void remove(CtMember mem) {
         CtMember node;
         for(Object m = this; (node = ((CtMember)m).next) != this; m = ((CtMember)m).next) {
            if (node == mem) {
               ((CtMember)m).next = node.next;
               if (node == this.methodTail) {
                  this.methodTail = (CtMember)m;
               }

               if (node == this.consTail) {
                  this.consTail = (CtMember)m;
               }

               if (node == this.fieldTail) {
                  this.fieldTail = (CtMember)m;
               }
               break;
            }
         }

      }
   }
}
