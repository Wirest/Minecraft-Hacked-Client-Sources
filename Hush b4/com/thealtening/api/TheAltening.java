// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api;

import java.util.concurrent.CompletableFuture;
import com.thealtening.api.data.AccountData;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.thealtening.api.utils.exceptions.TheAlteningException;
import com.google.gson.JsonObject;
import com.thealtening.api.data.LicenseData;
import com.google.gson.Gson;
import java.util.logging.Logger;
import com.thealtening.api.utils.HttpUtils;

public class TheAltening extends HttpUtils
{
    private final String apiKey;
    private final String endpoint = "http://api.thealtening.com/v1/";
    private final Logger logger;
    private final Gson gson;
    
    public TheAltening(final String apiKey) {
        this.logger = Logger.getLogger("TheAltening");
        this.gson = new Gson();
        System.out.println(this.gson == null);
        this.apiKey = apiKey;
    }
    
    public LicenseData getLicenseData() throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/license?token=" + this.apiKey);
            final JsonObject jsonObject = this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return this.gson.fromJson(jsonObject, LicenseData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public AccountData getAccountData() throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/generate?info=true&token=" + this.apiKey);
            final JsonObject jsonObject = this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return this.gson.fromJson(jsonObject, AccountData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public boolean isPrivate(final String token) throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/private?acctoken=" + token + "&token=" + this.apiKey);
            final JsonObject jsonObject = this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public boolean isFavorite(final String token) throws TheAlteningException {
        try {
            final String connectionData = this.connect("http://api.thealtening.com/v1/favorite?acctoken=" + token + "&token=" + this.apiKey);
            final JsonObject jsonObject = this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }
    
    public static class Asynchronous
    {
        private final TheAltening instance;
        
        public Asynchronous(final TheAltening instance) {
            this.instance = instance;
        }
        
        public CompletableFuture<LicenseData> getLicenseData() {
            final CompletableFuture<LicenseData> returnValue = new CompletableFuture<LicenseData>();
            try {
                returnValue.complete(this.instance.getLicenseData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<AccountData> getAccountData() {
            final CompletableFuture<AccountData> returnValue = new CompletableFuture<AccountData>();
            try {
                returnValue.complete(this.instance.getAccountData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<Boolean> isPrivate(final String token) {
            final CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isPrivate(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
        
        public CompletableFuture<Boolean> isFavorite(final String token) {
            final CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isFavorite(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
    }
}
