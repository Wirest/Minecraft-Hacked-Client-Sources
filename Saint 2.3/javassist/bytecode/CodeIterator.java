package javassist.bytecode;

import java.util.ArrayList;

public class CodeIterator implements Opcode {
   protected CodeAttribute codeAttr;
   protected byte[] bytecode;
   protected int endPos;
   protected int currentPos;
   protected int mark;
   private static final int[] opcodeLength = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5};

   protected CodeIterator(CodeAttribute ca) {
      this.codeAttr = ca;
      this.bytecode = ca.getCode();
      this.begin();
   }

   public void begin() {
      this.currentPos = this.mark = 0;
      this.endPos = this.getCodeLength();
   }

   public void move(int index) {
      this.currentPos = index;
   }

   public void setMark(int index) {
      this.mark = index;
   }

   public int getMark() {
      return this.mark;
   }

   public CodeAttribute get() {
      return this.codeAttr;
   }

   public int getCodeLength() {
      return this.bytecode.length;
   }

   public int byteAt(int index) {
      return this.bytecode[index] & 255;
   }

   public int signedByteAt(int index) {
      return this.bytecode[index];
   }

   public void writeByte(int value, int index) {
      this.bytecode[index] = (byte)value;
   }

   public int u16bitAt(int index) {
      return ByteArray.readU16bit(this.bytecode, index);
   }

   public int s16bitAt(int index) {
      return ByteArray.readS16bit(this.bytecode, index);
   }

   public void write16bit(int value, int index) {
      ByteArray.write16bit(value, this.bytecode, index);
   }

   public int s32bitAt(int index) {
      return ByteArray.read32bit(this.bytecode, index);
   }

   public void write32bit(int value, int index) {
      ByteArray.write32bit(value, this.bytecode, index);
   }

   public void write(byte[] code, int index) {
      int len = code.length;

      for(int j = 0; j < len; ++j) {
         this.bytecode[index++] = code[j];
      }

   }

   public boolean hasNext() {
      return this.currentPos < this.endPos;
   }

   public int next() throws BadBytecode {
      int pos = this.currentPos;
      this.currentPos = nextOpcode(this.bytecode, pos);
      return pos;
   }

   public int lookAhead() {
      return this.currentPos;
   }

   public int skipConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(-1);
   }

   public int skipSuperConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(0);
   }

   public int skipThisConstructor() throws BadBytecode {
      return this.skipSuperConstructor0(1);
   }

   private int skipSuperConstructor0(int skipThis) throws BadBytecode {
      this.begin();
      ConstPool cp = this.codeAttr.getConstPool();
      String thisClassName = this.codeAttr.getDeclaringClass();
      int nested = 0;

      while(this.hasNext()) {
         int index = this.next();
         int c = this.byteAt(index);
         if (c == 187) {
            ++nested;
         } else if (c == 183) {
            int mref = ByteArray.readU16bit(this.bytecode, index + 1);
            if (cp.getMethodrefName(mref).equals("<init>")) {
               --nested;
               if (nested < 0) {
                  if (skipThis < 0) {
                     return index;
                  }

                  String cname = cp.getMethodrefClassName(mref);
                  if (cname.equals(thisClassName) == skipThis > 0) {
                     return index;
                  }
                  break;
               }
            }
         }
      }

      this.begin();
      return -1;
   }

   public int insert(byte[] code) throws BadBytecode {
      return this.insert0(this.currentPos, code, false);
   }

   public void insert(int pos, byte[] code) throws BadBytecode {
      this.insert0(pos, code, false);
   }

   public int insertAt(int pos, byte[] code) throws BadBytecode {
      return this.insert0(pos, code, false);
   }

   public int insertEx(byte[] code) throws BadBytecode {
      return this.insert0(this.currentPos, code, true);
   }

   public void insertEx(int pos, byte[] code) throws BadBytecode {
      this.insert0(pos, code, true);
   }

   public int insertExAt(int pos, byte[] code) throws BadBytecode {
      return this.insert0(pos, code, true);
   }

   private int insert0(int pos, byte[] code, boolean exclusive) throws BadBytecode {
      int len = code.length;
      if (len <= 0) {
         return pos;
      } else {
         pos = this.insertGapAt(pos, len, exclusive).position;
         int p = pos;

         for(int j = 0; j < len; ++j) {
            this.bytecode[p++] = code[j];
         }

         return pos;
      }
   }

   public int insertGap(int length) throws BadBytecode {
      return this.insertGapAt(this.currentPos, length, false).position;
   }

   public int insertGap(int pos, int length) throws BadBytecode {
      return this.insertGapAt(pos, length, false).length;
   }

   public int insertExGap(int length) throws BadBytecode {
      return this.insertGapAt(this.currentPos, length, true).position;
   }

   public int insertExGap(int pos, int length) throws BadBytecode {
      return this.insertGapAt(pos, length, true).length;
   }

   public CodeIterator.Gap insertGapAt(int pos, int length, boolean exclusive) throws BadBytecode {
      CodeIterator.Gap gap = new CodeIterator.Gap();
      if (length <= 0) {
         gap.position = pos;
         gap.length = 0;
         return gap;
      } else {
         byte[] c;
         int length2;
         if (this.bytecode.length + length > 32767) {
            c = this.insertGapCore0w(this.bytecode, pos, length, exclusive, this.get().getExceptionTable(), this.codeAttr, gap);
            pos = gap.position;
            length2 = length;
         } else {
            int cur = this.currentPos;
            c = insertGapCore0(this.bytecode, pos, length, exclusive, this.get().getExceptionTable(), this.codeAttr);
            length2 = c.length - this.bytecode.length;
            gap.position = pos;
            gap.length = length2;
            if (cur >= pos) {
               this.currentPos = cur + length2;
            }

            if (this.mark > pos || this.mark == pos && exclusive) {
               this.mark += length2;
            }
         }

         this.codeAttr.setCode(c);
         this.bytecode = c;
         this.endPos = this.getCodeLength();
         this.updateCursors(pos, length2);
         return gap;
      }
   }

   protected void updateCursors(int pos, int length) {
   }

   public void insert(ExceptionTable et, int offset) {
      this.codeAttr.getExceptionTable().add(0, et, offset);
   }

   public int append(byte[] code) {
      int size = this.getCodeLength();
      int len = code.length;
      if (len <= 0) {
         return size;
      } else {
         this.appendGap(len);
         byte[] dest = this.bytecode;

         for(int i = 0; i < len; ++i) {
            dest[i + size] = code[i];
         }

         return size;
      }
   }

   public void appendGap(int gapLength) {
      byte[] code = this.bytecode;
      int codeLength = code.length;
      byte[] newcode = new byte[codeLength + gapLength];

      int i;
      for(i = 0; i < codeLength; ++i) {
         newcode[i] = code[i];
      }

      for(i = codeLength; i < codeLength + gapLength; ++i) {
         newcode[i] = 0;
      }

      this.codeAttr.setCode(newcode);
      this.bytecode = newcode;
      this.endPos = this.getCodeLength();
   }

   public void append(ExceptionTable et, int offset) {
      ExceptionTable table = this.codeAttr.getExceptionTable();
      table.add(table.size(), et, offset);
   }

   static int nextOpcode(byte[] code, int index) throws BadBytecode {
      int opcode;
      try {
         opcode = code[index] & 255;
      } catch (IndexOutOfBoundsException var8) {
         throw new BadBytecode("invalid opcode address");
      }

      try {
         int len = opcodeLength[opcode];
         if (len > 0) {
            return index + len;
         }

         if (opcode == 196) {
            if (code[index + 1] == -124) {
               return index + 6;
            }

            return index + 4;
         }

         int index2 = (index & -4) + 8;
         int low;
         if (opcode == 171) {
            low = ByteArray.read32bit(code, index2);
            return index2 + low * 8 + 4;
         }

         if (opcode == 170) {
            low = ByteArray.read32bit(code, index2);
            int high = ByteArray.read32bit(code, index2 + 4);
            return index2 + (high - low + 1) * 4 + 8;
         }
      } catch (IndexOutOfBoundsException var7) {
      }

      throw new BadBytecode(opcode);
   }

   static byte[] insertGapCore0(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
      if (gapLength <= 0) {
         return code;
      } else {
         try {
            return insertGapCore1(code, where, gapLength, exclusive, etable, ca);
         } catch (CodeIterator.AlignmentException var9) {
            try {
               return insertGapCore1(code, where, gapLength + 3 & -4, exclusive, etable, ca);
            } catch (CodeIterator.AlignmentException var8) {
               throw new RuntimeException("fatal error?");
            }
         }
      }
   }

   private static byte[] insertGapCore1(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode, CodeIterator.AlignmentException {
      int codeLength = code.length;
      byte[] newcode = new byte[codeLength + gapLength];
      insertGap2(code, where, gapLength, codeLength, newcode, exclusive);
      etable.shiftPc(where, gapLength, exclusive);
      LineNumberAttribute na = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
      if (na != null) {
         na.shiftPc(where, gapLength, exclusive);
      }

      LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
      if (va != null) {
         va.shiftPc(where, gapLength, exclusive);
      }

      LocalVariableAttribute vta = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
      if (vta != null) {
         vta.shiftPc(where, gapLength, exclusive);
      }

      StackMapTable smt = (StackMapTable)ca.getAttribute("StackMapTable");
      if (smt != null) {
         smt.shiftPc(where, gapLength, exclusive);
      }

      StackMap sm = (StackMap)ca.getAttribute("StackMap");
      if (sm != null) {
         sm.shiftPc(where, gapLength, exclusive);
      }

      return newcode;
   }

   private static void insertGap2(byte[] code, int where, int gapLength, int endPos, byte[] newcode, boolean exclusive) throws BadBytecode, CodeIterator.AlignmentException {
      int i = 0;

      int nextPos;
      for(int j = 0; i < endPos; i = nextPos) {
         int inst;
         if (i == where) {
            for(inst = j + gapLength; j < inst; newcode[j++] = 0) {
            }
         }

         nextPos = nextOpcode(code, i);
         inst = code[i] & 255;
         int i2;
         if ((153 > inst || inst > 168) && inst != 198 && inst != 199) {
            if (inst != 200 && inst != 201) {
               int defaultbyte;
               int npairs;
               int i0;
               int offset;
               if (inst == 170) {
                  if (i != j && (gapLength & 3) != 0) {
                     throw new CodeIterator.AlignmentException();
                  }

                  i2 = (i & -4) + 4;
                  j = copyGapBytes(newcode, j, code, i, i2);
                  defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
                  ByteArray.write32bit(defaultbyte, newcode, j);
                  npairs = ByteArray.read32bit(code, i2 + 4);
                  ByteArray.write32bit(npairs, newcode, j + 4);
                  i0 = ByteArray.read32bit(code, i2 + 8);
                  ByteArray.write32bit(i0, newcode, j + 8);
                  j += 12;
                  offset = i2 + 12;

                  for(i2 = offset + (i0 - npairs + 1) * 4; offset < i2; offset += 4) {
                     int offset = newOffset(i, ByteArray.read32bit(code, offset), where, gapLength, exclusive);
                     ByteArray.write32bit(offset, newcode, j);
                     j += 4;
                  }
               } else if (inst == 171) {
                  if (i != j && (gapLength & 3) != 0) {
                     throw new CodeIterator.AlignmentException();
                  }

                  i2 = (i & -4) + 4;
                  j = copyGapBytes(newcode, j, code, i, i2);
                  defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
                  ByteArray.write32bit(defaultbyte, newcode, j);
                  npairs = ByteArray.read32bit(code, i2 + 4);
                  ByteArray.write32bit(npairs, newcode, j + 4);
                  j += 8;
                  i0 = i2 + 8;

                  for(i2 = i0 + npairs * 8; i0 < i2; i0 += 8) {
                     ByteArray.copy32bit(code, i0, newcode, j);
                     offset = newOffset(i, ByteArray.read32bit(code, i0 + 4), where, gapLength, exclusive);
                     ByteArray.write32bit(offset, newcode, j + 4);
                     j += 8;
                  }
               } else {
                  while(i < nextPos) {
                     newcode[j++] = code[i++];
                  }
               }
            } else {
               i2 = ByteArray.read32bit(code, i + 1);
               i2 = newOffset(i, i2, where, gapLength, exclusive);
               newcode[j++] = code[i];
               ByteArray.write32bit(i2, newcode, j);
               j += 4;
            }
         } else {
            i2 = code[i + 1] << 8 | code[i + 2] & 255;
            i2 = newOffset(i, i2, where, gapLength, exclusive);
            newcode[j] = code[i];
            ByteArray.write16bit(i2, newcode, j + 1);
            j += 3;
         }
      }

   }

   private static int copyGapBytes(byte[] newcode, int j, byte[] code, int i, int iEnd) {
      switch(iEnd - i) {
      case 4:
         newcode[j++] = code[i++];
      case 3:
         newcode[j++] = code[i++];
      case 2:
         newcode[j++] = code[i++];
      case 1:
         newcode[j++] = code[i++];
      default:
         return j;
      }
   }

   private static int newOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
      int target = i + offset;
      if (i < where) {
         if (where < target || exclusive && where == target) {
            offset += gapLength;
         }
      } else if (i == where) {
         if (target < where) {
            offset -= gapLength;
         }
      } else if (target < where || !exclusive && where == target) {
         offset -= gapLength;
      }

      return offset;
   }

   static byte[] changeLdcToLdcW(byte[] code, ExceptionTable etable, CodeAttribute ca, CodeAttribute.LdcEntry ldcs) throws BadBytecode {
      CodeIterator.Pointers pointers = new CodeIterator.Pointers(0, 0, 0, etable, ca);

      ArrayList jumps;
      for(jumps = makeJumpList(code, code.length, pointers); ldcs != null; ldcs = ldcs.next) {
         addLdcW(ldcs, jumps);
      }

      byte[] r = insertGap2w(code, 0, 0, false, jumps, pointers);
      return r;
   }

   private static void addLdcW(CodeAttribute.LdcEntry ldcs, ArrayList jumps) {
      int where = ldcs.where;
      CodeIterator.LdcW ldcw = new CodeIterator.LdcW(where, ldcs.index);
      int s = jumps.size();

      for(int i = 0; i < s; ++i) {
         if (where < ((CodeIterator.Branch)jumps.get(i)).orgPos) {
            jumps.add(i, ldcw);
            return;
         }
      }

      jumps.add(ldcw);
   }

   private byte[] insertGapCore0w(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca, CodeIterator.Gap newWhere) throws BadBytecode {
      if (gapLength <= 0) {
         return code;
      } else {
         CodeIterator.Pointers pointers = new CodeIterator.Pointers(this.currentPos, this.mark, where, etable, ca);
         ArrayList jumps = makeJumpList(code, code.length, pointers);
         byte[] r = insertGap2w(code, where, gapLength, exclusive, jumps, pointers);
         this.currentPos = pointers.cursor;
         this.mark = pointers.mark;
         int where2 = pointers.mark0;
         if (where2 == this.currentPos && !exclusive) {
            this.currentPos += gapLength;
         }

         if (exclusive) {
            where2 -= gapLength;
         }

         newWhere.position = where2;
         newWhere.length = gapLength;
         return r;
      }
   }

   private static byte[] insertGap2w(byte[] code, int where, int gapLength, boolean exclusive, ArrayList jumps, CodeIterator.Pointers ptrs) throws BadBytecode {
      int n = jumps.size();
      if (gapLength > 0) {
         ptrs.shiftPc(where, gapLength, exclusive);

         for(int i = 0; i < n; ++i) {
            ((CodeIterator.Branch)jumps.get(i)).shift(where, gapLength, exclusive);
         }
      }

      boolean unstable = true;

      while(true) {
         int i;
         CodeIterator.Branch b;
         int p;
         int delta;
         int j;
         while(!unstable) {
            for(i = 0; i < n; ++i) {
               b = (CodeIterator.Branch)jumps.get(i);
               p = b.gapChanged();
               if (p > 0) {
                  unstable = true;
                  delta = b.pos;
                  ptrs.shiftPc(delta, p, false);

                  for(j = 0; j < n; ++j) {
                     ((CodeIterator.Branch)jumps.get(j)).shift(delta, p, false);
                  }
               }
            }

            if (!unstable) {
               return makeExapndedCode(code, jumps, where, gapLength);
            }
         }

         unstable = false;

         for(i = 0; i < n; ++i) {
            b = (CodeIterator.Branch)jumps.get(i);
            if (b.expanded()) {
               unstable = true;
               p = b.pos;
               delta = b.deltaSize();
               ptrs.shiftPc(p, delta, false);

               for(j = 0; j < n; ++j) {
                  ((CodeIterator.Branch)jumps.get(j)).shift(p, delta, false);
               }
            }
         }
      }
   }

   private static ArrayList makeJumpList(byte[] code, int endPos, CodeIterator.Pointers ptrs) throws BadBytecode {
      ArrayList jumps = new ArrayList();

      int nextPos;
      for(int i = 0; i < endPos; i = nextPos) {
         nextPos = nextOpcode(code, i);
         int inst = code[i] & 255;
         int i2;
         if ((153 > inst || inst > 168) && inst != 198 && inst != 199) {
            if (inst != 200 && inst != 201) {
               int npairs;
               int i0;
               int defaultbyte;
               if (inst == 170) {
                  i2 = (i & -4) + 4;
                  defaultbyte = ByteArray.read32bit(code, i2);
                  npairs = ByteArray.read32bit(code, i2 + 4);
                  i0 = ByteArray.read32bit(code, i2 + 8);
                  int i0 = i2 + 12;
                  int size = i0 - npairs + 1;
                  int[] offsets = new int[size];

                  for(int j = 0; j < size; ++j) {
                     offsets[j] = ByteArray.read32bit(code, i0);
                     i0 += 4;
                  }

                  jumps.add(new CodeIterator.Table(i, defaultbyte, npairs, i0, offsets, ptrs));
               } else if (inst == 171) {
                  i2 = (i & -4) + 4;
                  defaultbyte = ByteArray.read32bit(code, i2);
                  npairs = ByteArray.read32bit(code, i2 + 4);
                  i0 = i2 + 8;
                  int[] matches = new int[npairs];
                  int[] offsets = new int[npairs];

                  for(int j = 0; j < npairs; ++j) {
                     matches[j] = ByteArray.read32bit(code, i0);
                     offsets[j] = ByteArray.read32bit(code, i0 + 4);
                     i0 += 8;
                  }

                  jumps.add(new CodeIterator.Lookup(i, defaultbyte, matches, offsets, ptrs));
               }
            } else {
               i2 = ByteArray.read32bit(code, i + 1);
               jumps.add(new CodeIterator.Jump32(i, i2));
            }
         } else {
            i2 = code[i + 1] << 8 | code[i + 2] & 255;
            Object b;
            if (inst != 167 && inst != 168) {
               b = new CodeIterator.If16(i, i2);
            } else {
               b = new CodeIterator.Jump16(i, i2);
            }

            jumps.add(b);
         }
      }

      return jumps;
   }

   private static byte[] makeExapndedCode(byte[] code, ArrayList jumps, int where, int gapLength) throws BadBytecode {
      int n = jumps.size();
      int size = code.length + gapLength;

      for(int i = 0; i < n; ++i) {
         CodeIterator.Branch b = (CodeIterator.Branch)jumps.get(i);
         size += b.deltaSize();
      }

      byte[] newcode = new byte[size];
      int src = 0;
      int dest = 0;
      int bindex = 0;
      int len = code.length;
      CodeIterator.Branch b;
      int bpos;
      if (0 < n) {
         b = (CodeIterator.Branch)jumps.get(0);
         bpos = b.orgPos;
      } else {
         b = null;
         bpos = len;
      }

      while(src < len) {
         int s;
         if (src == where) {
            for(s = dest + gapLength; dest < s; newcode[dest++] = 0) {
            }
         }

         if (src != bpos) {
            newcode[dest++] = code[src++];
         } else {
            s = b.write(src, code, dest, newcode);
            src += s;
            dest += s + b.deltaSize();
            ++bindex;
            if (bindex < n) {
               b = (CodeIterator.Branch)jumps.get(bindex);
               bpos = b.orgPos;
            } else {
               b = null;
               bpos = len;
            }
         }
      }

      return newcode;
   }

   static class Lookup extends CodeIterator.Switcher {
      int[] matches;

      Lookup(int pos, int defaultByte, int[] matches, int[] offsets, CodeIterator.Pointers ptrs) {
         super(pos, defaultByte, offsets, ptrs);
         this.matches = matches;
      }

      int write2(int dest, byte[] newcode) {
         int n = this.matches.length;
         ByteArray.write32bit(n, newcode, dest);
         dest += 4;

         for(int i = 0; i < n; ++i) {
            ByteArray.write32bit(this.matches[i], newcode, dest);
            ByteArray.write32bit(this.offsets[i], newcode, dest + 4);
            dest += 8;
         }

         return 4 + 8 * n;
      }

      int tableSize() {
         return 4 + 8 * this.matches.length;
      }
   }

   static class Table extends CodeIterator.Switcher {
      int low;
      int high;

      Table(int pos, int defaultByte, int low, int high, int[] offsets, CodeIterator.Pointers ptrs) {
         super(pos, defaultByte, offsets, ptrs);
         this.low = low;
         this.high = high;
      }

      int write2(int dest, byte[] newcode) {
         ByteArray.write32bit(this.low, newcode, dest);
         ByteArray.write32bit(this.high, newcode, dest + 4);
         int n = this.offsets.length;
         dest += 8;

         for(int i = 0; i < n; ++i) {
            ByteArray.write32bit(this.offsets[i], newcode, dest);
            dest += 4;
         }

         return 8 + 4 * n;
      }

      int tableSize() {
         return 8 + 4 * this.offsets.length;
      }
   }

   abstract static class Switcher extends CodeIterator.Branch {
      int gap;
      int defaultByte;
      int[] offsets;
      CodeIterator.Pointers pointers;

      Switcher(int pos, int defaultByte, int[] offsets, CodeIterator.Pointers ptrs) {
         super(pos);
         this.gap = 3 - (pos & 3);
         this.defaultByte = defaultByte;
         this.offsets = offsets;
         this.pointers = ptrs;
      }

      void shift(int where, int gapLength, boolean exclusive) {
         int p = this.pos;
         this.defaultByte = shiftOffset(p, this.defaultByte, where, gapLength, exclusive);
         int num = this.offsets.length;

         for(int i = 0; i < num; ++i) {
            this.offsets[i] = shiftOffset(p, this.offsets[i], where, gapLength, exclusive);
         }

         super.shift(where, gapLength, exclusive);
      }

      int gapChanged() {
         int newGap = 3 - (this.pos & 3);
         if (newGap > this.gap) {
            int diff = newGap - this.gap;
            this.gap = newGap;
            return diff;
         } else {
            return 0;
         }
      }

      int deltaSize() {
         return this.gap - (3 - (this.orgPos & 3));
      }

      int write(int src, byte[] code, int dest, byte[] newcode) throws BadBytecode {
         int padding = 3 - (this.pos & 3);
         int nops = this.gap - padding;
         int bytecodeSize = 5 + (3 - (this.orgPos & 3)) + this.tableSize();
         if (nops > 0) {
            this.adjustOffsets(bytecodeSize, nops);
         }

         for(newcode[dest++] = code[src]; padding-- > 0; newcode[dest++] = 0) {
         }

         ByteArray.write32bit(this.defaultByte, newcode, dest);
         int size = this.write2(dest + 4, newcode);

         for(dest += size + 4; nops-- > 0; newcode[dest++] = 0) {
         }

         return 5 + (3 - (this.orgPos & 3)) + size;
      }

      abstract int write2(int dest, byte[] newcode);

      abstract int tableSize();

      void adjustOffsets(int size, int nops) throws BadBytecode {
         this.pointers.shiftForSwitch(this.pos + size, nops);
         if (this.defaultByte == size) {
            this.defaultByte -= nops;
         }

         for(int i = 0; i < this.offsets.length; ++i) {
            if (this.offsets[i] == size) {
               int[] var10000 = this.offsets;
               var10000[i] -= nops;
            }
         }

      }
   }

   static class Jump32 extends CodeIterator.Branch {
      int offset;

      Jump32(int p, int off) {
         super(p);
         this.offset = off;
      }

      void shift(int where, int gapLength, boolean exclusive) {
         this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
         super.shift(where, gapLength, exclusive);
      }

      int write(int src, byte[] code, int dest, byte[] newcode) {
         newcode[dest] = code[src];
         ByteArray.write32bit(this.offset, newcode, dest + 1);
         return 5;
      }
   }

   static class If16 extends CodeIterator.Branch16 {
      If16(int p, int off) {
         super(p, off);
      }

      int deltaSize() {
         return this.state == 2 ? 5 : 0;
      }

      void write32(int src, byte[] code, int dest, byte[] newcode) {
         newcode[dest] = (byte)this.opcode(code[src] & 255);
         newcode[dest + 1] = 0;
         newcode[dest + 2] = 8;
         newcode[dest + 3] = -56;
         ByteArray.write32bit(this.offset - 3, newcode, dest + 4);
      }

      int opcode(int op) {
         if (op == 198) {
            return 199;
         } else if (op == 199) {
            return 198;
         } else {
            return (op - 153 & 1) == 0 ? op + 1 : op - 1;
         }
      }
   }

   static class Jump16 extends CodeIterator.Branch16 {
      Jump16(int p, int off) {
         super(p, off);
      }

      int deltaSize() {
         return this.state == 2 ? 2 : 0;
      }

      void write32(int src, byte[] code, int dest, byte[] newcode) {
         newcode[dest] = (byte)((code[src] & 255) == 167 ? 200 : 201);
         ByteArray.write32bit(this.offset, newcode, dest + 1);
      }
   }

   abstract static class Branch16 extends CodeIterator.Branch {
      int offset;
      int state;
      static final int BIT16 = 0;
      static final int EXPAND = 1;
      static final int BIT32 = 2;

      Branch16(int p, int off) {
         super(p);
         this.offset = off;
         this.state = 0;
      }

      void shift(int where, int gapLength, boolean exclusive) {
         this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
         super.shift(where, gapLength, exclusive);
         if (this.state == 0 && (this.offset < -32768 || 32767 < this.offset)) {
            this.state = 1;
         }

      }

      boolean expanded() {
         if (this.state == 1) {
            this.state = 2;
            return true;
         } else {
            return false;
         }
      }

      abstract int deltaSize();

      abstract void write32(int src, byte[] code, int dest, byte[] newcode);

      int write(int src, byte[] code, int dest, byte[] newcode) {
         if (this.state == 2) {
            this.write32(src, code, dest, newcode);
         } else {
            newcode[dest] = code[src];
            ByteArray.write16bit(this.offset, newcode, dest + 1);
         }

         return 3;
      }
   }

   static class LdcW extends CodeIterator.Branch {
      int index;
      boolean state;

      LdcW(int p, int i) {
         super(p);
         this.index = i;
         this.state = true;
      }

      boolean expanded() {
         if (this.state) {
            this.state = false;
            return true;
         } else {
            return false;
         }
      }

      int deltaSize() {
         return 1;
      }

      int write(int srcPos, byte[] code, int destPos, byte[] newcode) {
         newcode[destPos] = 19;
         ByteArray.write16bit(this.index, newcode, destPos + 1);
         return 2;
      }
   }

   abstract static class Branch {
      int pos;
      int orgPos;

      Branch(int p) {
         this.pos = this.orgPos = p;
      }

      void shift(int where, int gapLength, boolean exclusive) {
         if (where < this.pos || where == this.pos && exclusive) {
            this.pos += gapLength;
         }

      }

      static int shiftOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
         int target = i + offset;
         if (i < where) {
            if (where < target || exclusive && where == target) {
               offset += gapLength;
            }
         } else if (i == where) {
            if (target < where && exclusive) {
               offset -= gapLength;
            } else if (where < target && !exclusive) {
               offset += gapLength;
            }
         } else if (target < where || !exclusive && where == target) {
            offset -= gapLength;
         }

         return offset;
      }

      boolean expanded() {
         return false;
      }

      int gapChanged() {
         return 0;
      }

      int deltaSize() {
         return 0;
      }

      abstract int write(int srcPos, byte[] code, int destPos, byte[] newcode) throws BadBytecode;
   }

   static class Pointers {
      int cursor;
      int mark0;
      int mark;
      ExceptionTable etable;
      LineNumberAttribute line;
      LocalVariableAttribute vars;
      LocalVariableAttribute types;
      StackMapTable stack;
      StackMap stack2;

      Pointers(int cur, int m, int m0, ExceptionTable et, CodeAttribute ca) {
         this.cursor = cur;
         this.mark = m;
         this.mark0 = m0;
         this.etable = et;
         this.line = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
         this.vars = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
         this.types = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
         this.stack = (StackMapTable)ca.getAttribute("StackMapTable");
         this.stack2 = (StackMap)ca.getAttribute("StackMap");
      }

      void shiftPc(int where, int gapLength, boolean exclusive) throws BadBytecode {
         if (where < this.cursor || where == this.cursor && exclusive) {
            this.cursor += gapLength;
         }

         if (where < this.mark || where == this.mark && exclusive) {
            this.mark += gapLength;
         }

         if (where < this.mark0 || where == this.mark0 && exclusive) {
            this.mark0 += gapLength;
         }

         this.etable.shiftPc(where, gapLength, exclusive);
         if (this.line != null) {
            this.line.shiftPc(where, gapLength, exclusive);
         }

         if (this.vars != null) {
            this.vars.shiftPc(where, gapLength, exclusive);
         }

         if (this.types != null) {
            this.types.shiftPc(where, gapLength, exclusive);
         }

         if (this.stack != null) {
            this.stack.shiftPc(where, gapLength, exclusive);
         }

         if (this.stack2 != null) {
            this.stack2.shiftPc(where, gapLength, exclusive);
         }

      }

      void shiftForSwitch(int where, int gapLength) throws BadBytecode {
         if (this.stack != null) {
            this.stack.shiftForSwitch(where, gapLength);
         }

         if (this.stack2 != null) {
            this.stack2.shiftForSwitch(where, gapLength);
         }

      }
   }

   static class AlignmentException extends Exception {
   }

   public static class Gap {
      public int position;
      public int length;
   }
}
