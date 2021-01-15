package javassist.expr;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;

public class ExprEditor {
   public boolean doit(CtClass clazz, MethodInfo minfo) throws CannotCompileException {
      CodeAttribute codeAttr = minfo.getCodeAttribute();
      if (codeAttr == null) {
         return false;
      } else {
         CodeIterator iterator = codeAttr.iterator();
         boolean edited = false;
         ExprEditor.LoopContext context = new ExprEditor.LoopContext(codeAttr.getMaxLocals());

         while(iterator.hasNext()) {
            if (this.loopBody(iterator, clazz, minfo, context)) {
               edited = true;
            }
         }

         ExceptionTable et = codeAttr.getExceptionTable();
         int n = et.size();

         for(int i = 0; i < n; ++i) {
            Handler h = new Handler(et, i, iterator, clazz, minfo);
            this.edit(h);
            if (h.edited()) {
               edited = true;
               context.updateMax(h.locals(), h.stack());
            }
         }

         if (codeAttr.getMaxLocals() < context.maxLocals) {
            codeAttr.setMaxLocals(context.maxLocals);
         }

         codeAttr.setMaxStack(codeAttr.getMaxStack() + context.maxStack);

         try {
            if (edited) {
               minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
            }

            return edited;
         } catch (BadBytecode var11) {
            throw new CannotCompileException(var11.getMessage(), var11);
         }
      }
   }

   boolean doit(CtClass clazz, MethodInfo minfo, ExprEditor.LoopContext context, CodeIterator iterator, int endPos) throws CannotCompileException {
      boolean edited = false;

      while(iterator.hasNext() && iterator.lookAhead() < endPos) {
         int size = iterator.getCodeLength();
         if (this.loopBody(iterator, clazz, minfo, context)) {
            edited = true;
            int size2 = iterator.getCodeLength();
            if (size != size2) {
               endPos += size2 - size;
            }
         }
      }

      return edited;
   }

   final boolean loopBody(CodeIterator iterator, CtClass clazz, MethodInfo minfo, ExprEditor.LoopContext context) throws CannotCompileException {
      try {
         Expr expr = null;
         int pos = iterator.next();
         int c = iterator.byteAt(pos);
         if (c >= 178) {
            if (c < 188) {
               if (c != 184 && c != 185 && c != 182) {
                  if (c != 180 && c != 178 && c != 181 && c != 179) {
                     if (c == 187) {
                        int index = iterator.u16bitAt(pos + 1);
                        context.newList = new ExprEditor.NewOp(context.newList, pos, minfo.getConstPool().getClassInfo(index));
                     } else if (c == 183) {
                        ExprEditor.NewOp newList = context.newList;
                        if (newList != null && minfo.getConstPool().isConstructor(newList.type, iterator.u16bitAt(pos + 1)) > 0) {
                           expr = new NewExpr(pos, iterator, clazz, minfo, newList.type, newList.pos);
                           this.edit((NewExpr)expr);
                           context.newList = newList.next;
                        } else {
                           MethodCall mcall = new MethodCall(pos, iterator, clazz, minfo);
                           if (mcall.getMethodName().equals("<init>")) {
                              ConstructorCall ccall = new ConstructorCall(pos, iterator, clazz, minfo);
                              expr = ccall;
                              this.edit(ccall);
                           } else {
                              expr = mcall;
                              this.edit(mcall);
                           }
                        }
                     }
                  } else {
                     expr = new FieldAccess(pos, iterator, clazz, minfo, c);
                     this.edit((FieldAccess)expr);
                  }
               } else {
                  expr = new MethodCall(pos, iterator, clazz, minfo);
                  this.edit((MethodCall)expr);
               }
            } else if (c != 188 && c != 189 && c != 197) {
               if (c == 193) {
                  expr = new Instanceof(pos, iterator, clazz, minfo);
                  this.edit((Instanceof)expr);
               } else if (c == 192) {
                  expr = new Cast(pos, iterator, clazz, minfo);
                  this.edit((Cast)expr);
               }
            } else {
               expr = new NewArray(pos, iterator, clazz, minfo, c);
               this.edit((NewArray)expr);
            }
         }

         if (expr != null && ((Expr)expr).edited()) {
            context.updateMax(((Expr)expr).locals(), ((Expr)expr).stack());
            return true;
         } else {
            return false;
         }
      } catch (BadBytecode var11) {
         throw new CannotCompileException(var11);
      }
   }

   public void edit(NewExpr e) throws CannotCompileException {
   }

   public void edit(NewArray a) throws CannotCompileException {
   }

   public void edit(MethodCall m) throws CannotCompileException {
   }

   public void edit(ConstructorCall c) throws CannotCompileException {
   }

   public void edit(FieldAccess f) throws CannotCompileException {
   }

   public void edit(Instanceof i) throws CannotCompileException {
   }

   public void edit(Cast c) throws CannotCompileException {
   }

   public void edit(Handler h) throws CannotCompileException {
   }

   static final class LoopContext {
      ExprEditor.NewOp newList;
      int maxLocals;
      int maxStack;

      LoopContext(int locals) {
         this.maxLocals = locals;
         this.maxStack = 0;
         this.newList = null;
      }

      void updateMax(int locals, int stack) {
         if (this.maxLocals < locals) {
            this.maxLocals = locals;
         }

         if (this.maxStack < stack) {
            this.maxStack = stack;
         }

      }
   }

   static final class NewOp {
      ExprEditor.NewOp next;
      int pos;
      String type;

      NewOp(ExprEditor.NewOp n, int p, String t) {
         this.next = n;
         this.pos = p;
         this.type = t;
      }
   }
}
