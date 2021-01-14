/*
 * Copyright (C) 2019 TheAltening
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.thealtening.api.response;

import com.google.gson.annotations.SerializedName;

public class License {

    private String username;
    @SerializedName("hasLicense")
    private boolean premium;

    @SerializedName("licenseType")
    private String premiumName;

    @SerializedName("expires")
    private String expiryDate;

    public String getUsername() {
        return username;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getPremiumName() {
        return premiumName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return String.format("License[%s:%s:%s:%s]", username, premium, premiumName, expiryDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof License)) {
            return false;
        }

        License other = (License) obj;
        return other.getExpiryDate().equals(getExpiryDate()) && other.getPremiumName().equals(getPremiumName()) && other.isPremium() == isPremium() && other.getUsername().equals(getUsername());
    }
}
