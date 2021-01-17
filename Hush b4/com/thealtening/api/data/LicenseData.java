// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api.data;

public class LicenseData
{
    private String username;
    private boolean premium;
    private String premiumName;
    private String expiryDate;
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean isPremium() {
        return this.premium;
    }
    
    public String getPremiumName() {
        return this.premiumName;
    }
    
    public String getExpiryDate() {
        return this.expiryDate;
    }
    
    @Override
    public String toString() {
        return String.format("LicenseData[%s:%s:%s:%s]", this.username, this.premium, this.premiumName, this.expiryDate);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof LicenseData)) {
            return false;
        }
        final LicenseData castedLicenseInfo = (LicenseData)obj;
        return castedLicenseInfo.getExpiryDate().equals(this.getExpiryDate()) && castedLicenseInfo.getPremiumName().equals(this.getPremiumName()) && castedLicenseInfo.isPremium() == this.isPremium() && castedLicenseInfo.getUsername().equals(this.getUsername());
    }
}
