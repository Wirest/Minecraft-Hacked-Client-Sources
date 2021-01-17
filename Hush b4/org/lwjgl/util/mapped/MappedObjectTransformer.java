// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.lwjgl.BufferUtils;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import java.nio.Buffer;
import org.lwjgl.MemoryUtil;
import java.util.Iterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.lwjgl.LWJGLUtil;
import java.nio.ByteBuffer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.lang.reflect.Modifier;
import java.util.Map;

public class MappedObjectTransformer
{
    static final boolean PRINT_ACTIVITY;
    static final boolean PRINT_TIMING;
    static final boolean PRINT_BYTECODE;
    static final Map<String, MappedSubtypeInfo> className_to_subtype;
    static final String MAPPED_OBJECT_JVM;
    static final String MAPPED_HELPER_JVM;
    static final String MAPPEDSET_PREFIX;
    static final String MAPPED_SET2_JVM;
    static final String MAPPED_SET3_JVM;
    static final String MAPPED_SET4_JVM;
    static final String CACHE_LINE_PAD_JVM;
    static final String VIEWADDRESS_METHOD_NAME = "getViewAddress";
    static final String NEXT_METHOD_NAME = "next";
    static final String ALIGN_METHOD_NAME = "getAlign";
    static final String SIZEOF_METHOD_NAME = "getSizeof";
    static final String CAPACITY_METHOD_NAME = "capacity";
    static final String VIEW_CONSTRUCTOR_NAME = "constructView$LWJGL";
    static final Map<Integer, String> OPCODE_TO_NAME;
    static final Map<Integer, String> INSNTYPE_TO_NAME;
    static boolean is_currently_computing_frames;
    
    public static void register(final Class<? extends MappedObject> type) {
        if (MappedObjectClassLoader.FORKED) {
            return;
        }
        final MappedType mapped = type.getAnnotation(MappedType.class);
        if (mapped != null && mapped.padding() < 0) {
            throw new ClassFormatError("Invalid mapped type padding: " + mapped.padding());
        }
        if (type.getEnclosingClass() != null && !Modifier.isStatic(type.getModifiers())) {
            throw new InternalError("only top-level or static inner classes are allowed");
        }
        final String className = jvmClassName(type);
        final Map<String, FieldInfo> fields = new HashMap<String, FieldInfo>();
        long sizeof = 0L;
        for (final Field field : type.getDeclaredFields()) {
            final FieldInfo fieldInfo = registerField(mapped == null || mapped.autoGenerateOffsets(), className, sizeof, field);
            if (fieldInfo != null) {
                fields.put(field.getName(), fieldInfo);
                sizeof = Math.max(sizeof, fieldInfo.offset + fieldInfo.lengthPadded);
            }
        }
        int align = 4;
        int padding = 0;
        boolean cacheLinePadded = false;
        if (mapped != null) {
            align = mapped.align();
            if (mapped.cacheLinePadding()) {
                if (mapped.padding() != 0) {
                    throw new ClassFormatError("Mapped type padding cannot be specified together with cacheLinePadding.");
                }
                final int cacheLineMod = (int)(sizeof % CacheUtil.getCacheLineSize());
                if (cacheLineMod != 0) {
                    padding = CacheUtil.getCacheLineSize() - cacheLineMod;
                }
                cacheLinePadded = true;
            }
            else {
                padding = mapped.padding();
            }
        }
        sizeof += padding;
        final MappedSubtypeInfo mappedType = new MappedSubtypeInfo(className, fields, (int)sizeof, align, padding, cacheLinePadded);
        if (MappedObjectTransformer.className_to_subtype.put(className, mappedType) != null) {
            throw new InternalError("duplicate mapped type: " + mappedType.className);
        }
    }
    
    private static FieldInfo registerField(final boolean autoGenerateOffsets, final String className, final long advancingOffset, final Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return null;
        }
        if (!field.getType().isPrimitive() && field.getType() != ByteBuffer.class) {
            throw new ClassFormatError("field '" + className + "." + field.getName() + "' not supported: " + field.getType());
        }
        final MappedField meta = field.getAnnotation(MappedField.class);
        if (meta == null && !autoGenerateOffsets) {
            throw new ClassFormatError("field '" + className + "." + field.getName() + "' missing annotation " + MappedField.class.getName() + ": " + className);
        }
        final Pointer pointer = field.getAnnotation(Pointer.class);
        if (pointer != null && field.getType() != Long.TYPE) {
            throw new ClassFormatError("The @Pointer annotation can only be used on long fields. @Pointer field found: " + className + "." + field.getName() + ": " + field.getType());
        }
        if (Modifier.isVolatile(field.getModifiers()) && (pointer != null || field.getType() == ByteBuffer.class)) {
            throw new ClassFormatError("The volatile keyword is not supported for @Pointer or ByteBuffer fields. Volatile field found: " + className + "." + field.getName() + ": " + field.getType());
        }
        long byteLength;
        if (field.getType() == Long.TYPE || field.getType() == Double.TYPE) {
            if (pointer == null) {
                byteLength = 8L;
            }
            else {
                byteLength = MappedObjectUnsafe.INSTANCE.addressSize();
            }
        }
        else if (field.getType() == Double.TYPE) {
            byteLength = 8L;
        }
        else if (field.getType() == Integer.TYPE || field.getType() == Float.TYPE) {
            byteLength = 4L;
        }
        else if (field.getType() == Character.TYPE || field.getType() == Short.TYPE) {
            byteLength = 2L;
        }
        else if (field.getType() == Byte.TYPE) {
            byteLength = 1L;
        }
        else {
            if (field.getType() != ByteBuffer.class) {
                throw new ClassFormatError(field.getType().getName());
            }
            byteLength = meta.byteLength();
            if (byteLength < 0L) {
                throw new IllegalStateException("invalid byte length for mapped ByteBuffer field: " + className + "." + field.getName() + " [length=" + byteLength + "]");
            }
        }
        if (field.getType() != ByteBuffer.class && advancingOffset % byteLength != 0L) {
            throw new IllegalStateException("misaligned mapped type: " + className + "." + field.getName());
        }
        final CacheLinePad pad = field.getAnnotation(CacheLinePad.class);
        long byteOffset = advancingOffset;
        if (meta != null && meta.byteOffset() != -1L) {
            if (meta.byteOffset() < 0L) {
                throw new ClassFormatError("Invalid field byte offset: " + className + "." + field.getName() + " [byteOffset=" + meta.byteOffset() + "]");
            }
            if (pad != null) {
                throw new ClassFormatError("A field byte offset cannot be specified together with cache-line padding: " + className + "." + field.getName());
            }
            byteOffset = meta.byteOffset();
        }
        long byteLengthPadded = byteLength;
        if (pad != null) {
            if (pad.before() && byteOffset % CacheUtil.getCacheLineSize() != 0L) {
                byteOffset += CacheUtil.getCacheLineSize() - (byteOffset & (long)(CacheUtil.getCacheLineSize() - 1));
            }
            if (pad.after() && (byteOffset + byteLength) % CacheUtil.getCacheLineSize() != 0L) {
                byteLengthPadded += CacheUtil.getCacheLineSize() - (byteOffset + byteLength) % CacheUtil.getCacheLineSize();
            }
            assert byteOffset % CacheUtil.getCacheLineSize() == 0L;
            assert (byteOffset + byteLengthPadded) % CacheUtil.getCacheLineSize() == 0L;
        }
        if (MappedObjectTransformer.PRINT_ACTIVITY) {
            LWJGLUtil.log(MappedObjectTransformer.class.getSimpleName() + ": " + className + "." + field.getName() + " [type=" + field.getType().getSimpleName() + ", offset=" + byteOffset + "]");
        }
        return new FieldInfo(byteOffset, byteLength, byteLengthPadded, Type.getType((Class)field.getType()), Modifier.isVolatile(field.getModifiers()), pointer != null);
    }
    
    static byte[] transformMappedObject(final byte[] bytecode) {
        final ClassWriter cw = new ClassWriter(0);
        final ClassVisitor cv = (ClassVisitor)new ClassAdapter(cw) {
            private final String[] DEFINALIZE_LIST = { "getViewAddress", "next", "getAlign", "getSizeof", "capacity" };
            
            public MethodVisitor visitMethod(int access, final String name, final String desc, final String signature, final String[] exceptions) {
                for (final String method : this.DEFINALIZE_LIST) {
                    if (name.equals(method)) {
                        access &= 0xFFFFFFEF;
                        break;
                    }
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
        new ClassReader(bytecode).accept(cv, 0);
        return cw.toByteArray();
    }
    
    static byte[] transformMappedAPI(final String className, byte[] bytecode) {
        final ClassWriter cw = new ClassWriter(2) {
            protected String getCommonSuperClass(final String a, final String b) {
                if ((MappedObjectTransformer.is_currently_computing_frames && !a.startsWith("java/")) || !b.startsWith("java/")) {
                    return "java/lang/Object";
                }
                return super.getCommonSuperClass(a, b);
            }
        };
        ClassVisitor cv;
        final TransformationAdapter ta = (TransformationAdapter)(cv = (ClassVisitor)new TransformationAdapter((ClassVisitor)cw, className));
        if (MappedObjectTransformer.className_to_subtype.containsKey(className)) {
            cv = (ClassVisitor)getMethodGenAdapter(className, cv);
        }
        new ClassReader(bytecode).accept(cv, 4);
        if (!ta.transformed) {
            return bytecode;
        }
        bytecode = cw.toByteArray();
        if (MappedObjectTransformer.PRINT_BYTECODE) {
            printBytecode(bytecode);
        }
        return bytecode;
    }
    
    private static ClassAdapter getMethodGenAdapter(final String className, final ClassVisitor cv) {
        return new ClassAdapter(cv) {
            public void visitEnd() {
                final MappedSubtypeInfo mappedSubtype = MappedObjectTransformer.className_to_subtype.get(className);
                this.generateViewAddressGetter();
                this.generateCapacity();
                this.generateAlignGetter(mappedSubtype);
                this.generateSizeofGetter();
                this.generateNext();
                for (final String fieldName : mappedSubtype.fields.keySet()) {
                    final FieldInfo field = mappedSubtype.fields.get(fieldName);
                    if (field.type.getDescriptor().length() > 1) {
                        this.generateByteBufferGetter(fieldName, field);
                    }
                    else {
                        this.generateFieldGetter(fieldName, field);
                        this.generateFieldSetter(fieldName, field);
                    }
                }
                super.visitEnd();
            }
            
            private void generateViewAddressGetter() {
                final MethodVisitor mv = super.visitMethod(1, "getViewAddress", "(I)J", (String)null, (String[])null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "baseAddress", "J");
                mv.visitVarInsn(21, 1);
                mv.visitFieldInsn(178, className, "SIZEOF", "I");
                mv.visitInsn(104);
                mv.visitInsn(133);
                mv.visitInsn(97);
                if (MappedObject.CHECKS) {
                    mv.visitInsn(92);
                    mv.visitVarInsn(25, 0);
                    mv.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "checkAddress", "(JL" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";)V");
                }
                mv.visitInsn(173);
                mv.visitMaxs(3, 2);
                mv.visitEnd();
            }
            
            private void generateCapacity() {
                final MethodVisitor mv = super.visitMethod(1, "capacity", "()I", (String)null, (String[])null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitMethodInsn(182, MappedObjectTransformer.MAPPED_OBJECT_JVM, "backingByteBuffer", "()L" + MappedObjectTransformer.jvmClassName(ByteBuffer.class) + ";");
                mv.visitInsn(89);
                mv.visitMethodInsn(182, MappedObjectTransformer.jvmClassName(ByteBuffer.class), "capacity", "()I");
                mv.visitInsn(95);
                mv.visitMethodInsn(184, MappedObjectTransformer.jvmClassName(MemoryUtil.class), "getAddress0", "(L" + MappedObjectTransformer.jvmClassName(Buffer.class) + ";)J");
                mv.visitVarInsn(25, 0);
                mv.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "baseAddress", "J");
                mv.visitInsn(101);
                mv.visitInsn(136);
                mv.visitInsn(96);
                mv.visitFieldInsn(178, className, "SIZEOF", "I");
                mv.visitInsn(108);
                mv.visitInsn(172);
                mv.visitMaxs(3, 1);
                mv.visitEnd();
            }
            
            private void generateAlignGetter(final MappedSubtypeInfo mappedSubtype) {
                final MethodVisitor mv = super.visitMethod(1, "getAlign", "()I", (String)null, (String[])null);
                mv.visitCode();
                MappedObjectTransformer.visitIntNode(mv, mappedSubtype.sizeof);
                mv.visitInsn(172);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            
            private void generateSizeofGetter() {
                final MethodVisitor mv = super.visitMethod(1, "getSizeof", "()I", (String)null, (String[])null);
                mv.visitCode();
                mv.visitFieldInsn(178, className, "SIZEOF", "I");
                mv.visitInsn(172);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            
            private void generateNext() {
                final MethodVisitor mv = super.visitMethod(1, "next", "()V", (String)null, (String[])null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitInsn(89);
                mv.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "viewAddress", "J");
                mv.visitFieldInsn(178, className, "SIZEOF", "I");
                mv.visitInsn(133);
                mv.visitInsn(97);
                mv.visitMethodInsn(182, className, "setViewAddress", "(J)V");
                mv.visitInsn(177);
                mv.visitMaxs(3, 1);
                mv.visitEnd();
            }
            
            private void generateByteBufferGetter(final String fieldName, final FieldInfo field) {
                final MethodVisitor mv = super.visitMethod(9, MappedObjectTransformer.getterName(fieldName), "(L" + className + ";I)" + field.type.getDescriptor(), (String)null, (String[])null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(21, 1);
                mv.visitMethodInsn(182, className, "getViewAddress", "(I)J");
                MappedObjectTransformer.visitIntNode(mv, (int)field.offset);
                mv.visitInsn(133);
                mv.visitInsn(97);
                MappedObjectTransformer.visitIntNode(mv, (int)field.length);
                mv.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + MappedObjectTransformer.jvmClassName(ByteBuffer.class) + ";");
                mv.visitInsn(176);
                mv.visitMaxs(3, 2);
                mv.visitEnd();
            }
            
            private void generateFieldGetter(final String fieldName, final FieldInfo field) {
                final MethodVisitor mv = super.visitMethod(9, MappedObjectTransformer.getterName(fieldName), "(L" + className + ";I)" + field.type.getDescriptor(), (String)null, (String[])null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(21, 1);
                mv.visitMethodInsn(182, className, "getViewAddress", "(I)J");
                MappedObjectTransformer.visitIntNode(mv, (int)field.offset);
                mv.visitInsn(133);
                mv.visitInsn(97);
                mv.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, field.getAccessType() + "get", "(J)" + field.type.getDescriptor());
                mv.visitInsn(field.type.getOpcode(172));
                mv.visitMaxs(3, 2);
                mv.visitEnd();
            }
            
            private void generateFieldSetter(final String fieldName, final FieldInfo field) {
                final MethodVisitor mv = super.visitMethod(9, MappedObjectTransformer.setterName(fieldName), "(L" + className + ";I" + field.type.getDescriptor() + ")V", (String)null, (String[])null);
                mv.visitCode();
                int load = 0;
                switch (field.type.getSort()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5: {
                        load = 21;
                        break;
                    }
                    case 6: {
                        load = 23;
                        break;
                    }
                    case 7: {
                        load = 22;
                        break;
                    }
                    case 8: {
                        load = 24;
                        break;
                    }
                }
                mv.visitVarInsn(load, 2);
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(21, 1);
                mv.visitMethodInsn(182, className, "getViewAddress", "(I)J");
                MappedObjectTransformer.visitIntNode(mv, (int)field.offset);
                mv.visitInsn(133);
                mv.visitInsn(97);
                mv.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, field.getAccessType() + "put", "(" + field.type.getDescriptor() + "J)V");
                mv.visitInsn(177);
                mv.visitMaxs(4, 4);
                mv.visitEnd();
            }
        };
    }
    
    static int transformMethodCall(final InsnList instructions, int i, final Map<AbstractInsnNode, Frame<BasicValue>> frameMap, final MethodInsnNode methodInsn, final MappedSubtypeInfo mappedType, final Map<Integer, MappedSubtypeInfo> arrayVars) {
        switch (methodInsn.getOpcode()) {
            case 182: {
                if ("asArray".equals(methodInsn.name) && methodInsn.desc.equals("()[L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";")) {
                    AbstractInsnNode nextInstruction;
                    checkInsnAfterIsArray(nextInstruction = methodInsn.getNext(), 192);
                    checkInsnAfterIsArray(nextInstruction = nextInstruction.getNext(), 58);
                    final Frame<BasicValue> frame = frameMap.get(nextInstruction);
                    final String targetType = ((BasicValue)frame.getStack(frame.getStackSize() - 1)).getType().getElementType().getInternalName();
                    if (!methodInsn.owner.equals(targetType)) {
                        throw new ClassCastException("Source: " + methodInsn.owner + " - Target: " + targetType);
                    }
                    final VarInsnNode varInstruction = (VarInsnNode)nextInstruction;
                    arrayVars.put(varInstruction.var, mappedType);
                    instructions.remove(methodInsn.getNext());
                    instructions.remove((AbstractInsnNode)methodInsn);
                }
                if ("dup".equals(methodInsn.name) && methodInsn.desc.equals("()L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";")) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateDupInstructions(methodInsn));
                    break;
                }
                if ("slice".equals(methodInsn.name) && methodInsn.desc.equals("()L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";")) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateSliceInstructions(methodInsn));
                    break;
                }
                if ("runViewConstructor".equals(methodInsn.name) && "()V".equals(methodInsn.desc)) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateRunViewConstructorInstructions(methodInsn));
                    break;
                }
                if ("copyTo".equals(methodInsn.name) && methodInsn.desc.equals("(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";)V")) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateCopyToInstructions(mappedType));
                    break;
                }
                if ("copyRange".equals(methodInsn.name) && methodInsn.desc.equals("(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)V")) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateCopyRangeInstructions(mappedType));
                    break;
                }
                break;
            }
            case 183: {
                if (methodInsn.owner.equals(MappedObjectTransformer.MAPPED_OBJECT_JVM) && "<init>".equals(methodInsn.name) && "()V".equals(methodInsn.desc)) {
                    instructions.remove(methodInsn.getPrevious());
                    instructions.remove((AbstractInsnNode)methodInsn);
                    i -= 2;
                    break;
                }
                break;
            }
            case 184: {
                final boolean isMapDirectMethod = "map".equals(methodInsn.name) && methodInsn.desc.equals("(JI)L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";");
                final boolean isMapBufferMethod = "map".equals(methodInsn.name) && methodInsn.desc.equals("(Ljava/nio/ByteBuffer;)L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";");
                final boolean isMallocMethod = "malloc".equals(methodInsn.name) && methodInsn.desc.equals("(I)L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";");
                if (isMapDirectMethod || isMapBufferMethod || isMallocMethod) {
                    i = replace(instructions, i, (AbstractInsnNode)methodInsn, generateMapInstructions(mappedType, methodInsn.owner, isMapDirectMethod, isMallocMethod));
                    break;
                }
                break;
            }
        }
        return i;
    }
    
    private static InsnList generateCopyRangeInstructions(final MappedSubtypeInfo mappedType) {
        final InsnList list = new InsnList();
        list.add(getIntNode(mappedType.sizeof));
        list.add((AbstractInsnNode)new InsnNode(104));
        list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "copy", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)V"));
        return list;
    }
    
    private static InsnList generateCopyToInstructions(final MappedSubtypeInfo mappedType) {
        final InsnList list = new InsnList();
        list.add(getIntNode(mappedType.sizeof - mappedType.padding));
        list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "copy", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)V"));
        return list;
    }
    
    private static InsnList generateRunViewConstructorInstructions(final MethodInsnNode methodInsn) {
        final InsnList list = new InsnList();
        list.add((AbstractInsnNode)new InsnNode(89));
        list.add((AbstractInsnNode)new MethodInsnNode(182, methodInsn.owner, "constructView$LWJGL", "()V"));
        return list;
    }
    
    private static InsnList generateSliceInstructions(final MethodInsnNode methodInsn) {
        final InsnList list = new InsnList();
        list.add((AbstractInsnNode)new TypeInsnNode(187, methodInsn.owner));
        list.add((AbstractInsnNode)new InsnNode(89));
        list.add((AbstractInsnNode)new MethodInsnNode(183, methodInsn.owner, "<init>", "()V"));
        list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "slice", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";)L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";"));
        return list;
    }
    
    private static InsnList generateDupInstructions(final MethodInsnNode methodInsn) {
        final InsnList list = new InsnList();
        list.add((AbstractInsnNode)new TypeInsnNode(187, methodInsn.owner));
        list.add((AbstractInsnNode)new InsnNode(89));
        list.add((AbstractInsnNode)new MethodInsnNode(183, methodInsn.owner, "<init>", "()V"));
        list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "dup", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";)L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";"));
        return list;
    }
    
    private static InsnList generateMapInstructions(final MappedSubtypeInfo mappedType, final String className, final boolean mapDirectMethod, final boolean mallocMethod) {
        final InsnList trg = new InsnList();
        if (mallocMethod) {
            trg.add(getIntNode(mappedType.sizeof));
            trg.add((AbstractInsnNode)new InsnNode(104));
            trg.add((AbstractInsnNode)new MethodInsnNode(184, mappedType.cacheLinePadded ? jvmClassName(CacheUtil.class) : jvmClassName(BufferUtils.class), "createByteBuffer", "(I)L" + jvmClassName(ByteBuffer.class) + ";"));
        }
        else if (mapDirectMethod) {
            trg.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + jvmClassName(ByteBuffer.class) + ";"));
        }
        trg.add((AbstractInsnNode)new TypeInsnNode(187, className));
        trg.add((AbstractInsnNode)new InsnNode(89));
        trg.add((AbstractInsnNode)new MethodInsnNode(183, className, "<init>", "()V"));
        trg.add((AbstractInsnNode)new InsnNode(90));
        trg.add((AbstractInsnNode)new InsnNode(95));
        trg.add(getIntNode(mappedType.align));
        trg.add(getIntNode(mappedType.sizeof));
        trg.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "setup", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";Ljava/nio/ByteBuffer;II)V"));
        return trg;
    }
    
    static InsnList transformFieldAccess(final FieldInsnNode fieldInsn) {
        final MappedSubtypeInfo mappedSubtype = MappedObjectTransformer.className_to_subtype.get(fieldInsn.owner);
        if (mappedSubtype == null) {
            if ("view".equals(fieldInsn.name) && fieldInsn.owner.startsWith(MappedObjectTransformer.MAPPEDSET_PREFIX)) {
                return generateSetViewInstructions(fieldInsn);
            }
            return null;
        }
        else {
            if ("SIZEOF".equals(fieldInsn.name)) {
                return generateSIZEOFInstructions(fieldInsn, mappedSubtype);
            }
            if ("view".equals(fieldInsn.name)) {
                return generateViewInstructions(fieldInsn, mappedSubtype);
            }
            if ("baseAddress".equals(fieldInsn.name) || "viewAddress".equals(fieldInsn.name)) {
                return generateAddressInstructions(fieldInsn);
            }
            final FieldInfo field = mappedSubtype.fields.get(fieldInsn.name);
            if (field == null) {
                return null;
            }
            if (fieldInsn.desc.equals("L" + jvmClassName(ByteBuffer.class) + ";")) {
                return generateByteBufferInstructions(fieldInsn, mappedSubtype, field.offset);
            }
            return generateFieldInstructions(fieldInsn, field);
        }
    }
    
    private static InsnList generateSetViewInstructions(final FieldInsnNode fieldInsn) {
        if (fieldInsn.getOpcode() == 180) {
            throwAccessErrorOnReadOnlyField(fieldInsn.owner, fieldInsn.name);
        }
        if (fieldInsn.getOpcode() != 181) {
            throw new InternalError();
        }
        final InsnList list = new InsnList();
        if (MappedObjectTransformer.MAPPED_SET2_JVM.equals(fieldInsn.owner)) {
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "put_views", "(L" + MappedObjectTransformer.MAPPED_SET2_JVM + ";I)V"));
        }
        else if (MappedObjectTransformer.MAPPED_SET3_JVM.equals(fieldInsn.owner)) {
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "put_views", "(L" + MappedObjectTransformer.MAPPED_SET3_JVM + ";I)V"));
        }
        else {
            if (!MappedObjectTransformer.MAPPED_SET4_JVM.equals(fieldInsn.owner)) {
                throw new InternalError();
            }
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "put_views", "(L" + MappedObjectTransformer.MAPPED_SET4_JVM + ";I)V"));
        }
        return list;
    }
    
    private static InsnList generateSIZEOFInstructions(final FieldInsnNode fieldInsn, final MappedSubtypeInfo mappedSubtype) {
        if (!"I".equals(fieldInsn.desc)) {
            throw new InternalError();
        }
        final InsnList list = new InsnList();
        if (fieldInsn.getOpcode() == 178) {
            list.add(getIntNode(mappedSubtype.sizeof));
            return list;
        }
        if (fieldInsn.getOpcode() == 179) {
            throwAccessErrorOnReadOnlyField(fieldInsn.owner, fieldInsn.name);
        }
        throw new InternalError();
    }
    
    private static InsnList generateViewInstructions(final FieldInsnNode fieldInsn, final MappedSubtypeInfo mappedSubtype) {
        if (!"I".equals(fieldInsn.desc)) {
            throw new InternalError();
        }
        final InsnList list = new InsnList();
        if (fieldInsn.getOpcode() == 180) {
            if (mappedSubtype.sizeof_shift != 0) {
                list.add(getIntNode(mappedSubtype.sizeof_shift));
                list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "get_view_shift", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)I"));
            }
            else {
                list.add(getIntNode(mappedSubtype.sizeof));
                list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "get_view", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)I"));
            }
            return list;
        }
        if (fieldInsn.getOpcode() == 181) {
            if (mappedSubtype.sizeof_shift != 0) {
                list.add(getIntNode(mappedSubtype.sizeof_shift));
                list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "put_view_shift", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";II)V"));
            }
            else {
                list.add(getIntNode(mappedSubtype.sizeof));
                list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "put_view", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";II)V"));
            }
            return list;
        }
        throw new InternalError();
    }
    
    private static InsnList generateAddressInstructions(final FieldInsnNode fieldInsn) {
        if (!"J".equals(fieldInsn.desc)) {
            throw new IllegalStateException();
        }
        if (fieldInsn.getOpcode() == 180) {
            return null;
        }
        if (fieldInsn.getOpcode() == 181) {
            throwAccessErrorOnReadOnlyField(fieldInsn.owner, fieldInsn.name);
        }
        throw new InternalError();
    }
    
    private static InsnList generateByteBufferInstructions(final FieldInsnNode fieldInsn, final MappedSubtypeInfo mappedSubtype, final long fieldOffset) {
        if (fieldInsn.getOpcode() == 181) {
            throwAccessErrorOnReadOnlyField(fieldInsn.owner, fieldInsn.name);
        }
        if (fieldInsn.getOpcode() == 180) {
            final InsnList list = new InsnList();
            list.add((AbstractInsnNode)new FieldInsnNode(180, mappedSubtype.className, "viewAddress", "J"));
            list.add((AbstractInsnNode)new LdcInsnNode((Object)fieldOffset));
            list.add((AbstractInsnNode)new InsnNode(97));
            list.add((AbstractInsnNode)new LdcInsnNode((Object)mappedSubtype.fields.get(fieldInsn.name).length));
            list.add((AbstractInsnNode)new InsnNode(136));
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + jvmClassName(ByteBuffer.class) + ";"));
            return list;
        }
        throw new InternalError();
    }
    
    private static InsnList generateFieldInstructions(final FieldInsnNode fieldInsn, final FieldInfo field) {
        final InsnList list = new InsnList();
        if (fieldInsn.getOpcode() == 181) {
            list.add(getIntNode((int)field.offset));
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, field.getAccessType() + "put", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";" + fieldInsn.desc + "I)V"));
            return list;
        }
        if (fieldInsn.getOpcode() == 180) {
            list.add(getIntNode((int)field.offset));
            list.add((AbstractInsnNode)new MethodInsnNode(184, MappedObjectTransformer.MAPPED_HELPER_JVM, field.getAccessType() + "get", "(L" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";I)" + fieldInsn.desc));
            return list;
        }
        throw new InternalError();
    }
    
    static int transformArrayAccess(final InsnList instructions, final int i, final Map<AbstractInsnNode, Frame<BasicValue>> frameMap, final VarInsnNode loadInsn, final MappedSubtypeInfo mappedSubtype, final int var) {
        final int loadStackSize = frameMap.get(loadInsn).getStackSize() + 1;
        AbstractInsnNode nextInsn = (AbstractInsnNode)loadInsn;
        while (true) {
            nextInsn = nextInsn.getNext();
            if (nextInsn == null) {
                throw new InternalError();
            }
            Frame<BasicValue> frame = frameMap.get(nextInsn);
            if (frame == null) {
                continue;
            }
            int stackSize = frame.getStackSize();
            if (stackSize == loadStackSize + 1 && nextInsn.getOpcode() == 50) {
                final AbstractInsnNode aaLoadInsn = nextInsn;
                while (true) {
                    nextInsn = nextInsn.getNext();
                    if (nextInsn == null) {
                        break;
                    }
                    frame = frameMap.get(nextInsn);
                    if (frame == null) {
                        continue;
                    }
                    stackSize = frame.getStackSize();
                    if (stackSize == loadStackSize + 1 && nextInsn.getOpcode() == 181) {
                        final FieldInsnNode fieldInsn = (FieldInsnNode)nextInsn;
                        instructions.insert(nextInsn, (AbstractInsnNode)new MethodInsnNode(184, mappedSubtype.className, setterName(fieldInsn.name), "(L" + mappedSubtype.className + ";I" + fieldInsn.desc + ")V"));
                        instructions.remove(nextInsn);
                        break;
                    }
                    if (stackSize == loadStackSize && nextInsn.getOpcode() == 180) {
                        final FieldInsnNode fieldInsn = (FieldInsnNode)nextInsn;
                        instructions.insert(nextInsn, (AbstractInsnNode)new MethodInsnNode(184, mappedSubtype.className, getterName(fieldInsn.name), "(L" + mappedSubtype.className + ";I)" + fieldInsn.desc));
                        instructions.remove(nextInsn);
                        break;
                    }
                    if (stackSize == loadStackSize && nextInsn.getOpcode() == 89 && nextInsn.getNext().getOpcode() == 180) {
                        final FieldInsnNode fieldInsn = (FieldInsnNode)nextInsn.getNext();
                        final MethodInsnNode getter = new MethodInsnNode(184, mappedSubtype.className, getterName(fieldInsn.name), "(L" + mappedSubtype.className + ";I)" + fieldInsn.desc);
                        instructions.insert(nextInsn, (AbstractInsnNode)new InsnNode(92));
                        instructions.insert(nextInsn.getNext(), (AbstractInsnNode)getter);
                        instructions.remove(nextInsn);
                        instructions.remove((AbstractInsnNode)fieldInsn);
                        nextInsn = (AbstractInsnNode)getter;
                    }
                    else {
                        if (stackSize < loadStackSize) {
                            throw new ClassFormatError("Invalid " + mappedSubtype.className + " view array usage detected: " + getOpcodeName(nextInsn));
                        }
                        continue;
                    }
                }
                instructions.remove(aaLoadInsn);
                return i;
            }
            if (stackSize == loadStackSize && nextInsn.getOpcode() == 190) {
                if (LWJGLUtil.DEBUG && loadInsn.getNext() != nextInsn) {
                    throw new InternalError();
                }
                instructions.remove(nextInsn);
                loadInsn.var = var;
                instructions.insert((AbstractInsnNode)loadInsn, (AbstractInsnNode)new MethodInsnNode(182, mappedSubtype.className, "capacity", "()I"));
                return i + 1;
            }
            else {
                if (stackSize < loadStackSize) {
                    throw new ClassFormatError("Invalid " + mappedSubtype.className + " view array usage detected: " + getOpcodeName(nextInsn));
                }
                continue;
            }
        }
    }
    
    private static void getClassEnums(final Class clazz, final Map<Integer, String> map, final String... prefixFilters) {
        try {
            for (final Field field : clazz.getFields()) {
                Label_0128: {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (field.getType() == Integer.TYPE) {
                            for (final String filter : prefixFilters) {
                                if (field.getName().startsWith(filter)) {
                                    break Label_0128;
                                }
                            }
                            if (map.put((Integer)field.get(null), field.getName()) != null) {
                                throw new IllegalStateException();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static String getOpcodeName(final AbstractInsnNode insn) {
        final String op = MappedObjectTransformer.OPCODE_TO_NAME.get(insn.getOpcode());
        return MappedObjectTransformer.INSNTYPE_TO_NAME.get(insn.getType()) + ": " + insn.getOpcode() + ((op == null) ? "" : (" [" + MappedObjectTransformer.OPCODE_TO_NAME.get(insn.getOpcode()) + "]"));
    }
    
    static String jvmClassName(final Class<?> type) {
        return type.getName().replace('.', '/');
    }
    
    static String getterName(final String fieldName) {
        return "get$" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1) + "$LWJGL";
    }
    
    static String setterName(final String fieldName) {
        return "set$" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1) + "$LWJGL";
    }
    
    private static void checkInsnAfterIsArray(final AbstractInsnNode instruction, final int opcode) {
        if (instruction == null) {
            throw new ClassFormatError("Unexpected end of instructions after .asArray() method.");
        }
        if (instruction.getOpcode() != opcode) {
            throw new ClassFormatError("The result of .asArray() must be stored to a local variable. Found: " + getOpcodeName(instruction));
        }
    }
    
    static AbstractInsnNode getIntNode(final int value) {
        if (value <= 5 && -1 <= value) {
            return (AbstractInsnNode)new InsnNode(2 + value + 1);
        }
        if (value >= -128 && value <= 127) {
            return (AbstractInsnNode)new IntInsnNode(16, value);
        }
        if (value >= -32768 && value <= 32767) {
            return (AbstractInsnNode)new IntInsnNode(17, value);
        }
        return (AbstractInsnNode)new LdcInsnNode((Object)value);
    }
    
    static void visitIntNode(final MethodVisitor mv, final int value) {
        if (value <= 5 && -1 <= value) {
            mv.visitInsn(2 + value + 1);
        }
        else if (value >= -128 && value <= 127) {
            mv.visitIntInsn(16, value);
        }
        else if (value >= -32768 && value <= 32767) {
            mv.visitIntInsn(17, value);
        }
        else {
            mv.visitLdcInsn((Object)value);
        }
    }
    
    static int replace(final InsnList instructions, final int i, final AbstractInsnNode location, final InsnList list) {
        final int size = list.size();
        instructions.insert(location, list);
        instructions.remove(location);
        return i + (size - 1);
    }
    
    private static void throwAccessErrorOnReadOnlyField(final String className, final String fieldName) {
        throw new IllegalAccessError("The " + className + "." + fieldName + " field is final.");
    }
    
    private static void printBytecode(final byte[] bytecode) {
        final StringWriter sw = new StringWriter();
        final ClassVisitor tracer = (ClassVisitor)new TraceClassVisitor((ClassVisitor)new ClassWriter(0), new PrintWriter(sw));
        new ClassReader(bytecode).accept(tracer, 0);
        final String dump = sw.toString();
        LWJGLUtil.log(dump);
    }
    
    static {
        PRINT_ACTIVITY = (LWJGLUtil.DEBUG && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintActivity"));
        PRINT_TIMING = (MappedObjectTransformer.PRINT_ACTIVITY && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintTiming"));
        PRINT_BYTECODE = (LWJGLUtil.DEBUG && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintBytecode"));
        MAPPED_OBJECT_JVM = jvmClassName(MappedObject.class);
        MAPPED_HELPER_JVM = jvmClassName(MappedHelper.class);
        MAPPEDSET_PREFIX = jvmClassName(MappedSet.class);
        MAPPED_SET2_JVM = jvmClassName(MappedSet2.class);
        MAPPED_SET3_JVM = jvmClassName(MappedSet3.class);
        MAPPED_SET4_JVM = jvmClassName(MappedSet4.class);
        CACHE_LINE_PAD_JVM = "L" + jvmClassName(CacheLinePad.class) + ";";
        OPCODE_TO_NAME = new HashMap<Integer, String>();
        INSNTYPE_TO_NAME = new HashMap<Integer, String>();
        getClassEnums(Opcodes.class, MappedObjectTransformer.OPCODE_TO_NAME, "V1_", "ACC_", "T_", "F_", "MH_");
        getClassEnums(AbstractInsnNode.class, MappedObjectTransformer.INSNTYPE_TO_NAME, new String[0]);
        (className_to_subtype = new HashMap<String, MappedSubtypeInfo>()).put(MappedObjectTransformer.MAPPED_OBJECT_JVM, new MappedSubtypeInfo(MappedObjectTransformer.MAPPED_OBJECT_JVM, null, -1, -1, -1, false));
        final String vmName = System.getProperty("java.vm.name");
        if (vmName != null && !vmName.contains("Server")) {
            System.err.println("Warning: " + MappedObject.class.getSimpleName() + "s have inferiour performance on Client VMs, please consider switching to a Server VM.");
        }
    }
    
    private static class TransformationAdapter extends ClassAdapter
    {
        final String className;
        boolean transformed;
        
        TransformationAdapter(final ClassVisitor cv, final String className) {
            super(cv);
            this.className = className;
        }
        
        public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
            final MappedSubtypeInfo mappedSubtype = MappedObjectTransformer.className_to_subtype.get(this.className);
            if (mappedSubtype != null && mappedSubtype.fields.containsKey(name)) {
                if (MappedObjectTransformer.PRINT_ACTIVITY) {
                    LWJGLUtil.log(MappedObjectTransformer.class.getSimpleName() + ": discarding field: " + this.className + "." + name + ":" + desc);
                }
                return null;
            }
            if ((access & 0x8) == 0x0) {
                return (FieldVisitor)new FieldNode(access, name, desc, signature, value) {
                    public void visitEnd() {
                        if (this.visibleAnnotations == null) {
                            this.accept(TransformationAdapter.this.cv);
                            return;
                        }
                        boolean before = false;
                        boolean after = false;
                        int byteLength = 0;
                        for (final AnnotationNode pad : this.visibleAnnotations) {
                            if (MappedObjectTransformer.CACHE_LINE_PAD_JVM.equals(pad.desc)) {
                                if ("J".equals(this.desc) || "D".equals(this.desc)) {
                                    byteLength = 8;
                                }
                                else if ("I".equals(this.desc) || "F".equals(this.desc)) {
                                    byteLength = 4;
                                }
                                else if ("S".equals(this.desc) || "C".equals(this.desc)) {
                                    byteLength = 2;
                                }
                                else {
                                    if (!"B".equals(this.desc) && !"Z".equals(this.desc)) {
                                        throw new ClassFormatError("The @CacheLinePad annotation cannot be used on non-primitive fields: " + TransformationAdapter.this.className + "." + this.name);
                                    }
                                    byteLength = 1;
                                }
                                TransformationAdapter.this.transformed = true;
                                after = true;
                                if (pad.values != null) {
                                    for (int i = 0; i < pad.values.size(); i += 2) {
                                        final boolean value = pad.values.get(i + 1).equals(Boolean.TRUE);
                                        if ("before".equals(pad.values.get(i))) {
                                            before = value;
                                        }
                                        else {
                                            after = value;
                                        }
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                        if (before) {
                            int j;
                            for (int count = j = CacheUtil.getCacheLineSize() / byteLength - 1; j >= 1; --j) {
                                TransformationAdapter.this.cv.visitField(this.access | 0x1 | 0x1000, this.name + "$PAD_" + j, this.desc, this.signature, (Object)null);
                            }
                        }
                        this.accept(TransformationAdapter.this.cv);
                        if (after) {
                            for (int count = CacheUtil.getCacheLineSize() / byteLength - 1, j = 1; j <= count; ++j) {
                                TransformationAdapter.this.cv.visitField(this.access | 0x1 | 0x1000, this.name + "$PAD" + j, this.desc, this.signature, (Object)null);
                            }
                        }
                    }
                };
            }
            return super.visitField(access, name, desc, signature, value);
        }
        
        public MethodVisitor visitMethod(final int access, String name, final String desc, final String signature, final String[] exceptions) {
            if ("<init>".equals(name)) {
                final MappedSubtypeInfo mappedSubtype = MappedObjectTransformer.className_to_subtype.get(this.className);
                if (mappedSubtype != null) {
                    if (!"()V".equals(desc)) {
                        throw new ClassFormatError(this.className + " can only have a default constructor, found: " + desc);
                    }
                    final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                    mv.visitVarInsn(25, 0);
                    mv.visitMethodInsn(183, MappedObjectTransformer.MAPPED_OBJECT_JVM, "<init>", "()V");
                    mv.visitInsn(177);
                    mv.visitMaxs(0, 0);
                    name = "constructView$LWJGL";
                }
            }
            final MethodVisitor mv2 = super.visitMethod(access, name, desc, signature, exceptions);
            return (MethodVisitor)new MethodNode(access, name, desc, signature, exceptions) {
                boolean needsTransformation;
                
                public void visitMaxs(final int a, final int b) {
                    try {
                        MappedObjectTransformer.is_currently_computing_frames = true;
                        super.visitMaxs(a, b);
                    }
                    finally {
                        MappedObjectTransformer.is_currently_computing_frames = false;
                    }
                }
                
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (MappedObjectTransformer.className_to_subtype.containsKey(owner) || owner.startsWith(MappedObjectTransformer.MAPPEDSET_PREFIX)) {
                        this.needsTransformation = true;
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
                
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (MappedObjectTransformer.className_to_subtype.containsKey(owner)) {
                        this.needsTransformation = true;
                    }
                    super.visitMethodInsn(opcode, owner, name, desc);
                }
                
                public void visitEnd() {
                    if (this.needsTransformation) {
                        TransformationAdapter.this.transformed = true;
                        try {
                            this.transformMethod(this.analyse());
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    this.accept(mv2);
                }
                
                private Frame<BasicValue>[] analyse() throws AnalyzerException {
                    final Analyzer<BasicValue> a = (Analyzer<BasicValue>)new Analyzer((Interpreter)new SimpleVerifier());
                    a.analyze(TransformationAdapter.this.className, (MethodNode)this);
                    return (Frame<BasicValue>[])a.getFrames();
                }
                
                private void transformMethod(final Frame<BasicValue>[] frames) {
                    final InsnList instructions = this.instructions;
                    final Map<Integer, MappedSubtypeInfo> arrayVars = new HashMap<Integer, MappedSubtypeInfo>();
                    final Map<AbstractInsnNode, Frame<BasicValue>> frameMap = new HashMap<AbstractInsnNode, Frame<BasicValue>>();
                    for (int i = 0; i < frames.length; ++i) {
                        frameMap.put(instructions.get(i), frames[i]);
                    }
                    for (int i = 0; i < instructions.size(); ++i) {
                        final AbstractInsnNode instruction = instructions.get(i);
                        switch (instruction.getType()) {
                            case 2: {
                                if (instruction.getOpcode() == 25) {
                                    final VarInsnNode varInsn = (VarInsnNode)instruction;
                                    final MappedSubtypeInfo mappedSubtype = arrayVars.get(varInsn.var);
                                    if (mappedSubtype != null) {
                                        i = MappedObjectTransformer.transformArrayAccess(instructions, i, frameMap, varInsn, mappedSubtype, varInsn.var);
                                    }
                                    break;
                                }
                                break;
                            }
                            case 4: {
                                final FieldInsnNode fieldInsn = (FieldInsnNode)instruction;
                                final InsnList list = MappedObjectTransformer.transformFieldAccess(fieldInsn);
                                if (list != null) {
                                    i = MappedObjectTransformer.replace(instructions, i, instruction, list);
                                    break;
                                }
                                break;
                            }
                            case 5: {
                                final MethodInsnNode methodInsn = (MethodInsnNode)instruction;
                                final MappedSubtypeInfo mappedType = MappedObjectTransformer.className_to_subtype.get(methodInsn.owner);
                                if (mappedType != null) {
                                    i = MappedObjectTransformer.transformMethodCall(instructions, i, frameMap, methodInsn, mappedType, arrayVars);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            };
        }
    }
    
    private static class FieldInfo
    {
        final long offset;
        final long length;
        final long lengthPadded;
        final Type type;
        final boolean isVolatile;
        final boolean isPointer;
        
        FieldInfo(final long offset, final long length, final long lengthPadded, final Type type, final boolean isVolatile, final boolean isPointer) {
            this.offset = offset;
            this.length = length;
            this.lengthPadded = lengthPadded;
            this.type = type;
            this.isVolatile = isVolatile;
            this.isPointer = isPointer;
        }
        
        String getAccessType() {
            return this.isPointer ? "a" : (this.type.getDescriptor().toLowerCase() + (this.isVolatile ? "v" : ""));
        }
    }
    
    private static class MappedSubtypeInfo
    {
        final String className;
        final int sizeof;
        final int sizeof_shift;
        final int align;
        final int padding;
        final boolean cacheLinePadded;
        final Map<String, FieldInfo> fields;
        
        MappedSubtypeInfo(final String className, final Map<String, FieldInfo> fields, final int sizeof, final int align, final int padding, final boolean cacheLinePadded) {
            this.className = className;
            this.sizeof = sizeof;
            if ((sizeof - 1 & sizeof) == 0x0) {
                this.sizeof_shift = getPoT(sizeof);
            }
            else {
                this.sizeof_shift = 0;
            }
            this.align = align;
            this.padding = padding;
            this.cacheLinePadded = cacheLinePadded;
            this.fields = fields;
        }
        
        private static int getPoT(int value) {
            int pot = -1;
            while (value > 0) {
                ++pot;
                value >>= 1;
            }
            return pot;
        }
    }
}
