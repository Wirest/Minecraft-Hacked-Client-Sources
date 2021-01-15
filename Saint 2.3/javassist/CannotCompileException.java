package javassist;

import javassist.compiler.CompileError;

public class CannotCompileException extends Exception {
   private Throwable myCause;
   private String message;

   public Throwable getCause() {
      return this.myCause == this ? null : this.myCause;
   }

   public synchronized Throwable initCause(Throwable cause) {
      this.myCause = cause;
      return this;
   }

   public String getReason() {
      return this.message != null ? this.message : this.toString();
   }

   public CannotCompileException(String msg) {
      super(msg);
      this.message = msg;
      this.initCause((Throwable)null);
   }

   public CannotCompileException(Throwable e) {
      super("by " + e.toString());
      this.message = null;
      this.initCause(e);
   }

   public CannotCompileException(String msg, Throwable e) {
      this(msg);
      this.initCause(e);
   }

   public CannotCompileException(NotFoundException e) {
      this((String)("cannot find " + e.getMessage()), (Throwable)e);
   }

   public CannotCompileException(CompileError e) {
      this((String)("[source error] " + e.getMessage()), (Throwable)e);
   }

   public CannotCompileException(ClassNotFoundException e, String name) {
      this((String)("cannot find " + name), (Throwable)e);
   }

   public CannotCompileException(ClassFormatError e, String name) {
      this((String)("invalid class format: " + name), (Throwable)e);
   }
}
