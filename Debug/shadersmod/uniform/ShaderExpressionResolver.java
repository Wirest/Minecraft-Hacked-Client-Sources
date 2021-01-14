package shadersmod.uniform;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.entity.model.anim.ConstantFloat;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IExpressionResolver;
import shadersmod.common.SMCLog;

public class ShaderExpressionResolver implements IExpressionResolver
{
    private Map<String, IExpression> mapExpressions = new HashMap();

    public ShaderExpressionResolver(Map<String, IExpression> map)
    {
        this.registerExpressions();

        for (String s : map.keySet())
        {
            IExpression iexpression = (IExpression)map.get(s);
            this.registerExpression(s, iexpression);
        }
    }

    private void registerExpressions()
    {
        ShaderParameterFloat[] ashaderparameterfloat = ShaderParameterFloat.values();

        for (int i = 0; i < ashaderparameterfloat.length; ++i)
        {
            ShaderParameterFloat shaderparameterfloat = ashaderparameterfloat[i];
            this.mapExpressions.put(shaderparameterfloat.getName(), shaderparameterfloat);
        }

        ShaderParameterBool[] ashaderparameterbool = ShaderParameterBool.values();

        for (int k = 0; k < ashaderparameterbool.length; ++k)
        {
            ShaderParameterBool shaderparameterbool = ashaderparameterbool[k];
            this.mapExpressions.put(shaderparameterbool.getName(), shaderparameterbool);
        }

        BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();

        for (int l = 0; l < abiomegenbase.length; ++l)
        {
            BiomeGenBase biomegenbase = abiomegenbase[l];

            if (biomegenbase != null)
            {
                String s = biomegenbase.biomeName.trim();
                s = "BIOME_" + s.toUpperCase().replace(' ', '_');
                int j = biomegenbase.biomeID;
                IExpression iexpression = new ConstantFloat((float)j);
                this.registerExpression(s, iexpression);
            }
        }
    }

    public boolean registerExpression(String name, IExpression expr)
    {
        if (this.mapExpressions.containsKey(name))
        {
            SMCLog.warning("Expression already defined: " + name);
            return false;
        }
        else
        {
            this.mapExpressions.put(name, expr);
            return true;
        }
    }

    public IExpression getExpression(String name)
    {
        return (IExpression)this.mapExpressions.get(name);
    }

    public boolean hasExpression(String name)
    {
        return this.mapExpressions.containsKey(name);
    }
}
