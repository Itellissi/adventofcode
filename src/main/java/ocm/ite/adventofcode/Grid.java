package ocm.ite.adventofcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public record Grid<T>(T[][] data) {

    public static <X> Grid<X> of(List<String> rows, Function<String, X[]> rowMapper, Class<X[]> type) {
        X[][] dat = (X[][]) Array.newInstance(type, rows.size());
        for (int i = 0; i < dat.length; i++) {
            dat[i] = rowMapper.apply(rows.get(i));
        }
        return new Grid<>(dat);
    }

    public List<Pair> findAllMatch(Predicate<T> predicate) {
        var result = new ArrayList<Pair>();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (predicate.test(data[i][j])) {
                    result.add(new Pair(j, i));
                }
            }
        }
        return result;
    }

    public void print() {
        print("");
    }

    public void print(String separator) {
        for (var row : data) {
            for (var e : row) {
                System.out.print(e + separator);
            }
            System.out.println();
        }
    }

    public Pair findAnyMatch(Predicate<T> predicate) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (predicate.test(data[i][j])) {
                    return new Pair(j, i);
                }
            }
        }
        return null;
    }

    public void updateElement(int x, int y, T newValue) {
        if (isInBounds(x, y)) {
            data[y][x] = newValue;
        }
    }

    public void updateElement(Pair pos, T newValue) {
        if (isInBounds(pos.x(), pos.y())) {
            data[pos.y()][pos.x()] = newValue;
        }
    }

    public T element(int x, int y) {
        if (isInBounds(x, y)) {
            return data[y][x];
        }
        return null;
    }

    public T element(Pair pos) {
        return element(pos, false);
    }

    public T element(Pair pos, boolean infinite) {
        if (infinite || isInBounds(pos.x(), pos.y())) {
            int actualY = pos.y() % data.length;
            actualY = actualY < 0 ? actualY + data.length : actualY;
            int actualX = pos.x() % data[actualY].length;
            actualX = actualX < 0 ? actualX + data[actualY].length : actualX;
            return data[actualY][actualX];
        }
        return null;
    }

    public boolean isInBounds(int x, int y) {
        return y >= 0 && y < data.length &&
                x >= 0 && x < data[y].length;
    }

    public boolean isInBounds(Pair pos) {
        return pos.y() >= 0 && pos.y() < data.length &&
                pos.x() >= 0 && pos.x() < data[pos.y()].length;
    }
}
