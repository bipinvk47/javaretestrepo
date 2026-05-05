package com.bejava.whitebox.coverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllUsesCoverageSummaryTest {

    @Test
    void mergeCombinesCountersAndRaisesComparableTotals() {
        AllUsesCoverageSummary a =
                new AllUsesCoverageSummary(new CounterTriple(1, 9), new CounterTriple(2, 8), new CounterTriple(0, 10));
        AllUsesCoverageSummary b =
                new AllUsesCoverageSummary(new CounterTriple(4, 6), new CounterTriple(1, 4), new CounterTriple(5, 5));
        AllUsesCoverageSummary m = a.merge(b);
        assertEquals(20, m.instructions().total());
        assertEquals(15, m.branches().total());
        assertEquals(20, m.lines().total());
    }
}
