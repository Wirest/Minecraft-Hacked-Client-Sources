package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.CtClass;

public final class ConstPool {
   LongVector items;
   int numOfItems;
   int thisClassInfo;
   HashMap itemsCache;
   public static final int CONST_Class = 7;
   public static final int CONST_Fieldref = 9;
   public static final int CONST_Methodref = 10;
   public static final int CONST_InterfaceMethodref = 11;
   public static final int CONST_String = 8;
   public static final int CONST_Integer = 3;
   public static final int CONST_Float = 4;
   public static final int CONST_Long = 5;
   public static final int CONST_Double = 6;
   public static final int CONST_NameAndType = 12;
   public static final int CONST_Utf8 = 1;
   public static final int CONST_MethodHandle = 15;
   public static final CtClass THIS = null;
   public static final int REF_getField = 1;
   public static final int REF_getStatic = 2;
   public static final int REF_putField = 3;
   public static final int REF_putStatic = 4;
   public static final int REF_invokeVirtual = 5;
   public static final int REF_invokeStatic = 6;
   public static final int REF_invokeSpecial = 7;
   public static final int REF_newInvokeSpecial = 8;
   public static final int REF_invokeInterface = 9;

   public ConstPool(String thisclass) {
      this.items = new LongVector();
      this.itemsCache = null;
      this.numOfItems = 0;
      this.addItem0((ConstInfo)null);
      this.thisClassInfo = this.addClassInfo(thisclass);
   }

   public ConstPool(DataInputStream in) throws IOException {
      this.itemsCache = null;
      this.thisClassInfo = 0;
      this.read(in);
   }

   void prune() {
      this.itemsCache = null;
   }

   public int getSize() {
      return this.numOfItems;
   }

   public String getClassName() {
      return this.getClassInfo(this.thisClassInfo);
   }

   public int getThisClassInfo() {
      return this.thisClassInfo;
   }

   void setThisClassInfo(int i) {
      this.thisClassInfo = i;
   }

   ConstInfo getItem(int n) {
      return this.items.elementAt(n);
   }

   public int getTag(int index) {
      return this.getItem(index).getTag();
   }

   public String getClassInfo(int index) {
      ClassInfo c = (ClassInfo)this.getItem(index);
      return c == null ? null : Descriptor.toJavaName(this.getUtf8Info(c.name));
   }

   public String getClassInfoByDescriptor(int index) {
      ClassInfo c = (ClassInfo)this.getItem(index);
      if (c == null) {
         return null;
      } else {
         String className = this.getUtf8Info(c.name);
         return className.charAt(0) == '[' ? className : Descriptor.of(className);
      }
   }

   public int getNameAndTypeName(int index) {
      NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(index);
      return ntinfo.memberName;
   }

   public int getNameAndTypeDescriptor(int index) {
      NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(index);
      return ntinfo.typeDescriptor;
   }

   public int getMemberClass(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.classIndex;
   }

   public int getMemberNameAndType(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.nameAndTypeIndex;
   }

   public int getFieldrefClass(int index) {
      FieldrefInfo finfo = (FieldrefInfo)this.getItem(index);
      return finfo.classIndex;
   }

   public String getFieldrefClassName(int index) {
      FieldrefInfo f = (FieldrefInfo)this.getItem(index);
      return f == null ? null : this.getClassInfo(f.classIndex);
   }

   public int getFieldrefNameAndType(int index) {
      FieldrefInfo finfo = (FieldrefInfo)this.getItem(index);
      return finfo.nameAndTypeIndex;
   }

   public String getFieldrefName(int index) {
      FieldrefInfo f = (FieldrefInfo)this.getItem(index);
      if (f == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(f.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.memberName);
      }
   }

   public String getFieldrefType(int index) {
      FieldrefInfo f = (FieldrefInfo)this.getItem(index);
      if (f == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(f.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.typeDescriptor);
      }
   }

   public int getMethodrefClass(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.classIndex;
   }

   public String getMethodrefClassName(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo == null ? null : this.getClassInfo(minfo.classIndex);
   }

   public int getMethodrefNameAndType(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.nameAndTypeIndex;
   }

   public String getMethodrefName(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      if (minfo == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.memberName);
      }
   }

   public String getMethodrefType(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      if (minfo == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.typeDescriptor);
      }
   }

   public int getInterfaceMethodrefClass(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.classIndex;
   }

   public String getInterfaceMethodrefClassName(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return this.getClassInfo(minfo.classIndex);
   }

   public int getInterfaceMethodrefNameAndType(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      return minfo.nameAndTypeIndex;
   }

   public String getInterfaceMethodrefName(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      if (minfo == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.memberName);
      }
   }

   public String getInterfaceMethodrefType(int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      if (minfo == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
         return n == null ? null : this.getUtf8Info(n.typeDescriptor);
      }
   }

   public Object getLdcValue(int index) {
      ConstInfo constInfo = this.getItem(index);
      Object value = null;
      if (constInfo instanceof StringInfo) {
         value = this.getStringInfo(index);
      } else if (constInfo instanceof FloatInfo) {
         value = new Float(this.getFloatInfo(index));
      } else if (constInfo instanceof IntegerInfo) {
         value = new Integer(this.getIntegerInfo(index));
      } else if (constInfo instanceof LongInfo) {
         value = new Long(this.getLongInfo(index));
      } else if (constInfo instanceof DoubleInfo) {
         value = new Double(this.getDoubleInfo(index));
      } else {
         value = null;
      }

      return value;
   }

   public int getIntegerInfo(int index) {
      IntegerInfo i = (IntegerInfo)this.getItem(index);
      return i.value;
   }

   public float getFloatInfo(int index) {
      FloatInfo i = (FloatInfo)this.getItem(index);
      return i.value;
   }

   public long getLongInfo(int index) {
      LongInfo i = (LongInfo)this.getItem(index);
      return i.value;
   }

   public double getDoubleInfo(int index) {
      DoubleInfo i = (DoubleInfo)this.getItem(index);
      return i.value;
   }

   public String getStringInfo(int index) {
      StringInfo si = (StringInfo)this.getItem(index);
      return this.getUtf8Info(si.string);
   }

   public String getUtf8Info(int index) {
      Utf8Info utf = (Utf8Info)this.getItem(index);
      return utf.string;
   }

   public int getMethodHandleKind(int index) {
      MethodHandleInfo mhinfo = (MethodHandleInfo)this.getItem(index);
      return mhinfo.refKind;
   }

   public int getMethodHandleIndex(int index) {
      MethodHandleInfo mhinfo = (MethodHandleInfo)this.getItem(index);
      return mhinfo.refIndex;
   }

   public int getMethodTypeInfo(int index) {
      MethodTypeInfo mtinfo = (MethodTypeInfo)this.getItem(index);
      return mtinfo.descriptor;
   }

   public int getInvokeDynamicBootstrap(int index) {
      InvokeDynamicInfo iv = (InvokeDynamicInfo)this.getItem(index);
      return iv.bootstrap;
   }

   public int getInvokeDynamicNameAndType(int index) {
      InvokeDynamicInfo iv = (InvokeDynamicInfo)this.getItem(index);
      return iv.nameAndType;
   }

   public String getInvokeDynamicType(int index) {
      InvokeDynamicInfo iv = (InvokeDynamicInfo)this.getItem(index);
      if (iv == null) {
         return null;
      } else {
         NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(iv.nameAndType);
         return n == null ? null : this.getUtf8Info(n.typeDescriptor);
      }
   }

   public int isConstructor(String classname, int index) {
      return this.isMember(classname, "<init>", index);
   }

   public int isMember(String classname, String membername, int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      if (this.getClassInfo(minfo.classIndex).equals(classname)) {
         NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
         if (this.getUtf8Info(ntinfo.memberName).equals(membername)) {
            return ntinfo.typeDescriptor;
         }
      }

      return 0;
   }

   public String eqMember(String membername, String desc, int index) {
      MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
      NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
      return this.getUtf8Info(ntinfo.memberName).equals(membername) && this.getUtf8Info(ntinfo.typeDescriptor).equals(desc) ? this.getClassInfo(minfo.classIndex) : null;
   }

   private int addItem0(ConstInfo info) {
      this.items.addElement(info);
      return this.numOfItems++;
   }

   private int addItem(ConstInfo info) {
      if (this.itemsCache == null) {
         this.itemsCache = makeItemsCache(this.items);
      }

      ConstInfo found = (ConstInfo)this.itemsCache.get(info);
      if (found != null) {
         return found.index;
      } else {
         this.items.addElement(info);
         this.itemsCache.put(info, info);
         return this.numOfItems++;
      }
   }

   public int copy(int n, ConstPool dest, Map classnames) {
      if (n == 0) {
         return 0;
      } else {
         ConstInfo info = this.getItem(n);
         return info.copy(this, dest, classnames);
      }
   }

   int addConstInfoPadding() {
      return this.addItem0(new ConstInfoPadding(this.numOfItems));
   }

   public int addClassInfo(CtClass c) {
      if (c == THIS) {
         return this.thisClassInfo;
      } else {
         return !c.isArray() ? this.addClassInfo(c.getName()) : this.addClassInfo(Descriptor.toJvmName(c));
      }
   }

   public int addClassInfo(String qname) {
      int utf8 = this.addUtf8Info(Descriptor.toJvmName(qname));
      return this.addItem(new ClassInfo(utf8, this.numOfItems));
   }

   public int addNameAndTypeInfo(String name, String type) {
      return this.addNameAndTypeInfo(this.addUtf8Info(name), this.addUtf8Info(type));
   }

   public int addNameAndTypeInfo(int name, int type) {
      return this.addItem(new NameAndTypeInfo(name, type, this.numOfItems));
   }

   public int addFieldrefInfo(int classInfo, String name, String type) {
      int nt = this.addNameAndTypeInfo(name, type);
      return this.addFieldrefInfo(classInfo, nt);
   }

   public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
      return this.addItem(new FieldrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
   }

   public int addMethodrefInfo(int classInfo, String name, String type) {
      int nt = this.addNameAndTypeInfo(name, type);
      return this.addMethodrefInfo(classInfo, nt);
   }

   public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
      return this.addItem(new MethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
   }

   public int addInterfaceMethodrefInfo(int classInfo, String name, String type) {
      int nt = this.addNameAndTypeInfo(name, type);
      return this.addInterfaceMethodrefInfo(classInfo, nt);
   }

   public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
      return this.addItem(new InterfaceMethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
   }

   public int addStringInfo(String str) {
      int utf = this.addUtf8Info(str);
      return this.addItem(new StringInfo(utf, this.numOfItems));
   }

   public int addIntegerInfo(int i) {
      return this.addItem(new IntegerInfo(i, this.numOfItems));
   }

   public int addFloatInfo(float f) {
      return this.addItem(new FloatInfo(f, this.numOfItems));
   }

   public int addLongInfo(long l) {
      int i = this.addItem(new LongInfo(l, this.numOfItems));
      if (i == this.numOfItems - 1) {
         this.addConstInfoPadding();
      }

      return i;
   }

   public int addDoubleInfo(double d) {
      int i = this.addItem(new DoubleInfo(d, this.numOfItems));
      if (i == this.numOfItems - 1) {
         this.addConstInfoPadding();
      }

      return i;
   }

   public int addUtf8Info(String utf8) {
      return this.addItem(new Utf8Info(utf8, this.numOfItems));
   }

   public int addMethodHandleInfo(int kind, int index) {
      return this.addItem(new MethodHandleInfo(kind, index, this.numOfItems));
   }

   public int addMethodTypeInfo(int desc) {
      return this.addItem(new MethodTypeInfo(desc, this.numOfItems));
   }

   public int addInvokeDynamicInfo(int bootstrap, int nameAndType) {
      return this.addItem(new InvokeDynamicInfo(bootstrap, nameAndType, this.numOfItems));
   }

   public Set getClassNames() {
      HashSet result = new HashSet();
      LongVector v = this.items;
      int size = this.numOfItems;

      for(int i = 1; i < size; ++i) {
         String className = v.elementAt(i).getClassName(this);
         if (className != null) {
            result.add(className);
         }
      }

      return result;
   }

   public void renameClass(String oldName, String newName) {
      LongVector v = this.items;
      int size = this.numOfItems;

      for(int i = 1; i < size; ++i) {
         ConstInfo ci = v.elementAt(i);
         ci.renameClass(this, oldName, newName, this.itemsCache);
      }

   }

   public void renameClass(Map classnames) {
      LongVector v = this.items;
      int size = this.numOfItems;

      for(int i = 1; i < size; ++i) {
         ConstInfo ci = v.elementAt(i);
         ci.renameClass(this, classnames, this.itemsCache);
      }

   }

   private void read(DataInputStream in) throws IOException {
      int n = in.readUnsignedShort();
      this.items = new LongVector(n);
      this.numOfItems = 0;
      this.addItem0((ConstInfo)null);

      while(true) {
         int tag;
         do {
            --n;
            if (n <= 0) {
               return;
            }

            tag = this.readOne(in);
         } while(tag != 5 && tag != 6);

         this.addConstInfoPadding();
         --n;
      }
   }

   private static HashMap makeItemsCache(LongVector items) {
      HashMap cache = new HashMap();
      int var2 = 1;

      while(true) {
         ConstInfo info = items.elementAt(var2++);
         if (info == null) {
            return cache;
         }

         cache.put(info, info);
      }
   }

   private int readOne(DataInputStream in) throws IOException {
      int tag = in.readUnsignedByte();
      Object info;
      switch(tag) {
      case 1:
         info = new Utf8Info(in, this.numOfItems);
         break;
      case 2:
      case 13:
      case 14:
      case 17:
      default:
         throw new IOException("invalid constant type: " + tag + " at " + this.numOfItems);
      case 3:
         info = new IntegerInfo(in, this.numOfItems);
         break;
      case 4:
         info = new FloatInfo(in, this.numOfItems);
         break;
      case 5:
         info = new LongInfo(in, this.numOfItems);
         break;
      case 6:
         info = new DoubleInfo(in, this.numOfItems);
         break;
      case 7:
         info = new ClassInfo(in, this.numOfItems);
         break;
      case 8:
         info = new StringInfo(in, this.numOfItems);
         break;
      case 9:
         info = new FieldrefInfo(in, this.numOfItems);
         break;
      case 10:
         info = new MethodrefInfo(in, this.numOfItems);
         break;
      case 11:
         info = new InterfaceMethodrefInfo(in, this.numOfItems);
         break;
      case 12:
         info = new NameAndTypeInfo(in, this.numOfItems);
         break;
      case 15:
         info = new MethodHandleInfo(in, this.numOfItems);
         break;
      case 16:
         info = new MethodTypeInfo(in, this.numOfItems);
         break;
      case 18:
         info = new InvokeDynamicInfo(in, this.numOfItems);
      }

      this.addItem0((ConstInfo)info);
      return tag;
   }

   public void write(DataOutputStream out) throws IOException {
      out.writeShort(this.numOfItems);
      LongVector v = this.items;
      int size = this.numOfItems;

      for(int i = 1; i < size; ++i) {
         v.elementAt(i).write(out);
      }

   }

   public void print() {
      this.print(new PrintWriter(System.out, true));
   }

   public void print(PrintWriter out) {
      int size = this.numOfItems;

      for(int i = 1; i < size; ++i) {
         out.print(i);
         out.print(" ");
         this.items.elementAt(i).print(out);
      }

   }
}
