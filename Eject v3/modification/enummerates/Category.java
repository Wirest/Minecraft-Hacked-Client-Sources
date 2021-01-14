package modification.enummerates;

public enum Category {
    COMBAT, MISC, MOVEMENT, PLAYER, VISUALS, WORLD;

    public final String displayName = name().substring(0, 1).toUpperCase().concat(name().substring(1).toLowerCase()).replaceAll("Misc", "Miscellaneous");

    private Category() {
    }
}




