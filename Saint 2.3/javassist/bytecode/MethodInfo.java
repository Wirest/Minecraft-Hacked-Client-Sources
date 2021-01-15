package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javassist.ClassPool;
import javassist.bytecode.stackmap.MapMaker;

public class MethodInfo {
   ConstPool constPool;
   int accessFlags;
   int name;
   String cachedName;
   int descriptor;
   ArrayList attribute;
   public static boolean doPreverify = false;
   public static final String nameInit = "<init>";
   public static final String nameClinit = "<clinit>";

   private MethodInfo(ConstPool cp) {
      this.constPool = cp;
      this.attribute = null;
   }

   public MethodInfo(ConstPool cp, String methodname, String desc) {
      this(cp);
      this.accessFlags = 0;
      this.name = cp.addUtf8Info(methodname);
      this.cachedName = methodname;
      this.descriptor = this.constPool.addUtf8Info(desc);
   }

   MethodInfo(ConstPool cp, DataInputStream in) throws IOException {
      this(cp);
      this.read(in);
   }

   public MethodInfo(ConstPool cp, String methodname, MethodInfo src, Map classnameMap) throws BadBytecode {
      this(cp);
      this.read(src, methodname, classnameMap);
   }

   public String toString() {
      return this.getName() + " " + this.getDescriptor();
   }

   void compact(ConstPool cp) {
      this.name = cp.addUtf8Info(this.getName());
      this.descriptor = cp.addUtf8Info(this.getDescriptor());
      this.attribute = AttributeInfo.copyAll(this.attribute, cp);
      this.constPool = cp;
   }

   void prune(ConstPool cp) {
      ArrayList newAttributes = new ArrayList();
      AttributeInfo invisibleAnnotations = this.getAttribute("RuntimeInvisibleAnnotations");
      if (invisibleAnnotations != null) {
         invisibleAnnotations = invisibleAnnotations.copy(cp, (Map)null);
         newAttributes.add(invisibleAnnotations);
      }

      AttributeInfo visibleAnnotations = this.getAttribute("RuntimeVisibleAnnotations");
      if (visibleAnnotations != null) {
         visibleAnnotations = visibleAnnotations.copy(cp, (Map)null);
         newAttributes.add(visibleAnnotations);
      }

      AttributeInfo parameterInvisibleAnnotations = this.getAttribute("RuntimeInvisibleParameterAnnotations");
      if (parameterInvisibleAnnotations != null) {
         parameterInvisibleAnnotations = parameterInvisibleAnnotations.copy(cp, (Map)null);
         newAttributes.add(parameterInvisibleAnnotations);
      }

      AttributeInfo parameterVisibleAnnotations = this.getAttribute("RuntimeVisibleParameterAnnotations");
      if (parameterVisibleAnnotations != null) {
         parameterVisibleAnnotations = parameterVisibleAnnotations.copy(cp, (Map)null);
         newAttributes.add(parameterVisibleAnnotations);
      }

      AnnotationDefaultAttribute defaultAttribute = (AnnotationDefaultAttribute)this.getAttribute("AnnotationDefault");
      if (defaultAttribute != null) {
         newAttributes.add(defaultAttribute);
      }

      ExceptionsAttribute ea = this.getExceptionsAttribute();
      if (ea != null) {
         newAttributes.add(ea);
      }

      AttributeInfo signature = this.getAttribute("Signature");
      if (signature != null) {
         signature = signature.copy(cp, (Map)null);
         newAttributes.add(signature);
      }

      this.attribute = newAttributes;
      this.name = cp.addUtf8Info(this.getName());
      this.descriptor = cp.addUtf8Info(this.getDescriptor());
      this.constPool = cp;
   }

   public String getName() {
      if (this.cachedName == null) {
         this.cachedName = this.constPool.getUtf8Info(this.name);
      }

      return this.cachedName;
   }

   public void setName(String newName) {
      this.name = this.constPool.addUtf8Info(newName);
      this.cachedName = newName;
   }

   public boolean isMethod() {
      String n = this.getName();
      return !n.equals("<init>") && !n.equals("<clinit>");
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public boolean isConstructor() {
      return this.getName().equals("<init>");
   }

   public boolean isStaticInitializer() {
      return this.getName().equals("<clinit>");
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   public void setAccessFlags(int acc) {
      this.accessFlags = acc;
   }

   public String getDescriptor() {
      return this.constPool.getUtf8Info(this.descriptor);
   }

   public void setDescriptor(String desc) {
      if (!desc.equals(this.getDescriptor())) {
         this.descriptor = this.constPool.addUtf8Info(desc);
      }

   }

   public List getAttributes() {
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      return this.attribute;
   }

   public AttributeInfo getAttribute(String name) {
      return AttributeInfo.lookup(this.attribute, name);
   }

   public void addAttribute(AttributeInfo info) {
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      AttributeInfo.remove(this.attribute, info.getName());
      this.attribute.add(info);
   }

   public ExceptionsAttribute getExceptionsAttribute() {
      AttributeInfo info = AttributeInfo.lookup(this.attribute, "Exceptions");
      return (ExceptionsAttribute)info;
   }

   public CodeAttribute getCodeAttribute() {
      AttributeInfo info = AttributeInfo.lookup(this.attribute, "Code");
      return (CodeAttribute)info;
   }

   public void removeExceptionsAttribute() {
      AttributeInfo.remove(this.attribute, "Exceptions");
   }

   public void setExceptionsAttribute(ExceptionsAttribute cattr) {
      this.removeExceptionsAttribute();
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      this.attribute.add(cattr);
   }

   public void removeCodeAttribute() {
      AttributeInfo.remove(this.attribute, "Code");
   }

   public void setCodeAttribute(CodeAttribute cattr) {
      this.removeCodeAttribute();
      if (this.attribute == null) {
         this.attribute = new ArrayList();
      }

      this.attribute.add(cattr);
   }

   public void rebuildStackMapIf6(ClassPool pool, ClassFile cf) throws BadBytecode {
      if (cf.getMajorVersion() >= 50) {
         this.rebuildStackMap(pool);
      }

      if (doPreverify) {
         this.rebuildStackMapForME(pool);
      }

   }

   public void rebuildStackMap(ClassPool pool) throws BadBytecode {
      CodeAttribute ca = this.getCodeAttribute();
      if (ca != null) {
         StackMapTable smt = MapMaker.make(pool, this);
         ca.setAttribute(smt);
      }

   }

   public void rebuildStackMapForME(ClassPool pool) throws BadBytecode {
      CodeAttribute ca = this.getCodeAttribute();
      if (ca != null) {
         StackMap sm = MapMaker.make2(pool, this);
         ca.setAttribute(sm);
      }

   }

   public int getLineNumber(int pos) {
      CodeAttribute ca = this.getCodeAttribute();
      if (ca == null) {
         return -1;
      } else {
         LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
         return ainfo == null ? -1 : ainfo.toLineNumber(pos);
      }
   }

   public void setSuperclass(String superclass) throws BadBytecode {
      if (this.isConstructor()) {
         CodeAttribute ca = this.getCodeAttribute();
         byte[] code = ca.getCode();
         CodeIterator iterator = ca.iterator();
         int pos = iterator.skipSuperConstructor();
         if (pos >= 0) {
            ConstPool cp = this.constPool;
            int mref = ByteArray.readU16bit(code, pos + 1);
            int nt = cp.getMethodrefNameAndType(mref);
            int sc = cp.addClassInfo(superclass);
            int mref2 = cp.addMethodrefInfo(sc, nt);
            ByteArray.write16bit(mref2, code, pos + 1);
         }

      }
   }

   private void read(MethodInfo src, String methodname, Map classnames) throws BadBytecode {
      ConstPool destCp = this.constPool;
      this.accessFlags = src.accessFlags;
      this.name = destCp.addUtf8Info(methodname);
      this.cachedName = methodname;
      ConstPool srcCp = src.constPool;
      String desc = srcCp.getUtf8Info(src.descriptor);
      String desc2 = Descriptor.rename(desc, classnames);
      this.descriptor = destCp.addUtf8Info(desc2);
      this.attribute = new ArrayList();
      ExceptionsAttribute eattr = src.getExceptionsAttribute();
      if (eattr != null) {
         this.attribute.add(eattr.copy(destCp, classnames));
      }

      CodeAttribute cattr = src.getCodeAttribute();
      if (cattr != null) {
         this.attribute.add(cattr.copy(destCp, classnames));
      }

   }

   private void read(DataInputStream in) throws IOException {
      this.accessFlags = in.readUnsignedShort();
      this.name = in.readUnsignedShort();
      this.descriptor = in.readUnsignedShort();
      int n = in.readUnsignedShort();
      this.attribute = new ArrayList();

      for(int i = 0; i < n; ++i) {
         this.attribute.add(AttributeInfo.read(this.constPool, in));
      }

   }

   void write(DataOutputStream out) throws IOException {
      out.writeShort(this.accessFlags);
      out.writeShort(this.name);
      out.writeShort(this.descriptor);
      if (this.attribute == null) {
         out.writeShort(0);
      } else {
         out.writeShort(this.attribute.size());
         AttributeInfo.writeAll(this.attribute, out);
      }

   }
}
