package ocm.ite.adventofcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class RangeTest {

    public static Stream<Arguments> intersect() {
        return Stream.of(
                arguments(
                        Range.of(1, 4), Range.of(2, 3), Range.of(2, 3)
                ),
                arguments(
                        Range.of(1, 4), Range.of(2, 5), Range.of(2, 4)
                ),
                arguments(
                        Range.of(1, 4), Range.of(5, 10), null
                )
        );
    }

    public static Stream<Arguments> remove() {
        return Stream.of(
                arguments(
                        Range.of(1, 4), Range.of(2, 3), List.of(Range.of(1, 1), Range.of(4, 4))
                ),
                arguments(
                        Range.of(1, 4), Range.of(2, 10), List.of(Range.of(1, 1))
                ),
                arguments(
                        Range.of(1, 4), Range.of(0, 3), List.of(Range.of(4, 4))
                ),
                arguments(
                        Range.of(1, 4), Range.of(0, 5), Collections.emptyList()
                )
        );
    }

    public static Stream<Arguments> merge() {
        return Stream.of(
                arguments(
                        Range.of(1, 4), Range.of(2, 3), List.of(Range.of(1, 4))
                ),
                arguments(
                        Range.of(1, 4), Range.of(2, 10), List.of(Range.of(1, 10))
                ),
                arguments(
                        Range.of(1, 4), Range.of(0, 3), List.of(Range.of(0, 4))
                ),
                arguments(
                        Range.of(1, 4), Range.of(0, 5), List.of(Range.of(0, 5))
                ),
                arguments(
                        Range.of(1, 4), Range.of(5, 10), List.of(Range.of(1, 10))
                ),
                arguments(
                        Range.of(1, 4), Range.of(6, 10), List.of(Range.of(1, 4), Range.of(6, 10))
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void intersect(Range left, Range right, Range expected) {
        var result1 = left.intersect(right);
        var result2 = right.intersect(left);

        Assertions.assertEquals(result1, result2);
        Assertions.assertEquals(result1, expected);
    }

    @ParameterizedTest
    @MethodSource
    void remove(Range left, Range right, List<Range> expected) {
        var result = left.remove(right);

        Assertions.assertEquals(result, expected);
    }

    @ParameterizedTest
    @MethodSource
    void merge(Range left, Range right, List<Range> expected) {
        var result = left.merge(right);

        Assertions.assertEquals(result, expected);
    }
}
