package dev.astroclient.security.web.url.store.impl;

import dev.astroclient.security.web.url.store.Store;

public class VersionStore implements Store {

    private final Character[] characters = new Character[]{'v', 'e', 'r', 's' , 'i' , 'o' , 'n' , '.' , 't' , 'x' , 't' };

    @Override
    public Character[] getCharacters() {
        return characters;
    }
}
