package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.MemberResolver;

public class CallExpr extends Expr {
   private MemberResolver.Method method = null;

   private CallExpr(ASTree _head, ASTList _tail) {
      super(67, _head, _tail);
   }

   public void setMethod(MemberResolver.Method m) {
      this.method = m;
   }

   public MemberResolver.Method getMethod() {
      return this.method;
   }

   public static CallExpr makeCall(ASTree target, ASTree args) {
      return new CallExpr(target, new ASTList(args));
   }

   public void accept(Visitor v) throws CompileError {
      v.atCallExpr(this);
   }
}
