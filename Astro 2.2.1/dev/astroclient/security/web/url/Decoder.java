package dev.astroclient.security.web.url;

import dev.astroclient.security.web.url.store.SimpleStoreManager;
import dev.astroclient.security.web.url.store.Store;

public class Decoder {

    public static String decode(Class<? extends Store> store) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : SimpleStoreManager.INSTANCE.get(store).getCharacters())
            stringBuilder.append(character);
        return stringBuilder.toString();
    }
}
