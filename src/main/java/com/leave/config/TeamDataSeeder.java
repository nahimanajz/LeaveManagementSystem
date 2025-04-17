package com.leave.config;

import com.leave.model.Team;
import com.leave.repository.TeamRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class TeamDataSeeder {

    @Bean
    public CommandLineRunner seedTeams(TeamRepository teamRepository) {
        return args -> {
            //check if there are any teams in the database
            if (teamRepository.count() == 0) {
                String[] teamNames = {
                    "Engineering",
                    "Marketing",
                    "Sales",
                    "Human Resources",
                    "Finance",
                    "Operations",
                    "Customer Support",
                    "Product Management",
                    "Quality Assurance",
                    "Research and Development"
                };

                for (String name : teamNames) {
                    Team team = new Team();
                    team.setName(name);
                    teamRepository.save(team);
                }
            }

        };
    }
} 