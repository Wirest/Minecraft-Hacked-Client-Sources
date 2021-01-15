package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ASTList extends ASTree {
   private ASTree left;
   private ASTList right;

   public ASTList(ASTree _head, ASTList _tail) {
      this.left = _head;
      this.right = _tail;
   }

   public ASTList(ASTree _head) {
      this.left = _head;
      this.right = null;
   }

   public static ASTList make(ASTree e1, ASTree e2, ASTree e3) {
      return new ASTList(e1, new ASTList(e2, new ASTList(e3)));
   }

   public ASTree getLeft() {
      return this.left;
   }

   public ASTree getRight() {
      return this.right;
   }

   public void setLeft(ASTree _left) {
      this.left = _left;
   }

   public void setRight(ASTree _right) {
      this.right = (ASTList)_right;
   }

   public ASTree head() {
      return this.left;
   }

   public void setHead(ASTree _head) {
      this.left = _head;
   }

   public ASTList tail() {
      return this.right;
   }

   public void setTail(ASTList _tail) {
      this.right = _tail;
   }

   public void accept(Visitor v) throws CompileError {
      v.atASTList(this);
   }

   public String toString() {
      StringBuffer sbuf = new StringBuffer();
      sbuf.append("(<");
      sbuf.append(this.getTag());
      sbuf.append('>');

      for(ASTList list = this; list != null; list = list.right) {
         sbuf.append(' ');
         ASTree a = list.left;
         sbuf.append(a == null ? "<null>" : a.toString());
      }

      sbuf.append(')');
      return sbuf.toString();
   }

   public int length() {
      return length(this);
   }

   public static int length(ASTList list) {
      if (list == null) {
         return 0;
      } else {
         int n;
         for(n = 0; list != null; ++n) {
            list = list.right;
         }

         return n;
      }
   }

   public ASTList sublist(int nth) {
      ASTList list;
      for(list = this; nth-- > 0; list = list.right) {
      }

      return list;
   }

   public boolean subst(ASTree newObj, ASTree oldObj) {
      for(ASTList list = this; list != null; list = list.right) {
         if (list.left == oldObj) {
            list.left = newObj;
            return true;
         }
      }

      return false;
   }

   public static ASTList append(ASTList a, ASTree b) {
      return concat(a, new ASTList(b));
   }

   public static ASTList concat(ASTList a, ASTList b) {
      if (a == null) {
         return b;
      } else {
         ASTList list;
         for(list = a; list.right != null; list = list.right) {
         }

         list.right = b;
         return a;
      }
   }
}
