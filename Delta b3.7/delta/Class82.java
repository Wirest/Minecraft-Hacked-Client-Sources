/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.deltaloader.utils.Java9Fix
 *  org.objectweb.asm.Opcodes
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package delta;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import me.xtrm.deltaloader.utils.Java9Fix;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Class82 {
    private static Map<Integer, String> aside$;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String _musician(int n) {
        if (aside$ != null) return aside$.get(n);
        try {
            aside$ = new HashMap<Integer, String>();
            int n2 = 137 - 161 + 136 - 53 + -59;
            Field[] arrfield = Opcodes.class.getDeclaredFields();
            int n3 = arrfield.length;
            for (int i = 199 - 250 + 129 - 129 + 51; i < n3; ++i) {
                Field field = arrfield[i];
                if (n2 < 131 - 141 + 44 + 26) {
                    ++n2;
                    continue;
                }
                Java9Fix.setAccessible((AccessibleObject)field);
                int n4 = (Integer)field.get(null);
                aside$.put(n4, field.getName());
            }
            return aside$.get(n);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            aside$ = null;
            reflectiveOperationException.printStackTrace();
        }
        return aside$.get(n);
    }

    public static AbstractInsnNode _killing(MethodNode methodNode) {
        AbstractInsnNode abstractInsnNode = methodNode.instructions.getLast();
        while (abstractInsnNode.getOpcode() != 72 - 109 + 74 - 20 + 155) {
            abstractInsnNode = abstractInsnNode.getPrevious();
        }
        return abstractInsnNode;
    }

    public static AbstractInsnNode _saints(MethodNode methodNode) {
        AbstractInsnNode abstractInsnNode = methodNode.instructions.getLast();
        while (abstractInsnNode.getOpcode() != 150 - 185 + 105 - 102 + 208) {
            abstractInsnNode = abstractInsnNode.getPrevious();
        }
        return abstractInsnNode;
    }

    public static AbstractInsnNode _bytes(MethodNode methodNode) {
        AbstractInsnNode abstractInsnNode = methodNode.instructions.getLast();
        while (abstractInsnNode.getOpcode() != 208 - 289 + 91 + 167) {
            abstractInsnNode = abstractInsnNode.getPrevious();
        }
        return abstractInsnNode;
    }
}

