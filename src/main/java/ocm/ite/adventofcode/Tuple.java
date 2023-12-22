package ocm.ite.adventofcode;

public record Tuple<T>(T... data) {

    public T $(int idx) {
        return data[idx];
    }

    public T x() {
        return $(0);
    }

    public T y() {
        return $(1);
    }

    public T z() {
        return $(2);
    }
}
