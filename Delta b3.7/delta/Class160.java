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
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package delta;

import delta.Class57;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Class160
extends Transformer
implements Opcodes {
    public String[] getTargetClasses() {
        String[] arrstring = new String[195 - 313 + 127 + -8];
        arrstring[257 - 290 + 32 - 8 + 9] = "net.gliby.voicechat.client.sound.SoundPreProcessor";
        return arrstring;
    }

    public byte[] transform(String string, byte[] arrby, boolean bl) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(arrby);
        classReader.accept((ClassVisitor)classNode, 115 - 197 + 118 + -30);
        InsnList insnList = new InsnList();
        insnList.add((AbstractInsnNode)new VarInsnNode(212 - 270 + 208 + -129, 26 - 34 + 11 + 0));
        LabelNode labelNode = new LabelNode();
        insnList.add((AbstractInsnNode)new JumpInsnNode(23 - 36 + 21 - 2 + 148, labelNode));
        insnList.add((AbstractInsnNode)new InsnNode(128 - 232 + 118 + -11));
        insnList.add((AbstractInsnNode)new InsnNode(194 - 368 + 146 + 200));
        insnList.add((AbstractInsnNode)labelNode);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode == null || !methodNode.desc.equals("(I[BIZB)Z")) continue;
            methodNode.instructions.insert(insnList);
        }
        ClassWriter classWriter = Class57.jGwh().9L0o()._travel();
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }

    public String getName() {
        return "Grillbys'";
    }

    public boolean isEnabled() {
        return 150 - 196 + 12 + 35;
    }
}

