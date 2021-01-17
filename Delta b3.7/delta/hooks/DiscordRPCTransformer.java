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
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package delta.hooks;

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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class DiscordRPCTransformer
extends Transformer
implements Opcodes {
    public byte[] transform(String string, byte[] arrby, boolean bl) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(arrby);
        classReader.accept((ClassVisitor)classNode, 37 - 63 + 43 - 3 + -8);
        int n = 145 - 206 + 76 - 71 + 56;
        for (MethodNode methodNode : classNode.methods) {
            AbstractInsnNode abstractInsnNode2;
            for (AbstractInsnNode abstractInsnNode2 : methodNode.instructions) {
                if (!(abstractInsnNode2 instanceof MethodInsnNode)) continue;
                MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode2;
                if (!methodInsnNode.owner.equalsIgnoreCase("club/minnced/discord/rpc/DiscordRPC") || !methodInsnNode.name.equalsIgnoreCase("Discord_Initialize") || !methodInsnNode.desc.equalsIgnoreCase("(Ljava/lang/String;Lclub/minnced/discord/rpc/DiscordEventHandlers;ZLjava/lang/String;)V")) continue;
                n = 38 - 43 + 3 - 3 + 6;
            }
            if (n == 0) continue;
            abstractInsnNode2 = new InsnList();
            abstractInsnNode2.add((AbstractInsnNode)new InsnNode(272 - 534 + 393 + 46));
            methodNode.instructions = abstractInsnNode2;
        }
        if (n == 0) {
            return arrby;
        }
        ClassWriter classWriter = Class57.jGwh().9L0o()._travel();
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }

    public String getName() {
        return "NoRPC";
    }

    public String[] getTargetClasses() {
        String[] arrstring = new String[38 - 69 + 39 - 31 + 24];
        arrstring[51 - 58 + 16 - 11 + 2] = "fr.paladium";
        return arrstring;
    }

    public boolean isEnabled() {
        return 88 - 175 + 40 - 32 + 80;
    }
}

