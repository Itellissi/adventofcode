package ocm.ite.adventofcode;

import java.util.ArrayList;
import java.util.List;

public record Range(long min, long max) {

    public static Range of(long min, long max) {
        return new Range(min, max);
    }

    public Range intersect(Range other) {
        long newMin = Math.max(min, other.min());
        long newMax = Math.min(max, other.max());
        return newMin <= newMax ? new Range(newMin, newMax) : null;
    }

    public List<Range> remove(Range other) {
        var innerMax = Math.max(min, other.min) - 1;
        var innerMin = Math.min(max, other.max) + 1;
        var result = new ArrayList<Range>();
        if (innerMax >= min) {
            result.add(new Range(min, Math.min(max, innerMax)));
        }
        if (innerMin <= max) {
            result.add(new Range(Math.max(min, innerMin), max));
        }
        return result;
    }

    public List<Range> merge(Range other) {
        if (Math.max(min, other.min) <= Math.min(max, other.max) + 1) {
            return List.of(new Range(Math.min(min, other.min), Math.max(max, other.max)));
        } else {
            return List.of(this, other);
        }
    }
}
