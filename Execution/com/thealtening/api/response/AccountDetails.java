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

public class AccountDetails {

    @SerializedName("hypixel.lvl")
    private int hypixelLevel;

    @SerializedName("hypixel.rank")
    private String hypixelRank;

    @SerializedName("mineplex.lvl")
    private int mineplexLevel;

    @SerializedName("mineplex.rank")
    private String mineplexRank;

    @SerializedName("labymod.cape")
    private boolean labymodCape;

    @SerializedName("5zig.cape")
    private boolean fiveZigCape;

    public int getHypixelLevel() {
        return hypixelLevel;
    }

    public String getHypixelRank() {
        return hypixelRank;
    }

    public int getMineplexLevel() {
        return mineplexLevel;
    }

    public String getMineplexRank() {
        return mineplexRank;
    }

    public boolean hasLabyModCape() {
        return labymodCape;
    }

    public boolean hasFiveZigCape() {
        return fiveZigCape;
    }

    @Override
    public String toString() {
        return String.format("AccountDetails[%s:%s:%s:%s:%s:%s]", hypixelLevel, hypixelRank, mineplexLevel, mineplexRank, labymodCape, fiveZigCape);
    }
}
