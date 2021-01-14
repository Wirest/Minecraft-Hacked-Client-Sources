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

package com.thealtening.api.retriever;

import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AsynchronousDataRetriever extends BasicDataRetriever {

    public AsynchronousDataRetriever(String apiKey) {
        super(apiKey);
    }

    public CompletableFuture<License> getLicenseDataAsync() {
        return completeTask(BasicDataRetriever::getLicense);
    }

    public CompletableFuture<Account> getAccountDataAsync() {
        return completeTask(BasicDataRetriever::getAccount);
    }

    public CompletableFuture<Boolean> isPrivateAsync(String token) {
        return completeTask(dr -> dr.isPrivate(token));
    }

    public CompletableFuture<Boolean> isFavoriteAsync(String token) {
        return completeTask(dr -> dr.isFavorite(token));
    }

    public CompletableFuture<List<Account>> getPrivatedAccountsAsync() {
        return completeTask(BasicDataRetriever::getPrivatedAccounts);
    }

    public CompletableFuture<List<Account>> getFavoritedAccountsAsync() {
        return completeTask(BasicDataRetriever::getFavoriteAccounts);
    }

    private <T> CompletableFuture<T> completeTask(Function<BasicDataRetriever, T> function) {
        CompletableFuture<T> returnValue = new CompletableFuture<>();
        try {
            returnValue.complete(function.apply(this));
        } catch (TheAlteningException exception) {
            returnValue.completeExceptionally(exception);
        }

        return returnValue;
    }
}