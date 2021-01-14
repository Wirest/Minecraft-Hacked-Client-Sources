package cedo.ui.changelog;

public class Change {
    String description;
    ChangeType type;

    public Change(String description, ChangeType type) {
        this.description = description;
        this.type = type;
    }

    public Change(ChangeType type, String description) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public ChangeType getType() {
        return type;
    }

}
