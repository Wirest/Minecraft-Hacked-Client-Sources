package javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public abstract class TypeData {
   public static TypeData[] make(int size) {
      TypeData[] array = new TypeData[size];

      for(int i = 0; i < size; ++i) {
         array[i] = TypeTag.TOP;
      }

      return array;
   }

   protected TypeData() {
   }

   private static void setType(TypeData td, String className, ClassPool cp) throws BadBytecode {
      td.setType(className, cp);
   }

   public abstract int getTypeTag();

   public abstract int getTypeData(ConstPool cp);

   public TypeData join() {
      return new TypeData.TypeVar(this);
   }

   public abstract TypeData.BasicType isBasicType();

   public abstract boolean is2WordType();

   public boolean isNullType() {
      return false;
   }

   public boolean isUninit() {
      return false;
   }

   public abstract boolean eq(TypeData d);

   public abstract String getName();

   public abstract void setType(String s, ClassPool cp) throws BadBytecode;

   public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
      return index;
   }

   protected TypeData.TypeVar toTypeVar() {
      return null;
   }

   public void constructorCalled(int offset) {
   }

   public static CtClass commonSuperClassEx(CtClass one, CtClass two) throws NotFoundException {
      if (one == two) {
         return one;
      } else if (one.isArray() && two.isArray()) {
         CtClass ele1 = one.getComponentType();
         CtClass ele2 = two.getComponentType();
         CtClass element = commonSuperClassEx(ele1, ele2);
         if (element == ele1) {
            return one;
         } else {
            return element == ele2 ? two : one.getClassPool().get(element == null ? "java.lang.Object" : element.getName() + "[]");
         }
      } else if (!one.isPrimitive() && !two.isPrimitive()) {
         return !one.isArray() && !two.isArray() ? commonSuperClass(one, two) : one.getClassPool().get("java.lang.Object");
      } else {
         return null;
      }
   }

   public static CtClass commonSuperClass(CtClass one, CtClass two) throws NotFoundException {
      CtClass deep = one;
      CtClass shallow = two;
      CtClass backupDeep = one;

      while(true) {
         if (eq(deep, shallow) && deep.getSuperclass() != null) {
            return deep;
         }

         CtClass deepSuper = deep.getSuperclass();
         CtClass shallowSuper = shallow.getSuperclass();
         if (shallowSuper == null) {
            shallow = two;
            break;
         }

         if (deepSuper == null) {
            backupDeep = two;
            deep = shallow;
            shallow = one;
            break;
         }

         deep = deepSuper;
         shallow = shallowSuper;
      }

      while(true) {
         deep = deep.getSuperclass();
         if (deep == null) {
            for(deep = backupDeep; !eq(deep, shallow); shallow = shallow.getSuperclass()) {
               deep = deep.getSuperclass();
            }

            return deep;
         }

         backupDeep = backupDeep.getSuperclass();
      }
   }

   static boolean eq(CtClass one, CtClass two) {
      return one == two || one != null && two != null && one.getName().equals(two.getName());
   }

   public static void aastore(TypeData array, TypeData value, ClassPool cp) throws BadBytecode {
      if (array instanceof TypeData.AbsTypeVar && !value.isNullType()) {
         ((TypeData.AbsTypeVar)array).merge(TypeData.ArrayType.make(value));
      }

      if (value instanceof TypeData.AbsTypeVar) {
         if (array instanceof TypeData.AbsTypeVar) {
            TypeData.ArrayElement.make(array);
         } else {
            if (!(array instanceof TypeData.ClassName)) {
               throw new BadBytecode("bad AASTORE: " + array);
            }

            if (!array.isNullType()) {
               String type = TypeData.ArrayElement.typeName(array.getName());
               value.setType(type, cp);
            }
         }
      }

   }

   public static class UninitThis extends TypeData.UninitData {
      UninitThis(String className) {
         super(-1, className);
      }

      public TypeData.UninitData copy() {
         return new TypeData.UninitThis(this.getName());
      }

      public int getTypeTag() {
         return 6;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }

      public String toString() {
         return "uninit:this";
      }
   }

   public static class UninitData extends TypeData.ClassName {
      int offset;
      boolean initialized;

      UninitData(int offset, String className) {
         super(className);
         this.offset = offset;
         this.initialized = false;
      }

      public TypeData.UninitData copy() {
         return new TypeData.UninitData(this.offset, this.getName());
      }

      public int getTypeTag() {
         return 8;
      }

      public int getTypeData(ConstPool cp) {
         return this.offset;
      }

      public TypeData join() {
         return (TypeData)(this.initialized ? new TypeData.TypeVar(new TypeData.ClassName(this.getName())) : new TypeData.UninitTypeVar(this.copy()));
      }

      public boolean isUninit() {
         return true;
      }

      public boolean eq(TypeData d) {
         if (!(d instanceof TypeData.UninitData)) {
            return false;
         } else {
            TypeData.UninitData ud = (TypeData.UninitData)d;
            return this.offset == ud.offset && this.getName().equals(ud.getName());
         }
      }

      public String toString() {
         return "uninit:" + this.getName() + "@" + this.offset;
      }

      public int offset() {
         return this.offset;
      }

      public void constructorCalled(int offset) {
         if (offset == this.offset) {
            this.initialized = true;
         }

      }
   }

   public static class NullType extends TypeData.ClassName {
      public NullType() {
         super("null-type");
      }

      public int getTypeTag() {
         return 5;
      }

      public boolean isNullType() {
         return true;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }
   }

   public static class ClassName extends TypeData {
      private String name;

      public ClassName(String n) {
         this.name = n;
      }

      public String getName() {
         return this.name;
      }

      public TypeData.BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      public int getTypeTag() {
         return 7;
      }

      public int getTypeData(ConstPool cp) {
         return cp.addClassInfo(this.getName());
      }

      public boolean eq(TypeData d) {
         return this.name.equals(d.getName());
      }

      public void setType(String typeName, ClassPool cp) throws BadBytecode {
      }
   }

   public static class UninitTypeVar extends TypeData.AbsTypeVar {
      protected TypeData type;

      public UninitTypeVar(TypeData.UninitData t) {
         this.type = t;
      }

      public int getTypeTag() {
         return this.type.getTypeTag();
      }

      public int getTypeData(ConstPool cp) {
         return this.type.getTypeData(cp);
      }

      public TypeData.BasicType isBasicType() {
         return this.type.isBasicType();
      }

      public boolean is2WordType() {
         return this.type.is2WordType();
      }

      public boolean isUninit() {
         return this.type.isUninit();
      }

      public boolean eq(TypeData d) {
         return this.type.eq(d);
      }

      public String getName() {
         return this.type.getName();
      }

      protected TypeData.TypeVar toTypeVar() {
         return null;
      }

      public TypeData join() {
         return this.type.join();
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.type.setType(s, cp);
      }

      public void merge(TypeData t) {
         if (!t.eq(this.type)) {
            this.type = TypeTag.TOP;
         }

      }

      public void constructorCalled(int offset) {
         this.type.constructorCalled(offset);
      }

      public int offset() {
         if (this.type instanceof TypeData.UninitData) {
            return ((TypeData.UninitData)this.type).offset;
         } else {
            throw new RuntimeException("not available");
         }
      }
   }

   public static class ArrayElement extends TypeData.AbsTypeVar {
      private TypeData.AbsTypeVar array;

      private ArrayElement(TypeData.AbsTypeVar a) {
         this.array = a;
      }

      public static TypeData make(TypeData array) throws BadBytecode {
         if (array instanceof TypeData.ArrayType) {
            return ((TypeData.ArrayType)array).elementType();
         } else if (array instanceof TypeData.AbsTypeVar) {
            return new TypeData.ArrayElement((TypeData.AbsTypeVar)array);
         } else if (array instanceof TypeData.ClassName && !array.isNullType()) {
            return new TypeData.ClassName(typeName(array.getName()));
         } else {
            throw new BadBytecode("bad AASTORE: " + array);
         }
      }

      public void merge(TypeData t) {
         try {
            if (!t.isNullType()) {
               this.array.merge(TypeData.ArrayType.make(t));
            }

         } catch (BadBytecode var3) {
            throw new RuntimeException("fatal: " + var3);
         }
      }

      public String getName() {
         return typeName(this.array.getName());
      }

      public TypeData.AbsTypeVar arrayType() {
         return this.array;
      }

      public TypeData.BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      private static String typeName(String arrayType) {
         if (arrayType.length() > 1 && arrayType.charAt(0) == '[') {
            char c = arrayType.charAt(1);
            if (c == 'L') {
               return arrayType.substring(2, arrayType.length() - 1).replace('/', '.');
            }

            if (c == '[') {
               return arrayType.substring(1);
            }
         }

         return "java.lang.Object";
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.array.setType(TypeData.ArrayType.typeName(s), cp);
      }

      protected TypeData.TypeVar toTypeVar() {
         return this.array.toTypeVar();
      }

      public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
         return this.array.dfs(order, index, cp);
      }
   }

   public static class ArrayType extends TypeData.AbsTypeVar {
      private TypeData.AbsTypeVar element;

      private ArrayType(TypeData.AbsTypeVar elementType) {
         this.element = elementType;
      }

      static TypeData make(TypeData element) throws BadBytecode {
         if (element instanceof TypeData.ArrayElement) {
            return ((TypeData.ArrayElement)element).arrayType();
         } else if (element instanceof TypeData.AbsTypeVar) {
            return new TypeData.ArrayType((TypeData.AbsTypeVar)element);
         } else if (element instanceof TypeData.ClassName && !element.isNullType()) {
            return new TypeData.ClassName(typeName(element.getName()));
         } else {
            throw new BadBytecode("bad AASTORE: " + element);
         }
      }

      public void merge(TypeData t) {
         try {
            if (!t.isNullType()) {
               this.element.merge(TypeData.ArrayElement.make(t));
            }

         } catch (BadBytecode var3) {
            throw new RuntimeException("fatal: " + var3);
         }
      }

      public String getName() {
         return typeName(this.element.getName());
      }

      public TypeData.AbsTypeVar elementType() {
         return this.element;
      }

      public TypeData.BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      public static String typeName(String elementType) {
         return elementType.charAt(0) == '[' ? "[" + elementType : "[L" + elementType.replace('.', '/') + ";";
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.element.setType(TypeData.ArrayElement.typeName(s), cp);
      }

      protected TypeData.TypeVar toTypeVar() {
         return this.element.toTypeVar();
      }

      public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
         return this.element.dfs(order, index, cp);
      }
   }

   public static class TypeVar extends TypeData.AbsTypeVar {
      protected ArrayList lowers = new ArrayList(2);
      protected ArrayList usedBy = new ArrayList(2);
      protected ArrayList uppers = null;
      protected String fixedType;
      private boolean is2WordType;
      private int visited = 0;
      private int smallest = 0;
      private boolean inList = false;

      public TypeVar(TypeData t) {
         this.merge(t);
         this.fixedType = null;
         this.is2WordType = t.is2WordType();
      }

      public String getName() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getName() : this.fixedType;
      }

      public TypeData.BasicType isBasicType() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isBasicType() : null;
      }

      public boolean is2WordType() {
         return this.fixedType == null ? this.is2WordType : false;
      }

      public boolean isNullType() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isNullType() : false;
      }

      public boolean isUninit() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isUninit() : false;
      }

      public void merge(TypeData t) {
         this.lowers.add(t);
         if (t instanceof TypeData.TypeVar) {
            ((TypeData.TypeVar)t).usedBy.add(this);
         }

      }

      public int getTypeTag() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getTypeTag() : super.getTypeTag();
      }

      public int getTypeData(ConstPool cp) {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getTypeData(cp) : super.getTypeData(cp);
      }

      public void setType(String typeName, ClassPool cp) throws BadBytecode {
         if (this.uppers == null) {
            this.uppers = new ArrayList();
         }

         this.uppers.add(typeName);
      }

      protected TypeData.TypeVar toTypeVar() {
         return this;
      }

      public int dfs(ArrayList preOrder, int index, ClassPool cp) throws NotFoundException {
         if (this.visited > 0) {
            return index;
         } else {
            ++index;
            this.visited = this.smallest = index;
            preOrder.add(this);
            this.inList = true;
            int n = this.lowers.size();

            TypeData.TypeVar child;
            for(int i = 0; i < n; ++i) {
               child = ((TypeData)this.lowers.get(i)).toTypeVar();
               if (child != null) {
                  if (child.visited == 0) {
                     index = child.dfs(preOrder, index, cp);
                     if (child.smallest < this.smallest) {
                        this.smallest = child.smallest;
                     }
                  } else if (child.inList && child.visited < this.smallest) {
                     this.smallest = child.visited;
                  }
               }
            }

            if (this.visited == this.smallest) {
               ArrayList scc = new ArrayList();

               do {
                  child = (TypeData.TypeVar)preOrder.remove(preOrder.size() - 1);
                  child.inList = false;
                  scc.add(child);
               } while(child != this);

               this.fixTypes(scc, cp);
            }

            return index;
         }
      }

      private void fixTypes(ArrayList scc, ClassPool cp) throws NotFoundException {
         HashSet lowersSet = new HashSet();
         boolean isBasicType = false;
         TypeData kind = null;
         int size = scc.size();

         int i;
         for(i = 0; i < size; ++i) {
            ArrayList tds = ((TypeData.TypeVar)scc.get(i)).lowers;
            int size2 = tds.size();

            for(int j = 0; j < size2; ++j) {
               TypeData d = (TypeData)tds.get(j);
               TypeData.BasicType bt = d.isBasicType();
               if (kind == null) {
                  if (bt == null) {
                     isBasicType = false;
                     kind = d;
                     if (d.isUninit()) {
                        break;
                     }
                  } else {
                     isBasicType = true;
                     kind = bt;
                  }
               } else if (bt == null && isBasicType || bt != null && kind != bt) {
                  isBasicType = true;
                  kind = TypeTag.TOP;
                  break;
               }

               if (bt == null && !d.isNullType()) {
                  lowersSet.add(d.getName());
               }
            }
         }

         if (isBasicType) {
            for(i = 0; i < size; ++i) {
               TypeData.TypeVar cv = (TypeData.TypeVar)scc.get(i);
               cv.lowers.clear();
               cv.lowers.add(kind);
               this.is2WordType = ((TypeData)kind).is2WordType();
            }
         } else {
            String typeName = this.fixTypes2(scc, lowersSet, cp);

            for(int i = 0; i < size; ++i) {
               TypeData.TypeVar cv = (TypeData.TypeVar)scc.get(i);
               cv.fixedType = typeName;
            }
         }

      }

      private String fixTypes2(ArrayList scc, HashSet lowersSet, ClassPool cp) throws NotFoundException {
         Iterator it = lowersSet.iterator();
         if (lowersSet.size() == 0) {
            return null;
         } else if (lowersSet.size() == 1) {
            return (String)it.next();
         } else {
            CtClass cc;
            for(cc = cp.get((String)it.next()); it.hasNext(); cc = commonSuperClassEx(cc, cp.get((String)it.next()))) {
            }

            if (cc.getSuperclass() == null || isObjectArray(cc)) {
               cc = this.fixByUppers(scc, cp, new HashSet(), cc);
            }

            return cc.isArray() ? Descriptor.toJvmName(cc) : cc.getName();
         }
      }

      private static boolean isObjectArray(CtClass cc) throws NotFoundException {
         return cc.isArray() && cc.getComponentType().getSuperclass() == null;
      }

      private CtClass fixByUppers(ArrayList users, ClassPool cp, HashSet visited, CtClass type) throws NotFoundException {
         if (users == null) {
            return type;
         } else {
            int size = users.size();

            for(int i = 0; i < size; ++i) {
               TypeData.TypeVar t = (TypeData.TypeVar)users.get(i);
               if (!visited.add(t)) {
                  return type;
               }

               if (t.uppers != null) {
                  int s = t.uppers.size();

                  for(int k = 0; k < s; ++k) {
                     CtClass cc = cp.get((String)t.uppers.get(k));
                     if (cc.subtypeOf(type)) {
                        type = cc;
                     }
                  }
               }

               type = this.fixByUppers(t.usedBy, cp, visited, type);
            }

            return type;
         }
      }
   }

   public abstract static class AbsTypeVar extends TypeData {
      public abstract void merge(TypeData t);

      public int getTypeTag() {
         return 7;
      }

      public int getTypeData(ConstPool cp) {
         return cp.addClassInfo(this.getName());
      }

      public boolean eq(TypeData d) {
         return this.getName().equals(d.getName());
      }
   }

   protected static class BasicType extends TypeData {
      private String name;
      private int typeTag;

      public BasicType(String type, int tag) {
         this.name = type;
         this.typeTag = tag;
      }

      public int getTypeTag() {
         return this.typeTag;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }

      public TypeData join() {
         return (TypeData)(this == TypeTag.TOP ? this : super.join());
      }

      public TypeData.BasicType isBasicType() {
         return this;
      }

      public boolean is2WordType() {
         return this.typeTag == 4 || this.typeTag == 3;
      }

      public boolean eq(TypeData d) {
         return this == d;
      }

      public String getName() {
         return this.name;
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         throw new BadBytecode("conflict: " + this.name + " and " + s);
      }

      public String toString() {
         return this.name;
      }
   }
}
