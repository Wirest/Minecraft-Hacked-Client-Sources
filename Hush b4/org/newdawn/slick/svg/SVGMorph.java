// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.MorphShape;
import java.util.ArrayList;

public class SVGMorph extends Diagram
{
    private ArrayList figures;
    
    public SVGMorph(final Diagram diagram) {
        super(diagram.getWidth(), diagram.getHeight());
        this.figures = new ArrayList();
        for (int i = 0; i < diagram.getFigureCount(); ++i) {
            final Figure figure = diagram.getFigure(i);
            final Figure copy = new Figure(figure.getType(), new MorphShape(figure.getShape()), figure.getData(), figure.getTransform());
            this.figures.add(copy);
        }
    }
    
    public void addStep(final Diagram diagram) {
        if (diagram.getFigureCount() != this.figures.size()) {
            throw new RuntimeException("Mismatched diagrams, missing ids");
        }
        for (int i = 0; i < diagram.getFigureCount(); ++i) {
            final Figure figure = diagram.getFigure(i);
            final String id = figure.getData().getMetaData();
            for (int j = 0; j < this.figures.size(); ++j) {
                final Figure existing = this.figures.get(j);
                if (existing.getData().getMetaData().equals(id)) {
                    final MorphShape morph = (MorphShape)existing.getShape();
                    morph.addShape(figure.getShape());
                    break;
                }
            }
        }
    }
    
    public void setExternalDiagram(final Diagram diagram) {
        for (int i = 0; i < this.figures.size(); ++i) {
            final Figure figure = this.figures.get(i);
            for (int j = 0; j < diagram.getFigureCount(); ++j) {
                final Figure newBase = diagram.getFigure(j);
                if (newBase.getData().getMetaData().equals(figure.getData().getMetaData())) {
                    final MorphShape shape = (MorphShape)figure.getShape();
                    shape.setExternalFrame(newBase.getShape());
                    break;
                }
            }
        }
    }
    
    public void updateMorphTime(final float delta) {
        for (int i = 0; i < this.figures.size(); ++i) {
            final Figure figure = this.figures.get(i);
            final MorphShape shape = (MorphShape)figure.getShape();
            shape.updateMorphTime(delta);
        }
    }
    
    public void setMorphTime(final float time) {
        for (int i = 0; i < this.figures.size(); ++i) {
            final Figure figure = this.figures.get(i);
            final MorphShape shape = (MorphShape)figure.getShape();
            shape.setMorphTime(time);
        }
    }
    
    @Override
    public int getFigureCount() {
        return this.figures.size();
    }
    
    @Override
    public Figure getFigure(final int index) {
        return this.figures.get(index);
    }
}
