/*
 * Copyright 2020 Hippo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.hippo.api.lwjeb.listener;

import me.hippo.api.lwjeb.configuration.config.impl.ExceptionHandlingConfiguration;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

/**
 * @author Hippo
 * @version 5.0.1, 12/2/19
 * @since 5.0.0
 * <p>
 * A listener is used to invoke handlers.
 */
@FunctionalInterface
public interface Listener {

    /**
     * Invokes the handler.
     *
     * @param parent The parent.
     * @param topic  The topic.
     * @throws ReflectiveOperationException If there is an error invoking the handler.
     */
    void invoke(Object parent, Object topic) throws ReflectiveOperationException;

    /**
     * Dynamically generates a listener to invoke {@code method}.
     *
     * <p>
     * Comments included throughout the method to understand the bytecode better.
     * </p>
     *
     * @param parent                         The parent.
     * @param method                         The method.
     * @param topic                          The topic.
     * @param exceptionHandlingConfiguration The exception handling configuration.
     * @return The dynamic listener.
     */
    static Listener of(Class<?> parent, Method method, Class<?> topic, ExceptionHandlingConfiguration exceptionHandlingConfiguration) {

        ClassNode classNode = new ClassNode();

        classNode.visit(V1_8, ACC_PUBLIC + ACC_SUPER + ACC_FINAL, "lwjeb/generated/" + parent.getName().replace('.', '/') + "/" + getUniqueMethodName(method), null, "java/lang/Object", new String[]{Listener.class.getName().replace('.', '/')}); //Sets the class name, access, and listeners.
        classNode.methods = new ArrayList<>();

        /*Creates a default constructor, just super()*/
        MethodNode constructorMethodNode = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        constructorMethodNode.instructions.add(new VarInsnNode(ALOAD, 0));
        constructorMethodNode.instructions.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
        constructorMethodNode.instructions.add(new InsnNode(RETURN));

        MethodNode invokeMethodNode = new MethodNode(ACC_PUBLIC, "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        invokeMethodNode.instructions.add(new VarInsnNode(ALOAD, 1)); //pushes the parent to the stack.
        invokeMethodNode.instructions.add(new TypeInsnNode(CHECKCAST, parent.getName().replace('.', '/'))); //cast parent to the actual parents type.
        invokeMethodNode.instructions.add(new VarInsnNode(ALOAD, 2)); //pushes the topic to the stack.
        invokeMethodNode.instructions.add(new TypeInsnNode(CHECKCAST, topic.getName().replace('.', '/'))); //cast the topic to the actual topics type.
        invokeMethodNode.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, parent.getName().replace('.', '/'), method.getName(), Type.getMethodDescriptor(method))); //invokes the method.
        invokeMethodNode.instructions.add(new InsnNode(RETURN));

        classNode.methods.add(constructorMethodNode);
        classNode.methods.add(invokeMethodNode);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES); //computes the method stack map frames
        classNode.accept(classWriter);


        try {
            Class<?> compiledClass = ListenerClassLoader.getInstance().createClass(classNode.name.replace('/', '.'), classWriter.toByteArray());
            return (Listener) compiledClass.newInstance();
        } catch (ReflectiveOperationException e) {
            exceptionHandlingConfiguration.getExceptionHandler().handleException(e);
            return method::invoke;
        }

    }

    /**
     * Gets a unique method name from a method instance.
     *
     * @param method The method.
     * @return The unique name.
     */
    static String getUniqueMethodName(Method method) {
        StringBuilder parameters = new StringBuilder();
        for (Parameter parameter : method.getParameters()) {
            parameters.append(parameter.getType().getName().replace('.', '_'));
        }
        return method.getName() + parameters.toString();
    }
}
