package com.bejava.whitebox.coverage;

import java.util.Objects;

/** Holds JaCoCo-style missed/covered/total counters for one metric bucket. */
public final class CounterTriple {

    public static final CounterTriple EMPTY = new CounterTriple(0, 0);

    private final long missed;
    private final long covered;

    public CounterTriple(long missed, long covered) {
        if (missed < 0 || covered < 0) {
            throw new IllegalArgumentException("Counters must be non-negative");
        }
        this.missed = missed;
        this.covered = covered;
    }

    public long missed() {
        return missed;
    }

    public long covered() {
        return covered;
    }

    public long total() {
        return missed + covered;
    }

    public double ratio() {
        long t = total();
        return t == 0 ? 1.0 : (double) covered / t;
    }

    public CounterTriple merge(CounterTriple other) {
        Objects.requireNonNull(other, "other");
        return new CounterTriple(this.missed + other.missed, this.covered + other.covered);
    }
}
