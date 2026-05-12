package com.example.restservice.ingestion.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.ingestion.SlabAccumulator;

@RestController
@RequestMapping("/v1/ingestion/probes")
public class IngestProbeController {

  private final SlabAccumulator slabAccumulator;

  public IngestProbeController(SlabAccumulator slabAccumulator) {
    this.slabAccumulator = slabAccumulator;
  }

  @GetMapping("/slab-sum")
  public SlabSumResponse slabSum(@RequestParam(defaultValue = "3") int passes) {
    long total = slabAccumulator.rollingSumWithFreshSlabs(passes);
    return new SlabSumResponse(total);
  }

  public record SlabSumResponse(long total) {}
}
