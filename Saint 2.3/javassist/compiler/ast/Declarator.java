package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class Declarator extends ASTList implements TokenId {
   protected int varType;
   protected int arrayDim;
   protected int localVar;
   protected String qualifiedClass;

   public Declarator(int type, int dim) {
      super((ASTree)null);
      this.varType = type;
      this.arrayDim = dim;
      this.localVar = -1;
      this.qualifiedClass = null;
   }

   public Declarator(ASTList className, int dim) {
      super((ASTree)null);
      this.varType = 307;
      this.arrayDim = dim;
      this.localVar = -1;
      this.qualifiedClass = astToClassName(className, '/');
   }

   public Declarator(int type, String jvmClassName, int dim, int var, Symbol sym) {
      super((ASTree)null);
      this.varType = type;
      this.arrayDim = dim;
      this.localVar = var;
      this.qualifiedClass = jvmClassName;
      this.setLeft(sym);
      append(this, (ASTree)null);
   }

   public Declarator make(Symbol sym, int dim, ASTree init) {
      Declarator d = new Declarator(this.varType, this.arrayDim + dim);
      d.qualifiedClass = this.qualifiedClass;
      d.setLeft(sym);
      append(d, init);
      return d;
   }

   public int getType() {
      return this.varType;
   }

   public int getArrayDim() {
      return this.arrayDim;
   }

   public void addArrayDim(int d) {
      this.arrayDim += d;
   }

   public String getClassName() {
      return this.qualifiedClass;
   }

   public void setClassName(String s) {
      this.qualifiedClass = s;
   }

   public Symbol getVariable() {
      return (Symbol)this.getLeft();
   }

   public void setVariable(Symbol sym) {
      this.setLeft(sym);
   }

   public ASTree getInitializer() {
      ASTList t = this.tail();
      return t != null ? t.head() : null;
   }

   public void setLocalVar(int n) {
      this.localVar = n;
   }

   public int getLocalVar() {
      return this.localVar;
   }

   public String getTag() {
      return "decl";
   }

   public void accept(Visitor v) throws CompileError {
      v.atDeclarator(this);
   }

   public static String astToClassName(ASTList name, char sep) {
      if (name == null) {
         return null;
      } else {
         StringBuffer sbuf = new StringBuffer();
         astToClassName(sbuf, name, sep);
         return sbuf.toString();
      }
   }

   private static void astToClassName(StringBuffer sbuf, ASTList name, char sep) {
      while(true) {
         ASTree h = name.head();
         if (h instanceof Symbol) {
            sbuf.append(((Symbol)h).get());
         } else if (h instanceof ASTList) {
            astToClassName(sbuf, (ASTList)h, sep);
         }

         name = name.tail();
         if (name == null) {
            return;
         }

         sbuf.append(sep);
      }
   }
}
