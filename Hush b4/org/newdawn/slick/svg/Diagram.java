// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import java.util.HashMap;
import java.util.ArrayList;

public class Diagram
{
    private ArrayList figures;
    private HashMap patterns;
    private HashMap gradients;
    private HashMap figureMap;
    private float width;
    private float height;
    
    public Diagram(final float width, final float height) {
        this.figures = new ArrayList();
        this.patterns = new HashMap();
        this.gradients = new HashMap();
        this.figureMap = new HashMap();
        this.width = width;
        this.height = height;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void addPatternDef(final String name, final String href) {
        this.patterns.put(name, href);
    }
    
    public void addGradient(final String name, final Gradient gradient) {
        this.gradients.put(name, gradient);
    }
    
    public String getPatternDef(final String name) {
        return this.patterns.get(name);
    }
    
    public Gradient getGradient(final String name) {
        return this.gradients.get(name);
    }
    
    public String[] getPatternDefNames() {
        return (String[])this.patterns.keySet().toArray(new String[0]);
    }
    
    public Figure getFigureByID(final String id) {
        return this.figureMap.get(id);
    }
    
    public void addFigure(final Figure figure) {
        this.figures.add(figure);
        this.figureMap.put(figure.getData().getAttribute("id"), figure);
        final String fillRef = figure.getData().getAsReference("fill");
        final Gradient gradient = this.getGradient(fillRef);
        if (gradient != null && gradient.isRadial()) {
            for (int i = 0; i < InkscapeLoader.RADIAL_TRIANGULATION_LEVEL; ++i) {
                figure.getShape().increaseTriangulation();
            }
        }
    }
    
    public int getFigureCount() {
        return this.figures.size();
    }
    
    public Figure getFigure(final int index) {
        return this.figures.get(index);
    }
    
    public void removeFigure(final Figure figure) {
        this.figures.remove(figure);
        this.figureMap.remove(figure.getData().getAttribute("id"));
    }
}
