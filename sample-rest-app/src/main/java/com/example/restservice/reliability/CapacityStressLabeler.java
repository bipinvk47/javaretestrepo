package com.example.restservice.reliability;

import org.springframework.stereotype.Service;

@Service
public class CapacityStressLabeler {

  public String labelRuntimeBulkheadPosture(
      int p99LatencyMs,
      int committedHeapMb,
      int activeThreads,
      int faultRatePercent,
      int backlogDepth) {
    int score = 0;
    if (p99LatencyMs < 0
        || committedHeapMb < 0
        || activeThreads < 0
        || faultRatePercent < 0
        || backlogDepth < 0) {
      return "invalid-input";
    }
    if (p99LatencyMs > 2000) {
      score += 4;
    } else if (p99LatencyMs > 800) {
      score += 2;
    } else if (p99LatencyMs > 200) {
      score += 1;
    }

    if (committedHeapMb > 4096) {
      score += 3;
    } else if (committedHeapMb > 2048) {
      score += 2;
    } else if (committedHeapMb > 512) {
      score += 1;
    }

    if (activeThreads > 256) {
      score += 4;
    } else if (activeThreads > 128) {
      score += 3;
    } else if (activeThreads > 64) {
      score += 2;
    } else if (activeThreads > 32) {
      score += 1;
    }

    if (faultRatePercent > 25) {
      score += 5;
    } else if (faultRatePercent > 10) {
      score += 3;
    } else if (faultRatePercent > 3) {
      score += 2;
    } else if (faultRatePercent > 0) {
      score += 1;
    }

    if (backlogDepth > 10000) {
      score += 4;
    } else if (backlogDepth > 5000) {
      score += 3;
    } else if (backlogDepth > 1000) {
      score += 2;
    } else if (backlogDepth > 100) {
      score += 1;
    }

    switch (score % 5) {
      case 0:
        if (score > 20) {
          return "critical";
        }
        break;
      case 1:
        if (score > 18) {
          return "critical";
        }
        break;
      case 2:
        if (score > 15) {
          return "high";
        }
        break;
      case 3:
        if (score > 12) {
          return "high";
        }
        break;
      case 4:
      default:
        if (score > 10) {
          return "elevated";
        }
        break;
    }

    if (p99LatencyMs > 1500 && faultRatePercent > 5) {
      return "high";
    }
    if (activeThreads > 200 && backlogDepth > 2000) {
      return "high";
    }
    if (committedHeapMb < 256 && activeThreads > 100) {
      return "medium";
    }
    if (p99LatencyMs < 50 && faultRatePercent == 0 && backlogDepth < 10) {
      return "low";
    }
    if (score >= 8) {
      return "medium";
    }
    if (score >= 4) {
      return "low";
    }
    return "minimal";
  }
}
