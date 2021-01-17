// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil.response;

public class Response
{
    private String error;
    private String errorMessage;
    private String cause;
    
    public String getError() {
        return this.error;
    }
    
    public String getCause() {
        return this.cause;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    protected void setError(final String error) {
        this.error = error;
    }
    
    protected void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    protected void setCause(final String cause) {
        this.cause = cause;
    }
}
