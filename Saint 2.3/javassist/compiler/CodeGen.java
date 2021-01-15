package javassist.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.Visitor;

public abstract class CodeGen extends Visitor implements Opcode, TokenId {
   static final String javaLangObject = "java.lang.Object";
   static final String jvmJavaLangObject = "java/lang/Object";
   static final String javaLangString = "java.lang.String";
   static final String jvmJavaLangString = "java/lang/String";
   protected Bytecode bytecode;
   private int tempVar;
   TypeChecker typeChecker;
   protected boolean hasReturned;
   public boolean inStaticMethod;
   protected ArrayList breakList;
   protected ArrayList continueList;
   protected CodeGen.ReturnHook returnHooks;
   protected int exprType;
   protected int arrayDim;
   protected String className;
   static final int[] binOp = new int[]{43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124};
   private static final int[] ifOp = new int[]{358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164};
   private static final int[] ifOp2 = new int[]{358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158};
   private static final int P_DOUBLE = 0;
   private static final int P_FLOAT = 1;
   private static final int P_LONG = 2;
   private static final int P_INT = 3;
   private static final int P_OTHER = -1;
   private static final int[] castOp = new int[]{0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0};

   public CodeGen(Bytecode b) {
      this.bytecode = b;
      this.tempVar = -1;
      this.typeChecker = null;
      this.hasReturned = false;
      this.inStaticMethod = false;
      this.breakList = null;
      this.continueList = null;
      this.returnHooks = null;
   }

   public void setTypeChecker(TypeChecker checker) {
      this.typeChecker = checker;
   }

   protected static void fatal() throws CompileError {
      throw new CompileError("fatal");
   }

   public static boolean is2word(int type, int dim) {
      return dim == 0 && (type == 312 || type == 326);
   }

   public int getMaxLocals() {
      return this.bytecode.getMaxLocals();
   }

   public void setMaxLocals(int n) {
      this.bytecode.setMaxLocals(n);
   }

   protected void incMaxLocals(int size) {
      this.bytecode.incMaxLocals(size);
   }

   protected int getTempVar() {
      if (this.tempVar < 0) {
         this.tempVar = this.getMaxLocals();
         this.incMaxLocals(2);
      }

      return this.tempVar;
   }

   protected int getLocalVar(Declarator d) {
      int v = d.getLocalVar();
      if (v < 0) {
         v = this.getMaxLocals();
         d.setLocalVar(v);
         this.incMaxLocals(1);
      }

      return v;
   }

   protected abstract String getThisName();

   protected abstract String getSuperName() throws CompileError;

   protected abstract String resolveClassName(ASTList name) throws CompileError;

   protected abstract String resolveClassName(String jvmClassName) throws CompileError;

   protected static String toJvmArrayName(String name, int dim) {
      if (name == null) {
         return null;
      } else if (dim == 0) {
         return name;
      } else {
         StringBuffer sbuf = new StringBuffer();
         int var3 = dim;

         while(var3-- > 0) {
            sbuf.append('[');
         }

         sbuf.append('L');
         sbuf.append(name);
         sbuf.append(';');
         return sbuf.toString();
      }
   }

   protected static String toJvmTypeName(int type, int dim) {
      char c = 'I';
      switch(type) {
      case 301:
         c = 'Z';
         break;
      case 303:
         c = 'B';
         break;
      case 306:
         c = 'C';
         break;
      case 312:
         c = 'D';
         break;
      case 317:
         c = 'F';
         break;
      case 324:
         c = 'I';
         break;
      case 326:
         c = 'J';
         break;
      case 334:
         c = 'S';
         break;
      case 344:
         c = 'V';
      }

      StringBuffer sbuf = new StringBuffer();

      while(dim-- > 0) {
         sbuf.append('[');
      }

      sbuf.append(c);
      return sbuf.toString();
   }

   public void compileExpr(ASTree expr) throws CompileError {
      this.doTypeCheck(expr);
      expr.accept(this);
   }

   public boolean compileBooleanExpr(boolean branchIf, ASTree expr) throws CompileError {
      this.doTypeCheck(expr);
      return this.booleanExpr(branchIf, expr);
   }

   public void doTypeCheck(ASTree expr) throws CompileError {
      if (this.typeChecker != null) {
         expr.accept(this.typeChecker);
      }

   }

   public void atASTList(ASTList n) throws CompileError {
      fatal();
   }

   public void atPair(Pair n) throws CompileError {
      fatal();
   }

   public void atSymbol(Symbol n) throws CompileError {
      fatal();
   }

   public void atFieldDecl(FieldDecl field) throws CompileError {
      field.getInit().accept(this);
   }

   public void atMethodDecl(MethodDecl method) throws CompileError {
      ASTList mods = method.getModifiers();
      this.setMaxLocals(1);

      while(mods != null) {
         Keyword k = (Keyword)mods.head();
         mods = mods.tail();
         if (k.get() == 335) {
            this.setMaxLocals(0);
            this.inStaticMethod = true;
         }
      }

      for(ASTList params = method.getParams(); params != null; params = params.tail()) {
         this.atDeclarator((Declarator)params.head());
      }

      Stmnt s = method.getBody();
      this.atMethodBody(s, method.isConstructor(), method.getReturn().getType() == 344);
   }

   public void atMethodBody(Stmnt s, boolean isCons, boolean isVoid) throws CompileError {
      if (s != null) {
         if (isCons && this.needsSuperCall(s)) {
            this.insertDefaultSuperCall();
         }

         this.hasReturned = false;
         s.accept(this);
         if (!this.hasReturned) {
            if (!isVoid) {
               throw new CompileError("no return statement");
            }

            this.bytecode.addOpcode(177);
            this.hasReturned = true;
         }

      }
   }

   private boolean needsSuperCall(Stmnt body) throws CompileError {
      if (body.getOperator() == 66) {
         body = (Stmnt)body.head();
      }

      if (body != null && body.getOperator() == 69) {
         ASTree expr = body.head();
         if (expr != null && expr instanceof Expr && ((Expr)expr).getOperator() == 67) {
            ASTree target = ((Expr)expr).head();
            if (target instanceof Keyword) {
               int token = ((Keyword)target).get();
               return token != 339 && token != 336;
            }
         }
      }

      return true;
   }

   protected abstract void insertDefaultSuperCall() throws CompileError;

   public void atStmnt(Stmnt st) throws CompileError {
      if (st != null) {
         int op = st.getOperator();
         if (op == 69) {
            ASTree expr = st.getLeft();
            this.doTypeCheck(expr);
            if (expr instanceof AssignExpr) {
               this.atAssignExpr((AssignExpr)expr, false);
            } else if (isPlusPlusExpr(expr)) {
               Expr e = (Expr)expr;
               this.atPlusPlus(e.getOperator(), e.oprand1(), e, false);
            } else {
               expr.accept(this);
               if (is2word(this.exprType, this.arrayDim)) {
                  this.bytecode.addOpcode(88);
               } else if (this.exprType != 344) {
                  this.bytecode.addOpcode(87);
               }
            }
         } else if (op != 68 && op != 66) {
            if (op == 320) {
               this.atIfStmnt(st);
            } else if (op != 346 && op != 311) {
               if (op == 318) {
                  this.atForStmnt(st);
               } else if (op != 302 && op != 309) {
                  if (op == 333) {
                     this.atReturnStmnt(st);
                  } else if (op == 340) {
                     this.atThrowStmnt(st);
                  } else if (op == 343) {
                     this.atTryStmnt(st);
                  } else if (op == 337) {
                     this.atSwitchStmnt(st);
                  } else {
                     if (op != 338) {
                        this.hasReturned = false;
                        throw new CompileError("sorry, not supported statement: TokenId " + op);
                     }

                     this.atSyncStmnt(st);
                  }
               } else {
                  this.atBreakStmnt(st, op == 302);
               }
            } else {
               this.atWhileStmnt(st, op == 346);
            }
         } else {
            Object list = st;

            while(list != null) {
               ASTree h = ((ASTList)list).head();
               list = ((ASTList)list).tail();
               if (h != null) {
                  h.accept(this);
               }
            }
         }

      }
   }

   private void atIfStmnt(Stmnt st) throws CompileError {
      ASTree expr = st.head();
      Stmnt thenp = (Stmnt)st.tail().head();
      Stmnt elsep = (Stmnt)st.tail().tail().head();
      if (this.compileBooleanExpr(false, expr)) {
         this.hasReturned = false;
         if (elsep != null) {
            elsep.accept(this);
         }

      } else {
         int pc = this.bytecode.currentPc();
         int pc2 = 0;
         this.bytecode.addIndex(0);
         this.hasReturned = false;
         if (thenp != null) {
            thenp.accept(this);
         }

         boolean thenHasReturned = this.hasReturned;
         this.hasReturned = false;
         if (elsep != null && !thenHasReturned) {
            this.bytecode.addOpcode(167);
            pc2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
         }

         this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
         if (elsep != null) {
            elsep.accept(this);
            if (!thenHasReturned) {
               this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
            }

            this.hasReturned = thenHasReturned && this.hasReturned;
         }

      }
   }

   private void atWhileStmnt(Stmnt st, boolean notDo) throws CompileError {
      ArrayList prevBreakList = this.breakList;
      ArrayList prevContList = this.continueList;
      this.breakList = new ArrayList();
      this.continueList = new ArrayList();
      ASTree expr = st.head();
      Stmnt body = (Stmnt)st.tail();
      int pc = 0;
      if (notDo) {
         this.bytecode.addOpcode(167);
         pc = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
      }

      int pc2 = this.bytecode.currentPc();
      if (body != null) {
         body.accept(this);
      }

      int pc3 = this.bytecode.currentPc();
      if (notDo) {
         this.bytecode.write16bit(pc, pc3 - pc + 1);
      }

      boolean alwaysBranch = this.compileBooleanExpr(true, expr);
      if (alwaysBranch) {
         this.bytecode.addOpcode(167);
         alwaysBranch = this.breakList.size() == 0;
      }

      this.bytecode.addIndex(pc2 - this.bytecode.currentPc() + 1);
      this.patchGoto(this.breakList, this.bytecode.currentPc());
      this.patchGoto(this.continueList, pc3);
      this.continueList = prevContList;
      this.breakList = prevBreakList;
      this.hasReturned = alwaysBranch;
   }

   protected void patchGoto(ArrayList list, int targetPc) {
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         int pc = (Integer)list.get(i);
         this.bytecode.write16bit(pc, targetPc - pc + 1);
      }

   }

   private void atForStmnt(Stmnt st) throws CompileError {
      ArrayList prevBreakList = this.breakList;
      ArrayList prevContList = this.continueList;
      this.breakList = new ArrayList();
      this.continueList = new ArrayList();
      Stmnt init = (Stmnt)st.head();
      ASTList p = st.tail();
      ASTree expr = p.head();
      p = p.tail();
      Stmnt update = (Stmnt)p.head();
      Stmnt body = (Stmnt)p.tail();
      if (init != null) {
         init.accept(this);
      }

      int pc = this.bytecode.currentPc();
      int pc2 = 0;
      if (expr != null) {
         if (this.compileBooleanExpr(false, expr)) {
            this.continueList = prevContList;
            this.breakList = prevBreakList;
            this.hasReturned = false;
            return;
         }

         pc2 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
      }

      if (body != null) {
         body.accept(this);
      }

      int pc3 = this.bytecode.currentPc();
      if (update != null) {
         update.accept(this);
      }

      this.bytecode.addOpcode(167);
      this.bytecode.addIndex(pc - this.bytecode.currentPc() + 1);
      int pc4 = this.bytecode.currentPc();
      if (expr != null) {
         this.bytecode.write16bit(pc2, pc4 - pc2 + 1);
      }

      this.patchGoto(this.breakList, pc4);
      this.patchGoto(this.continueList, pc3);
      this.continueList = prevContList;
      this.breakList = prevBreakList;
      this.hasReturned = false;
   }

   private void atSwitchStmnt(Stmnt st) throws CompileError {
      this.compileExpr(st.head());
      ArrayList prevBreakList = this.breakList;
      this.breakList = new ArrayList();
      int opcodePc = this.bytecode.currentPc();
      this.bytecode.addOpcode(171);
      int var4 = 3 - (opcodePc & 3);

      while(var4-- > 0) {
         this.bytecode.add(0);
      }

      Stmnt body = (Stmnt)st.tail();
      int npairs = 0;

      for(Object list = body; list != null; list = ((ASTList)list).tail()) {
         if (((Stmnt)((ASTList)list).head()).getOperator() == 304) {
            ++npairs;
         }
      }

      int opcodePc2 = this.bytecode.currentPc();
      this.bytecode.addGap(4);
      this.bytecode.add32bit(npairs);
      this.bytecode.addGap(npairs * 8);
      long[] pairs = new long[npairs];
      int ipairs = 0;
      int defaultPc = -1;

      for(Object list = body; list != null; list = ((ASTList)list).tail()) {
         Stmnt label = (Stmnt)((ASTList)list).head();
         int op = label.getOperator();
         if (op == 310) {
            defaultPc = this.bytecode.currentPc();
         } else if (op != 304) {
            fatal();
         } else {
            pairs[ipairs++] = ((long)this.computeLabel(label.head()) << 32) + ((long)(this.bytecode.currentPc() - opcodePc) & -1L);
         }

         this.hasReturned = false;
         ((Stmnt)label.tail()).accept(this);
      }

      Arrays.sort(pairs);
      int pc = opcodePc2 + 8;

      int endPc;
      for(endPc = 0; endPc < npairs; ++endPc) {
         this.bytecode.write32bit(pc, (int)(pairs[endPc] >>> 32));
         this.bytecode.write32bit(pc + 4, (int)pairs[endPc]);
         pc += 8;
      }

      if (defaultPc < 0 || this.breakList.size() > 0) {
         this.hasReturned = false;
      }

      endPc = this.bytecode.currentPc();
      if (defaultPc < 0) {
         defaultPc = endPc;
      }

      this.bytecode.write32bit(opcodePc2, defaultPc - opcodePc);
      this.patchGoto(this.breakList, endPc);
      this.breakList = prevBreakList;
   }

   private int computeLabel(ASTree expr) throws CompileError {
      this.doTypeCheck(expr);
      expr = TypeChecker.stripPlusExpr(expr);
      if (expr instanceof IntConst) {
         return (int)((IntConst)expr).get();
      } else {
         throw new CompileError("bad case label");
      }
   }

   private void atBreakStmnt(Stmnt st, boolean notCont) throws CompileError {
      if (st.head() != null) {
         throw new CompileError("sorry, not support labeled break or continue");
      } else {
         this.bytecode.addOpcode(167);
         Integer pc = new Integer(this.bytecode.currentPc());
         this.bytecode.addIndex(0);
         if (notCont) {
            this.breakList.add(pc);
         } else {
            this.continueList.add(pc);
         }

      }
   }

   protected void atReturnStmnt(Stmnt st) throws CompileError {
      this.atReturnStmnt2(st.getLeft());
   }

   protected final void atReturnStmnt2(ASTree result) throws CompileError {
      short op;
      if (result == null) {
         op = 177;
      } else {
         this.compileExpr(result);
         if (this.arrayDim > 0) {
            op = 176;
         } else {
            int type = this.exprType;
            if (type == 312) {
               op = 175;
            } else if (type == 317) {
               op = 174;
            } else if (type == 326) {
               op = 173;
            } else if (isRefType(type)) {
               op = 176;
            } else {
               op = 172;
            }
         }
      }

      for(CodeGen.ReturnHook har = this.returnHooks; har != null; har = har.next) {
         if (har.doit(this.bytecode, op)) {
            this.hasReturned = true;
            return;
         }
      }

      this.bytecode.addOpcode(op);
      this.hasReturned = true;
   }

   private void atThrowStmnt(Stmnt st) throws CompileError {
      ASTree e = st.getLeft();
      this.compileExpr(e);
      if (this.exprType == 307 && this.arrayDim <= 0) {
         this.bytecode.addOpcode(191);
         this.hasReturned = true;
      } else {
         throw new CompileError("bad throw statement");
      }
   }

   protected void atTryStmnt(Stmnt st) throws CompileError {
      this.hasReturned = false;
   }

   private void atSyncStmnt(Stmnt st) throws CompileError {
      int nbreaks = getListSize(this.breakList);
      int ncontinues = getListSize(this.continueList);
      this.compileExpr(st.head());
      if (this.exprType != 307 && this.arrayDim == 0) {
         throw new CompileError("bad type expr for synchronized block");
      } else {
         Bytecode bc = this.bytecode;
         final int var = bc.getMaxLocals();
         bc.incMaxLocals(1);
         bc.addOpcode(89);
         bc.addAstore(var);
         bc.addOpcode(194);
         CodeGen.ReturnHook rh = new CodeGen.ReturnHook(this) {
            protected boolean doit(Bytecode b, int opcode) {
               b.addAload(var);
               b.addOpcode(195);
               return false;
            }
         };
         int pc = bc.currentPc();
         Stmnt body = (Stmnt)st.tail();
         if (body != null) {
            body.accept(this);
         }

         int pc2 = bc.currentPc();
         int pc3 = 0;
         if (!this.hasReturned) {
            rh.doit(bc, 0);
            bc.addOpcode(167);
            pc3 = bc.currentPc();
            bc.addIndex(0);
         }

         if (pc < pc2) {
            int pc4 = bc.currentPc();
            rh.doit(bc, 0);
            bc.addOpcode(191);
            bc.addExceptionHandler(pc, pc2, pc4, 0);
         }

         if (!this.hasReturned) {
            bc.write16bit(pc3, bc.currentPc() - pc3 + 1);
         }

         rh.remove(this);
         if (getListSize(this.breakList) != nbreaks || getListSize(this.continueList) != ncontinues) {
            throw new CompileError("sorry, cannot break/continue in synchronized block");
         }
      }
   }

   private static int getListSize(ArrayList list) {
      return list == null ? 0 : list.size();
   }

   private static boolean isPlusPlusExpr(ASTree expr) {
      if (!(expr instanceof Expr)) {
         return false;
      } else {
         int op = ((Expr)expr).getOperator();
         return op == 362 || op == 363;
      }
   }

   public void atDeclarator(Declarator d) throws CompileError {
      d.setLocalVar(this.getMaxLocals());
      d.setClassName(this.resolveClassName(d.getClassName()));
      byte size;
      if (is2word(d.getType(), d.getArrayDim())) {
         size = 2;
      } else {
         size = 1;
      }

      this.incMaxLocals(size);
      ASTree init = d.getInitializer();
      if (init != null) {
         this.doTypeCheck(init);
         this.atVariableAssign((Expr)null, 61, (Variable)null, d, init, false);
      }

   }

   public abstract void atNewExpr(NewExpr n) throws CompileError;

   public abstract void atArrayInit(ArrayInit init) throws CompileError;

   public void atAssignExpr(AssignExpr expr) throws CompileError {
      this.atAssignExpr(expr, true);
   }

   protected void atAssignExpr(AssignExpr expr, boolean doDup) throws CompileError {
      int op = expr.getOperator();
      ASTree left = expr.oprand1();
      ASTree right = expr.oprand2();
      if (left instanceof Variable) {
         this.atVariableAssign(expr, op, (Variable)left, ((Variable)left).getDeclarator(), right, doDup);
      } else {
         if (left instanceof Expr) {
            Expr e = (Expr)left;
            if (e.getOperator() == 65) {
               this.atArrayAssign(expr, op, (Expr)left, right, doDup);
               return;
            }
         }

         this.atFieldAssign(expr, op, left, right, doDup);
      }

   }

   protected static void badAssign(Expr expr) throws CompileError {
      String msg;
      if (expr == null) {
         msg = "incompatible type for assignment";
      } else {
         msg = "incompatible type for " + expr.getName();
      }

      throw new CompileError(msg);
   }

   private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right, boolean doDup) throws CompileError {
      int varType = d.getType();
      int varArray = d.getArrayDim();
      String varClass = d.getClassName();
      int varNo = this.getLocalVar(d);
      if (op != 61) {
         this.atVariable(var);
      }

      if (expr == null && right instanceof ArrayInit) {
         this.atArrayVariableAssign((ArrayInit)right, varType, varArray, varClass);
      } else {
         this.atAssignCore(expr, op, right, varType, varArray, varClass);
      }

      if (doDup) {
         if (is2word(varType, varArray)) {
            this.bytecode.addOpcode(92);
         } else {
            this.bytecode.addOpcode(89);
         }
      }

      if (varArray > 0) {
         this.bytecode.addAstore(varNo);
      } else if (varType == 312) {
         this.bytecode.addDstore(varNo);
      } else if (varType == 317) {
         this.bytecode.addFstore(varNo);
      } else if (varType == 326) {
         this.bytecode.addLstore(varNo);
      } else if (isRefType(varType)) {
         this.bytecode.addAstore(varNo);
      } else {
         this.bytecode.addIstore(varNo);
      }

      this.exprType = varType;
      this.arrayDim = varArray;
      this.className = varClass;
   }

   protected abstract void atArrayVariableAssign(ArrayInit init, int varType, int varArray, String varClass) throws CompileError;

   private void atArrayAssign(Expr expr, int op, Expr array, ASTree right, boolean doDup) throws CompileError {
      this.arrayAccess(array.oprand1(), array.oprand2());
      if (op != 61) {
         this.bytecode.addOpcode(92);
         this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
      }

      int aType = this.exprType;
      int aDim = this.arrayDim;
      String cname = this.className;
      this.atAssignCore(expr, op, right, aType, aDim, cname);
      if (doDup) {
         if (is2word(aType, aDim)) {
            this.bytecode.addOpcode(94);
         } else {
            this.bytecode.addOpcode(91);
         }
      }

      this.bytecode.addOpcode(getArrayWriteOp(aType, aDim));
      this.exprType = aType;
      this.arrayDim = aDim;
      this.className = cname;
   }

   protected abstract void atFieldAssign(Expr expr, int op, ASTree left, ASTree right, boolean doDup) throws CompileError;

   protected void atAssignCore(Expr expr, int op, ASTree right, int type, int dim, String cname) throws CompileError {
      if (op == 354 && dim == 0 && type == 307) {
         this.atStringPlusEq(expr, type, dim, cname, right);
      } else {
         right.accept(this);
         if (this.invalidDim(this.exprType, this.arrayDim, this.className, type, dim, cname, false) || op != 61 && dim > 0) {
            badAssign(expr);
         }

         if (op != 61) {
            int token = assignOps[op - 351];
            int k = lookupBinOp(token);
            if (k < 0) {
               fatal();
            }

            this.atArithBinExpr(expr, token, k, type);
         }
      }

      if (op != 61 || dim == 0 && !isRefType(type)) {
         this.atNumCastExpr(this.exprType, type);
      }

   }

   private void atStringPlusEq(Expr expr, int type, int dim, String cname, ASTree right) throws CompileError {
      if (!"java/lang/String".equals(cname)) {
         badAssign(expr);
      }

      this.convToString(type, dim);
      right.accept(this);
      this.convToString(this.exprType, this.arrayDim);
      this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
   }

   private boolean invalidDim(int srcType, int srcDim, String srcClass, int destType, int destDim, String destClass, boolean isCast) {
      if (srcDim != destDim) {
         if (srcType == 412) {
            return false;
         } else if (destDim == 0 && destType == 307 && "java/lang/Object".equals(destClass)) {
            return false;
         } else {
            return !isCast || srcDim != 0 || srcType != 307 || !"java/lang/Object".equals(srcClass);
         }
      } else {
         return false;
      }
   }

   public void atCondExpr(CondExpr expr) throws CompileError {
      if (this.booleanExpr(false, expr.condExpr())) {
         expr.elseExpr().accept(this);
      } else {
         int pc = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
         expr.thenExpr().accept(this);
         int dim1 = this.arrayDim;
         this.bytecode.addOpcode(167);
         int pc2 = this.bytecode.currentPc();
         this.bytecode.addIndex(0);
         this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
         expr.elseExpr().accept(this);
         if (dim1 != this.arrayDim) {
            throw new CompileError("type mismatch in ?:");
         }

         this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
      }

   }

   static int lookupBinOp(int token) {
      int[] code = binOp;
      int s = code.length;

      for(int k = 0; k < s; k += 5) {
         if (code[k] == token) {
            return k;
         }
      }

      return -1;
   }

   public void atBinExpr(BinExpr expr) throws CompileError {
      int token = expr.getOperator();
      int k = lookupBinOp(token);
      if (k >= 0) {
         expr.oprand1().accept(this);
         ASTree right = expr.oprand2();
         if (right == null) {
            return;
         }

         int type1 = this.exprType;
         int dim1 = this.arrayDim;
         String cname1 = this.className;
         right.accept(this);
         if (dim1 != this.arrayDim) {
            throw new CompileError("incompatible array types");
         }

         if (token != 43 || dim1 != 0 || type1 != 307 && this.exprType != 307) {
            this.atArithBinExpr(expr, token, k, type1);
         } else {
            this.atStringConcatExpr(expr, type1, dim1, cname1);
         }
      } else {
         if (!this.booleanExpr(true, expr)) {
            this.bytecode.addIndex(7);
            this.bytecode.addIconst(0);
            this.bytecode.addOpcode(167);
            this.bytecode.addIndex(4);
         }

         this.bytecode.addIconst(1);
      }

   }

   private void atArithBinExpr(Expr expr, int token, int index, int type1) throws CompileError {
      if (this.arrayDim != 0) {
         badTypes(expr);
      }

      int type2 = this.exprType;
      if (token != 364 && token != 366 && token != 370) {
         this.convertOprandTypes(type1, type2, expr);
      } else if (type2 != 324 && type2 != 334 && type2 != 306 && type2 != 303) {
         badTypes(expr);
      } else {
         this.exprType = type1;
      }

      int p = typePrecedence(this.exprType);
      if (p >= 0) {
         int op = binOp[index + p + 1];
         if (op != 0) {
            if (p == 3 && this.exprType != 301) {
               this.exprType = 324;
            }

            this.bytecode.addOpcode(op);
            return;
         }
      }

      badTypes(expr);
   }

   private void atStringConcatExpr(Expr expr, int type1, int dim1, String cname1) throws CompileError {
      int type2 = this.exprType;
      int dim2 = this.arrayDim;
      boolean type2Is2 = is2word(type2, dim2);
      boolean type2IsString = type2 == 307 && "java/lang/String".equals(this.className);
      if (type2Is2) {
         this.convToString(type2, dim2);
      }

      if (is2word(type1, dim1)) {
         this.bytecode.addOpcode(91);
         this.bytecode.addOpcode(87);
      } else {
         this.bytecode.addOpcode(95);
      }

      this.convToString(type1, dim1);
      this.bytecode.addOpcode(95);
      if (!type2Is2 && !type2IsString) {
         this.convToString(type2, dim2);
      }

      this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
   }

   private void convToString(int type, int dim) throws CompileError {
      String method = "valueOf";
      if (!isRefType(type) && dim <= 0) {
         if (type == 312) {
            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(D)Ljava/lang/String;");
         } else if (type == 317) {
            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(F)Ljava/lang/String;");
         } else if (type == 326) {
            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(J)Ljava/lang/String;");
         } else if (type == 301) {
            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Z)Ljava/lang/String;");
         } else if (type == 306) {
            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(C)Ljava/lang/String;");
         } else {
            if (type == 344) {
               throw new CompileError("void type expression");
            }

            this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(I)Ljava/lang/String;");
         }
      } else {
         this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
      }

   }

   private boolean booleanExpr(boolean branchIf, ASTree expr) throws CompileError {
      int op = getCompOperator(expr);
      BinExpr bexpr;
      int pc;
      if (op == 358) {
         bexpr = (BinExpr)expr;
         pc = this.compileOprands(bexpr);
         this.compareExpr(branchIf, bexpr.getOperator(), pc, bexpr);
      } else {
         if (op == 33) {
            return this.booleanExpr(!branchIf, ((Expr)expr).oprand1());
         }

         boolean isAndAnd;
         if (!(isAndAnd = op == 369) && op != 368) {
            if (isAlwaysBranch(expr, branchIf)) {
               this.exprType = 301;
               this.arrayDim = 0;
               return true;
            }

            expr.accept(this);
            if (this.exprType != 301 || this.arrayDim != 0) {
               throw new CompileError("boolean expr is required");
            }

            this.bytecode.addOpcode(branchIf ? 154 : 153);
         } else {
            bexpr = (BinExpr)expr;
            if (this.booleanExpr(!isAndAnd, bexpr.oprand1())) {
               this.exprType = 301;
               this.arrayDim = 0;
               return true;
            }

            pc = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
            if (this.booleanExpr(isAndAnd, bexpr.oprand2())) {
               this.bytecode.addOpcode(167);
            }

            this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 3);
            if (branchIf != isAndAnd) {
               this.bytecode.addIndex(6);
               this.bytecode.addOpcode(167);
            }
         }
      }

      this.exprType = 301;
      this.arrayDim = 0;
      return false;
   }

   private static boolean isAlwaysBranch(ASTree expr, boolean branchIf) {
      if (expr instanceof Keyword) {
         int t = ((Keyword)expr).get();
         return branchIf ? t == 410 : t == 411;
      } else {
         return false;
      }
   }

   static int getCompOperator(ASTree expr) throws CompileError {
      if (expr instanceof Expr) {
         Expr bexpr = (Expr)expr;
         int token = bexpr.getOperator();
         if (token == 33) {
            return 33;
         } else {
            return bexpr instanceof BinExpr && token != 368 && token != 369 && token != 38 && token != 124 ? 358 : token;
         }
      } else {
         return 32;
      }
   }

   private int compileOprands(BinExpr expr) throws CompileError {
      expr.oprand1().accept(this);
      int type1 = this.exprType;
      int dim1 = this.arrayDim;
      expr.oprand2().accept(this);
      if (dim1 != this.arrayDim) {
         if (type1 != 412 && this.exprType != 412) {
            throw new CompileError("incompatible array types");
         }

         if (this.exprType == 412) {
            this.arrayDim = dim1;
         }
      }

      return type1 == 412 ? this.exprType : type1;
   }

   private void compareExpr(boolean branchIf, int token, int type1, BinExpr expr) throws CompileError {
      if (this.arrayDim == 0) {
         this.convertOprandTypes(type1, this.exprType, expr);
      }

      int p = typePrecedence(this.exprType);
      if (p != -1 && this.arrayDim <= 0) {
         int[] op;
         int i;
         if (p == 3) {
            op = ifOp;

            for(i = 0; i < op.length; i += 3) {
               if (op[i] == token) {
                  this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
                  return;
               }
            }

            badTypes(expr);
         } else {
            if (p == 0) {
               if (token != 60 && token != 357) {
                  this.bytecode.addOpcode(151);
               } else {
                  this.bytecode.addOpcode(152);
               }
            } else if (p == 1) {
               if (token != 60 && token != 357) {
                  this.bytecode.addOpcode(149);
               } else {
                  this.bytecode.addOpcode(150);
               }
            } else if (p == 2) {
               this.bytecode.addOpcode(148);
            } else {
               fatal();
            }

            op = ifOp2;

            for(i = 0; i < op.length; i += 3) {
               if (op[i] == token) {
                  this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
                  return;
               }
            }

            badTypes(expr);
         }
      } else if (token == 358) {
         this.bytecode.addOpcode(branchIf ? 165 : 166);
      } else if (token == 350) {
         this.bytecode.addOpcode(branchIf ? 166 : 165);
      } else {
         badTypes(expr);
      }

   }

   protected static void badTypes(Expr expr) throws CompileError {
      throw new CompileError("invalid types for " + expr.getName());
   }

   protected static boolean isRefType(int type) {
      return type == 307 || type == 412;
   }

   private static int typePrecedence(int type) {
      if (type == 312) {
         return 0;
      } else if (type == 317) {
         return 1;
      } else if (type == 326) {
         return 2;
      } else if (isRefType(type)) {
         return -1;
      } else {
         return type == 344 ? -1 : 3;
      }
   }

   static boolean isP_INT(int type) {
      return typePrecedence(type) == 3;
   }

   static boolean rightIsStrong(int type1, int type2) {
      int type1_p = typePrecedence(type1);
      int type2_p = typePrecedence(type2);
      return type1_p >= 0 && type2_p >= 0 && type1_p > type2_p;
   }

   private void convertOprandTypes(int type1, int type2, Expr expr) throws CompileError {
      int type1_p = typePrecedence(type1);
      int type2_p = typePrecedence(type2);
      if (type2_p >= 0 || type1_p >= 0) {
         if (type2_p < 0 || type1_p < 0) {
            badTypes(expr);
         }

         boolean rightStrong;
         int op;
         int result_type;
         if (type1_p <= type2_p) {
            rightStrong = false;
            this.exprType = type1;
            op = castOp[type2_p * 4 + type1_p];
            result_type = type1_p;
         } else {
            rightStrong = true;
            op = castOp[type1_p * 4 + type2_p];
            result_type = type2_p;
         }

         if (rightStrong) {
            if (result_type != 0 && result_type != 2) {
               if (result_type == 1) {
                  if (type1_p == 2) {
                     this.bytecode.addOpcode(91);
                     this.bytecode.addOpcode(87);
                  } else {
                     this.bytecode.addOpcode(95);
                  }

                  this.bytecode.addOpcode(op);
                  this.bytecode.addOpcode(95);
               } else {
                  fatal();
               }
            } else {
               if (type1_p != 0 && type1_p != 2) {
                  this.bytecode.addOpcode(93);
               } else {
                  this.bytecode.addOpcode(94);
               }

               this.bytecode.addOpcode(88);
               this.bytecode.addOpcode(op);
               this.bytecode.addOpcode(94);
               this.bytecode.addOpcode(88);
            }
         } else if (op != 0) {
            this.bytecode.addOpcode(op);
         }

      }
   }

   public void atCastExpr(CastExpr expr) throws CompileError {
      String cname = this.resolveClassName(expr.getClassName());
      String toClass = this.checkCastExpr(expr, cname);
      int srcType = this.exprType;
      this.exprType = expr.getType();
      this.arrayDim = expr.getArrayDim();
      this.className = cname;
      if (toClass == null) {
         this.atNumCastExpr(srcType, this.exprType);
      } else {
         this.bytecode.addCheckcast(toClass);
      }

   }

   public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
      String cname = this.resolveClassName(expr.getClassName());
      String toClass = this.checkCastExpr(expr, cname);
      this.bytecode.addInstanceof(toClass);
      this.exprType = 301;
      this.arrayDim = 0;
   }

   private String checkCastExpr(CastExpr expr, String name) throws CompileError {
      String msg = "invalid cast";
      ASTree oprand = expr.getOprand();
      int dim = expr.getArrayDim();
      int type = expr.getType();
      oprand.accept(this);
      int srcType = this.exprType;
      if (!this.invalidDim(srcType, this.arrayDim, this.className, type, dim, name, true) && srcType != 344 && type != 344) {
         if (type == 307) {
            if (!isRefType(srcType)) {
               throw new CompileError("invalid cast");
            } else {
               return toJvmArrayName(name, dim);
            }
         } else {
            return dim > 0 ? toJvmTypeName(type, dim) : null;
         }
      } else {
         throw new CompileError("invalid cast");
      }
   }

   void atNumCastExpr(int srcType, int destType) throws CompileError {
      if (srcType != destType) {
         int stype = typePrecedence(srcType);
         int dtype = typePrecedence(destType);
         int op;
         if (0 <= stype && stype < 3) {
            op = castOp[stype * 4 + dtype];
         } else {
            op = 0;
         }

         short op2;
         if (destType == 312) {
            op2 = 135;
         } else if (destType == 317) {
            op2 = 134;
         } else if (destType == 326) {
            op2 = 133;
         } else if (destType == 334) {
            op2 = 147;
         } else if (destType == 306) {
            op2 = 146;
         } else if (destType == 303) {
            op2 = 145;
         } else {
            op2 = 0;
         }

         if (op != 0) {
            this.bytecode.addOpcode(op);
         }

         if ((op == 0 || op == 136 || op == 139 || op == 142) && op2 != 0) {
            this.bytecode.addOpcode(op2);
         }

      }
   }

   public void atExpr(Expr expr) throws CompileError {
      int token = expr.getOperator();
      ASTree oprand = expr.oprand1();
      if (token == 46) {
         String member = ((Symbol)expr.oprand2()).get();
         if (member.equals("class")) {
            this.atClassObject(expr);
         } else {
            this.atFieldRead(expr);
         }
      } else if (token == 35) {
         this.atFieldRead(expr);
      } else if (token == 65) {
         this.atArrayRead(oprand, expr.oprand2());
      } else if (token != 362 && token != 363) {
         if (token == 33) {
            if (!this.booleanExpr(false, expr)) {
               this.bytecode.addIndex(7);
               this.bytecode.addIconst(1);
               this.bytecode.addOpcode(167);
               this.bytecode.addIndex(4);
            }

            this.bytecode.addIconst(0);
         } else if (token == 67) {
            fatal();
         } else {
            expr.oprand1().accept(this);
            int type = typePrecedence(this.exprType);
            if (this.arrayDim > 0) {
               badType(expr);
            }

            if (token == 45) {
               if (type == 0) {
                  this.bytecode.addOpcode(119);
               } else if (type == 1) {
                  this.bytecode.addOpcode(118);
               } else if (type == 2) {
                  this.bytecode.addOpcode(117);
               } else if (type == 3) {
                  this.bytecode.addOpcode(116);
                  this.exprType = 324;
               } else {
                  badType(expr);
               }
            } else if (token == 126) {
               if (type == 3) {
                  this.bytecode.addIconst(-1);
                  this.bytecode.addOpcode(130);
                  this.exprType = 324;
               } else if (type == 2) {
                  this.bytecode.addLconst(-1L);
                  this.bytecode.addOpcode(131);
               } else {
                  badType(expr);
               }
            } else if (token == 43) {
               if (type == -1) {
                  badType(expr);
               }
            } else {
               fatal();
            }
         }
      } else {
         this.atPlusPlus(token, oprand, expr, true);
      }

   }

   protected static void badType(Expr expr) throws CompileError {
      throw new CompileError("invalid type for " + expr.getName());
   }

   public abstract void atCallExpr(CallExpr expr) throws CompileError;

   protected abstract void atFieldRead(ASTree expr) throws CompileError;

   public void atClassObject(Expr expr) throws CompileError {
      ASTree op1 = expr.oprand1();
      if (!(op1 instanceof Symbol)) {
         throw new CompileError("fatal error: badly parsed .class expr");
      } else {
         String cname = ((Symbol)op1).get();
         if (cname.startsWith("[")) {
            int i = cname.indexOf("[L");
            if (i >= 0) {
               String name = cname.substring(i + 2, cname.length() - 1);
               String name2 = this.resolveClassName(name);
               if (!name.equals(name2)) {
                  name2 = MemberResolver.jvmToJavaName(name2);
                  StringBuffer sbuf = new StringBuffer();

                  while(i-- >= 0) {
                     sbuf.append('[');
                  }

                  sbuf.append('L').append(name2).append(';');
                  cname = sbuf.toString();
               }
            }
         } else {
            cname = this.resolveClassName(MemberResolver.javaToJvmName(cname));
            cname = MemberResolver.jvmToJavaName(cname);
         }

         this.atClassObject2(cname);
         this.exprType = 307;
         this.arrayDim = 0;
         this.className = "java/lang/Class";
      }
   }

   protected void atClassObject2(String cname) throws CompileError {
      int start = this.bytecode.currentPc();
      this.bytecode.addLdc(cname);
      this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
      int end = this.bytecode.currentPc();
      this.bytecode.addOpcode(167);
      int pc = this.bytecode.currentPc();
      this.bytecode.addIndex(0);
      this.bytecode.addExceptionHandler(start, end, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
      this.bytecode.growStack(1);
      this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
      this.bytecode.addOpcode(191);
      this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
   }

   public void atArrayRead(ASTree array, ASTree index) throws CompileError {
      this.arrayAccess(array, index);
      this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
   }

   protected void arrayAccess(ASTree array, ASTree index) throws CompileError {
      array.accept(this);
      int type = this.exprType;
      int dim = this.arrayDim;
      if (dim == 0) {
         throw new CompileError("bad array access");
      } else {
         String cname = this.className;
         index.accept(this);
         if (typePrecedence(this.exprType) == 3 && this.arrayDim <= 0) {
            this.exprType = type;
            this.arrayDim = dim - 1;
            this.className = cname;
         } else {
            throw new CompileError("bad array index");
         }
      }
   }

   protected static int getArrayReadOp(int type, int dim) {
      if (dim > 0) {
         return 50;
      } else {
         switch(type) {
         case 301:
         case 303:
            return 51;
         case 306:
            return 52;
         case 312:
            return 49;
         case 317:
            return 48;
         case 324:
            return 46;
         case 326:
            return 47;
         case 334:
            return 53;
         default:
            return 50;
         }
      }
   }

   protected static int getArrayWriteOp(int type, int dim) {
      if (dim > 0) {
         return 83;
      } else {
         switch(type) {
         case 301:
         case 303:
            return 84;
         case 306:
            return 85;
         case 312:
            return 82;
         case 317:
            return 81;
         case 324:
            return 79;
         case 326:
            return 80;
         case 334:
            return 86;
         default:
            return 83;
         }
      }
   }

   private void atPlusPlus(int token, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
      boolean isPost = oprand == null;
      if (isPost) {
         oprand = expr.oprand2();
      }

      if (oprand instanceof Variable) {
         Declarator d = ((Variable)oprand).getDeclarator();
         int t = this.exprType = d.getType();
         this.arrayDim = d.getArrayDim();
         int var = this.getLocalVar(d);
         if (this.arrayDim > 0) {
            badType(expr);
         }

         if (t == 312) {
            this.bytecode.addDload(var);
            if (doDup && isPost) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addDconst(1.0D);
            this.bytecode.addOpcode(token == 362 ? 99 : 103);
            if (doDup && !isPost) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addDstore(var);
         } else if (t == 326) {
            this.bytecode.addLload(var);
            if (doDup && isPost) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addLconst(1L);
            this.bytecode.addOpcode(token == 362 ? 97 : 101);
            if (doDup && !isPost) {
               this.bytecode.addOpcode(92);
            }

            this.bytecode.addLstore(var);
         } else if (t == 317) {
            this.bytecode.addFload(var);
            if (doDup && isPost) {
               this.bytecode.addOpcode(89);
            }

            this.bytecode.addFconst(1.0F);
            this.bytecode.addOpcode(token == 362 ? 98 : 102);
            if (doDup && !isPost) {
               this.bytecode.addOpcode(89);
            }

            this.bytecode.addFstore(var);
         } else if (t != 303 && t != 306 && t != 334 && t != 324) {
            badType(expr);
         } else {
            if (doDup && isPost) {
               this.bytecode.addIload(var);
            }

            int delta = token == 362 ? 1 : -1;
            if (var > 255) {
               this.bytecode.addOpcode(196);
               this.bytecode.addOpcode(132);
               this.bytecode.addIndex(var);
               this.bytecode.addIndex(delta);
            } else {
               this.bytecode.addOpcode(132);
               this.bytecode.add(var);
               this.bytecode.add(delta);
            }

            if (doDup && !isPost) {
               this.bytecode.addIload(var);
            }
         }
      } else {
         if (oprand instanceof Expr) {
            Expr e = (Expr)oprand;
            if (e.getOperator() == 65) {
               this.atArrayPlusPlus(token, isPost, e, doDup);
               return;
            }
         }

         this.atFieldPlusPlus(token, isPost, oprand, expr, doDup);
      }

   }

   public void atArrayPlusPlus(int token, boolean isPost, Expr expr, boolean doDup) throws CompileError {
      this.arrayAccess(expr.oprand1(), expr.oprand2());
      int t = this.exprType;
      int dim = this.arrayDim;
      if (dim > 0) {
         badType(expr);
      }

      this.bytecode.addOpcode(92);
      this.bytecode.addOpcode(getArrayReadOp(t, this.arrayDim));
      int dup_code = is2word(t, dim) ? 94 : 91;
      this.atPlusPlusCore(dup_code, doDup, token, isPost, expr);
      this.bytecode.addOpcode(getArrayWriteOp(t, dim));
   }

   protected void atPlusPlusCore(int dup_code, boolean doDup, int token, boolean isPost, Expr expr) throws CompileError {
      int t = this.exprType;
      if (doDup && isPost) {
         this.bytecode.addOpcode(dup_code);
      }

      if (t != 324 && t != 303 && t != 306 && t != 334) {
         if (t == 326) {
            this.bytecode.addLconst(1L);
            this.bytecode.addOpcode(token == 362 ? 97 : 101);
         } else if (t == 317) {
            this.bytecode.addFconst(1.0F);
            this.bytecode.addOpcode(token == 362 ? 98 : 102);
         } else if (t == 312) {
            this.bytecode.addDconst(1.0D);
            this.bytecode.addOpcode(token == 362 ? 99 : 103);
         } else {
            badType(expr);
         }
      } else {
         this.bytecode.addIconst(1);
         this.bytecode.addOpcode(token == 362 ? 96 : 100);
         this.exprType = 324;
      }

      if (doDup && !isPost) {
         this.bytecode.addOpcode(dup_code);
      }

   }

   protected abstract void atFieldPlusPlus(int token, boolean isPost, ASTree oprand, Expr expr, boolean doDup) throws CompileError;

   public abstract void atMember(Member n) throws CompileError;

   public void atVariable(Variable v) throws CompileError {
      Declarator d = v.getDeclarator();
      this.exprType = d.getType();
      this.arrayDim = d.getArrayDim();
      this.className = d.getClassName();
      int var = this.getLocalVar(d);
      if (this.arrayDim > 0) {
         this.bytecode.addAload(var);
      } else {
         switch(this.exprType) {
         case 307:
            this.bytecode.addAload(var);
            break;
         case 312:
            this.bytecode.addDload(var);
            break;
         case 317:
            this.bytecode.addFload(var);
            break;
         case 326:
            this.bytecode.addLload(var);
            break;
         default:
            this.bytecode.addIload(var);
         }
      }

   }

   public void atKeyword(Keyword k) throws CompileError {
      this.arrayDim = 0;
      int token = k.get();
      switch(token) {
      case 336:
      case 339:
         if (this.inStaticMethod) {
            throw new CompileError("not-available: " + (token == 339 ? "this" : "super"));
         }

         this.bytecode.addAload(0);
         this.exprType = 307;
         if (token == 339) {
            this.className = this.getThisName();
         } else {
            this.className = this.getSuperName();
         }
         break;
      case 410:
         this.bytecode.addIconst(1);
         this.exprType = 301;
         break;
      case 411:
         this.bytecode.addIconst(0);
         this.exprType = 301;
         break;
      case 412:
         this.bytecode.addOpcode(1);
         this.exprType = 412;
         break;
      default:
         fatal();
      }

   }

   public void atStringL(StringL s) throws CompileError {
      this.exprType = 307;
      this.arrayDim = 0;
      this.className = "java/lang/String";
      this.bytecode.addLdc(s.get());
   }

   public void atIntConst(IntConst i) throws CompileError {
      this.arrayDim = 0;
      long value = i.get();
      int type = i.getType();
      if (type != 402 && type != 401) {
         this.exprType = 326;
         this.bytecode.addLconst(value);
      } else {
         this.exprType = type == 402 ? 324 : 306;
         this.bytecode.addIconst((int)value);
      }

   }

   public void atDoubleConst(DoubleConst d) throws CompileError {
      this.arrayDim = 0;
      if (d.getType() == 405) {
         this.exprType = 312;
         this.bytecode.addDconst(d.get());
      } else {
         this.exprType = 317;
         this.bytecode.addFconst((float)d.get());
      }

   }

   protected abstract static class ReturnHook {
      CodeGen.ReturnHook next;

      protected abstract boolean doit(Bytecode b, int opcode);

      protected ReturnHook(CodeGen gen) {
         this.next = gen.returnHooks;
         gen.returnHooks = this;
      }

      protected void remove(CodeGen gen) {
         gen.returnHooks = this.next;
      }
   }
}
