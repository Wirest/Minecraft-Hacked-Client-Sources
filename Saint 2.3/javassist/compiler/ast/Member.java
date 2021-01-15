package javassist.compiler.ast;

import javassist.CtField;
import javassist.compiler.CompileError;

public class Member extends Symbol {
   private CtField field = null;

   public Member(String name) {
      super(name);
   }

   public void setField(CtField f) {
      this.field = f;
   }

   public CtField getField() {
      return this.field;
   }

   public void accept(Visitor v) throws CompileError {
      v.atMember(this);
   }
}
