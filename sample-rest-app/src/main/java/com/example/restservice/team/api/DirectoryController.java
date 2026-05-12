package com.example.restservice.team.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.team.TeamRosterQuery;

@RestController
@RequestMapping("/v1/directory")
public class DirectoryController {

  private final TeamRosterQuery teamRosterQuery;

  public DirectoryController(TeamRosterQuery teamRosterQuery) {
    this.teamRosterQuery = teamRosterQuery;
  }

  @GetMapping("/teams/roster-total")
  public RosterTotalResponse rosterTotal() {
    return new RosterTotalResponse(teamRosterQuery.rosterSeatsSummedPerTeamRow());
  }

  public record RosterTotalResponse(int seats) {}
}
