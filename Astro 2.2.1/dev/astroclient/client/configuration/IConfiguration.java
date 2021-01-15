package dev.astroclient.client.configuration;

import com.google.gson.JsonObject;

/**
 * made by Xen for Astro
 * at 12/8/2019
 **/
public interface IConfiguration {
    void loadConfig(JsonObject jsonObject);

    JsonObject saveConfig();
}
