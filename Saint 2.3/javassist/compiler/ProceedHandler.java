package javassist.compiler;

import javassist.bytecode.Bytecode;
import javassist.compiler.ast.ASTList;

public interface ProceedHandler {
   void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError;

   void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError;
}
