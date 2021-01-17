// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.internal;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Rows
{
    private final int overallWidth;
    private final int columnSeparatorWidth;
    private final Set<Row> rows;
    private int widthOfWidestOption;
    private int widthOfWidestDescription;
    
    public Rows(final int overallWidth, final int columnSeparatorWidth) {
        this.rows = new LinkedHashSet<Row>();
        this.overallWidth = overallWidth;
        this.columnSeparatorWidth = columnSeparatorWidth;
    }
    
    public void add(final String option, final String description) {
        this.add(new Row(option, description));
    }
    
    private void add(final Row row) {
        this.rows.add(row);
        this.widthOfWidestOption = Math.max(this.widthOfWidestOption, row.option.length());
        this.widthOfWidestDescription = Math.max(this.widthOfWidestDescription, row.description.length());
    }
    
    private void reset() {
        this.rows.clear();
        this.widthOfWidestOption = 0;
        this.widthOfWidestDescription = 0;
    }
    
    public void fitToWidth() {
        final Columns columns = new Columns(this.optionWidth(), this.descriptionWidth());
        final Set<Row> fitted = new LinkedHashSet<Row>();
        for (final Row each : this.rows) {
            fitted.addAll(columns.fit(each));
        }
        this.reset();
        for (final Row each : fitted) {
            this.add(each);
        }
    }
    
    public String render() {
        final StringBuilder buffer = new StringBuilder();
        for (final Row each : this.rows) {
            this.pad(buffer, each.option, this.optionWidth()).append(Strings.repeat(' ', this.columnSeparatorWidth));
            this.pad(buffer, each.description, this.descriptionWidth()).append(Strings.LINE_SEPARATOR);
        }
        return buffer.toString();
    }
    
    private int optionWidth() {
        return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestOption);
    }
    
    private int descriptionWidth() {
        return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestDescription);
    }
    
    private StringBuilder pad(final StringBuilder buffer, final String s, final int length) {
        buffer.append(s).append(Strings.repeat(' ', length - s.length()));
        return buffer;
    }
}
