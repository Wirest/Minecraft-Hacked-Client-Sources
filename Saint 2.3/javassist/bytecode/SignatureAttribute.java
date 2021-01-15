package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javassist.CtClass;

public class SignatureAttribute extends AttributeInfo {
   public static final String tag = "Signature";

   SignatureAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public SignatureAttribute(ConstPool cp, String signature) {
      super(cp, "Signature");
      int index = cp.addUtf8Info(signature);
      byte[] bvalue = new byte[]{(byte)(index >>> 8), (byte)index};
      this.set(bvalue);
   }

   public String getSignature() {
      return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
   }

   public void setSignature(String sig) {
      int index = this.getConstPool().addUtf8Info(sig);
      ByteArray.write16bit(index, this.info, 0);
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      return new SignatureAttribute(newCp, this.getSignature());
   }

   void renameClass(String oldname, String newname) {
      String sig = renameClass(this.getSignature(), oldname, newname);
      this.setSignature(sig);
   }

   void renameClass(Map classnames) {
      String sig = renameClass(this.getSignature(), classnames);
      this.setSignature(sig);
   }

   static String renameClass(String desc, String oldname, String newname) {
      Map map = new HashMap();
      map.put(oldname, newname);
      return renameClass(desc, (Map)map);
   }

   static String renameClass(String desc, Map map) {
      if (map == null) {
         return desc;
      } else {
         StringBuilder newdesc = new StringBuilder();
         int head = 0;
         int i = 0;

         int j;
         while(true) {
            j = desc.indexOf(76, i);
            if (j < 0) {
               break;
            }

            StringBuilder nameBuf = new StringBuilder();
            int k = j;

            char c;
            try {
               while(true) {
                  ++k;
                  if ((c = desc.charAt(k)) == ';') {
                     break;
                  }

                  nameBuf.append(c);
                  if (c == '<') {
                     while(true) {
                        ++k;
                        if ((c = desc.charAt(k)) == '>') {
                           nameBuf.append(c);
                           break;
                        }

                        nameBuf.append(c);
                     }
                  }
               }
            } catch (IndexOutOfBoundsException var11) {
               break;
            }

            i = k + 1;
            String name = nameBuf.toString();
            String name2 = (String)map.get(name);
            if (name2 != null) {
               newdesc.append(desc.substring(head, j));
               newdesc.append('L');
               newdesc.append(name2);
               newdesc.append(c);
               head = i;
            }
         }

         if (head == 0) {
            return desc;
         } else {
            j = desc.length();
            if (head < j) {
               newdesc.append(desc.substring(head, j));
            }

            return newdesc.toString();
         }
      }
   }

   private static boolean isNamePart(int c) {
      return c != 59 && c != 60;
   }

   public static SignatureAttribute.ClassSignature toClassSignature(String sig) throws BadBytecode {
      try {
         return parseSig(sig);
      } catch (IndexOutOfBoundsException var2) {
         throw error(sig);
      }
   }

   public static SignatureAttribute.MethodSignature toMethodSignature(String sig) throws BadBytecode {
      try {
         return parseMethodSig(sig);
      } catch (IndexOutOfBoundsException var2) {
         throw error(sig);
      }
   }

   public static SignatureAttribute.ObjectType toFieldSignature(String sig) throws BadBytecode {
      try {
         return parseObjectType(sig, new SignatureAttribute.Cursor(), false);
      } catch (IndexOutOfBoundsException var2) {
         throw error(sig);
      }
   }

   public static SignatureAttribute.Type toTypeSignature(String sig) throws BadBytecode {
      try {
         return parseType(sig, new SignatureAttribute.Cursor());
      } catch (IndexOutOfBoundsException var2) {
         throw error(sig);
      }
   }

   private static SignatureAttribute.ClassSignature parseSig(String sig) throws BadBytecode, IndexOutOfBoundsException {
      SignatureAttribute.Cursor cur = new SignatureAttribute.Cursor();
      SignatureAttribute.TypeParameter[] tp = parseTypeParams(sig, cur);
      SignatureAttribute.ClassType superClass = parseClassType(sig, cur);
      int sigLen = sig.length();
      ArrayList ifArray = new ArrayList();

      while(cur.position < sigLen && sig.charAt(cur.position) == 'L') {
         ifArray.add(parseClassType(sig, cur));
      }

      SignatureAttribute.ClassType[] ifs = (SignatureAttribute.ClassType[])((SignatureAttribute.ClassType[])ifArray.toArray(new SignatureAttribute.ClassType[ifArray.size()]));
      return new SignatureAttribute.ClassSignature(tp, superClass, ifs);
   }

   private static SignatureAttribute.MethodSignature parseMethodSig(String sig) throws BadBytecode {
      SignatureAttribute.Cursor cur = new SignatureAttribute.Cursor();
      SignatureAttribute.TypeParameter[] tp = parseTypeParams(sig, cur);
      if (sig.charAt(cur.position++) != '(') {
         throw error(sig);
      } else {
         ArrayList params = new ArrayList();

         SignatureAttribute.Type ret;
         while(sig.charAt(cur.position) != ')') {
            ret = parseType(sig, cur);
            params.add(ret);
         }

         ++cur.position;
         ret = parseType(sig, cur);
         int sigLen = sig.length();
         ArrayList exceptions = new ArrayList();

         while(cur.position < sigLen && sig.charAt(cur.position) == '^') {
            ++cur.position;
            SignatureAttribute.ObjectType t = parseObjectType(sig, cur, false);
            if (t instanceof SignatureAttribute.ArrayType) {
               throw error(sig);
            }

            exceptions.add(t);
         }

         SignatureAttribute.Type[] p = (SignatureAttribute.Type[])((SignatureAttribute.Type[])params.toArray(new SignatureAttribute.Type[params.size()]));
         SignatureAttribute.ObjectType[] ex = (SignatureAttribute.ObjectType[])((SignatureAttribute.ObjectType[])exceptions.toArray(new SignatureAttribute.ObjectType[exceptions.size()]));
         return new SignatureAttribute.MethodSignature(tp, p, ret, ex);
      }
   }

   private static SignatureAttribute.TypeParameter[] parseTypeParams(String sig, SignatureAttribute.Cursor cur) throws BadBytecode {
      ArrayList typeParam = new ArrayList();
      if (sig.charAt(cur.position) == '<') {
         ++cur.position;

         while(sig.charAt(cur.position) != '>') {
            int nameBegin = cur.position;
            int nameEnd = cur.indexOf(sig, 58);
            SignatureAttribute.ObjectType classBound = parseObjectType(sig, cur, true);
            ArrayList ifBound = new ArrayList();

            while(sig.charAt(cur.position) == ':') {
               ++cur.position;
               SignatureAttribute.ObjectType t = parseObjectType(sig, cur, false);
               ifBound.add(t);
            }

            SignatureAttribute.TypeParameter p = new SignatureAttribute.TypeParameter(sig, nameBegin, nameEnd, classBound, (SignatureAttribute.ObjectType[])((SignatureAttribute.ObjectType[])ifBound.toArray(new SignatureAttribute.ObjectType[ifBound.size()])));
            typeParam.add(p);
         }

         ++cur.position;
      }

      return (SignatureAttribute.TypeParameter[])((SignatureAttribute.TypeParameter[])typeParam.toArray(new SignatureAttribute.TypeParameter[typeParam.size()]));
   }

   private static SignatureAttribute.ObjectType parseObjectType(String sig, SignatureAttribute.Cursor c, boolean dontThrow) throws BadBytecode {
      int begin = c.position;
      switch(sig.charAt(begin)) {
      case 'L':
         return parseClassType2(sig, c, (SignatureAttribute.ClassType)null);
      case 'T':
         int i = c.indexOf(sig, 59);
         return new SignatureAttribute.TypeVariable(sig, begin + 1, i);
      case '[':
         return parseArray(sig, c);
      default:
         if (dontThrow) {
            return null;
         } else {
            throw error(sig);
         }
      }
   }

   private static SignatureAttribute.ClassType parseClassType(String sig, SignatureAttribute.Cursor c) throws BadBytecode {
      if (sig.charAt(c.position) == 'L') {
         return parseClassType2(sig, c, (SignatureAttribute.ClassType)null);
      } else {
         throw error(sig);
      }
   }

   private static SignatureAttribute.ClassType parseClassType2(String sig, SignatureAttribute.Cursor c, SignatureAttribute.ClassType parent) throws BadBytecode {
      int start = ++c.position;

      char t;
      do {
         t = sig.charAt(c.position++);
      } while(t != '$' && t != '<' && t != ';');

      int end = c.position - 1;
      SignatureAttribute.TypeArgument[] targs;
      if (t == '<') {
         targs = parseTypeArgs(sig, c);
         t = sig.charAt(c.position++);
      } else {
         targs = null;
      }

      SignatureAttribute.ClassType thisClass = SignatureAttribute.ClassType.make(sig, start, end, targs, parent);
      if (t != '$' && t != '.') {
         return thisClass;
      } else {
         --c.position;
         return parseClassType2(sig, c, thisClass);
      }
   }

   private static SignatureAttribute.TypeArgument[] parseTypeArgs(String sig, SignatureAttribute.Cursor c) throws BadBytecode {
      ArrayList args;
      char t;
      SignatureAttribute.TypeArgument ta;
      for(args = new ArrayList(); (t = sig.charAt(c.position++)) != '>'; args.add(ta)) {
         if (t == '*') {
            ta = new SignatureAttribute.TypeArgument((SignatureAttribute.ObjectType)null, '*');
         } else {
            if (t != '+' && t != '-') {
               t = ' ';
               --c.position;
            }

            ta = new SignatureAttribute.TypeArgument(parseObjectType(sig, c, false), t);
         }
      }

      return (SignatureAttribute.TypeArgument[])((SignatureAttribute.TypeArgument[])args.toArray(new SignatureAttribute.TypeArgument[args.size()]));
   }

   private static SignatureAttribute.ObjectType parseArray(String sig, SignatureAttribute.Cursor c) throws BadBytecode {
      int dim;
      for(dim = 1; sig.charAt(++c.position) == '['; ++dim) {
      }

      return new SignatureAttribute.ArrayType(dim, parseType(sig, c));
   }

   private static SignatureAttribute.Type parseType(String sig, SignatureAttribute.Cursor c) throws BadBytecode {
      SignatureAttribute.Type t = parseObjectType(sig, c, true);
      if (t == null) {
         t = new SignatureAttribute.BaseType(sig.charAt(c.position++));
      }

      return (SignatureAttribute.Type)t;
   }

   private static BadBytecode error(String sig) {
      return new BadBytecode("bad signature: " + sig);
   }

   public static class TypeVariable extends SignatureAttribute.ObjectType {
      String name;

      TypeVariable(String sig, int begin, int end) {
         this.name = sig.substring(begin, end);
      }

      public TypeVariable(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      void encode(StringBuffer sb) {
         sb.append('T').append(this.name).append(';');
      }
   }

   public static class ArrayType extends SignatureAttribute.ObjectType {
      int dim;
      SignatureAttribute.Type componentType;

      public ArrayType(int d, SignatureAttribute.Type comp) {
         this.dim = d;
         this.componentType = comp;
      }

      public int getDimension() {
         return this.dim;
      }

      public SignatureAttribute.Type getComponentType() {
         return this.componentType;
      }

      public String toString() {
         StringBuffer sbuf = new StringBuffer(this.componentType.toString());

         for(int i = 0; i < this.dim; ++i) {
            sbuf.append("[]");
         }

         return sbuf.toString();
      }

      void encode(StringBuffer sb) {
         for(int i = 0; i < this.dim; ++i) {
            sb.append('[');
         }

         this.componentType.encode(sb);
      }
   }

   public static class NestedClassType extends SignatureAttribute.ClassType {
      SignatureAttribute.ClassType parent;

      NestedClassType(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, SignatureAttribute.ClassType p) {
         super(s, b, e, targs);
         this.parent = p;
      }

      public NestedClassType(SignatureAttribute.ClassType parent, String className, SignatureAttribute.TypeArgument[] args) {
         super(className, args);
         this.parent = parent;
      }

      public SignatureAttribute.ClassType getDeclaringClass() {
         return this.parent;
      }
   }

   public static class ClassType extends SignatureAttribute.ObjectType {
      String name;
      SignatureAttribute.TypeArgument[] arguments;
      public static SignatureAttribute.ClassType OBJECT = new SignatureAttribute.ClassType("java.lang.Object", (SignatureAttribute.TypeArgument[])null);

      static SignatureAttribute.ClassType make(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, SignatureAttribute.ClassType parent) {
         return (SignatureAttribute.ClassType)(parent == null ? new SignatureAttribute.ClassType(s, b, e, targs) : new SignatureAttribute.NestedClassType(s, b, e, targs, parent));
      }

      ClassType(String signature, int begin, int end, SignatureAttribute.TypeArgument[] targs) {
         this.name = signature.substring(begin, end).replace('/', '.');
         this.arguments = targs;
      }

      public ClassType(String className, SignatureAttribute.TypeArgument[] args) {
         this.name = className;
         this.arguments = args;
      }

      public ClassType(String className) {
         this(className, (SignatureAttribute.TypeArgument[])null);
      }

      public String getName() {
         return this.name;
      }

      public SignatureAttribute.TypeArgument[] getTypeArguments() {
         return this.arguments;
      }

      public SignatureAttribute.ClassType getDeclaringClass() {
         return null;
      }

      public String toString() {
         StringBuffer sbuf = new StringBuffer();
         SignatureAttribute.ClassType parent = this.getDeclaringClass();
         if (parent != null) {
            sbuf.append(parent.toString()).append('.');
         }

         return this.toString2(sbuf);
      }

      private String toString2(StringBuffer sbuf) {
         sbuf.append(this.name);
         if (this.arguments != null) {
            sbuf.append('<');
            int n = this.arguments.length;

            for(int i = 0; i < n; ++i) {
               if (i > 0) {
                  sbuf.append(", ");
               }

               sbuf.append(this.arguments[i].toString());
            }

            sbuf.append('>');
         }

         return sbuf.toString();
      }

      public String jvmTypeName() {
         StringBuffer sbuf = new StringBuffer();
         SignatureAttribute.ClassType parent = this.getDeclaringClass();
         if (parent != null) {
            sbuf.append(parent.jvmTypeName()).append('$');
         }

         return this.toString2(sbuf);
      }

      void encode(StringBuffer sb) {
         sb.append('L');
         this.encode2(sb);
         sb.append(';');
      }

      void encode2(StringBuffer sb) {
         SignatureAttribute.ClassType parent = this.getDeclaringClass();
         if (parent != null) {
            parent.encode2(sb);
            sb.append('$');
         }

         sb.append(this.name.replace('.', '/'));
         if (this.arguments != null) {
            SignatureAttribute.TypeArgument.encode(sb, this.arguments);
         }

      }
   }

   public abstract static class ObjectType extends SignatureAttribute.Type {
      public String encode() {
         StringBuffer sb = new StringBuffer();
         this.encode(sb);
         return sb.toString();
      }
   }

   public static class BaseType extends SignatureAttribute.Type {
      char descriptor;

      BaseType(char c) {
         this.descriptor = c;
      }

      public BaseType(String typeName) {
         this(Descriptor.of(typeName).charAt(0));
      }

      public char getDescriptor() {
         return this.descriptor;
      }

      public CtClass getCtlass() {
         return Descriptor.toPrimitiveClass(this.descriptor);
      }

      public String toString() {
         return Descriptor.toClassName(Character.toString(this.descriptor));
      }

      void encode(StringBuffer sb) {
         sb.append(this.descriptor);
      }
   }

   public abstract static class Type {
      abstract void encode(StringBuffer sb);

      static void toString(StringBuffer sbuf, SignatureAttribute.Type[] ts) {
         for(int i = 0; i < ts.length; ++i) {
            if (i > 0) {
               sbuf.append(", ");
            }

            sbuf.append(ts[i]);
         }

      }

      public String jvmTypeName() {
         return this.toString();
      }
   }

   public static class TypeArgument {
      SignatureAttribute.ObjectType arg;
      char wildcard;

      TypeArgument(SignatureAttribute.ObjectType a, char w) {
         this.arg = a;
         this.wildcard = w;
      }

      public TypeArgument(SignatureAttribute.ObjectType t) {
         this(t, ' ');
      }

      public TypeArgument() {
         this((SignatureAttribute.ObjectType)null, '*');
      }

      public static SignatureAttribute.TypeArgument subclassOf(SignatureAttribute.ObjectType t) {
         return new SignatureAttribute.TypeArgument(t, '+');
      }

      public static SignatureAttribute.TypeArgument superOf(SignatureAttribute.ObjectType t) {
         return new SignatureAttribute.TypeArgument(t, '-');
      }

      public char getKind() {
         return this.wildcard;
      }

      public boolean isWildcard() {
         return this.wildcard != ' ';
      }

      public SignatureAttribute.ObjectType getType() {
         return this.arg;
      }

      public String toString() {
         if (this.wildcard == '*') {
            return "?";
         } else {
            String type = this.arg.toString();
            if (this.wildcard == ' ') {
               return type;
            } else {
               return this.wildcard == '+' ? "? extends " + type : "? super " + type;
            }
         }
      }

      static void encode(StringBuffer sb, SignatureAttribute.TypeArgument[] args) {
         sb.append('<');

         for(int i = 0; i < args.length; ++i) {
            SignatureAttribute.TypeArgument ta = args[i];
            if (ta.isWildcard()) {
               sb.append(ta.wildcard);
            }

            if (ta.getType() != null) {
               ta.getType().encode(sb);
            }
         }

         sb.append('>');
      }
   }

   public static class TypeParameter {
      String name;
      SignatureAttribute.ObjectType superClass;
      SignatureAttribute.ObjectType[] superInterfaces;

      TypeParameter(String sig, int nb, int ne, SignatureAttribute.ObjectType sc, SignatureAttribute.ObjectType[] si) {
         this.name = sig.substring(nb, ne);
         this.superClass = sc;
         this.superInterfaces = si;
      }

      public TypeParameter(String name, SignatureAttribute.ObjectType superClass, SignatureAttribute.ObjectType[] superInterfaces) {
         this.name = name;
         this.superClass = superClass;
         if (superInterfaces == null) {
            this.superInterfaces = new SignatureAttribute.ObjectType[0];
         } else {
            this.superInterfaces = superInterfaces;
         }

      }

      public TypeParameter(String name) {
         this(name, (SignatureAttribute.ObjectType)null, (SignatureAttribute.ObjectType[])null);
      }

      public String getName() {
         return this.name;
      }

      public SignatureAttribute.ObjectType getClassBound() {
         return this.superClass;
      }

      public SignatureAttribute.ObjectType[] getInterfaceBound() {
         return this.superInterfaces;
      }

      public String toString() {
         StringBuffer sbuf = new StringBuffer(this.getName());
         if (this.superClass != null) {
            sbuf.append(" extends ").append(this.superClass.toString());
         }

         int len = this.superInterfaces.length;
         if (len > 0) {
            for(int i = 0; i < len; ++i) {
               if (i <= 0 && this.superClass == null) {
                  sbuf.append(" extends ");
               } else {
                  sbuf.append(" & ");
               }

               sbuf.append(this.superInterfaces[i].toString());
            }
         }

         return sbuf.toString();
      }

      static void toString(StringBuffer sbuf, SignatureAttribute.TypeParameter[] tp) {
         sbuf.append('<');

         for(int i = 0; i < tp.length; ++i) {
            if (i > 0) {
               sbuf.append(", ");
            }

            sbuf.append(tp[i]);
         }

         sbuf.append('>');
      }

      void encode(StringBuffer sb) {
         sb.append(this.name);
         if (this.superClass == null) {
            sb.append(":Ljava/lang/Object;");
         } else {
            sb.append(':');
            this.superClass.encode(sb);
         }

         for(int i = 0; i < this.superInterfaces.length; ++i) {
            sb.append(':');
            this.superInterfaces[i].encode(sb);
         }

      }
   }

   public static class MethodSignature {
      SignatureAttribute.TypeParameter[] typeParams;
      SignatureAttribute.Type[] params;
      SignatureAttribute.Type retType;
      SignatureAttribute.ObjectType[] exceptions;

      public MethodSignature(SignatureAttribute.TypeParameter[] tp, SignatureAttribute.Type[] params, SignatureAttribute.Type ret, SignatureAttribute.ObjectType[] ex) {
         this.typeParams = tp == null ? new SignatureAttribute.TypeParameter[0] : tp;
         this.params = params == null ? new SignatureAttribute.Type[0] : params;
         this.retType = (SignatureAttribute.Type)(ret == null ? new SignatureAttribute.BaseType("void") : ret);
         this.exceptions = ex == null ? new SignatureAttribute.ObjectType[0] : ex;
      }

      public SignatureAttribute.TypeParameter[] getTypeParameters() {
         return this.typeParams;
      }

      public SignatureAttribute.Type[] getParameterTypes() {
         return this.params;
      }

      public SignatureAttribute.Type getReturnType() {
         return this.retType;
      }

      public SignatureAttribute.ObjectType[] getExceptionTypes() {
         return this.exceptions;
      }

      public String toString() {
         StringBuffer sbuf = new StringBuffer();
         SignatureAttribute.TypeParameter.toString(sbuf, this.typeParams);
         sbuf.append(" (");
         SignatureAttribute.Type.toString(sbuf, this.params);
         sbuf.append(") ");
         sbuf.append(this.retType);
         if (this.exceptions.length > 0) {
            sbuf.append(" throws ");
            SignatureAttribute.Type.toString(sbuf, this.exceptions);
         }

         return sbuf.toString();
      }

      public String encode() {
         StringBuffer sbuf = new StringBuffer();
         int i;
         if (this.typeParams.length > 0) {
            sbuf.append('<');

            for(i = 0; i < this.typeParams.length; ++i) {
               this.typeParams[i].encode(sbuf);
            }

            sbuf.append('>');
         }

         sbuf.append('(');

         for(i = 0; i < this.params.length; ++i) {
            this.params[i].encode(sbuf);
         }

         sbuf.append(')');
         this.retType.encode(sbuf);
         if (this.exceptions.length > 0) {
            for(i = 0; i < this.exceptions.length; ++i) {
               sbuf.append('^');
               this.exceptions[i].encode(sbuf);
            }
         }

         return sbuf.toString();
      }
   }

   public static class ClassSignature {
      SignatureAttribute.TypeParameter[] params;
      SignatureAttribute.ClassType superClass;
      SignatureAttribute.ClassType[] interfaces;

      public ClassSignature(SignatureAttribute.TypeParameter[] params, SignatureAttribute.ClassType superClass, SignatureAttribute.ClassType[] interfaces) {
         this.params = params == null ? new SignatureAttribute.TypeParameter[0] : params;
         this.superClass = superClass == null ? SignatureAttribute.ClassType.OBJECT : superClass;
         this.interfaces = interfaces == null ? new SignatureAttribute.ClassType[0] : interfaces;
      }

      public ClassSignature(SignatureAttribute.TypeParameter[] p) {
         this(p, (SignatureAttribute.ClassType)null, (SignatureAttribute.ClassType[])null);
      }

      public SignatureAttribute.TypeParameter[] getParameters() {
         return this.params;
      }

      public SignatureAttribute.ClassType getSuperClass() {
         return this.superClass;
      }

      public SignatureAttribute.ClassType[] getInterfaces() {
         return this.interfaces;
      }

      public String toString() {
         StringBuffer sbuf = new StringBuffer();
         SignatureAttribute.TypeParameter.toString(sbuf, this.params);
         sbuf.append(" extends ").append(this.superClass);
         if (this.interfaces.length > 0) {
            sbuf.append(" implements ");
            SignatureAttribute.Type.toString(sbuf, this.interfaces);
         }

         return sbuf.toString();
      }

      public String encode() {
         StringBuffer sbuf = new StringBuffer();
         int i;
         if (this.params.length > 0) {
            sbuf.append('<');

            for(i = 0; i < this.params.length; ++i) {
               this.params[i].encode(sbuf);
            }

            sbuf.append('>');
         }

         this.superClass.encode(sbuf);

         for(i = 0; i < this.interfaces.length; ++i) {
            this.interfaces[i].encode(sbuf);
         }

         return sbuf.toString();
      }
   }

   private static class Cursor {
      int position;

      private Cursor() {
         this.position = 0;
      }

      int indexOf(String s, int ch) throws BadBytecode {
         int i = s.indexOf(ch, this.position);
         if (i < 0) {
            throw SignatureAttribute.error(s);
         } else {
            this.position = i + 1;
            return i;
         }
      }

      // $FF: synthetic method
      Cursor(Object x0) {
         this();
      }
   }
}
