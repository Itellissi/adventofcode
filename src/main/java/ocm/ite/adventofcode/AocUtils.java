package ocm.ite.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AocUtils {
    public static List<String> readLines(String filePath) {
        return mapLines(filePath, Function.identity());
    }

    public static <T> List<T> mapLines(String filePath, Function<String, T> mapper) {
        try (
                var is = AocUtils.class.getResourceAsStream(filePath);
                var r = new InputStreamReader(Objects.requireNonNull(is));
                var reader = new BufferedReader(r);
        ) {
            return reader.lines()
                    .map(mapper)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}