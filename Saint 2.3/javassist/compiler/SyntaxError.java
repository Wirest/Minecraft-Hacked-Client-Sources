package javassist.compiler;

public class SyntaxError extends CompileError {
   public SyntaxError(Lex lexer) {
      super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
   }
}
