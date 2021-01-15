package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import javassist.CannotCompileException;

public class StackMapTable extends AttributeInfo {
   public static final String tag = "StackMapTable";
   public static final int TOP = 0;
   public static final int INTEGER = 1;
   public static final int FLOAT = 2;
   public static final int DOUBLE = 3;
   public static final int LONG = 4;
   public static final int NULL = 5;
   public static final int THIS = 6;
   public static final int OBJECT = 7;
   public static final int UNINIT = 8;

   StackMapTable(ConstPool cp, byte[] newInfo) {
      super(cp, "StackMapTable", newInfo);
   }

   StackMapTable(ConstPool cp, int name_id, DataInputStream in) throws IOException {
      super(cp, name_id, in);
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) throws StackMapTable.RuntimeCopyException {
      try {
         return new StackMapTable(newCp, (new StackMapTable.Copier(this.constPool, this.info, newCp, classnames)).doit());
      } catch (BadBytecode var4) {
         throw new StackMapTable.RuntimeCopyException("bad bytecode. fatal?");
      }
   }

   void write(DataOutputStream out) throws IOException {
      super.write(out);
   }

   public void insertLocal(int index, int tag, int classInfo) throws BadBytecode {
      byte[] data = (new StackMapTable.InsertLocal(this.get(), index, tag, classInfo)).doit();
      this.set(data);
   }

   public static int typeTagOf(char descriptor) {
      switch(descriptor) {
      case 'D':
         return 3;
      case 'F':
         return 2;
      case 'J':
         return 4;
      case 'L':
      case '[':
         return 7;
      default:
         return 1;
      }
   }

   public void println(PrintWriter w) {
      StackMapTable.Printer.print(this, w);
   }

   public void println(PrintStream ps) {
      StackMapTable.Printer.print(this, new PrintWriter(ps, true));
   }

   void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
      (new StackMapTable.OffsetShifter(this, where, gapSize)).parse();
      (new StackMapTable.Shifter(this, where, gapSize, exclusive)).doit();
   }

   void shiftForSwitch(int where, int gapSize) throws BadBytecode {
      (new StackMapTable.SwitchShifter(this, where, gapSize)).doit();
   }

   public void removeNew(int where) throws CannotCompileException {
      try {
         byte[] data = (new StackMapTable.NewRemover(this.get(), where)).doit();
         this.set(data);
      } catch (BadBytecode var3) {
         throw new CannotCompileException("bad stack map table", var3);
      }
   }

   static class NewRemover extends StackMapTable.SimpleCopy {
      int posOfNew;

      public NewRemover(byte[] data, int pos) {
         super(data);
         this.posOfNew = pos;
      }

      public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
         if (stackTag == 8 && stackData == this.posOfNew) {
            super.sameFrame(pos, offsetDelta);
         } else {
            super.sameLocals(pos, offsetDelta, stackTag, stackData);
         }

      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         int n = stackTags.length - 1;

         for(int i = 0; i < n; ++i) {
            if (stackTags[i] == 8 && stackData[i] == this.posOfNew && stackTags[i + 1] == 8 && stackData[i + 1] == this.posOfNew) {
               ++n;
               int[] stackTags2 = new int[n - 2];
               int[] stackData2 = new int[n - 2];
               int k = 0;

               for(int j = 0; j < n; ++j) {
                  if (j == i) {
                     ++j;
                  } else {
                     stackTags2[k] = stackTags[j];
                     stackData2[k++] = stackData[j];
                  }
               }

               stackTags = stackTags2;
               stackData = stackData2;
               break;
            }
         }

         super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
      }
   }

   static class SwitchShifter extends StackMapTable.Shifter {
      SwitchShifter(StackMapTable smt, int where, int gap) {
         super(smt, where, gap, false);
      }

      void update(int pos, int offsetDelta, int base, int entry) {
         int oldPos = this.position;
         this.position = oldPos + offsetDelta + (oldPos == 0 ? 0 : 1);
         int newDelta;
         if (this.where == this.position) {
            newDelta = offsetDelta - this.gap;
         } else {
            if (this.where != oldPos) {
               return;
            }

            newDelta = offsetDelta + this.gap;
         }

         byte[] newinfo;
         if (offsetDelta < 64) {
            if (newDelta < 64) {
               this.info[pos] = (byte)(newDelta + base);
            } else {
               newinfo = insertGap(this.info, pos, 2);
               newinfo[pos] = (byte)entry;
               ByteArray.write16bit(newDelta, newinfo, pos + 1);
               this.updatedInfo = newinfo;
            }
         } else if (newDelta < 64) {
            newinfo = deleteGap(this.info, pos, 2);
            newinfo[pos] = (byte)(newDelta + base);
            this.updatedInfo = newinfo;
         } else {
            ByteArray.write16bit(newDelta, this.info, pos + 1);
         }

      }

      static byte[] deleteGap(byte[] info, int where, int gap) {
         where += gap;
         int len = info.length;
         byte[] newinfo = new byte[len - gap];

         for(int i = 0; i < len; ++i) {
            newinfo[i - (i < where ? 0 : gap)] = info[i];
         }

         return newinfo;
      }

      void update(int pos, int offsetDelta) {
         int oldPos = this.position;
         this.position = oldPos + offsetDelta + (oldPos == 0 ? 0 : 1);
         int newDelta;
         if (this.where == this.position) {
            newDelta = offsetDelta - this.gap;
         } else {
            if (this.where != oldPos) {
               return;
            }

            newDelta = offsetDelta + this.gap;
         }

         ByteArray.write16bit(newDelta, this.info, pos + 1);
      }
   }

   static class Shifter extends StackMapTable.Walker {
      private StackMapTable stackMap;
      int where;
      int gap;
      int position;
      byte[] updatedInfo;
      boolean exclusive;

      public Shifter(StackMapTable smt, int where, int gap, boolean exclusive) {
         super(smt);
         this.stackMap = smt;
         this.where = where;
         this.gap = gap;
         this.position = 0;
         this.updatedInfo = null;
         this.exclusive = exclusive;
      }

      public void doit() throws BadBytecode {
         this.parse();
         if (this.updatedInfo != null) {
            this.stackMap.set(this.updatedInfo);
         }

      }

      public void sameFrame(int pos, int offsetDelta) {
         this.update(pos, offsetDelta, 0, 251);
      }

      public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
         this.update(pos, offsetDelta, 64, 247);
      }

      void update(int pos, int offsetDelta, int base, int entry) {
         int oldPos = this.position;
         this.position = oldPos + offsetDelta + (oldPos == 0 ? 0 : 1);
         boolean match;
         if (this.exclusive) {
            match = oldPos < this.where && this.where <= this.position;
         } else {
            match = oldPos <= this.where && this.where < this.position;
         }

         if (match) {
            int newDelta = offsetDelta + this.gap;
            this.position += this.gap;
            if (newDelta < 64) {
               this.info[pos] = (byte)(newDelta + base);
            } else if (offsetDelta < 64) {
               byte[] newinfo = insertGap(this.info, pos, 2);
               newinfo[pos] = (byte)entry;
               ByteArray.write16bit(newDelta, newinfo, pos + 1);
               this.updatedInfo = newinfo;
            } else {
               ByteArray.write16bit(newDelta, this.info, pos + 1);
            }
         }

      }

      static byte[] insertGap(byte[] info, int where, int gap) {
         int len = info.length;
         byte[] newinfo = new byte[len + gap];

         for(int i = 0; i < len; ++i) {
            newinfo[i + (i < where ? 0 : gap)] = info[i];
         }

         return newinfo;
      }

      public void chopFrame(int pos, int offsetDelta, int k) {
         this.update(pos, offsetDelta);
      }

      public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
         this.update(pos, offsetDelta);
      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         this.update(pos, offsetDelta);
      }

      void update(int pos, int offsetDelta) {
         int oldPos = this.position;
         this.position = oldPos + offsetDelta + (oldPos == 0 ? 0 : 1);
         boolean match;
         if (this.exclusive) {
            match = oldPos < this.where && this.where <= this.position;
         } else {
            match = oldPos <= this.where && this.where < this.position;
         }

         if (match) {
            int newDelta = offsetDelta + this.gap;
            ByteArray.write16bit(newDelta, this.info, pos + 1);
            this.position += this.gap;
         }

      }
   }

   static class OffsetShifter extends StackMapTable.Walker {
      int where;
      int gap;

      public OffsetShifter(StackMapTable smt, int where, int gap) {
         super(smt);
         this.where = where;
         this.gap = gap;
      }

      public void objectOrUninitialized(int tag, int data, int pos) {
         if (tag == 8 && this.where <= data) {
            ByteArray.write16bit(data + this.gap, this.info, pos);
         }

      }
   }

   static class Printer extends StackMapTable.Walker {
      private PrintWriter writer;
      private int offset;

      public static void print(StackMapTable smt, PrintWriter writer) {
         try {
            (new StackMapTable.Printer(smt.get(), writer)).parse();
         } catch (BadBytecode var3) {
            writer.println(var3.getMessage());
         }

      }

      Printer(byte[] data, PrintWriter pw) {
         super(data);
         this.writer = pw;
         this.offset = -1;
      }

      public void sameFrame(int pos, int offsetDelta) {
         this.offset += offsetDelta + 1;
         this.writer.println(this.offset + " same frame: " + offsetDelta);
      }

      public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
         this.offset += offsetDelta + 1;
         this.writer.println(this.offset + " same locals: " + offsetDelta);
         this.printTypeInfo(stackTag, stackData);
      }

      public void chopFrame(int pos, int offsetDelta, int k) {
         this.offset += offsetDelta + 1;
         this.writer.println(this.offset + " chop frame: " + offsetDelta + ",    " + k + " last locals");
      }

      public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
         this.offset += offsetDelta + 1;
         this.writer.println(this.offset + " append frame: " + offsetDelta);

         for(int i = 0; i < tags.length; ++i) {
            this.printTypeInfo(tags[i], data[i]);
         }

      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         this.offset += offsetDelta + 1;
         this.writer.println(this.offset + " full frame: " + offsetDelta);
         this.writer.println("[locals]");

         int i;
         for(i = 0; i < localTags.length; ++i) {
            this.printTypeInfo(localTags[i], localData[i]);
         }

         this.writer.println("[stack]");

         for(i = 0; i < stackTags.length; ++i) {
            this.printTypeInfo(stackTags[i], stackData[i]);
         }

      }

      private void printTypeInfo(int tag, int data) {
         String msg = null;
         switch(tag) {
         case 0:
            msg = "top";
            break;
         case 1:
            msg = "integer";
            break;
         case 2:
            msg = "float";
            break;
         case 3:
            msg = "double";
            break;
         case 4:
            msg = "long";
            break;
         case 5:
            msg = "null";
            break;
         case 6:
            msg = "this";
            break;
         case 7:
            msg = "object (cpool_index " + data + ")";
            break;
         case 8:
            msg = "uninitialized (offset " + data + ")";
         }

         this.writer.print("    ");
         this.writer.println(msg);
      }
   }

   public static class Writer {
      ByteArrayOutputStream output;
      int numOfEntries;

      public Writer(int size) {
         this.output = new ByteArrayOutputStream(size);
         this.numOfEntries = 0;
         this.output.write(0);
         this.output.write(0);
      }

      public byte[] toByteArray() {
         byte[] b = this.output.toByteArray();
         ByteArray.write16bit(this.numOfEntries, b, 0);
         return b;
      }

      public StackMapTable toStackMapTable(ConstPool cp) {
         return new StackMapTable(cp, this.toByteArray());
      }

      public void sameFrame(int offsetDelta) {
         ++this.numOfEntries;
         if (offsetDelta < 64) {
            this.output.write(offsetDelta);
         } else {
            this.output.write(251);
            this.write16(offsetDelta);
         }

      }

      public void sameLocals(int offsetDelta, int tag, int data) {
         ++this.numOfEntries;
         if (offsetDelta < 64) {
            this.output.write(offsetDelta + 64);
         } else {
            this.output.write(247);
            this.write16(offsetDelta);
         }

         this.writeTypeInfo(tag, data);
      }

      public void chopFrame(int offsetDelta, int k) {
         ++this.numOfEntries;
         this.output.write(251 - k);
         this.write16(offsetDelta);
      }

      public void appendFrame(int offsetDelta, int[] tags, int[] data) {
         ++this.numOfEntries;
         int k = tags.length;
         this.output.write(k + 251);
         this.write16(offsetDelta);

         for(int i = 0; i < k; ++i) {
            this.writeTypeInfo(tags[i], data[i]);
         }

      }

      public void fullFrame(int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         ++this.numOfEntries;
         this.output.write(255);
         this.write16(offsetDelta);
         int n = localTags.length;
         this.write16(n);

         int i;
         for(i = 0; i < n; ++i) {
            this.writeTypeInfo(localTags[i], localData[i]);
         }

         n = stackTags.length;
         this.write16(n);

         for(i = 0; i < n; ++i) {
            this.writeTypeInfo(stackTags[i], stackData[i]);
         }

      }

      private void writeTypeInfo(int tag, int data) {
         this.output.write(tag);
         if (tag == 7 || tag == 8) {
            this.write16(data);
         }

      }

      private void write16(int value) {
         this.output.write(value >>> 8 & 255);
         this.output.write(value & 255);
      }
   }

   static class InsertLocal extends StackMapTable.SimpleCopy {
      private int varIndex;
      private int varTag;
      private int varData;

      public InsertLocal(byte[] data, int varIndex, int varTag, int varData) {
         super(data);
         this.varIndex = varIndex;
         this.varTag = varTag;
         this.varData = varData;
      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         int len = localTags.length;
         if (len < this.varIndex) {
            super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
         } else {
            int typeSize = this.varTag != 4 && this.varTag != 3 ? 1 : 2;
            int[] localTags2 = new int[len + typeSize];
            int[] localData2 = new int[len + typeSize];
            int index = this.varIndex;
            int j = 0;

            for(int i = 0; i < len; ++i) {
               if (j == index) {
                  j += typeSize;
               }

               localTags2[j] = localTags[i];
               localData2[j++] = localData[i];
            }

            localTags2[index] = this.varTag;
            localData2[index] = this.varData;
            if (typeSize > 1) {
               localTags2[index + 1] = 0;
               localData2[index + 1] = 0;
            }

            super.fullFrame(pos, offsetDelta, localTags2, localData2, stackTags, stackData);
         }
      }
   }

   static class Copier extends StackMapTable.SimpleCopy {
      private ConstPool srcPool;
      private ConstPool destPool;
      private Map classnames;

      public Copier(ConstPool src, byte[] data, ConstPool dest, Map names) {
         super(data);
         this.srcPool = src;
         this.destPool = dest;
         this.classnames = names;
      }

      protected int copyData(int tag, int data) {
         return tag == 7 ? this.srcPool.copy(data, this.destPool, this.classnames) : data;
      }

      protected int[] copyData(int[] tags, int[] data) {
         int[] newData = new int[data.length];

         for(int i = 0; i < data.length; ++i) {
            if (tags[i] == 7) {
               newData[i] = this.srcPool.copy(data[i], this.destPool, this.classnames);
            } else {
               newData[i] = data[i];
            }
         }

         return newData;
      }
   }

   static class SimpleCopy extends StackMapTable.Walker {
      private StackMapTable.Writer writer;

      public SimpleCopy(byte[] data) {
         super(data);
         this.writer = new StackMapTable.Writer(data.length);
      }

      public byte[] doit() throws BadBytecode {
         this.parse();
         return this.writer.toByteArray();
      }

      public void sameFrame(int pos, int offsetDelta) {
         this.writer.sameFrame(offsetDelta);
      }

      public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
         this.writer.sameLocals(offsetDelta, stackTag, this.copyData(stackTag, stackData));
      }

      public void chopFrame(int pos, int offsetDelta, int k) {
         this.writer.chopFrame(offsetDelta, k);
      }

      public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
         this.writer.appendFrame(offsetDelta, tags, this.copyData(tags, data));
      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
         this.writer.fullFrame(offsetDelta, localTags, this.copyData(localTags, localData), stackTags, this.copyData(stackTags, stackData));
      }

      protected int copyData(int tag, int data) {
         return data;
      }

      protected int[] copyData(int[] tags, int[] data) {
         return data;
      }
   }

   public static class Walker {
      byte[] info;
      int numOfEntries;

      public Walker(StackMapTable smt) {
         this(smt.get());
      }

      public Walker(byte[] data) {
         this.info = data;
         this.numOfEntries = ByteArray.readU16bit(data, 0);
      }

      public final int size() {
         return this.numOfEntries;
      }

      public void parse() throws BadBytecode {
         int n = this.numOfEntries;
         int pos = 2;

         for(int i = 0; i < n; ++i) {
            pos = this.stackMapFrames(pos, i);
         }

      }

      int stackMapFrames(int pos, int nth) throws BadBytecode {
         int type = this.info[pos] & 255;
         if (type < 64) {
            this.sameFrame(pos, type);
            ++pos;
         } else if (type < 128) {
            pos = this.sameLocals(pos, type);
         } else {
            if (type < 247) {
               throw new BadBytecode("bad frame_type in StackMapTable");
            }

            if (type == 247) {
               pos = this.sameLocals(pos, type);
            } else {
               int offset;
               if (type < 251) {
                  offset = ByteArray.readU16bit(this.info, pos + 1);
                  this.chopFrame(pos, offset, 251 - type);
                  pos += 3;
               } else if (type == 251) {
                  offset = ByteArray.readU16bit(this.info, pos + 1);
                  this.sameFrame(pos, offset);
                  pos += 3;
               } else if (type < 255) {
                  pos = this.appendFrame(pos, type);
               } else {
                  pos = this.fullFrame(pos);
               }
            }
         }

         return pos;
      }

      public void sameFrame(int pos, int offsetDelta) throws BadBytecode {
      }

      private int sameLocals(int pos, int type) throws BadBytecode {
         int top = pos;
         int offset;
         if (type < 128) {
            offset = type - 64;
         } else {
            offset = ByteArray.readU16bit(this.info, pos + 1);
            pos += 2;
         }

         int tag = this.info[pos + 1] & 255;
         int data = 0;
         if (tag == 7 || tag == 8) {
            data = ByteArray.readU16bit(this.info, pos + 2);
            this.objectOrUninitialized(tag, data, pos + 2);
            pos += 2;
         }

         this.sameLocals(top, offset, tag, data);
         return pos + 2;
      }

      public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) throws BadBytecode {
      }

      public void chopFrame(int pos, int offsetDelta, int k) throws BadBytecode {
      }

      private int appendFrame(int pos, int type) throws BadBytecode {
         int k = type - 251;
         int offset = ByteArray.readU16bit(this.info, pos + 1);
         int[] tags = new int[k];
         int[] data = new int[k];
         int p = pos + 3;

         for(int i = 0; i < k; ++i) {
            int tag = this.info[p] & 255;
            tags[i] = tag;
            if (tag != 7 && tag != 8) {
               data[i] = 0;
               ++p;
            } else {
               data[i] = ByteArray.readU16bit(this.info, p + 1);
               this.objectOrUninitialized(tag, data[i], p + 1);
               p += 3;
            }
         }

         this.appendFrame(pos, offset, tags, data);
         return p;
      }

      public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) throws BadBytecode {
      }

      private int fullFrame(int pos) throws BadBytecode {
         int offset = ByteArray.readU16bit(this.info, pos + 1);
         int numOfLocals = ByteArray.readU16bit(this.info, pos + 3);
         int[] localsTags = new int[numOfLocals];
         int[] localsData = new int[numOfLocals];
         int p = this.verifyTypeInfo(pos + 5, numOfLocals, localsTags, localsData);
         int numOfItems = ByteArray.readU16bit(this.info, p);
         int[] itemsTags = new int[numOfItems];
         int[] itemsData = new int[numOfItems];
         p = this.verifyTypeInfo(p + 2, numOfItems, itemsTags, itemsData);
         this.fullFrame(pos, offset, localsTags, localsData, itemsTags, itemsData);
         return p;
      }

      public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) throws BadBytecode {
      }

      private int verifyTypeInfo(int pos, int n, int[] tags, int[] data) {
         for(int i = 0; i < n; ++i) {
            int tag = this.info[pos++] & 255;
            tags[i] = tag;
            if (tag == 7 || tag == 8) {
               data[i] = ByteArray.readU16bit(this.info, pos);
               this.objectOrUninitialized(tag, data[i], pos);
               pos += 2;
            }
         }

         return pos;
      }

      public void objectOrUninitialized(int tag, int data, int pos) {
      }
   }

   public static class RuntimeCopyException extends RuntimeException {
      public RuntimeCopyException(String s) {
         super(s);
      }
   }
}
