package javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class SubroutineScanner implements Opcode {
   private Subroutine[] subroutines;
   Map subTable = new HashMap();
   Set done = new HashSet();

   public Subroutine[] scan(MethodInfo method) throws BadBytecode {
      CodeAttribute code = method.getCodeAttribute();
      CodeIterator iter = code.iterator();
      this.subroutines = new Subroutine[code.getCodeLength()];
      this.subTable.clear();
      this.done.clear();
      this.scan(0, iter, (Subroutine)null);
      ExceptionTable exceptions = code.getExceptionTable();

      for(int i = 0; i < exceptions.size(); ++i) {
         int handler = exceptions.handlerPc(i);
         this.scan(handler, iter, this.subroutines[exceptions.startPc(i)]);
      }

      return this.subroutines;
   }

   private void scan(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
      if (!this.done.contains(new Integer(pos))) {
         this.done.add(new Integer(pos));
         int old = iter.lookAhead();
         iter.move(pos);

         boolean next;
         do {
            pos = iter.next();
            next = this.scanOp(pos, iter, sub) && iter.hasNext();
         } while(next);

         iter.move(old);
      }
   }

   private boolean scanOp(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
      this.subroutines[pos] = sub;
      int opcode = iter.byteAt(pos);
      if (opcode == 170) {
         this.scanTableSwitch(pos, iter, sub);
         return false;
      } else if (opcode == 171) {
         this.scanLookupSwitch(pos, iter, sub);
         return false;
      } else if (!Util.isReturn(opcode) && opcode != 169 && opcode != 191) {
         if (Util.isJumpInstruction(opcode)) {
            int target = Util.getJumpTarget(pos, iter);
            if (opcode != 168 && opcode != 201) {
               this.scan(target, iter, sub);
               if (Util.isGoto(opcode)) {
                  return false;
               }
            } else {
               Subroutine s = (Subroutine)this.subTable.get(new Integer(target));
               if (s == null) {
                  s = new Subroutine(target, pos);
                  this.subTable.put(new Integer(target), s);
                  this.scan(target, iter, s);
               } else {
                  s.addCaller(pos);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void scanLookupSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
      int index = (pos & -4) + 4;
      this.scan(pos + iter.s32bitAt(index), iter, sub);
      index += 4;
      int npairs = iter.s32bitAt(index);
      int var10000 = npairs * 8;
      index += 4;
      int end = var10000 + index;

      for(index += 4; index < end; index += 8) {
         int target = iter.s32bitAt(index) + pos;
         this.scan(target, iter, sub);
      }

   }

   private void scanTableSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
      int index = (pos & -4) + 4;
      this.scan(pos + iter.s32bitAt(index), iter, sub);
      index += 4;
      int low = iter.s32bitAt(index);
      index += 4;
      int high = iter.s32bitAt(index);
      int var10000 = (high - low + 1) * 4;
      index += 4;

      for(int end = var10000 + index; index < end; index += 4) {
         int target = iter.s32bitAt(index) + pos;
         this.scan(target, iter, sub);
      }

   }
}
