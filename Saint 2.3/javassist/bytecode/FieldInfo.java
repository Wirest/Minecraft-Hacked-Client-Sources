package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FieldInfo {
   ConstPool constPool;
   int accessFlags;
   int name;
   String cachedName;
   String cachedType;
   int descriptor;
   ArrayList attribute;

   private FieldInfo(ConstPool cp) {
      this.constPool = cp;
      this.accessFlags = 0;
      this.attribute = null;
   }

   public FieldInfo(ConstPool cp, String fieldName, String desc) {
      this(cp);
      this.name = cp.addUtf8Info(fieldName);
      this.cachedName = fieldName;
      this.descriptor = cp.addUtf8Info(desc);
   }

   FieldInfo(ConstPool cp, DataInputStream in) throws IOException {
      this(cp);
      this.read(in);
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

      AttributeInfo signature = this.getAttribute("Signature");
      if (signature != null) {
         signature = signature.copy(cp, (Map)null);
         newAttributes.add(signature);
      }

      int index = this.getConstantValue();
      if (index != 0) {
         index = this.constPool.copy(index, cp, (Map)null);
         newAttributes.add(new ConstantAttribute(cp, index));
      }

      this.attribute = newAttributes;
      this.name = cp.addUtf8Info(this.getName());
      this.descriptor = cp.addUtf8Info(this.getDescriptor());
      this.constPool = cp;
   }

   public ConstPool getConstPool() {
      return this.constPool;
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

   public int getConstantValue() {
      if ((this.accessFlags & 8) == 0) {
         return 0;
      } else {
         ConstantAttribute attr = (ConstantAttribute)this.getAttribute("ConstantValue");
         return attr == null ? 0 : attr.getConstantValue();
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
