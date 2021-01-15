/*
 * Copyright (C) 2019 BasicDataRetriever
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

package com.thealtening.api.retriever;

import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;

public class BasicDataRetriever implements DataRetriever {

    private String apiKey;

    public BasicDataRetriever(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void updateKey(String newApiKey) {
        this.apiKey = newApiKey;
    }

    @Override
    public License getLicence() throws TheAlteningException {
        JsonObject jsonObject = retrieveData(LICENCE_URL + apiKey);
        return gson.fromJson(jsonObject, License.class);
    }

    @Override
    public Account getAccount() throws TheAlteningException {
        JsonObject jsonObject = retrieveData(GENERATE_URL + apiKey);
        return gson.fromJson(jsonObject, Account.class);
    }

    @Override
    public boolean isPrivate(String token) throws TheAlteningException {
        JsonObject jsonObject = retrieveData(PRIVATE_ACC_URL + token + "&token=" + apiKey);
        return isSuccess(jsonObject);
    }

    @Override
    public boolean isFavorite(String token) throws TheAlteningException {
        JsonObject jsonObject = retrieveData(FAVORITE_ACC_URL + token + "&token=" + apiKey);
        return isSuccess(jsonObject);
    }

    public AsynchronousDataRetriever toAsync() {
        return new AsynchronousDataRetriever(apiKey);
    }
}
