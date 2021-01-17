package me.cupboard.command.argument.factory;

import java.lang.reflect.*;
import java.util.*;
import me.cupboard.command.argument.*;
import java.lang.annotation.*;

public abstract class ArgumentFactory
{
    private final Map<String, Method> arguments;
    
    public ArgumentFactory() {
        this.arguments = new HashMap<String, Method>();
    }
    
    public void load() {
        Method[] declaredMethods;
        for (int length = (declaredMethods = this.getClass().getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (method.isAnnotationPresent(Argument.class)) {
                final Argument argument = method.getAnnotation(Argument.class);
                String[] handles;
                for (int length2 = (handles = argument.handles()).length, j = 0; j < length2; ++j) {
                    final String handle = handles[j];
                    if (this.arguments.containsKey(handle)) {
                        throw new UnsupportedOperationException(String.format("Overlapping arguments at %s and %s.", this.arguments.get(handle).getName(), method.getName()));
                    }
                    this.arguments.put(handle, method);
                }
            }
        }
    }
    
    public String getSyntax(final String command, final String argument) {
        final Method target = this.getMethod(argument);
        if (target.isAnnotationPresent(Argument.class)) {
            final Argument annotation = target.getAnnotation(Argument.class);
            return String.join(" ", command, argument, annotation.syntax());
        }
        return "no syntax provided";
    }
    
    public boolean hasArgument(final String argument) {
        return this.arguments.containsKey(argument);
    }
    
    public Method getMethod(final String argument) {
        if (this.arguments.containsKey(argument)) {
            final Method target = this.arguments.get(argument);
            target.setAccessible(true);
            return target;
        }
        return null;
    }
}
