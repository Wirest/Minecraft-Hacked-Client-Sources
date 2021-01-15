package javassist.bytecode;

class ExceptionTableEntry {
   int startPc;
   int endPc;
   int handlerPc;
   int catchType;

   ExceptionTableEntry(int start, int end, int handle, int type) {
      this.startPc = start;
      this.endPc = end;
      this.handlerPc = handle;
      this.catchType = type;
   }
}
