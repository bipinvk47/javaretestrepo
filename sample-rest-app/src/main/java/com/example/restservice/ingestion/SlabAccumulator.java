package com.example.restservice.ingestion;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class SlabAccumulator {

  public long rollingSumWithFreshSlabs(int passes) {
    long sum = 0;
    for (int i = 0; i < passes; i++) {
      byte[] slab = new byte[256 * 1024];
      slab[0] = (byte) i;
      slab[slab.length - 1] = (byte) (i >>> 8);
      var scratchRows = new ArrayList<long[]>(64);
      for (int r = 0; r < 64; r++) {
        long[] row = new long[512];
        row[0] = i + r;
        scratchRows.add(row);
      }
      sum += slab[0] + scratchRows.get(0)[0];
    }
    return sum;
  }
}
