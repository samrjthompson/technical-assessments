package org.example.problems.jpmorgan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class MoneyLendingTest {

    private final MoneyLending moneyLending = new MoneyLending();

    @Test
    void resolveTransactions() {
        // given
        List<String> input = List.of("alice,bob,10", "bob,chris,5", "alice,chris,5", "chris,alice,5");
        List<String> expected = Stream.of("bob,alice,5", "chris,alice,5").sorted().toList();

        // when
        final List<String> actual = moneyLending.resolveTransactions(input);

        // then
        assertEquals(expected, actual);
    }
}