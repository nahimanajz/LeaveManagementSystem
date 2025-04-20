package com.leave.config;

import com.leave.model.User;
import com.leave.model.Team;
import com.leave.repository.UserRepository;
import com.leave.repository.TeamRepository;
import com.leave.shared.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Configuration
@Profile("!prod")
@Order(2) // Run after TeamDataSeeder
public class UserDataSeeder {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            //check if there are any users in the database then create users
            if (userRepository.count() == 0) {
               
                List<Team> existingTeams = teamRepository.findAll();
                if (existingTeams.isEmpty()) {
                    throw new IllegalStateException(
                            "No teams found in database. Please ensure teams exist before seeding users.");
                }
            
            // Get existing teams from database

            Random random = new Random();

            // Admin
            User admin = new User();
            admin.setEmail("admin@ist.com");
            admin.setName("John doe");
            admin.setPosition("controls every thing");
            admin.setDepartment("Admin Department");
            admin.setStartDate(LocalDate.now());
            admin.setRole(UserRole.ADMIN);
            admin.setMicrosoftId("admin-ms-id");
            admin.setAvatarUrl("https://example.com/admin-avatar.jpg");
            admin.setActive(true);
            if (this.isEmailUnique(admin.getEmail())) {

                userRepository.save(admin);
            }

            // Managers
            for (int i = 1; i <= 2; i++) {
                User manager = new User();
                manager.setEmail("manager" + i + "@ist.com");
                manager.setName("Manager " + i);
                manager.setPosition("controls every thing");
                manager.setDepartment("Admin Department");
                manager.setStartDate(LocalDate.now());
                manager.setRole(UserRole.MANAGER);
                manager.setMicrosoftId("manager" + i + "-ms-id");
                manager.setAvatarUrl("https://example.com/manager" + i + "-avatar.jpg");
                manager.setActive(true);

                if (this.isEmailUnique(manager.getEmail())) {
                    userRepository.save(manager);
                }

            }

            // Staff - assign to existing teams
            for (int i = 1; i <= 20; i++) {
                User staff = new User();
                staff.setEmail("staff" + i + "@example.com");
                staff.setPosition("software engineer"+i);
                staff.setStartDate(LocalDate.now());
                staff.setName("Staff Member " + i);
                staff.setRole(UserRole.STAFF);
                staff.setMicrosoftId("staff" + i + "-ms-id");
                staff.setAvatarUrl("https://example.com/staff" + i + "-avatar.jpg");
                staff.setActive(true);

                // Get a random team from existing teams
                Team randomTeam = existingTeams.get(random.nextInt(existingTeams.size()));
                staff.setDepartment(randomTeam.getName());
                userRepository.save(staff);
            }
            }
        };
    }

    private boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }
}