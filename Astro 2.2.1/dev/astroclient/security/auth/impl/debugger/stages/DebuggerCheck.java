package dev.astroclient.security.auth.impl.debugger.stages;

import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.indirection.MethodIndirection;

import java.lang.management.ManagementFactory;

/**
 * made by Xen for Astro
 * at 12/3/2019
 **/
public class DebuggerCheck implements IStage {
    private String[] debugTypes = new String[]{
            "-Xbootclasspath", "-Xdebug",
            "-agentlib", "-javaagent:",
            "-Xrunjdwp:", "-verbose", "jdwp="
    };

    @Override
    public byte getStage() {
        return 5;
    }

    @Override
    public boolean pass() {
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            for(int i = 0; i  < debugTypes.length; i++) {
                String thing = debugTypes[i];
                if (arg.contains(thing)) {
                    return false;
                }
            }
        }

        return true;
    }
}
