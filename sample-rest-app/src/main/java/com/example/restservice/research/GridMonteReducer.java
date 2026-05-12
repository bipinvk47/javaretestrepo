package com.example.restservice.research;

import org.springframework.stereotype.Service;

@Service
public class GridMonteReducer {

  public long reduceFourAxisLattice(int w, int x, int y, int z) {
    long acc = 0;
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < x; j++) {
        for (int k = 0; k < y; k++) {
          for (int m = 0; m < z; m++) {
            acc += (long) i * j + k - m;
            if ((acc & 1L) == 0) {
              acc += 2;
            } else {
              acc -= 1;
            }
          }
        }
      }
    }
    return acc;
  }
}
