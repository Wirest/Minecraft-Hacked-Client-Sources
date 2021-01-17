// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "trustStore", category = "Core", printObject = true)
public class TrustStoreConfiguration extends StoreConfiguration
{
    private KeyStore trustStore;
    private String trustStoreType;
    
    public TrustStoreConfiguration(final String location, final String password) {
        super(location, password);
        this.trustStoreType = "JKS";
        this.trustStore = null;
    }
    
    @Override
    protected void load() throws StoreConfigurationException {
        KeyStore ts = null;
        InputStream in = null;
        TrustStoreConfiguration.LOGGER.debug("Loading truststore from file with params(location={})", this.getLocation());
        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }
            ts = KeyStore.getInstance(this.trustStoreType);
            in = new FileInputStream(this.getLocation());
            ts.load(in, this.getPasswordAsCharArray());
        }
        catch (CertificateException e) {
            TrustStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", this.trustStoreType);
            throw new StoreConfigurationException(e);
        }
        catch (NoSuchAlgorithmException e2) {
            TrustStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
            throw new StoreConfigurationException(e2);
        }
        catch (KeyStoreException e3) {
            TrustStoreConfiguration.LOGGER.error(e3);
            throw new StoreConfigurationException(e3);
        }
        catch (FileNotFoundException e4) {
            TrustStoreConfiguration.LOGGER.error("The keystore file({}) is not found", this.getLocation());
            throw new StoreConfigurationException(e4);
        }
        catch (IOException e5) {
            TrustStoreConfiguration.LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", e5.getMessage());
            throw new StoreConfigurationException(e5);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e6) {
                TrustStoreConfiguration.LOGGER.warn("Error closing {}", this.getLocation(), e6);
            }
        }
        this.trustStore = ts;
        TrustStoreConfiguration.LOGGER.debug("Truststore successfully loaded with params(location={})", this.getLocation());
    }
    
    public KeyStore getTrustStore() throws StoreConfigurationException {
        if (this.trustStore == null) {
            this.load();
        }
        return this.trustStore;
    }
    
    @PluginFactory
    public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute("location") final String location, @PluginAttribute("password") final String password) {
        return new TrustStoreConfiguration(location, password);
    }
}
