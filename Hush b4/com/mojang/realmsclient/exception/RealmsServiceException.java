// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.exception;

import net.minecraft.realms.RealmsScreen;
import com.mojang.realmsclient.client.RealmsError;

public class RealmsServiceException extends Exception
{
    public final int httpResultCode;
    public final String httpResponseContent;
    public final int errorCode;
    public final String errorMsg;
    
    public RealmsServiceException(final int httpResultCode, final String httpResponseText, final RealmsError error) {
        super(httpResponseText);
        this.httpResultCode = httpResultCode;
        this.httpResponseContent = httpResponseText;
        this.errorCode = error.getErrorCode();
        this.errorMsg = error.getErrorMessage();
    }
    
    public RealmsServiceException(final int httpResultCode, final String httpResponseText, final int errorCode, final String errorMsg) {
        super(httpResponseText);
        this.httpResultCode = httpResultCode;
        this.httpResponseContent = httpResponseText;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    @Override
    public String toString() {
        if (this.errorCode != -1) {
            final String translationKey = "mco.errorMessage." + this.errorCode;
            final String translated = RealmsScreen.getLocalizedString(translationKey);
            return (translated.equals(translationKey) ? this.errorMsg : translated) + " - " + this.errorCode;
        }
        return "Realms (" + this.httpResultCode + ") " + this.httpResponseContent;
    }
}
