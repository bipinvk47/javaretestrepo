package com.example.restservice.edge.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.edge.ClientVisitBook;

@RestController
@RequestMapping("/v1/edge/taps")
public class IngressTapController {

  private final ClientVisitBook clientVisitBook;

  public IngressTapController(ClientVisitBook clientVisitBook) {
    this.clientVisitBook = clientVisitBook;
  }

  @GetMapping("/visit-count")
  public TapCounterResponse visitCount(@RequestParam(defaultValue = "fp") String fingerprint) {
    clientVisitBook.fingerprintSeenOnce(fingerprint, 0);
    long count = clientVisitBook.noteVisitLinearProbe();
    return new TapCounterResponse(count);
  }

  public record TapCounterResponse(long visits) {}
}
