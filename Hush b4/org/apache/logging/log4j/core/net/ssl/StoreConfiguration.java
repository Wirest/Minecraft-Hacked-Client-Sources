// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration
{
    protected static final StatusLogger LOGGER;
    private String location;
    private String password;
    
    public StoreConfiguration(final String location, final String password) {
        this.location = location;
        this.password = password;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public char[] getPasswordAsCharArray() {
        if (this.password == null) {
            return null;
        }
        return this.password.toCharArray();
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public boolean equals(final StoreConfiguration config) {
        if (config == null) {
            return false;
        }
        boolean locationEquals = false;
        boolean passwordEquals = false;
        if (this.location != null) {
            locationEquals = this.location.equals(config.location);
        }
        else {
            locationEquals = (this.location == config.location);
        }
        if (this.password != null) {
            passwordEquals = this.password.equals(config.password);
        }
        else {
            passwordEquals = (this.password == config.password);
        }
        return locationEquals && passwordEquals;
    }
    
    protected void load() throws StoreConfigurationException {
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
