package net.optifine.entity.model.anim;

public class ConstantFloat implements IExpressionFloat
{
    private float value;

    public ConstantFloat(float value)
    {
        this.value = value;
    }

    public float eval()
    {
        return this.value;
    }

    public ExpressionType getExpressionType()
    {
        return ExpressionType.FLOAT;
    }

    public String toString()
    {
        return "" + this.value;
    }
}
