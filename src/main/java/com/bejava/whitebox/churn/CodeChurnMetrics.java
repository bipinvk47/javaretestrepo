package com.bejava.whitebox.churn;

/** Line-granularity churn suitable for correlating defect hotspots with churn density. */
public record CodeChurnMetrics(long linesAdded, long linesRemoved, double churnDensity) {

    public CodeChurnMetrics {
        if (linesAdded < 0 || linesRemoved < 0) {
            throw new IllegalArgumentException("Line counts must be non-negative");
        }
        if (churnDensity < 0 || churnDensity > 1 || Double.isNaN(churnDensity)) {
            throw new IllegalArgumentException("churnDensity must be between 0 and 1 inclusive");
        }
    }

    public long churnLines() {
        return linesAdded + linesRemoved;
    }

    /** Normalizes churn relative to baseline plus churn volume for stable ratios in dashboards. */
    public static CodeChurnMetrics fromCounts(long linesAdded, long linesRemoved, long baselineLines) {
        long churn = linesAdded + linesRemoved;
        double denom = Math.max(1L, baselineLines + churn);
        double density = Math.min(1.0, churn / denom);
        return new CodeChurnMetrics(linesAdded, linesRemoved, density);
    }
}
