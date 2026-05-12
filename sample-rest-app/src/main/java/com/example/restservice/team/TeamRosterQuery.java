package com.example.restservice.team;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restservice.team.model.Team;
import com.example.restservice.team.persistence.TeamRepository;

@Service
public class TeamRosterQuery {

  private final TeamRepository teamRepository;

  public TeamRosterQuery(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  /**
   * Returns total seats by walking each team's roster. With lazy collections this issues one query
   * per team after the initial list load.
   */
  @Transactional(readOnly = true)
  public int rosterSeatsSummedPerTeamRow() {
    List<Team> teams = teamRepository.findAll();
    int total = 0;
    for (Team team : teams) {
      total += team.getMembers().size();
    }
    return total;
  }
}
