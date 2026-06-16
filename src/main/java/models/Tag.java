package models;

public enum Tag {
    CLASSIC("CLASSIC"),
    LIVE("LIVE"),
    REMIX("REMIX"),
    INSTRUMENTAL("INSTRUMENTAL"),
    NEW_RELEASE("NEW RELEASE");

    private final String value;

    // Costruttore dell'enum
    Tag(String value) {
        this.value = value;
    }

    // Metodo per ottenere la stringa pulita
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}