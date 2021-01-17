// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl.util;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import sun.security.x509.X509CertImpl;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateIssuerName;
import java.security.cert.CertificateException;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateSerialNumber;
import java.util.Random;
import java.math.BigInteger;
import sun.security.x509.CertificateVersion;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertInfo;
import java.security.SecureRandom;
import java.security.KeyPair;

final class OpenJdkSelfSignedCertGenerator
{
    static String[] generate(final String fqdn, final KeyPair keypair, final SecureRandom random) throws Exception {
        final PrivateKey key = keypair.getPrivate();
        final X509CertInfo info = new X509CertInfo();
        final X500Name owner = new X500Name("CN=" + fqdn);
        info.set("version", new CertificateVersion(2));
        info.set("serialNumber", new CertificateSerialNumber(new BigInteger(64, random)));
        try {
            info.set("subject", new CertificateSubjectName(owner));
        }
        catch (CertificateException ignore) {
            info.set("subject", owner);
        }
        try {
            info.set("issuer", new CertificateIssuerName(owner));
        }
        catch (CertificateException ignore) {
            info.set("issuer", owner);
        }
        info.set("validity", new CertificateValidity(SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER));
        info.set("key", new CertificateX509Key(keypair.getPublic()));
        info.set("algorithmID", new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid)));
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(key, "SHA1withRSA");
        info.set("algorithmID.algorithm", cert.get("x509.algorithm"));
        cert = new X509CertImpl(info);
        cert.sign(key, "SHA1withRSA");
        cert.verify(keypair.getPublic());
        return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
    }
    
    private OpenJdkSelfSignedCertGenerator() {
    }
}
