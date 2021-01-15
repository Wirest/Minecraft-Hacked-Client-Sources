package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class MethodDecl extends ASTList {
   public static final String initName = "<init>";

   public MethodDecl(ASTree _head, ASTList _tail) {
      super(_head, _tail);
   }

   public boolean isConstructor() {
      Symbol sym = this.getReturn().getVariable();
      return sym != null && "<init>".equals(sym.get());
   }

   public ASTList getModifiers() {
      return (ASTList)this.getLeft();
   }

   public Declarator getReturn() {
      return (Declarator)this.tail().head();
   }

   public ASTList getParams() {
      return (ASTList)this.sublist(2).head();
   }

   public ASTList getThrows() {
      return (ASTList)this.sublist(3).head();
   }

   public Stmnt getBody() {
      return (Stmnt)this.sublist(4).head();
   }

   public void accept(Visitor v) throws CompileError {
      v.atMethodDecl(this);
   }
}
