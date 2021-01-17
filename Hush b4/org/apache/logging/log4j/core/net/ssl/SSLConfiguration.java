// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.security.UnrecoverableKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ssl", category = "Core", printObject = true)
public class SSLConfiguration
{
    private static final StatusLogger LOGGER;
    private KeyStoreConfiguration keyStoreConfig;
    private TrustStoreConfiguration trustStoreConfig;
    private SSLContext sslContext;
    
    private SSLConfiguration(final KeyStoreConfiguration keyStoreConfig, final TrustStoreConfiguration trustStoreConfig) {
        this.keyStoreConfig = keyStoreConfig;
        this.trustStoreConfig = trustStoreConfig;
        this.sslContext = null;
    }
    
    public SSLSocketFactory getSSLSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }
        return this.sslContext.getSocketFactory();
    }
    
    public SSLServerSocketFactory getSSLServerSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }
        return this.sslContext.getServerSocketFactory();
    }
    
    private SSLContext createSSLContext() {
        SSLContext context = null;
        try {
            context = this.createSSLContextBasedOnConfiguration();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with the given parameters");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createSSLContextWithTrustStoreFailure();
        }
        catch (KeyStoreConfigurationException e2) {
            context = this.createSSLContextWithKeyStoreFailure();
        }
        return context;
    }
    
    private SSLContext createSSLContextWithTrustStoreFailure() {
        SSLContext context;
        try {
            context = this.createSSLContextWithDefaultTrustManagerFactory();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default truststore");
        }
        catch (KeyStoreConfigurationException e) {
            context = this.createDefaultSSLContext();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }
    
    private SSLContext createSSLContextWithKeyStoreFailure() {
        SSLContext context;
        try {
            context = this.createSSLContextWithDefaultKeyManagerFactory();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default keystore");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createDefaultSSLContext();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }
    
    private SSLContext createSSLContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        return this.createSSLContext(false, false);
    }
    
    private SSLContext createSSLContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
        try {
            return this.createSSLContext(true, false);
        }
        catch (KeyStoreConfigurationException dummy) {
            SSLConfiguration.LOGGER.debug("Exception occured while using default keystore. This should be a BUG");
            return null;
        }
    }
    
    private SSLContext createSSLContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
        try {
            return this.createSSLContext(false, true);
        }
        catch (TrustStoreConfigurationException dummy) {
            SSLConfiguration.LOGGER.debug("Exception occured while using default truststore. This should be a BUG");
            return null;
        }
    }
    
    private SSLContext createDefaultSSLContext() {
        try {
            return SSLContext.getDefault();
        }
        catch (NoSuchAlgorithmException e) {
            SSLConfiguration.LOGGER.error("Failed to create an SSLContext with default configuration");
            return null;
        }
    }
    
    private SSLContext createSSLContext(final boolean loadDefaultKeyManagerFactory, final boolean loadDefaultTrustManagerFactory) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        try {
            KeyManager[] kManagers = null;
            TrustManager[] tManagers = null;
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            if (!loadDefaultKeyManagerFactory) {
                final KeyManagerFactory kmFactory = this.loadKeyManagerFactory();
                kManagers = kmFactory.getKeyManagers();
            }
            if (!loadDefaultTrustManagerFactory) {
                final TrustManagerFactory tmFactory = this.loadTrustManagerFactory();
                tManagers = tmFactory.getTrustManagers();
            }
            sslContext.init(kManagers, tManagers, null);
            return sslContext;
        }
        catch (NoSuchAlgorithmException e) {
            SSLConfiguration.LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol");
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyManagementException e2) {
            SSLConfiguration.LOGGER.error("Failed to initialize the SSLContext");
            throw new KeyStoreConfigurationException(e2);
        }
    }
    
    private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
        KeyStore trustStore = null;
        TrustManagerFactory tmFactory = null;
        if (this.trustStoreConfig == null) {
            throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
        }
        try {
            trustStore = this.trustStoreConfig.getTrustStore();
            tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(trustStore);
        }
        catch (NoSuchAlgorithmException e) {
            SSLConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyStoreException e2) {
            SSLConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new TrustStoreConfigurationException(e2);
        }
        catch (StoreConfigurationException e3) {
            throw new TrustStoreConfigurationException(e3);
        }
        return tmFactory;
    }
    
    private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
        KeyStore keyStore = null;
        KeyManagerFactory kmFactory = null;
        if (this.keyStoreConfig == null) {
            throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
        }
        try {
            keyStore = this.keyStoreConfig.getKeyStore();
            kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmFactory.init(keyStore, this.keyStoreConfig.getPasswordAsCharArray());
        }
        catch (NoSuchAlgorithmException e) {
            SSLConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new KeyStoreConfigurationException(e);
        }
        catch (KeyStoreException e2) {
            SSLConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new KeyStoreConfigurationException(e2);
        }
        catch (StoreConfigurationException e3) {
            throw new KeyStoreConfigurationException(e3);
        }
        catch (UnrecoverableKeyException e4) {
            SSLConfiguration.LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)");
            throw new KeyStoreConfigurationException(e4);
        }
        return kmFactory;
    }
    
    public boolean equals(final SSLConfiguration config) {
        if (config == null) {
            return false;
        }
        boolean keyStoreEquals = false;
        boolean trustStoreEquals = false;
        if (this.keyStoreConfig != null) {
            keyStoreEquals = this.keyStoreConfig.equals(config.keyStoreConfig);
        }
        else {
            keyStoreEquals = (this.keyStoreConfig == config.keyStoreConfig);
        }
        if (this.trustStoreConfig != null) {
            trustStoreEquals = this.trustStoreConfig.equals(config.trustStoreConfig);
        }
        else {
            trustStoreEquals = (this.trustStoreConfig == config.trustStoreConfig);
        }
        return keyStoreEquals && trustStoreEquals;
    }
    
    @PluginFactory
    public static SSLConfiguration createSSLConfiguration(@PluginElement("keyStore") final KeyStoreConfiguration keyStoreConfig, @PluginElement("trustStore") final TrustStoreConfiguration trustStoreConfig) {
        return new SSLConfiguration(keyStoreConfig, trustStoreConfig);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
