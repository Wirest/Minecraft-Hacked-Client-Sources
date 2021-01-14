package shadersmod.uniform;

import org.lwjgl.opengl.ARBShaderObjects;
import shadersmod.client.Shaders;

public class ShaderUniformFloat extends ShaderUniformBase
{
    private float value = -1.0F;

    public ShaderUniformFloat(String name)
    {
        super(name);
    }

    protected void onProgramChanged()
    {
        this.value = -1.0F;
    }

    public void setValue(float value)
    {
        if (this.getLocation() >= 0)
        {
            if (this.value != value)
            {
                ARBShaderObjects.glUniform1fARB(this.getLocation(), value);
                Shaders.checkGLError(this.getName());
                this.value = value;
            }
        }
    }

    public float getValue()
    {
        return this.value;
    }
}
