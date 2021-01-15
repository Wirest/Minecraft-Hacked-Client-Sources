package javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;

public class BasicBlock {
   protected int position;
   protected int length;
   protected int incoming;
   protected BasicBlock[] exit;
   protected boolean stop;
   protected BasicBlock.Catch toCatch;

   protected BasicBlock(int pos) {
      this.position = pos;
      this.length = 0;
      this.incoming = 0;
   }

   public static BasicBlock find(BasicBlock[] blocks, int pos) throws BadBytecode {
      for(int i = 0; i < blocks.length; ++i) {
         int iPos = blocks[i].position;
         if (iPos <= pos && pos < iPos + blocks[i].length) {
            return blocks[i];
         }
      }

      throw new BadBytecode("no basic block at " + pos);
   }

   public String toString() {
      StringBuffer sbuf = new StringBuffer();
      String cname = this.getClass().getName();
      int i = cname.lastIndexOf(46);
      sbuf.append(i < 0 ? cname : cname.substring(i + 1));
      sbuf.append("[");
      this.toString2(sbuf);
      sbuf.append("]");
      return sbuf.toString();
   }

   protected void toString2(StringBuffer sbuf) {
      sbuf.append("pos=").append(this.position).append(", len=").append(this.length).append(", in=").append(this.incoming).append(", exit{");
      if (this.exit != null) {
         for(int i = 0; i < this.exit.length; ++i) {
            sbuf.append(this.exit[i].position).append(",");
         }
      }

      sbuf.append("}, {");

      for(BasicBlock.Catch th = this.toCatch; th != null; th = th.next) {
         sbuf.append("(").append(th.body.position).append(", ").append(th.typeIndex).append("), ");
      }

      sbuf.append("}");
   }

   public static class Maker {
      protected BasicBlock makeBlock(int pos) {
         return new BasicBlock(pos);
      }

      protected BasicBlock[] makeArray(int size) {
         return new BasicBlock[size];
      }

      private BasicBlock[] makeArray(BasicBlock b) {
         BasicBlock[] array = this.makeArray(1);
         array[0] = b;
         return array;
      }

      private BasicBlock[] makeArray(BasicBlock b1, BasicBlock b2) {
         BasicBlock[] array = this.makeArray(2);
         array[0] = b1;
         array[1] = b2;
         return array;
      }

      public BasicBlock[] make(MethodInfo minfo) throws BadBytecode {
         CodeAttribute ca = minfo.getCodeAttribute();
         if (ca == null) {
            return null;
         } else {
            CodeIterator ci = ca.iterator();
            return this.make(ci, 0, ci.getCodeLength(), ca.getExceptionTable());
         }
      }

      public BasicBlock[] make(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
         HashMap marks = this.makeMarks(ci, begin, end, et);
         BasicBlock[] bb = this.makeBlocks(marks);
         this.addCatchers(bb, et);
         return bb;
      }

      private BasicBlock.Mark makeMark(HashMap table, int pos) {
         return this.makeMark0(table, pos, true, true);
      }

      private BasicBlock.Mark makeMark(HashMap table, int pos, BasicBlock[] jump, int size, boolean always) {
         BasicBlock.Mark m = this.makeMark0(table, pos, false, false);
         m.setJump(jump, size, always);
         return m;
      }

      private BasicBlock.Mark makeMark0(HashMap table, int pos, boolean isBlockBegin, boolean isTarget) {
         Integer p = new Integer(pos);
         BasicBlock.Mark m = (BasicBlock.Mark)table.get(p);
         if (m == null) {
            m = new BasicBlock.Mark(pos);
            table.put(p, m);
         }

         if (isBlockBegin) {
            if (m.block == null) {
               m.block = this.makeBlock(pos);
            }

            if (isTarget) {
               ++m.block.incoming;
            }
         }

         return m;
      }

      private HashMap makeMarks(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
         ci.begin();
         ci.move(begin);
         HashMap marks = new HashMap();

         int index;
         while(ci.hasNext()) {
            index = ci.next();
            if (index >= end) {
               break;
            }

            int op = ci.byteAt(index);
            if ((153 > op || op > 166) && op != 198 && op != 199) {
               if (167 <= op && op <= 171) {
                  int p;
                  int p;
                  int pos;
                  int ncases;
                  switch(op) {
                  case 167:
                     this.makeGoto(marks, index, index + ci.s16bitAt(index + 1), 3);
                     break;
                  case 168:
                     this.makeJsr(marks, index, index + ci.s16bitAt(index + 1), 3);
                     break;
                  case 169:
                     this.makeMark(marks, index, (BasicBlock[])null, 2, true);
                     break;
                  case 170:
                     pos = (index & -4) + 4;
                     ncases = ci.s32bitAt(pos + 4);
                     int high = ci.s32bitAt(pos + 8);
                     p = high - ncases + 1;
                     BasicBlock[] to = this.makeArray(p + 1);
                     to[0] = this.makeMark(marks, index + ci.s32bitAt(pos)).block;
                     p = pos + 12;
                     int n = p + p * 4;

                     for(int var15 = 1; p < n; p += 4) {
                        to[var15++] = this.makeMark(marks, index + ci.s32bitAt(p)).block;
                     }

                     this.makeMark(marks, index, to, n - index, true);
                     break;
                  case 171:
                     pos = (index & -4) + 4;
                     ncases = ci.s32bitAt(pos + 4);
                     BasicBlock[] to = this.makeArray(ncases + 1);
                     to[0] = this.makeMark(marks, index + ci.s32bitAt(pos)).block;
                     p = pos + 8 + 4;
                     int n = p + ncases * 8 - 4;

                     for(p = 1; p < n; p += 8) {
                        to[p++] = this.makeMark(marks, index + ci.s32bitAt(p)).block;
                     }

                     this.makeMark(marks, index, to, n - index, true);
                  }
               } else if ((172 > op || op > 177) && op != 191) {
                  if (op == 200) {
                     this.makeGoto(marks, index, index + ci.s32bitAt(index + 1), 5);
                  } else if (op == 201) {
                     this.makeJsr(marks, index, index + ci.s32bitAt(index + 1), 5);
                  } else if (op == 196 && ci.byteAt(index + 1) == 169) {
                     this.makeMark(marks, index, (BasicBlock[])null, 4, true);
                  }
               } else {
                  this.makeMark(marks, index, (BasicBlock[])null, 1, true);
               }
            } else {
               BasicBlock.Mark to = this.makeMark(marks, index + ci.s16bitAt(index + 1));
               BasicBlock.Mark next = this.makeMark(marks, index + 3);
               this.makeMark(marks, index, this.makeArray(to.block, next.block), 3, false);
            }
         }

         if (et != null) {
            index = et.size();

            while(true) {
               --index;
               if (index < 0) {
                  break;
               }

               this.makeMark0(marks, et.startPc(index), true, false);
               this.makeMark(marks, et.handlerPc(index));
            }
         }

         return marks;
      }

      private void makeGoto(HashMap marks, int pos, int target, int size) {
         BasicBlock.Mark to = this.makeMark(marks, target);
         BasicBlock[] jumps = this.makeArray(to.block);
         this.makeMark(marks, pos, jumps, size, true);
      }

      protected void makeJsr(HashMap marks, int pos, int target, int size) throws BadBytecode {
         throw new BasicBlock.JsrBytecode();
      }

      private BasicBlock[] makeBlocks(HashMap markTable) {
         BasicBlock.Mark[] marks = (BasicBlock.Mark[])((BasicBlock.Mark[])markTable.values().toArray(new BasicBlock.Mark[markTable.size()]));
         Arrays.sort(marks);
         ArrayList blocks = new ArrayList();
         int i = 0;
         BasicBlock prev;
         if (marks.length > 0 && marks[0].position == 0 && marks[0].block != null) {
            prev = getBBlock(marks[i++]);
         } else {
            prev = this.makeBlock(0);
         }

         blocks.add(prev);

         while(i < marks.length) {
            BasicBlock.Mark m = marks[i++];
            BasicBlock bb = getBBlock(m);
            if (bb == null) {
               if (prev.length > 0) {
                  prev = this.makeBlock(prev.position + prev.length);
                  blocks.add(prev);
               }

               prev.length = m.position + m.size - prev.position;
               prev.exit = m.jump;
               prev.stop = m.alwaysJmp;
            } else {
               if (prev.length == 0) {
                  prev.length = m.position - prev.position;
                  ++bb.incoming;
                  prev.exit = this.makeArray(bb);
               } else if (prev.position + prev.length < m.position) {
                  prev = this.makeBlock(prev.position + prev.length);
                  blocks.add(prev);
                  prev.length = m.position - prev.position;
                  prev.exit = this.makeArray(bb);
               }

               blocks.add(bb);
               prev = bb;
            }
         }

         return (BasicBlock[])((BasicBlock[])blocks.toArray(this.makeArray(blocks.size())));
      }

      private static BasicBlock getBBlock(BasicBlock.Mark m) {
         BasicBlock b = m.block;
         if (b != null && m.size > 0) {
            b.exit = m.jump;
            b.length = m.size;
            b.stop = m.alwaysJmp;
         }

         return b;
      }

      private void addCatchers(BasicBlock[] blocks, ExceptionTable et) throws BadBytecode {
         if (et != null) {
            int i = et.size();

            while(true) {
               --i;
               if (i < 0) {
                  return;
               }

               BasicBlock handler = BasicBlock.find(blocks, et.handlerPc(i));
               int start = et.startPc(i);
               int end = et.endPc(i);
               int type = et.catchType(i);
               --handler.incoming;

               for(int k = 0; k < blocks.length; ++k) {
                  BasicBlock bb = blocks[k];
                  int iPos = bb.position;
                  if (start <= iPos && iPos < end) {
                     bb.toCatch = new BasicBlock.Catch(handler, type, bb.toCatch);
                     ++handler.incoming;
                  }
               }
            }
         }
      }
   }

   static class Mark implements Comparable {
      int position;
      BasicBlock block;
      BasicBlock[] jump;
      boolean alwaysJmp;
      int size;
      BasicBlock.Catch catcher;

      Mark(int p) {
         this.position = p;
         this.block = null;
         this.jump = null;
         this.alwaysJmp = false;
         this.size = 0;
         this.catcher = null;
      }

      public int compareTo(Object obj) {
         if (obj instanceof BasicBlock.Mark) {
            int pos = ((BasicBlock.Mark)obj).position;
            return this.position - pos;
         } else {
            return -1;
         }
      }

      void setJump(BasicBlock[] bb, int s, boolean always) {
         this.jump = bb;
         this.size = s;
         this.alwaysJmp = always;
      }
   }

   public static class Catch {
      public BasicBlock.Catch next;
      public BasicBlock body;
      public int typeIndex;

      Catch(BasicBlock b, int i, BasicBlock.Catch c) {
         this.body = b;
         this.typeIndex = i;
         this.next = c;
      }
   }

   static class JsrBytecode extends BadBytecode {
      JsrBytecode() {
         super("JSR");
      }
   }
}
