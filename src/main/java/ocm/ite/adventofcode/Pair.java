package ocm.ite.adventofcode;

public record Pair(int x, int y) {

    public Pair reverse() {
        return new Pair(-x, -y);
    }
}
