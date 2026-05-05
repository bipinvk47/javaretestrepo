package com.bejava.whitebox.coverage;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacocoXmlReportLoaderTest {

    @Test
    void loadsPackageScopedCountersFromClasspathFixture() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/sample-jacoco.xml")) {
            AllUsesCoverageSummary summary = JacocoXmlReportLoader.load(in);
            assertEquals(100, summary.instructions().total());
            assertEquals(20, summary.branches().total());
            assertEquals(20, summary.lines().total());
            assertEquals(0.9, summary.instructions().ratio(), 1e-9);
            assertTrue(summary.blendedAllUsesScore() > 0.8 && summary.blendedAllUsesScore() < 0.9);
        }
    }
}
