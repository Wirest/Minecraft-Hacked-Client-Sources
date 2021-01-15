package dev.astroclient.security.auth.impl.debugger;

import dev.astroclient.security.auth.IAuthenticator;
import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.auth.impl.debugger.stages.DebugProcessesCheck;
import dev.astroclient.security.auth.impl.debugger.stages.DebuggerCheck;
import dev.astroclient.security.indirection.MethodIndirection;

import java.util.Arrays;
import java.util.List;

/**
 * made by Xen for Astro
 * at 12/3/2019
 **/
public class DebugAuthenticator implements IAuthenticator {
    private static final List<IStage> stages;

    static {                                             /* add in when not in inteli intelij adds most of these  */
        stages = Arrays.asList(new DebugProcessesCheck() /*, new DebuggerCheck()*/);
    }

    @Override
    public boolean pass() {
        return true;
    }

    @Override
    public List<IStage> getStages() {
        return stages;
    }

}
