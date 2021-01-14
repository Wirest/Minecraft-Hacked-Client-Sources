package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformFloat4
        extends ShaderUniformBase {
    private float[] values = new float[4];

    public ShaderUniformFloat4(String paramString) {
        super(paramString);
    }

    protected void onProgramChanged() {
        this.values[0] = 0.0F;
        this.values[1] = 0.0F;
        this.values[2] = 0.0F;
        this.values[3] = 0.0F;
    }

    public void setValue(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        if ((getLocation() >= 0) && ((this.values[0] != paramFloat1) || (this.values[1] != paramFloat2) || (this.values[2] != paramFloat3) || (this.values[3] != paramFloat4))) {
            ARBShaderObjects.glUniform4fARB(getLocation(), paramFloat1, paramFloat2, paramFloat3, paramFloat4);
            Shaders.checkGLError(getName());
            this.values[0] = paramFloat1;
            this.values[1] = paramFloat2;
            this.values[2] = paramFloat3;
            this.values[3] = paramFloat4;
        }
    }

    public float[] getValues() {
        return this.values;
    }
}




