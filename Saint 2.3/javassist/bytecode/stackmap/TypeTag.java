package javassist.bytecode.stackmap;

public interface TypeTag {
   String TOP_TYPE = "*top*";
   TypeData TOP = new TypeData.BasicType("*top*", 0);
   TypeData INTEGER = new TypeData.BasicType("int", 1);
   TypeData FLOAT = new TypeData.BasicType("float", 2);
   TypeData DOUBLE = new TypeData.BasicType("double", 3);
   TypeData LONG = new TypeData.BasicType("long", 4);
}
