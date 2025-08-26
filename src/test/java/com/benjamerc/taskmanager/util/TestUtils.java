package com.benjamerc.taskmanager.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static <T, K extends Comparable<K>> List<T> sortAndAssertSize(
            T[] expectedArray,
            T[] actualArray,
            Function<T, K> idExtractor
    ) {
        List<T> expectedList = Arrays.stream(expectedArray)
                .sorted(Comparator.comparing(idExtractor))
                .toList();

        List<T> actualList = Arrays.stream(actualArray)
                .sorted(Comparator.comparing(idExtractor))
                .toList();

        assertEquals(expectedList.size(), actualList.size());

        return actualList;
    }
}
