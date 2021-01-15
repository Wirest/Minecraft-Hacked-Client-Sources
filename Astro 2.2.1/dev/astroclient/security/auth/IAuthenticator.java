package dev.astroclient.security.auth;

import java.util.List;

public interface IAuthenticator {

    List<IStage> getStages();

    boolean pass();
}
