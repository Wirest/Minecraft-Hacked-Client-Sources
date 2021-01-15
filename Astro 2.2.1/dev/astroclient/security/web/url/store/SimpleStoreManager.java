package dev.astroclient.security.web.url.store;

import dev.astroclient.security.web.url.store.impl.UUIDStore;
import dev.astroclient.security.web.url.store.impl.VersionStore;

public class SimpleStoreManager {

    public static final SimpleStoreManager INSTANCE = new SimpleStoreManager();

    public Store[] getStores() {
        return stores;
    }

    public Store get(Class<? extends Store> clazz) {
        for (Store store : stores) {
            if (store.getClass() == clazz)
                return store;
        }
        return null;
    }

    private final Store[] stores = new Store[]{
            new UUIDStore(),
            new VersionStore()
    };
}
