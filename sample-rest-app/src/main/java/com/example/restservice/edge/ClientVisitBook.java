package com.example.restservice.edge;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ClientVisitBook {

  private long visitsRecorded;
  private final Map<String, Integer> dedupeScratch = new HashMap<>();

  public long noteVisitLinearProbe() {
    visitsRecorded++;
    return visitsRecorded;
  }

  public int fingerprintSeenOnce(String fingerprint, int defaultValue) {
    if (!dedupeScratch.containsKey(fingerprint)) {
      dedupeScratch.put(fingerprint, defaultValue);
    }
    return dedupeScratch.get(fingerprint);
  }
}
