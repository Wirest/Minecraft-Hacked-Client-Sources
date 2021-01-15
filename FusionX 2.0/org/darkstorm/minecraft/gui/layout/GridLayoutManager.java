// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

public class GridLayoutManager implements LayoutManager
{
    private int columns;
    private int rows;
    
    public GridLayoutManager(final int columns, final int rows) {
        this.columns = columns;
        this.rows = rows;
    }
    
    @Override
    public void reposition(final Rectangle area, final Rectangle[] componentAreas, final Constraint[][] constraints) {
        if (componentAreas.length == 0) {
            return;
        }
        int componentsPerColumn;
        int componentsPerRow;
        if (this.columns == 0) {
            if (this.rows == 0) {
                final double square = Math.sqrt(componentAreas.length);
                componentsPerColumn = (int)square;
                componentsPerRow = (int)square;
                if (square - (int)square > 0.0) {
                    ++componentsPerColumn;
                }
            }
            else {
                componentsPerRow = componentAreas.length / this.rows;
                if (componentAreas.length % this.rows > 0) {
                    ++componentsPerRow;
                }
                componentsPerColumn = this.rows;
            }
        }
        else if (this.rows == 0) {
            componentsPerColumn = componentAreas.length / this.columns;
            if (componentAreas.length % this.columns > 0) {
                ++componentsPerColumn;
            }
            componentsPerRow = this.columns;
        }
        else {
            componentsPerRow = this.columns;
            componentsPerColumn = this.rows;
        }
        final double elementWidth = area.width / componentsPerRow;
        final double elementHeight = area.height / componentsPerColumn;
    Label_0561:
        for (int row = 0; row < componentsPerColumn; ++row) {
            for (int element = 0; element < componentsPerRow; ++element) {
                final int index = row * componentsPerRow + element;
                if (index >= componentAreas.length) {
                    break Label_0561;
                }
                final Rectangle componentArea = componentAreas[index];
                final Constraint[] componentConstraints = constraints[index];
                HorizontalGridConstraint horizontalAlign = HorizontalGridConstraint.LEFT;
                VerticalGridConstraint verticalAlign = VerticalGridConstraint.CENTER;
                Constraint[] array;
                for (int length = (array = componentConstraints).length, i = 0; i < length; ++i) {
                    final Constraint constraint = array[i];
                    if (constraint instanceof HorizontalGridConstraint) {
                        horizontalAlign = (HorizontalGridConstraint)constraint;
                    }
                    else if (constraint instanceof VerticalGridConstraint) {
                        verticalAlign = (VerticalGridConstraint)constraint;
                    }
                }
                switch (horizontalAlign) {
                    case FILL: {
                        componentArea.width = (int)elementWidth;
                    }
                    case LEFT: {
                        componentArea.x = (int)(area.x + element * elementWidth);
                        break;
                    }
                    case RIGHT: {
                        componentArea.x = (int)(area.x + (element + 1) * elementWidth - componentArea.width);
                        break;
                    }
                    case CENTER: {
                        componentArea.x = (int)(area.x + element * elementWidth + elementWidth / 2.0 - componentArea.width / 2);
                        break;
                    }
                }
                switch (verticalAlign) {
                    case FILL: {
                        componentArea.height = (int)elementHeight;
                    }
                    case TOP: {
                        componentArea.y = (int)(area.y + row * elementHeight);
                        break;
                    }
                    case BOTTOM: {
                        componentArea.y = (int)(area.y + (row + 1) * elementHeight - componentArea.height);
                        break;
                    }
                    case CENTER: {
                        componentArea.y = (int)(area.y + row * elementHeight + elementHeight / 2.0 - componentArea.height / 2);
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public Dimension getOptimalPositionedSize(final Rectangle[] componentAreas, final Constraint[][] constraints) {
        if (componentAreas.length == 0) {
            return new Dimension(0, 0);
        }
        int componentsPerColumn;
        int componentsPerRow;
        if (this.columns == 0) {
            if (this.rows == 0) {
                final double square = Math.sqrt(componentAreas.length);
                componentsPerColumn = (int)square;
                componentsPerRow = (int)square;
                if (square - (int)square > 0.0) {
                    ++componentsPerColumn;
                }
            }
            else {
                componentsPerRow = componentAreas.length / this.rows;
                if (componentAreas.length % this.rows > 0) {
                    ++componentsPerRow;
                }
                componentsPerColumn = this.rows;
            }
        }
        else if (this.rows == 0) {
            componentsPerColumn = componentAreas.length / this.columns;
            if (componentAreas.length % this.columns > 0) {
                ++componentsPerColumn;
            }
            componentsPerRow = this.columns;
        }
        else {
            componentsPerRow = this.columns;
            componentsPerColumn = this.rows;
        }
        int maxElementWidth = 0;
        int maxElementHeight = 0;
        for (final Rectangle component : componentAreas) {
            maxElementWidth = Math.max(maxElementWidth, component.width);
            maxElementHeight = Math.max(maxElementHeight, component.height);
        }
        return new Dimension(maxElementWidth * componentsPerRow, maxElementHeight * componentsPerColumn);
    }
    
    public int getColumns() {
        return this.columns;
    }
    
    public int getRows() {
        return this.rows;
    }
    
    public void setColumns(final int columns) {
        this.columns = columns;
    }
    
    public void setRows(final int rows) {
        this.rows = rows;
    }
    
    public enum HorizontalGridConstraint implements Constraint
    {
        CENTER("CENTER", 0), 
        LEFT("LEFT", 1), 
        RIGHT("RIGHT", 2), 
        FILL("FILL", 3);
        
        private HorizontalGridConstraint(final String s, final int n) {
        }
    }
    
    public enum VerticalGridConstraint implements Constraint
    {
        CENTER("CENTER", 0), 
        TOP("TOP", 1), 
        BOTTOM("BOTTOM", 2), 
        FILL("FILL", 3);
        
        private VerticalGridConstraint(final String s, final int n) {
        }
    }
}
