package sun.security.ec;

import sun.security.pkcs.PKCS8Key;
import sun.security.util.ArrayUtil;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

public final class ECPrivateKeyImpl
        extends PKCS8Key
        implements ECPrivateKey {
    private static final long serialVersionUID = 88695385615075129L;
    private BigInteger s;
    private byte[] arrayS;
    private ECParameterSpec params;

    public ECPrivateKeyImpl(byte[] paramArrayOfByte)
            throws InvalidKeyException {
        decode(paramArrayOfByte);
    }

    public ECPrivateKeyImpl(BigInteger paramBigInteger, ECParameterSpec paramECParameterSpec)
            throws InvalidKeyException {
        this.s = paramBigInteger;
        this.params = paramECParameterSpec;
        makeEncoding(paramBigInteger);
    }

    ECPrivateKeyImpl(byte[] paramArrayOfByte, ECParameterSpec paramECParameterSpec)
            throws InvalidKeyException {
        this.arrayS = ((byte[]) paramArrayOfByte.clone());
        this.params = paramECParameterSpec;
        makeEncoding(paramArrayOfByte);
    }

    private void makeEncoding(byte[] paramArrayOfByte)
            throws InvalidKeyException {
        this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(this.params));
        try {
            DerOutputStream localDerOutputStream = new DerOutputStream();
            localDerOutputStream.putInteger(1);
            byte[] arrayOfByte = (byte[]) paramArrayOfByte.clone();
            ArrayUtil.reverse(arrayOfByte);
            localDerOutputStream.putOctetString(arrayOfByte);
            DerValue localDerValue = new DerValue((byte) 48, localDerOutputStream.toByteArray());
            this.key = localDerValue.toByteArray();
        } catch (IOException localIOException) {
            throw new InvalidKeyException(localIOException);
        }
    }

    private void makeEncoding(BigInteger paramBigInteger)
            throws InvalidKeyException {
        this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(this.params));
        try {
            byte[] arrayOfByte1 = paramBigInteger.toByteArray();
            int i = -8;
            byte[] arrayOfByte2 = new byte[i];
            int j = Math.max(arrayOfByte1.length - arrayOfByte2.length, 0);
            int k = Math.max(arrayOfByte2.length - arrayOfByte1.length, 0);
            int m = Math.min(arrayOfByte1.length, arrayOfByte2.length);
            System.arraycopy(arrayOfByte1, j, arrayOfByte2, k, m);
            DerOutputStream localDerOutputStream = new DerOutputStream();
            localDerOutputStream.putInteger(1);
            localDerOutputStream.putOctetString(arrayOfByte2);
            DerValue localDerValue = new DerValue((byte) 48, localDerOutputStream.toByteArray());
            this.key = localDerValue.toByteArray();
        } catch (IOException localIOException) {
            throw new InvalidKeyException(localIOException);
        }
    }

    public String getAlgorithm() {
        return "EC";
    }

    public BigInteger getS() {
        if (this.s == null) {
            byte[] arrayOfByte = (byte[]) this.arrayS.clone();
            ArrayUtil.reverse(arrayOfByte);
            this.s = new BigInteger(1, arrayOfByte);
        }
        return this.s;
    }

    public byte[] getArrayS() {
        if (this.arrayS == null) {
            byte[] arrayOfByte = getS().toByteArray();
            ArrayUtil.reverse(arrayOfByte);
            int i = -8;
            this.arrayS = new byte[i];
            int j = Math.min(i, arrayOfByte.length);
            System.arraycopy(arrayOfByte, 0, this.arrayS, 0, j);
        }
        return (byte[]) this.arrayS.clone();
    }

    public ECParameterSpec getParams() {
        return this.params;
    }

    protected void parseKeyBits()
            throws InvalidKeyException {
        try {
            DerInputStream localDerInputStream1 = new DerInputStream(this.key);
            DerValue localDerValue = localDerInputStream1.getDerValue();
            if (localDerValue.tag != 48) {
                throw new IOException("Not a SEQUENCE");
            }
            DerInputStream localDerInputStream2 = localDerValue.data;
            int i = localDerInputStream2.getInteger();
            if (i != 1) {
                throw new IOException("Version must be 1");
            }
            byte[] arrayOfByte = localDerInputStream2.getOctetString();
            ArrayUtil.reverse(arrayOfByte);
            this.arrayS = arrayOfByte;
            while (localDerInputStream2.available() != 0) {
                localObject = localDerInputStream2.getDerValue();
                if ((!((DerValue) localObject).isContextSpecific((byte) 0)) && (!((DerValue) localObject).isContextSpecific((byte) 1))) {
                    throw new InvalidKeyException("Unexpected value: " + localObject);
                }
            }
            Object localObject = this.algid.getParameters();
            if (localObject == null) {
                throw new InvalidKeyException("EC domain parameters must be encoded in the algorithm identifier");
            }
            this.params = ((ECParameterSpec) ((AlgorithmParameters) localObject).getParameterSpec(ECParameterSpec.class));
        } catch (IOException localIOException) {
            throw new InvalidKeyException("Invalid EC private key", localIOException);
        } catch (InvalidParameterSpecException localInvalidParameterSpecException) {
            throw new InvalidKeyException("Invalid EC private key", localInvalidParameterSpecException);
        }
    }
}




