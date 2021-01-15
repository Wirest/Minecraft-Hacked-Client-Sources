package dev.astroclient.security.web.url.store.impl;

import dev.astroclient.security.web.url.store.Store;

public class UUIDStore implements Store {
    private final Character[] characters = new Character[]{'p' ,
            'r' , 'e' , 'm' , 'i' , 'u' , 'm' , '/' , 'c' , 'h' , 'e' , 'c' , 'k','H','W','I','D','.','p','h','p','?','h','w','i','d','='};

    @Override
    public Character[] getCharacters() {
        return characters;
    }
}
