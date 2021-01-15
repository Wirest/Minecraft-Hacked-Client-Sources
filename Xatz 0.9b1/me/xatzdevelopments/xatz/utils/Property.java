package me.xatzdevelopments.xatz.utils;

/**
 * @author antja03
 */
public abstract class Property<T> {

    private String id;
    private String description;
    public T value;
    public T defaultValue;
    private me.xatzdevelopments.xatz.utils.Dependency dependency;

    protected Property(String id, String description, me.xatzdevelopments.xatz.utils.Dependency dependency) {
        this.id = id;
        this.description = description;
        this.dependency = dependency;
    }

    public abstract void setValue(String input);

    public void setDefault() {
        value = defaultValue;
    }

    public String getId() {
        return id;
    }

    public String getDescription() { return this.description; }

    public T getValue() {
        return value;
    }

    public String getValueAsString() {
        return String.valueOf(value);
    }

    public void setValue(T input) {
        value = input;
    }

    public boolean checkDependency() {
        if (dependency == null) {
            return true;
        } else {
            return dependency.check();
        }
    }

    public interface Dependency {
        boolean check();
    }
}
