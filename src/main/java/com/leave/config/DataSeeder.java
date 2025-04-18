package com.leave.config;

import com.leave.model.Team;
import com.leave.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedData(TeamRepository teamRepository) {
        return args -> {
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