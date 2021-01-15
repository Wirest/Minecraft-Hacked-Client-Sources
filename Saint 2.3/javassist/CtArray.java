package javassist;

final class CtArray extends CtClass {
   protected ClassPool pool;
   private CtClass[] interfaces = null;

   CtArray(String name, ClassPool cp) {
      super(name);
      this.pool = cp;
   }

   public ClassPool getClassPool() {
      return this.pool;
   }

   public boolean isArray() {
      return true;
   }

   public int getModifiers() {
      int mod = 16;

      try {
         mod |= this.getComponentType().getModifiers() & 7;
      } catch (NotFoundException var3) {
      }

      return mod;
   }

   public CtClass[] getInterfaces() throws NotFoundException {
      if (this.interfaces == null) {
         Class[] intfs = Object[].class.getInterfaces();
         this.interfaces = new CtClass[intfs.length];

         for(int i = 0; i < intfs.length; ++i) {
            this.interfaces[i] = this.pool.get(intfs[i].getName());
         }
      }

      return this.interfaces;
   }

   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
      if (super.subtypeOf(clazz)) {
         return true;
      } else {
         String cname = clazz.getName();
         if (cname.equals("java.lang.Object")) {
            return true;
         } else {
            CtClass[] intfs = this.getInterfaces();

            for(int i = 0; i < intfs.length; ++i) {
               if (intfs[i].subtypeOf(clazz)) {
                  return true;
               }
            }

            return clazz.isArray() && this.getComponentType().subtypeOf(clazz.getComponentType());
         }
      }
   }

   public CtClass getComponentType() throws NotFoundException {
      String name = this.getName();
      return this.pool.get(name.substring(0, name.length() - 2));
   }

   public CtClass getSuperclass() throws NotFoundException {
      return this.pool.get("java.lang.Object");
   }

   public CtMethod[] getMethods() {
      try {
         return this.getSuperclass().getMethods();
      } catch (NotFoundException var2) {
         return super.getMethods();
      }
   }

   public CtMethod getMethod(String name, String desc) throws NotFoundException {
      return this.getSuperclass().getMethod(name, desc);
   }

   public CtConstructor[] getConstructors() {
      try {
         return this.getSuperclass().getConstructors();
      } catch (NotFoundException var2) {
         return super.getConstructors();
      }
   }
}
