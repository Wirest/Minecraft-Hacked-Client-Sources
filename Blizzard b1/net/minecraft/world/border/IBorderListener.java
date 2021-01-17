package net.minecraft.world.border;

public interface IBorderListener
{
    void onSizeChanged(WorldBorder border, double newSize);

    void func_177692_a(WorldBorder border, double p_177692_2_, double p_177692_4_, long p_177692_6_);

    void onCenterChanged(WorldBorder border, double x, double z);

    void onWarningTimeChanged(WorldBorder border, int p_177691_2_);

    void onWarningDistanceChanged(WorldBorder border, int p_177690_2_);

    void func_177696_b(WorldBorder border, double p_177696_2_);

    void func_177695_c(WorldBorder border, double p_177695_2_);
}
