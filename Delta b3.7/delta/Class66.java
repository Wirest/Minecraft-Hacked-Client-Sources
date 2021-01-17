/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.api.load.transform.Transformer
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.Opcodes
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package delta;

import delta.Class130;
import delta.Class196;
import delta.Class57;
import delta.Class82;
import java.util.ArrayList;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Class66
extends Transformer
implements Opcodes {
    public byte[] transform(String string, byte[] arrby, boolean bl) {
        if (bl) {
            return arrby;
        }
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(arrby);
        classReader.accept((ClassVisitor)classNode, 73 - 78 + 76 - 4 + -61);
        InsnList insnList = new InsnList();
        insnList.add((AbstractInsnNode)new VarInsnNode(134 - 257 + 43 - 24 + 129, 247 - 321 + 180 - 64 + -42));
        Class[] arrclass = new Class[158 - 314 + 161 - 31 + 28];
        arrclass[124 - 197 + 90 - 17 + 0] = String.class;
        arrclass[156 - 172 + 146 + -129] = String.class;
        insnList.add((AbstractInsnNode)new MethodInsnNode(213 - 302 + 3 + 270, Class130._music().getName().replace(184 - 266 + 263 + -135, 164 - 249 + 104 + 28), Class196._google(Class130._music(), "ftg", String.class, arrclass).getName(), "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", 202 - 258 + 163 - 67 + -40));
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equalsIgnoreCase("getString")) {
                methodNode.instructions.insertBefore(Class82._saints(methodNode), insnList);
            }
            if (!methodNode.name.equalsIgnoreCase("start")) continue;
            ArrayList<AbstractInsnNode> arrayList = new ArrayList<AbstractInsnNode>();
            for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                AbstractInsnNode abstractInsnNode2;
                AbstractInsnNode abstractInsnNode3;
                AbstractInsnNode abstractInsnNode4;
                if (abstractInsnNode.getOpcode() != 232 - 261 + 178 + 29 || (abstractInsnNode4 = abstractInsnNode.getNext()) == null || abstractInsnNode4.getOpcode() != 68 - 98 + 48 - 13 + 20 || (abstractInsnNode3 = abstractInsnNode4.getNext()) == null || abstractInsnNode3.getOpcode() != 135 - 233 + 9 + 271 || (abstractInsnNode2 = abstractInsnNode3.getNext()) == null || abstractInsnNode2.getOpcode() != 167 - 234 + 159 + 90) continue;
                arrayList.add(abstractInsnNode);
                arrayList.add(abstractInsnNode4);
                arrayList.add(abstractInsnNode3);
                arrayList.add(abstractInsnNode2);
                break;
            }
            arrayList.forEach(((InsnList)methodNode.instructions)::remove);
        }
        ClassWriter classWriter = Class57.jGwh().9L0o()._travel();
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }

    public String[] getTargetClasses() {
        String[] arrstring = new String[225 - 378 + 156 - 4 + 2];
        arrstring[102 - 201 + 56 - 5 + 48] = "cpw.mods.fml.client.SplashProgress";
        return arrstring;
    }

    public boolean isEnabled() {
        return 61 - 116 + 6 - 6 + 56;
    }

    public String getName() {
        return "Splashy";
    }
}

