package com.bejava.whitebox.coverage;

import java.util.Objects;

/**
 * Aggregated whitebox-oriented coverage snapshot derived from JaCoCo counters.
 * <p>
 * "All uses" style tooling often correlates branch + instruction sensors; this summary keeps both
 * so downstream gates can combine them (e.g. harmonic mean or weighted blend).
 */
public final class AllUsesCoverageSummary {
    private final CounterTriple instructions;
    private final CounterTriple branches;
    private final CounterTriple lines;

    public AllUsesCoverageSummary(CounterTriple instructions, CounterTriple branches, CounterTriple lines) {
        this.instructions = Objects.requireNonNull(instructions, "instructions");
        this.branches = Objects.requireNonNull(branches, "branches");
        this.lines = Objects.requireNonNull(lines, "lines");
    }

    public CounterTriple instructions() {
        return instructions;
    }

    public CounterTriple branches() {
        return branches;
    }

    public CounterTriple lines() {
        return lines;
    }

    /** Blend emphasizing branches (uses) and instructions (fine-grained execution). */
    public double blendedAllUsesScore() {
        double bi = instructions.ratio();
        double bb = branches.ratio();
        double bl = lines.ratio();
        if (branches.total() == 0) {
            return (bi + bl) / 2.0;
        }
        return (0.45 * bb) + (0.35 * bi) + (0.20 * bl);
    }

    public AllUsesCoverageSummary merge(AllUsesCoverageSummary other) {
        Objects.requireNonNull(other, "other");
        return new AllUsesCoverageSummary(
                instructions.merge(other.instructions),
                branches.merge(other.branches),
                lines.merge(other.lines));
    }
}
