package dev.astroclient.security.auth.impl.outdated.stage;

import dev.astroclient.client.Client;
import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.indirection.MethodIndirection;
import dev.astroclient.security.web.DataRetriever;
import dev.astroclient.security.web.url.Decoder;
import dev.astroclient.security.web.url.store.SimpleStoreManager;
import dev.astroclient.security.web.url.store.impl.VersionStore;

import java.io.IOException;

public class CheckVersionStage implements IStage {

    @Override
    public byte getStage() {
        return 3;
    }

    @Override
    public boolean pass() {
        return true;
    }
}
