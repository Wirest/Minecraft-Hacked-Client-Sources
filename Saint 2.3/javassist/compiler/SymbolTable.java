package javassist.compiler;

import java.util.HashMap;
import javassist.compiler.ast.Declarator;

public final class SymbolTable extends HashMap {
   private SymbolTable parent;

   public SymbolTable() {
      this((SymbolTable)null);
   }

   public SymbolTable(SymbolTable p) {
      this.parent = p;
   }

   public SymbolTable getParent() {
      return this.parent;
   }

   public Declarator lookup(String name) {
      Declarator found = (Declarator)this.get(name);
      return found == null && this.parent != null ? this.parent.lookup(name) : found;
   }

   public void append(String name, Declarator value) {
      this.put(name, value);
   }
}
