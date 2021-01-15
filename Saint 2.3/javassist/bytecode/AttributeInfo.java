package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class AttributeInfo {
   protected ConstPool constPool;
   int name;
   byte[] info;

   protected AttributeInfo(ConstPool cp, int attrname, byte[] attrinfo) {
      this.constPool = cp;
      this.name = attrname;
      this.info = attrinfo;
   }

   protected AttributeInfo(ConstPool cp, String attrname) {
      this(cp, attrname, (byte[])null);
   }

   public AttributeInfo(ConstPool cp, String attrname, byte[] attrinfo) {
      this(cp, cp.addUtf8Info(attrname), attrinfo);
   }

   protected AttributeInfo(ConstPool cp, int n, DataInputStream in) throws IOException {
      this.constPool = cp;
      this.name = n;
      int len = in.readInt();
      this.info = new byte[len];
      if (len > 0) {
         in.readFully(this.info);
      }

   }

   static AttributeInfo read(ConstPool cp, DataInputStream in) throws IOException {
      int name = in.readUnsignedShort();
      String nameStr = cp.getUtf8Info(name);
      char first = nameStr.charAt(0);
      if (first < 'M') {
         if (first < 'E') {
            if (nameStr.equals("AnnotationDefault")) {
               return new AnnotationDefaultAttribute(cp, name, in);
            }

            if (nameStr.equals("BootstrapMethods")) {
               return new BootstrapMethodsAttribute(cp, name, in);
            }

            if (nameStr.equals("Code")) {
               return new CodeAttribute(cp, name, in);
            }

            if (nameStr.equals("ConstantValue")) {
               return new ConstantAttribute(cp, name, in);
            }

            if (nameStr.equals("Deprecated")) {
               return new DeprecatedAttribute(cp, name, in);
            }
         } else {
            if (nameStr.equals("EnclosingMethod")) {
               return new EnclosingMethodAttribute(cp, name, in);
            }

            if (nameStr.equals("Exceptions")) {
               return new ExceptionsAttribute(cp, name, in);
            }

            if (nameStr.equals("InnerClasses")) {
               return new InnerClassesAttribute(cp, name, in);
            }

            if (nameStr.equals("LineNumberTable")) {
               return new LineNumberAttribute(cp, name, in);
            }

            if (nameStr.equals("LocalVariableTable")) {
               return new LocalVariableAttribute(cp, name, in);
            }

            if (nameStr.equals("LocalVariableTypeTable")) {
               return new LocalVariableTypeAttribute(cp, name, in);
            }
         }
      } else if (first < 'S') {
         if (nameStr.equals("MethodParameters")) {
            return new MethodParametersAttribute(cp, name, in);
         }

         if (nameStr.equals("RuntimeVisibleAnnotations") || nameStr.equals("RuntimeInvisibleAnnotations")) {
            return new AnnotationsAttribute(cp, name, in);
         }

         if (nameStr.equals("RuntimeVisibleParameterAnnotations") || nameStr.equals("RuntimeInvisibleParameterAnnotations")) {
            return new ParameterAnnotationsAttribute(cp, name, in);
         }

         if (nameStr.equals("RuntimeVisibleTypeAnnotations") || nameStr.equals("RuntimeInvisibleTypeAnnotations")) {
            return new TypeAnnotationsAttribute(cp, name, in);
         }
      } else {
         if (nameStr.equals("Signature")) {
            return new SignatureAttribute(cp, name, in);
         }

         if (nameStr.equals("SourceFile")) {
            return new SourceFileAttribute(cp, name, in);
         }

         if (nameStr.equals("Synthetic")) {
            return new SyntheticAttribute(cp, name, in);
         }

         if (nameStr.equals("StackMap")) {
            return new StackMap(cp, name, in);
         }

         if (nameStr.equals("StackMapTable")) {
            return new StackMapTable(cp, name, in);
         }
      }

      return new AttributeInfo(cp, name, in);
   }

   public String getName() {
      return this.constPool.getUtf8Info(this.name);
   }

   public ConstPool getConstPool() {
      return this.constPool;
   }

   public int length() {
      return this.info.length + 6;
   }

   public byte[] get() {
      return this.info;
   }

   public void set(byte[] newinfo) {
      this.info = newinfo;
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      int s = this.info.length;
      byte[] srcInfo = this.info;
      byte[] newInfo = new byte[s];

      for(int i = 0; i < s; ++i) {
         newInfo[i] = srcInfo[i];
      }

      return new AttributeInfo(newCp, this.getName(), newInfo);
   }

   void write(DataOutputStream out) throws IOException {
      out.writeShort(this.name);
      out.writeInt(this.info.length);
      if (this.info.length > 0) {
         out.write(this.info);
      }

   }

   static int getLength(ArrayList list) {
      int size = 0;
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         AttributeInfo attr = (AttributeInfo)list.get(i);
         size += attr.length();
      }

      return size;
   }

   static AttributeInfo lookup(ArrayList list, String name) {
      if (list == null) {
         return null;
      } else {
         ListIterator iterator = list.listIterator();

         AttributeInfo ai;
         do {
            if (!iterator.hasNext()) {
               return null;
            }

            ai = (AttributeInfo)iterator.next();
         } while(!ai.getName().equals(name));

         return ai;
      }
   }

   static synchronized void remove(ArrayList list, String name) {
      if (list != null) {
         ListIterator iterator = list.listIterator();

         while(iterator.hasNext()) {
            AttributeInfo ai = (AttributeInfo)iterator.next();
            if (ai.getName().equals(name)) {
               iterator.remove();
            }
         }

      }
   }

   static void writeAll(ArrayList list, DataOutputStream out) throws IOException {
      if (list != null) {
         int n = list.size();

         for(int i = 0; i < n; ++i) {
            AttributeInfo attr = (AttributeInfo)list.get(i);
            attr.write(out);
         }

      }
   }

   static ArrayList copyAll(ArrayList list, ConstPool cp) {
      if (list == null) {
         return null;
      } else {
         ArrayList newList = new ArrayList();
         int n = list.size();

         for(int i = 0; i < n; ++i) {
            AttributeInfo attr = (AttributeInfo)list.get(i);
            newList.add(attr.copy(cp, (Map)null));
         }

         return newList;
      }
   }

   void renameClass(String oldname, String newname) {
   }

   void renameClass(Map classnames) {
   }

   static void renameClass(List attributes, String oldname, String newname) {
      Iterator iterator = attributes.iterator();

      while(iterator.hasNext()) {
         AttributeInfo ai = (AttributeInfo)iterator.next();
         ai.renameClass(oldname, newname);
      }

   }

   static void renameClass(List attributes, Map classnames) {
      Iterator iterator = attributes.iterator();

      while(iterator.hasNext()) {
         AttributeInfo ai = (AttributeInfo)iterator.next();
         ai.renameClass(classnames);
      }

   }

   void getRefClasses(Map classnames) {
   }

   static void getRefClasses(List attributes, Map classnames) {
      Iterator iterator = attributes.iterator();

      while(iterator.hasNext()) {
         AttributeInfo ai = (AttributeInfo)iterator.next();
         ai.getRefClasses(classnames);
      }

   }
}
