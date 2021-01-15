package dev.astroclient.security.auth.impl.outdated;

import dev.astroclient.client.Client;
import dev.astroclient.security.auth.IAuthenticator;
import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.auth.impl.outdated.stage.CheckVersionStage;

import java.util.Collections;
import java.util.List;

public class VersionAuthenticator implements IAuthenticator {

    private static final List<IStage> stages;

    static {
        stages = Collections.singletonList(new CheckVersionStage());
    }

    @Override
    public boolean pass() {
        for (IStage stage : stages)
            if (!stage.pass())
                return false;
        Client.INSTANCE.pass_2 = true;
        return true;
    }

    @Override
    public List<IStage> getStages() {
        return stages;
    }
}
