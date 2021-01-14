package net.minecraft.world.border;

public interface IBorderListener {
    void onSizeChanged(WorldBorder var1, double var2);

    void func_177692_a(WorldBorder var1, double var2, double var4, long var6);

    void onCenterChanged(WorldBorder var1, double var2, double var4);

    void onWarningTimeChanged(WorldBorder var1, int var2);

    void onWarningDistanceChanged(WorldBorder var1, int var2);

    void func_177696_b(WorldBorder var1, double var2);

    void func_177695_c(WorldBorder var1, double var2);
}
