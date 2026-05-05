package com.bejava.whitebox.churn;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderedLineChurnAnalyzerTest {

    @Test
    void analyzeCountsAddsAndRemovesAgainstLongestSharedLines() {
        CodeChurnMetrics metrics =
                OrderedLineChurnAnalyzer.analyze("alpha\nbeta\ngamma\n", "alpha\nomega\ngamma\n");
        assertEquals(1L, metrics.linesRemoved());
        assertEquals(1L, metrics.linesAdded());
        assertEquals(2L, metrics.churnLines());
    }

    @Test
    void splitLinesHandlesMixedNewlines() {
        List<String> lines = OrderedLineChurnAnalyzer.splitLines("x\r\ny\rz");
        assertEquals(List.of("x", "y", "z"), lines);
    }
}
