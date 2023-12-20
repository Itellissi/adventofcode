package ocm.ite.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

    public static long ppcm(long nb1, long nb2) {
        long product, rest, ppcm;

        product = nb1 * nb2;
        rest = nb1 % nb2;
        while (rest != 0) {
            nb1 = nb2;
            nb2 = rest;
            rest = nb1 % nb2;
        }
        ppcm = product / nb2;
        return ppcm;
    }

    public static BigInteger ppcm(BigInteger nb1, BigInteger nb2) {
        BigInteger product, rest, ppcm;

        product = nb1.multiply(nb2);
        rest = nb1.mod(nb2);
        while (!rest.equals(BigInteger.ZERO)) {
            nb1 = nb2;
            nb2 = rest;
            rest = nb1.mod(nb2);
        }
        ppcm = product.divide(nb2);
        return ppcm;
    }
}