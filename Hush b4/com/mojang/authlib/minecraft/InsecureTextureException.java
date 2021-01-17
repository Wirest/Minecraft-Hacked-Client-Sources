// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.minecraft;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import java.util.Calendar;
import java.util.Date;

public class InsecureTextureException extends RuntimeException
{
    public InsecureTextureException(final String message) {
        super(message);
    }
    
    public static class OutdatedTextureException extends InsecureTextureException
    {
        private final Date validFrom;
        private final Calendar limit;
        
        public OutdatedTextureException(final Date validFrom, final Calendar limit) {
            super("Decrypted textures payload is too old (" + validFrom + ", but we need it to be at least " + limit + ")");
            this.validFrom = validFrom;
            this.limit = limit;
        }
    }
    
    public static class WrongTextureOwnerException extends InsecureTextureException
    {
        private final GameProfile expected;
        private final UUID resultId;
        private final String resultName;
        
        public WrongTextureOwnerException(final GameProfile expected, final UUID resultId, final String resultName) {
            super("Decrypted textures payload was for another user (expected " + expected.getId() + "/" + expected.getName() + " but was for " + resultId + "/" + resultName + ")");
            this.expected = expected;
            this.resultId = resultId;
            this.resultName = resultName;
        }
    }
    
    public static class MissingTextureException extends InsecureTextureException
    {
        public MissingTextureException() {
            super("No texture information found");
        }
    }
}
