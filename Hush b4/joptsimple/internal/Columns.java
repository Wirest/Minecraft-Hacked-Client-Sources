// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.internal;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

class Columns
{
    private static final int INDENT_WIDTH = 2;
    private final int optionWidth;
    private final int descriptionWidth;
    
    Columns(final int optionWidth, final int descriptionWidth) {
        this.optionWidth = optionWidth;
        this.descriptionWidth = descriptionWidth;
    }
    
    List<Row> fit(final Row row) {
        final List<String> options = this.piecesOf(row.option, this.optionWidth);
        final List<String> descriptions = this.piecesOf(row.description, this.descriptionWidth);
        final List<Row> rows = new ArrayList<Row>();
        for (int i = 0; i < Math.max(options.size(), descriptions.size()); ++i) {
            rows.add(new Row(itemOrEmpty(options, i), itemOrEmpty(descriptions, i)));
        }
        return rows;
    }
    
    private static String itemOrEmpty(final List<String> items, final int index) {
        return (index >= items.size()) ? "" : items.get(index);
    }
    
    private List<String> piecesOf(final String raw, final int width) {
        final List<String> pieces = new ArrayList<String>();
        for (final String each : raw.trim().split(Strings.LINE_SEPARATOR)) {
            pieces.addAll(this.piecesOfEmbeddedLine(each, width));
        }
        return pieces;
    }
    
    private List<String> piecesOfEmbeddedLine(final String line, final int width) {
        final List<String> pieces = new ArrayList<String>();
        final BreakIterator words = BreakIterator.getLineInstance(Locale.US);
        words.setText(line);
        StringBuilder nextPiece = new StringBuilder();
        int start = words.first();
        for (int end = words.next(); end != -1; end = words.next()) {
            nextPiece = this.processNextWord(line, nextPiece, start, end, width, pieces);
            start = end;
        }
        if (nextPiece.length() > 0) {
            pieces.add(nextPiece.toString());
        }
        return pieces;
    }
    
    private StringBuilder processNextWord(final String source, final StringBuilder nextPiece, final int start, final int end, final int width, final List<String> pieces) {
        StringBuilder augmented = nextPiece;
        final String word = source.substring(start, end);
        if (augmented.length() + word.length() > width) {
            pieces.add(augmented.toString().replaceAll("\\s+$", ""));
            augmented = new StringBuilder(Strings.repeat(' ', 2)).append(word);
        }
        else {
            augmented.append(word);
        }
        return augmented;
    }
}
