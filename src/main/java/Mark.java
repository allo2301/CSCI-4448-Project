public enum Mark {
    X, O, EMPTY;

    public String display() {
        return this == EMPTY ? "·" : name();
    }

    public Mark opponent() {
        if (this == X) return O;
        if (this == O) return X;
        throw new IllegalStateException("EMPTY has no opponent");
    }
}