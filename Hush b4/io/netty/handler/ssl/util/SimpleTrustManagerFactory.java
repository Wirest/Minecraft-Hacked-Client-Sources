// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.ManagerFactoryParameters;
import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactorySpi;
import io.netty.util.concurrent.FastThreadLocal;
import java.security.Provider;
import javax.net.ssl.TrustManagerFactory;

public abstract class SimpleTrustManagerFactory extends TrustManagerFactory
{
    private static final Provider PROVIDER;
    private static final FastThreadLocal<SimpleTrustManagerFactorySpi> CURRENT_SPI;
    
    protected SimpleTrustManagerFactory() {
        this("");
    }
    
    protected SimpleTrustManagerFactory(final String name) {
        super(SimpleTrustManagerFactory.CURRENT_SPI.get(), SimpleTrustManagerFactory.PROVIDER, name);
        SimpleTrustManagerFactory.CURRENT_SPI.get().init(this);
        SimpleTrustManagerFactory.CURRENT_SPI.remove();
        if (name == null) {
            throw new NullPointerException("name");
        }
    }
    
    protected abstract void engineInit(final KeyStore p0) throws Exception;
    
    protected abstract void engineInit(final ManagerFactoryParameters p0) throws Exception;
    
    protected abstract TrustManager[] engineGetTrustManagers();
    
    static {
        PROVIDER = new Provider("", 0.0, "") {
            private static final long serialVersionUID = -2680540247105807895L;
        };
        CURRENT_SPI = new FastThreadLocal<SimpleTrustManagerFactorySpi>() {
            @Override
            protected SimpleTrustManagerFactorySpi initialValue() {
                return new SimpleTrustManagerFactorySpi();
            }
        };
    }
    
    static final class SimpleTrustManagerFactorySpi extends TrustManagerFactorySpi
    {
        private SimpleTrustManagerFactory parent;
        
        void init(final SimpleTrustManagerFactory parent) {
            this.parent = parent;
        }
        
        @Override
        protected void engineInit(final KeyStore keyStore) throws KeyStoreException {
            try {
                this.parent.engineInit(keyStore);
            }
            catch (KeyStoreException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new KeyStoreException(e2);
            }
        }
        
        @Override
        protected void engineInit(final ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            try {
                this.parent.engineInit(managerFactoryParameters);
            }
            catch (InvalidAlgorithmParameterException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }
        
        @Override
        protected TrustManager[] engineGetTrustManagers() {
            return this.parent.engineGetTrustManagers();
        }
    }
}
