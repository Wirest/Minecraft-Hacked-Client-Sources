package javassist.bytecode;

class CodeAnalyzer implements Opcode {
   private ConstPool constPool;
   private CodeAttribute codeAttr;

   public CodeAnalyzer(CodeAttribute ca) {
      this.codeAttr = ca;
      this.constPool = ca.getConstPool();
   }

   public int computeMaxStack() throws BadBytecode {
      CodeIterator ci = this.codeAttr.iterator();
      int length = ci.getCodeLength();
      int[] stack = new int[length];
      this.constPool = this.codeAttr.getConstPool();
      this.initStack(stack, this.codeAttr);

      boolean repeat;
      int maxStack;
      do {
         repeat = false;

         for(maxStack = 0; maxStack < length; ++maxStack) {
            if (stack[maxStack] < 0) {
               repeat = true;
               this.visitBytecode(ci, stack, maxStack);
            }
         }
      } while(repeat);

      maxStack = 1;

      for(int i = 0; i < length; ++i) {
         if (stack[i] > maxStack) {
            maxStack = stack[i];
         }
      }

      return maxStack - 1;
   }

   private void initStack(int[] stack, CodeAttribute ca) {
      stack[0] = -1;
      ExceptionTable et = ca.getExceptionTable();
      if (et != null) {
         int size = et.size();

         for(int i = 0; i < size; ++i) {
            stack[et.handlerPc(i)] = -2;
         }
      }

   }

   private void visitBytecode(CodeIterator ci, int[] stack, int index) throws BadBytecode {
      int codeLength = stack.length;
      ci.move(index);
      int stackDepth = -stack[index];
      int[] jsrDepth = new int[]{-1};

      while(ci.hasNext()) {
         index = ci.next();
         stack[index] = stackDepth;
         int op = ci.byteAt(index);
         stackDepth = this.visitInst(op, ci, index, stackDepth);
         if (stackDepth < 1) {
            throw new BadBytecode("stack underflow at " + index);
         }

         if (this.processBranch(op, ci, index, codeLength, stack, stackDepth, jsrDepth) || isEnd(op)) {
            break;
         }

         if (op == 168 || op == 201) {
            --stackDepth;
         }
      }

   }

   private boolean processBranch(int opcode, CodeIterator ci, int index, int codeLength, int[] stack, int stackDepth, int[] jsrDepth) throws BadBytecode {
      int target;
      if ((153 > opcode || opcode > 166) && opcode != 198 && opcode != 199) {
         switch(opcode) {
         case 167:
            target = index + ci.s16bitAt(index + 1);
            this.checkTarget(index, target, codeLength, stack, stackDepth);
            return true;
         case 168:
         case 201:
            if (opcode == 168) {
               target = index + ci.s16bitAt(index + 1);
            } else {
               target = index + ci.s32bitAt(index + 1);
            }

            this.checkTarget(index, target, codeLength, stack, stackDepth);
            if (jsrDepth[0] < 0) {
               jsrDepth[0] = stackDepth;
               return false;
            }

            if (stackDepth == jsrDepth[0]) {
               return false;
            }

            throw new BadBytecode("sorry, cannot compute this data flow due to JSR: " + stackDepth + "," + jsrDepth[0]);
         case 169:
            if (jsrDepth[0] < 0) {
               jsrDepth[0] = stackDepth + 1;
               return false;
            }

            if (stackDepth + 1 == jsrDepth[0]) {
               return true;
            }

            throw new BadBytecode("sorry, cannot compute this data flow due to RET: " + stackDepth + "," + jsrDepth[0]);
         case 170:
         case 171:
            int index2 = (index & -4) + 4;
            target = index + ci.s32bitAt(index2);
            this.checkTarget(index, target, codeLength, stack, stackDepth);
            int npairs;
            int i;
            if (opcode == 171) {
               npairs = ci.s32bitAt(index2 + 4);
               index2 += 12;

               for(i = 0; i < npairs; ++i) {
                  target = index + ci.s32bitAt(index2);
                  this.checkTarget(index, target, codeLength, stack, stackDepth);
                  index2 += 8;
               }
            } else {
               npairs = ci.s32bitAt(index2 + 4);
               i = ci.s32bitAt(index2 + 8);
               int n = i - npairs + 1;
               index2 += 12;

               for(int i = 0; i < n; ++i) {
                  target = index + ci.s32bitAt(index2);
                  this.checkTarget(index, target, codeLength, stack, stackDepth);
                  index2 += 4;
               }
            }

            return true;
         case 200:
            target = index + ci.s32bitAt(index + 1);
            this.checkTarget(index, target, codeLength, stack, stackDepth);
            return true;
         }
      } else {
         target = index + ci.s16bitAt(index + 1);
         this.checkTarget(index, target, codeLength, stack, stackDepth);
      }

      return false;
   }

   private void checkTarget(int opIndex, int target, int codeLength, int[] stack, int stackDepth) throws BadBytecode {
      if (target >= 0 && codeLength > target) {
         int d = stack[target];
         if (d == 0) {
            stack[target] = -stackDepth;
         } else if (d != stackDepth && d != -stackDepth) {
            throw new BadBytecode("verification error (" + stackDepth + "," + d + ") at " + opIndex);
         }

      } else {
         throw new BadBytecode("bad branch offset at " + opIndex);
      }
   }

   private static boolean isEnd(int opcode) {
      return 172 <= opcode && opcode <= 177 || opcode == 191;
   }

   private int visitInst(int op, CodeIterator ci, int index, int stack) throws BadBytecode {
      String desc;
      switch(op) {
      case 178:
         stack += this.getFieldSize(ci, index);
         break;
      case 179:
         stack -= this.getFieldSize(ci, index);
         break;
      case 180:
         stack += this.getFieldSize(ci, index) - 1;
         break;
      case 181:
         stack -= this.getFieldSize(ci, index) + 1;
         break;
      case 182:
      case 183:
         desc = this.constPool.getMethodrefType(ci.u16bitAt(index + 1));
         stack += Descriptor.dataSize(desc) - 1;
         break;
      case 184:
         desc = this.constPool.getMethodrefType(ci.u16bitAt(index + 1));
         stack += Descriptor.dataSize(desc);
         break;
      case 185:
         desc = this.constPool.getInterfaceMethodrefType(ci.u16bitAt(index + 1));
         stack += Descriptor.dataSize(desc) - 1;
         break;
      case 186:
         desc = this.constPool.getInvokeDynamicType(ci.u16bitAt(index + 1));
         stack += Descriptor.dataSize(desc);
         break;
      case 191:
         stack = 1;
         break;
      case 196:
         op = ci.byteAt(index + 1);
      case 187:
      case 188:
      case 189:
      case 190:
      case 192:
      case 193:
      case 194:
      case 195:
      default:
         stack += STACK_GROW[op];
         break;
      case 197:
         stack += 1 - ci.byteAt(index + 3);
      }

      return stack;
   }

   private int getFieldSize(CodeIterator ci, int index) {
      String desc = this.constPool.getFieldrefType(ci.u16bitAt(index + 1));
      return Descriptor.dataSize(desc);
   }
}
