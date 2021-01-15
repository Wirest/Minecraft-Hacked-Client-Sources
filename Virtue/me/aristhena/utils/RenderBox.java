package me.aristhena.utils;

public class RenderBox {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public RenderBox(double p1, double p2, double p3, double p4, double p5, double p6) {
        this.minX = p1;
        this.minY = p2;
        this.minZ = p3;
        this.maxX = p4;
        this.maxZ = p5;
        this.maxY = p5;
    }
}


