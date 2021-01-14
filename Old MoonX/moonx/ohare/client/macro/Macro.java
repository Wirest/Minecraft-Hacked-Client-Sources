package moonx.ohare.client.macro;

public class Macro {
    private String label, text;
    private int key;

    public Macro(String label, int key, String text) {
        this.label = label;
        this.text = text;
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }

    public int getKey() {
        return key;
    }
}
