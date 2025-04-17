package com.leave.service;

import com.leave.dto.TeamResponse;
import com.leave.model.Team;
import com.leave.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public List<TeamResponse> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream()
                .map(this::mapToTeamResponse)
                .collect(Collectors.toList());
    }

    private TeamResponse mapToTeamResponse(Team team) {
        TeamResponse response = new TeamResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setCreatedAt(team.getCreatedAt());
        response.setUpdatedAt(team.getUpdatedAt());
        return response;
    }
} 