package javassist.bytecode.annotation;

public interface MemberValueVisitor {
   void visitAnnotationMemberValue(AnnotationMemberValue node);

   void visitArrayMemberValue(ArrayMemberValue node);

   void visitBooleanMemberValue(BooleanMemberValue node);

   void visitByteMemberValue(ByteMemberValue node);

   void visitCharMemberValue(CharMemberValue node);

   void visitDoubleMemberValue(DoubleMemberValue node);

   void visitEnumMemberValue(EnumMemberValue node);

   void visitFloatMemberValue(FloatMemberValue node);

   void visitIntegerMemberValue(IntegerMemberValue node);

   void visitLongMemberValue(LongMemberValue node);

   void visitShortMemberValue(ShortMemberValue node);

   void visitStringMemberValue(StringMemberValue node);

   void visitClassMemberValue(ClassMemberValue node);
}
