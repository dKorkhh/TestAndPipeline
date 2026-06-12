package org.example.exception;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Suite
@SelectPackages("org.example.exception")
@IncludeTags("validation")
class CounterServiceTest {
    private final CounterService sut = new CounterService();

    @Tag("validation")
    @ParameterizedTest
    @ValueSource(ints = {0, 50, 99})
    void shouldReturnTrueForValidSalary(int gross) {
        assertTrue(sut.isValidSalary(gross));
    }

    @Tag("exception")
    @ParameterizedTest
    @MethodSource("invalidGrossValues")
    void shouldThrowExpectedException(int gross, Class<? extends RuntimeException> expected) {
        assertThrows(expected, () -> sut.countSalary(gross));
    }

    private static Stream<Arguments> invalidGrossValues() {
        return Stream.of(
                Arguments.of(-1, LessThenZeroException.class),
                Arguments.of(101, BiggerThenHundredException.class)
        );
    }

    record Case(int gross, boolean ok, double expected, Class<? extends RuntimeException> exception) {}

    @Tag("exception")
    @TestFactory
    List<DynamicTest> salaryTests() {
        List<Case> cases = List.of(
                new Case(-1, false, 0, LessThenZeroException.class),
                new Case(50, true, 60.0, null),
                new Case(100, true, 120.0, null),
                new Case(101, false, 0, BiggerThenHundredException.class)
        );

        return cases.stream()
                .map(test -> DynamicTest.dynamicTest(
                        "gross=" + test.gross(),
                        () -> {
                            if (test.exception() != null) {
                                assertThrows(test.exception(), () -> sut.countSalary(test.gross()));
                            }
                            else assertEquals(test.expected(), sut.countSalary(test.gross()));
                        }
                ))
                .toList();
    }

    @Tag("assumption")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 50, 100, 150})
    void shouldCalculateSalaryOnlyForValidRange(int gross) {
        Assumptions.assumeTrue(gross >= 0 && gross <= 100, "Gross is out of valid range, skipping test");

        double result = sut.countSalary(gross);

        assertEquals(gross * 1.2, result);
    }
}