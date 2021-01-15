package javassist.bytecode.analysis;

import java.util.Iterator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class Analyzer implements Opcode {
   private final SubroutineScanner scanner = new SubroutineScanner();
   private CtClass clazz;
   private Analyzer.ExceptionInfo[] exceptions;
   private Frame[] frames;
   private Subroutine[] subroutines;

   public Frame[] analyze(CtClass clazz, MethodInfo method) throws BadBytecode {
      this.clazz = clazz;
      CodeAttribute codeAttribute = method.getCodeAttribute();
      if (codeAttribute == null) {
         return null;
      } else {
         int maxLocals = codeAttribute.getMaxLocals();
         int maxStack = codeAttribute.getMaxStack();
         int codeLength = codeAttribute.getCodeLength();
         CodeIterator iter = codeAttribute.iterator();
         IntQueue queue = new IntQueue();
         this.exceptions = this.buildExceptionInfo(method);
         this.subroutines = this.scanner.scan(method);
         Executor executor = new Executor(clazz.getClassPool(), method.getConstPool());
         this.frames = new Frame[codeLength];
         this.frames[iter.lookAhead()] = this.firstFrame(method, maxLocals, maxStack);
         queue.add(iter.next());

         while(!queue.isEmpty()) {
            this.analyzeNextEntry(method, iter, queue, executor);
         }

         return this.frames;
      }
   }

   public Frame[] analyze(CtMethod method) throws BadBytecode {
      return this.analyze(method.getDeclaringClass(), method.getMethodInfo2());
   }

   private void analyzeNextEntry(MethodInfo method, CodeIterator iter, IntQueue queue, Executor executor) throws BadBytecode {
      int pos = queue.take();
      iter.move(pos);
      iter.next();
      Frame frame = this.frames[pos].copy();
      Subroutine subroutine = this.subroutines[pos];

      try {
         executor.execute(method, pos, iter, frame, subroutine);
      } catch (RuntimeException var10) {
         throw new BadBytecode(var10.getMessage() + "[pos = " + pos + "]", var10);
      }

      int opcode = iter.byteAt(pos);
      if (opcode == 170) {
         this.mergeTableSwitch(queue, pos, iter, frame);
      } else if (opcode == 171) {
         this.mergeLookupSwitch(queue, pos, iter, frame);
      } else if (opcode == 169) {
         this.mergeRet(queue, iter, pos, frame, subroutine);
      } else if (Util.isJumpInstruction(opcode)) {
         int target = Util.getJumpTarget(pos, iter);
         if (Util.isJsr(opcode)) {
            this.mergeJsr(queue, this.frames[pos], this.subroutines[target], pos, this.lookAhead(iter, pos));
         } else if (!Util.isGoto(opcode)) {
            this.merge(queue, frame, this.lookAhead(iter, pos));
         }

         this.merge(queue, frame, target);
      } else if (opcode != 191 && !Util.isReturn(opcode)) {
         this.merge(queue, frame, this.lookAhead(iter, pos));
      }

      this.mergeExceptionHandlers(queue, method, pos, frame);
   }

   private Analyzer.ExceptionInfo[] buildExceptionInfo(MethodInfo method) {
      ConstPool constPool = method.getConstPool();
      ClassPool classes = this.clazz.getClassPool();
      ExceptionTable table = method.getCodeAttribute().getExceptionTable();
      Analyzer.ExceptionInfo[] exceptions = new Analyzer.ExceptionInfo[table.size()];

      for(int i = 0; i < table.size(); ++i) {
         int index = table.catchType(i);

         Type type;
         try {
            type = index == 0 ? Type.THROWABLE : Type.get(classes.get(constPool.getClassInfo(index)));
         } catch (NotFoundException var10) {
            throw new IllegalStateException(var10.getMessage());
         }

         exceptions[i] = new Analyzer.ExceptionInfo(table.startPc(i), table.endPc(i), table.handlerPc(i), type);
      }

      return exceptions;
   }

   private Frame firstFrame(MethodInfo method, int maxLocals, int maxStack) {
      int pos = 0;
      Frame first = new Frame(maxLocals, maxStack);
      if ((method.getAccessFlags() & 8) == 0) {
         first.setLocal(pos++, Type.get(this.clazz));
      }

      CtClass[] parameters;
      try {
         parameters = Descriptor.getParameterTypes(method.getDescriptor(), this.clazz.getClassPool());
      } catch (NotFoundException var9) {
         throw new RuntimeException(var9);
      }

      for(int i = 0; i < parameters.length; ++i) {
         Type type = this.zeroExtend(Type.get(parameters[i]));
         first.setLocal(pos++, type);
         if (type.getSize() == 2) {
            first.setLocal(pos++, Type.TOP);
         }
      }

      return first;
   }

   private int getNext(CodeIterator iter, int of, int restore) throws BadBytecode {
      iter.move(of);
      iter.next();
      int next = iter.lookAhead();
      iter.move(restore);
      iter.next();
      return next;
   }

   private int lookAhead(CodeIterator iter, int pos) throws BadBytecode {
      if (!iter.hasNext()) {
         throw new BadBytecode("Execution falls off end! [pos = " + pos + "]");
      } else {
         return iter.lookAhead();
      }
   }

   private void merge(IntQueue queue, Frame frame, int target) {
      Frame old = this.frames[target];
      boolean changed;
      if (old == null) {
         this.frames[target] = frame.copy();
         changed = true;
      } else {
         changed = old.merge(frame);
      }

      if (changed) {
         queue.add(target);
      }

   }

   private void mergeExceptionHandlers(IntQueue queue, MethodInfo method, int pos, Frame frame) {
      for(int i = 0; i < this.exceptions.length; ++i) {
         Analyzer.ExceptionInfo exception = this.exceptions[i];
         if (pos >= exception.start && pos < exception.end) {
            Frame newFrame = frame.copy();
            newFrame.clearStack();
            newFrame.push(exception.type);
            this.merge(queue, newFrame, exception.handler);
         }
      }

   }

   private void mergeJsr(IntQueue queue, Frame frame, Subroutine sub, int pos, int next) throws BadBytecode {
      if (sub == null) {
         throw new BadBytecode("No subroutine at jsr target! [pos = " + pos + "]");
      } else {
         Frame old = this.frames[next];
         boolean changed = false;
         if (old == null) {
            old = this.frames[next] = frame.copy();
            changed = true;
         } else {
            for(int i = 0; i < frame.localsLength(); ++i) {
               if (!sub.isAccessed(i)) {
                  Type oldType = old.getLocal(i);
                  Type newType = frame.getLocal(i);
                  if (oldType == null) {
                     old.setLocal(i, newType);
                     changed = true;
                  } else {
                     newType = oldType.merge(newType);
                     old.setLocal(i, newType);
                     if (!newType.equals(oldType) || newType.popChanged()) {
                        changed = true;
                     }
                  }
               }
            }
         }

         if (!old.isJsrMerged()) {
            old.setJsrMerged(true);
            changed = true;
         }

         if (changed && old.isRetMerged()) {
            queue.add(next);
         }

      }
   }

   private void mergeLookupSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
      int index = (pos & -4) + 4;
      this.merge(queue, frame, pos + iter.s32bitAt(index));
      index += 4;
      int npairs = iter.s32bitAt(index);
      int var10000 = npairs * 8;
      index += 4;
      int end = var10000 + index;

      for(index += 4; index < end; index += 8) {
         int target = iter.s32bitAt(index) + pos;
         this.merge(queue, frame, target);
      }

   }

   private void mergeRet(IntQueue queue, CodeIterator iter, int pos, Frame frame, Subroutine subroutine) throws BadBytecode {
      if (subroutine == null) {
         throw new BadBytecode("Ret on no subroutine! [pos = " + pos + "]");
      } else {
         Iterator callerIter = subroutine.callers().iterator();

         while(callerIter.hasNext()) {
            int caller = (Integer)callerIter.next();
            int returnLoc = this.getNext(iter, caller, pos);
            boolean changed = false;
            Frame old = this.frames[returnLoc];
            if (old == null) {
               old = this.frames[returnLoc] = frame.copyStack();
               changed = true;
            } else {
               changed = old.mergeStack(frame);
            }

            Iterator i = subroutine.accessed().iterator();

            while(i.hasNext()) {
               int index = (Integer)i.next();
               Type oldType = old.getLocal(index);
               Type newType = frame.getLocal(index);
               if (oldType != newType) {
                  old.setLocal(index, newType);
                  changed = true;
               }
            }

            if (!old.isRetMerged()) {
               old.setRetMerged(true);
               changed = true;
            }

            if (changed && old.isJsrMerged()) {
               queue.add(returnLoc);
            }
         }

      }
   }

   private void mergeTableSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
      int index = (pos & -4) + 4;
      this.merge(queue, frame, pos + iter.s32bitAt(index));
      index += 4;
      int low = iter.s32bitAt(index);
      index += 4;
      int high = iter.s32bitAt(index);
      int var10000 = (high - low + 1) * 4;
      index += 4;

      for(int end = var10000 + index; index < end; index += 4) {
         int target = iter.s32bitAt(index) + pos;
         this.merge(queue, frame, target);
      }

   }

   private Type zeroExtend(Type type) {
      return type != Type.SHORT && type != Type.BYTE && type != Type.CHAR && type != Type.BOOLEAN ? type : Type.INTEGER;
   }

   private static class ExceptionInfo {
      private int end;
      private int handler;
      private int start;
      private Type type;

      private ExceptionInfo(int start, int end, int handler, Type type) {
         this.start = start;
         this.end = end;
         this.handler = handler;
         this.type = type;
      }

      // $FF: synthetic method
      ExceptionInfo(int x0, int x1, int x2, Type x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }
}
