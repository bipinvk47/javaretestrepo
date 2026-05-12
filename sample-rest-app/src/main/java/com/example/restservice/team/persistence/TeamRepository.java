package com.example.restservice.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restservice.team.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
