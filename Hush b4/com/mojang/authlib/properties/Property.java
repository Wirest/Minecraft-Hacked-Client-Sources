// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.properties;

import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import java.security.Signature;
import java.security.PublicKey;

public class Property
{
    private final String name;
    private final String value;
    private final String signature;
    
    public Property(final String value, final String name) {
        this(value, name, null);
    }
    
    public Property(final String name, final String value, final String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getSignature() {
        return this.signature;
    }
    
    public boolean hasSignature() {
        return this.signature != null;
    }
    
    public boolean isSignatureValid(final PublicKey publicKey) {
        try {
            final Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(this.value.getBytes());
            return signature.verify(Base64.decodeBase64(this.signature));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e2) {
            e2.printStackTrace();
        }
        catch (SignatureException e3) {
            e3.printStackTrace();
        }
        return false;
    }
}
