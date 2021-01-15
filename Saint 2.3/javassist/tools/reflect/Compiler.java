package javassist.tools.reflect;

import java.io.PrintStream;
import javassist.ClassPool;
import javassist.CtClass;

public class Compiler {
   public static void main(String[] args) throws Exception {
      if (args.length == 0) {
         help(System.err);
      } else {
         CompiledClass[] entries = new CompiledClass[args.length];
         int n = parse(args, entries);
         if (n < 1) {
            System.err.println("bad parameter.");
         } else {
            processClasses(entries, n);
         }
      }
   }

   private static void processClasses(CompiledClass[] entries, int n) throws Exception {
      Reflection implementor = new Reflection();
      ClassPool pool = ClassPool.getDefault();
      implementor.start(pool);

      int i;
      for(i = 0; i < n; ++i) {
         CtClass c = pool.get(entries[i].classname);
         if (entries[i].metaobject == null && entries[i].classobject == null) {
            System.err.println(c.getName() + ": not reflective");
         } else {
            String metaobj;
            if (entries[i].metaobject == null) {
               metaobj = "javassist.tools.reflect.Metaobject";
            } else {
               metaobj = entries[i].metaobject;
            }

            String classobj;
            if (entries[i].classobject == null) {
               classobj = "javassist.tools.reflect.ClassMetaobject";
            } else {
               classobj = entries[i].classobject;
            }

            if (!implementor.makeReflective(c, pool.get(metaobj), pool.get(classobj))) {
               System.err.println("Warning: " + c.getName() + " is reflective.  It was not changed.");
            }

            System.err.println(c.getName() + ": " + metaobj + ", " + classobj);
         }
      }

      for(i = 0; i < n; ++i) {
         implementor.onLoad(pool, entries[i].classname);
         pool.get(entries[i].classname).writeFile();
      }

   }

   private static int parse(String[] args, CompiledClass[] result) {
      int n = -1;

      for(int i = 0; i < args.length; ++i) {
         String a = args[i];
         CompiledClass var10000;
         if (a.equals("-m")) {
            if (n < 0 || i + 1 > args.length) {
               return -1;
            }

            var10000 = result[n];
            ++i;
            var10000.metaobject = args[i];
         } else if (a.equals("-c")) {
            if (n < 0 || i + 1 > args.length) {
               return -1;
            }

            var10000 = result[n];
            ++i;
            var10000.classobject = args[i];
         } else {
            if (a.charAt(0) == '-') {
               return -1;
            }

            CompiledClass cc = new CompiledClass();
            cc.classname = a;
            cc.metaobject = null;
            cc.classobject = null;
            ++n;
            result[n] = cc;
         }
      }

      return n + 1;
   }

   private static void help(PrintStream out) {
      out.println("Usage: java javassist.tools.reflect.Compiler");
      out.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
   }
}
