package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ExceptionTable implements Cloneable {
   private ConstPool constPool;
   private ArrayList entries;

   public ExceptionTable(ConstPool cp) {
      this.constPool = cp;
      this.entries = new ArrayList();
   }

   ExceptionTable(ConstPool cp, DataInputStream in) throws IOException {
      this.constPool = cp;
      int length = in.readUnsignedShort();
      ArrayList list = new ArrayList(length);

      for(int i = 0; i < length; ++i) {
         int start = in.readUnsignedShort();
         int end = in.readUnsignedShort();
         int handle = in.readUnsignedShort();
         int type = in.readUnsignedShort();
         list.add(new ExceptionTableEntry(start, end, handle, type));
      }

      this.entries = list;
   }

   public Object clone() throws CloneNotSupportedException {
      ExceptionTable r = (ExceptionTable)super.clone();
      r.entries = new ArrayList(this.entries);
      return r;
   }

   public int size() {
      return this.entries.size();
   }

   public int startPc(int nth) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      return e.startPc;
   }

   public void setStartPc(int nth, int value) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      e.startPc = value;
   }

   public int endPc(int nth) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      return e.endPc;
   }

   public void setEndPc(int nth, int value) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      e.endPc = value;
   }

   public int handlerPc(int nth) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      return e.handlerPc;
   }

   public void setHandlerPc(int nth, int value) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      e.handlerPc = value;
   }

   public int catchType(int nth) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      return e.catchType;
   }

   public void setCatchType(int nth, int value) {
      ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(nth);
      e.catchType = value;
   }

   public void add(int index, ExceptionTable table, int offset) {
      int len = table.size();

      while(true) {
         --len;
         if (len < 0) {
            return;
         }

         ExceptionTableEntry e = (ExceptionTableEntry)table.entries.get(len);
         this.add(index, e.startPc + offset, e.endPc + offset, e.handlerPc + offset, e.catchType);
      }
   }

   public void add(int index, int start, int end, int handler, int type) {
      if (start < end) {
         this.entries.add(index, new ExceptionTableEntry(start, end, handler, type));
      }

   }

   public void add(int start, int end, int handler, int type) {
      if (start < end) {
         this.entries.add(new ExceptionTableEntry(start, end, handler, type));
      }

   }

   public void remove(int index) {
      this.entries.remove(index);
   }

   public ExceptionTable copy(ConstPool newCp, Map classnames) {
      ExceptionTable et = new ExceptionTable(newCp);
      ConstPool srcCp = this.constPool;
      int len = this.size();

      for(int i = 0; i < len; ++i) {
         ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(i);
         int type = srcCp.copy(e.catchType, newCp, classnames);
         et.add(e.startPc, e.endPc, e.handlerPc, type);
      }

      return et;
   }

   void shiftPc(int where, int gapLength, boolean exclusive) {
      int len = this.size();

      for(int i = 0; i < len; ++i) {
         ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(i);
         e.startPc = shiftPc(e.startPc, where, gapLength, exclusive);
         e.endPc = shiftPc(e.endPc, where, gapLength, exclusive);
         e.handlerPc = shiftPc(e.handlerPc, where, gapLength, exclusive);
      }

   }

   private static int shiftPc(int pc, int where, int gapLength, boolean exclusive) {
      if (pc > where || exclusive && pc == where) {
         pc += gapLength;
      }

      return pc;
   }

   void write(DataOutputStream out) throws IOException {
      int len = this.size();
      out.writeShort(len);

      for(int i = 0; i < len; ++i) {
         ExceptionTableEntry e = (ExceptionTableEntry)this.entries.get(i);
         out.writeShort(e.startPc);
         out.writeShort(e.endPc);
         out.writeShort(e.handlerPc);
         out.writeShort(e.catchType);
      }

   }
}
