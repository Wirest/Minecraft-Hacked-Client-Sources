package javassist.bytecode;

import java.io.PrintWriter;
import java.util.List;
import javassist.Modifier;

public class ClassFilePrinter {
   public static void print(ClassFile cf) {
      print(cf, new PrintWriter(System.out, true));
   }

   public static void print(ClassFile cf, PrintWriter out) {
      int mod = AccessFlag.toModifier(cf.getAccessFlags() & -33);
      out.println("major: " + cf.major + ", minor: " + cf.minor + " modifiers: " + Integer.toHexString(cf.getAccessFlags()));
      out.println(Modifier.toString(mod) + " class " + cf.getName() + " extends " + cf.getSuperclass());
      String[] infs = cf.getInterfaces();
      int i;
      if (infs != null && infs.length > 0) {
         out.print("    implements ");
         out.print(infs[0]);

         for(i = 1; i < infs.length; ++i) {
            out.print(", " + infs[i]);
         }

         out.println();
      }

      out.println();
      List list = cf.getFields();
      int n = list.size();

      int acc;
      for(i = 0; i < n; ++i) {
         FieldInfo finfo = (FieldInfo)list.get(i);
         acc = finfo.getAccessFlags();
         out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + finfo.getName() + "\t" + finfo.getDescriptor());
         printAttributes(finfo.getAttributes(), out, 'f');
      }

      out.println();
      list = cf.getMethods();
      n = list.size();

      for(i = 0; i < n; ++i) {
         MethodInfo minfo = (MethodInfo)list.get(i);
         acc = minfo.getAccessFlags();
         out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + minfo.getName() + "\t" + minfo.getDescriptor());
         printAttributes(minfo.getAttributes(), out, 'm');
         out.println();
      }

      out.println();
      printAttributes(cf.getAttributes(), out, 'c');
   }

   static void printAttributes(List list, PrintWriter out, char kind) {
      if (list != null) {
         int n = list.size();

         for(int i = 0; i < n; ++i) {
            AttributeInfo ai = (AttributeInfo)list.get(i);
            if (ai instanceof CodeAttribute) {
               CodeAttribute ca = (CodeAttribute)ai;
               out.println("attribute: " + ai.getName() + ": " + ai.getClass().getName());
               out.println("max stack " + ca.getMaxStack() + ", max locals " + ca.getMaxLocals() + ", " + ca.getExceptionTable().size() + " catch blocks");
               out.println("<code attribute begin>");
               printAttributes(ca.getAttributes(), out, kind);
               out.println("<code attribute end>");
            } else if (ai instanceof AnnotationsAttribute) {
               out.println("annnotation: " + ai.toString());
            } else if (ai instanceof ParameterAnnotationsAttribute) {
               out.println("parameter annnotations: " + ai.toString());
            } else if (ai instanceof StackMapTable) {
               out.println("<stack map table begin>");
               StackMapTable.Printer.print((StackMapTable)ai, out);
               out.println("<stack map table end>");
            } else if (ai instanceof StackMap) {
               out.println("<stack map begin>");
               ((StackMap)ai).print(out);
               out.println("<stack map end>");
            } else if (ai instanceof SignatureAttribute) {
               SignatureAttribute sa = (SignatureAttribute)ai;
               String sig = sa.getSignature();
               out.println("signature: " + sig);

               try {
                  String s;
                  if (kind == 'c') {
                     s = SignatureAttribute.toClassSignature(sig).toString();
                  } else if (kind == 'm') {
                     s = SignatureAttribute.toMethodSignature(sig).toString();
                  } else {
                     s = SignatureAttribute.toFieldSignature(sig).toString();
                  }

                  out.println("           " + s);
               } catch (BadBytecode var9) {
                  out.println("           syntax error");
               }
            } else {
               out.println("attribute: " + ai.getName() + " (" + ai.get().length + " byte): " + ai.getClass().getName());
            }
         }

      }
   }
}
