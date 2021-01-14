package sun.security.ec;

import sun.security.util.DerValue;
import sun.security.util.ECKeySizeParameterSpec;
import sun.security.util.ObjectIdentifier;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.AlgorithmParametersSpi;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

public final class ECParameters
        extends AlgorithmParametersSpi {
    private NamedCurve namedCurve;

    static AlgorithmParameters getAlgorithmParameters(ECParameterSpec paramECParameterSpec)
            throws InvalidKeyException {
        try {
            AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("EC", "SunEC");
            localAlgorithmParameters.init(paramECParameterSpec);
            return localAlgorithmParameters;
        } catch (GeneralSecurityException localGeneralSecurityException) {
            throw new InvalidKeyException("EC parameters error", localGeneralSecurityException);
        }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
            throws InvalidParameterSpecException {
        if (paramAlgorithmParameterSpec == null) {
            throw new InvalidParameterSpecException("paramSpec must not be null");
        }
        if ((paramAlgorithmParameterSpec instanceof NamedCurve)) {
            this.namedCurve = ((NamedCurve) paramAlgorithmParameterSpec);
            return;
        }
        if ((paramAlgorithmParameterSpec instanceof ECParameterSpec)) {
            this.namedCurve = CurveDB.lookup((ECParameterSpec) paramAlgorithmParameterSpec);
        } else if ((paramAlgorithmParameterSpec instanceof ECGenParameterSpec)) {
            String str = ((ECGenParameterSpec) paramAlgorithmParameterSpec).getName();
            this.namedCurve = CurveDB.lookup(str);
        } else if ((paramAlgorithmParameterSpec instanceof ECKeySizeParameterSpec)) {
            int i = ((ECKeySizeParameterSpec) paramAlgorithmParameterSpec).getKeySize();
            this.namedCurve = CurveDB.lookup(i);
        } else {
            throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
        }
        if (this.namedCurve == null) {
            throw new InvalidParameterSpecException("Not a supported curve: " + paramAlgorithmParameterSpec);
        }
    }

    protected void engineInit(byte[] paramArrayOfByte)
            throws IOException {
        DerValue localDerValue = new DerValue(paramArrayOfByte);
        if (localDerValue.tag == 6) {
            ObjectIdentifier localObjectIdentifier = localDerValue.getOID();
            NamedCurve localNamedCurve = CurveDB.lookup(localObjectIdentifier.toString());
            if (localNamedCurve == null) {
                throw new IOException("Unknown named curve: " + localObjectIdentifier);
            }
            this.namedCurve = localNamedCurve;
            return;
        }
        throw new IOException("Only named ECParameters supported");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
            throws IOException {
        engineInit(paramArrayOfByte);
    }

    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> paramClass)
            throws InvalidParameterSpecException {
        if (paramClass.isAssignableFrom(ECParameterSpec.class)) {
            return (AlgorithmParameterSpec) paramClass.cast(this.namedCurve);
        }
        if (paramClass.isAssignableFrom(ECGenParameterSpec.class)) {
            String str = this.namedCurve.getObjectId();
            return (AlgorithmParameterSpec) paramClass.cast(new ECGenParameterSpec(str));
        }
        if (paramClass.isAssignableFrom(ECKeySizeParameterSpec.class)) {
            int i = this.namedCurve.getCurve().getField().getFieldSize();
            return (AlgorithmParameterSpec) paramClass.cast(new ECKeySizeParameterSpec(i));
        }
        throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
    }

    protected byte[] engineGetEncoded()
            throws IOException {
        return this.namedCurve.getEncoded();
    }

    protected byte[] engineGetEncoded(String paramString)
            throws IOException {
        return engineGetEncoded();
    }

    protected String engineToString() {
        if (this.namedCurve == null) {
            return "Not initialized";
        }
        return this.namedCurve.toString();
    }
}




