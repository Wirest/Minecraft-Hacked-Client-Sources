// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "keyStore", category = "Core", printObject = true)
public class KeyStoreConfiguration extends StoreConfiguration
{
    private KeyStore keyStore;
    private String keyStoreType;
    
    public KeyStoreConfiguration(final String location, final String password) {
        super(location, password);
        this.keyStoreType = "JKS";
        this.keyStore = null;
    }
    
    @Override
    protected void load() throws StoreConfigurationException {
        FileInputStream fin = null;
        KeyStoreConfiguration.LOGGER.debug("Loading keystore from file with params(location={})", this.getLocation());
        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }
            fin = new FileInputStream(this.getLocation());
            final KeyStore ks = KeyStore.getInstance(this.keyStoreType);
            ks.load(fin, this.getPasswordAsCharArray());
            this.keyStore = ks;
        }
        catch (CertificateException e) {
            KeyStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", this.keyStoreType);
            throw new StoreConfigurationException(e);
        }
        catch (NoSuchAlgorithmException e2) {
            KeyStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
            throw new StoreConfigurationException(e2);
        }
        catch (KeyStoreException e3) {
            KeyStoreConfiguration.LOGGER.error(e3);
            throw new StoreConfigurationException(e3);
        }
        catch (FileNotFoundException e4) {
            KeyStoreConfiguration.LOGGER.error("The keystore file({}) is not found", this.getLocation());
            throw new StoreConfigurationException(e4);
        }
        catch (IOException e5) {
            KeyStoreConfiguration.LOGGER.error("Something is wrong with the format of the keystore or the given password");
            throw new StoreConfigurationException(e5);
        }
        finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ex) {}
        }
        KeyStoreConfiguration.LOGGER.debug("Keystore successfully loaded with params(location={})", this.getLocation());
    }
    
    public KeyStore getKeyStore() throws StoreConfigurationException {
        if (this.keyStore == null) {
            this.load();
        }
        return this.keyStore;
    }
    
    @PluginFactory
    public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") final String location, @PluginAttribute("password") final String password) {
        return new KeyStoreConfiguration(location, password);
    }
}
