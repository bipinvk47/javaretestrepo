package com.example.restservice.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.restservice.team.model.Member;
import com.example.restservice.team.model.Team;
import com.example.restservice.team.persistence.TeamRepository;

@Component
public class SyntheticOrgSeeder implements CommandLineRunner {

  private final TeamRepository teamRepository;

  public SyntheticOrgSeeder(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  @Override
  public void run(String... args) {
    for (int t = 0; t < 5; t++) {
      Team team = new Team();
      team.setCode("TEAM-" + t);
      for (int m = 0; m < 8; m++) {
        Member member = new Member();
        member.setDisplayName(team.getCode() + "-M" + m);
        member.setTeam(team);
        team.getMembers().add(member);
      }
      teamRepository.save(team);
    }
  }
}
