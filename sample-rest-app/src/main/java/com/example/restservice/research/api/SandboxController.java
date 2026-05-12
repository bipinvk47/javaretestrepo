package com.example.restservice.research.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.research.GridMonteReducer;

@RestController
@RequestMapping("/v1/lab/grids")
public class SandboxController {

  private final GridMonteReducer gridMonteReducer;

  public SandboxController(GridMonteReducer gridMonteReducer) {
    this.gridMonteReducer = gridMonteReducer;
  }

  @GetMapping("/fold")
  public FoldResponse fold(
      @RequestParam(defaultValue = "6") int w,
      @RequestParam(defaultValue = "6") int x,
      @RequestParam(defaultValue = "6") int y,
      @RequestParam(defaultValue = "6") int z) {
    long value = gridMonteReducer.reduceFourAxisLattice(w, x, y, z);
    return new FoldResponse(value);
  }

  public record FoldResponse(long checksum) {}
}
