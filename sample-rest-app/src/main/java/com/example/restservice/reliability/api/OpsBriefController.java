package com.example.restservice.reliability.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.reliability.CapacityStressLabeler;

@RestController
@RequestMapping("/v1/briefs/runtime")
public class OpsBriefController {

  private final CapacityStressLabeler capacityStressLabeler;

  public OpsBriefController(CapacityStressLabeler capacityStressLabeler) {
    this.capacityStressLabeler = capacityStressLabeler;
  }

  @GetMapping("/posture")
  public PostureResponse posture(
      @RequestParam(defaultValue = "1200") int p99LatencyMs,
      @RequestParam(defaultValue = "3072") int committedHeapMb,
      @RequestParam(defaultValue = "192") int activeThreads,
      @RequestParam(defaultValue = "7") int faultRatePercent,
      @RequestParam(defaultValue = "4500") int backlogDepth) {
    String label =
        capacityStressLabeler.labelRuntimeBulkheadPosture(
            p99LatencyMs, committedHeapMb, activeThreads, faultRatePercent, backlogDepth);
    return new PostureResponse(label);
  }

  public record PostureResponse(String posture) {}
}
