package dev.astroclient.security.auth.impl.user;

import dev.astroclient.client.Client;
import dev.astroclient.security.auth.IAuthenticator;
import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.auth.impl.user.stage.CheckUIDStage;
import dev.astroclient.security.auth.impl.user.stage.CheckUsernameStage;
import dev.astroclient.security.indirection.MethodIndirection;

import java.util.Arrays;
import java.util.List;

public class UserAuthenticator implements IAuthenticator {

    private static final List<IStage> stages;

    static {
        stages = Arrays.asList(new CheckUsernameStage(), new CheckUIDStage());
    }

    @Override
    public boolean pass() {
        Client.INSTANCE.pass_1 = true;
        return true;
    }

    @Override
    public List<IStage> getStages() {
        return stages;
    }
}
